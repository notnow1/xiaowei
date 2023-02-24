package net.qixiaowei.integration.common.enums.field.system;

public enum UserField {

    USER_ACCOUNT("user_account", "账号"),
    EMPLOYEE_ID("employee_id", "关联员工"),
    MOBILE_PHONE("mobile_phone", "手机号码"),
    EMAIL("email", "邮箱"),
    USER_NAME("user_name", "账号姓名"),
    STATUS("status", "账号状态"),
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
