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
* 战略举措清单表
* @author Graves
* @since 2023-03-07
*/
@Data
@Accessors(chain = true)
public class StrategyMeasureDTO {

    //查询检验
    public interface QueryStrategyMeasureDTO extends Default{

    }
    //新增检验
    public interface AddStrategyMeasureDTO extends Default{

    }

    //删除检验
    public interface DeleteStrategyMeasureDTO extends Default{

    }
    //修改检验
    public interface UpdateStrategyMeasureDTO extends Default{

    }
    /**
    * ID
    */
    private  Long strategyMeasureId;
    /**
    * 规划年度
    */
    private  Integer planYear;
    /**
    * 规划业务单元ID
    */
    private  Long planBusinessUnitId;
    /**
    * 规划业务单元维度(region,department,product,industry)
    */
    private  String businessUnitDecompose;
    /**
    * 区域ID
    */
    private  Long areaId;
    /**
    * 部门ID
    */
    private  Long departmentId;
    /**
    * 产品ID
    */
    private  Long productId;
    /**
    * 行业ID
    */
    private  Long industryId;
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

