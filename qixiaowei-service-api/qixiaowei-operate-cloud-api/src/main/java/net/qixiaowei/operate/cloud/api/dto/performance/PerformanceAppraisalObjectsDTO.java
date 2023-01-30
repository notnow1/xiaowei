package net.qixiaowei.operate.cloud.api.dto.performance;

import java.awt.*;
import java.time.LocalDate;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 绩效考核对象表
 *
 * @author Graves
 * @since 2022-12-05
 */
@Data
@Accessors(chain = true)
public class PerformanceAppraisalObjectsDTO {

    //查询检验
    public interface QueryPerformanceAppraisalObjectsDTO extends Default {

    }

    //新增检验
    public interface AddPerformanceAppraisalObjectsDTO extends Default {

    }

    //删除检验
    public interface DeletePerformanceAppraisalObjectsDTO extends Default {

    }

    //修改检验
    public interface UpdatePerformanceAppraisalObjectsDTO extends Default {

    }

    /**
     * ID
     */
    private Long performAppraisalObjectsId;
    /**
     * 考核名称
     */
    private String appraisalName;
    /**
     * 考核年度
     */
    private Integer appraisalYear;
    /**
     * 考核周期
     */
    private Integer cycleNumber;
    /**
     * 考核周期名称
     */
    private String cycleNumberName;
    /**
     * 周期类型:1月度;2季度;3半年度;4年度
     */
    private Integer cycleType;
    /**
     * 周期类型:1月度;2季度;3半年度;4年度
     */
    private String CycleTypeName;
    /**
     * 名次
     */
    private Integer rank;
    /**
     * 绩效考核ID
     */
    private Long performanceAppraisalId;
    /**
     * 绩效考核名称
     */
    private String performanceAppraisalName;
    /**
     * 考核对象ID
     */
    private Long appraisalObjectId;
    /**
     * 考核对象编码
     */
    private String appraisalObjectCode;
    /**
     * 考核对象名称
     */
    private String appraisalObjectName;
    /**
     * 部门ID
     */
    private Long departmentId;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 岗位ID
     */
    private Long postId;
    /**
     * 岗位名称
     */
    private String postName;
    /**
     * 职级体系ID
     */
    private Long officialRankSystemId;
    /**
     * 职级
     */
    private Integer officialRank;
    /**
     * 职级名称
     */
    private String officialRankName;

    /**
     * 考核负责人ID
     */
    private Long appraisalPrincipalId;
    /**
     * 考核负责人姓名
     */
    private String appraisalPrincipalName;
    /**
     * 评议分数
     */
    private BigDecimal evaluationScore;
    /**
     * 考核结果(绩效等级系数ID)
     */
    private Long appraisalResultId;
    /**
     * 考核结果
     */
    private String appraisalResult;
    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private LocalDate appraisalStartDate;
    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private LocalDate appraisalEndDate;
    /**
     * 绩效等级ID
     */
    private Long performanceRankId;
    /**
     * 绩效等级名称
     */
    private String performanceRankName;
    /**
     * 考核对象状态:1待制定目标;2已制定目标-草稿;3待评议;4已评议-草稿;5待排名
     */
    private Integer appraisalObjectStatus;
    /**
     * 考核对象状态列表
     */
    private List<Integer> appraisalObjectStatusList;
    /**
     * 考核任务ID集合
     */
    private List<Long> performanceAppraisalIds;
    /**
     * 考核对象状态:1待制定目标;2已制定目标-草稿;3待评议;4已评议-草稿;5待排名
     */
    private String appraisalObjectStatusName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否提交（1-提交，0-保存）
     */
    private Integer isSubmit;
    /**
     * 考核比例统计
     */
    private List<Map<String, Object>> performanceAppraisalRankDTOS;
    /**
     * 制定指标信息
     */
    private List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDTOS;
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

}

