package net.qixiaowei.strategy.cloud.api.domain.strategyDecode;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 战略衡量指标规划表
* @author Graves
* @since 2023-03-07
*/
@Data
@Accessors(chain = true)
public class StrategyMetricsPlan extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  strategyMetricsPlanId;
     /**
     * 战略衡量指标ID
     */
     private  Long  strategyMetricsId;
     /**
     * 战略衡量指标详情ID
     */
     private  Long  strategyMetricsDetailId;
     /**
     * 规划年度
     */
     private  Integer  planYear;
     /**
     * 规划值
     */
     private  BigDecimal  planValue;

}

