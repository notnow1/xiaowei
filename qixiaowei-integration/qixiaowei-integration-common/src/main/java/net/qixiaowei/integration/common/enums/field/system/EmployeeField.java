package net.qixiaowei.integration.common.enums.field.system;

public enum EmployeeField {

    EMPLOYEE_CODE("employee_code", "工号"),
    EMPLOYEE_NAME("employee_name", "姓名"),
    EMPLOYEE_GENDER("employee_gender", "性别"),
    TOP_LEVEL_DEPARTMENT_ID("top_level_department_id", "所属一级部门"),
    EMPLOYEE_DEPARTMENT_ID("employee_department_id", "最小部门"),
    EMPLOYEE_POST_ID("employee_post_id", "岗位"),
    EMPLOYMENT_STATUS("employment_status", "用工关系状态"),
    EMPLOYMENT_DATE("employment_date", "入职时间"),
    DEPARTURE_DATE("departure_date", "离职时间"),
    OFFICIAL_RANK_SYSTEM_ID("official_rank_system_id", "职级体系"),
    POST_RANK_NAME("post_rank_name", "岗位职级"),
    EMPLOYEE_RANK("employee_rank", "个人职级"),
    EMPLOYEE_BASIC_WAGE("employee_basic_wage", "基本工资"),
    NATIONALITY("nationality", "国籍"),
    NATION("nation", "民族"),
    IDENTITY_CARD("identity_card", "身份证号码"),
    EMPLOYEE_BIRTHDAY("employee_birthday", "出生日期"),
    MARITAL_STATUS("marital_status", "婚姻状态"),
    EMPLOYEE_MOBILE("employee_mobile", "手机"),
    EMPLOYEE_EMAIL("employee_email", "邮箱"),
    WECHAT_CODE("wechat_code", "微信"),
    STATUS("status", "员工状态"),
    ;

    private final String code;
    private final String info;

    EmployeeField(String code, String info) {
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
