package net.qixiaowei.integration.common.enums.field.operate;

public enum EmpSalaryAdjustPlanField {
    EMPLOYEE_CODE("employee_code", "工号"),
    EMPLOYEE_ID("employee_id", "姓名"),
    DEPARTMENT_ID("department_id", "原部门"),
    ADJUST_DEPARTMENT_ID("adjust_department_id", "调整后部门"),
    POST_ID("post_id", "原岗位"),
    ADJUST_POST_ID("adjust_post_id", "调整后岗位"),
    OFFICIAL_RANK("official_rank", "原职级"),
    ADJUST_OFFICIAL_RANK("adjust_official_rank", "调整后职级"),
    BASIC_WAGE("basic_wage", "原基本工资"),
    ADJUST_EMOLUMENT("adjust_emolument", "调整后基本工资"),
    EFFECTIVE_DATE("effective_date", "生效时间"),

    ;

    private final String code;
    private final String info;

    EmpSalaryAdjustPlanField(String code, String info) {
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
