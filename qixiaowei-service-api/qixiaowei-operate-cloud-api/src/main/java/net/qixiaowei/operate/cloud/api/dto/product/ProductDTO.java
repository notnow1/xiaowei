package net.qixiaowei.operate.cloud.api.dto.product;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 产品表
* @author TANGMICHI
* @since 2022-10-08
*/
@Data
@Accessors(chain = true)
public class ProductDTO {

    //查询检验
    public interface QueryProductDTO extends Default{

    }
    //新增检验
    public interface AddProductDTO extends Default{

    }

    //删除检验
    public interface DeleteProductDTO extends Default{

    }
    //修改检验
    public interface UpdateProductDTO extends Default{

    }
    /**
    * ID
    */
    private  Long productId;
    /**
    * 父级产品ID
    */
    private  Long parentProductId;
    /**
    * 祖级列表ID，按层级用英文逗号隔开
    */
    private  String ancestors;
    /**
    * 产品编码
    */
    private  String productCode;
    /**
    * 产品名称
    */
    private  String productName;
    /**
    * 层级
    */
    private  Integer level;
    /**
    * 产品单位ID
    */
    private  Long productUnitId;
    /**
    * 产品类别
    */
    private  String productCategory;
    /**
    * 产品描述
    */
    private  String productDescription;
    /**
    * 目录价
    */
    private  BigDecimal listPrice;
    /**
    * 上架标记：0下架;1上架
    */
    private  Integer listingFlag;
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

