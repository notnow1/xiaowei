package net.qixiaowei.operate.cloud.service.impl.product;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.product.ProductUnit;
import net.qixiaowei.operate.cloud.api.dto.product.ProductUnitDTO;
import net.qixiaowei.operate.cloud.mapper.product.ProductUnitMapper;
import net.qixiaowei.operate.cloud.service.product.IProductUnitService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* ProductUnitService业务层处理
* @author TANGMICHI
* @since 2022-10-08
*/
@Service
public class ProductUnitServiceImpl implements IProductUnitService{
    @Autowired
    private ProductUnitMapper productUnitMapper;

    /**
    * 查询产品单位表
    *
    * @param productUnitId 产品单位表主键
    * @return 产品单位表
    */
    @Override
    public ProductUnitDTO selectProductUnitByProductUnitId(Long productUnitId)
    {
    return productUnitMapper.selectProductUnitByProductUnitId(productUnitId);
    }

    /**
    * 查询产品单位表列表
    *
    * @param productUnitDTO 产品单位表
    * @return 产品单位表
    */
    @Override
    public List<ProductUnitDTO> selectProductUnitList(ProductUnitDTO productUnitDTO)
    {
    ProductUnit productUnit=new ProductUnit();
    BeanUtils.copyProperties(productUnitDTO,productUnit);
    return productUnitMapper.selectProductUnitList(productUnit);
    }

    /**
    * 新增产品单位表
    *
    * @param productUnitDTO 产品单位表
    * @return 结果
    */
    @Transactional
    @Override
    public int insertProductUnit(ProductUnitDTO productUnitDTO){
    ProductUnit productUnit=new ProductUnit();
    BeanUtils.copyProperties(productUnitDTO,productUnit);
    productUnit.setCreateBy(SecurityUtils.getUserId());
    productUnit.setCreateTime(DateUtils.getNowDate());
    productUnit.setUpdateTime(DateUtils.getNowDate());
    productUnit.setUpdateBy(SecurityUtils.getUserId());
    productUnit.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    return productUnitMapper.insertProductUnit(productUnit);
    }

    /**
    * 修改产品单位表
    *
    * @param productUnitDTO 产品单位表
    * @return 结果
    */
    @Transactional
    @Override
    public int updateProductUnit(ProductUnitDTO productUnitDTO)
    {
    ProductUnit productUnit=new ProductUnit();
    BeanUtils.copyProperties(productUnitDTO,productUnit);
    productUnit.setUpdateTime(DateUtils.getNowDate());
    productUnit.setUpdateBy(SecurityUtils.getUserId());
    return productUnitMapper.updateProductUnit(productUnit);
    }

    /**
    * 逻辑批量删除产品单位表
    *
    * @param productUnitDtos 需要删除的产品单位表主键
    * @return 结果
    */

    @Transactional
    @Override
    public int logicDeleteProductUnitByProductUnitIds(List<ProductUnitDTO> productUnitDtos){
            List<Long> stringList = new ArrayList();
            for (ProductUnitDTO productUnitDTO : productUnitDtos) {
                stringList.add(productUnitDTO.getProductUnitId());
            }
    return productUnitMapper.logicDeleteProductUnitByProductUnitIds(stringList,productUnitDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
    * 物理删除产品单位表信息
    *
    * @param productUnitId 产品单位表主键
    * @return 结果
    */

    @Transactional
    @Override
    public int deleteProductUnitByProductUnitId(Long productUnitId)
    {
    return productUnitMapper.deleteProductUnitByProductUnitId(productUnitId);
    }

     /**
     * 逻辑删除产品单位表信息
     *
     * @param  productUnitDTO 产品单位表
     * @return 结果
     */
     @Transactional
     @Override
     public int logicDeleteProductUnitByProductUnitId(ProductUnitDTO productUnitDTO)
     {
     ProductUnit productUnit=new ProductUnit();
     BeanUtils.copyProperties(productUnitDTO,productUnit);
     return productUnitMapper.logicDeleteProductUnitByProductUnitId(productUnit,SecurityUtils.getUserId(),DateUtils.getNowDate());
     }

     /**
     * 物理删除产品单位表信息
     *
     * @param  productUnitDTO 产品单位表
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteProductUnitByProductUnitId(ProductUnitDTO productUnitDTO)
     {
     ProductUnit productUnit=new ProductUnit();
     BeanUtils.copyProperties(productUnitDTO,productUnit);
     return productUnitMapper.deleteProductUnitByProductUnitId(productUnit.getProductUnitId());
     }
     /**
     * 物理批量删除产品单位表
     *
     * @param productUnitDtos 需要删除的产品单位表主键
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteProductUnitByProductUnitIds(List<ProductUnitDTO> productUnitDtos){
     List<Long> stringList = new ArrayList();
     for (ProductUnitDTO productUnitDTO : productUnitDtos) {
     stringList.add(productUnitDTO.getProductUnitId());
     }
     return productUnitMapper.deleteProductUnitByProductUnitIds(stringList);
     }

    /**
    * 批量新增产品单位表信息
    *
    * @param productUnitDtos 产品单位表对象
    */
    @Transactional
    public int insertProductUnits(List<ProductUnitDTO> productUnitDtos){
      List<ProductUnit> productUnitList = new ArrayList();

    for (ProductUnitDTO productUnitDTO : productUnitDtos) {
      ProductUnit productUnit =new ProductUnit();
      BeanUtils.copyProperties(productUnitDTO,productUnit);
       productUnit.setCreateBy(SecurityUtils.getUserId());
       productUnit.setCreateTime(DateUtils.getNowDate());
       productUnit.setUpdateTime(DateUtils.getNowDate());
       productUnit.setUpdateBy(SecurityUtils.getUserId());
       productUnit.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      productUnitList.add(productUnit);
    }
    return productUnitMapper.batchProductUnit(productUnitList);
    }

    /**
    * 批量修改产品单位表信息
    *
    * @param productUnitDtos 产品单位表对象
    */
    @Transactional
    public int updateProductUnits(List<ProductUnitDTO> productUnitDtos){
     List<ProductUnit> productUnitList = new ArrayList();

     for (ProductUnitDTO productUnitDTO : productUnitDtos) {
     ProductUnit productUnit =new ProductUnit();
     BeanUtils.copyProperties(productUnitDTO,productUnit);
        productUnit.setCreateBy(SecurityUtils.getUserId());
        productUnit.setCreateTime(DateUtils.getNowDate());
        productUnit.setUpdateTime(DateUtils.getNowDate());
        productUnit.setUpdateBy(SecurityUtils.getUserId());
     productUnitList.add(productUnit);
     }
     return productUnitMapper.updateProductUnits(productUnitList);
    }
}

