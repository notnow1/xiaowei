package net.qixiaowei.system.manage.mapper.productPackage;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.productPackage.ProductPackage;
import net.qixiaowei.system.manage.api.dto.productPackage.ProductPackageDTO;
import net.qixiaowei.system.manage.api.vo.productPackage.ProductPackageVO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* ProductPackageMapper接口
* @author TANGMICHI
* @since 2022-10-09
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
     * 批量查询产品包
     *
     * @param productPackageIds 产品包主键集合
     * @return 产品包
     */
    List<ProductPackageDTO> selectProductPackageByProductPackageIds(@Param("productPackageIds")List<Long> productPackageIds);

    /**
     * 根据产品包名查询产品包
     *
     * @param productPackageName 产品包名称
     * @return 产品包
     */
    ProductPackageDTO selectProductPackageByProductPackageName(@Param("productPackageName")String productPackageName);

    /**
     * 根据产品包名批量查询产品包
     *
     * @param productPackageNames 产品包名称
     * @return 产品包
     */
    List<ProductPackageDTO> selectProductPackageByProductPackageNames(@Param("productPackageNames")List<String> productPackageNames);
    /**
    * 查询产品包列表
    *
    * @param productPackage 产品包
    * @return 产品包集合
    */
    List<ProductPackageDTO> selectProductPackageList(@Param("productPackage")ProductPackage productPackage);

    /**
     * 查询所有产品包列表
     *
     * @return 产品包集合
     */
    List<ProductPackageVO> selectProductPackageAll();

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
    int logicDeleteProductPackageByProductPackageId(@Param("productPackage")ProductPackage productPackage);

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
