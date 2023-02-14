package net.qixiaowei.integration.common.enums.field.operate;

public enum TargetDecomposeOrderField {

    TARGET_YEAR("target_year", "目标年度"),
    DECOMPOSITION_DIMENSION("decomposition_dimension", "分解维度"),
    TIME_DIMENSION("time_dimension", "时间维度"),
    TARGET_VALUE("target_value", "公司目标"),
    DECOMPOSE_TARGET("decompose_target", "分解目标"),

    ;

    private final String code;
    private final String info;

    TargetDecomposeOrderField(String code, String info) {
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
