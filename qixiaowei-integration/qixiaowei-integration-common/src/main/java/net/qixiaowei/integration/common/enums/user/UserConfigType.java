package net.qixiaowei.integration.common.enums.user;

/**
 * @description 用户配置类型
 * @Author hzk
 * @Date 2023-01-30 16:05
 **/
public enum UserConfigType {

    BACKUP_LOG_NOTICE(1, "启用待办通知"),

    ;

    private final Integer code;
    private final String info;

    UserConfigType(Integer code, String info) {
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

