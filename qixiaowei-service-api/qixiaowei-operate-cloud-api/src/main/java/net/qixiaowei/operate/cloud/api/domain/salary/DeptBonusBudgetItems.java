package net.qixiaowei.operate.cloud.api.domain.salary;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 部门奖金预算项目表
* @author TANGMICHI
* @since 2022-11-29
*/
@Data
@Accessors(chain = true)
public class DeptBonusBudgetItems extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  deptBonusBudgetItemsId;
     /**
     * 部门奖金包预算ID
     */
     private  Long  deptBonusBudgetId;
     /**
     * 部门奖金预算明细ID
     */
     private  Long  deptBonusBudgetDetailsId;
     /**
     * 工资项ID
     */
     private  Long  salaryItemId;
     /**
     * 奖金占比
     */
     private BigDecimal bonusPercentage;

}
