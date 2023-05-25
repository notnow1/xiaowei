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
 * 战略举措清单表
 *
 * @author Graves
 * @since 2023-03-07
 */
@Data
@Accessors(chain = true)
public class StrategyMeasureDTO extends BaseDTO {

    //查询检验
    public interface QueryStrategyMeasureDTO extends Default {

    }

    //新增检验
    public interface AddStrategyMeasureDTO extends Default {

    }

    //删除检验
    public interface DeleteStrategyMeasureDTO extends Default {

    }

    //修改检验
    public interface UpdateStrategyMeasureDTO extends Default {

    }

    /**
     * ID
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
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 战略举措清单详情表
     */
    private List<StrategyMeasureDetailVO> strategyMeasureDetailVOS;

}

