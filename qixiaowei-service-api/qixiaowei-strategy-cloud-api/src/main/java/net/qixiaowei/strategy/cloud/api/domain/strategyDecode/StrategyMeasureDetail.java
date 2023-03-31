package net.qixiaowei.strategy.cloud.api.domain.strategyDecode;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
 * 战略举措清单详情表
 *
 * @author Graves
 * @since 2023-03-07
 */
@Data
@Accessors(chain = true)
public class StrategyMeasureDetail extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long strategyMeasureDetailId;
    /**
     * 战略举措清单ID
     */
    private Long strategyMeasureId;
    /**
     * 战略指标维度ID
     */
    private Long strategyIndexDimensionId;
    /**
     * 序列号
     */
    private Integer serialNumber;
    /**
     * 战略举措名称
     */
    private String strategyMeasureName;
    /**
     * 战略举措来源
     */
    private Long strategyMeasureSource;
    /**
     * 排序
     */
    private Integer sort;

}

