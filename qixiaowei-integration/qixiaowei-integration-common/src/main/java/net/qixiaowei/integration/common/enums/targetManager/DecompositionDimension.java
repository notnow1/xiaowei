package net.qixiaowei.integration.common.enums.targetManager;

/**
 * 时间维度:1年度;2半年度;3季度;4月度;5周
 */
public enum DecompositionDimension {

    EMPLOYEE("销售员"),

    AREA( "区域"),

    DEPARTMENT( "部门"),

    PRODUCT( "产品"),
    REGION( "省份"),

    INDUSTRY("行业");

    private final String info;

    DecompositionDimension( String info) {
        this.info = info;
    }
    public String getInfo() {
        return info;
    }


}
