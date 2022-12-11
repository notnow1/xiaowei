package net.qixiaowei.operate.cloud.api.domain.salary;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 部门调薪计划表
* @author Graves
* @since 2022-12-11
*/
@Data
@Accessors(chain = true)
public class DeptSalaryAdjustPlan extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  deptSalaryAdjustPlanId;
     /**
     * 预算年度
     */
     private  Integer  planYear;
     /**
     * 调薪总数
     */
     private  BigDecimal  salaryAdjustTotal;

}

