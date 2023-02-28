package net.qixiaowei.operate.cloud.api.vo.strategyIntent;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* 战略意图经营表
* @author TANGMICHI
* @since 2023-02-23
*/
@Data
@Accessors(chain = true)
public class StrategyIntentOperateVO {

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
     * 指标ID集合
     */
    private List<Long> indicatorIds;
    /**
     * 目标年度集合
     */
    private List<String> targetYears;
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
    * 请求参数
    */
    private Map<String, Object> params;
    /**
     * 年度指标对应值集合
     */
    private List<StrategyIntentOperateMapVO> strategyIntentOperateMapDTOS;
}

