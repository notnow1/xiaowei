package net.qixiaowei.integration.common.enums.system;

import net.qixiaowei.integration.common.utils.StringUtils;

/**
 * @description 角色数据范围枚举---1全公司;2本部门及下属部门;3本部门;4本人及下属;5本人
 * @Author hzk
 * @Date 2022-12-02 13:50
 **/
public enum RoleDataScope {

    ALL(1, 5, "全公司"),

    ALL_SUB_DEPARTMENT(2, 4, "本部门及下属部门"),

    DEPARTMENT(3, 3, "本部门"),

    SELF_AND_SUBORDINATE(4, 2, "本人及下属"),

    SELF(5, 1, "本人");

    private final Integer code;

    private final Integer salesCode;
    private final String info;

    RoleDataScope(Integer code, Integer salesCode, String info) {
        this.code = code;
        this.salesCode = salesCode;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public Integer getSalesCode() {
        return salesCode;
    }

    public static RoleDataScope parseEnum(Integer code) {
        for (RoleDataScope roleDataScope : RoleDataScope.values()) {
            if (roleDataScope.getCode().equals(code)) {
                return roleDataScope;
            }
        }
        return SELF;
    }

    public static Integer convertSalesCode(Integer code) {
        code = StringUtils.isNull(code) ? SELF.getCode() : code;
        Integer salesCode = SELF.getSalesCode();
        for (RoleDataScope roleDataScope : RoleDataScope.values()) {
            if (roleDataScope.getCode().equals(code)) {
                return roleDataScope.getSalesCode();
            }
        }
        return salesCode;
    }
}

