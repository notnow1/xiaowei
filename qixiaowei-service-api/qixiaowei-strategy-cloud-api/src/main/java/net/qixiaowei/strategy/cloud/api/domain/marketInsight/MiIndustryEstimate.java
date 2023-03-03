package net.qixiaowei.strategy.cloud.api.domain.marketInsight;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 市场洞察行业预估表
* @author TANGMICHI
* @since 2023-03-03
*/
@Data
@Accessors(chain = true)
public class MiIndustryEstimate extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  miMacroEstimateId;
     /**
     * 市场洞察行业ID
     */
     private  Long  marketInsightIndustryId;
     /**
     * 市场洞察行业详情ID
     */
     private  Long  miIndustryDetailId;
     /**
     * 规划年度
     */
     private  Integer  planYear;
     /**
     * 整体空间金额
     */
     private  BigDecimal  overallSpaceAmount;
     /**
     * 可参与空间金额
     */
     private  BigDecimal  participateSpaceAmount;
     /**
     * 目标市场空间金额
     */
     private  BigDecimal  targetMarketSpaceAmount;

}

