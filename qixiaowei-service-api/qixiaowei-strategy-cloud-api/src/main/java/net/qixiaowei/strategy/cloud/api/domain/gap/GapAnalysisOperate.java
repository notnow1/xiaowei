package net.qixiaowei.strategy.cloud.api.domain.gap;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 差距分析经营情况表
* @author Graves
* @since 2023-02-24
*/
@Data
@Accessors(chain = true)
public class GapAnalysisOperate extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  gapAnalysisOperateId;
     /**
     * 差距分析ID
     */
     private  Long  gapAnalysisId;
     /**
     * 指标ID
     */
     private  Long  indicatorId;
     /**
     * 经营年度
     */
     private  Integer  operateYear;
     /**
     * 目标值
     */
     private  BigDecimal  targetValue;
     /**
     * 实际值
     */
     private  BigDecimal  actualValue;
     /**
     * 排序
     */
     private  Integer  sort;

}

