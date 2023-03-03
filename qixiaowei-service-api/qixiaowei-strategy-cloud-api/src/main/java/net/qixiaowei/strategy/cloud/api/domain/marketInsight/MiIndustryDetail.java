package net.qixiaowei.strategy.cloud.api.domain.marketInsight;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 市场洞察行业详情表
* @author TANGMICHI
* @since 2023-03-03
*/
@Data
@Accessors(chain = true)
public class MiIndustryDetail extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  miIndustryDetailId;
     /**
     * 市场洞察行业ID
     */
     private  Long  marketInsightIndustryId;
     /**
     * 行业ID
     */
     private  Long  industryId;
     /**
     * 行业类型
     */
     private  Long  industryType;
     /**
     * 规划期
     */
     private  Integer  planPeriod;
     /**
     * 整体空间
     */
     private  BigDecimal  overallSpace;
     /**
     * 可参与空间
     */
     private  BigDecimal  participateSpace;
     /**
     * 目标市场空间
     */
     private  BigDecimal  targetMarketSpace;
     /**
     * 排序
     */
     private  Integer  sort;

}

