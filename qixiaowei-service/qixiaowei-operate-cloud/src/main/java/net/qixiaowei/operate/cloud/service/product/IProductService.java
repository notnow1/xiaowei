package net.qixiaowei.operate.cloud.service.product;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.excel.product.ProductExportExcel;
import net.qixiaowei.operate.cloud.excel.product.ProductExcel;


/**
 * ProductService接口
 *
 * @author TANGMICHI
 * @since 2022-10-08
 */
public interface IProductService {
    /**
     * 查询产品表
     *
     * @param productId 产品表主键
     * @return 产品表
     */
    ProductDTO selectProductByProductId(Long productId);

    /**
     * 查询产品表列表-列表结构
     *
     * @param productDTO 产品表
     * @return 产品表集合
     */
    List<ProductDTO> selectProduct(ProductDTO productDTO);
    /**
     * 处理返回
     *
     * @param result 返回集合
     * @return 返回集合
     */
    void handleResult(List<ProductDTO> result);
    /**
     * 查询产品表列表-树结构
     *
     * @param productDTO 产品表
     * @return 产品表集合
     */
    List<ProductDTO> selectProductList(ProductDTO productDTO);

    /**
     * 查询产品表列表-平铺下拉
     *
     * @param productDTO 产品表
     * @return 产品表集合
     */
    List<ProductDTO> selectDropList(ProductDTO productDTO);

    /**
     * 生成产品编码
     *
     * @return 产品编码
     */
    String generateProductCode();

    /**
     * 新增产品表
     *
     * @param productDTO 产品表
     * @return 结果
     */
    ProductDTO insertProduct(ProductDTO productDTO);

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
     * @param productIds 需要删除的产品表集合
     * @return 结果
     */
    int logicDeleteProductByProductIds(List<Long> productIds);

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

    /**
     * 查询产品是否用到枚举
     *
     * @param productDTO
     * @return
     */
    List<ProductDTO> queryProductQuote(ProductDTO productDTO);

    /**
     * 返回产品层级
     *
     * @return
     */
    List<Integer> selectLevel();

    /**
     * 查询上级产品
     *
     * @return
     * @param productId
     */
    List<ProductDTO> queryparent(Long productId);

    /**
     * 根据产品IDS获取产品列表
     *
     * @param productIds
     * @return
     */
    List<ProductDTO> selectProductList(List<Long> productIds);

    /**
     * 导入产品
     * @param list
     */
    void importProduct(List<ProductExcel> list);

    /**
     * 将树按顺序打平(导出产品用)
     * @return
     */
    List<Long> treeToList();

    /**
     * 导出产品列表
     * @return
     * @param productIds
     */
    List<ProductExportExcel> exportProduct(List<Long> productIds);

    /**
     * 根据id查询所有子级数据
     * @param productId
     * @return
     */
    List<ProductDTO> selectAncestors(Long productId);
}
