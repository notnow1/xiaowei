package net.qixiaowei.operate.cloud.api.domain.performance;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
* 绩效比例表
* @author Graves
* @since 2022-10-10
*/
@Data
@Accessors(chain = true)
public class PerformancePercentage extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  performancePercentageId;
     /**
     * 绩效比例名称
     */
     private  String  performancePercentageName;
     /**
     * 组织绩效等级ID
     */
     private  Long  orgPerformanceRankId;
     /**
     * 个人绩效等级ID
     */
     private  Long  personPerformanceRankId;

}

