package net.qixiaowei.system.manage.api.domain.tenant;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 租户联系人表
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class TenantContacts extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  tenantContactsId;
     /**
     * 租户ID
     */
     private  Long  tenantId;
     /**
     * 联系人姓名
     */
     private  String  contactName;
     /**
     * 联系人手机号
     */
     private  String  contactTel;
     /**
     * 联系人邮箱
     */
     private  String  contactEmail;
     /**
     * 联系人职务
     */
     private  String  contactDuty;

}

