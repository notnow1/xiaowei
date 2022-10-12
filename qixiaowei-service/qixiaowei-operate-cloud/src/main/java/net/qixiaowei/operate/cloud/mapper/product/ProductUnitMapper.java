package net.qixiaowei.operate.cloud.mapper.product;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.product.ProductUnit;
import net.qixiaowei.operate.cloud.api.dto.product.ProductUnitDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* ProductUnitMapper接口
* @author TANGMICHI
* @since 2022-10-08
*/
public interface ProductUnitMapper{
    /**
    * 查询产品单位表
    *
    * @param productUnitId 产品单位表主键
    * @return 产品单位表
    */
    ProductUnitDTO selectProductUnitByProductUnitId(@Param("productUnitId")Long productUnitId);

    /**
     * 批量查询产品单位表
     *
     * @param productUnitIds 产品单位表主键
     * @return 产品单位表
     */
    List<ProductUnitDTO> selectProductUnitByProductUnitIds(@Param("productUnitIds")List<Long> productUnitIds);

    /**
     * 根据code查询产品单位表
     *
     * @param productUnitCode 产品单位表主键
     * @return 产品单位表
     */
    ProductUnitDTO selectProductUnitByProductUnitCode(@Param("productUnitCode")String productUnitCode);
    /**
    * 查询产品单位表列表
    *
    * @param productUnit 产品单位表
    * @return 产品单位表集合
    */
    List<ProductUnitDTO> selectProductUnitList(@Param("productUnit")ProductUnit productUnit);

    /**
    * 新增产品单位表
    *
    * @param productUnit 产品单位表
    * @return 结果
    */
    int insertProductUnit(@Param("productUnit")ProductUnit productUnit);

    /**
    * 修改产品单位表
    *
    * @param productUnit 产品单位表
    * @return 结果
    */
    int updateProductUnit(@Param("productUnit")ProductUnit productUnit);

    /**
    * 批量修改产品单位表
    *
    * @param productUnitList 产品单位表
    * @return 结果
    */
    int updateProductUnits(@Param("productUnitList")List<ProductUnit> productUnitList);
    /**
    * 逻辑删除产品单位表
    *
    * @param productUnit
    * @return 结果
    */
    int logicDeleteProductUnitByProductUnitId(@Param("productUnit")ProductUnit productUnit,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
    * 逻辑批量删除产品单位表
    *
    * @param productUnitIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteProductUnitByProductUnitIds(@Param("productUnitIds")List<Long> productUnitIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除产品单位表
    *
    * @param productUnitId 产品单位表主键
    * @return 结果
    */
    int deleteProductUnitByProductUnitId(@Param("productUnitId")Long productUnitId);

    /**
    * 物理批量删除产品单位表
    *
    * @param productUnitIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteProductUnitByProductUnitIds(@Param("productUnitIds")List<Long> productUnitIds);

    /**
    * 批量新增产品单位表
    *
    * @param ProductUnits 产品单位表列表
    * @return 结果
    */
    int batchProductUnit(@Param("productUnits")List<ProductUnit> ProductUnits);
}
