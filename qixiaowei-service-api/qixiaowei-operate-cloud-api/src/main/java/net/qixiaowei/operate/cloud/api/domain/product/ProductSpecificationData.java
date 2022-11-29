package net.qixiaowei.operate.cloud.api.domain.product;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
* 产品规格数据表
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class ProductSpecificationData extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  productSpecificationDataId;
     /**
     * 产品ID
     */
     private  Long  productId;
     /**
     * 产品规格ID
     */
     private  Long  productSpecificationId;
     /**
     * 产品规格参数ID
     */
     private  Long  productSpecificationParamId;
     /**
     * 产品规格参数数值
     */
     private  String  value;

}

