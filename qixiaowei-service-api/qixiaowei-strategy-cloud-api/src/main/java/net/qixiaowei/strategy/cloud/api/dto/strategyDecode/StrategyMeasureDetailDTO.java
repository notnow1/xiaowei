package net.qixiaowei.strategy.cloud.api.dto.strategyDecode;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Map;

/**
* 战略举措清单详情表
* @author Graves
* @since 2023-03-07
*/
@Data
@Accessors(chain = true)
public class StrategyMeasureDetailDTO {

    //查询检验
    public interface QueryStrategyMeasureDetailDTO extends Default{

    }
    //新增检验
    public interface AddStrategyMeasureDetailDTO extends Default{

    }

    //删除检验
    public interface DeleteStrategyMeasureDetailDTO extends Default{

    }
    //修改检验
    public interface UpdateStrategyMeasureDetailDTO extends Default{

    }
    /**
    * ID
    */
    private  Long strategyMeasureDetailId;
    /**
    * 战略举措清单ID
    */
    private  Long strategyMeasureId;
    /**
    * 战略指标维度ID
    */
    private  Long strategyIndexDimensionId;
    /**
    * 序列号
    */
    private  Integer serialNumber;
    /**
    * 战略举措名称
    */
    private  String strategyMeasureName;
    /**
    * 战略举措来源
    */
    private  Long strategyMeasureSource;
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

