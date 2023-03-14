package net.qixiaowei.strategy.cloud.api.dto.marketInsight;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import java.util.List;
import java.util.Map;

/**
* 市场洞察对手选择表
* @author TANGMICHI
* @since 2023-03-12
*/
@Data
@Accessors(chain = true)
public class MiOpponentChoiceDTO extends BaseDTO {

    //查询检验
    public interface QueryMiOpponentChoiceDTO extends Default{

    }
    //新增检验
    public interface AddMiOpponentChoiceDTO extends Default{

    }

    //删除检验
    public interface DeleteMiOpponentChoiceDTO extends Default{

    }
    //修改检验
    public interface UpdateMiOpponentChoiceDTO extends Default{

    }
    /**
    * ID
    */
    private  Long miOpponentChoiceId;
    /**
    * 市场洞察对手ID
    */
    private  Long marketInsightOpponentId;
    /**
    * 行业ID
    */
    private  Long industryId;
    /**
     * 行业名称
     */
    private  String industryName;
    /**
    * 对手名称
    */
    private  String opponentName;
    /**
    * 对比项目
    */
    private  Long comparisonItem;
    /**
     * 对比项目名称
     */
    private  String comparisonItemName;
    /**
    * 能力评估分数
    */
    private  BigDecimal abilityAssessScore;
    /**
    * 对手核心能力分析
    */
    private  String analysisOpponentCoreAbility;
    /**
    * 自身优势
    */
    private  String ownAdvantage;
    /**
    * 自身劣势
    */
    private  String ownDisadvantage;
    /**
    * 竞争对手类别
    */
    private  Long competitorCategory;

    /**
     * 竞争对手类别名称
     */
    private  String competitorCategoryName;
    /**
    * 竞争战略类型
    */
    private  Long competitionStrategyType;
    /**
     * 竞争战略类型名称
     */
    private  String competitionStrategyTypeName;
    /**
    * 经营历史期
    */
    private  Integer operateHistoryPeriod;
    /**
    * 排序
    */
    private  Integer sort;
    /**
     * 对手财务具体经营值集合
     */
    private List<MiOpponentFinanceDTO> miOpponentFinanceDTOS;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 租户ID
    */
    private  Long tenantId;
}

