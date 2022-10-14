package net.qixiaowei.operate.cloud.api.dto.performance;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 绩效比例数据表
* @author Graves
* @since 2022-10-10
*/
@Data
@Accessors(chain = true)
public class PerformancePercentageDataDTO {

    //查询检验
    public interface QueryPerformancePercentageDataDTO extends Default{

    }
    //新增检验
    public interface AddPerformancePercentageDataDTO extends Default{

    }

    //删除检验
    public interface DeletePerformancePercentageDataDTO extends Default{

    }
    //修改检验
    public interface UpdatePerformancePercentageDataDTO extends Default{

    }
    /**
    * ID
    */
    private  Long performancePercentageDataId;
    /**
    * 绩效比例ID
    */
    private  Long performancePercentageId;
    /**
    * 组织绩效等级系数ID
    */
    private  Long orgRankFactorId;
    /**
    * 个人绩效等级系数ID
    */
    private  Long personRankFactorId;
    /**
    * 数值,单位:百分号%
    */
    private BigDecimal value;
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
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

