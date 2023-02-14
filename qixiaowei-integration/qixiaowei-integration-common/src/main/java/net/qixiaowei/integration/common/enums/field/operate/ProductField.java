package net.qixiaowei.integration.common.enums.field.operate;

public enum ProductField {

    PRODUCT_NAME("product_name", "产品名称"),
    PRODUCT_CODE("product_code", "产品编码"),
    PARENT_PRODUCT_ID("parent_product_id", "上级产品"),
    LEVEL("level", "产品层级"),
    PRODUCT_UNIT_ID("product_unit_id", "产品量纲"),
    PRODUCT_CATEGORY("product_category", "产品类别"),
    LISTING_FLAG("listing_flag", "是否上下架"),
    ;

    private final String code;
    private final String info;

    ProductField(String code, String info) {
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
