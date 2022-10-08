package net.qixiaowei.operate.cloud.service.product;

import java.util.List;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;


/**
* ProductService接口
* @author TANGMICHI
* @since 2022-10-08
*/
public interface IProductService{
    /**
    * 查询产品表
    *
    * @param productId 产品表主键
    * @return 产品表
    */
    ProductDTO selectProductByProductId(Long productId);

    /**
    * 查询产品表列表
    *
    * @param productDTO 产品表
    * @return 产品表集合
    */
    List<ProductDTO> selectProductList(ProductDTO productDTO);

    /**
    * 新增产品表
    *
    * @param productDTO 产品表
    * @return 结果
    */
    int insertProduct(ProductDTO productDTO);

    /**
    * 修改产品表
    *
    * @param productDTO 产品表
    * @return 结果
    */
    int updateProduct(ProductDTO productDTO);

    /**
    * 批量修改产品表
    *
    * @param productDtos 产品表
    * @return 结果
    */
    int updateProducts(List<ProductDTO> productDtos);

    /**
    * 批量新增产品表
    *
    * @param productDtos 产品表
    * @return 结果
    */
    int insertProducts(List<ProductDTO> productDtos);

    /**
    * 逻辑批量删除产品表
    *
    * @param ProductDtos 需要删除的产品表集合
    * @return 结果
    */
    int logicDeleteProductByProductIds(List<ProductDTO> ProductDtos);

    /**
    * 逻辑删除产品表信息
    *
    * @param productDTO
    * @return 结果
    */
    int logicDeleteProductByProductId(ProductDTO productDTO);
    /**
    * 逻辑批量删除产品表
    *
    * @param ProductDtos 需要删除的产品表集合
    * @return 结果
    */
    int deleteProductByProductIds(List<ProductDTO> ProductDtos);

    /**
    * 逻辑删除产品表信息
    *
    * @param productDTO
    * @return 结果
    */
    int deleteProductByProductId(ProductDTO productDTO);


    /**
    * 删除产品表信息
    *
    * @param productId 产品表主键
    * @return 结果
    */
    int deleteProductByProductId(Long productId);
}
