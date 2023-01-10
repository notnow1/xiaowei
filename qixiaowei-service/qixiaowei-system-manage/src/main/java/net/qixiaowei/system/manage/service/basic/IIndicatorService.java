package net.qixiaowei.system.manage.service.basic;

import cn.hutool.core.lang.tree.Tree;
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
     * 关键经营结果获取Indicator
     *
     * @param indicatorDTO 指标表
     * @return 指标表集合
     */
    List<IndicatorDTO> selectTargetIndicatorList(IndicatorDTO indicatorDTO);

    /**
     * 查询指标表列表通过指标编码
     *
     * @param indicatorCode 指标编码
     * @return 指标表集合
     */
    IndicatorDTO selectIndicatorByCode(String indicatorCode);

    /**
     * 查询指标表树状图
     *
     * @param indicatorDTO
     * @return
     */
    List<Tree<Long>> selectTreeList(IndicatorDTO indicatorDTO);

    /**
     * 生成指标编码
     *
     * @return 指标编码
     */
    String generateIndicatorCode(Integer indicatorType);

    /**
     * 新增指标表
     *
     * @param indicatorDTO 指标表
     * @return 结果
     */
    IndicatorDTO insertIndicator(IndicatorDTO indicatorDTO);

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
     * @param indicatorIds 需要删除的指标表集合
     * @return 结果
     */
    int logicDeleteIndicatorByIndicatorIds(List<Long> indicatorIds);

    /**
     * 逻辑删除指标表信息
     *
     * @param indicatorId
     * @return 结果
     */
    int logicDeleteIndicatorByIndicatorId(Long indicatorId);

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

    /**
     * 指标详情
     *
     * @param indicatorId
     * @return
     */
    IndicatorDTO detailIndicator(Long indicatorId);

    /**
     * 获取指标最大层级
     *
     * @return
     */
    List<Integer> getLevel();

    /**
     * 通过CodeList查找指标列表
     *
     * @param indicatorCodes
     * @return
     */
    List<IndicatorDTO> selectIndicatorByCodeList(List<String> indicatorCodes);

    /**
     * 通过指标IDS查找指标
     *
     * @param indicatorIds
     * @return
     */
    List<IndicatorDTO> selectIndicatorByIds(List<Long> indicatorIds);

    /**
     * 通过指标名称获取指标内容
     *
     * @param indicatorNames 指标名称集合
     * @return
     */
    List<IndicatorDTO> selectIndicatorByNames(List<String> indicatorNames);

    /**
     * 通过ID查询指标列表
     *
     * @param indicatorId
     * @return
     */
    IndicatorDTO selectIndicatorById(Long indicatorId);

    /**
     * 查找驱动因素为“是”的指标项
     *
     * @return
     */
    List<IndicatorDTO> selectIsDriverList();

    /**
     * 查询绩效的指标表树状图下拉
     *
     * @param indicatorDTO
     * @return
     */
    List<Tree<Long>> performanceTreeList(IndicatorDTO indicatorDTO);
}
