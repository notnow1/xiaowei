package net.qixiaowei.integration.tenant.db;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.tenant.context.TenantContextHolder;
import net.qixiaowei.integration.tenant.domain.SqlCondition;
import net.qixiaowei.integration.tenant.utils.SqlConditionHelper;
import net.qixiaowei.integration.tenant.utils.SqlFieldHelper;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 基于 MyBatis实现 DB 层面的多租户的功能
 *
 * @author hzk
 */
@Slf4j
@Intercepts({@Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
@Component
public class TenantDatabaseInterceptor implements Interceptor {

    /**
     * 租户字段名
     */
    private static final String TENANT_ID_COLUMN = "tenant_id";

    //新增，编辑 时候字段赋值
    @Autowired
    private SqlFieldHelper sqlFieldHelper;
    //sql条件生成
    @Autowired
    private SqlConditionHelper conditionHelper;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Long tenantId = SecurityUtils.getTenantId();
        //@Signature 上面拦截的方法 的参数
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        //这个巨重要 不然会 新sql成功生成， 但mybatis运行的是旧sql
        BoundSql boundSql;
        if (args.length == 6) {
            // 6 个参数时
            boundSql = (BoundSql) args[5];
        } else {
            boundSql = ms.getBoundSql(parameter);
        }
        String processSql = boundSql.getSql();
        log.debug("替换前SQL：{}", processSql);
        //语法分析生成新sql
        String newSql = getNewSql(processSql, tenantId);
        log.debug("替换后SQL：{}", newSql);
        //通过反射修改sql字段
        MetaObject boundSqlMeta = MetaObject.forObject(boundSql, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
        // 把新sql设置到boundSql
        boundSqlMeta.setValue("sql", newSql);
        // 重新new一个查询语句对象
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), newSql,
                boundSql.getParameterMappings(), boundSql.getParameterObject());
        // 把新的查询放到statement里
        MappedStatement newMs = newMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        args[0] = newMs;

        return invocation.proceed();
    }

    /**
     * 定义一个内部辅助类，作用是包装 SQL
     */
    static class BoundSqlSqlSource implements SqlSource {

        private final BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }

    }


    /**
     * 根据原MappedStatement更新SqlSource生成新MappedStatement
     *
     * @param ms           MappedStatement
     * @param newSqlSource 新SqlSource
     * @return
     */
    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new
                MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }


    /**
     * 给sql语句where添加租户id过滤条件
     *
     * @param sql              要添加过滤条件的sql语句
     * @param tenantFieldValue 当前的租户id
     * @return 添加条件后的sql语句
     */
    private String getNewSql(String sql, Long tenantFieldValue) {
        List<SQLStatement> statementList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        if (statementList.size() == 0) {
            return sql;
        }
        SQLStatement sqlStatement = statementList.get(0);
        //新增，修改 字段赋值 (创建人 时间，修改人 时间，多租户字段)
        sqlFieldHelper.addStatementField(sqlStatement, TENANT_ID_COLUMN, tenantFieldValue);
        //查询、修改、删除  where条件添加多租户
        conditionHelper.addStatementCondition(sqlStatement, new SqlCondition(TENANT_ID_COLUMN, tenantFieldValue));
        String newSql = SQLUtils.toSQLString(statementList, JdbcConstants.MYSQL);
        //去掉自动加上去的 \
        return newSql.replaceAll("\\\\", "");
    }


}
