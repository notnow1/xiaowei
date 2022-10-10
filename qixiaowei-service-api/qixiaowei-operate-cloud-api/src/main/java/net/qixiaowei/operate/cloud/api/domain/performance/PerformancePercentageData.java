package net.qixiaowei.operate.cloud.api.domain.performance;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 绩效比例数据表
* @author Graves
* @since 2022-10-10
*/
@Data
@Accessors(chain = true)
public class PerformancePercentageData extends BaseEntity {

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
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

