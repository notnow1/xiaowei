package net.qixiaowei.operate.cloud.api.domain.targetManager;


import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 目标制定收入表
 *
 * @author Graves
 * @since 2022-10-31
 */
@Data
@Accessors(chain = true)
public class TargetSettingIncome extends BaseEntity {

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
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;

}

