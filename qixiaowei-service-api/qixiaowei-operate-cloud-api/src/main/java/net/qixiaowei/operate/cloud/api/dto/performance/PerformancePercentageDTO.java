package net.qixiaowei.operate.cloud.api.dto.performance;

import java.math.BigDecimal;
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
import net.qixiaowei.integration.common.domain.dto.BaseDTO;
import net.qixiaowei.operate.cloud.api.dto.product.ProductUnitDTO;

/**
 * 绩效比例表
 *
 * @author Graves
 * @since 2022-10-10
 */
@Data
@Accessors(chain = true)
public class PerformancePercentageDTO extends BaseDTO {

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
    @NotNull(message = "绩效比例id不能为空", groups = {PerformancePercentageDTO.UpdatePerformancePercentageDTO.class, PerformancePercentageDTO.DeletePerformancePercentageDTO.class})
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
     * 组织绩效等级名称
     */
    private String orgPerformanceRankName;
    /**
     * 个人绩效等级ID
     */
    @NotNull(message = "个人绩效等级ID", groups = {PerformancePercentageDTO.AddPerformancePercentageDTO.class})
    private Long personPerformanceRankId;
    /**
     * 个人绩效等级名称
     */
    private String personPerformanceRankName;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 组织菜单
     */
    private List<String> orgMenu;
    /**
     * 个人菜单{personId,personName}
     */
    private List<Map<String, String>> personMenu;
    /**
     * 绩效比例信息
     */
    private List<Map<String, BigDecimal>> informationList;
    /**
     * 接收比例信息
     */
    private List<Map<String, String>> receiveList;
}

