package net.qixiaowei.operate.cloud.mapper.product;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.product.ProductSpecification;
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* ProductSpecificationMapper接口
* @author TANGMICHI
* @since 2022-10-09
*/
public interface ProductSpecificationMapper{
    /**
    * 查询产品规格表
    *
    * @param productSpecificationId 产品规格表主键
    * @return 产品规格表
    */
    ProductSpecificationDTO selectProductSpecificationByProductSpecificationId(@Param("productSpecificationId")Long productSpecificationId);

    /**
     * 根据产品id查询产品规格表
     *
     * @param productId 产品规格表
     * @return 产品规格表
     */
    List<ProductSpecificationDTO> selectProductId(@Param("productId")Long productId);

    /**
    * 查询产品规格表列表
    *
    * @param productSpecification 产品规格表
    * @return 产品规格表集合
    */
    List<ProductSpecificationDTO> selectProductSpecificationList(@Param("productSpecification")ProductSpecification productSpecification);

    /**
    * 新增产品规格表
    *
    * @param productSpecification 产品规格表
    * @return 结果
    */
    int insertProductSpecification(@Param("productSpecification")ProductSpecification productSpecification);

    /**
    * 修改产品规格表
    *
    * @param productSpecification 产品规格表
    * @return 结果
    */
    int updateProductSpecification(@Param("productSpecification")ProductSpecification productSpecification);

    /**
    * 批量修改产品规格表
    *
    * @param productSpecificationList 产品规格表
    * @return 结果
    */
    int updateProductSpecifications(@Param("productSpecificationList")List<ProductSpecification> productSpecificationList);
    /**
    * 逻辑删除产品规格表
    *
    * @param productSpecification
    * @return 结果
    */
    int logicDeleteProductSpecificationByProductSpecificationId(@Param("productSpecification")ProductSpecification productSpecification);

    /**
    * 逻辑批量删除产品规格表
    *
    * @param productSpecificationIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteProductSpecificationByProductSpecificationIds(@Param("productSpecificationIds")List<Long> productSpecificationIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
     * 根据产品id删除逻辑批量删除产品规格表
     *
     * @param productIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteProductSpecificationByProductIds(@Param("productIds")List<Long> productIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
    * 物理删除产品规格表
    *
    * @param productSpecificationId 产品规格表主键
    * @return 结果
    */
    int deleteProductSpecificationByProductSpecificationId(@Param("productSpecificationId")Long productSpecificationId);

    /**
    * 物理批量删除产品规格表
    *
    * @param productSpecificationIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteProductSpecificationByProductSpecificationIds(@Param("productSpecificationIds")List<Long> productSpecificationIds);

    /**
    * 批量新增产品规格表
    *
    * @param ProductSpecifications 产品规格表列表
    * @return 结果
    */
    int batchProductSpecification(@Param("productSpecifications")List<ProductSpecification> ProductSpecifications);
}
