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
import lombok.ToString;
import net.qixiaowei.integration.common.web.domain.BaseEntity;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    public EmployeeExcel() {
    }

    public EmployeeExcel(String employeeCode, String employeeName, String employmentStatus, String employeeGender, String identityCard, String employeeBirthday, String maritalStatus, String nationalityName, String nationName, String residentCityName, String insuredCityName, String permanentAddressName, String employmentDate, String departureDate, String postCode, String departmentCode, String employeeRankName, String employeeBasicWage, String employeeMobile, String employeeEmail, String wechatCode, String contactAddress, String emergencyContact, String emergencyMobile, String contactAddressDetail) {
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.employmentStatus = employmentStatus;
        this.employeeGender = employeeGender;
        this.identityCard = identityCard;
        this.employeeBirthday = employeeBirthday;
        this.maritalStatus = maritalStatus;
        this.nationalityName = nationalityName;
        this.nationName = nationName;
        this.residentCityName = residentCityName;
        this.insuredCityName = insuredCityName;
        this.permanentAddressName = permanentAddressName;
        this.employmentDate = employmentDate;
        this.departureDate = departureDate;
        this.postCode = postCode;
        this.departmentCode = departmentCode;
        this.employeeRankName = employeeRankName;
        this.employeeBasicWage = employeeBasicWage;
        this.employeeMobile = employeeMobile;
        this.employeeEmail = employeeEmail;
        this.wechatCode = wechatCode;
        this.contactAddress = contactAddress;
        this.emergencyContact = emergencyContact;
        this.emergencyMobile = emergencyMobile;
        this.contactAddressDetail = contactAddressDetail;
    }

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
     * 岗位编码
     */
    @ExcelIgnore
    private  String postCode;
    /**
     * 部门编码
     */
    @ExcelIgnore
    private  String departmentCode;
    /**
     * 个人职级
     */
    @ExcelIgnore
    private  String employeeRankName;

    /**
     * 员工基本工资
     */
    @ExcelIgnore
    @JsonFormat(shape =JsonFormat.Shape.STRING)
    private String employeeBasicWage;
    /**
     * 员工手机号
     */
    @ExcelIgnore
    private  String employeeMobile;

    /**
     * 员工邮箱
     */
    @ExcelIgnore
    private  String employeeEmail;
    /**
     * 微信号
     */
    @ExcelIgnore
    private  String wechatCode;
    /**
     * 通信地址
     */
    @ExcelIgnore
    private  String contactAddress;
    /**
     * 紧急联系人
     */
    @ExcelIgnore
    private  String emergencyContact;
    /**
     * 紧急联系人电话
     */
    @ExcelIgnore
    private  String emergencyMobile;
    /**
     * 通信地址详情
     */
    private  String contactAddressDetail;

}

