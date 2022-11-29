package net.qixiaowei.operate.cloud.api.domain.salary;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
 * 工资发薪明细表
 *
 * @author Graves
 * @since 2022-11-17
 */
@Data
@Accessors(chain = true)
public class SalaryPayDetails extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long salaryPayDetailsId;
    /**
     * 工资发薪ID
     */
    private Long salaryPayId;
    /**
     * 工资项ID
     */
    private Long salaryItemId;
    /**
     * 金额(单位/元)
     */
    private BigDecimal amount;
    /**
     * 排序
     */
    private Integer sort;

}

