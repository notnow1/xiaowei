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
* 行业吸引力要素表
* @author TANGMICHI
* @since 2023-02-20
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class IndustryAttractionElement extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  industryAttractionElementId;
     /**
     * 行业吸引力ID
     */
     private  Long  industryAttractionId;
     /**
     * 评估标准名称
     */
     private  String  assessStandardName;
     /**
     * 评估标准说明
     */
     private  String  assessStandardDescription;
     /**
     * 显示颜色
     */
     private  String  displayColor;
     /**
     * 排序
     */
     private  Integer  sort;
     /**
     * 状态:0失效;1生效
     */
     private  Integer  status;

}

