package net.qixiaowei.integration.common.enums.system;

/**
 * @description 角色数据范围枚举---1全公司;2本部门及下属部门;3本部门;4本人及下属;5本人
 * @Author hzk
 * @Date 2022-12-02 13:50
 **/
public enum RoleDataScope {

    ALL(1, "全公司"),

    ALL_SUB_DEPARTMENT(2, "本部门及下属部门"),

    DEPARTMENT(3, "本部门"),

    SELF_AND_SUBORDINATE(4, "本人及下属"),

    SELF(5, "本人");

    private final Integer code;
    private final String info;

    RoleDataScope(Integer code, String info) {
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

