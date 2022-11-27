package net.qixiaowei.operate.cloud.api.dto.performance;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 绩效考核表
 *
 * @author Graves
 * @since 2022-11-24
 */
@Data
@Accessors(chain = true)
public class PerformanceAppraisalDTO {

    //查询检验
    public interface QueryPerformanceAppraisalDTO extends Default {

    }

    //新增检验
    public interface AddPerformanceAppraisalDTO extends Default {

    }

    //删除检验
    public interface DeletePerformanceAppraisalDTO extends Default {

    }

    //修改检验
    public interface UpdatePerformanceAppraisalDTO extends Default {

    }

    /**
     * ID
     */
    private Long performanceAppraisalId;
    /**
     * 绩效等级ID
     */
    @NotNull(message = "绩效等级ID不能为空", groups = {PerformancePercentageDTO.AddPerformancePercentageDTO.class})
    private Long performanceRankId;
    /**
     * 绩效等级名称
     */
    private String performanceRankName;
    /**
     * 考核年度
     */
    @NotNull(message = "考核年度不能为空", groups = {PerformancePercentageDTO.AddPerformancePercentageDTO.class})
    private Integer appraisalYear;
    /**
     * 考核名称
     */
    @NotBlank(message = "考核名称不能为空", groups = {PerformancePercentageDTO.AddPerformancePercentageDTO.class})
    private String appraisalName;
    /**
     * 周期性考核标记:0否;1是
     */
    @NotNull(message = "请选择周期性考核标记", groups = {PerformancePercentageDTO.AddPerformancePercentageDTO.class})
    private Integer cycleFlag;
    /**
     * 周期类型:1月度;2季度;3半年度;4年度
     */
    @NotNull(message = "请选择周期类型", groups = {PerformancePercentageDTO.AddPerformancePercentageDTO.class})
    private Integer cycleType;
    /**
     * 周期类型:1月度;2季度;3半年度;4年度
     */
    private String cycleTypeName;
    /**
     * 考核周期
     */
    private Integer cycleNumber;
    /**
     * 考核周期名称
     */
    private String cycleNumberName;
    /**
     * 考核开始日期
     */
    private LocalDate appraisalStartDate;
    /**
     * 考核结束日期
     */
    private LocalDate appraisalEndDate;
    /**
     * 归档日期
     */
    private LocalDate filingDate;
    /**
     * 考核流程:1系统流程;2仅导入结果
     */
    private Integer appraisalFlow;
    /**
     * 考核流程:1系统流程;2仅导入结果
     */
    private String appraisalFlowName;
    /**
     * 考核对象:1组织;2员工
     */
    private Integer appraisalObject;
    /**
     * 考核状态:1制定目标;2评议;3排名;4归档
     */
    private Integer appraisalStatus;
    /**
     * 考核状态:1制定目标;2评议;3排名;4归档
     */
    private String appraisalStatusName;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 考核任务范围
     */
    @NotEmpty(message = "请选择考核任务范围", groups = {PerformancePercentageDTO.AddPerformancePercentageDTO.class})
    private List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS;
    /**
     * 考核比例统计
     */
    private List<Map<String, Object>> performanceAppraisalRankDTOS;

}

