package net.qixiaowei.operate.cloud.api.dto.product;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 产品规格参数表
* @author TANGMICHI
* @since 2022-10-08
*/
@Data
@Accessors(chain = true)
public class ProductSpecificationParamDTO {

    //查询检验
    public interface QueryProductSpecificationParamDTO extends Default{

    }
    //新增检验
    public interface AddProductSpecificationParamDTO extends Default{

    }

    //删除检验
    public interface DeleteProductSpecificationParamDTO extends Default{

    }
    //修改检验
    public interface UpdateProductSpecificationParamDTO extends Default{

    }
    /**
    * ID
    */
    private  Long productSpecificationParamId;
    /**
    * 产品ID
    */
    private  Long productId;
    /**
    * 规格参数名称
    */
    private  String specificationParamName;
    /**
    * 排序
    */
    private  Integer sort;
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

