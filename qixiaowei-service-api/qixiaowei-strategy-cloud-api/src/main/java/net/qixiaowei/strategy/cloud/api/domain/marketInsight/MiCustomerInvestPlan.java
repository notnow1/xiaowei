package net.qixiaowei.strategy.cloud.api.domain.marketInsight;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 市场洞察客户投资计划表
* @author TANGMICHI
* @since 2023-03-07
*/
@Data
@Accessors(chain = true)
public class MiCustomerInvestPlan extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  miCustomerInvestPlanId;
     /**
     * 市场洞察客户ID
     */
     private  Long  marketInsightCustomerId;
     /**
     * 行业ID
     */
     private  Long  industryId;
     /**
     * 客户名称
     */
     private  String  customerName;
     /**
     * 客户类别
     */
     private  Long  customerCategory;
     /**
     * 现有市场占有率
     */
     private  BigDecimal  existMarketShare;
     /**
     * 上年销售额
     */
     private  BigDecimal  previousYearSales;
     /**
     * 规划期
     */
     private  Integer  planPeriod;
     /**
     * 未来可参与市场空间
     */
     private  BigDecimal  futurePartMarketSpace;
     /**
     * 排序
     */
     private  Integer  sort;

}

