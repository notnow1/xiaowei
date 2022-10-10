package net.qixiaowei.operate.cloud.service.product;

import java.util.List;
import net.qixiaowei.operate.cloud.api.dto.product.ProductFileDTO;


/**
* ProductFileService接口
* @author TANGMICHI
* @since 2022-10-09
*/
public interface IProductFileService{
    /**
    * 查询产品文件表
    *
    * @param productFileId 产品文件表主键
    * @return 产品文件表
    */
    ProductFileDTO selectProductFileByProductFileId(Long productFileId);

    /**
    * 查询产品文件表列表
    *
    * @param productFileDTO 产品文件表
    * @return 产品文件表集合
    */
    List<ProductFileDTO> selectProductFileList(ProductFileDTO productFileDTO);

    /**
    * 新增产品文件表
    *
    * @param productFileDTO 产品文件表
    * @return 结果
    */
    int insertProductFile(ProductFileDTO productFileDTO);

    /**
    * 修改产品文件表
    *
    * @param productFileDTO 产品文件表
    * @return 结果
    */
    int updateProductFile(ProductFileDTO productFileDTO);

    /**
    * 批量修改产品文件表
    *
    * @param productFileDtos 产品文件表
    * @return 结果
    */
    int updateProductFiles(List<ProductFileDTO> productFileDtos);

    /**
    * 批量新增产品文件表
    *
    * @param productFileDtos 产品文件表
    * @return 结果
    */
    int insertProductFiles(List<ProductFileDTO> productFileDtos);

    /**
    * 逻辑批量删除产品文件表
    *
    * @param ProductFileDtos 需要删除的产品文件表集合
    * @return 结果
    */
    int logicDeleteProductFileByProductFileIds(List<ProductFileDTO> ProductFileDtos);

    /**
    * 逻辑删除产品文件表信息
    *
    * @param productFileDTO
    * @return 结果
    */
    int logicDeleteProductFileByProductFileId(ProductFileDTO productFileDTO);
    /**
    * 逻辑批量删除产品文件表
    *
    * @param ProductFileDtos 需要删除的产品文件表集合
    * @return 结果
    */
    int deleteProductFileByProductFileIds(List<ProductFileDTO> ProductFileDtos);

    /**
    * 逻辑删除产品文件表信息
    *
    * @param productFileDTO
    * @return 结果
    */
    int deleteProductFileByProductFileId(ProductFileDTO productFileDTO);


    /**
    * 删除产品文件表信息
    *
    * @param productFileId 产品文件表主键
    * @return 结果
    */
    int deleteProductFileByProductFileId(Long productFileId);
}
