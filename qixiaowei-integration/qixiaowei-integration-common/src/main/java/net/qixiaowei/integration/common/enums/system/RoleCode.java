package net.qixiaowei.integration.common.enums.system;

/**
 * @description 角色编码---admin，超级管理员；JS001，租户管理员
 * @Author hzk
 * @Date 2022-12-02 13:50
 **/
public enum RoleCode {

    SUPER_ADMIN("admin", "超级管理员", "super_admin"),

    TENANT_ADMIN("JS001", "系统管理员", "admin"),

    TENANT_DEFAULT("JS002", "默认角色", "default");

    private final String code;
    private final String info;

    private final String remark;

    RoleCode(String code, String info, String remark) {
        this.code = code;
        this.info = info;
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public String getRemark() {
        return remark;
    }
}

