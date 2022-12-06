package net.qixiaowei.integration.tenant.domain;


/**
 * @description 租户字段条件对象
 * @Author hzk
 * @Date 2022-11-29 20:22
 **/
public class SqlCondition {

    private String fieldName;

    private Long fieldValue;

    public SqlCondition(String fieldName, Long fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Long getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Long fieldValue) {
        this.fieldValue = fieldValue;
    }
}
