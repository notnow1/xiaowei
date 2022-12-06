package net.qixiaowei.integration.tenant.utils;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @description sql字段拼接工具
 * @Author hzk
 * @Date 2022-11-29 20:19
 **/
@Component
public class SqlFieldHelper {

    private static final String CREATE_BY = "create_by";

    private static final String UPDATE_BY = "update_by";

    private static final String CREATE_TIME = "create_time";

    private static final String UPDATE_TIME = "update_time";

    private static final String DELETE_FLAG = "delete_flag";


    /**
     * 为sql语句添加指定字段
     *
     * @param sqlStatement
     * @param fieldName
     * @param fieldValue
     */
    public void addStatementField(SQLStatement sqlStatement, String fieldName, Long fieldValue) {
        if (sqlStatement instanceof SQLInsertStatement) {
            SQLInsertStatement insertStatement = (SQLInsertStatement) sqlStatement;
            addInsertStatementField(insertStatement, fieldName, fieldValue);
        } else if (sqlStatement instanceof SQLUpdateStatement) {
            SQLUpdateStatement updateStatement = (SQLUpdateStatement) sqlStatement;
            addUpdateStatementField(updateStatement, fieldName, fieldValue);
        }
    }

    /**
     * 为insert语句添加 字段
     *
     * @param insertStatement
     * @param fieldName
     * @param fieldValue
     */
    private void addInsertStatementField(SQLInsertStatement insertStatement, String fieldName, Long fieldValue) {
        String tableName = insertStatement.getTableName().getSimpleName().replace("`", "");
        List<SQLExpr> columns = insertStatement.getColumns();
        //list是存在批量插入的情况
        List<SQLInsertStatement.ValuesClause> valuesList = insertStatement.getValuesList();
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        this.addInsertItemField(columns, valuesList, CREATE_BY, userId);
        this.addInsertItemField(columns, valuesList, CREATE_TIME, nowDate);
        this.addInsertItemField(columns, valuesList, UPDATE_BY, userId);
        this.addInsertItemField(columns, valuesList, UPDATE_TIME, nowDate);
        this.addInsertItemField(columns, valuesList, DELETE_FLAG, DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        if (!TenantDatabaseUtil.isIgnore(tableName)) {
            //多租户字段
            this.addInsertItemField(columns, valuesList, fieldName, fieldValue);
        }
    }


    private void addInsertItemField(List<SQLExpr> columns, List<SQLInsertStatement.ValuesClause> valuesList, String fieldName, Object fieldValue) {
        if (fieldName == null || fieldValue == null) {
            return;
        }
        for (int i = 0; i < columns.size(); i++) {
            SQLIdentifierExpr column = (SQLIdentifierExpr) columns.get(i);
            //如果字段名字匹配
            if (column.getName().equals(fieldName)) {
                for (SQLInsertStatement.ValuesClause valuesClause : valuesList) {
                    List<SQLExpr> values = valuesClause.getValues();
                    SQLExpr valExpr = values.get(i);
                    //如果值是空的就设置
                    if (valExpr instanceof SQLNullExpr) {
                        values.set(i, getValuableExpr(fieldValue));
                    }
                }
                return;
            }
        }
        //如果没有匹配， 加入这个字段
        columns.add(new SQLIdentifierExpr(fieldName));
        for (SQLInsertStatement.ValuesClause valuesClause : valuesList) {
            List<SQLExpr> values = valuesClause.getValues();
            values.add(getValuableExpr(fieldValue));
        }
    }


    /**
     * 为update语句添加 字段
     *
     * @param updateStatement
     * @param fieldName
     * @param fieldValue
     */
    private void addUpdateStatementField(SQLUpdateStatement updateStatement, String fieldName, Object fieldValue) {
        String tableName = updateStatement.getTableName().getSimpleName().replace("`", "");
        List<SQLUpdateSetItem> items = updateStatement.getItems();
        addUpdateItemField(items, UPDATE_BY, SecurityUtils.getUserId());
        addUpdateItemField(items, UPDATE_TIME, DateUtils.getNowDate());
        if (!TenantDatabaseUtil.isIgnore(tableName)) {
            addUpdateItemField(items, fieldName, fieldValue);
        }
    }

    private void addUpdateItemField(List<SQLUpdateSetItem> items, String fieldName, Object fieldValue) {
        if (fieldName == null || fieldValue == null) {
            return;
        }
        for (SQLUpdateSetItem item : items) {
//            if (((SQLIdentifierExpr)item.getColumn()).getName().equals(fieldName) && item.getValue() instanceof SQLNullExpr) {
//                item.setValue(getValuableExpr(fieldValue));
//                return;
//            }
            if (((SQLIdentifierExpr) item.getColumn()).getName().equals(fieldName)) {
                return;
            }
        }

        //如果没有匹配， 加入这个字段
        SQLUpdateSetItem sqlUpdateSetItem = new SQLUpdateSetItem();
        sqlUpdateSetItem.setColumn(new SQLIdentifierExpr(fieldName));
        sqlUpdateSetItem.setValue(getValuableExpr(fieldValue));
        items.add(sqlUpdateSetItem);
    }


    /**
     * 封装SQLValuableExpr
     *
     * @param val
     * @return
     */
    private SQLValuableExpr getValuableExpr(Object val) {
        if (val == null) {
            return new SQLNullExpr();
        } else if (val instanceof Number) {
            return new SQLNumberExpr((Number) val);
        } else if (val instanceof Date) {
            return new SQLTimestampExpr((Date) val);
        } else if (val instanceof String) {
            return new SQLCharExpr((String) val);
        } else {
            return new SQLCharExpr(val.toString());
        }
    }

}

