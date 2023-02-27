package net.qixiaowei.operate.cloud.api.domain.targetManager;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 目标分解表
 *
 * @author TANGMICHI
 * @since 2022-10-27
 */
@Data
@Accessors(chain = true)
public class TargetDecompose extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long targetDecomposeId;
    /**
     * ID集合
     */
    private  List<Long> targetDecomposeIds;
    /**
     * 目标分解类型:0自定义;1销售订单;2销售收入;3销售回款
     */
    private Integer targetDecomposeType;
    /**
     * 指标ID
     */
    private Long indicatorId;
    /**
     * 指标名称
     */
    private String indicatorName;
    /**
     * 目标年度
     */
    private Integer targetYear;
    /**
     * 目标分解维度ID
     */
    private Long targetDecomposeDimensionId;
    /**
     * 分解维度
     */
    private String decompositionDimension;

    /**
     * 指标ID集合
     */
    private List<Long> indicatorIds;

    /**
     * 远程指标ID集合
     */
    private List<String> remoteIndicatorIds;
    /**
     * 负责人ID
     */
    private Long principalEmployeeId;
    /**
     * 负责人名称
     */
    private String principalEmployeeName;
    /**
     * 时间维度:1年度;2半年度;3季度;4月度;5周
     */
    private Integer timeDimension;
    /**
     * 分解目标值
     */
    private BigDecimal decomposeTarget;
    /**
     * 年度预测值
     */
    private BigDecimal forecastYear;
    /**
     * 累计实际值
     */
    private BigDecimal actualTotal;
    /**
     * 状态:0待录入;1已录入
     */
    private Integer status;

}

