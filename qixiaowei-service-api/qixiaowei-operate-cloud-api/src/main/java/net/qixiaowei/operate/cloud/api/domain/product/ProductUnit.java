package net.qixiaowei.operate.cloud.api.domain.product;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 产品单位表
* @author TANGMICHI
* @since 2022-10-08
*/
@Data
@Accessors(chain = true)
public class ProductUnit extends BaseEntity {

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
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

