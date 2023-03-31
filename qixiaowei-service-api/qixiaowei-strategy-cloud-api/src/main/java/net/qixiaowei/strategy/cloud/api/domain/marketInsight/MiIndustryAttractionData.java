package net.qixiaowei.strategy.cloud.api.domain.marketInsight;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 市场洞察行业吸引力数据表
* @author TANGMICHI
* @since 2023-03-03
*/
@Data
@Accessors(chain = true)
public class MiIndustryAttractionData extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  miIndustryAttractionDataId;
     /**
     * 市场洞察行业ID
     */
     private  Long  marketInsightIndustryId;
     /**
     * 市场洞察行业详情ID
     */
     private  Long  miIndustryDetailId;
     /**
     * 市场洞察行业吸引力ID
     */
     private  Long  miIndustryAttractionId;
     /**
     * 行业吸引力要素ID
     */
     private  Long  industryAttractionElementId;

}

