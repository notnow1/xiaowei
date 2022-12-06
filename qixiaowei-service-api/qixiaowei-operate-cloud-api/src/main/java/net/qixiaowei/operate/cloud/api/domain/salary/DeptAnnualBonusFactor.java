package net.qixiaowei.operate.cloud.api.domain.salary;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 部门年终奖系数表
* @author TANGMICHI
* @since 2022-12-06
*/
@Data
@Accessors(chain = true)
public class DeptAnnualBonusFactor extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  deptAnnualBonusFactorId;
     /**
     * 部门年终奖ID
     */
     private  Long  deptAnnualBonusId;
     /**
     * 部门ID
     */
     private  Long  departmentId;
     /**
     * 权重
     */
     private  BigDecimal  weight;
     /**
     * 最近绩效结果
     */
     private  String  lastPerformanceResulted;
     /**
     * 绩效等级ID
     */
     private  Long  performanceRankId;
     /**
     * 绩效等级系数ID
     */
     private  Long  performanceRankFactorId;
     /**
     * 绩效
     */
     private  String  performanceRank;
     /**
     * 组织绩效奖金系数
     */
     private  BigDecimal  performanceBonusFactor;
     /**
     * 组织重要性系数
     */
     private  BigDecimal  importanceFactor;
     /**
     * 奖金占比
     */
     private  BigDecimal  bonusPercentage;
     /**
     * 可分配年终奖
     */
     private  BigDecimal  distributeBonus;

}

