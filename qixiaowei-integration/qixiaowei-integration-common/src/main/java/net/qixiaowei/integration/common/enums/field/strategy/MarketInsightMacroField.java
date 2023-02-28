package net.qixiaowei.integration.common.enums.field.strategy;


public enum MarketInsightMacroField {
                    PLAN_YEAR("plan_year", "规划年度"),
                    PLAN_BUSINESS_UNIT_ID("plan_business_unit_id", "规划业务单元ID"),
                    AREA_ID("area_id", "区域ID"),
                    DEPARTMENT_ID("department_id", "部门ID"),
                    PRODUCT_ID("product_id", "产品ID"),
                    INDUSTRY_ID("industry_id", "行业ID"),
    ;
    private final String code;
    private final String info;

    MarketInsightMacroField(String code, String info) {
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

