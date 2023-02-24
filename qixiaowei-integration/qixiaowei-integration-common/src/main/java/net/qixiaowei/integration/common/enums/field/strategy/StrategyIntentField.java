package net.qixiaowei.integration.common.enums.field.strategy;


public enum StrategyIntentField {
                PLAN_YEAR("plan_year", "规划年度"),
                OPERATE_PLAN_PERIOD("operate_plan_period", "经营规划期"),
                OPERATE_HISTORY_YEAR("operate_history_year", "经营历史年份"),
                CREATE_BY("create_by", "创建人名称"),
                CREATE_TIME("create_time", "创建时间"),
    ;
    private final String code;
    private final String info;

    StrategyIntentField(String code, String info) {
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

