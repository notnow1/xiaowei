package net.qixiaowei.operate.cloud.mapper.product;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.product.ProductSpecificationParam;
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationParamDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* ProductSpecificationParamMapper接口
* @author TANGMICHI
* @since 2022-10-08
*/
public interface ProductSpecificationParamMapper{
    /**
    * 查询产品规格参数表
    *
    * @param productSpecificationParamId 产品规格参数表主键
    * @return 产品规格参数表
    */
    ProductSpecificationParamDTO selectProductSpecificationParamByProductSpecificationParamId(@Param("productSpecificationParamId")Long productSpecificationParamId);

    /**
     * 根据产品id查询产品规格参数表
     *
     * @param productId 产品规格参数表
     * @return 产品规格参数表
     */
    List<ProductSpecificationParamDTO> selectProductId(@Param("productId")Long productId);
    /**
    * 查询产品规格参数表列表
    *
    * @param productSpecificationParam 产品规格参数表
    * @return 产品规格参数表集合
    */
    List<ProductSpecificationParamDTO> selectProductSpecificationParamList(@Param("productSpecificationParam")ProductSpecificationParam productSpecificationParam);

    /**
    * 新增产品规格参数表
    *
    * @param productSpecificationParam 产品规格参数表
    * @return 结果
    */
    int insertProductSpecificationParam(@Param("productSpecificationParam")ProductSpecificationParam productSpecificationParam);

    /**
    * 修改产品规格参数表
    *
    * @param productSpecificationParam 产品规格参数表
    * @return 结果
    */
    int updateProductSpecificationParam(@Param("productSpecificationParam")ProductSpecificationParam productSpecificationParam);

    /**
    * 批量修改产品规格参数表
    *
    * @param productSpecificationParamList 产品规格参数表
    * @return 结果
    */
    int updateProductSpecificationParams(@Param("productSpecificationParamList")List<ProductSpecificationParam> productSpecificationParamList);
    /**
    * 逻辑删除产品规格参数表
    *
    * @param productSpecificationParam
    * @return 结果
    */
    int logicDeleteProductSpecificationParamByProductSpecificationParamId(@Param("productSpecificationParam")ProductSpecificationParam productSpecificationParam,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
    * 逻辑批量删除产品规格参数表
    *
    * @param productSpecificationParamIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteProductSpecificationParamByProductSpecificationParamIds(@Param("productSpecificationParamIds")List<Long> productSpecificationParamIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除产品规格参数表
    *
    * @param productSpecificationParamId 产品规格参数表主键
    * @return 结果
    */
    int deleteProductSpecificationParamByProductSpecificationParamId(@Param("productSpecificationParamId")Long productSpecificationParamId);

    /**
    * 物理批量删除产品规格参数表
    *
    * @param productSpecificationParamIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteProductSpecificationParamByProductSpecificationParamIds(@Param("productSpecificationParamIds")List<Long> productSpecificationParamIds);

    /**
    * 批量新增产品规格参数表
    *
    * @param ProductSpecificationParams 产品规格参数表列表
    * @return 结果
    */
    int batchProductSpecificationParam(@Param("productSpecificationParams")List<ProductSpecificationParam> ProductSpecificationParams);
}
