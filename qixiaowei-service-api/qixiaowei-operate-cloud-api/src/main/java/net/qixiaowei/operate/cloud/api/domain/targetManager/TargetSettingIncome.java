package net.qixiaowei.operate.cloud.api.domain.targetManager;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
 * 目标制定收入表
 *
 * @author Graves
 * @since 2022-10-31
 */
@Data
@Accessors(chain = true)
public class TargetSettingIncome extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long targetSettingIncomeId;
    /**
     * 目标制定ID
     */
    private Long targetSettingId;
    /**
     * 一年前订单金额
     */
    private BigDecimal moneyBeforeOne;
    /**
     * 两年前订单金额
     */
    private BigDecimal moneyBeforeTwo;
    /**
     * 三年前订单金额
     */
    private BigDecimal moneyBeforeThree;
    /**
     * 一年前订单转化率
     */
    private BigDecimal conversionBeforeOne;
    /**
     * 两年前订单转化率
     */
    private BigDecimal conversionBeforeTwo;
    /**
     * 三年前订单转化率
     */
    private BigDecimal conversionBeforeThree;

}

