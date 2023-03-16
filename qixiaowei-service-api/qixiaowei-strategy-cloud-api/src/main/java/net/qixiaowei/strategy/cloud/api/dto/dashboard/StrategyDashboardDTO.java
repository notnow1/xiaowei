package net.qixiaowei.strategy.cloud.api.dto.dashboard;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import javax.validation.groups.Default;

/**
* 战略意图表
* @author TANGMICHI
* @since 2023-02-23
*/
@Data
@Accessors(chain = true)
public class StrategyDashboardDTO extends BaseDTO {

    //查询检验
    public interface QueryStrategyIntentDTO extends Default{

    }
    //新增检验
    public interface AddStrategyIntentDTO extends Default{

    }

    //删除检验
    public interface DeleteStrategyIntentDTO extends Default{

    }
    //修改检验
    public interface UpdateStrategyIntentDTO extends Default{

    }
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
    private String planBusinessUnitName;
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
}

