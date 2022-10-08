package net.qixiaowei.operate.cloud.api.domain.product;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 产品规格表
* @author TANGMICHI
* @since 2022-10-08
*/
@Data
@Accessors(chain = true)
public class ProductSpecification extends BaseEntity {

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
     private  BigDecimal  listPrice;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

