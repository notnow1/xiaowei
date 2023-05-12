package net.qixiaowei.integration.common.enums.field.system;

public enum UserField {
    EMPLOYEE_NAME("employee_name", "姓名"),
    USER_ACCOUNT("user_account", "账号（手机号）"),
    DEPARTMENT_ID("department_id", "组织"),
    STATUS("status", "账号状态"),
    ROLES("role_names", "角色"),
    EMAIL("email", "邮箱"),
    ;

    private final String code;
    private final String info;

    UserField(String code, String info) {
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
