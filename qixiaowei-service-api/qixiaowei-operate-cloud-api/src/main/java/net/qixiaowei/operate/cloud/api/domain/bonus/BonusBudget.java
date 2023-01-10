package net.qixiaowei.operate.cloud.api.domain.bonus;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 奖金预算表
 *
 * @author TANGMICHI
 * @since 2022-11-26
 */
@Data
@Accessors(chain = true)
public class BonusBudget extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long bonusBudgetId;
    /**
     * 预算年度
     */
    private Integer budgetYear;
    /**
     * 总奖金包预算
     */
    private BigDecimal amountBonusBudget;
    /**
     * 预算年度前一年的总奖金包
     */
    private BigDecimal bonusBeforeOne;
    /**
     * 指标id集合
     */
    private List<Long> indicatorIds;
}

