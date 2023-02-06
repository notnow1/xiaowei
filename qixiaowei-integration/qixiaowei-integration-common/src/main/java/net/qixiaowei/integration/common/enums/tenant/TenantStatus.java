package net.qixiaowei.integration.common.enums.tenant;

/**
 * 租户状态（0待初始化 1正常 2禁用 3过期）
 */
public enum TenantStatus {

    WAITING_INIT(0, "待初始化"),
    NORMAL(1, "正常"),
    DISABLE(2, "禁用"),
    OVERDUE(3, "过期"),
    ;

    private final Integer code;
    private final String info;

    TenantStatus(Integer code, String info) {
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
