package net.qixiaowei.integration.common.enums.field.strategyDecode;


public enum AnnualKeyWorkField {
                ANNUAL_KEY_WORK_ID("annual_key_work_id", "ID"),
                        PLAN_YEAR("plan_year", "规划年度"),
                        PLAN_BUSINESS_UNIT_ID("plan_business_unit_id", "规划业务单元ID"),
                        BUSINESS_UNIT_DECOMPOSE("business_unit_decompose", "规划业务单元维度(region,department,product,industry)"),
                        AREA_ID("area_id", "区域ID"),
                        DEPARTMENT_ID("department_id", "部门ID"),
                        PRODUCT_ID("product_id", "产品ID"),
                        INDUSTRY_ID("industry_id", "行业ID"),
                        PLAN_RANK("plan_rank", "规划级别:1部门;2公司"),
        ;
        private final String code;
        private final String info;
        AnnualKeyWorkField(String code, String info) {
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

