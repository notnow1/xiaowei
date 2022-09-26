package net.qixiaowei.system.manage.mapper.tenant;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.tenant.ProductPackage;
import net.qixiaowei.system.manage.api.dto.tenant.ProductPackageDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* ProductPackageMapper接口
* @author TANGMICHI
* @since 2022-09-26
*/
public interface ProductPackageMapper{
    /**
    * 查询产品包
    *
    * @param productPackageId 产品包主键
    * @return 产品包
    */
    ProductPackageDTO selectProductPackageByProductPackageId(@Param("productPackageId")Long productPackageId);

    /**
    * 查询产品包列表
    *
    * @param productPackage 产品包
    * @return 产品包集合
    */
    List<ProductPackageDTO> selectProductPackageList(@Param("productPackage")ProductPackage productPackage);

    /**
    * 新增产品包
    *
    * @param productPackage 产品包
    * @return 结果
    */
    int insertProductPackage(@Param("productPackage")ProductPackage productPackage);

    /**
    * 修改产品包
    *
    * @param productPackage 产品包
    * @return 结果
    */
    int updateProductPackage(@Param("productPackage")ProductPackage productPackage);

    /**
    * 批量修改产品包
    *
    * @param productPackageList 产品包
    * @return 结果
    */
    int updateProductPackages(@Param("productPackageList")List<ProductPackage> productPackageList);
    /**
    * 逻辑删除产品包
    *
    * @param productPackage
    * @return 结果
    */
    int logicDeleteProductPackageByProductPackageId(@Param("productPackage")ProductPackage productPackage,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
    * 逻辑批量删除产品包
    *
    * @param productPackageIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteProductPackageByProductPackageIds(@Param("productPackageIds")List<Long> productPackageIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除产品包
    *
    * @param productPackageId 产品包主键
    * @return 结果
    */
    int deleteProductPackageByProductPackageId(@Param("productPackageId")Long productPackageId);

    /**
    * 物理批量删除产品包
    *
    * @param productPackageIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteProductPackageByProductPackageIds(@Param("productPackageIds")List<Long> productPackageIds);

    /**
    * 批量新增产品包
    *
    * @param ProductPackages 产品包列表
    * @return 结果
    */
    int batchProductPackage(@Param("productPackages")List<ProductPackage> ProductPackages);
}
