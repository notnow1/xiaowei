package net.qixiaowei.system.manage.service.productPackage;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.productPackage.ProductPackageDTO;
import net.qixiaowei.system.manage.api.vo.productPackage.ProductPackageVO;


/**
* ProductPackageService接口
* @author TANGMICHI
* @since 2022-10-09
*/
public interface IProductPackageService{
    /**
    * 查询产品包
    *
    * @param productPackageId 产品包主键
    * @return 产品包
    */
    ProductPackageDTO selectProductPackageByProductPackageId(Long productPackageId);

    /**
    * 查询产品包列表
    *
    * @param productPackageDTO 产品包
    * @return 产品包集合
    */
    List<ProductPackageDTO> selectProductPackageList(ProductPackageDTO productPackageDTO);

    /**
     * 查询所有产品包列表
     *
     * @return 产品包集合
     */
    List<ProductPackageVO> selectProductPackageAll();

    /**
    * 新增产品包
    *
    * @param productPackageDTO 产品包
    * @return 结果
    */
    int insertProductPackage(ProductPackageDTO productPackageDTO);

    /**
    * 修改产品包
    *
    * @param productPackageDTO 产品包
    * @return 结果
    */
    int updateProductPackage(ProductPackageDTO productPackageDTO);

    /**
    * 批量修改产品包
    *
    * @param productPackageDtos 产品包
    * @return 结果
    */
    int updateProductPackages(List<ProductPackageDTO> productPackageDtos);

    /**
    * 批量新增产品包
    *
    * @param productPackageDtos 产品包
    * @return 结果
    */
    int insertProductPackages(List<ProductPackageDTO> productPackageDtos);

    /**
    * 逻辑批量删除产品包
    *
    * @param ProductPackageDtos 需要删除的产品包集合
    * @return 结果
    */
    int logicDeleteProductPackageByProductPackageIds(List<ProductPackageDTO> ProductPackageDtos);

    /**
    * 逻辑删除产品包信息
    *
    * @param productPackageDTO
    * @return 结果
    */
    int logicDeleteProductPackageByProductPackageId(ProductPackageDTO productPackageDTO);
    /**
    * 逻辑批量删除产品包
    *
    * @param ProductPackageDtos 需要删除的产品包集合
    * @return 结果
    */
    int deleteProductPackageByProductPackageIds(List<ProductPackageDTO> ProductPackageDtos);

    /**
    * 逻辑删除产品包信息
    *
    * @param productPackageDTO
    * @return 结果
    */
    int deleteProductPackageByProductPackageId(ProductPackageDTO productPackageDTO);


    /**
    * 删除产品包信息
    *
    * @param productPackageId 产品包主键
    * @return 结果
    */
    int deleteProductPackageByProductPackageId(Long productPackageId);
}
