package net.qixiaowei.operate.cloud.mapper.product;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.product.ProductSpecificationData;
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationDataDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* ProductSpecificationDataMapper接口
* @author TANGMICHI
* @since 2022-10-08
*/
public interface ProductSpecificationDataMapper{
    /**
    * 查询产品规格数据表
    *
    * @param productSpecificationDataId 产品规格数据表主键
    * @return 产品规格数据表
    */
    ProductSpecificationDataDTO selectProductSpecificationDataByProductSpecificationDataId(@Param("productSpecificationDataId")Long productSpecificationDataId);

    /**
    * 查询产品规格数据表列表
    *
    * @param productSpecificationData 产品规格数据表
    * @return 产品规格数据表集合
    */
    List<ProductSpecificationDataDTO> selectProductSpecificationDataList(@Param("productSpecificationData")ProductSpecificationData productSpecificationData);

    /**
    * 新增产品规格数据表
    *
    * @param productSpecificationData 产品规格数据表
    * @return 结果
    */
    int insertProductSpecificationData(@Param("productSpecificationData")ProductSpecificationData productSpecificationData);

    /**
    * 修改产品规格数据表
    *
    * @param productSpecificationData 产品规格数据表
    * @return 结果
    */
    int updateProductSpecificationData(@Param("productSpecificationData")ProductSpecificationData productSpecificationData);

    /**
    * 批量修改产品规格数据表
    *
    * @param productSpecificationDataList 产品规格数据表
    * @return 结果
    */
    int updateProductSpecificationDatas(@Param("productSpecificationDataList")List<ProductSpecificationData> productSpecificationDataList);
    /**
    * 逻辑删除产品规格数据表
    *
    * @param productSpecificationData
    * @return 结果
    */
    int logicDeleteProductSpecificationDataByProductSpecificationDataId(@Param("productSpecificationData")ProductSpecificationData productSpecificationData,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
    * 逻辑批量删除产品规格数据表
    *
    * @param productSpecificationDataIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteProductSpecificationDataByProductSpecificationDataIds(@Param("productSpecificationDataIds")List<Long> productSpecificationDataIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除产品规格数据表
    *
    * @param productSpecificationDataId 产品规格数据表主键
    * @return 结果
    */
    int deleteProductSpecificationDataByProductSpecificationDataId(@Param("productSpecificationDataId")Long productSpecificationDataId);

    /**
    * 物理批量删除产品规格数据表
    *
    * @param productSpecificationDataIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteProductSpecificationDataByProductSpecificationDataIds(@Param("productSpecificationDataIds")List<Long> productSpecificationDataIds);

    /**
    * 批量新增产品规格数据表
    *
    * @param ProductSpecificationDatas 产品规格数据表列表
    * @return 结果
    */
    int batchProductSpecificationData(@Param("productSpecificationDatas")List<ProductSpecificationData> ProductSpecificationDatas);
}
