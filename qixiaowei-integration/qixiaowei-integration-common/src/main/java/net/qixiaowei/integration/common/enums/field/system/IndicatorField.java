package net.qixiaowei.integration.common.enums.field.system;

public enum IndicatorField {

    INDICATOR_NAME("indicator_name", "指标名称"),
    INDICATOR_CODE("indicator_code", "指标编码"),
    INDICATOR_TYPE("indicator_type", "指标类型"),
    INDICATOR_VALUE_TYPE("indicator_value_type", "指标值类型"),
    CHOICE_FLAG("choice_flag", "是否必选"),
    EXAMINE_DIRECTION("examine_direction", "考核方向"),
    PARENT_INDICATOR_ID("parent_indicator_id", "上级指标"),
    LEVEL("level", "指标层级"),
    DRIVING_FACTOR_FLAG("driving_factor_flag", "是否驱动因素"),
    ;

    private final String code;
    private final String info;

    IndicatorField(String code, String info) {
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
