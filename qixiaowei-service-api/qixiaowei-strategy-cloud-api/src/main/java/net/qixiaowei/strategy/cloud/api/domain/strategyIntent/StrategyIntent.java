package net.qixiaowei.strategy.cloud.api.domain.strategyIntent;


import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;

import java.math.BigDecimal;

import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 战略意图表
 *
 * @author TANGMICHI
 * @since 2023-02-23
 */
@Data
@Accessors(chain = true)
public class StrategyIntent extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long strategyIntentId;
    /**
     * 规划年度
     */
    private Integer planYear;
    /**
     * 愿景
     */
    private String vision;
    /**
     * 使命
     */
    private String mission;
    /**
     * 战略定位
     */
    private String strategyTarget;
    /**
     * 战略目标
     */
    private String strategyPosition;
    /**
     * 经营规划期
     */
    private Integer operatePlanPeriod;
    /**
     * 经营历史年份
     */
    private Integer operateHistoryYear;
    /**
     * 创建人名称
     */
    private String createByName;

}

