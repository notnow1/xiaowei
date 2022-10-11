package net.qixiaowei.system.manage.api.vo.productPackage;

import lombok.Data;

/**
 * 产品包返回列表
 *
 * @author hzk
 * @since 2022-10-11
 */
@Data
public class ProductPackageVO {

    /**
     * ID
     */
    private Long productPackageId;
    /**
     * 产品包名
     */
    private String productPackageName;

}

