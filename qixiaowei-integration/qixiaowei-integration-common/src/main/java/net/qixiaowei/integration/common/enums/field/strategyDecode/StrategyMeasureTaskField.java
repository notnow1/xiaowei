package net.qixiaowei.integration.common.enums.field.strategyDecode;


public enum StrategyMeasureTaskField {
                STRATEGY_MEASURE_TASK_ID("strategy_measure_task_id", "ID"),
                        STRATEGY_MEASURE_ID("strategy_measure_id", "战略举措清单ID"),
                        STRATEGY_MEASURE_DETAIL_ID("strategy_measure_detail_id", "战略举措清单详情ID"),
                        KEY_TASK("key_task", "关键任务"),
                        CLOSE_STANDARD("close_standard", "闭环标准"),
                        DUTY_DEPARTMENT_ID("duty_department_id", "责任部门"),
                        DUTY_EMPLOYEE_ID("duty_employee_id", "责任人员ID"),
                        DUTY_EMPLOYEE_NAME("duty_employee_name", "责任人员姓名"),
                        DUTY_EMPLOYEE_CODE("duty_employee_code", "责任人员编码"),
                        SORT("sort", "排序"),
        ;
        private final String code;
        private final String info;
        StrategyMeasureTaskField(String code, String info) {
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

