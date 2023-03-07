package net.qixiaowei.integration.common.enums.field.strategyDecode;


public enum StrategyMetricsDetailField {
                STRATEGY_METRICS_DETAIL_ID("strategy_metrics_detail_id", "ID"),
                        STRATEGY_METRICS_ID("strategy_metrics_id", "战略衡量指标ID"),
                        STRATEGY_INDEX_DIMENSION_ID("strategy_index_dimension_id", "战略指标维度ID"),
                        SERIAL_NUMBER("serial_number", "序列号"),
                        STRATEGY_MEASURE_NAME("strategy_measure_name", "战略举措名称"),
                        INDICATOR_ID("indicator_id", "指标ID"),
                        SORT("sort", "排序"),
        ;
        private final String code;
        private final String info;
        StrategyMetricsDetailField(String code, String info) {
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

