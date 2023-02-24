package net.qixiaowei.strategy.cloud.api.dto.strategyIntent;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;

import java.util.List;
import java.util.Map;

/**
* 战略意图表
* @author TANGMICHI
* @since 2023-02-23
*/
@Data
@Accessors(chain = true)
public class StrategyIntentDTO {

    //查询检验
    public interface QueryStrategyIntentDTO extends Default{

    }
    //新增检验
    public interface AddStrategyIntentDTO extends Default{

    }

    //删除检验
    public interface DeleteStrategyIntentDTO extends Default{

    }
    //修改检验
    public interface UpdateStrategyIntentDTO extends Default{

    }
    /**
    * ID
    */
    @NotNull(message = "id不能为空", groups = {StrategyIntentDTO.DeleteStrategyIntentDTO.class,StrategyIntentDTO.UpdateStrategyIntentDTO.class})
    private  Long strategyIntentId;
    /**
    * 规划年度
    */
    @NotNull(message = "规划年度不能为空", groups = {StrategyIntentDTO.AddStrategyIntentDTO.class,StrategyIntentDTO.UpdateStrategyIntentDTO.class})
    private  Integer planYear;
    /**
    * 愿景
    */
    private  String vision;
    /**
    * 使命
    */
    private  String mission;
    /**
    * 战略定位
    */
    private  String strategyTarget;
    /**
    * 战略目标
    */
    private  String strategyPosition;
    /**
    * 经营规划期
    */
    private  Integer operatePlanPeriod;
    /**
    * 经营历史年份
    */
    private  Integer operateHistoryYear;
    /**
     * 战略意图经营表
     */
    private List<StrategyIntentOperateDTO> strategyIntentOperateDTOS;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 创建人
    */
    private  Long createBy;
    /**
     * 创建人名称
     */
    private  String createByName;
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
    * 请求参数
    */
    private Map<String, Object> params;
}
