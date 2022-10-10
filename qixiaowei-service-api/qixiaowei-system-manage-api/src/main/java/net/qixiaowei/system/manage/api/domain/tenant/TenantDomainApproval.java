package net.qixiaowei.system.manage.api.domain.tenant;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 租户域名申请
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class TenantDomainApproval extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  tenantDomainApprovalId;
     /**
     * 租户ID
     */
     private  Long  tenantId;
     /**
     * 申请域名
     */
     private  String  approvalDomain;
     /**
     * 申请人用户ID
     */
     private  Long  applicantUserId;
     /**
     * 申请人账户
     */
     private  String  applicantUserAccount;
     /**
     * 提交时间
     */
     private  Date   submissionTime;
     /**
     * 审核时间
     */
     private  Date   approvalTime;
     /**
     * 审核人用户ID
     */
     private  Long  approvalUserId;
     /**
     * 申请状态:0待审核;1审核通过;2审核驳回
     */
     private  Integer  approvalStatus;
     /**
     * 删除标记
     */
     private  Integer  deleteFlag;

}

