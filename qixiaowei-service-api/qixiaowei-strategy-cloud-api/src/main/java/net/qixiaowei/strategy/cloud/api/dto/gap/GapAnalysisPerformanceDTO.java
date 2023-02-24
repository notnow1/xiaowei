package net.qixiaowei.strategy.cloud.api.dto.gap;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.util.Date;
import java.util.Map;

/**
 * 业绩差距表
 *
 * @author Graves
 * @since 2023-02-24
 */
@Data
@Accessors(chain = true)
public class GapAnalysisPerformanceDTO {

    //查询检验
    public interface QueryGapAnalysisPerformanceDTO extends Default {

    }

    //新增检验
    public interface AddGapAnalysisPerformanceDTO extends Default {

    }

    //删除检验
    public interface DeleteGapAnalysisPerformanceDTO extends Default {

    }

    //修改检验
    public interface UpdateGapAnalysisPerformanceDTO extends Default {

    }

    /**
     * ID
     */
    private Long gapAnalysisPerformanceId;
    /**
     * 差距分析ID
     */
    private Long gapAnalysisId;
    /**
     * 序列号
     */
    private Integer serialNumber;
    /**
     * 业绩差距名称
     */
    private String gapPerformanceName;
    /**
     * 差距描述
     */
    private String gapDescription;
    /**
     * 根因分析
     */
    private String rootCauseAnalysis;
    /**
     * 根因类别
     */
    private String rootCauseCategory;
    /**
     * 根因子类
     */
    private String rootCauseSubtype;
    /**
     * 建议措施
     */
    private String recommendedPractice;
    /**
     * 提出人员ID
     */
    private Long proposeEmployeeId;
    /**
     * 提出人员姓名
     */
    private String proposeEmployeeName;
    /**
     * 提出人员编码
     */
    private String proposeEmployeeCode;
    /**
     * 排序
     */
    private Integer sort;
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
     * 租户ID
     */
    private Long tenantId;

    /**
     * 请求参数
     */
    private Map<String, Object> params;
}

