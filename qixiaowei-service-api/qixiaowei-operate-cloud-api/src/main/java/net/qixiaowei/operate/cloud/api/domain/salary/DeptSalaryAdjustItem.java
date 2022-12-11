package net.qixiaowei.operate.cloud.api.domain.salary;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 部门调薪项表
* @author Graves
* @since 2022-12-11
*/
@Data
@Accessors(chain = true)
public class DeptSalaryAdjustItem extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  deptSalaryAdjustItemId;
     /**
     * 部门调薪计划ID
     */
     private  Long  deptSalaryAdjustPlanId;
     /**
     * 部门ID
     */
     private  Long  departmentId;
     /**
     * 覆盖比例
     */
     private  BigDecimal  coveragePercentage;
     /**
     * 调整比例
     */
     private  BigDecimal  adjustmentPercentage;
     /**
     * 调整时间
     */
     private  Date   adjustmentTime;

}

