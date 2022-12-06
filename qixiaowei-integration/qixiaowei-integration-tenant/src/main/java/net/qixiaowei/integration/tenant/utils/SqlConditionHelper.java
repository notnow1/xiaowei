package net.qixiaowei.integration.tenant.utils;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.tenant.domain.SqlCondition;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

/**
 * @description sql条件拼接工具
 * @Author hzk
 * @Date 2022-11-29 19:19
 **/
@Component
public class SqlConditionHelper {


    /**
     * 为sql语句添加指定where条件
     *
     * @param sqlStatement
     * @param sqlCondition
     */
    public void addStatementCondition(SQLStatement sqlStatement, SqlCondition sqlCondition) {
        if (sqlStatement instanceof SQLSelectStatement) {
            SQLSelectQueryBlock queryObject = (SQLSelectQueryBlock) ((SQLSelectStatement) sqlStatement).getSelect().getQuery();
            addSelectStatementCondition(queryObject, queryObject.getFrom(), sqlCondition);
        } else if (sqlStatement instanceof SQLUpdateStatement) {
            SQLUpdateStatement updateStatement = (SQLUpdateStatement) sqlStatement;
            addUpdateStatementCondition(updateStatement, sqlCondition);
        } else if (sqlStatement instanceof SQLDeleteStatement) {
            SQLDeleteStatement deleteStatement = (SQLDeleteStatement) sqlStatement;
            addDeleteStatementCondition(deleteStatement, sqlCondition);
        } else if (sqlStatement instanceof SQLInsertStatement) {
            SQLInsertStatement insertStatement = (SQLInsertStatement) sqlStatement;
            addInsertStatementCondition(insertStatement, sqlCondition);
        }
    }

    /**
     * 为insert语句添加where条件
     *
     * @param insertStatement
     * @param sqlCondition
     */
    private void addInsertStatementCondition(SQLInsertStatement insertStatement, SqlCondition sqlCondition) {
        if (insertStatement != null) {
            SQLSelect sqlSelect = insertStatement.getQuery();
            if (sqlSelect != null) {
                SQLSelectQueryBlock selectQueryBlock = (SQLSelectQueryBlock) sqlSelect.getQuery();
                addSelectStatementCondition(selectQueryBlock, selectQueryBlock.getFrom(), sqlCondition);
            }
        }
    }


    /**
     * 为delete语句添加where条件
     *
     * @param deleteStatement
     * @param sqlCondition
     */
    private void addDeleteStatementCondition(SQLDeleteStatement deleteStatement, SqlCondition sqlCondition) {
        SQLExpr where = deleteStatement.getWhere();
        //添加子查询中的where条件
        addSQLExprCondition(where, sqlCondition);

        SQLExpr newCondition = newEqualityCondition(deleteStatement.getTableName().getSimpleName(), deleteStatement.getTableSource().getAlias(), sqlCondition, where);
        deleteStatement.setWhere(newCondition);
    }

    /**
     * where中添加指定筛选条件
     *
     * @param where        源where条件
     * @param sqlCondition
     */
    private void addSQLExprCondition(SQLExpr where, SqlCondition sqlCondition) {
        if (where instanceof SQLInSubQueryExpr) {
            SQLInSubQueryExpr inWhere = (SQLInSubQueryExpr) where;
            SQLSelect subSelectObject = inWhere.getSubQuery();
            SQLSelectQueryBlock subQueryObject = (SQLSelectQueryBlock) subSelectObject.getQuery();
            addSelectStatementCondition(subQueryObject, subQueryObject.getFrom(), sqlCondition);
        } else if (where instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr opExpr = (SQLBinaryOpExpr) where;
            SQLExpr left = opExpr.getLeft();
            SQLExpr right = opExpr.getRight();
            addSQLExprCondition(left, sqlCondition);
            addSQLExprCondition(right, sqlCondition);
        } else if (where instanceof SQLQueryExpr) {
            SQLSelectQueryBlock selectQueryBlock = (SQLSelectQueryBlock) (((SQLQueryExpr) where).getSubQuery()).getQuery();
            addSelectStatementCondition(selectQueryBlock, selectQueryBlock.getFrom(), sqlCondition);
        }
    }

