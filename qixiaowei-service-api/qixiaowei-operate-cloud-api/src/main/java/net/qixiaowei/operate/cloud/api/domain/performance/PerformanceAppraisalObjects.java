package net.qixiaowei.operate.cloud.api.domain.performance;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
* 绩效考核对象表
* @author Graves
* @since 2022-11-24
*/
@Data
@Accessors(chain = true)
public class PerformanceAppraisalObjects extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  performAppraisalObjectsId;
     /**
     * 绩效考核ID
     */
     private  Long  performanceAppraisalId;
     /**
     * 考核对象ID
     */
     private  Long  appraisalObjectId;
     /**
     * 考核负责人ID
     */
     private  Long  appraisalPrincipalId;
     /**
     * 评议分数
     */
     private BigDecimal evaluationScore;
     /**
     * 考核结果(绩效等级ID)
     */
     private  Long  appraisalResultId;
     /**
     * 考核对象状态:1制定目标;2评议;3排名;4归档
     */
     private  Integer  appraisalObjectStatus;
     /**
     * 排序
     */
     private  Integer  sort;


}

