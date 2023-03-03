package net.qixiaowei.strategy.cloud.api.domain.marketInsight;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 市场洞察行业吸引力表
* @author TANGMICHI
* @since 2023-03-03
*/
@Data
@Accessors(chain = true)
public class MiIndustryAttraction extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  miIndustryAttractionId;
     /**
     * 市场洞察行业ID
     */
     private  Long  marketInsightIndustryId;
     /**
     * 行业吸引力ID
     */
     private  Long  industryAttractionId;
     /**
     * 排序
     */
     private  Integer  sort;

}

