package net.qixiaowei.integration.common.enums.field.strategyDecode;


public enum AnnualKeyWorkDetailField {
                ANNUAL_KEY_WORK_DETAIL_ID("annual_key_work_detail_id", "ID"),
                        ANNUAL_KEY_WORK_ID("annual_key_work_id", "年度重点工作ID"),
                        TASK_NUMBER("task_number", "任务编号"),
                        TASK_NAME("task_name", "任务名称"),
                        DEPARTMENT_ID("department_id", "所属部门"),
                        DEPARTMENT_EMPLOYEE_ID("department_employee_id", "部门主管"),
                        TASK_DESCRIPTION("task_description", "任务描述"),
                        CLOSE_STANDARD("close_standard", "闭环标准"),
                        TASK_START_TIME("task_start_time", "任务开始时间"),
                        TASK_END_TIME("task_end_time", "任务结束时间"),
                        DUTY_EMPLOYEE_ID("duty_employee_id", "责任人"),
                        DUTY_EMPLOYEE_NAME("duty_employee_name", "责任人员姓名"),
                        DUTY_EMPLOYEE_CODE("duty_employee_code", "责任人员编码"),
        ;
        private final String code;
        private final String info;
        AnnualKeyWorkDetailField(String code, String info) {
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

