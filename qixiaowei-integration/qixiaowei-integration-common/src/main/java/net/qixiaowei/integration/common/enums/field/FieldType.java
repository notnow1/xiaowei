package net.qixiaowei.integration.common.enums.field;

/**
 * @description 字段类型
 * @Author hzk
 * @Date 2022-12-13 15:50
 **/
public enum FieldType {

    TEXT(1, "文本"),

    AMOUNT(2, "金额"),

    PERCENTAGE(3, "百分比"),
    ;


    private Integer code;
    private String info;

    FieldType(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
