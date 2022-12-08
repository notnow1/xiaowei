package net.qixiaowei.operate.cloud.api.domain.bonus;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 奖金发放预算部门表
* @author TANGMICHI
* @since 2022-12-08
*/
@Data
@Accessors(chain = true)
public class BonusPayBudgetDept extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  bonusPayBudgetDeptId;
     /**
     * 奖金发放申请ID
     */
     private  Long  bonusPayApplicationId;
     /**
     * 部门ID
     */
     private  Long  departmentId;
     /**
     * 奖金比例
     */
     private  BigDecimal  bonusPercentage;

}

