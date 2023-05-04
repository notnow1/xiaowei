package net.qixiaowei.system.manage.api.dto.basic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;
import net.qixiaowei.system.manage.api.vo.basic.EmployeeSalarySnapVO;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 员工表
 *
 * @author TANGMICHI
 * @since 2022-09-30
 */
@Data
@Accessors(chain = true)
public class EmployeeDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    //查询检验
    public interface QueryEmployeeDTO extends Default {

    }

    //新增检验
    public interface AddEmployeeDTO extends Default {

    }

    //新增检验
    public interface DeleteEmployeeDTO extends Default {

    }

    //修改检验
    public interface UpdateEmployeeDTO extends Default {

    }

    /**
     * 目标分解ID
     */
    private Long targetDecomposeId;
    /**
     * 目标分解ID集合
     */
    private List<Long> targetDecomposeIds;
    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = {EmployeeDTO.UpdateEmployeeDTO.class, EmployeeDTO.DeleteEmployeeDTO.class})
    private Long employeeId;

    /**
     * 人员ID集合
     */
    private List<Long> employeeIds;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 个人年终奖发送的用户id
     */
    private Long sendUserId;
    /**
     * 用户账号
     */
    private String userName;
    /**
     * 工号
     */
    @NotBlank(message = "工号不能为空", groups = {EmployeeDTO.AddEmployeeDTO.class, EmployeeDTO.UpdateEmployeeDTO.class})
    private String employeeCode;
    /**
     * 指标名称
     */
    private String indicatorName;
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空", groups = {EmployeeDTO.AddEmployeeDTO.class, EmployeeDTO.UpdateEmployeeDTO.class})
    private String employeeName;
    /**
     * 性别:1男;2女
     */
//    @NotNull(message = "性别不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private Integer employeeGender;
    /**
     * 证件类型
     */
//    @NotNull(message = "证件类型不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private Integer identityType;
    /**
     * 证件号码
     */
    @Pattern(regexp = "(^[1-9]\\d{5}(19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)", message = "请输入正确的身份证号码", groups = {EmployeeDTO.AddEmployeeDTO.class, EmployeeDTO.UpdateEmployeeDTO.class})
    @NotBlank(message = "证件号码不能为空", groups = {EmployeeDTO.AddEmployeeDTO.class, EmployeeDTO.UpdateEmployeeDTO.class})
    private String identityCard;
    /**
     * 出生日期
     */
//    @NotNull(message = "出生日期不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date employeeBirthday;
    /**
     * 员工手机号
     */
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$", message = "请输入正确的手机号码")
//    @NotBlank(message = "员工手机号不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private String employeeMobile;
    /**
     * 员工邮箱
     */
    @Email
//    @NotBlank(message = "员工邮箱不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private String employeeEmail;

    /**
     * 婚姻状况:0未婚;1已婚
     */
//    @NotNull(message = "婚姻状况不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private Integer maritalStatus;
    /**
     * 国籍
     */
//    @NotBlank(message = "国籍不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private String nationality;
    private String nationalityName;
    /**
     * 民族
     */
    private String nation;
    private String nationName;
    /**
     * 户口所在地
     */
//    @NotBlank(message = "户口所在地不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private String residentCity;
    /**
     * 户口所在地名称
     */
    private String residentCityName;
    /**
     * 参保地
     */
    private String insuredCity;

    /**
     * 参保地名称
     */
    private String insuredCityName;
    /**
     * 常住地
     */
    private String permanentAddress;
    /**
     * 常住地名称
     */
    private String permanentAddressName;

    /**
     * 岗位编码
     */
    private String postCode;
    /**
     * 部门编码
     */
    private String departmentCode;
    /**
     * 员工部门ID
     */
    @NotNull(message = "员工部门不能为空", groups = {EmployeeDTO.AddEmployeeDTO.class, EmployeeDTO.UpdateEmployeeDTO.class})
    private Long employeeDepartmentId;
    /**
     * 一级部门id
     */
    private Long topLevelDepartmentId;
    /**
     * 一级部门名称
     */
    private String topLevelDepartmentName;
    /**
     * 员工部门名称
     */
    private String employeeDepartmentName;
    /**
     * 员工岗位ID
     */
   // @NotNull(message = "员工岗位不能为空", groups = {EmployeeDTO.AddEmployeeDTO.class, EmployeeDTO.UpdateEmployeeDTO.class})
    private Long employeePostId;

    /**
     * 员工岗位名称
     */
    private String employeePostName;
    /**
     * 岗位职级名称
     */
    private String postRankName;

    /**
     * 员工职级
     */
   //@NotNull(message = "个人职级不能为空", groups = {EmployeeDTO.AddEmployeeDTO.class, EmployeeDTO.UpdateEmployeeDTO.class})
    private Integer employeeRank;

    private String employeeRankName;
    /**
     * 员工基本工资
     */
//    @NotNull(message = "员工基本工资不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private BigDecimal employeeBasicWage;
    /**
     * 入职日期
     */
//    @NotNull(message = "入职日期不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date employmentDate;
    /**
     * 离职日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date departureDate;
    /**
     * 用工状态:0离职;1在职
     */
//    @NotNull(message = "用工状态不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private Integer employmentStatus;
    private String employmentStatusName;
    /**
     * 状态:0暂存;1生效
     */
//    @NotNull(message = "状态不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private Integer status;

    /**
     * 微信号
     */
    private String wechatCode;

    /**
     * 紧急联系人
     */
//    @NotBlank(message = "紧急联系人不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private String emergencyContact;
    /**
     * 紧急联系人电话
     */
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$", message = "请输入正确的手机号码")
//    @NotBlank(message = "紧急联系人电话不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private String emergencyMobile;
    /**
     * 通信地址
     */
//    @NotBlank(message = "通信地址不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private String contactAddress;
    /**
     * 通信地址名称
     */
    private String contactAddressName;
    /**
     * 通信地址详情
     */
//    @NotBlank(message = "通信地址详情不能为空",groups = {EmployeeDTO.AddEmployeeDTO.class,EmployeeDTO.UpdateEmployeeDTO.class})
    private String contactAddressDetail;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 主管
     */
    private String inCharge;
    /**
     * 职级体系名称
     */
    private String officialRankSystemName;
    /**
     * 职级体系ID
     */
    private Long officialRankSystemId;
    /**
     * 司龄
     */
    private String workingAge;
    /**
     * 个人年终奖快照司龄
     */
    private String seniority;
    /**
     * 上年期末数
     */
    private Integer amountLastYear;
    /**
     * 预算年度
     */
    private Integer planYear;
    /**
     * 相同部门相同职级平均人数
     */
    private BigDecimal annualAverageNum;
    /**
     * 部门负责人id
     */
    private Long departmentLeaderId;
    /**
     * 部门负责人名称
     */
    private  String departmentLeaderName;
    /**
     * 考核负责人ID
     */
    private  Long examinationLeaderId;
    /**
     * 考核负责人名称
     */
    private  String examinationLeaderName;
    /**
     * 级别前缀编码
     */
    private String rankPrefixCode;
    /**
     * user 在职且有账号额度员工
     * 状态:user:生效在职有账号的员工 userAll:生效包含在职离职有账号的员工 1:生效包含在职离职的员工 不传为生效在职员工
     */
    private String  employeeFlag;
}

