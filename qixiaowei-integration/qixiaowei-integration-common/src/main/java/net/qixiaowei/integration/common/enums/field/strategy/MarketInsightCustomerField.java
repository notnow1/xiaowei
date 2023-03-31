package net.qixiaowei.integration.common.enums.field.strategy;


public enum MarketInsightCustomerField {
                        PLAN_YEAR("plan_year", "规划年度"),
                        PLAN_BUSINESS_UNIT_ID("plan_business_unit_id", "规划业务单元名称"),
                        AREA_ID("area_id", "区域"),
                        DEPARTMENT_ID("department_id", "部门"),
                        PRODUCT_ID("product_id", "产品"),
                        INDUSTRY_ID("industry_id", "行业"),
        ;
        private final String code;
        private final String info;
        MarketInsightCustomerField(String code, String info) {
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

