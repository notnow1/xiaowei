package net.qixiaowei.operate.cloud.api.domain.bonus;

import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 部门奖金包预算表
 *
 * @author TANGMICHI
 * @since 2022-11-29
 */
@Data
@Accessors(chain = true)
public class DeptBonusBudget extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long deptBonusBudgetId;
    /**
     * 预算年度
     */
    private Integer budgetYear;
    /**
     * 战略奖比例
     */
    private BigDecimal strategyAwardPercentage;

}

