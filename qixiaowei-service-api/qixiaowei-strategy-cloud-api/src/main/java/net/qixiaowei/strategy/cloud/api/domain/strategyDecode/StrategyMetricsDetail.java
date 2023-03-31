package net.qixiaowei.strategy.cloud.api.domain.strategyDecode;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 战略衡量指标详情表
* @author Graves
* @since 2023-03-07
*/
@Data
@Accessors(chain = true)
public class StrategyMetricsDetail extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  strategyMetricsDetailId;
     /**
     * 战略衡量指标ID
     */
     private  Long  strategyMetricsId;
     /**
     * 战略指标维度ID
     */
     private  Long  strategyIndexDimensionId;
     /**
     * 序列号
     */
     private  Integer  serialNumber;
     /**
     * 战略举措名称
     */
     private  String  strategyMeasureName;
     /**
     * 指标ID
     */
     private  Long  indicatorId;
     /**
     * 排序
     */
     private  Integer  sort;

}

