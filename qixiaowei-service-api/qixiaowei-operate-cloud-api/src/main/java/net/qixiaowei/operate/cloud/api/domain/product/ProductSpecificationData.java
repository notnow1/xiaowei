package net.qixiaowei.operate.cloud.api.domain.product;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 产品规格数据表
* @author TANGMICHI
* @since 2022-10-08
*/
@Data
@Accessors(chain = true)
public class ProductSpecificationData extends BaseEntity {

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
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

