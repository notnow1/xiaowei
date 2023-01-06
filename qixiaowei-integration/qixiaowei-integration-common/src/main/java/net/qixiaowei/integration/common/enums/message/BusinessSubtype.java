package net.qixiaowei.integration.common.enums.message;

/**
 * @description 业务子类型枚举
 * @Author hzk
 * @Date 2022-12-09 19:13
 **/
public enum BusinessSubtype {

    TENANT_DOMAIN_APPROVAL(101, BusinessType.TENANT, "二级域名申请"),
    TENANT_DOMAIN_APPROVAL_PASS(102, BusinessType.TENANT, "域名申请通过"),
    TENANT_DOMAIN_APPROVAL_REFUSE(103, BusinessType.TENANT, "域名申请不通过"),

    TARGET_DECOMPOSE_ORDER_NOTIFY(2301, BusinessType.TARGET_DECOMPOSE_ORDER, "销售订单目标分解通知预测人"),

    TARGET_DECOMPOSE_INCOME_NOTIFY(2401, BusinessType.TARGET_DECOMPOSE_INCOME, "销售收入目标分解通知预测人"),

    TARGET_DECOMPOSE_RECOVERY_NOTIFY(2501, BusinessType.TARGET_DECOMPOSE_RECOVERY, "销售回款目标分解通知预测人"),

    TARGET_DECOMPOSE_CUSTOM_NOTIFY(2601, BusinessType.TARGET_DECOMPOSE, "自定义目标分解通知预测人"),
    ROLLING_PREDICTION_MANAGE_TRANSFER(2005, BusinessType.TARGET_DECOMPOSE_ROLL, "滚动预测管理移交"),

    EMPLOYEE_ANNUAL_BONUS_COMMENT_SUPERVISOR(4301, BusinessType.EMPLOYEE_ANNUAL_BONUS, "个人年终奖生成主管初评"),
    EMPLOYEE_ANNUAL_BONUS_COMMENT_MANAGEMENT_TEAM(4302, BusinessType.EMPLOYEE_ANNUAL_BONUS, "个人年终奖生成管理团队评议");


    private Integer code;

    private BusinessType parentBusinessType;
    private String info;

    BusinessSubtype(Integer code, BusinessType parentBusinessType, String info) {
        this.code = code;
        this.parentBusinessType = parentBusinessType;
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

    public BusinessType getParentBusinessType() {
        return parentBusinessType;
    }

    public void setParentBusinessType(BusinessType parentBusinessType) {
        this.parentBusinessType = parentBusinessType;
    }

}
