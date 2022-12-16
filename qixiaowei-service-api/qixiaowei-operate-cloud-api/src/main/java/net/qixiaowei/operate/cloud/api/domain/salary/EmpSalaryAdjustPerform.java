package net.qixiaowei.operate.cloud.api.domain.salary;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 个人调薪绩效记录表
* @author Graves
* @since 2022-12-14
*/
@Data
@Accessors(chain = true)
public class EmpSalaryAdjustPerform extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  empSalaryAdjustPerformId;
     /**
     * 个人调薪计划ID
     */
     private  Long  empSalaryAdjustPlanId;
     /**
     * 绩效考核ID
     */
     private  Long  performanceAppraisalId;
     /**
     * 绩效考核对象ID
     */
     private  Long  performAppraisalObjectsId;
     /**
     * 周期类型:1月度;2季度;3半年度;4年度
     */
     private  Integer  cycleType;
     /**
     * 考核周期
     */
     private  Integer  cycleNumber;
     /**
     * 归档日期
     */
     private  Date   filingDate;
     /**
     * 考核结果
     */
     private  String  appraisalResult;

}

