package net.qixiaowei.integration.common.enums.field.operate;

public enum EmpSalaryAdjustPlanField {

    EMPLOYEE_ID("employee_id", "姓名"),
    EMPLOYEE_CODE("employee_code", "工号"),
    EMPLOYMENT_DATE("employment_date", "入职时间"),
    SENIORITY("seniority", "司龄"),
    DEPARTMENT_ID("department_id", "原部门"),
    ADJUST_DEPARTMENT_ID("adjust_department_id", "调整后部门"),
    POST_ID("post_id", "原岗位"),
    ADJUST_POST_ID("adjust_post_id", "调整后岗位"),
    OFFICIAL_RANK_SYSTEM_ID("rank_system_id", "原职级体系"),
    ADJUST_OFFICIAL_RANK_SYSTEM_ID("adjust_official_rank_system_id", "调整后职级体系"),
    OFFICIAL_RANK("official_rank", "原职级"),
    ADJUST_OFFICIAL_RANK("adjust_official_rank", "调整职级"),
    BASIC_WAGE("basic_wage", "原基本工资"),
    ADJUST_EMOLUMENT("adjust_emolument", "调整后基本工资"),
    EFFECTIVE_DATE("effective_date", "生效日期"),

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
