package net.qixiaowei.integration.common.enums.field.operate;

public enum PerformanceAppraisalOrgReviewField {

    APPRAISAL_NAME("appraisal_name", "考核任务名称"),
    APPRAISAL_YEAR("appraisal_year", "考核年度"),
    CYCLE_TYPE("cycle_type", "周期类型"),
    CYCLE_NUMBER("cycle_number", "周期"),
    APPRAISAL_OBJECT_NAME("appraisal_object_name", "部门"),
    APPRAISAL_PRINCIPAL_ID("appraisal_principal_id", "考核责任人"),
    EVALUATION_SCORE("evaluation_score", "评议分数"),
    APPRAISAL_OBJECT_STATUS("appraisal_object_status", "状态"),

    ;

    private final String code;
    private final String info;

    PerformanceAppraisalOrgReviewField(String code, String info) {
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
