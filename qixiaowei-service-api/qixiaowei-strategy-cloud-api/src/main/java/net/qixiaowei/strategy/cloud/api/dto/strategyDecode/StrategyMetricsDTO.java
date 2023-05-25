package net.qixiaowei.strategy.cloud.api.dto.strategyDecode;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;
import net.qixiaowei.strategy.cloud.api.vo.strategyDecode.StrategyMeasureDetailVO;

import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 战略衡量指标表
 *
 * @author Graves
 * @since 2023-03-07
 */
@Data
@Accessors(chain = true)
public class StrategyMetricsDTO extends BaseDTO {

    //查询检验
    public interface QueryStrategyMetricsDTO extends Default {

    }

    //新增检验
    public interface AddStrategyMetricsDTO extends Default {

    }

    //删除检验
    public interface DeleteStrategyMetricsDTO extends Default {

    }

    //修改检验
    public interface UpdateStrategyMetricsDTO extends Default {

    }

    /**
     * ID
     */
    private Long strategyMetricsId;
    /**
     * 战略举措清单ID
     */
    private Long strategyMeasureId;
    /**
     * 规划年度
     */
    private Integer planYear;
    /**
     * 规划业务单元ID
     */
    private Long planBusinessUnitId;
    /**
     * 规划业务单元名称
     */
    private String businessUnitName;
    /**
     * 战略指标维度名称
     */
    private String indexDimensionName;
    /**
     * 规划业务单元维度(region,department,product,industry)
     */
    private String businessUnitDecompose;
    /**
     * 规划业务单元维度(region,department,product,industry)
     */
    private List<Map<String, Object>> BusinessUnitDecomposes;
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
     * 规划期
     */
    private Integer planPeriod;
    /**
     * 规划期
     */
    private String planPeriodName;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 战略衡量指标详情表
     */
    private List<StrategyMetricsDetailDTO> strategyMetricsDetailDTOS;
}

