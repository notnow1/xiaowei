package net.qixiaowei.system.manage.api.domain.productPackage;

import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
* 产品包
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class ProductPackage extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  productPackageId;
     /**
     * 产品包名
     */
     private  String  productPackageName;
     /**
     * 产品包描述
     */
     private  String  productPackageDescription;
     /**
     * 排序
     */
     private  Integer  sort;


}

