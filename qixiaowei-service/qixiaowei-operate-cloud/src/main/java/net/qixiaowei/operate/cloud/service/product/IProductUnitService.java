package net.qixiaowei.operate.cloud.service.product;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.product.ProductUnitDTO;


/**
* ProductUnitService接口
* @author TANGMICHI
* @since 2022-10-08
*/
public interface IProductUnitService{
    /**
    * 查询产品单位表
    *
    * @param productUnitId 产品单位表主键
    * @return 产品单位表
    */
    ProductUnitDTO selectProductUnitByProductUnitId(Long productUnitId);

    /**
    * 查询产品单位表列表
    *
    * @param productUnitDTO 产品单位表
    * @return 产品单位表集合
    */
    List<ProductUnitDTO> selectProductUnitList(ProductUnitDTO productUnitDTO);
    /**
     * 处理返回
     *
     * @param result 返回集合
     * @return 返回集合
     */
    void handleResult(List<ProductUnitDTO> result);
    /**
    * 新增产品单位表
    *
    * @param productUnitDTO 产品单位表
    * @return 结果
    */
    ProductUnitDTO insertProductUnit(ProductUnitDTO productUnitDTO);

    /**
    * 修改产品单位表
    *
    * @param productUnitDTO 产品单位表
    * @return 结果
    */
    int updateProductUnit(ProductUnitDTO productUnitDTO);

    /**
    * 批量修改产品单位表
    *
    * @param productUnitDtos 产品单位表
    * @return 结果
    */
    int updateProductUnits(List<ProductUnitDTO> productUnitDtos);

    /**
    * 批量新增产品单位表
    *
    * @param productUnitDtos 产品单位表
    * @return 结果
    */
    int insertProductUnits(List<ProductUnitDTO> productUnitDtos);

    /**
    * 逻辑批量删除产品单位表
    *
    * @param productUnitIds 需要删除的产品单位表集合
    * @return 结果
    */
    int logicDeleteProductUnitByProductUnitIds(List<Long>  productUnitIds);

    /**
    * 逻辑删除产品单位表信息
    *
    * @param productUnitDTO
    * @return 结果
    */
    int logicDeleteProductUnitByProductUnitId(ProductUnitDTO productUnitDTO);
    /**
    * 逻辑批量删除产品单位表
    *
    * @param ProductUnitDtos 需要删除的产品单位表集合
    * @return 结果
    */
    int deleteProductUnitByProductUnitIds(List<ProductUnitDTO> ProductUnitDtos);

    /**
    * 逻辑删除产品单位表信息
    *
    * @param productUnitDTO
    * @return 结果
    */
    int deleteProductUnitByProductUnitId(ProductUnitDTO productUnitDTO);


    /**
    * 删除产品单位表信息
    *
    * @param productUnitId 产品单位表主键
    * @return 结果
    */
    int deleteProductUnitByProductUnitId(Long productUnitId);
}
