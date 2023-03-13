package net.qixiaowei.strategy.cloud.api.domain.marketInsight;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 市场洞察对手选择表
* @author TANGMICHI
* @since 2023-03-12
*/
@Data
@Accessors(chain = true)
public class MiOpponentChoice extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  miOpponentChoiceId;
     /**
     * 市场洞察对手ID
     */
     private  Long  marketInsightOpponentId;
     /**
     * 行业ID
     */
     private  Long  industryId;
     /**
     * 对手名称
     */
     private  String  opponentName;
     /**
     * 对比项目
     */
     private  Long  comparisonItem;
     /**
     * 能力评估分数
     */
     private  BigDecimal  abilityAssessScore;
     /**
     * 对手核心能力分析
     */
     private  String  analysisOpponentCoreAbility;
     /**
     * 自身优势
     */
     private  String  ownAdvantage;
     /**
     * 自身劣势
     */
     private  String  ownDisadvantage;
     /**
     * 竞争对手类别
     */
     private  Long  competitorCategory;
     /**
     * 竞争战略类型
     */
     private  Long  competitionStrategyType;
     /**
     * 经营历史期
     */
     private  Integer  operateHistoryPeriod;
     /**
     * 排序
     */
     private  Integer  sort;

}

