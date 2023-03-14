package net.qixiaowei.strategy.cloud.service.strategyDecode;

import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsPlanDTO;

import java.util.List;


/**
 * StrategyMetricsPlanService接口
 *
 * @author Graves
 * @since 2023-03-07
 */
public interface IStrategyMetricsPlanService {
    /**
     * 查询战略衡量指标规划表
     *
     * @param strategyMetricsPlanId 战略衡量指标规划表主键
     * @return 战略衡量指标规划表
     */
    StrategyMetricsPlanDTO selectStrategyMetricsPlanByStrategyMetricsPlanId(Long strategyMetricsPlanId);

    /**
     * 查询战略衡量指标规划表列表
     *
     * @param strategyMetricsPlanDTO 战略衡量指标规划表
     * @return 战略衡量指标规划表集合
     */
    List<StrategyMetricsPlanDTO> selectStrategyMetricsPlanList(StrategyMetricsPlanDTO strategyMetricsPlanDTO);

    /**
     * 新增战略衡量指标规划表
     *
     * @param strategyMetricsPlanDTO 战略衡量指标规划表
     * @return 结果
     */
    StrategyMetricsPlanDTO insertStrategyMetricsPlan(StrategyMetricsPlanDTO strategyMetricsPlanDTO);

    /**
     * 修改战略衡量指标规划表
     *
     * @param strategyMetricsPlanDTO 战略衡量指标规划表
     * @return 结果
     */
    int updateStrategyMetricsPlan(StrategyMetricsPlanDTO strategyMetricsPlanDTO);

    /**
     * 批量修改战略衡量指标规划表
     *
     * @param strategyMetricsPlanDtos 战略衡量指标规划表
     * @return 结果
     */
    int updateStrategyMetricsPlans(List<StrategyMetricsPlanDTO> strategyMetricsPlanDtos);

    /**
     * 批量新增战略衡量指标规划表
     *
     * @param strategyMetricsPlanDtos 战略衡量指标规划表
     * @return 结果
     */
    int insertStrategyMetricsPlans(List<StrategyMetricsPlanDTO> strategyMetricsPlanDtos);

    /**
     * 逻辑批量删除战略衡量指标规划表
     *
     * @param strategyMetricsPlanIds 需要删除的战略衡量指标规划表集合
     * @return 结果
     */
    int logicDeleteStrategyMetricsPlanByStrategyMetricsPlanIds(List<Long> strategyMetricsPlanIds);

    /**
     * 逻辑删除战略衡量指标规划表信息
     *
     * @param strategyMetricsPlanDTO
     * @return 结果
     */
    int logicDeleteStrategyMetricsPlanByStrategyMetricsPlanId(StrategyMetricsPlanDTO strategyMetricsPlanDTO);

    /**
     * 批量删除战略衡量指标规划表
     *
     * @param StrategyMetricsPlanDtos
     * @return 结果
     */
    int deleteStrategyMetricsPlanByStrategyMetricsPlanIds(List<StrategyMetricsPlanDTO> StrategyMetricsPlanDtos);

    /**
     * 逻辑删除战略衡量指标规划表信息
     *
     * @param strategyMetricsPlanDTO
     * @return 结果
     */
    int deleteStrategyMetricsPlanByStrategyMetricsPlanId(StrategyMetricsPlanDTO strategyMetricsPlanDTO);


    /**
     * 删除战略衡量指标规划表信息
     *
     * @param strategyMetricsPlanId 战略衡量指标规划表主键
     * @return 结果
     */
    int deleteStrategyMetricsPlanByStrategyMetricsPlanId(Long strategyMetricsPlanId);

    /**
     * @param editStrategyMetricsDetailIds 战略衡量指标规划表主键
     * @return 结果
     */
    List<StrategyMetricsPlanDTO> selectStrategyMetricsPlanByStrategyMetricsDetailIds(List<Long> editStrategyMetricsDetailIds);
}
