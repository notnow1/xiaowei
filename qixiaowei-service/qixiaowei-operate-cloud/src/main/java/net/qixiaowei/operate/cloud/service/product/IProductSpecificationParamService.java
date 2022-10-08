package net.qixiaowei.operate.cloud.service.product;

import java.util.List;
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationParamDTO;


/**
* ProductSpecificationParamService接口
* @author TANGMICHI
* @since 2022-10-08
*/
public interface IProductSpecificationParamService{
    /**
    * 查询产品规格参数表
    *
    * @param productSpecificationParamId 产品规格参数表主键
    * @return 产品规格参数表
    */
    ProductSpecificationParamDTO selectProductSpecificationParamByProductSpecificationParamId(Long productSpecificationParamId);

    /**
    * 查询产品规格参数表列表
    *
    * @param productSpecificationParamDTO 产品规格参数表
    * @return 产品规格参数表集合
    */
    List<ProductSpecificationParamDTO> selectProductSpecificationParamList(ProductSpecificationParamDTO productSpecificationParamDTO);

    /**
    * 新增产品规格参数表
    *
    * @param productSpecificationParamDTO 产品规格参数表
    * @return 结果
    */
    int insertProductSpecificationParam(ProductSpecificationParamDTO productSpecificationParamDTO);

    /**
    * 修改产品规格参数表
    *
    * @param productSpecificationParamDTO 产品规格参数表
    * @return 结果
    */
    int updateProductSpecificationParam(ProductSpecificationParamDTO productSpecificationParamDTO);

    /**
    * 批量修改产品规格参数表
    *
    * @param productSpecificationParamDtos 产品规格参数表
    * @return 结果
    */
    int updateProductSpecificationParams(List<ProductSpecificationParamDTO> productSpecificationParamDtos);

    /**
    * 批量新增产品规格参数表
    *
    * @param productSpecificationParamDtos 产品规格参数表
    * @return 结果
    */
    int insertProductSpecificationParams(List<ProductSpecificationParamDTO> productSpecificationParamDtos);

    /**
    * 逻辑批量删除产品规格参数表
    *
    * @param ProductSpecificationParamDtos 需要删除的产品规格参数表集合
    * @return 结果
    */
    int logicDeleteProductSpecificationParamByProductSpecificationParamIds(List<ProductSpecificationParamDTO> ProductSpecificationParamDtos);

    /**
    * 逻辑删除产品规格参数表信息
    *
    * @param productSpecificationParamDTO
    * @return 结果
    */
    int logicDeleteProductSpecificationParamByProductSpecificationParamId(ProductSpecificationParamDTO productSpecificationParamDTO);
    /**
    * 逻辑批量删除产品规格参数表
    *
    * @param ProductSpecificationParamDtos 需要删除的产品规格参数表集合
    * @return 结果
    */
    int deleteProductSpecificationParamByProductSpecificationParamIds(List<ProductSpecificationParamDTO> ProductSpecificationParamDtos);

    /**
    * 逻辑删除产品规格参数表信息
    *
    * @param productSpecificationParamDTO
    * @return 结果
    */
    int deleteProductSpecificationParamByProductSpecificationParamId(ProductSpecificationParamDTO productSpecificationParamDTO);


    /**
    * 删除产品规格参数表信息
    *
    * @param productSpecificationParamId 产品规格参数表主键
    * @return 结果
    */
    int deleteProductSpecificationParamByProductSpecificationParamId(Long productSpecificationParamId);
}
