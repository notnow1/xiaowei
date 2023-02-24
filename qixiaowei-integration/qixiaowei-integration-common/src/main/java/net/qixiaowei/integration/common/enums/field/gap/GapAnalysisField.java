package net.qixiaowei.integration.common.enums.field.gap;


public enum GapAnalysisField {
    GAP_ANALYSIS_ID("gap_analysis_id", "ID"),
    PLAN_YEAR("plan_year", "规划年度"),
    PLAN_BUSINESS_UNIT_ID("plan_business_unit_id", "规划业务单元ID"),
    BUSINESS_UNIT_DECOMPOSE("business_unit_decompose", "规划业务单元维度(region,department,product,industry)"),
    AREA_ID("area_id", "区域"),
    DEPARTMENT_ID("department_id", "部门ID"),
    PRODUCT_ID("product_id", "产品ID"),
    INDUSTRY_ID("industry_id", "行业ID"),
    OPERATE_HISTORY_YEAR("operate_history_year", "经营历史年份"),
    ;
    private final String code;
    private final String info;

    GapAnalysisField(String code, String info) {
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

