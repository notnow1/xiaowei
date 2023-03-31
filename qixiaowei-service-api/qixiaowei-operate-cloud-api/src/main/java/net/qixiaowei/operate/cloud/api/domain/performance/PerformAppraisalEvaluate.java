package net.qixiaowei.operate.cloud.api.domain.performance;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 绩效考核评议表
* @author Graves
* @since 2023-03-23
*/
@Data
@Accessors(chain = true)
public class PerformAppraisalEvaluate extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  performAppraisalEvaluateId;
     /**
     * 绩效考核对象ID
     */
     private  Long  performAppraisalObjectsId;
     /**
     * 绩效考核项目ID
     */
     private  Long  performAppraisalItemsId;
     /**
     * 评议周期
     */
     private  Integer  evaluateNumber;
     /**
     * 实际值
     */
     private  BigDecimal  actualValue;

}