    /**
     * 为update语句添加where条件
     *
     * @param updateStatement
     * @param sqlCondition
     */
    private void addUpdateStatementCondition(SQLUpdateStatement updateStatement, SqlCondition sqlCondition) {
        SQLExpr where = updateStatement.getWhere();
        //添加子查询中的where条件
        addSQLExprCondition(where, sqlCondition);
        SQLExpr newCondition = newEqualityCondition(updateStatement.getTableName().getSimpleName(), updateStatement.getTableSource().getAlias(), sqlCondition, where);
        updateStatement.setWhere(newCondition);
    }

    /**
     * 给一个查询对象添加一个where条件
     *
     * @param queryObject
     * @param sqlCondition
     */
    private void addSelectStatementCondition(SQLSelectQueryBlock queryObject, SQLTableSource from, SqlCondition sqlCondition) {
        if (from == null || queryObject == null) {
            return;
        }
        SQLExpr originCondition = queryObject.getWhere();
        //添加子查询中的where条件
        addSQLExprCondition(originCondition, sqlCondition);
        if (from instanceof SQLExprTableSource) {
            //普通情况
            String tableName = ((SQLIdentifierExpr) ((SQLExprTableSource) from).getExpr()).getName();
            String alias = from.getAlias();
            SQLExpr newCondition = newEqualityCondition(tableName, alias, sqlCondition, originCondition);
            //设置最后的条件
            queryObject.setWhere(newCondition);
        } else if (from instanceof SQLJoinTableSource) {
            //表关联，需要判断是否是union all情况
            SQLJoinTableSource joinObject = (SQLJoinTableSource) from;
            SQLTableSource left = joinObject.getLeft();
            SQLTableSource right = joinObject.getRight();
            SQLExpr onExpr = joinObject.getCondition();
            addSelectStatementCondition(queryObject, left, sqlCondition);
//            addSelectStatementCondition(queryObject, right, sqlCondition);
//            if (left instanceof SQLSubqueryTableSource) {
//                SQLSelect subSelectObject = ((SQLSubqueryTableSource) left).getSelect();
//                SQLSelectQueryBlock subQueryObject = (SQLSelectQueryBlock) subSelectObject.getQuery();
//                parseUnion(subQueryObject, sqlCondition);
//                addSelectStatementCondition(subQueryObject, subQueryObject.getFrom(), sqlCondition);
//            } else if (left instanceof SQLExprTableSource) {
//                String tableName = ((SQLIdentifierExpr) ((SQLExprTableSource) left).getExpr()).getName();
//                String alias = left.getAlias();
//                onExpr = newEqualityCondition(tableName, alias, sqlCondition, originCondition);
//                joinObject.setCondition(onExpr);
//            }
            if (right instanceof SQLSubqueryTableSource) {
                SQLSelect subSelectObject = ((SQLSubqueryTableSource) right).getSelect();
                SQLSelectQueryBlock subQueryObject = (SQLSelectQueryBlock) subSelectObject.getQuery();
                parseUnion(subQueryObject, sqlCondition);
                addSelectStatementCondition(subQueryObject, subQueryObject.getFrom(), sqlCondition);
            } else if (right instanceof SQLExprTableSource) {
                String tableName = ((SQLIdentifierExpr) ((SQLExprTableSource) right).getExpr()).getName();
                String alias = right.getAlias();
                onExpr = newEqualityCondition(tableName, alias, sqlCondition, onExpr);
            }
            joinObject.setCondition(onExpr);
        } else if (from instanceof SQLSubqueryTableSource) {
            //嵌套子查询
            SQLSelect subSelectObject = ((SQLSubqueryTableSource) from).getSelect();
            SQLSelectQueryBlock subQueryObject = (SQLSelectQueryBlock) subSelectObject.getQuery();
            addSelectStatementCondition(subQueryObject, subQueryObject.getFrom(), sqlCondition);
        } else if (from instanceof SQLUnionQueryTableSource) {
            SQLUnionQueryTableSource union = (SQLUnionQueryTableSource) from;
            SQLUnionQuery sqlUnionQuery = union.getUnion();
            //这里判断查询类型
            addSelectStatementConditionUnion(queryObject, sqlUnionQuery, sqlCondition);

        } else {
            throw new NotImplementedException("未处理的异常");
        }
    }


