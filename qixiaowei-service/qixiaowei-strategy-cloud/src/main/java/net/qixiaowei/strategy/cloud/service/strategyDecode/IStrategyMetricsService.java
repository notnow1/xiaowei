package net.qixiaowei.strategy.cloud.service.strategyDecode;

import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDetailDTO;

import java.util.List;


/**
 * StrategyMetricsService接口
 *
 * @author Graves
 * @since 2023-03-07
 */
public interface IStrategyMetricsService {
    /**
     * 查询战略衡量指标表
     *
     * @param strategyMetricsId 战略衡量指标表主键
     * @return 战略衡量指标表
     */
    StrategyMetricsDTO selectStrategyMetricsByStrategyMetricsId(Long strategyMetricsId);

    /**
     * 查询战略衡量指标表列表
     *
     * @param strategyMetricsDTO 战略衡量指标表
     * @return 战略衡量指标表集合
     */
    List<StrategyMetricsDTO> selectStrategyMetricsList(StrategyMetricsDTO strategyMetricsDTO);

    /**
     * 新增战略衡量指标表
     *
     * @param strategyMetricsDTO 战略衡量指标表
     * @return 结果
     */
    Long insertStrategyMetrics(StrategyMetricsDTO strategyMetricsDTO);

    /**
     * 修改战略衡量指标表
     *
     * @param strategyMetricsDTO 战略衡量指标表
     * @return 结果
     */
    int updateStrategyMetrics(StrategyMetricsDTO strategyMetricsDTO);

    /**
     * 更新衡量指标表
     *
     * @param strategyMetricsDTO 衡量指标DTO
     */
    Long editStrategyMetrics(StrategyMetricsDTO strategyMetricsDTO);

    /**
     * 批量修改战略衡量指标表
     *
     * @param strategyMetricsDtos 战略衡量指标表
     * @return 结果
     */
    int updateStrategyMetricss(List<StrategyMetricsDTO> strategyMetricsDtos);

    /**
     * 批量新增战略衡量指标表
     *
     * @param strategyMetricsDtos 战略衡量指标表
     * @return 结果
     */
    int insertStrategyMetricss(List<StrategyMetricsDTO> strategyMetricsDtos);

    /**
     * 逻辑批量删除战略衡量指标表
     *
     * @param strategyMetricsIds 需要删除的战略衡量指标表集合
     * @return 结果
     */
    int logicDeleteStrategyMetricsByStrategyMetricsIds(List<Long> strategyMetricsIds);

    /**
     * 逻辑删除战略衡量指标表信息
     *
     * @param strategyMetricsDTO
     * @return 结果
     */
    int logicDeleteStrategyMetricsByStrategyMetricsId(StrategyMetricsDTO strategyMetricsDTO);

    /**
     * 批量删除战略衡量指标表
     *
     * @param StrategyMetricsDtos
     * @return 结果
     */
    int deleteStrategyMetricsByStrategyMetricsIds(List<StrategyMetricsDTO> StrategyMetricsDtos);

    /**
     * 逻辑删除战略衡量指标表信息
     *
     * @param strategyMetricsDTO
     * @return 结果
     */
    int deleteStrategyMetricsByStrategyMetricsId(StrategyMetricsDTO strategyMetricsDTO);


    /**
     * 删除战略衡量指标表信息
     *
     * @param strategyMetricsId 战略衡量指标表主键
     * @return 结果
     */
    int deleteStrategyMetricsByStrategyMetricsId(Long strategyMetricsId);

    /**
     * 根据清单ID查询战略衡量指标
     *
     * @param strategyMeasureId 清单ID
     * @return 战略衡量指标
     */
    StrategyMetricsDTO selectStrategyMetricsByStrategyMeasureId(Long strategyMeasureId);

    /**
     * 根据清单ID集合查询战略衡量指标
     *
     * @param strategyMeasureIds 清单ID集合
     * @return List
     */
    List<StrategyMetricsDTO> selectStrategyMetricsByStrategyMeasureIds(List<Long> strategyMeasureIds);

    /**
     * 获取远程列表
     *
     * @param strategyMetricsDTO 衡量指标DTO
     * @return List
     */
    List<StrategyMetricsDTO> remoteStrategyMetrics(StrategyMetricsDTO strategyMetricsDTO);

    /**
     * 远程根据指标ID集合查找战略衡量指标
     *
     * @param strategyMetricsDTO 战略衡量指标
     * @return 结果
     */
    List<StrategyMetricsDetailDTO> remoteListByIndicator(StrategyMetricsDTO strategyMetricsDTO);
}
