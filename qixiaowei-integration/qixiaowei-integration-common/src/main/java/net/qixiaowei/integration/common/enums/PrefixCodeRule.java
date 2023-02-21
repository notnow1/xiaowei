package net.qixiaowei.integration.common.enums;

/**
 * 编码规则前缀枚举
 */
public enum PrefixCodeRule {

    TENANT("KH", "KH+六位流水号，流水号起始值为000001，步长为1，断号自动补充"),

    ROLE("JS", "JS+三位流水号，流水号起始值为001，步长为1，断号自动补充"),

    DEPARTMENT("ZZ", "ZZ+三位流水号，流水号起始值为001，步长为1，断号自动补充"),

    EMPLOYEE("YG", "YG+六位流水号，流水号起始值为000001，步长为1，断号自动补充"),

    POST("GW", "GW+三位流水号，流水号起始值为001，步长为1，断号自动补充"),

    INDUSTRY("HY", "HY+三位流水号，流水号起始值为001，步长为1，断号自动补充"),

    INDICATOR_FINANCE("CW", "CW+三位流水号，流水号起始值为001，步长为1，断号自动补充"),

    INDICATOR_BUSINESS("YW", "YW+三位流水号，流水号起始值为001，步长为1，断号自动补充"),

    INDICATOR_CATEGORY_FINANCE("CL", "CL+三位流水号，流水号起始值为001，步长为1，断号自动补充"),

    INDICATOR_CATEGORY_BUSINESS("YL", "YL+三位流水号，流水号起始值为001，步长为1，断号自动补充"),

    AREA("QY", "QY+三位流水号，流水号起始值为001，步长为1，断号自动补充"),

    PRODUCT("PR", "PR+六位流水号，流水号起始值为000001，步长为1，断号自动补充"),

    BONUS_PAY_APPLICATION("JX", "JX+六位流水号，流水号起始值为000001，步长为1，断号自动补充"),

    PLAN_BUSINESS_UNIT("GD", "GD+三位流水号，流水号起始值为001，步长为1，断号自动补充。"),


    ;

    private final String code;
    private final String info;

    PrefixCodeRule(String code, String info) {
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
