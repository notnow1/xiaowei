package net.qixiaowei.integration.common.enums.system;

/**
 * 字典类型枚举
 */
public enum DictionaryTypeCode {

    PRODUCT_CATEGORY("PRODUCT_CATEGORY", "产品类别");

    private final String code;
    private final String info;

    DictionaryTypeCode(String code, String info) {
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
