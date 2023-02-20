package net.qixiaowei.integration.common.enums.field.strategy;

public enum PlanBusinessUnitField {

    BUSINESS_UNIT_CODE("business_unit_code", "规划业务单元编码"),
    BUSINESS_UNIT_NAME("business_unit_name", "规划业务单元名称"),
    BUSINESS_UNIT_DECOMPOSE("business_unit_name", "规划业务单元维度(region,department,product,industry)"),
    STATUS("status", "状态:0失效;1生效"),

    ;

    private final String code;
    private final String info;

    PlanBusinessUnitField(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

}
