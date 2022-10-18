package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;

import net.qixiaowei.system.manage.api.domain.basic.IndicatorCategory;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorCategoryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * IndicatorCategoryMapper接口
 *
 * @author Graves
 * @since 2022-09-28
 */
public interface IndicatorCategoryMapper {
    /**
     * 查询指标分类表
     *
     * @param indicatorCategoryId 指标分类表主键
     * @return 指标分类表
     */
    IndicatorCategoryDTO selectIndicatorCategoryByIndicatorCategoryId(@Param("indicatorCategoryId") Long indicatorCategoryId);

    /**
     * 根据指标分类ids查找指标分类names
     *
     * @param indicatorCategoryIds
     * @return
     */
    List<IndicatorCategory> selectIndicatorCategoryByIndicatorCategoryIds(@Param("indicatorCategoryIds") List<Long> indicatorCategoryIds);

    /**
     * 查询指标分类表列表
     *
     * @param indicatorCategory 指标分类表
     * @return 指标分类表集合
     */
    List<IndicatorCategoryDTO> selectIndicatorCategoryList(@Param("indicatorCategory") IndicatorCategory indicatorCategory);

    /**
     * 新增指标分类表
     *
     * @param indicatorCategory 指标分类表
     * @return 结果
     */
    int insertIndicatorCategory(@Param("indicatorCategory") IndicatorCategory indicatorCategory);

    /**
     * 修改指标分类表
     *
     * @param indicatorCategory 指标分类表
     * @return 结果
     */
    int updateIndicatorCategory(@Param("indicatorCategory") IndicatorCategory indicatorCategory);

    /**
     * 批量修改指标分类表
     *
     * @param indicatorCategoryList 指标分类表
     * @return 结果
     */
    int updateIndicatorCategorys(@Param("indicatorCategoryList") List<IndicatorCategory> indicatorCategoryList);

    /**
     * 逻辑删除指标分类表
     *
     * @param indicatorCategory
     * @return 结果
     */
    int logicDeleteIndicatorCategoryByIndicatorCategoryId(@Param("indicatorCategory") IndicatorCategory indicatorCategory, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 逻辑批量删除指标分类表
     *
     * @param indicatorCategoryIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteIndicatorCategoryByIndicatorCategoryIds(@Param("indicatorCategoryIds") List<Long> indicatorCategoryIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除指标分类表
     *
     * @param indicatorCategoryId 指标分类表主键
     * @return 结果
     */
    int deleteIndicatorCategoryByIndicatorCategoryId(@Param("indicatorCategoryId") Long indicatorCategoryId);

    /**
     * 物理批量删除指标分类表
     *
     * @param indicatorCategoryIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteIndicatorCategoryByIndicatorCategoryIds(@Param("indicatorCategoryIds") List<Long> indicatorCategoryIds);

    /**
     * 批量新增指标分类表
     *
     * @param IndicatorCategorys 指标分类表列表
     * @return 结果
     */
    int batchIndicatorCategory(@Param("indicatorCategorys") List<IndicatorCategory> IndicatorCategorys);

    /**
     * 指标编码唯一性校验
     *
     * @param indicatorCategoryCode
     * @return
     */
    IndicatorCategoryDTO checkUnique(@Param("indicatorCategoryCode") String indicatorCategoryCode);

    /**
     * 根据id集合判断是否存在
     *
     * @param indicatorCategoryIds
     * @return
     */
    List<Long> isExist(@Param("indicatorCategoryIds") List<Long> indicatorCategoryIds);

}
