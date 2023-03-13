package net.qixiaowei.integration.common.enums.field.strategy;


public enum MarketInsightOpponentField {
                        PLAN_YEAR("plan_year", "规划年度"),
                        PLAN_BUSINESS_UNIT_ID("plan_business_unit_id", "规划业务单元名称"),
                        AREA_ID("area_id", "区域名称"),
                        DEPARTMENT_ID("department_id", "部门名称"),
                        PRODUCT_ID("product_id", "产品名称"),
                        INDUSTRY_ID("industry_id", "行业名称"),
        ;
        private final String code;
        private final String info;
        MarketInsightOpponentField(String code, String info) {
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

