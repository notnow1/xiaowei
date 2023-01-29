package net.qixiaowei.integration.common.enums.system;

/**
 * @description 角色类型:0内置角色;1自定义角色
 * @Author hzk
 * @Date 2023-01-28 13:50
 **/
public enum RoleType {

    BUILT_IN(0, "内置角色"),

    CUSTOM(1, "自定义角色"),
    ;

    private final Integer code;
    private final String info;

    RoleType(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}

