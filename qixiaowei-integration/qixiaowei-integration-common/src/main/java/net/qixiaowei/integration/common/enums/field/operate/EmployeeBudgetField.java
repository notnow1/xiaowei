package net.qixiaowei.integration.common.enums.field.operate;

public enum EmployeeBudgetField {
    BUDGET_YEAR("budget_year", "预算年度"),
    DEPARTMENT_ID("department_id", "预算部门"),
    OFFICIAL_RANK_SYSTEM_ID("official_rank_system_id", "职级体系"),
    AMOUNT_LAST_YEAR("amount_last_year", "上年期末人数"),
    AMOUNT_ADJUST("amount_adjust", "本年新增人数"),
    AMOUNT_AVERAGE_ADJUST("amount_average_adjust", "平均新增人数"),
    ANNUAL_AVERAGE_NUM("annual_average_num", "年度平均人数"),
    BUDGET_CYCLE("budget_cycle", "预算周期"),

    ;

    private final String code;
    private final String info;

    EmployeeBudgetField(String code, String info) {
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
