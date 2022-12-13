package net.qixiaowei.integration.common.enums.message;

/**
 * @description 业务子类型枚举
 * @Author hzk
 * @Date 2022-12-09 19:13
 **/
public enum BusinessSubtype {

    TENANT_DOMAIN_APPROVAL(1001, BusinessType.TENANT, "域名申请"),
    TENANT_DOMAIN_APPROVAL_PASS(1002, BusinessType.TENANT, "域名申请通过"),
    TENANT_DOMAIN_APPROVAL_REFUSE(1003, BusinessType.TENANT, "域名申请不通过"),

    ROLLING_PREDICTION_ORDER(2001, BusinessType.TARGET, "销售订单滚动预测"),
    ROLLING_PREDICTION_INCOME(2002, BusinessType.TARGET, "销售收入滚动预测"),
    ROLLING_PREDICTION_RECOVERY(2003, BusinessType.TARGET, "销售回款滚动预测"),
    ROLLING_PREDICTION_CUSTOM(2004, BusinessType.TARGET, "自定义滚动预测"),
    ROLLING_PREDICTION_MANAGE_TRANSFER(2005, BusinessType.TARGET, "滚动预测管理移交"),

    EMPLOYEE_ANNUAL_BONUS_COMMENT_SUPERVISOR(4001, BusinessType.BONUS, "个人年终奖生成主管初评"),
    EMPLOYEE_ANNUAL_BONUS_COMMENT_MANAGEMENT_TEAM(4002, BusinessType.BONUS, "个人年终奖生成管理团队评议");


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
