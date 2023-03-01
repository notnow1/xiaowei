package net.qixiaowei.strategy.cloud.api.domain.businessDesign;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 业务设计轴配置表
* @author Graves
* @since 2023-02-28
*/
@Data
@Accessors(chain = true)
public class BusinessDesignAxisConfig extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  businessDesignAxisConfigId;
     /**
     * 业务设计ID
     */
     private  Long  businessDesignId;
     /**
     * 参数维度:1产品;2客户;3区域
     */
     private  Integer  paramDimension;
     /**
     * 坐标轴:1 x轴;2 y轴
     */
     private  Integer  coordinateAxis;
     /**
     * 高区值
     */
     private  BigDecimal  upperValue;
     /**
     * 低区值
     */
     private  BigDecimal  lowerValue;

}

