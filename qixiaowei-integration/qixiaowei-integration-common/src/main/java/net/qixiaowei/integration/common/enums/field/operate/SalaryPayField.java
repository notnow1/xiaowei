package net.qixiaowei.integration.common.enums.field.operate;

public enum SalaryPayField {

    PAY_YEAR_MONTH("pay_year_month", "发薪年月"),
    EMPLOYEE_CODE("employee_code", "工号"),
    EMPLOYEE_NAME("employee_name", "姓名"),
    EMPLOYEE_DEPARTMENT_NAME("employee_department_name", "最小部门"),
    EMPLOYEE_POST_NAME("employee_post_name", "岗位"),
    EMPLOYEE_RANK_NAME("employee_rank_name", "个人职级"),
    TOTAL_WAGES("total_wages", "总工资包"),
    BONUS_AMOUNT("bonus_amount", "总奖金包"),
    TOTAL_AMOUNT("total_amount", "总薪酬包"),
    TOTAL_DEDUCTIONS("total_deductions", "总扣减项"),
    PAY_AMOUNT("pay_amount", "实发"),
    ;

    private final String code;
    private final String info;

    SalaryPayField(String code, String info) {
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
