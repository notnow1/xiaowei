package net.qixiaowei.operate.cloud.service.product;

import java.util.List;
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationDTO;


/**
* ProductSpecificationService接口
* @author TANGMICHI
* @since 2022-10-09
*/
public interface IProductSpecificationService{
    /**
    * 查询产品规格表
    *
    * @param productSpecificationId 产品规格表主键
    * @return 产品规格表
    */
    ProductSpecificationDTO selectProductSpecificationByProductSpecificationId(Long productSpecificationId);

    /**
    * 查询产品规格表列表
    *
    * @param productSpecificationDTO 产品规格表
    * @return 产品规格表集合
    */
    List<ProductSpecificationDTO> selectProductSpecificationList(ProductSpecificationDTO productSpecificationDTO);

    /**
    * 新增产品规格表
    *
    * @param productSpecificationDTO 产品规格表
    * @return 结果
    */
    int insertProductSpecification(ProductSpecificationDTO productSpecificationDTO);

    /**
    * 修改产品规格表
    *
    * @param productSpecificationDTO 产品规格表
    * @return 结果
    */
    int updateProductSpecification(ProductSpecificationDTO productSpecificationDTO);

    /**
    * 批量修改产品规格表
    *
    * @param productSpecificationDtos 产品规格表
    * @return 结果
    */
    int updateProductSpecifications(List<ProductSpecificationDTO> productSpecificationDtos);

    /**
    * 批量新增产品规格表
    *
    * @param productSpecificationDtos 产品规格表
    * @return 结果
    */
    int insertProductSpecifications(List<ProductSpecificationDTO> productSpecificationDtos);

    /**
    * 逻辑批量删除产品规格表
    *
    * @param ProductSpecificationDtos 需要删除的产品规格表集合
    * @return 结果
    */
    int logicDeleteProductSpecificationByProductSpecificationIds(List<ProductSpecificationDTO> ProductSpecificationDtos);

    /**
    * 逻辑删除产品规格表信息
    *
    * @param productSpecificationDTO
    * @return 结果
    */
    int logicDeleteProductSpecificationByProductSpecificationId(ProductSpecificationDTO productSpecificationDTO);
    /**
    * 逻辑批量删除产品规格表
    *
    * @param ProductSpecificationDtos 需要删除的产品规格表集合
    * @return 结果
    */
    int deleteProductSpecificationByProductSpecificationIds(List<ProductSpecificationDTO> ProductSpecificationDtos);

    /**
    * 逻辑删除产品规格表信息
    *
    * @param productSpecificationDTO
    * @return 结果
    */
    int deleteProductSpecificationByProductSpecificationId(ProductSpecificationDTO productSpecificationDTO);


    /**
    * 删除产品规格表信息
    *
    * @param productSpecificationId 产品规格表主键
    * @return 结果
    */
    int deleteProductSpecificationByProductSpecificationId(Long productSpecificationId);
}
