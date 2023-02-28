package net.qixiaowei.strategy.cloud.api.domain.marketInsight;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 市场洞察宏观预估表
* @author TANGMICHI
* @since 2023-02-28
*/
@Data
@Accessors(chain = true)
public class MiMacroEstimate extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  miMacroEstimateId;
     /**
     * 市场洞察宏观ID
     */
     private  Long  marketInsightMacroId;
     /**
     * 市场洞察宏观详情ID
     */
     private  Long  miMacroDetailId;
     /**
     * 规划年度
     */
     private  Integer  planYear;
     /**
     * 预估机会点金额
     */
     private  BigDecimal  estimateOpportunityAmount;

}

