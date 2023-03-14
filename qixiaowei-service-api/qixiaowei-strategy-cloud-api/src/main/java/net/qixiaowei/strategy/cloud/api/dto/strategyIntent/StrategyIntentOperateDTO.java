package net.qixiaowei.strategy.cloud.api.dto.strategyIntent;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import java.util.List;
import java.util.Map;

/**
* 战略意图经营表
* @author TANGMICHI
* @since 2023-02-23
*/
@Data
@Accessors(chain = true)
public class StrategyIntentOperateDTO extends BaseDTO {

    //查询检验
    public interface QueryStrategyIntentOperateDTO extends Default{

    }
    //新增检验
    public interface AddStrategyIntentOperateDTO extends Default{

    }

    //删除检验
    public interface DeleteStrategyIntentOperateDTO extends Default{

    }
    //修改检验
    public interface UpdateStrategyIntentOperateDTO extends Default{

    }
    /**
    * ID
    */
    private  Long strategyIntentOperateId;
    /**
    * 战略意图ID
    */
    private  Long strategyIntentId;
    /**
    * 指标ID
    */
    private  Long indicatorId;
    /**
    * 指标名称
    */
    private  String indicatorName;
    /**
    * 经营年度
    */
    private  Integer operateYear;
    /**
    * 经营值
    */
    private  BigDecimal operateValue;
    /**
    * 排序
    */
    private  Integer sort;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
     * 指标值类型:1金额;2比率
     */
    private Integer indicatorValueType;



    /**
     * 年度指标对应值集合
     */
    private List<StrategyIntentOperateMapDTO> strategyIntentOperateMapDTOS;
}

