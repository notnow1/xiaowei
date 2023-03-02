package net.qixiaowei.integration.common.enums.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * label: "销售员", value: "employeeId"
     *
     * @param code 编码
     * @return List
     */
    public static List<Map<String, String>> getDropList(String code) {
        List<Map<String, String>> dropList = new ArrayList<>();
        for (PlanBusinessUnitCode item : PlanBusinessUnitCode.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("label", item.getCode());
            map.put("value", item.getInfo());
            map.put("name", item.getName());
            dropList.add(map);
        }
        return dropList;
    }
}


