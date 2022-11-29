package net.qixiaowei.operate.cloud.api.domain.product;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
* 产品规格表
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class ProductSpecification extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  productSpecificationId;
     /**
     * 产品ID
     */
     private  Long  productId;
     /**
     * 规格名称
     */
     private  String  specificationName;
     /**
     * 目录价,单位元
     */
     private BigDecimal listPrice;

}

