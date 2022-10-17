package net.qixiaowei.integration.common.enums.system;

/**
 * 配置编码枚举
 */
public enum ConfigCode {

    INDUSTRY_ENABLE("industry_enable", "行业启用：1系统；2自定义");

    private final String code;
    private final String info;

    ConfigCode(String code, String info) {
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
