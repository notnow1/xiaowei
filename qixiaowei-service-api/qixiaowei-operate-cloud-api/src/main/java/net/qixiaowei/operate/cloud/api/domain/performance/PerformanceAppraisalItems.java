package net.qixiaowei.operate.cloud.api.domain.performance;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 绩效考核项目表
* @author Graves
* @since 2022-12-06
*/
@Data
@Accessors(chain = true)
public class PerformanceAppraisalItems extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  performAppraisalItemsId;
     /**
     * 绩效考核对象ID
     */
     private  Long  performAppraisalObjectsId;
     /**
     * 指标ID
     */
     private  Long  indicatorId;
     /**
     * 指标名称
     */
     private  String  indicatorName;
     /**
     * 指标值类型:1金额;2比率
     */
     private  Integer  indicatorValueType;
     /**
     * 考核方向:0反向;1正向
     */
     private  Integer  examineDirection;
     /**
     * 挑战值
     */
     private  BigDecimal  challengeValue;
     /**
     * 目标值
     */
     private  BigDecimal  targetValue;
     /**
     * 保底值
     */
     private  BigDecimal  guaranteedValue;
     /**
     * 实际值
     */
     private  BigDecimal  actualValue;
     /**
     * 权重百分比(%)
     */
     private  BigDecimal  weight;

}

