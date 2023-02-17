package net.qixiaowei.strategy.cloud.api.dto.plan;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 规划业务单元
* @author wangchen
* @since 2023-02-17
*/
@Data
@Accessors(chain = true)
public class PlanBusinessUnitDTO {

    //查询检验
    public interface QueryPlanBusinessUnitDTO extends Default{

    }
    //新增检验
    public interface AddPlanBusinessUnitDTO extends Default{

    }

    //删除检验
    public interface DeletePlanBusinessUnitDTO extends Default{

    }
    //修改检验
    public interface UpdatePlanBusinessUnitDTO extends Default{

    }
    /**
    * ID
    */
    private  Long planBusinessUnitId;
    /**
    * 规划业务单元编码
    */
    private  String businessUnitCode;
    /**
    * 规划业务单元名称
    */
    private  String businessUnitName;
    /**
    * 规划业务单元维度(region,department,product,industry)
    */
    private  String businessUnitDecompose;
    /**
    * 状态:0失效;1生效
    */
    private  Integer status;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;
    /**
    * 租户ID
    */
    private  Long tenantId;

}

