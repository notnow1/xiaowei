package net.qixiaowei.operate.cloud.api.dto.product;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

/**
* 产品表
* @author TANGMICHI
* @since 2022-10-08
*/
@Data
@Accessors(chain = true)
public class ProductDTO extends BaseDTO {

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
    @NotNull(message = "产品ID不能为空",groups = {ProductDTO.DeleteProductDTO.class,ProductDTO.UpdateProductDTO.class})
    private  Long productId;
    /**
     * 产品id集合
     */
    private  List<Long> productIds;
    /**
    * 父级产品ID
    */
    private  Long parentProductId;
    /**
     * 父级产品名称
     */
    private  String parentProductName;
    /**
    * 祖级列表ID，按层级用英文逗号隔开
    */
    private  String ancestors;
    /**
    * 产品编码
    */
    @NotBlank(message = "产品编码不能为空",groups = {ProductDTO.AddProductDTO.class,ProductDTO.UpdateProductDTO.class})
    private  String productCode;
    /**
    * 产品名称
    */
    @NotBlank(message = "产品名称不能为空",groups = {ProductDTO.AddProductDTO.class,ProductDTO.UpdateProductDTO.class})
    private  String productName;
    /**
     * 指标id
     */
    private String indicatorName;
    /**
     * 指标名称
     */
    private Long indicatorId;
    /**
    * 层级
    */
    @NotNull(message = "层级不能为空",groups = {ProductDTO.AddProductDTO.class,ProductDTO.UpdateProductDTO.class})
    private  Integer level;
    /**
    * 产品单位ID
    */
    @NotNull(message = "产品量纲不能为空",groups = {ProductDTO.AddProductDTO.class,ProductDTO.UpdateProductDTO.class})
    private  Long productUnitId;
    private  String productUnitName;
    /**
    * 产品类别
    */
//    @NotBlank(message = "产品类别不能为空",groups = {ProductDTO.AddProductDTO.class,ProductDTO.UpdateProductDTO.class})
    private  String productCategory;
    private  String productCategoryName;
    /**
    * 产品描述
    */
    private  String productDescription;
    /**
    * 目录价
    */
    private BigDecimal listPrice;
    /**
    * 上架标记：0下架;1上架
    */
    @NotNull(message = "是否上下架不能为空",groups = {ProductDTO.AddProductDTO.class,ProductDTO.UpdateProductDTO.class})
    private  Integer listingFlag;

    private  String listingFlagName;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
     * 产品规格参数表集合
     */
    private List<ProductSpecificationParamDTO> productSpecificationParamDTOList;

    /**
     * 产品规格数据表集合
     */
    private List<ProductDataDTO> productDataDTOList;

    /**
     * 产品文件表集合
     */
    private List<ProductFileDTO> productFileDTOList;
    /**
     * 组织子节点信息
     */
    private List<ProductDTO> children;
}

