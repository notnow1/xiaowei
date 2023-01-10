package net.qixiaowei.system.manage.service.basic;

import java.util.List;

import net.qixiaowei.system.manage.api.dto.basic.IndicatorCategoryDTO;


/**
 * IndicatorCategoryService接口
 *
 * @author Graves
 * @since 2022-09-28
 */
public interface IIndicatorCategoryService {
    /**
     * 查询指标分类表
     *
     * @param indicatorCategoryId 指标分类表主键
     * @return 指标分类表
     */
    IndicatorCategoryDTO selectIndicatorCategoryByIndicatorCategoryId(Long indicatorCategoryId);

    /**
     * 查询指标分类表列表
     *
     * @param indicatorCategoryDTO 指标分类表
     * @return 指标分类表集合
     */
    List<IndicatorCategoryDTO> selectIndicatorCategoryList(IndicatorCategoryDTO indicatorCategoryDTO);

    /**
     * 生成指标分类编码
     *
     * @return 指标分类编码
     */
    String generateIndicatorCategoryCode(Integer indicatorType);

    /**
     * 新增指标分类表
     *
     * @param indicatorCategoryDTO 指标分类表
     * @return 结果
     */
    IndicatorCategoryDTO insertIndicatorCategory(IndicatorCategoryDTO indicatorCategoryDTO);

    /**
     * 修改指标分类表
     *
     * @param indicatorCategoryDTO 指标分类表
     * @return 结果
     */
    int updateIndicatorCategory(IndicatorCategoryDTO indicatorCategoryDTO);

    /**
     * 批量修改指标分类表
     *
     * @param indicatorCategoryDtos 指标分类表
     * @return 结果
     */
    int updateIndicatorCategorys(List<IndicatorCategoryDTO> indicatorCategoryDtos);

    /**
     * 批量新增指标分类表
     *
     * @param indicatorCategoryDtos 指标分类表
     * @return 结果
     */
    int insertIndicatorCategorys(List<IndicatorCategoryDTO> indicatorCategoryDtos);

    /**
     * 逻辑批量删除指标分类表
     *
     * @param indicatorCategoryIds 需要删除的指标分类表集合
     * @return 结果
     */
    int logicDeleteIndicatorCategoryByIndicatorCategoryIds(List<Long> indicatorCategoryIds);

    /**
     * 逻辑删除指标分类表信息
     *
     * @param indicatorId
     * @return 结果
     */
    int logicDeleteIndicatorCategoryByIndicatorCategoryId(Long indicatorId);

    /**
     * 逻辑批量删除指标分类表
     *
     * @param IndicatorCategoryDtos 需要删除的指标分类表集合
     * @return 结果
     */
    int deleteIndicatorCategoryByIndicatorCategoryIds(List<IndicatorCategoryDTO> IndicatorCategoryDtos);

    /**
     * 逻辑删除指标分类表信息
     *
     * @param indicatorCategoryDTO
     * @return 结果
     */
    int deleteIndicatorCategoryByIndicatorCategoryId(IndicatorCategoryDTO indicatorCategoryDTO);


    /**
     * 删除指标分类表信息
     *
     * @param indicatorCategoryId 指标分类表主键
     * @return 结果
     */
    int deleteIndicatorCategoryByIndicatorCategoryId(Long indicatorCategoryId);

    /**
     * 指标类型详情
     *
     * @param indicatorCategoryId
     * @return
     */
    IndicatorCategoryDTO detailIndicatorCategory(Long indicatorCategoryId);
}
