package net.qixiaowei.integration.common.enums.field.operate;

public enum EmpSalaryAdjustPlanField {

    EMPLOYEE_ID("employee_id", "姓名"),
    EMPLOYEE_CODE("employee_code", "工号"),
    EMPLOYMENT_DATE("employment_date", "入职时间"),
    SENIORITY("seniority", "司龄"),
    DEPARTMENT_ID("department_id", "所在部门"),
    DEPARTMENT_LEADER_ID("department_leader_id", "部门主管"),
    ADJUST_POST_ID("adjust_post_id", "调整岗位"),
    ADJUST_OFFICIAL_RANK_SYSTEM_ID("adjust_official_rank_system_id", "职级体系"),
    ADJUST_OFFICIAL_RANK("adjust_official_rank", "调整职级"),
    ADJUST_EMOLUMENT("adjust_emolument", "调整基本工资"),
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
