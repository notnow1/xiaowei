package net.qixiaowei.operate.cloud.api.dto.performance;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.operate.cloud.api.dto.product.ProductUnitDTO;

/**
 * 绩效比例表
 *
 * @author Graves
 * @since 2022-10-10
 */
@Data
@Accessors(chain = true)
public class PerformancePercentageDTO {

    //查询检验
    public interface QueryPerformancePercentageDTO extends Default {

    }

    //新增检验
    public interface AddPerformancePercentageDTO extends Default {

    }

    //删除检验
    public interface DeletePerformancePercentageDTO extends Default {

    }

    //修改检验
    public interface UpdatePerformancePercentageDTO extends Default {

    }

    /**
     * ID
     */
    private Long performancePercentageId;
    /**
     * 绩效比例名称
     */
    @NotBlank(message = "绩效比例名称不能为空", groups = {PerformancePercentageDTO.AddPerformancePercentageDTO.class, PerformancePercentageDTO.UpdatePerformancePercentageDTO.class})
    @Size(min = 0, max = 30, message = "绩效比例名称不能超过30个字符")
    private String performancePercentageName;
    /**
     * 组织绩效等级ID
     */
    @NotNull(message = "组织绩效等级ID", groups = {PerformancePercentageDTO.AddPerformancePercentageDTO.class})
    private Long orgPerformanceRankId;
    /**
     * 个人绩效等级ID
     */
    @NotNull(message = "个人绩效等级ID", groups = {PerformancePercentageDTO.AddPerformancePercentageDTO.class})
    private Long personPerformanceRankId;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 绩效比例信息
     */
    private List<Map<Long, String>> informationList;

}

