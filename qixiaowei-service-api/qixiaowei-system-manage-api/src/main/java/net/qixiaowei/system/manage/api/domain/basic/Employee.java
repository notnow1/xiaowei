package net.qixiaowei.system.manage.api.domain.basic;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 员工表
* @author TANGMICHI
* @since 2022-09-30
*/
@Data
@Accessors(chain = true)
public class Employee extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  employeeId;
     /**
     * 工号
     */
     private  String  employeeCode;
     /**
     * 姓名
     */
     private  String  employeeName;
     /**
     * 性别:1男;2女
     */
     private  Integer  employeeGender;
     /**
     * 证件类型
     */
     private  Integer  identityType;
     /**
     * 证件号码
     */
     private  String  identityCard;
     /**
     * 出生日期
     */
     private Date employeeBirthday;
     /**
     * 员工手机号
     */
     private  String  employeeMobile;
     /**
     * 员工邮箱
     */
     private  String  employeeEmail;
     /**
     * 员工部门ID
     */
     private  Long  employeeDepartmentId;
     /**
     * 员工岗位ID
     */
     private  Long  employeePostId;
     /**
     * 员工职级
     */
     private  Integer  employeeRank;
     /**
     * 员工基本工资
     */
     private BigDecimal employeeBasicWage;
     /**
     * 入职日期
     */
     private  Date  employmentDate;
     /**
     * 离职日期
     */
     private  Date  departureDate;
     /**
     * 用工状态:0离职;1在职
     */
     private  Integer  employmentStatus;
     /**
     * 状态:0失效;1生效
     */
     private  Integer  status;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

