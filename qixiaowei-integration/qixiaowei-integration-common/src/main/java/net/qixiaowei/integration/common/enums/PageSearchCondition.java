package net.qixiaowei.integration.common.enums;

/**
 * 页面查找条件枚举
 */
public enum PageSearchCondition {

    EQUAL("Equal", "等于"),
    NOT_EQUAL("NotEqual", "不等于"),
    LIKE("Like", "包含"),
    NOT_LIKE("NotLike", "不包含"),
    NULL("Null", "为空"),
    NOT_NULL("NotNull", "不为空"),
    BEFORE("Before", "早于"),
    NOT_BEFORE("NotBefore", "不早于，包含等于"),
    AFTER("After", "晚于"),
    NOT_AFTER("NotAfter", "不晚于，包含等于"),
    GREATER_THAN("GreaterThan", "大于"),
    NOT_GREATER_THAN("NotGreaterThan", "不大于"),
    LESS_THAN("LessThan", "小于"),
    NOT_LESS_THAN("NotLessThan", "不小于"),
    BETWEEN_START("Start", "介于，开始"),
    BETWEEN_END("End", "介于，结束"),
    ;
    private final String code;

    private final String info;

    PageSearchCondition(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static boolean endsWithCodeOfList(String code) {
        return code.endsWith(PageSearchCondition.EQUAL.getCode())
                || code.endsWith(PageSearchCondition.NOT_EQUAL.getCode())
                || code.endsWith(PageSearchCondition.LIKE.getCode())
                || code.endsWith(PageSearchCondition.NOT_LIKE.getCode());
    }
}
