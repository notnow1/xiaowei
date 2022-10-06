package net.qixiaowei.operate.cloud.api.domain.performance;


import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 绩效等级系数
 *
 * @author Graves
 * @since 2022-10-06
 */
@Data
@Accessors(chain = true)
public class PerformanceRankFactor extends BaseEntity {

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
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;

}

