package net.qixiaowei.operate.cloud.api.domain.salary;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 个人年终奖发放对象表
* @author TANGMICHI
* @since 2022-12-02
*/
@Data
@Accessors(chain = true)
public class EmpAnnualBonusObjects extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  empAnnualBonusObjectsId;
     /**
     * 个人年终奖ID
     */
     private  Long  employeeAnnualBonusId;
     /**
     * 员工ID
     */
     private  Long  employeeId;
     /**
     * 选中标记:0否;1是
     */
     private  Integer  choiceFlag;
     /**
     * 绩效等级系数ID
     */
     private  Long  performanceRankFactorId;
     /**
     * 绩效奖金系数
     */
     private BigDecimal performanceBonusFactor;
     /**
     * 考勤系数
     */
     private  BigDecimal  attendanceFactor;
     /**
     * 建议值
     */
     private  BigDecimal  recommendValue;
     /**
     * 评议值
     */
     private  BigDecimal  commentValue;


}

