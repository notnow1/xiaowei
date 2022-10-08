package net.qixiaowei.operate.cloud.service.product;

import java.util.List;
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationDataDTO;


/**
* ProductSpecificationDataService接口
* @author TANGMICHI
* @since 2022-10-08
*/
public interface IProductSpecificationDataService{
    /**
    * 查询产品规格数据表
    *
    * @param productSpecificationDataId 产品规格数据表主键
    * @return 产品规格数据表
    */
    ProductSpecificationDataDTO selectProductSpecificationDataByProductSpecificationDataId(Long productSpecificationDataId);

    /**
    * 查询产品规格数据表列表
    *
    * @param productSpecificationDataDTO 产品规格数据表
    * @return 产品规格数据表集合
    */
    List<ProductSpecificationDataDTO> selectProductSpecificationDataList(ProductSpecificationDataDTO productSpecificationDataDTO);

    /**
    * 新增产品规格数据表
    *
    * @param productSpecificationDataDTO 产品规格数据表
    * @return 结果
    */
    int insertProductSpecificationData(ProductSpecificationDataDTO productSpecificationDataDTO);

    /**
    * 修改产品规格数据表
    *
    * @param productSpecificationDataDTO 产品规格数据表
    * @return 结果
    */
    int updateProductSpecificationData(ProductSpecificationDataDTO productSpecificationDataDTO);

    /**
    * 批量修改产品规格数据表
    *
    * @param productSpecificationDataDtos 产品规格数据表
    * @return 结果
    */
    int updateProductSpecificationDatas(List<ProductSpecificationDataDTO> productSpecificationDataDtos);

    /**
    * 批量新增产品规格数据表
    *
    * @param productSpecificationDataDtos 产品规格数据表
    * @return 结果
    */
    int insertProductSpecificationDatas(List<ProductSpecificationDataDTO> productSpecificationDataDtos);

    /**
    * 逻辑批量删除产品规格数据表
    *
    * @param ProductSpecificationDataDtos 需要删除的产品规格数据表集合
    * @return 结果
    */
    int logicDeleteProductSpecificationDataByProductSpecificationDataIds(List<ProductSpecificationDataDTO> ProductSpecificationDataDtos);

    /**
    * 逻辑删除产品规格数据表信息
    *
    * @param productSpecificationDataDTO
    * @return 结果
    */
    int logicDeleteProductSpecificationDataByProductSpecificationDataId(ProductSpecificationDataDTO productSpecificationDataDTO);
    /**
    * 逻辑批量删除产品规格数据表
    *
    * @param ProductSpecificationDataDtos 需要删除的产品规格数据表集合
    * @return 结果
    */
    int deleteProductSpecificationDataByProductSpecificationDataIds(List<ProductSpecificationDataDTO> ProductSpecificationDataDtos);

    /**
    * 逻辑删除产品规格数据表信息
    *
    * @param productSpecificationDataDTO
    * @return 结果
    */
    int deleteProductSpecificationDataByProductSpecificationDataId(ProductSpecificationDataDTO productSpecificationDataDTO);


    /**
    * 删除产品规格数据表信息
    *
    * @param productSpecificationDataId 产品规格数据表主键
    * @return 结果
    */
    int deleteProductSpecificationDataByProductSpecificationDataId(Long productSpecificationDataId);
}
