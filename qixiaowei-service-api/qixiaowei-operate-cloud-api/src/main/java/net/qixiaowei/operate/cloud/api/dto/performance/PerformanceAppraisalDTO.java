package net.qixiaowei.operate.cloud.api.dto.performance;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

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
 * @since 2022-12-05
 */
@Data
@Accessors(chain = true)
public class PerformanceAppraisalDTO extends BaseDTO {

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
    @NotNull(message = "请选择考核年度", groups = {PerformancePercentageDTO.AddPerformancePercentageDTO.class})
    private Integer appraisalYear;
    /**
     * 考核名称
     */
    @NotBlank(message = "请输入考核名称", groups = {PerformancePercentageDTO.AddPerformancePercentageDTO.class})
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
     * 评议周期类型:1月度;2季度;3半年度;4年度
     */
    private Integer evaluationType;
    /**
     * 评议周期类型:1月度;2季度;3半年度;4年度
     */
    private String evaluationTypeName;
    /**
     * 考核开始日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private LocalDate appraisalStartDate;
    /**
     * 考核开始日期开始
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date appraisalStartDateStart;
    /**
     * 考核开始日期结束
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date appraisalStartDateEnd;
    /**
     * 考核结束日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private LocalDate appraisalEndDate;
    /**
     * 考核结束日期开始
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date appraisalEndDateStart;
    /**
     * 考核结束日期结束
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date appraisalEndDateEnd;
    /**
     * 归档日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private LocalDate filingDate;
    /**
     * 归档日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date filingDateStart;
    /**
     * 归档日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date filingDateEnd;
    /**
     * 是否归档(0-未归档，1-归档)
     */
    private Integer isFiling;
    /**
     * 导入导出类型(1系统流程2仅导入结果)
     */
    private Integer importType;
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
     * 自定义列标记:0否;1是
     */
    private Integer selfDefinedColumnsFlag;
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
     * 考核任务范围
     */
    @NotEmpty(message = "请选择考核任务范围", groups = {PerformancePercentageDTO.AddPerformancePercentageDTO.class})
    private List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS;
    /**
     * 考核比例统计
     */
    private List<Map<String, Object>> performanceAppraisalRankDTOS;
    /**
     * 是否提交（1  提交，0  保存）
     */
    private Integer isSubmit;
    /**
     * 组织ID集合
     */
    private List<Long> appraisalObjectsIds;
    /**
     * 查询类型(1-全部，2-一级组织，0-自定义)
     */
    private Integer queryType;
}

