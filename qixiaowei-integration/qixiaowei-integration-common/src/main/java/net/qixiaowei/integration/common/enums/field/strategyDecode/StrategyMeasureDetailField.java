package net.qixiaowei.integration.common.enums.field.strategyDecode;


public enum StrategyMeasureDetailField {
                STRATEGY_MEASURE_DETAIL_ID("strategy_measure_detail_id", "ID"),
                        STRATEGY_MEASURE_ID("strategy_measure_id", "战略举措清单ID"),
                        STRATEGY_INDEX_DIMENSION_ID("strategy_index_dimension_id", "战略指标维度ID"),
                        SERIAL_NUMBER("serial_number", "序列号"),
                        STRATEGY_MEASURE_NAME("strategy_measure_name", "战略举措名称"),
                        STRATEGY_MEASURE_SOURCE("strategy_measure_source", "战略举措来源"),
                        SORT("sort", "排序"),
        ;
        private final String code;
        private final String info;
        StrategyMeasureDetailField(String code, String info) {
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

