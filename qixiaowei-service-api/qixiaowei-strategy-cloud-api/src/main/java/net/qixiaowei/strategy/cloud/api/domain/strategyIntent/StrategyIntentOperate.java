package net.qixiaowei.strategy.cloud.api.domain.strategyIntent;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 战略意图经营表
* @author TANGMICHI
* @since 2023-02-23
*/
@Data
@Accessors(chain = true)
public class StrategyIntentOperate extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  strategyIntentOperateId;
     /**
     * 战略意图ID
     */
     private  Long  strategyIntentId;
     /**
     * 指标ID
     */
     private  Long  indicatorId;
     /**
     * 指标名称
     */
     private  String  indicatorName;
     /**
     * 经营年度
     */
     private  Integer  operateYear;
     /**
     * 经营值
     */
     private  BigDecimal  operateValue;
     /**
     * 排序
     */
     private  Integer  sort;

}

