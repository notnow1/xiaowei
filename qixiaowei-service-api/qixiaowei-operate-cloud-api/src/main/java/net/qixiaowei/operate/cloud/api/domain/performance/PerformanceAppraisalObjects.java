package net.qixiaowei.operate.cloud.api.domain.performance;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 绩效考核对象表
* @author Graves
* @since 2022-12-05
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
     * 考核负责人姓名
     */
     private  String  appraisalPrincipalName;
     /**
     * 评议分数
     */
     private  BigDecimal  evaluationScore;
     /**
     * 考核结果(绩效等级系数ID)
     */
     private  Long  appraisalResultId;
     /**
     * 考核结果
     */
     private  String  appraisalResult;
     /**
     * 考核对象状态:1待制定目标;2已制定目标-草稿;3待评议;4已评议-草稿;5待排名
     */
     private  Integer  appraisalObjectStatus;
     /**
     * 排序
     */
     private  Integer  sort;

}

