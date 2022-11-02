package net.qixiaowei.operate.cloud.api.dto.product;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 产品规格数据表
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class ProductSpecificationDataDTO {

    //查询检验
    public interface QueryProductSpecificationDataDTO extends Default{

    }
    //新增检验
    public interface AddProductSpecificationDataDTO extends Default{

    }

    //删除检验
    public interface DeleteProductSpecificationDataDTO extends Default{

    }
    //修改检验
    public interface UpdateProductSpecificationDataDTO extends Default{

    }


    /**
    * ID
    */
    private  Long productSpecificationDataId;
    /**
    * 产品ID
    */
    private  Long productId;
    /**
    * 产品规格ID
    */
    private  Long productSpecificationId;
    /**
    * 产品规格参数ID
    */
    private  Long productSpecificationParamId;

    /**
     * 规格名称
     */
    private  String specificationName;
    /**
     * 目录价,单位元
     */
    private BigDecimal listPrice;
    /**
    * 产品规格参数数值
    */
    private  String value;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 创建人
    */
    private  Long createBy;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

