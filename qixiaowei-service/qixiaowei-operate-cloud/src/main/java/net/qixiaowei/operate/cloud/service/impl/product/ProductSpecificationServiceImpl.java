package net.qixiaowei.operate.cloud.service.impl.product;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.product.ProductSpecification;
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationDTO;
import net.qixiaowei.operate.cloud.mapper.product.ProductSpecificationMapper;
import net.qixiaowei.operate.cloud.service.product.IProductSpecificationService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* ProductSpecificationService业务层处理
* @author TANGMICHI
* @since 2022-10-09
*/
@Service
public class ProductSpecificationServiceImpl implements IProductSpecificationService{
    @Autowired
    private ProductSpecificationMapper productSpecificationMapper;

    /**
    * 查询产品规格表
    *
    * @param productSpecificationId 产品规格表主键
    * @return 产品规格表
    */
    @Override
    public ProductSpecificationDTO selectProductSpecificationByProductSpecificationId(Long productSpecificationId)
    {
    return productSpecificationMapper.selectProductSpecificationByProductSpecificationId(productSpecificationId);
    }

    /**
    * 查询产品规格表列表
    *
    * @param productSpecificationDTO 产品规格表
    * @return 产品规格表
    */
    @Override
    public List<ProductSpecificationDTO> selectProductSpecificationList(ProductSpecificationDTO productSpecificationDTO)
    {
    ProductSpecification productSpecification=new ProductSpecification();
    BeanUtils.copyProperties(productSpecificationDTO,productSpecification);
    return productSpecificationMapper.selectProductSpecificationList(productSpecification);
    }

    /**
    * 新增产品规格表
    *
    * @param productSpecificationDTO 产品规格表
    * @return 结果
    */
    @Override
    public int insertProductSpecification(ProductSpecificationDTO productSpecificationDTO){
    ProductSpecification productSpecification=new ProductSpecification();
    BeanUtils.copyProperties(productSpecificationDTO,productSpecification);
    productSpecification.setCreateBy(SecurityUtils.getUserId());
    productSpecification.setCreateTime(DateUtils.getNowDate());
    productSpecification.setUpdateTime(DateUtils.getNowDate());
    productSpecification.setUpdateBy(SecurityUtils.getUserId());
    productSpecification.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    return productSpecificationMapper.insertProductSpecification(productSpecification);
    }

    /**
    * 修改产品规格表
    *
    * @param productSpecificationDTO 产品规格表
    * @return 结果
    */
    @Override
    public int updateProductSpecification(ProductSpecificationDTO productSpecificationDTO)
    {
    ProductSpecification productSpecification=new ProductSpecification();
    BeanUtils.copyProperties(productSpecificationDTO,productSpecification);
    productSpecification.setUpdateTime(DateUtils.getNowDate());
    productSpecification.setUpdateBy(SecurityUtils.getUserId());
    return productSpecificationMapper.updateProductSpecification(productSpecification);
    }

    /**
    * 逻辑批量删除产品规格表
    *
    * @param productSpecificationDtos 需要删除的产品规格表主键
    * @return 结果
    */
    @Override
    public int logicDeleteProductSpecificationByProductSpecificationIds(List<ProductSpecificationDTO> productSpecificationDtos){
            List<Long> stringList = new ArrayList();
            for (ProductSpecificationDTO productSpecificationDTO : productSpecificationDtos) {
                stringList.add(productSpecificationDTO.getProductSpecificationId());
            }
    return productSpecificationMapper.logicDeleteProductSpecificationByProductSpecificationIds(stringList,productSpecificationDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
    * 物理删除产品规格表信息
    *
    * @param productSpecificationId 产品规格表主键
    * @return 结果
    */
    @Override
    public int deleteProductSpecificationByProductSpecificationId(Long productSpecificationId)
    {
    return productSpecificationMapper.deleteProductSpecificationByProductSpecificationId(productSpecificationId);
    }

     /**
     * 逻辑删除产品规格表信息
     *
     * @param  productSpecificationDTO 产品规格表
     * @return 结果
     */
     @Override
     public int logicDeleteProductSpecificationByProductSpecificationId(ProductSpecificationDTO productSpecificationDTO)
     {
     ProductSpecification productSpecification=new ProductSpecification();
     productSpecification.setProductSpecificationId(productSpecificationDTO.getProductSpecificationId());
     productSpecification.setUpdateTime(DateUtils.getNowDate());
     productSpecification.setUpdateBy(SecurityUtils.getUserId());
     return productSpecificationMapper.logicDeleteProductSpecificationByProductSpecificationId(productSpecification);
     }

     /**
     * 物理删除产品规格表信息
     *
     * @param  productSpecificationDTO 产品规格表
     * @return 结果
     */
     
     @Override
     public int deleteProductSpecificationByProductSpecificationId(ProductSpecificationDTO productSpecificationDTO)
     {
     ProductSpecification productSpecification=new ProductSpecification();
     BeanUtils.copyProperties(productSpecificationDTO,productSpecification);
     return productSpecificationMapper.deleteProductSpecificationByProductSpecificationId(productSpecification.getProductSpecificationId());
     }
     /**
     * 物理批量删除产品规格表
     *
     * @param productSpecificationDtos 需要删除的产品规格表主键
     * @return 结果
     */
     
     @Override
     public int deleteProductSpecificationByProductSpecificationIds(List<ProductSpecificationDTO> productSpecificationDtos){
     List<Long> stringList = new ArrayList();
     for (ProductSpecificationDTO productSpecificationDTO : productSpecificationDtos) {
     stringList.add(productSpecificationDTO.getProductSpecificationId());
     }
     return productSpecificationMapper.deleteProductSpecificationByProductSpecificationIds(stringList);
     }

    /**
    * 批量新增产品规格表信息
    *
    * @param productSpecificationDtos 产品规格表对象
    */
    
    @Override
    public int insertProductSpecifications(List<ProductSpecificationDTO> productSpecificationDtos){
      List<ProductSpecification> productSpecificationList = new ArrayList();

    for (ProductSpecificationDTO productSpecificationDTO : productSpecificationDtos) {
      ProductSpecification productSpecification =new ProductSpecification();
      BeanUtils.copyProperties(productSpecificationDTO,productSpecification);
       productSpecification.setCreateBy(SecurityUtils.getUserId());
       productSpecification.setCreateTime(DateUtils.getNowDate());
       productSpecification.setUpdateTime(DateUtils.getNowDate());
       productSpecification.setUpdateBy(SecurityUtils.getUserId());
       productSpecification.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      productSpecificationList.add(productSpecification);
    }
    return productSpecificationMapper.batchProductSpecification(productSpecificationList);
    }

    /**
    * 批量修改产品规格表信息
    *
    * @param productSpecificationDtos 产品规格表对象
    */
    
    @Override
    public int updateProductSpecifications(List<ProductSpecificationDTO> productSpecificationDtos){
     List<ProductSpecification> productSpecificationList = new ArrayList();

     for (ProductSpecificationDTO productSpecificationDTO : productSpecificationDtos) {
     ProductSpecification productSpecification =new ProductSpecification();
     BeanUtils.copyProperties(productSpecificationDTO,productSpecification);
        productSpecification.setCreateBy(SecurityUtils.getUserId());
        productSpecification.setCreateTime(DateUtils.getNowDate());
        productSpecification.setUpdateTime(DateUtils.getNowDate());
        productSpecification.setUpdateBy(SecurityUtils.getUserId());
     productSpecificationList.add(productSpecification);
     }
     return productSpecificationMapper.updateProductSpecifications(productSpecificationList);
    }
}

