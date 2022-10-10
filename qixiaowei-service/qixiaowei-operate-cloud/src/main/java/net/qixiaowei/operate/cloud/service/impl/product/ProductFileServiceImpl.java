package net.qixiaowei.operate.cloud.service.impl.product;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.product.ProductFile;
import net.qixiaowei.operate.cloud.api.dto.product.ProductFileDTO;
import net.qixiaowei.operate.cloud.mapper.product.ProductFileMapper;
import net.qixiaowei.operate.cloud.service.product.IProductFileService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* ProductFileService业务层处理
* @author TANGMICHI
* @since 2022-10-09
*/
@Service
public class ProductFileServiceImpl implements IProductFileService{
    @Autowired
    private ProductFileMapper productFileMapper;

    /**
    * 查询产品文件表
    *
    * @param productFileId 产品文件表主键
    * @return 产品文件表
    */
    @Override
    public ProductFileDTO selectProductFileByProductFileId(Long productFileId)
    {
    return productFileMapper.selectProductFileByProductFileId(productFileId);
    }

    /**
    * 查询产品文件表列表
    *
    * @param productFileDTO 产品文件表
    * @return 产品文件表
    */
    @Override
    public List<ProductFileDTO> selectProductFileList(ProductFileDTO productFileDTO)
    {
    ProductFile productFile=new ProductFile();
    BeanUtils.copyProperties(productFileDTO,productFile);
    return productFileMapper.selectProductFileList(productFile);
    }

    /**
    * 新增产品文件表
    *
    * @param productFileDTO 产品文件表
    * @return 结果
    */
    @Override
    public int insertProductFile(ProductFileDTO productFileDTO){
    ProductFile productFile=new ProductFile();
    BeanUtils.copyProperties(productFileDTO,productFile);
    productFile.setCreateBy(SecurityUtils.getUserId());
    productFile.setCreateTime(DateUtils.getNowDate());
    productFile.setUpdateTime(DateUtils.getNowDate());
    productFile.setUpdateBy(SecurityUtils.getUserId());
    productFile.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    return productFileMapper.insertProductFile(productFile);
    }

    /**
    * 修改产品文件表
    *
    * @param productFileDTO 产品文件表
    * @return 结果
    */
    @Override
    public int updateProductFile(ProductFileDTO productFileDTO)
    {
    ProductFile productFile=new ProductFile();
    BeanUtils.copyProperties(productFileDTO,productFile);
    productFile.setUpdateTime(DateUtils.getNowDate());
    productFile.setUpdateBy(SecurityUtils.getUserId());
    return productFileMapper.updateProductFile(productFile);
    }

    /**
    * 逻辑批量删除产品文件表
    *
    * @param productFileDtos 需要删除的产品文件表主键
    * @return 结果
    */
    @Override
    public int logicDeleteProductFileByProductFileIds(List<ProductFileDTO> productFileDtos){
            List<Long> stringList = new ArrayList();
            for (ProductFileDTO productFileDTO : productFileDtos) {
                stringList.add(productFileDTO.getProductFileId());
            }
    return productFileMapper.logicDeleteProductFileByProductFileIds(stringList,productFileDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
    * 物理删除产品文件表信息
    *
    * @param productFileId 产品文件表主键
    * @return 结果
    */
    @Override
    public int deleteProductFileByProductFileId(Long productFileId)
    {
    return productFileMapper.deleteProductFileByProductFileId(productFileId);
    }

     /**
     * 逻辑删除产品文件表信息
     *
     * @param  productFileDTO 产品文件表
     * @return 结果
     */
     @Override
     public int logicDeleteProductFileByProductFileId(ProductFileDTO productFileDTO)
     {
     ProductFile productFile=new ProductFile();
     productFile.setProductFileId(productFileDTO.getProductFileId());
     productFile.setUpdateTime(DateUtils.getNowDate());
     productFile.setUpdateBy(SecurityUtils.getUserId());
     return productFileMapper.logicDeleteProductFileByProductFileId(productFile);
     }

     /**
     * 物理删除产品文件表信息
     *
     * @param  productFileDTO 产品文件表
     * @return 结果
     */
     
     @Override
     public int deleteProductFileByProductFileId(ProductFileDTO productFileDTO)
     {
     ProductFile productFile=new ProductFile();
     BeanUtils.copyProperties(productFileDTO,productFile);
     return productFileMapper.deleteProductFileByProductFileId(productFile.getProductFileId());
     }
     /**
     * 物理批量删除产品文件表
     *
     * @param productFileDtos 需要删除的产品文件表主键
     * @return 结果
     */
     
     @Override
     public int deleteProductFileByProductFileIds(List<ProductFileDTO> productFileDtos){
     List<Long> stringList = new ArrayList();
     for (ProductFileDTO productFileDTO : productFileDtos) {
     stringList.add(productFileDTO.getProductFileId());
     }
     return productFileMapper.deleteProductFileByProductFileIds(stringList);
     }

    /**
    * 批量新增产品文件表信息
    *
    * @param productFileDtos 产品文件表对象
    */
    
    public int insertProductFiles(List<ProductFileDTO> productFileDtos){
      List<ProductFile> productFileList = new ArrayList();

    for (ProductFileDTO productFileDTO : productFileDtos) {
      ProductFile productFile =new ProductFile();
      BeanUtils.copyProperties(productFileDTO,productFile);
       productFile.setCreateBy(SecurityUtils.getUserId());
       productFile.setCreateTime(DateUtils.getNowDate());
       productFile.setUpdateTime(DateUtils.getNowDate());
       productFile.setUpdateBy(SecurityUtils.getUserId());
       productFile.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      productFileList.add(productFile);
    }
    return productFileMapper.batchProductFile(productFileList);
    }

    /**
    * 批量修改产品文件表信息
    *
    * @param productFileDtos 产品文件表对象
    */
    
    public int updateProductFiles(List<ProductFileDTO> productFileDtos){
     List<ProductFile> productFileList = new ArrayList();

     for (ProductFileDTO productFileDTO : productFileDtos) {
     ProductFile productFile =new ProductFile();
     BeanUtils.copyProperties(productFileDTO,productFile);
        productFile.setCreateBy(SecurityUtils.getUserId());
        productFile.setCreateTime(DateUtils.getNowDate());
        productFile.setUpdateTime(DateUtils.getNowDate());
        productFile.setUpdateBy(SecurityUtils.getUserId());
     productFileList.add(productFile);
     }
     return productFileMapper.updateProductFiles(productFileList);
    }
}

