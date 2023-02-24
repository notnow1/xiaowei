package net.qixiaowei.integration.common.enums.field.operate;

public enum BonusBudgetField {

    BUDGET_YEAR("budget_year", "预算年度"),
    AMOUNT_BONUS_BUDGET("amount_bonus_budget", "总奖金包预算"),
    RAISE_SALARY_BONUS_BUDGET("raise_salary_bonus_budget", "涨薪包预算"),
    PAYMENT_BONUS_BUDGET("payment_bonus_budget", "总薪酬包预算"),
    AMOUNT_WAGE_BUDGET("amount_wage_budget", "总工资包预算"),
    ELASTICITY_BONUS_BUDGET("elasticity_bonus_budget", "弹性薪酬包预算"),

    ;

    private final String code;
    private final String info;

    BonusBudgetField(String code, String info) {
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
