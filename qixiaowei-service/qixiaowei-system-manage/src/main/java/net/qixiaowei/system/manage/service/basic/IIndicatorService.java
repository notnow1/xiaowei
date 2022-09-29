package net.qixiaowei.system.manage.service.basic;

import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;

import java.util.List;


/**
 * IndicatorService接口
 *
 * @author Graves
 * @since 2022-09-28
 */
public interface IIndicatorService {
    /**
     * 查询指标表
     *
     * @param indicatorId 指标表主键
     * @return 指标表
     */
    IndicatorDTO selectIndicatorByIndicatorId(Long indicatorId);

    /**
     * 查询指标表列表
     *
     * @param indicatorDTO 指标表
     * @return 指标表集合
     */
    List<IndicatorDTO> selectIndicatorList(IndicatorDTO indicatorDTO);

    /**
     * 查询指标表树状图
     *
     * @param indicatorDTO
     * @return
     */
    List<IndicatorDTO> selectTreeList(IndicatorDTO indicatorDTO);

    /**
     * 新增指标表
     *
     * @param indicatorDTO 指标表
     * @return 结果
     */
    int insertIndicator(IndicatorDTO indicatorDTO);

    /**
     * 修改指标表
     *
     * @param indicatorDTO 指标表
     * @return 结果
     */
    int updateIndicator(IndicatorDTO indicatorDTO);

    /**
     * 批量修改指标表
     *
     * @param indicatorDtos 指标表
     * @return 结果
     */
    int updateIndicators(List<IndicatorDTO> indicatorDtos);

    /**
     * 批量新增指标表
     *
     * @param indicatorDtos 指标表
     * @return 结果
     */
    int insertIndicators(List<IndicatorDTO> indicatorDtos);

    /**
     * 逻辑批量删除指标表-
     *
     * @param IndicatorDtos 需要删除的指标表集合
     * @return 结果
     */
    int logicDeleteIndicatorByIndicatorIds(List<IndicatorDTO> IndicatorDtos);

    /**
     * 逻辑删除指标表信息
     *
     * @param indicatorDTO
     * @return 结果
     */
    int logicDeleteIndicatorByIndicatorId(IndicatorDTO indicatorDTO);

    /**
     * 逻辑批量删除指标表
     *
     * @param IndicatorDtos 需要删除的指标表集合
     * @return 结果
     */
    int deleteIndicatorByIndicatorIds(List<IndicatorDTO> IndicatorDtos);

    /**
     * 逻辑删除指标表信息
     *
     * @param indicatorDTO
     * @return 结果
     */
    int deleteIndicatorByIndicatorId(IndicatorDTO indicatorDTO);


    /**
     * 删除指标表信息
     *
     * @param indicatorId 指标表主键
     * @return 结果
     */
    int deleteIndicatorByIndicatorId(Long indicatorId);
}
