package net.qixiaowei.operate.cloud.api.domain.performance;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
 * 绩效等级系数
 *
 * @author Graves
 * @since 2022-10-06
 */
@Data
@Accessors(chain = true)
public class PerformanceRankFactor extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long performanceRankFactorId;
    /**
     * 绩效等级ID
     */
    private Long performanceRankId;
    /**
     * 绩效等级名称
     */
    private String performanceRankName;
    /**
     * 奖金系数
     */
    private BigDecimal bonusFactor;
    /**
     * 排序
     */
    private Integer sort;


}

