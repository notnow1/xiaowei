package net.qixiaowei.strategy.cloud.api.dto.gap;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 差距分析表
 *
 * @author Graves
 * @since 2023-02-24
 */
@Data
@Accessors(chain = true)
public class GapAnalysisDTO {

    //查询检验
    public interface QueryGapAnalysisDTO extends Default {

    }

    //新增检验
    public interface AddGapAnalysisDTO extends Default {

    }

    //删除检验
    public interface DeleteGapAnalysisDTO extends Default {

    }

    //修改检验
    public interface UpdateGapAnalysisDTO extends Default {

    }

    /**
     * ID
     */
    @NotNull(message = "id不能为空", groups = {GapAnalysisDTO.DeleteGapAnalysisDTO.class})
    private Long gapAnalysisId;
    /**
     * 规划年度
     */
    @NotNull(message = "规划年度不能为空", groups = {GapAnalysisDTO.AddGapAnalysisDTO.class})
    private Integer planYear;
    /**
     * 规划业务单元ID
     */
    @NotNull(message = "规划业务单元不能为空", groups = {GapAnalysisDTO.AddGapAnalysisDTO.class})
    private Long planBusinessUnitId;
    /**
     * 规划业务单元维度(region,department,product,industry)
     */
    private String businessUnitDecompose;
    /**
     * 区域ID
     */
    private Long areaId;
    /**
     * 部门ID
     */
    private Long departmentId;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 行业ID
     */
    private Long industryId;
    /**
     * 经营历史年份
     */
    @NotNull(message = "经营历史年份不能为空", groups = {GapAnalysisDTO.AddGapAnalysisDTO.class, GapAnalysisDTO.UpdateGapAnalysisDTO.class})
    private Integer operateHistoryYear;
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
     * 差距分析经营情况表
     */
    private List<GapAnalysisOperateDTO> gapAnalysisOperateDTOS;
    /**
     * 机会差距表
     */
    private List<GapAnalysisOpportunityDTO> gapAnalysisOpportunityDTOS;
    /**
     * 业绩差距表
     */
    private List<GapAnalysisPerformanceDTO> gapAnalysisPerformanceDTOS;
}

