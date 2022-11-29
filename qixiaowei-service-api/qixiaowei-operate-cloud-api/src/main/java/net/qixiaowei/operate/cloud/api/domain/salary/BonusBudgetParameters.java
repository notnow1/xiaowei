package net.qixiaowei.operate.cloud.api.domain.salary;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
* 奖金预算参数表
* @author TANGMICHI
* @since 2022-11-26
*/
@Data
@Accessors(chain = true)
public class BonusBudgetParameters extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  bonusBudgetParametersId;
     /**
     * 奖金预算ID
     */
     private  Long  bonusBudgetId;
     /**
     * 指标ID
     */
     private  Long  indicatorId;
     /**
     * 奖金权重(%)
     */
     private  BigDecimal  bonusWeight;
     /**
     * 奖金占比基准值(%)
     */
     private BigDecimal bonusProportionStandard;
     /**
     * 奖金占比浮动差值
     */
     private  BigDecimal  bonusProportionVariation;
     /**
     * 挑战值
     */
     private  BigDecimal  challengeValue;
     /**
     * 目标值
     */
     private  BigDecimal  targetValue;
     /**
     * 保底值
     */
     private  BigDecimal  guaranteedValue;
     /**
     * 预计目标达成率(%)
     */
     private  BigDecimal  targetCompletionRate;
     /**
     * 预算年后一年业绩增长率
     */
     private  BigDecimal  performanceAfterOne;
     /**
     * 预算年后二年业绩增长率
     */
     private  BigDecimal  performanceAfterTwo;
     /**
     * 预算年后一年奖金折让系数
     */
     private  BigDecimal  bonusAllowanceAfterOne;
     /**
     * 预算年后二年奖金折让系数
     */
     private  BigDecimal  bonusAllowanceAfterTwo;

}

