package net.qixiaowei.system.manage.api.dto.tenant;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 租户域名申请
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class TenantDomainApprovalDTO {

    //查询检验
    public interface QueryTenantDomainApprovalDTO extends Default{

    }
    //新增检验
    public interface AddTenantDomainApprovalDTO extends Default{

    }

    //删除检验
    public interface DeleteTenantDomainApprovalDTO extends Default{

    }
    //修改检验
    public interface UpdateTenantDomainApprovalDTO extends Default{

    }
    /**
    * ID
    */
    private  Long tenantDomainApprovalId;
    /**
    * 租户ID
    */
    private  Long tenantId;
    /**
    * 申请域名
    */
    private  String approvalDomain;
    /**
    * 申请人用户ID
    */
    private  Long applicantUserId;
    /**
    * 申请人账户
    */
    private  String applicantUserAccount;
    /**
    * 提交时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  submissionTime;
    /**
    * 审核时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  approvalTime;
    /**
    * 审核人用户ID
    */
    private  Long approvalUserId;
    /**
    * 申请状态:0待审核;1审核通过;2审核驳回
    */
    private  Integer approvalStatus;
    /**
    * 备注
    */
    private  String remark;
    /**
    * 删除标记
    */
    private  Integer deleteFlag;
    /**
    * 创建人
    */
    private  Long createBy;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

