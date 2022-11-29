package net.qixiaowei.operate.cloud.api.domain.salary;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
 * 工资发薪表
 *
 * @author Graves
 * @since 2022-11-17
 */
@Data
@Accessors(chain = true)
public class SalaryPay extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long salaryPayId;
    /**
     * 员工ID
     */
    private Long employeeId;
    /**
     * 发薪年份
     */
    private Integer payYear;
    /**
     * 发薪月份
     */
    private Integer payMonth;
    /**
     * 工资金额
     */
    private BigDecimal salaryAmount;
    /**
     * 津贴金额
     */
    private BigDecimal allowanceAmount;
    /**
     * 福利金额
     */
    private BigDecimal welfareAmount;
    /**
     * 奖金金额
     */
    private BigDecimal bonusAmount;
    /**
     * 代扣代缴金额
     */
    private BigDecimal withholdRemitTax;
    /**
     * 其他扣款金额
     */
    private BigDecimal otherDeductions;
    /**
     * 发薪金额
     */
    private BigDecimal payAmount;

}

