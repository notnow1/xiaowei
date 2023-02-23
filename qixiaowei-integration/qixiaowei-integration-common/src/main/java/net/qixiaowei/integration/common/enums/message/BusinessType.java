package net.qixiaowei.integration.common.enums.message;

/**
 * @description 业务类型枚举
 * @Author hzk
 * @Date 2022-12-09 19:15
 **/
public enum BusinessType {

    TENANT(1, "租户管理"),
    USER(2, "用户管理"),
    ROLE(3, "角色管理"),
    DEPARTMENT(4, "部门管理"),
    EMPLOYEE(5, "人员管理"),
    POST(6, "岗位管理"),
    OFFICIAL_RANK_SYSTEM(7, "职级管理"),
    INDUSTRY(8, "行业管理"),
    INDICATOR(9, "指标管理"),
    INDICATOR_CATEGORY(10, "指标分类管理"),
    DICTIONARY_TYPE(11, "枚举值管理"),
    TARGET_DECOMPOSE_DIMENSION(12, "分解维度管理"),
    AREA(13, "区域管理"),
    PRODUCT(14, "产品管理"),
    PRODUCT_UNIT(15, "产品单位管理"),
    PERFORMANCE_RANK(16, "绩效等级管理"),
    PERFORMANCE_PERCENTAGE(17, "绩效比例管理"),
    SALARY_ITEM(18, "工资条管理"),
    TARGET_SETTING_ORDER(19, "目标制定-销售订单管理"),
    TARGET_SETTING_INCOME(20, "目标制定-销售收入管理"),
    TARGET_SETTING_RECOVERY(21, "目标制定-销售回款管理"),
    TARGET_SETTING(22, "目标制定-关键经营"),
    TARGET_DECOMPOSE_ORDER(23, "目标分解-销售订单管理"),
    TARGET_DECOMPOSE_INCOME(24, "目标分解-销售收入管理"),
    TARGET_DECOMPOSE_RECOVERY(25, "目标分解-销售回款管理"),
    TARGET_DECOMPOSE(26, "目标分解-自定义管理"),
    TARGET_DECOMPOSE_ROLL(27, "滚动预测管理"),
    PERFORMANCE_APPRAISAL(28, "绩效考核任务"),
    PERFORMANCE_APPRAISAL_ORG_SETTING(29, "组织绩效制定"),
    PERFORMANCE_APPRAISAL_ORG_REVIEW(30, "组织绩效评议"),
    PERFORMANCE_APPRAISAL_ORG_RANKING(31, "组织绩效排名"),
    PERFORMANCE_APPRAISAL_ORG_ARCHIVED(32, "组织绩效归档"),
    PERFORMANCE_APPRAISAL_PERSON_SETTING(33, "个人绩效制定"),
    PERFORMANCE_APPRAISAL_PERSON_REVIEW(34, "个人绩效评议"),
    PERFORMANCE_APPRAISAL_PERSON_RANKING(35, "个人绩效排名"),
    PERFORMANCE_APPRAISAL_PERSON_ARCHIVED(36, "个人绩效归档"),
    EMPLOYEE_BUDGET(37, "人力预算调控"),
    EMOLUMENT_PLAN(38, "薪酬规划"),
    BONUS_BUDGET(39, "总奖金包预算"),
    DEPT_BONUS_BUDGET(40, "部门奖金包预算"),
    BONUS_PAY_APPLICATION(41, "奖金发放申请"),
    DEPT_ANNUAL_BONUS(42, "部门年终奖"),
    EMPLOYEE_ANNUAL_BONUS(43, "个人年终奖"),
    DEPT_SALARY_ADJUST_PLAN(44, "部门调薪计划"),
    EMP_SALARY_ADJUST_PLAN(45, "个人调薪计划"),
    OFFICIAL_RANK_EMOLUMENT(46, "职级确定薪酬"),
    SALARY_PAY(47, "月度工资数据管理"),
    PLAN_BUSINESS_UNIT(48, "规划业务单元配置"),
    INDUSTRY_ATTRACTION(49, "行业吸引力配置"),
    STRATEGY_INTENT(50, "规划业务单元配置"),
    ;


    private Integer code;
    private String info;

    BusinessType(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * 根据业务类型获取业务枚举
     *
     * @param code 业务类型code
     * @return {@link BusinessType}
     */
    public static BusinessType getBusinessType(Integer code) {
        for (BusinessType businessType : BusinessType.values()) {
            if (businessType.getCode().equals(code)) {
                return businessType;
            }
        }
        return null;
    }
}
