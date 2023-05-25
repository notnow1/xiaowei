package net.qixiaowei.strategy.cloud.api.dto.gap;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

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
public class GapAnalysisDTO extends BaseDTO {

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
    @NotNull(message = "id不能为空", groups = {GapAnalysisDTO.DeleteGapAnalysisDTO.class, GapAnalysisDTO.UpdateGapAnalysisDTO.class})
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
     * 规划业务单元名称
     */
    private String businessUnitName;
    /**
     * 规划业务单元维度(region,department,product,industry)
     */
    private String businessUnitDecompose;
    /**
     * 规划业务单元维度(region,department,product,industry)
     */
    private String businessUnitDecomposeName;
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
     * 区域名称
     */
    private String areaName;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 行业名称
     */
    private String industryName;
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
     * 租户ID
     */
    private Long tenantId;
    /**
     * 规划业务单元维度列表
     */
    List<Map<String, Object>> businessUnitDecomposes;
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

