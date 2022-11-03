package net.qixiaowei.integration.common.enums.targetManager;

public enum TargetDecomposeDimensionCode {

    REGION("region", "区域", "areaId","areaName"),

    SALESMAN("salesman", "销售员", "employeeId","employeeName"),

    DEPARTMENT("department", "部门", "departmentId","departmentName"),

    PRODUCT("product", "产品", "productId","departmentName"),

    PROVINCE("province", "省份", "regionId","regionName"),

    INDUSTRY("industry", "行业", "industryId","industryName");

    private final String code;
    private final String info;
    private final String fieldName;
    private final String fieldValue;


    TargetDecomposeDimensionCode(String code, String info, String fieldName,String fieldValue) {
        this.code = code;
        this.info = info;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
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

    public String getFieldValue() {
        return fieldValue;
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

    public static String selectFiledValue(String code) {
        for (TargetDecomposeDimensionCode targetDecomposeDimensionCode : TargetDecomposeDimensionCode.values()) {
            if (targetDecomposeDimensionCode.getCode().equals(code)) {
                return targetDecomposeDimensionCode.getFieldValue();
            }
        }
        return "";
    }
}
