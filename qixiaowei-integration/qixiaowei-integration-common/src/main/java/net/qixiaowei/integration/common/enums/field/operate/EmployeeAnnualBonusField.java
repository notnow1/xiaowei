package net.qixiaowei.integration.common.enums.field.operate;

public enum EmployeeAnnualBonusField {

    ANNUAL_BONUS_YEAR("annual_bonus_year", "评议年度"),
    DEPARTMENT_ID("department_id", "一级部门"),
    DISTRIBUTE_BONUS_AMOUNT("distribute_bonus_amount", "可分配年终奖"),
    APPLY_BONUS_AMOUNT("apply_bonus_amount", "申请年终奖总额"),
    APPLY_DEPARTMENT_ID("apply_department_id", "申请部门"),
    APPLY_EMPLOYEE_ID("apply_employee_id", "申请人"),
    COMMENT_FLAG("comment_flag", "是否发起评议流程"),
    STATUS("status", "状态"),
    COMMENT_DATE("comment_date", "评议日期"),

    ;

    private final String code;
    private final String info;

    EmployeeAnnualBonusField(String code, String info) {
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
