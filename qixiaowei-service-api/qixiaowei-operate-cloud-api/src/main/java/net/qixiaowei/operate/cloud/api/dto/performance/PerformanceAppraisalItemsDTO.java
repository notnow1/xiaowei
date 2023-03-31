package net.qixiaowei.operate.cloud.api.dto.performance;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 绩效考核项目表
 *
 * @author Graves
 * @since 2022-12-06
 */
@Data
@Accessors(chain = true)
public class PerformanceAppraisalItemsDTO {

    //查询检验
    public interface QueryPerformanceAppraisalItemsDTO extends Default {

    }

    //新增检验
    public interface AddPerformanceAppraisalItemsDTO extends Default {

    }

    //删除检验
    public interface DeletePerformanceAppraisalItemsDTO extends Default {

    }

    //修改检验
    public interface UpdatePerformanceAppraisalItemsDTO extends Default {

    }

    /**
     * 绩效等级名称
     */
    private String performanceRankName;
    /**
     * ID
     */
    private Long performAppraisalItemsId;
    /**
     * 绩效考核对象ID
     */
    private Long performAppraisalObjectsId;
    /**
     * 指标ID
     */
    private Long indicatorId;
    /**
     * 指标名称
     */
    private String indicatorName;
    /**
     * 指标值类型:1金额;2比率
     */
    private Integer indicatorValueType;
    /**
     * 考核方向:0反向;1正向
     */
    private Integer examineDirection;
    /**
     * 挑战值
     */
    private BigDecimal challengeValue;
    /**
     * 目标值
     */
    private BigDecimal targetValue;
    /**
     * 保底值
     */
    private BigDecimal guaranteedValue;
    /**
     * 实际值
     */
    private BigDecimal actualValue;
    /**
     * 评议分数
     */
    private BigDecimal evaluationScore;
    /**
     * 权重百分比(%)
     */
    private BigDecimal weight;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间s
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 备注
     */
    private String remark;

    /**
     * 实际值列表
     */
    private List<Map<String, Object>> evaluateList;

}

