package net.qixiaowei.strategy.cloud.api.domain.industry;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 行业吸引力表
* @author TANGMICHI
* @since 2023-02-20
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class IndustryAttraction extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  industryAttractionId;
     /**
     * 行业吸引力要素名称
     */
     private  String  attractionElementName;
     /**
     * 状态:0失效;1生效
     */
     private  Integer  status;

}

