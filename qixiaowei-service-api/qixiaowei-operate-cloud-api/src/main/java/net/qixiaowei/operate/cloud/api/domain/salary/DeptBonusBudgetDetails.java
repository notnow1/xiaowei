package net.qixiaowei.operate.cloud.api.domain.salary;

import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


/**
* 部门奖金预算明细表
* @author TANGMICHI
* @since 2022-11-29
*/
@Data
@Accessors(chain = true)
public class DeptBonusBudgetDetails extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  deptBonusBudgetDetailsId;
     /**
     * 部门奖金包预算ID
     */
     private  Long  deptBonusBudgetId;
     /**
     * 部门ID
     */
     private  Long  departmentId;
     /**
     * 部门奖金占比
     */
     private BigDecimal deptBonusPercentage;

}

