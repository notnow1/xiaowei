package net.qixiaowei.integration.common.enums.targetManager;

import net.qixiaowei.integration.common.enums.salary.ThirdLevelSalaryCode;

public enum TargetDecomposeDimensionCode {

    REGION("region", "区域"),

    SALESMAN("salesman", "销售员"),

    DEPARTMENT("department", "部门"),

    PRODUCT("product", "产品"),

    PROVINCE("province", "省份"),

    INDUSTRY("industry", "行业");

    private final String code;
    private final String info;

    TargetDecomposeDimensionCode(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static Boolean containCode(String code) {
        for (TargetDecomposeDimensionCode targetDecomposeDimensionCode : TargetDecomposeDimensionCode.values()) {
            if (targetDecomposeDimensionCode.getCode().equals(code)) {
                return false;
            }
        }
        return true;
    }

    public static String selectInfo(String code) {
        for (TargetDecomposeDimensionCode targetDecomposeDimensionCode : TargetDecomposeDimensionCode.values()) {
            if (targetDecomposeDimensionCode.getCode().equals(code)) {
                return targetDecomposeDimensionCode.getInfo();
            }
        }
        return "";
    }
}
