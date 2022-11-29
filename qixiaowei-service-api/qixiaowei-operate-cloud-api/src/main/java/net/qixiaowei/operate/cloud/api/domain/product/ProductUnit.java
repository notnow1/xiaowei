package net.qixiaowei.operate.cloud.api.domain.product;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
* 产品单位表
* @author TANGMICHI
* @since 2022-10-08
*/
@Data
@Accessors(chain = true)
public class ProductUnit extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  productUnitId;
     /**
     * 产品单位编码
     */
     private  String  productUnitCode;
     /**
     * 产品单位名称
     */
     private  String  productUnitName;
     /**
     * 保留的小数位(0代表整数;1代表1位小数...)
     */
     private  Integer  reserveDigit;

}

