package net.qixiaowei.operate.cloud.api.domain.product;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
* 产品文件表
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class ProductFile extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  productFileId;
     /**
     * 产品ID
     */
     private  Long  productId;
     /**
     * 产品文件名称
     */
     private  String  productFileName;
     /**
     * 产品文件格式
     */
     private  String  productFileFormat;
     /**
     * 产品文件大小
     */
     private  Long  productFileSize;
     /**
     * 产品文件路径
     */
     private  String  productFilePath;
     /**
     * 排序
     */
     private  Integer  sort;


}

