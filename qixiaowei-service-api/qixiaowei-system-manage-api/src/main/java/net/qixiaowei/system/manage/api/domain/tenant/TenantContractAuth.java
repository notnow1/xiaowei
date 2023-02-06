package net.qixiaowei.system.manage.api.domain.tenant;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 租户合同授权表
* @author hzk
* @since 2023-01-31
*/
@Data
@Accessors(chain = true)
public class TenantContractAuth extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  tenantContractAuthId;
     /**
     * 租户合同ID
     */
     private  Long  tenantContractId;
     /**
     * 菜单ID
     */
     private  Long  menuId;

}

