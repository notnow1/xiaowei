package net.qixiaowei.strategy.cloud.api.dto.gap;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 差距分析经营情况表
 *
 * @author Graves
 * @since 2023-02-24
 */
@Data
@Accessors(chain = true)
public class GapAnalysisOperateDTO {

    //查询检验
    public interface QueryGapAnalysisOperateDTO extends Default {

    }

    //新增检验
    public interface AddGapAnalysisOperateDTO extends Default {

    }

    //删除检验
    public interface DeleteGapAnalysisOperateDTO extends Default {

    }

    //修改检验
    public interface UpdateGapAnalysisOperateDTO extends Default {

    }

    /**
     * ID
     */
    private Long gapAnalysisOperateId;
    /**
     * 差距分析ID
     */
    private Long gapAnalysisId;
    /**
     * 指标ID
     */
    private Long indicatorId;
    /**
     * 指标名称
     */
    private String indicatorName;
    /**
     * 目标值
     */
    private BigDecimal targetValue;
    /**
     * 实际值
     */
    private BigDecimal actualValue;
    /**
     * 完成率
     */
    private BigDecimal completionRate;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 历史年份
     */
    private Integer historyYear;
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
    /**
     * 差距分析经营情况表List
     */
    private List<GapAnalysisOperateDTO> gapAnalysisOperateDTOS;
}

