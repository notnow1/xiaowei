package net.qixiaowei.operate.cloud.api.domain.employee;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
* 人力预算调整表
* @author TANGMICHI
* @since 2022-11-22
*/
@Data
@Accessors(chain = true)
public class EmployeeBudgetAdjusts extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  employeeBudgetAdjustsId;
     /**
     * 人力预算明细ID
     */
     private  Long  employeeBudgetDetailsId;
     /**
     * 周期数(顺序递增)
     */
     private  Integer  cycleNumber;
     /**
     * 调整人数
     */
     private  Integer  numberAdjust;

}

