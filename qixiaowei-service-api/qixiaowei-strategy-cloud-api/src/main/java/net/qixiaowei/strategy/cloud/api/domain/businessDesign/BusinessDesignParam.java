package net.qixiaowei.strategy.cloud.api.domain.businessDesign;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 业务设计参数表
* @author Graves
* @since 2023-02-28
*/
@Data
@Accessors(chain = true)
public class BusinessDesignParam extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  businessDesignParamId;
     /**
     * 业务设计ID
     */
     private  Long  businessDesignId;
     /**
     * 参数维度:1产品;2客户;3区域
     */
     private  Integer  paramDimension;
     /**
     * 参数关联ID
     */
     private  Long  paramRelationId;
     /**
     * 参数名称
     */
     private  String  paramName;
     /**
     * 历史平均毛利率
     */
     private  BigDecimal  historyAverageRate;
     /**
     * 历史权重
     */
     private  BigDecimal  historyWeight;
     /**
     * 预测毛利率
     */
     private  BigDecimal  forecastRate;
     /**
     * 预测权重
     */
     private  BigDecimal  forecastWeight;
     /**
     * 历史订单额
     */
     private  BigDecimal  historyOrderAmount;
     /**
     * 历史订单权重
     */
     private  BigDecimal  historyOrderWeight;
     /**
     * 预测订单额
     */
     private  BigDecimal  forecastOrderAmount;
     /**
     * 预测订单权重
     */
     private  BigDecimal  forecastOrderWeight;
     /**
     * 排序
     */
     private  Integer  sort;

}

