package net.qixiaowei.operate.cloud.api.domain.product;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 产品规格参数表
* @author TANGMICHI
* @since 2022-10-08
*/
@Data
@Accessors(chain = true)
public class ProductSpecificationParam extends BaseEntity {

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
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

