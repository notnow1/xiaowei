package net.qixiaowei.integration.common.enums.field.operate;

public enum PerformanceAppraisalPersonSettingField {
    APPRAISAL_YEAR("appraisal_year", "考核年度"),
    APPRAISAL_NAME("appraisal_name", "考核任务名称"),
    CYCLE_TYPE("cycle_type", "考核周期"),
    CYCLE_NUMBER("cycle_number", "周期"),
    EVALUATION_TYPE("evaluation_type", "评议周期"),
    APPRAISAL_OBJECT_CODE("appraisal_object_code", "被考核人工号"),
    APPRAISAL_OBJECT_NAME("appraisal_object_name", "被考核人姓名"),
    DEPARTMENT_NAME("department_name", "被考核人部门"),
    POST_NAME("post_name", "被考核人岗位"),
    APPRAISAL_PRINCIPAL_ID("appraisal_principal_id", "考核责任人"),
    APPRAISAL_OBJECT_STATUS("appraisal_object_status", "状态"),

    ;

    private final String code;
    private final String info;

    PerformanceAppraisalPersonSettingField(String code, String info) {
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
