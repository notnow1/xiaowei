package net.qixiaowei.system.manage.api.domain.tenant;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 租户合同信息
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class TenantContract extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  tenantContractId;
     /**
     * 租户ID
     */
     private  Long  tenantId;
     /**
     * 销售合同编号
     */
     private  String  salesContractNo;
     /**
     * 销售人员
     */
     private  String  salesPersonnel;
     /**
     * 合同金额
     */
     private BigDecimal contractAmount;
     /**
     * 合同开始时间
     */
     private  Date   contractStartTime;
     /**
     * 合同结束时间
     */
     private  Date   contractEndTime;
     /**
     * 开通的产品包
     */
     private  String  productPackage;

}

