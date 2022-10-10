package net.qixiaowei.operate.cloud.mapper.product;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.product.ProductFile;
import net.qixiaowei.operate.cloud.api.dto.product.ProductFileDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* ProductFileMapper接口
* @author TANGMICHI
* @since 2022-10-09
*/
public interface ProductFileMapper{
    /**
    * 查询产品文件表
    *
    * @param productFileId 产品文件表主键
    * @return 产品文件表
    */
    ProductFileDTO selectProductFileByProductFileId(@Param("productFileId")Long productFileId);

    /**
    * 查询产品文件表列表
    *
    * @param productFile 产品文件表
    * @return 产品文件表集合
    */
    List<ProductFileDTO> selectProductFileList(@Param("productFile")ProductFile productFile);

    /**
    * 新增产品文件表
    *
    * @param productFile 产品文件表
    * @return 结果
    */
    int insertProductFile(@Param("productFile")ProductFile productFile);

    /**
    * 修改产品文件表
    *
    * @param productFile 产品文件表
    * @return 结果
    */
    int updateProductFile(@Param("productFile")ProductFile productFile);

    /**
    * 批量修改产品文件表
    *
    * @param productFileList 产品文件表
    * @return 结果
    */
    int updateProductFiles(@Param("productFileList")List<ProductFile> productFileList);
    /**
    * 逻辑删除产品文件表
    *
    * @param productFile
    * @return 结果
    */
    int logicDeleteProductFileByProductFileId(@Param("productFile")ProductFile productFile);

    /**
    * 逻辑批量删除产品文件表
    *
    * @param productFileIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteProductFileByProductFileIds(@Param("productFileIds")List<Long> productFileIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除产品文件表
    *
    * @param productFileId 产品文件表主键
    * @return 结果
    */
    int deleteProductFileByProductFileId(@Param("productFileId")Long productFileId);

    /**
    * 物理批量删除产品文件表
    *
    * @param productFileIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteProductFileByProductFileIds(@Param("productFileIds")List<Long> productFileIds);

    /**
    * 批量新增产品文件表
    *
    * @param ProductFiles 产品文件表列表
    * @return 结果
    */
    int batchProductFile(@Param("productFiles")List<ProductFile> ProductFiles);
}
