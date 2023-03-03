package net.qixiaowei.integration.common.enums.system;

/**
 * 字典类型枚举
 */
public enum DictionaryTypeCode {

    PRODUCT_CATEGORY("PRODUCT_CATEGORY", "产品类别", "配置管理", "基础配置", "产品配置"),
    MARKET_INSIGHT_MACRO_VISUAL_ANGLE("MARKET_INSIGHT_MACRO_VISUAL_ANGLE", "视角", "战略云", "市场洞察", "看宏观"),
    MARKET_INSIGHT_INDUSTRY_INDUSTRY_TYPE("MARKET_INSIGHT_INDUSTRY_INDUSTRY_TYPE", "行业类型", "战略云", "市场洞察", "看行业"),
    MARKET_INSIGHT_CUSTOMER_CUSTOMER_CATEGORY("MARKET_INSIGHT_CUSTOMER_CUSTOMER_CATEGORY", "客户类别", "战略云", "市场洞察", "看客户"),
    MARKET_INSIGHT_OPPONENT_COMPARISON_ITEM("MARKET_INSIGHT_OPPONENT_COMPARISON_ITEM", "对比项目", "战略云", "市场洞察", "看对手"),
    MARKET_INSIGHT_OPPONENT_COMPETITOR_CATEGORY("MARKET_INSIGHT_OPPONENT_COMPETITOR_CATEGORY", "竞争对手类别", "战略云", "市场洞察", "看对手"),
    MARKET_INSIGHT_OPPONENT_COMPETITION_STRATEGY_TYPE("MARKET_INSIGHT_OPPONENT_COMPETITION_STRATEGY_TYPE", "竞争策略类型", "战略云", "市场洞察", "看对手"),
    MARKET_INSIGHT_SELF_CAPACITY_FACTOR("MARKET_INSIGHT_SELF_CAPACITY_FACTOR", "能力要素", "战略云", "市场洞察", "看自身"),
    STRATEGY_MEASURE_SOURCE("STRATEGY_MEASURE_SOURCE", "来源", "战略云", "战略解码", "战略举措清单"),

    ;

    private final String code;
    private final String info;

    /**
     * 菜单0层级名称
     */
    private final String menuZerothName;
    /**
     * 菜单1层级名称
     */
    private final String menuFirstName;
    /**
     * 菜单2层级名称
     */
    private final String menuSecondName;

    DictionaryTypeCode(String code, String info, String menuZerothName, String menuFirstName, String menuSecondName) {
        this.code = code;
        this.info = info;
        this.menuZerothName = menuZerothName;
        this.menuFirstName = menuFirstName;
        this.menuSecondName = menuSecondName;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public String getMenuZerothName() {
        return menuZerothName;
    }

    public String getMenuFirstName() {
        return menuFirstName;
    }

    public String getMenuSecondName() {
        return menuSecondName;
    }
}
