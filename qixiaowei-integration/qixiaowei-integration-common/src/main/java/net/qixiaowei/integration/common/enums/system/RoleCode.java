package net.qixiaowei.integration.common.enums.system;

/**
 * @description 角色编码---admin，超级管理员；JS001，租户管理员
 * @Author hzk
 * @Date 2022-12-02 13:50
 **/
public enum RoleCode {

    SUPER_ADMIN("admin", "超级管理员"),

    TENANT_ADMIN("JS001", "系统管理员");

    private final String code;
    private final String info;

    RoleCode(String code, String info) {
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

