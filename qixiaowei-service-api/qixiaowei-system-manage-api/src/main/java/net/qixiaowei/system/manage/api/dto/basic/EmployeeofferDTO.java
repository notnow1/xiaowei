package net.qixiaowei.system.manage.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.util.Date;

@Data
@Accessors(chain = true)
public class EmployeeofferDTO {

    //查询检验
    public interface QueryEmployeeofferDTO extends Default {

    }
    //新增检验
    public interface AddEmployeeofferDTO extends Default{

    }

    //新增检验
    public interface DeleteEmployeeofferDTO extends Default{

    }
    //修改检验
    public interface UpdateEmployeeofferDTO extends Default{

    }
    /**
     * ID
     */
    private  Long employeeInfoId;
    /**
     * 员工ID
     */
    private  Long employeeId;
    /**
     * 婚姻状况:0未婚;1已婚
     */
    private  Integer maritalStatus;
    /**
     * 国籍
     */
    private  String nationality;
    /**
     * 民族
     */
    private  String nation;
    /**
     * 户口所在地
     */
    private  String residentCity;
    /**
     * 参保地
     */
    private  String insuredCity;
    /**
     * 常住地
     */
    private  String permanentAddress;
    /**
     * 通信地址
     */
    @NotBlank(message = "通信地址不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private  String contactAddress;
    /**
     * 员工手机号
     */
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$", message = "手机号码有误！")
    @NotBlank(message = "员工手机号不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private  String employeeMobile;
    /**
     * 员工邮箱
     */
    @Email
    @NotBlank(message = "员工邮箱不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private  String employeeEmail;
    /**
     * 通信地址详情
     */
    @NotBlank(message = "通信地址详情不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private  String contactAddressDetail;
    /**
     * 微信号
     */
    private  String wechatCode;
    /**
     * 紧急联系人
     */
    @NotBlank(message = "紧急联系人不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private  String emergencyContact;
    /**
     * 紧急联系人电话
     */
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$", message = "手机号码有误！")
    @NotBlank(message = "紧急联系人电话不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private  String emergencyMobile;
    /**
     * 创建人
     */
    private  Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private  Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;
}
