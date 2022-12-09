package net.qixiaowei.operate.cloud.api.domain.bonus;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 个人年终奖表
* @author TANGMICHI
* @since 2022-12-06
*/
@Data
@Accessors(chain = true)
public class EmployeeAnnualBonus extends TenantEntity {

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
     * 一级部门名称
     */
     private  String  departmentName;
     /**
     * 申请部门ID
     */
     private  Long  applyDepartmentId;
     /**
     * 申请部门名称
     */
     private  String  applyDepartmentName;
     /**
     * 申请人ID
     */
     private  Long  applyEmployeeId;
     /**
     * 申请人姓名
     */
     private  String  applyEmployeeName;
     /**
     * 分配年终奖金额
     */
     private  BigDecimal  distributeBonusAmount;
     /**
     * 发起评议流程标记:0否;1是
     */
     private  Integer  commentFlag;
     /**
     * 评议环节:1管理团队评议;2主管初评+管理团队评议
     */
     private  Integer  commentStep;
     /**
     * 管理团队评议人ID
     */
     private  Long  commentEmployeeId;
     /**
     * 管理团队评议人
     */
     private  String  commentEmployeeName;
     /**
     * 评议日期
     */
     private  Date   commentDate;
     /**
     * 状态:0草稿;1待初评;2待评议;3已评议
     */
     private  Integer  status;

}

