package net.qixiaowei.strategy.cloud.api.domain.plan;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 规划业务单元
* @author wangchen
* @since 2023-02-17
*/
@Data
@Accessors(chain = true)
public class PlanBusinessUnit extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  planBusinessUnitId;
     /**
     * 规划业务单元编码
     */
     private  String  businessUnitCode;
     /**
     * 规划业务单元名称
     */
     private  String  businessUnitName;
     /**
     * 规划业务单元维度(region,department,product,industry)
     */
     private  String  businessUnitDecompose;
     /**
     * 状态:0失效;1生效
     */
     private  Integer  status;

}

