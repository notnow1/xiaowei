package net.qixiaowei.operate.cloud.service.impl.product;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.product.Product;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.mapper.product.ProductMapper;
import net.qixiaowei.operate.cloud.service.product.IProductService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* ProductService业务层处理
* @author TANGMICHI
* @since 2022-10-08
*/
@Service
public class ProductServiceImpl implements IProductService{
    @Autowired
    private ProductMapper productMapper;

    /**
    * 查询产品表
    *
    * @param productId 产品表主键
    * @return 产品表
    */
    @Override
    public ProductDTO selectProductByProductId(Long productId)
    {
    return productMapper.selectProductByProductId(productId);
    }

    /**
    * 查询产品表列表
    *
    * @param productDTO 产品表
    * @return 产品表
    */
    @Override
    public List<ProductDTO> selectProductList(ProductDTO productDTO)
    {
    Product product=new Product();
    BeanUtils.copyProperties(productDTO,product);
    return productMapper.selectProductList(product);
    }

    /**
    * 新增产品表
    *
    * @param productDTO 产品表
    * @return 结果
    */
    @Transactional
    @Override
    public int insertProduct(ProductDTO productDTO){
    Product product=new Product();
    BeanUtils.copyProperties(productDTO,product);
    product.setCreateBy(SecurityUtils.getUserId());
    product.setCreateTime(DateUtils.getNowDate());
    product.setUpdateTime(DateUtils.getNowDate());
    product.setUpdateBy(SecurityUtils.getUserId());
    product.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    return productMapper.insertProduct(product);
    }

    /**
    * 修改产品表
    *
    * @param productDTO 产品表
    * @return 结果
    */
    @Transactional
    @Override
    public int updateProduct(ProductDTO productDTO)
    {
    Product product=new Product();
    BeanUtils.copyProperties(productDTO,product);
    product.setUpdateTime(DateUtils.getNowDate());
    product.setUpdateBy(SecurityUtils.getUserId());
    return productMapper.updateProduct(product);
    }

    /**
    * 逻辑批量删除产品表
    *
    * @param productDtos 需要删除的产品表主键
    * @return 结果
    */

    @Transactional
    @Override
    public int logicDeleteProductByProductIds(List<ProductDTO> productDtos){
            List<Long> stringList = new ArrayList();
            for (ProductDTO productDTO : productDtos) {
                stringList.add(productDTO.getProductId());
            }
    return productMapper.logicDeleteProductByProductIds(stringList,productDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
    * 物理删除产品表信息
    *
    * @param productId 产品表主键
    * @return 结果
    */

    @Transactional
    @Override
    public int deleteProductByProductId(Long productId)
    {
    return productMapper.deleteProductByProductId(productId);
    }

     /**
     * 逻辑删除产品表信息
     *
     * @param  productDTO 产品表
     * @return 结果
     */
     @Transactional
     @Override
     public int logicDeleteProductByProductId(ProductDTO productDTO)
     {
     Product product=new Product();
     BeanUtils.copyProperties(productDTO,product);
     return productMapper.logicDeleteProductByProductId(product,SecurityUtils.getUserId(),DateUtils.getNowDate());
     }

     /**
     * 物理删除产品表信息
     *
     * @param  productDTO 产品表
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteProductByProductId(ProductDTO productDTO)
     {
     Product product=new Product();
     BeanUtils.copyProperties(productDTO,product);
     return productMapper.deleteProductByProductId(product.getProductId());
     }
     /**
     * 物理批量删除产品表
     *
     * @param productDtos 需要删除的产品表主键
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteProductByProductIds(List<ProductDTO> productDtos){
     List<Long> stringList = new ArrayList();
     for (ProductDTO productDTO : productDtos) {
     stringList.add(productDTO.getProductId());
     }
     return productMapper.deleteProductByProductIds(stringList);
     }

    /**
    * 批量新增产品表信息
    *
    * @param productDtos 产品表对象
    */
    @Transactional
    public int insertProducts(List<ProductDTO> productDtos){
      List<Product> productList = new ArrayList();

    for (ProductDTO productDTO : productDtos) {
      Product product =new Product();
      BeanUtils.copyProperties(productDTO,product);
       product.setCreateBy(SecurityUtils.getUserId());
       product.setCreateTime(DateUtils.getNowDate());
       product.setUpdateTime(DateUtils.getNowDate());
       product.setUpdateBy(SecurityUtils.getUserId());
       product.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      productList.add(product);
    }
    return productMapper.batchProduct(productList);
    }

    /**
    * 批量修改产品表信息
    *
    * @param productDtos 产品表对象
    */
    @Transactional
    public int updateProducts(List<ProductDTO> productDtos){
     List<Product> productList = new ArrayList();

     for (ProductDTO productDTO : productDtos) {
     Product product =new Product();
     BeanUtils.copyProperties(productDTO,product);
        product.setCreateBy(SecurityUtils.getUserId());
        product.setCreateTime(DateUtils.getNowDate());
        product.setUpdateTime(DateUtils.getNowDate());
        product.setUpdateBy(SecurityUtils.getUserId());
     productList.add(product);
     }
     return productMapper.updateProducts(productList);
    }
}

