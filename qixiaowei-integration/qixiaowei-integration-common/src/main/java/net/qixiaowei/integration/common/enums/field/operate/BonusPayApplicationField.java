package net.qixiaowei.integration.common.enums.field.operate;

public enum BonusPayApplicationField {

    AWARD_NAME("award_name", "奖项名称"),
    AWARD_CODE("award_code", "奖项编码"),
    SALARY_ITEM_ID("salary_item_id", "奖项类别"),
    AWARD_YEAR_MONTH("award_year_month", "获奖时间"),
    APPLY_DEPARTMENT_ID("apply_department_id", "申请部门"),
    BUDGET_DEPARTMENT_LIST("budget_department_list", "预算部门"),
    AWARD_TOTAL_AMOUNT("award_total_amount", "奖金总额"),
    BONUS_PAY_OBJECT("bonus_pay_object", "发放对象"),
    CREATE_TIME("create_time", "创建时间"),
    CREATE_BY("create_by", "创建人"),
    ;

    private final String code;
    private final String info;

    BonusPayApplicationField(String code, String info) {
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
