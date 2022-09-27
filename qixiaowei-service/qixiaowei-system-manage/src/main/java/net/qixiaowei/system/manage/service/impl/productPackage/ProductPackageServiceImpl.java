package net.qixiaowei.system.manage.service.impl.productPackage;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.productPackage.ProductPackage;
import net.qixiaowei.system.manage.api.dto.productPackage.ProductPackageDTO;
import net.qixiaowei.system.manage.mapper.productPackage.ProductPackageMapper;
import net.qixiaowei.system.manage.service.productPackage.IProductPackageService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* ProductPackageService业务层处理
* @author TANGMICHI
* @since 2022-09-27
*/
@Service
public class ProductPackageServiceImpl implements IProductPackageService{
    @Autowired
    private ProductPackageMapper productPackageMapper;

    /**
    * 查询产品包
    *
    * @param productPackageId 产品包主键
    * @return 产品包
    */
    @Override
    public ProductPackageDTO selectProductPackageByProductPackageId(Long productPackageId)
    {
    return productPackageMapper.selectProductPackageByProductPackageId(productPackageId);
    }

    /**
    * 查询产品包列表
    *
    * @param productPackageDTO 产品包
    * @return 产品包
    */
    @Override
    public List<ProductPackageDTO> selectProductPackageList(ProductPackageDTO productPackageDTO)
    {
    ProductPackage productPackage=new ProductPackage();
    BeanUtils.copyProperties(productPackageDTO,productPackage);
    return productPackageMapper.selectProductPackageList(productPackage);
    }

    /**
    * 新增产品包
    *
    * @param productPackageDTO 产品包
    * @return 结果
    */
    @Transactional
    @Override
    public int insertProductPackage(ProductPackageDTO productPackageDTO){
    ProductPackage productPackage=new ProductPackage();
    BeanUtils.copyProperties(productPackageDTO,productPackage);
    productPackage.setCreateBy(SecurityUtils.getUserId());
    productPackage.setCreateTime(DateUtils.getNowDate());
    productPackage.setUpdateTime(DateUtils.getNowDate());
    productPackage.setUpdateBy(SecurityUtils.getUserId());
    productPackage.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    return productPackageMapper.insertProductPackage(productPackage);
    }

    /**
    * 修改产品包
    *
    * @param productPackageDTO 产品包
    * @return 结果
    */
    @Transactional
    @Override
    public int updateProductPackage(ProductPackageDTO productPackageDTO)
    {
    ProductPackage productPackage=new ProductPackage();
    BeanUtils.copyProperties(productPackageDTO,productPackage);
    productPackage.setUpdateTime(DateUtils.getNowDate());
    productPackage.setUpdateBy(SecurityUtils.getUserId());
    return productPackageMapper.updateProductPackage(productPackage);
    }

    /**
    * 逻辑批量删除产品包
    *
    * @param productPackageDtos 需要删除的产品包主键
    * @return 结果
    */

    @Transactional
    @Override
    public int logicDeleteProductPackageByProductPackageIds(List<ProductPackageDTO> productPackageDtos){
            List<Long> stringList = new ArrayList();
            for (ProductPackageDTO productPackageDTO : productPackageDtos) {
                stringList.add(productPackageDTO.getProductPackageId());
            }
    return productPackageMapper.logicDeleteProductPackageByProductPackageIds(stringList,productPackageDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
    * 物理删除产品包信息
    *
    * @param productPackageId 产品包主键
    * @return 结果
    */

    @Transactional
    @Override
    public int deleteProductPackageByProductPackageId(Long productPackageId)
    {
    return productPackageMapper.deleteProductPackageByProductPackageId(productPackageId);
    }

     /**
     * 逻辑删除产品包信息
     *
     * @param  productPackageDTO 产品包
     * @return 结果
     */
     @Transactional
     @Override
     public int logicDeleteProductPackageByProductPackageId(ProductPackageDTO productPackageDTO)
     {
     ProductPackage productPackage=new ProductPackage();
     BeanUtils.copyProperties(productPackageDTO,productPackage);
     return productPackageMapper.logicDeleteProductPackageByProductPackageId(productPackage,SecurityUtils.getUserId(),DateUtils.getNowDate());
     }

     /**
     * 物理删除产品包信息
     *
     * @param  productPackageDTO 产品包
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteProductPackageByProductPackageId(ProductPackageDTO productPackageDTO)
     {
     ProductPackage productPackage=new ProductPackage();
     BeanUtils.copyProperties(productPackageDTO,productPackage);
     return productPackageMapper.deleteProductPackageByProductPackageId(productPackage.getProductPackageId());
     }
     /**
     * 物理批量删除产品包
     *
     * @param productPackageDtos 需要删除的产品包主键
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteProductPackageByProductPackageIds(List<ProductPackageDTO> productPackageDtos){
     List<Long> stringList = new ArrayList();
     for (ProductPackageDTO productPackageDTO : productPackageDtos) {
     stringList.add(productPackageDTO.getProductPackageId());
     }
     return productPackageMapper.deleteProductPackageByProductPackageIds(stringList);
     }

    /**
    * 批量新增产品包信息
    *
    * @param productPackageDtos 产品包对象
    */
    @Transactional
    public int insertProductPackages(List<ProductPackageDTO> productPackageDtos){
      List<ProductPackage> productPackageList = new ArrayList();

    for (ProductPackageDTO productPackageDTO : productPackageDtos) {
      ProductPackage productPackage =new ProductPackage();
      BeanUtils.copyProperties(productPackageDTO,productPackage);
       productPackage.setCreateBy(SecurityUtils.getUserId());
       productPackage.setCreateTime(DateUtils.getNowDate());
       productPackage.setUpdateTime(DateUtils.getNowDate());
       productPackage.setUpdateBy(SecurityUtils.getUserId());
       productPackage.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      productPackageList.add(productPackage);
    }
    return productPackageMapper.batchProductPackage(productPackageList);
    }

    /**
    * 批量修改产品包信息
    *
    * @param productPackageDtos 产品包对象
    */
    @Transactional
    public int updateProductPackages(List<ProductPackageDTO> productPackageDtos){
     List<ProductPackage> productPackageList = new ArrayList();

     for (ProductPackageDTO productPackageDTO : productPackageDtos) {
     ProductPackage productPackage =new ProductPackage();
     BeanUtils.copyProperties(productPackageDTO,productPackage);
        productPackage.setCreateBy(SecurityUtils.getUserId());
        productPackage.setCreateTime(DateUtils.getNowDate());
        productPackage.setUpdateTime(DateUtils.getNowDate());
        productPackage.setUpdateBy(SecurityUtils.getUserId());
     productPackageList.add(productPackage);
     }
     return productPackageMapper.updateProductPackages(productPackageList);
    }
}

