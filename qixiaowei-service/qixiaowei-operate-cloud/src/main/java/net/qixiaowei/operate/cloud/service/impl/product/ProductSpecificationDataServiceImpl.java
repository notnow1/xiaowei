package net.qixiaowei.operate.cloud.service.impl.product;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.product.ProductSpecificationData;
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationDataDTO;
import net.qixiaowei.operate.cloud.mapper.product.ProductSpecificationDataMapper;
import net.qixiaowei.operate.cloud.service.product.IProductSpecificationDataService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* ProductSpecificationDataService业务层处理
* @author TANGMICHI
* @since 2022-10-09
*/
@Service
public class ProductSpecificationDataServiceImpl implements IProductSpecificationDataService{
    @Autowired
    private ProductSpecificationDataMapper productSpecificationDataMapper;

    /**
    * 查询产品规格数据表
    *
    * @param productSpecificationDataId 产品规格数据表主键
    * @return 产品规格数据表
    */
    @Override
    public ProductSpecificationDataDTO selectProductSpecificationDataByProductSpecificationDataId(Long productSpecificationDataId)
    {
    return productSpecificationDataMapper.selectProductSpecificationDataByProductSpecificationDataId(productSpecificationDataId);
    }

    /**
    * 查询产品规格数据表列表
    *
    * @param productSpecificationDataDTO 产品规格数据表
    * @return 产品规格数据表
    */
    @Override
    public List<ProductSpecificationDataDTO> selectProductSpecificationDataList(ProductSpecificationDataDTO productSpecificationDataDTO)
    {
    ProductSpecificationData productSpecificationData=new ProductSpecificationData();
    BeanUtils.copyProperties(productSpecificationDataDTO,productSpecificationData);
    return productSpecificationDataMapper.selectProductSpecificationDataList(productSpecificationData);
    }

    /**
    * 新增产品规格数据表
    *
    * @param productSpecificationDataDTO 产品规格数据表
    * @return 结果
    */
    @Override
    public int insertProductSpecificationData(ProductSpecificationDataDTO productSpecificationDataDTO){
    ProductSpecificationData productSpecificationData=new ProductSpecificationData();
    BeanUtils.copyProperties(productSpecificationDataDTO,productSpecificationData);
    productSpecificationData.setCreateBy(SecurityUtils.getUserId());
    productSpecificationData.setCreateTime(DateUtils.getNowDate());
    productSpecificationData.setUpdateTime(DateUtils.getNowDate());
    productSpecificationData.setUpdateBy(SecurityUtils.getUserId());
    productSpecificationData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    return productSpecificationDataMapper.insertProductSpecificationData(productSpecificationData);
    }

    /**
    * 修改产品规格数据表
    *
    * @param productSpecificationDataDTO 产品规格数据表
    * @return 结果
    */
    @Override
    public int updateProductSpecificationData(ProductSpecificationDataDTO productSpecificationDataDTO)
    {
    ProductSpecificationData productSpecificationData=new ProductSpecificationData();
    BeanUtils.copyProperties(productSpecificationDataDTO,productSpecificationData);
    productSpecificationData.setUpdateTime(DateUtils.getNowDate());
    productSpecificationData.setUpdateBy(SecurityUtils.getUserId());
    return productSpecificationDataMapper.updateProductSpecificationData(productSpecificationData);
    }

