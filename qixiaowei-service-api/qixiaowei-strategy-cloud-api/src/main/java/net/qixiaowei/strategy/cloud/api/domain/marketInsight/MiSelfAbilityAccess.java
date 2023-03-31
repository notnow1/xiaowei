package net.qixiaowei.strategy.cloud.api.domain.marketInsight;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 市场洞察自身能力评估表
* @author TANGMICHI
* @since 2023-03-13
*/
@Data
@Accessors(chain = true)
public class MiSelfAbilityAccess extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  miSelfAbilityAccessId;
     /**
     * 市场洞察自身ID
     */
     private  Long  marketInsightSelfId;
     /**
     * 能力要素
     */
     private  Long  capacityFactor;
     /**
     * 现状描述
     */
     private  String  descriptionActuality;
     /**
     * 能力评估分数
     */
     private  BigDecimal  abilityAssessScore;
     /**
     * 战略控制点标记:0否;1是
     */
     private  Integer  strategyControlPointFlag;
     /**
     * 排序
     */
     private  Integer  sort;

}

