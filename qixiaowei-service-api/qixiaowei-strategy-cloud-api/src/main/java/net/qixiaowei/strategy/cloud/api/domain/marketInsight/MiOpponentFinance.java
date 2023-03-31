package net.qixiaowei.strategy.cloud.api.domain.marketInsight;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 市场洞察对手财务表
* @author TANGMICHI
* @since 2023-03-12
*/
@Data
@Accessors(chain = true)
public class MiOpponentFinance extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  miOpponentFinanceId;
     /**
     * 市场洞察对手ID
     */
     private  Long  marketInsightOpponentId;
     /**
     * 市场洞察对手选择ID
     */
     private  Long  miOpponentChoiceId;
     /**
     * 指标ID
     */
     private  Long  indicatorId;
     /**
     * 经营年度
     */
     private  Integer  operateYear;
     /**
     * 经营值
     */
     private  BigDecimal  operateValue;
     /**
     * 排序
     */
     private  Integer  sort;

}

