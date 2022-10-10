package net.qixiaowei.operate.cloud.api.domain.performance;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 绩效比例表
* @author Graves
* @since 2022-10-10
*/
@Data
@Accessors(chain = true)
public class PerformancePercentage extends BaseEntity {

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
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

