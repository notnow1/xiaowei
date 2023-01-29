package net.qixiaowei.integration.common.enums.user;

/**
 * @description 用户类型:0其他;1系统管理员
 * @Author hzk
 * @Date 2023-01-28 16:50
 **/
public enum UserType {

    OTHER(0, "其他"),

    SYSTEM(1, "系统管理员"),
    ;

    private final Integer code;
    private final String info;

    UserType(Integer code, String info) {
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

