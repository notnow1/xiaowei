package net.qixiaowei.strategy.cloud.api.domain.marketInsight;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 市场洞察客户投资详情表
* @author TANGMICHI
* @since 2023-03-07
*/
@Data
@Accessors(chain = true)
public class MiCustomerInvestDetail extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  miCustomerInvestDetailId;
     /**
     * 市场洞察客户ID
     */
     private  Long  marketInsightCustomerId;
     /**
     * 市场洞察客户投资计划ID
     */
     private  Long  miCustomerInvestPlanId;
     /**
     * 规划年度
     */
     private  Integer  planYear;
     /**
     * 产品ID
     */
     private  Long  productId;
     /**
     * 年需求总量
     */
     private  String  totalAnnualDemand;
     /**
     * 客户投资计划金额
     */
     private  BigDecimal  customerInvestPlanAmount;
     /**
     * 预计市场占有率
     */
     private  BigDecimal  estimateMarketShare;
     /**
     * 排序
     */
     private  Integer  sort;

}

