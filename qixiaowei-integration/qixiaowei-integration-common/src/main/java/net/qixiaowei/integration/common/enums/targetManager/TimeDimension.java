package net.qixiaowei.integration.common.enums.targetManager;

/**
 * 时间维度:1年度;2半年度;3季度;4月度;5周
 */
public enum TimeDimension {

    YEAR(1, "年度"),

    SEMI_ANNUAL(2, "半年度"),

    QUARTER(3, "季度"),

    MONTHLY(4, "月度"),

    WEEKLY(5, "周");

    private final Integer code;
    private final String info;

    TimeDimension(Integer code, String info) {
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
        for (TimeDimension timeDimension : TimeDimension.values()) {
            if (timeDimension.getCode().equals(code)) {
                return timeDimension.getInfo();
            }
        }
        return "";
    }


}
