package net.qixiaowei.integration.common.enums.field;

public enum BaseField {

    CREATE_TIME("create_time", "创建时间"),
    CREATE_BY("create_by", "创建人"),
    ;

    private final String code;
    private final String info;

    BaseField(String code, String info) {
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
