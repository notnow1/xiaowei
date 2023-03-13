package net.qixiaowei.strategy.cloud.api.dto.marketInsight;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Map;

/**
* 市场洞察自身能力评估表
* @author TANGMICHI
* @since 2023-03-13
*/
@Data
@Accessors(chain = true)
public class MiSelfAbilityAccessDTO {

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
    * 创建人
    */
    private  Long createBy;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;
    /**
    * 租户ID
    */
    private  Long tenantId;

    /**
    * 请求参数
    */
    private Map<String, Object> params;
}

