package net.qixiaowei.integration.common.enums.field.operate;

public enum DeptBonusBudgetField {

    BUDGET_YEAR("budget_year", "预算年度"),
    AMOUNT_BONUS_BUDGET("amount_bonus_budget", "总奖金包预算"),
    STRATEGY_AWARD_AMOUNT("strategy_award_amount", "战略奖预算"),
    STRATEGY_AWARD_PERCENTAGE("strategy_award_percentage", "战略奖占比（%）"),
    DEPT_AMOUNT_BONUS("dept_amount_bonus", "部门奖金包"),

    ;

    private final String code;
    private final String info;

    DeptBonusBudgetField(String code, String info) {
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
