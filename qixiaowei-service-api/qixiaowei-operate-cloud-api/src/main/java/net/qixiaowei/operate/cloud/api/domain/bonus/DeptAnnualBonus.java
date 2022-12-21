package net.qixiaowei.operate.cloud.api.domain.bonus;


import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;

import java.math.BigDecimal;

import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 部门年终奖表
 *
 * @author TANGMICHI
 * @since 2022-12-06
 */
@Data
@Accessors(chain = true)
public class DeptAnnualBonus extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long deptAnnualBonusId;
    /**
     * 部门ID
     */

    private Long departmentId;
    /**
     * 年终奖年度
     */
    private Integer annualBonusYear;
    /**
     * 公司年终奖总包
     */
    private BigDecimal companyAnnualBonus;
    /**
     * 部门年终奖总包
     */
    private BigDecimal departmentAnnualBonus;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 评议年度
     */
    private  String  createTimeYear;
    /**
     * 创建时间
     */
    private String createTimeFormat;
}

