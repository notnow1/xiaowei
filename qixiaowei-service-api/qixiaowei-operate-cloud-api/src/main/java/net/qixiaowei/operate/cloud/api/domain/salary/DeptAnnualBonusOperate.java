package net.qixiaowei.operate.cloud.api.domain.salary;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 部门年终奖经营绩效结果表
* @author TANGMICHI
* @since 2022-12-06
*/
@Data
@Accessors(chain = true)
public class DeptAnnualBonusOperate extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  deptAnnualBonusOperateId;
     /**
     * 部门年终奖ID
     */
     private  Long  deptAnnualBonusId;
     /**
     * 指标ID
     */
     private  Long  indicatorId;
     /**
     * 指标名称
     */
     private  String  indicatorName;
     /**
     * 目标值
     */
     private  BigDecimal  targetValue;
     /**
     * 实际值
     */
     private  BigDecimal  actualValue;

}

