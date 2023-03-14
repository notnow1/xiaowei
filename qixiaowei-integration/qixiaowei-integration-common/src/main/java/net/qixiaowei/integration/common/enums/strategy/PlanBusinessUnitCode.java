package net.qixiaowei.integration.common.enums.strategy;

import net.qixiaowei.integration.common.utils.StringUtils;

import java.util.*;

/**
 * 业务单元枚举
 */
public enum PlanBusinessUnitCode {

    REGION("region", "区域", "areaId"),
    DEPARTMENT("department", "部门", "departmentId"),
    PRODUCT("product", "产品", "productId"),
    INDUSTRY("industry", "行业", "industryId");


    private final String code;
    private final String info;
    private final String name;

    PlanBusinessUnitCode(String code, String info, String name) {
        this.code = code;
        this.info = info;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    /**
     * 获取规划业务单元下拉列表
     *
     * @param decompose 维度
     * @return List
     */
    public static List<Map<String, String>> getExportDropList(String decompose) {
        List<Map<String, String>> dropList = new ArrayList<>();
        if (StringUtils.isNotNull(decompose)) {
            String[] decomposeList = decompose.split(";");
            for (String decomposerValue : decomposeList) {
                for (PlanBusinessUnitCode item : PlanBusinessUnitCode.values()) {
                    if (item.code.equals(decomposerValue)) {
                        Map<String, String> map = new HashMap<>();
                        map.put("label", item.getInfo());
                        map.put("value", item.getCode());
                        map.put("name", item.getName());
                        dropList.add(map);
                        break;
                    }
                }
            }
            return dropList;
        }
        return new ArrayList<>();
    }

    /**
     * 获取规划业务单元下拉列表
     *
     * @param businessUnitDecompose 维度
     * @return List
     */
    public static List<Map<String, Object>> getDropList(String businessUnitDecompose) {
        if (StringUtils.isNotEmpty(businessUnitDecompose)) {
            List<String> businessUnitDecomposeList = Arrays.asList(businessUnitDecompose.split(";"));
            List<Map<String, Object>> businessUnitDecomposes = new ArrayList<>();
            businessUnitDecomposeList.forEach(decompose -> {
                for (PlanBusinessUnitCode item : PlanBusinessUnitCode.values()) {
                    if (item.code.equals(decompose)) {
                        Map<String, Object> businessUnitDecomposeMap = new HashMap<>();
                        businessUnitDecomposeMap.put("label", item.getInfo());
                        businessUnitDecomposeMap.put("value", item.getCode());
                        businessUnitDecomposes.add(businessUnitDecomposeMap);
                        break;
                    }
                }
            });
            return businessUnitDecomposes;
        }
        return new ArrayList<>();
    }


    /**
     * 是否包含
     *
     * @param code 编码
     * @return 真假
     */
    public static Boolean contains(String code) {
        for (PlanBusinessUnitCode item : PlanBusinessUnitCode.values()) {
            if (item.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

}