    /**
    * 逻辑批量删除产品规格数据表
    *
    * @param productSpecificationDataDtos 需要删除的产品规格数据表主键
    * @return 结果
    */
    @Override
    public int logicDeleteProductSpecificationDataByProductSpecificationDataIds(List<ProductSpecificationDataDTO> productSpecificationDataDtos){
            List<Long> stringList = new ArrayList();
            for (ProductSpecificationDataDTO productSpecificationDataDTO : productSpecificationDataDtos) {
                stringList.add(productSpecificationDataDTO.getProductSpecificationDataId());
            }
    return productSpecificationDataMapper.logicDeleteProductSpecificationDataByProductSpecificationDataIds(stringList,productSpecificationDataDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
    * 物理删除产品规格数据表信息
    *
    * @param productSpecificationDataId 产品规格数据表主键
    * @return 结果
    */
    @Override
    public int deleteProductSpecificationDataByProductSpecificationDataId(Long productSpecificationDataId)
    {
    return productSpecificationDataMapper.deleteProductSpecificationDataByProductSpecificationDataId(productSpecificationDataId);
    }

     /**
     * 逻辑删除产品规格数据表信息
     *
     * @param  productSpecificationDataDTO 产品规格数据表
     * @return 结果
     */
     @Override
     public int logicDeleteProductSpecificationDataByProductSpecificationDataId(ProductSpecificationDataDTO productSpecificationDataDTO)
     {
     ProductSpecificationData productSpecificationData=new ProductSpecificationData();
     productSpecificationData.setProductSpecificationDataId(productSpecificationDataDTO.getProductSpecificationDataId());
     productSpecificationData.setUpdateTime(DateUtils.getNowDate());
     productSpecificationData.setUpdateBy(SecurityUtils.getUserId());
     return productSpecificationDataMapper.logicDeleteProductSpecificationDataByProductSpecificationDataId(productSpecificationData);
     }

     /**
     * 物理删除产品规格数据表信息
     *
     * @param  productSpecificationDataDTO 产品规格数据表
     * @return 结果
     */
     
     @Override
     public int deleteProductSpecificationDataByProductSpecificationDataId(ProductSpecificationDataDTO productSpecificationDataDTO)
     {
     ProductSpecificationData productSpecificationData=new ProductSpecificationData();
     BeanUtils.copyProperties(productSpecificationDataDTO,productSpecificationData);
     return productSpecificationDataMapper.deleteProductSpecificationDataByProductSpecificationDataId(productSpecificationData.getProductSpecificationDataId());
     }
     /**
     * 物理批量删除产品规格数据表
     *
     * @param productSpecificationDataDtos 需要删除的产品规格数据表主键
     * @return 结果
     */
     
     @Override
     public int deleteProductSpecificationDataByProductSpecificationDataIds(List<ProductSpecificationDataDTO> productSpecificationDataDtos){
     List<Long> stringList = new ArrayList();
     for (ProductSpecificationDataDTO productSpecificationDataDTO : productSpecificationDataDtos) {
     stringList.add(productSpecificationDataDTO.getProductSpecificationDataId());
     }
     return productSpecificationDataMapper.deleteProductSpecificationDataByProductSpecificationDataIds(stringList);
     }

    /**
    * 批量新增产品规格数据表信息
    *
    * @param productSpecificationDataDtos 产品规格数据表对象
    */
    
    public int insertProductSpecificationDatas(List<ProductSpecificationDataDTO> productSpecificationDataDtos){
      List<ProductSpecificationData> productSpecificationDataList = new ArrayList();

    for (ProductSpecificationDataDTO productSpecificationDataDTO : productSpecificationDataDtos) {
      ProductSpecificationData productSpecificationData =new ProductSpecificationData();
      BeanUtils.copyProperties(productSpecificationDataDTO,productSpecificationData);
       productSpecificationData.setCreateBy(SecurityUtils.getUserId());
       productSpecificationData.setCreateTime(DateUtils.getNowDate());
       productSpecificationData.setUpdateTime(DateUtils.getNowDate());
       productSpecificationData.setUpdateBy(SecurityUtils.getUserId());
       productSpecificationData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      productSpecificationDataList.add(productSpecificationData);
    }
    return productSpecificationDataMapper.batchProductSpecificationData(productSpecificationDataList);
    }

    /**
    * 批量修改产品规格数据表信息
    *
    * @param productSpecificationDataDtos 产品规格数据表对象
    */
    
    public int updateProductSpecificationDatas(List<ProductSpecificationDataDTO> productSpecificationDataDtos){
     List<ProductSpecificationData> productSpecificationDataList = new ArrayList();

     for (ProductSpecificationDataDTO productSpecificationDataDTO : productSpecificationDataDtos) {
     ProductSpecificationData productSpecificationData =new ProductSpecificationData();
     BeanUtils.copyProperties(productSpecificationDataDTO,productSpecificationData);
        productSpecificationData.setCreateBy(SecurityUtils.getUserId());
        productSpecificationData.setCreateTime(DateUtils.getNowDate());
        productSpecificationData.setUpdateTime(DateUtils.getNowDate());
        productSpecificationData.setUpdateBy(SecurityUtils.getUserId());
     productSpecificationDataList.add(productSpecificationData);
     }
     return productSpecificationDataMapper.updateProductSpecificationDatas(productSpecificationDataList);
    }
}

