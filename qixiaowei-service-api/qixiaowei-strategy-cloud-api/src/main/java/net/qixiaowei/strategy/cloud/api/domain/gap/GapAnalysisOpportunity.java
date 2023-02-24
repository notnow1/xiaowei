package net.qixiaowei.strategy.cloud.api.domain.gap;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 机会差距表
* @author Graves
* @since 2023-02-24
*/
@Data
@Accessors(chain = true)
public class GapAnalysisOpportunity extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  gapAnalysisOpportunityId;
     /**
     * 差距分析ID
     */
     private  Long  gapAnalysisId;
     /**
     * 序列号
     */
     private  Integer  serialNumber;
     /**
     * 业绩差距名称
     */
     private  String  gapPerformanceName;
     /**
     * 差距描述
     */
     private  String  gapDescription;
     /**
     * 根因分析
     */
     private  String  rootCauseAnalysis;
     /**
     * 根因类别
     */
     private  String  rootCauseCategory;
     /**
     * 根因子类
     */
     private  String  rootCauseSubtype;
     /**
     * 建议措施
     */
     private  String  recommendedPractice;
     /**
     * 提出人员ID
     */
     private  Long  proposeEmployeeId;
     /**
     * 提出人员姓名
     */
     private  String  proposeEmployeeName;
     /**
     * 提出人员编码
     */
     private  String  proposeEmployeeCode;
     /**
     * 排序
     */
     private  Integer  sort;

}

