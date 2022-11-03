package net.qixiaowei.integration.common.enums.basic;

/**
 * 系统指标枚举
 */
public enum IndicatorCode {

    ORDER("CW001", "订单（不含税）"),
    INCOME("CW002", "销售收入"),
    RECEIVABLE("CW022", "回款金额（含税）"),
    GROSS("CW010", "销售毛利"),
    PROFITS("CW020", "净利润");

    private final String code;
    private final String info;

    IndicatorCode(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static Boolean contains(String code) {
        for (IndicatorCode item : IndicatorCode.values()) {
            if (item.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
