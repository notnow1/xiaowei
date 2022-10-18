package net.qixiaowei.operate.cloud.mapper.product;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.product.Product;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDataDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* ProductMapper接口
* @author TANGMICHI
* @since 2022-10-08
*/
public interface ProductMapper{
    /**
    * 查询产品表
    *
    * @param productId 产品表主键
    * @return 产品表
    */
    ProductDTO selectProductByProductId(@Param("productId")Long productId);

    /**
     * 根据产品单位ID查询产品表
     *
     * @param productUnitId 产品表主键
     * @return 产品表
     */
    List<ProductDTO> selectProductByProductUnitId(@Param("productUnitId")Long productUnitId);


    /**
     * 根据产品单位ID批量查询产品表
     *
     * @param productUnitIds 产品表主键
     * @return 产品表
     */
    List<ProductDTO> selectProductByProductUnitIds(@Param("productUnitIds")List<Long> productUnitIds);

    /**
     * 根据产品编码查询产品表
     *
     * @param productCode 产品表主键
     * @return 产品表
     */
    ProductDTO selectProductByProductCode(@Param("productCode")String productCode);
    /**
    * 查询产品表列表
    *
    * @param product 产品表
    * @return 产品表集合
    */
    List<ProductDTO> selectProductList(@Param("product")Product product);

    /**
    * 新增产品表
    *
    * @param product 产品表
    * @return 结果
    */
    int insertProduct(@Param("product")Product product);

    /**
    * 修改产品表
    *
    * @param product 产品表
    * @return 结果
    */
    int updateProduct(@Param("product")Product product);

    /**
    * 批量修改产品表
    *
    * @param productList 产品表
    * @return 结果
    */
    int updateProducts(@Param("productList")List<Product> productList);
    /**
    * 逻辑删除产品表
    *
    * @param product
    * @return 结果
    */
    int logicDeleteProductByProductId(@Param("product")Product product,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
    * 逻辑批量删除产品表
    *
    * @param productIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteProductByProductIds(@Param("productIds")List<Long> productIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除产品表
    *
    * @param productId 产品表主键
    * @return 结果
    */
    int deleteProductByProductId(@Param("productId")Long productId);

    /**
    * 物理批量删除产品表
    *
    * @param productIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteProductByProductIds(@Param("productIds")List<Long> productIds);

    /**
    * 批量新增产品表
    *
    * @param Products 产品表列表
    * @return 结果
    */
    int batchProduct(@Param("products")List<Product> Products);

    /**
     * 根据父级id查询数据
     * @param parentProductId
     * @return
     */
    ProductDTO selectProductByParentProductId(@Param("parentProductId")Long parentProductId);

    /**
     * 查询产品是否用到枚举
     * @param product
     * @return
     */
    List<ProductDTO> queryDictionaryType(@Param("product")Product product);

    /**
     * 返回产品层级
     * @return
     */
    List<Integer> selectProdLevel();
}
