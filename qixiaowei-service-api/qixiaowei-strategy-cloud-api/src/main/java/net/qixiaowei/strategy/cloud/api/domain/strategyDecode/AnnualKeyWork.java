package net.qixiaowei.strategy.cloud.api.domain.strategyDecode;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
 * 年度重点工作表
 *
 * @author Graves
 * @since 2023-03-14
 */
@Data
@Accessors(chain = true)
public class AnnualKeyWork extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long annualKeyWorkId;
    /**
     * 规划年度
     */
    private Integer planYear;
    /**
     * 规划业务单元ID
     */
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
     * 规划级别:1部门;2公司
     */
    private Integer planRank;

}

