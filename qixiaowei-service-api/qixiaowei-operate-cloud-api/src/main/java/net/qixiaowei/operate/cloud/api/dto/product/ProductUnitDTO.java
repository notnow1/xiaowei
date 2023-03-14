package net.qixiaowei.operate.cloud.api.dto.product;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

/**
* 产品单位表
* @author TANGMICHI
* @since 2022-10-08
*/
@Data
@Accessors(chain = true)
public class ProductUnitDTO extends BaseDTO {

    //查询检验
    public interface QueryProductUnitDTO extends Default{

    }
    //新增检验
    public interface AddProductUnitDTO extends Default{

    }

    //删除检验
    public interface DeleteProductUnitDTO extends Default{

    }
    //修改检验
    public interface UpdateProductUnitDTO extends Default{

    }
    /**
    * ID
    */
    @NotNull(message = "产品单位id不能为空",groups = {ProductUnitDTO.UpdateProductUnitDTO.class,ProductUnitDTO.DeleteProductUnitDTO.class})
    private  Long productUnitId;
    /**
    * 产品单位编码
    */
    @NotBlank(message = "产品单位编码不能为空",groups = {ProductUnitDTO.AddProductUnitDTO.class,ProductUnitDTO.UpdateProductUnitDTO.class})
    private  String productUnitCode;
    /**
    * 产品单位名称
    */
    @NotBlank(message = "产品单位名称不能为空",groups = {ProductUnitDTO.AddProductUnitDTO.class,ProductUnitDTO.UpdateProductUnitDTO.class})
    private  String productUnitName;
    /**
    * 保留的小数位(0代表整数;1代表1位小数...)
    */
    @NotNull(message = "产品单位小数位不能为空",groups = {ProductUnitDTO.UpdateProductUnitDTO.class,ProductUnitDTO.UpdateProductUnitDTO.class})
    private  Integer reserveDigit;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;

}

