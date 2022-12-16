package net.qixiaowei.operate.cloud.api.domain.salary;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 个人调薪快照表
* @author Graves
* @since 2022-12-15
*/
@Data
@Accessors(chain = true)
public class EmpSalaryAdjustSnap extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  empSalaryAdjustSnapId;
     /**
     * 个人调薪计划ID
     */
     private  Long  empSalaryAdjustPlanId;
     /**
     * 员工姓名
     */
     private  String  employeeName;
     /**
     * 入职日期
     */
     private  Date   employmentDate;
     /**
     * 司龄
     */
     private  String  seniority;
     /**
     * 原部门ID
     */
     private  Long  departmentId;
     /**
     * 原部门名称
     */
     private  String  departmentName;
     /**
     * 部门负责人ID
     */
     private  Long  departmentLeaderId;
     /**
     * 部门负责人姓名
     */
     private  String  departmentLeaderName;
     /**
     * 原岗位ID
     */
     private  Long  postId;
     /**
     * 原岗位名称
     */
     private  String  postName;
     /**
     * 原职级体系ID
     */
     private  Long  officialRankSystemId;
     /**
     * 原职级体系名称
     */
     private  String  officialRankSystemName;
     /**
     * 原职级
     */
     private  Integer  officialRank;
     /**
     * 原职级名称
     */
     private  String  officialRankName;
     /**
     * 基本工资(当前薪酬)
     */
     private  BigDecimal  basicWage;

}

