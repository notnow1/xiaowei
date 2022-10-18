package net.qixiaowei.system.manage.excel;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import net.qixiaowei.integration.common.web.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
* 员工表
* @author TANGMICHI
* @since 2022-09-30
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class EmployeeExcel{

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     @ExcelIgnore
     private  Long  employeeId;
     /**
     * 工号
     */
     @ExcelProperty("人员工号")
     private  String  employeeCode;
     /**
     * 姓名
     */
     @ExcelProperty("人员姓名")
     private  String  employeeName;
     /**
     * 性别:1男;2女
     */
     @ExcelProperty("性别")
     private  Integer  employeeGender;
     /**
     * 证件类型
     */
     @ExcelIgnore
     private  Integer  identityType;
     /**
     * 证件号码
     */
     @ExcelProperty("证件号码")
     private  String  identityCard;
     /**
     * 出生日期
     */
     @ExcelProperty("出生日期")
     @DateTimeFormat(value = "yyyy/MM/dd")
     private Date employeeBirthday;
     /**
     * 员工手机号
     */
     @ExcelProperty("员工手机号")
     private  String  employeeMobile;
     /**
     * 员工邮箱
     */
     @ExcelProperty("员工邮箱")
     private  String  employeeEmail;
     /**
     * 员工部门ID
     */
     @ExcelIgnore
     private  Long  employeeDepartmentId;
     /**
     * 员工岗位ID
     */
     @ExcelIgnore
     private  Long  employeePostId;
     /**
     * 员工职级
     */
     @ExcelIgnore
     private  Integer  employeeRank;
     /**
     * 员工基本工资
     */
     @ExcelProperty("员工基本工资")
     private BigDecimal employeeBasicWage;
     /**
     * 入职日期
     */
     @ExcelProperty("入职日期")
     @DateTimeFormat(value = "yyyy/MM/dd")
     private  Date  employmentDate;
     /**
     * 离职日期
     */
     @ExcelProperty("离职日期")
     @DateTimeFormat(value = "yyyy/MM/dd")
     private  Date  departureDate;
     /**
     * 用工状态:0离职;1在职
     */
     @ExcelProperty("用工状态")
     private  Integer  employmentStatus;
     /**
     * 状态:0暂存;1生效
     */
     @ExcelIgnore
     private  Integer  status;
     /**
     * 删除标记:0未删除;1已删除
     */
     @ExcelIgnore
     private  Integer  deleteFlag;

}

