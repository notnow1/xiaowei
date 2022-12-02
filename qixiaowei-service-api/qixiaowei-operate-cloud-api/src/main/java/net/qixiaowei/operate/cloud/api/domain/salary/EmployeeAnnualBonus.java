package net.qixiaowei.operate.cloud.api.domain.salary;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 个人年终奖表
* @author TANGMICHI
* @since 2022-12-02
*/
@Data
@Accessors(chain = true)
public class EmployeeAnnualBonus extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  employeeAnnualBonusId;
     /**
     * 年终奖年度
     */
     private  Integer  annualBonusYear;
     /**
     * 一级部门ID
     */
     private  Long  departmentId;
     /**
     * 申请部门ID
     */
     private  Long  applyDepartmentId;
     /**
     * 申请人ID
     */
     private  Long  applyEmployeeId;
     /**
     * 分配年终奖金额
     */
     private BigDecimal distributeBonusAmount;
     /**
     * 发起评议流程标记:0否;1是
     */
     private  Integer  commentFlag;
     /**
     * 评议环节:1管理团队评议;2主管初评+管理团队评议
     */
     private  Integer  commentStep;
     /**
     * 管理团队评议人
     */
     private  Long  commentEmployeeId;
     /**
     * 评议日期
     */
     private  Date   commentDate;
     /**
     * 状态:0草稿;1待初评;2待评议;3已评议
     */
     private  Integer  status;


}

