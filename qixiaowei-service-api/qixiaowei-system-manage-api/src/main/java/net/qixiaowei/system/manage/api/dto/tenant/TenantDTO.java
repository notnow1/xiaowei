package net.qixiaowei.system.manage.api.dto.tenant;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;

/**
* 租户表
* @author TANGMICHI
* @since 2022-09-24
*/
@Data
@Accessors(chain = true)
public class TenantDTO {
    //查询检验
    public interface QueryTenantDTO extends Default{

    }
    //新增检验
    public interface AddTenantDTO extends Default{

    }

    //删除检验
    public interface DeleteTenantDTO extends Default{

    }
    //修改检验
    public interface UpdateTenantDTO extends Default{

    }
    //修改检验
    public interface UpdateTenantInfoDTO extends Default{

    }
    /**
    * 租户ID
    */
    @NotNull(message = "租户ID不能为空",groups = {TenantDTO.DeleteTenantDTO.class,TenantDTO.UpdateTenantDTO.class,TenantDTO.UpdateTenantInfoDTO.class})
    private  Long tenantId;
    /**
    * 租户编码
    */
    private  String tenantCode;
    /**
    * 租户名称
    */
    @NotBlank(message = "租户名称不能为空",groups = {TenantDTO.AddTenantDTO.class,TenantDTO.UpdateTenantDTO.class,TenantDTO.UpdateTenantInfoDTO.class})
    private  String tenantName;
    /**
    * 租户地址
    */
    private  String tenantAddress;
    /**
    * 租户行业
    */
    @NotNull(message = "租户行业不能为空",groups = {TenantDTO.AddTenantDTO.class,TenantDTO.UpdateTenantDTO.class,TenantDTO.UpdateTenantInfoDTO.class})
    private  Long tenantIndustry;

    /**
     * 租户行业名称
     */
    private  String tenantIndustryName;
    /**
    * 域名
    */
    @NotBlank(message = "域名不能为空",groups = {TenantDTO.AddTenantDTO.class,TenantDTO.UpdateTenantDTO.class,TenantDTO.UpdateTenantInfoDTO.class})
    private  String domain;
    /**
    * 管理员帐号
    */
    @NotBlank(message = "管理员帐号不能为空",groups = {TenantDTO.AddTenantDTO.class,TenantDTO.UpdateTenantDTO.class,TenantDTO.UpdateTenantInfoDTO.class})
    private  String adminAccount;
    /**
    * 管理员密码
    */
    @NotBlank(message = "管理员密码不能为空",groups = {TenantDTO.AddTenantDTO.class,TenantDTO.UpdateTenantDTO.class,TenantDTO.UpdateTenantInfoDTO.class})
    private  String adminPassword;
    /**
    * 客服人员
    */
    @NotBlank(message = "客服人员不能为空",groups = {TenantDTO.AddTenantDTO.class,TenantDTO.UpdateTenantDTO.class,TenantDTO.UpdateTenantInfoDTO.class})
    private  String supportStaff;


    /**
     * 客服人员名称
     */
    private  String supportStaffName;
    /**
    * 租户登录背景图片URL
    */
    private  String loginBackground;
    /**
    * 租户logo图片URL
    */
    private  String tenantLogo;
    /**
    * 状态（0待初始化 1正常 2禁用 3过期）
    */
    @NotNull(message = "租户状态不能为空",groups = {TenantDTO.AddTenantDTO.class,TenantDTO.UpdateTenantDTO.class,TenantDTO.UpdateTenantInfoDTO.class})
    private  Integer tenantStatus;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 创建人
    */
    private  Long createBy;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

    /**
     * 合同开始时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private  Date  contractStartTime;
    /**
     * 合同结束时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private  Date  contractEndTime;

    /**
     * 租户联系人表
     */
    private List<TenantContactsDTO> tenantContactsDTOList;
    /**
     * 租户合同信息表
     */
    @NotEmpty(message = "租户合同信息不能为空",groups = {TenantDTO.AddTenantDTO.class})
    @Valid
    private List<TenantContractDTO> tenantContractDTOList;
    /**
     * 租户域名申请表
     */
    private List<TenantDomainApprovalDTO> tenantDomainApprovalDTOList;

    /**
     * 申请状态:0待审核;1审核通过;2审核驳回
     */
    private  Integer approvalStatus;
}

