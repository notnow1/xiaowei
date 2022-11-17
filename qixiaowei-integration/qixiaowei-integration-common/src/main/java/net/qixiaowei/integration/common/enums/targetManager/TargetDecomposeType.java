package net.qixiaowei.integration.common.enums.targetManager;

/**
 * 时间维度:1年度;2半年度;3季度;4月度;5周
 */
public enum TargetDecomposeType {

    ORDER(1),

    INCOME(2),

    RECEIVABLE(3),

    CUSTOM(0);
    private final Integer code;


    TargetDecomposeType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
