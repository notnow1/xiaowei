package net.qixiaowei.system.manage.excel.basic;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @ExcelIgnore
    @ExcelProperty("工号")
    private String employeeCode;
    /**
     * 姓名
     */
    @ExcelIgnore
    @ExcelProperty("姓名")
    private String employeeName;
    @ExcelIgnore
    @ExcelProperty("用工关系状态")
    private String employmentStatus;
    /**
     * 性别:1男;2女
     */
    @ExcelIgnore
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
    @ExcelIgnore
    @ExcelProperty("证件号码")
    private String identityCard;
    /**
     * 出生日期
     */
    @ExcelIgnore
    @ExcelProperty("出生日期")
    @DateTimeFormat(value = "yyyy/MM/dd")
    private String employeeBirthday;
    /**
     * 婚姻状况:0未婚;1已婚
     */
    @ExcelIgnore
    private  String maritalStatus;

    /**
     * 国籍
     */
    @ExcelIgnore
    private  String nationalityName;
    /**
     * 民族
     */
    @ExcelIgnore
    private  String nationName;
    /**
     * 户口所在地名称
     */
    @ExcelIgnore
    private  String residentCityName;

    /**
     * 参保地名称
     */
    @ExcelIgnore
    private  String insuredCityName;
    /**
     * 常住地名称
     */
    @ExcelIgnore
    private  String permanentAddressName;
    /**
     * 入职日期
     */
    @ExcelIgnore
    private  String employmentDate;
    /**
     * 离职日期
     */
    @ExcelIgnore
    private  String departureDate;
    /**
     * 员工手机号
     */
    @ExcelIgnore
    @ExcelProperty("员工手机号")
    private String employeeMobile;

}