    /**
     * 拼接union查询的租户字段
     *
     * @param queryObject
     * @param sqlUnionQuery
     * @param sqlCondition
     */
    private void addSelectStatementConditionUnion(SQLSelectQueryBlock queryObject, SQLUnionQuery sqlUnionQuery, SqlCondition sqlCondition) {
        if (sqlUnionQuery.getLeft() instanceof SQLUnionQuery) {
            SQLUnionQuery temQuery = (SQLUnionQuery) sqlUnionQuery.getLeft();
            addSelectStatementConditionUnion(queryObject, temQuery, sqlCondition);
        }
        if (sqlUnionQuery.getLeft() instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock left = (SQLSelectQueryBlock) sqlUnionQuery.getLeft();
            addSelectStatementCondition(left, left.getFrom(), sqlCondition);
        }
        if (sqlUnionQuery.getRight() instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock right = (SQLSelectQueryBlock) sqlUnionQuery.getRight();
            addSelectStatementCondition(right, right.getFrom(), sqlCondition);
        }

    }

    /**
     * 如果是union all的情况,则通过递归进入内层
     *
     * @param query
     * @param sqlCondition
     */
    private void parseUnion(final SQLSelectQuery query, SqlCondition sqlCondition) {
        if (query instanceof SQLUnionQuery) {
            SQLUnionQuery unionQuery = (SQLUnionQuery) query;
            if (unionQuery.getLeft() instanceof SQLUnionQuery) {
                parseUnion(unionQuery.getLeft(), sqlCondition);
            } else if (unionQuery.getLeft() instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) unionQuery.getLeft();
                addSelectStatementCondition(queryBlock, queryBlock.getFrom(), sqlCondition);
            }
            if (unionQuery.getRight() instanceof SQLUnionQuery) {
                parseUnion(unionQuery.getRight(), sqlCondition);
            } else if (unionQuery.getRight() instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) unionQuery.getRight();
                addSelectStatementCondition(queryBlock, queryBlock.getFrom(), sqlCondition);
            }
        }
    }

    /**
     * 根据原来的condition创建一个新的condition
     *
     * @param tableName       表名称
     * @param tableAlias      表别名 字段名
     * @param sqlCondition    字段值
     * @param originCondition 原始条件
     * @return
     */
    private SQLExpr newEqualityCondition(String tableName, String tableAlias, SqlCondition sqlCondition, SQLExpr originCondition) {
        String fieldName = sqlCondition.getFieldName();
        Long fieldValue = sqlCondition.getFieldValue();
        //如果不需要判断租户
        if (TenantDatabaseUtil.isIgnore(tableName)) {
            return originCondition;
        }
        String filedName = StringUtils.isBlank(tableAlias) ? fieldName : tableAlias + "." + fieldName;
        SQLIdentifierExpr sqlIdentifierExpr = new SQLIdentifierExpr(filedName);
        SQLIntegerExpr sqlIntegerExpr = new SQLIntegerExpr(fieldValue);
        SQLExpr condition = new SQLBinaryOpExpr(sqlIdentifierExpr, sqlIntegerExpr, SQLBinaryOperator.Equality);
        return SQLUtils.buildCondition(SQLBinaryOperator.BooleanAnd, condition, false, originCondition);
    }

}

