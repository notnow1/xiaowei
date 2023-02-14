package net.qixiaowei.integration.common.enums.field.system;

public enum DepartmentField {

    DEPARTMENT_NAME("department_name", "组织名称"),
    DEPARTMENT_CODE("department_code", "组织编码"),
    PARENT_DEPARTMENT_ID("parent_department_id", "上级组织"),
    LEVEL("level", "组织层级"),
    DEPARTMENT_LEADER_ID("department_leader_id", "组织负责人"),
    DEPARTMENT_LEADER_POST_ID("department_leader_post_id", "组织负责人岗位"),
    EXAMINATION_LEADER_ID("examination_leader_id", "考核负责人"),
    DEPARTMENT_IMPORTANCE_FACTOR("department_importance_factor", "组织重要性系数"),
    STATUS("status", "组织状态"),

    ;

    private final String code;
    private final String info;

    DepartmentField(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

}
