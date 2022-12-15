package net.qixiaowei.integration.common.enums.targetManager;

/**
 * 目标分解类型:0自定义;1销售订单;2销售收入;3销售回款
 */
public enum TargetDecomposeType {

    ORDER(1, "销售订单"),

    INCOME(2, "销售收入"),

    RECEIVABLE(3, "销售回款"),

    CUSTOM(0, "自定义");
    private final Integer code;

    private final String info;

    TargetDecomposeType(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static String getInfo(Integer code) {
        for (TargetDecomposeType targetDecomposeType : TargetDecomposeType.values()) {
            if (targetDecomposeType.getCode().equals(code)) {
                return targetDecomposeType.getInfo();
            }
        }
        return "";
    }
}
