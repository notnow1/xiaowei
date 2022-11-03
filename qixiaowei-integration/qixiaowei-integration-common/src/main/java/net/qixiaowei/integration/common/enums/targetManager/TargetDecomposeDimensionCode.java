package net.qixiaowei.integration.common.enums.targetManager;

public enum TargetDecomposeDimensionCode {

    REGION("region", "区域", "areaId"),

    SALESMAN("salesman", "销售员", "employeeId"),

    DEPARTMENT("department", "部门", "departmentId"),

    PRODUCT("product", "产品", "productId"),

    PROVINCE("province", "省份", "regionId"),

    INDUSTRY("industry", "行业", "industryId");

    private final String code;
    private final String info;
    private final String fieldName;

    TargetDecomposeDimensionCode(String code, String info, String fieldName) {
        this.code = code;
        this.info = info;
        this.fieldName = fieldName;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public String getFieldName() {
        return fieldName;
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

    public static String selectFiledName(String code) {
        for (TargetDecomposeDimensionCode targetDecomposeDimensionCode : TargetDecomposeDimensionCode.values()) {
            if (targetDecomposeDimensionCode.getCode().equals(code)) {
                return targetDecomposeDimensionCode.getFieldName();
            }
        }
        return "";
    }
}
