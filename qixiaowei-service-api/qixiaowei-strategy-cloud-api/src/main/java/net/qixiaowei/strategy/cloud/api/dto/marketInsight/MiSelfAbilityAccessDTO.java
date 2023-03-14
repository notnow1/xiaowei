package net.qixiaowei.strategy.cloud.api.dto.marketInsight;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import java.util.Map;

/**
* 市场洞察自身能力评估表
* @author TANGMICHI
* @since 2023-03-13
*/
@Data
@Accessors(chain = true)
public class MiSelfAbilityAccessDTO extends BaseDTO {

    //查询检验
    public interface QueryMiSelfAbilityAccessDTO extends Default{

    }
    //新增检验
    public interface AddMiSelfAbilityAccessDTO extends Default{

    }

    //删除检验
    public interface DeleteMiSelfAbilityAccessDTO extends Default{

    }
    //修改检验
    public interface UpdateMiSelfAbilityAccessDTO extends Default{

    }
    /**
    * ID
    */
    private  Long miSelfAbilityAccessId;
    /**
    * 市场洞察自身ID
    */
    private  Long marketInsightSelfId;
    /**
    * 能力要素
    */
    private  Long capacityFactor;
    /**
     * 能力要素名称
     */
    private  String capacityFactorName;
    /**
    * 现状描述
    */
    private  String descriptionActuality;
    /**
    * 能力评估分数
    */
    private  BigDecimal abilityAssessScore;
    /**
    * 战略控制点标记:0否;1是
    */
    private  Integer strategyControlPointFlag;
    /**
    * 排序
    */
    private  Integer sort;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 租户ID
    */
    private  Long tenantId;
}

