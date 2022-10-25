package net.qixiaowei.system.manage.excel.basic;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import net.qixiaowei.integration.common.web.domain.BaseEntity;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工表
 *
 * @author TANGMICHI
 * @since 2022-09-30
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class EmployeeExcel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelIgnore
    private Long employeeId;
    /**
     * 工号
     */
    @ExcelProperty("工号")
    private String employeeCode;
    /**
     * 姓名
     */
    @ExcelProperty("姓名")
    private String employeeName;

    @ExcelProperty("用工关系状态")
    private String employmentStatus;
    /**
     * 性别:1男;2女
     */
    @ExcelProperty("性别")
    private String employeeGender;
    /**
     * 证件类型
     */
    @ExcelIgnore
    private Integer identityType;
    /**
     * 证件号码
     */
    @ExcelProperty("证件号码")
    private String identityCard;
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
    private String employeeMobile;
    /**
     * 入职日期
     */
    @ExcelProperty("入职日期")
    @DateTimeFormat(value = "yyyy/MM/dd")
    private Date employmentDate;
    /**
     * 员工邮箱
     */
    @ExcelProperty("员工邮箱")
    private String employeeEmail;
    /**
     * 员工部门ID
     */

    @ExcelProperty("部门编码")
    private Long employeeDepartmentId;
    @ExcelIgnore
    private String employeeDepartmentName;
    /**
     * 员工岗位ID
     */
    @ExcelProperty("岗位编码")
    private Long employeePostId;
    @ExcelIgnore
    private String employeePostName;
    /**
     * 员工职级
     */
    @ExcelProperty("个人职级")
    private Integer employeeRank;
    /**
     * 员工基本工资
     */
    @ExcelProperty("员工基本工资")
    private BigDecimal employeeBasicWage;

    /**
     * 离职日期
     */
    @ExcelProperty("离职日期")
    @DateTimeFormat(value = "yyyy/MM/dd")
    private Date departureDate;

    /**
     * 状态:0暂存;1生效
     */
    @ExcelIgnore
    private Integer status;
    /**
     * 删除标记:0未删除;1已删除
     */
    @ExcelIgnore
    private Integer deleteFlag;

}

