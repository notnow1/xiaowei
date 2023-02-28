package net.qixiaowei.strategy.cloud.api.domain.marketInsight;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 市场洞察宏观详情表
* @author TANGMICHI
* @since 2023-02-28
*/
@Data
@Accessors(chain = true)
public class MiMacroDetail extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  miMacroDetailId;
     /**
     * 市场洞察宏观ID
     */
     private  Long  marketInsightMacroId;
     /**
     * 视角
     */
     private  Long  visualAngle;
     /**
     * 企业相关因素
     */
     private  String  companyRelatedFactor;
     /**
     * 变化及趋势
     */
     private  String  changeTrend;
     /**
     * 影响描述
     */
     private  String  influenceDescription;
     /**
     * 建议措施
     */
     private  String  recommendedPractice;
     /**
     * 规划期
     */
     private  Integer  planPeriod;
     /**
     * 预估机会点金额
     */
     private  BigDecimal  estimateOpportunityAmount;
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

