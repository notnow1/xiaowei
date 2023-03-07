package net.qixiaowei.integration.common.enums.field.strategyDecode;


public enum StrategyMetricsPlanField {
                STRATEGY_METRICS_PLAN_ID("strategy_metrics_plan_id", "ID"),
                        STRATEGY_METRICS_ID("strategy_metrics_id", "战略衡量指标ID"),
                        STRATEGY_METRICS_DETAIL_ID("strategy_metrics_detail_id", "战略衡量指标详情ID"),
                        PLAN_YEAR("plan_year", "规划年度"),
                        PLAN_VALUE("plan_value", "规划值"),
        ;
        private final String code;
        private final String info;
        StrategyMetricsPlanField(String code, String info) {
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

