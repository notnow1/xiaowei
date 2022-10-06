package net.qixiaowei.operate.cloud.api.domain.performance;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 绩效等级表
* @author Graves
* @since 2022-10-06
*/
@Data
@Accessors(chain = true)
public class PerformanceRank extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  performanceRankId;
     /**
     * 绩效等级类别:1组织;2个人
     */
     private  Integer  performanceRankCategory;
     /**
     * 绩效等级名称
     */
     private  String  performanceRankName;
     /**
     * 绩效等级描述
     */
     private  String  performanceRankDescription;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

