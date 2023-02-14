package net.qixiaowei.integration.common.enums.field.operate;

public enum PerformanceAppraisalField {

    APPRAISAL_NAME("appraisal_name", "考核任务名称"),
    APPRAISAL_YEAR("appraisal_year", "考核年度"),
    APPRAISAL_OBJECT("appraisal_object", "考核对象"),
    CYCLE_FLAG("cycle_flag", "是否周期性考核"),
    CYCLE_TYPE("cycle_type", "周期类型"),
    CYCLE_NUMBER("cycle_number", "周期"),
    APPRAISAL_START_DATE("appraisal_start_date", "考核开始日期"),
    APPRAISAL_END_DATE("appraisal_end_date", "考核结束日期"),
    PERFORMANCE_RANK_ID("performance_rank_id", "绩效等级"),
    APPRAISAL_FLOW("appraisal_flow", "考核流程"),
    APPRAISAL_STATUS("appraisal_status", "任务阶段"),
    ;

    private final String code;
    private final String info;

    PerformanceAppraisalField(String code, String info) {
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
