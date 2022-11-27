package net.qixiaowei.operate.cloud.api.dto.performance;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 绩效考核对象表
 *
 * @author Graves
 * @since 2022-11-24
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
     * 绩效考核ID
     */
    private Long performanceAppraisalId;
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
     * 考核负责人ID
     */
    private Long appraisalPrincipalId;
    /**
     * 考核负责人
     */
    private String appraisalPrincipalName;
    /**
     * 评议分数
     */
    private BigDecimal evaluationScore;
    /**
     * 考核结果(绩效等级ID)
     */
    private Long appraisalResultId;
    /**
     * 绩效等级ID
     */
    private Long performanceRankId;
    /**
     * 绩效等级名称
     */
    private String performanceRankName;
    /**
     * 自定义列标记:0否;1是
     */
    private Integer selfDefinedColumnsFlag;
    /**
     * 考核对象状态:1制定目标;2评议;3排名;4归档
     */
    private Integer appraisalObjectStatus;
    /**
     * 名次
     */
    private Integer rank;
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

