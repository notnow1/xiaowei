package net.qixiaowei.operate.cloud.api.dto.product;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 产品规格表
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class ProductSpecificationDTO {

    //查询检验
    public interface QueryProductSpecificationDTO extends Default{

    }
    //新增检验
    public interface AddProductSpecificationDTO extends Default{

    }

    //删除检验
    public interface DeleteProductSpecificationDTO extends Default{

    }
    //修改检验
    public interface UpdateProductSpecificationDTO extends Default{

    }
    /**
    * ID
    */
    private  Long productSpecificationId;
    /**
    * 产品ID
    */
    private  Long productId;
    /**
    * 规格名称
    */
    private  String specificationName;
    /**
    * 目录价,单位元
    */
    private BigDecimal listPrice;
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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

