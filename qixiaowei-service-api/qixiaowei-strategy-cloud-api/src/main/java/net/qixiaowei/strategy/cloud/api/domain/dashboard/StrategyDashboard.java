package net.qixiaowei.strategy.cloud.api.domain.dashboard;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
 * 战略意图表
 *
 * @author TANGMICHI
 * @since 2023-02-23
 */
@Data
@Accessors(chain = true)
public class StrategyDashboard extends TenantEntity {
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

