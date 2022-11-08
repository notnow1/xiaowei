package net.qixiaowei.integration.common.enums.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统指标枚举
 */
public enum IndicatorCode {

    ORDER("CW001", "订单（不含税）", 1),
    INCOME("CW002", "销售收入", 1),
    RECEIVABLE("CW022", "回款金额（含税）", 1),
    GROSS("CW010", "销售毛利", 2),
    PROFITS("CW020", "净利润", 2);

    private final String code;
    private final String info;
    /**
     * 标识（1-不可编辑和删除，2-可编辑不可删除，3-可编辑可删除)
     */
    private final Integer isPreset;

    IndicatorCode(String code, String info, Integer isPreset) {
        this.code = code;
        this.info = info;
        this.isPreset = isPreset;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public Integer getIsPreset() {
        return isPreset;
    }

    public static Boolean contains(String code) {
        for (IndicatorCode item : IndicatorCode.values()) {
            if (item.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static Integer selectIsPreset(String code) {
        for (IndicatorCode indicatorCode : IndicatorCode.values()) {
            if (indicatorCode.getCode().equals(code)) {
                return indicatorCode.getIsPreset();
            }
        }
        return 4;
    }

    public static String selectInfo(String code) {
        for (IndicatorCode indicatorCode : IndicatorCode.values()) {
            if (indicatorCode.getCode().equals(code)) {
                return indicatorCode.getInfo();
            }
        }
        return null;
    }

    public static List<String> getAllCodes() {
        ArrayList<String> indicatorCodes = new ArrayList<>();
        for (IndicatorCode indicatorCode : IndicatorCode.values()) {
            indicatorCodes.add(indicatorCode.code);
        }
        return indicatorCodes;
    }

    public static List<String> getCodesByIsPreset(Integer isPreset) {
        ArrayList<String> indicatorCodes = new ArrayList<>();
        for (IndicatorCode indicatorCode : IndicatorCode.values()) {
            if (indicatorCode.getIsPreset().equals(isPreset)) {
                indicatorCodes.add(indicatorCode.code);
            }
        }
        return indicatorCodes;
    }
}
