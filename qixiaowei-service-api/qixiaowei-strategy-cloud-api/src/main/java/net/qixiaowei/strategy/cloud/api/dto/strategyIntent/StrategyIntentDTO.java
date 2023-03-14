package net.qixiaowei.strategy.cloud.api.dto.strategyIntent;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
import java.util.Map;

/**
* 战略意图表
* @author TANGMICHI
* @since 2023-02-23
*/
@Data
@Accessors(chain = true)
public class StrategyIntentDTO extends BaseDTO {

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
}

