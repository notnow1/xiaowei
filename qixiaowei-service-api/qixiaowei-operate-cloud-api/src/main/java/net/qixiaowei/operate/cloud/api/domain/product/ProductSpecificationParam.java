package net.qixiaowei.operate.cloud.api.domain.product;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
* 产品规格参数表
* @author TANGMICHI
* @since 2022-10-08
*/
@Data
@Accessors(chain = true)
public class ProductSpecificationParam extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  productSpecificationParamId;
     /**
     * 产品ID
     */
     private  Long  productId;
     /**
     * 规格参数名称
     */
     private  String  specificationParamName;
     /**
     * 排序
     */
     private  Integer  sort;

}

