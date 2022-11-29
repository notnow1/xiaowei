package net.qixiaowei.operate.cloud.api.domain.performance;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
* 绩效比例数据表
* @author Graves
* @since 2022-10-10
*/
@Data
@Accessors(chain = true)
public class PerformancePercentageData extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  performancePercentageDataId;
     /**
     * 绩效比例ID
     */
     private  Long  performancePercentageId;
     /**
     * 组织绩效等级系数ID
     */
     private  Long  orgRankFactorId;
     /**
     * 个人绩效等级系数ID
     */
     private  Long  personRankFactorId;
     /**
     * 数值,单位:百分号%
     */
     private BigDecimal value;


}

