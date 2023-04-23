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

    /**
     * 错误数据
     */
    @ExcelIgnore
    @ExcelProperty(index = 0)
    private String errorData;
    /**
     * 工号
     */
    @ExcelIgnore
    @ExcelProperty(index = 1)
    private String employeeCode;
    /**
     * 姓名
     */
    @ExcelIgnore
    @ExcelProperty(index = 2)
    private String employeeName;
    @ExcelIgnore
    @ExcelProperty(index = 3)
    private String employmentStatus;
    /**
     * 性别:1男;2女
     */
    @ExcelIgnore
    @ExcelProperty(index = 4)
    private String employeeGender;
    /**
     * 证件号码
     */
    @ExcelIgnore
    @ExcelProperty(index = 5)
    private String identityCard;
    /**
     * 婚姻状况:0未婚;1已婚
     */
    @ExcelIgnore
    @ExcelProperty(index = 6)
    private  String maritalStatus;
    /**
     * 国籍
     */
    @ExcelIgnore
    @ExcelProperty(index = 7)
    private  String nationalityName;


    /**
     * 民族
     */
    @ExcelIgnore
    @ExcelProperty(index = 8)
    private  String nationName;
    /**
     * 户口所在地名称
     */
    @ExcelIgnore
    @ExcelProperty(index = 9)
    private  String residentCityName;

    /**
     * 参保地名称
     */
    @ExcelIgnore
    @ExcelProperty(index = 10)
    private  String insuredCityName;
    /**
     * 常住地名称
     */
    @ExcelIgnore
    @ExcelProperty(index = 11)
    private  String permanentAddressName;
    /**
     * 入职日期
     */
    @ExcelIgnore
    @ExcelProperty(index = 12)
    private  String employmentDate;
    /**
     * 离职日期
     */
    @ExcelIgnore
    @ExcelProperty(index = 13)
    private  String departureDate;
    /**
     * 部门
     */
    @ExcelIgnore
    @ExcelProperty(index = 14)
    private  String departmentName;
    /**
     * 岗位
     */
    @ExcelIgnore
    @ExcelProperty(index = 15)
    private  String postName;

    /**
     * 个人职级
     */
    @ExcelIgnore
    @ExcelProperty(index = 16)
    private  String employeeRankName;

    /**
     * 员工基本工资
     */
    @ExcelIgnore
    @JsonFormat(shape =JsonFormat.Shape.STRING)
    @ExcelProperty(index = 17)
    private String employeeBasicWage;
    /**
     * 员工手机号
     */
    @ExcelIgnore
    @ExcelProperty(index = 18)
    private  String employeeMobile;

    /**
     * 员工邮箱
     */
    @ExcelIgnore
    @ExcelProperty(index = 19)
    private  String employeeEmail;
    /**
     * 微信号
     */
    @ExcelIgnore
    @ExcelProperty(index = 20)
    private  String wechatCode;
    /**
     * 通信地址
     */
    @ExcelIgnore
    @ExcelProperty(index = 21)
    private  String contactAddress;
    /**
     * 通信地址详情
     */
    @ExcelIgnore
    @ExcelProperty(index = 22)
    private  String contactAddressDetail;
    /**
     * 紧急联系人
     */
    @ExcelIgnore
    @ExcelProperty(index = 23)
    private  String emergencyContact;
    /**
     * 紧急联系人电话
     */
    @ExcelIgnore
    @ExcelProperty(index = 24)
    private  String emergencyMobile;


}

