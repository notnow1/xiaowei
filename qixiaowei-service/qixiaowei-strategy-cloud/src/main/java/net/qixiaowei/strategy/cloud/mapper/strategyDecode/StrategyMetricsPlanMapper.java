package net.qixiaowei.strategy.cloud.mapper.strategyDecode;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMetricsPlan;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsPlanDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* StrategyMetricsPlanMapper接口
* @author Graves
* @since 2023-03-07
*/
public interface StrategyMetricsPlanMapper{
    /**
    * 查询战略衡量指标规划表
    *
    * @param strategyMetricsPlanId 战略衡量指标规划表主键
    * @return 战略衡量指标规划表
    */
    StrategyMetricsPlanDTO selectStrategyMetricsPlanByStrategyMetricsPlanId(@Param("strategyMetricsPlanId")Long strategyMetricsPlanId);


    /**
    * 批量查询战略衡量指标规划表
    *
    * @param strategyMetricsPlanIds 战略衡量指标规划表主键集合
    * @return 战略衡量指标规划表
    */
    List<StrategyMetricsPlanDTO> selectStrategyMetricsPlanByStrategyMetricsPlanIds(@Param("strategyMetricsPlanIds") List<Long> strategyMetricsPlanIds);

    /**
    * 查询战略衡量指标规划表列表
    *
    * @param strategyMetricsPlan 战略衡量指标规划表
    * @return 战略衡量指标规划表集合
    */
    List<StrategyMetricsPlanDTO> selectStrategyMetricsPlanList(@Param("strategyMetricsPlan")StrategyMetricsPlan strategyMetricsPlan);

    /**
    * 新增战略衡量指标规划表
    *
    * @param strategyMetricsPlan 战略衡量指标规划表
    * @return 结果
    */
    int insertStrategyMetricsPlan(@Param("strategyMetricsPlan")StrategyMetricsPlan strategyMetricsPlan);

    /**
    * 修改战略衡量指标规划表
    *
    * @param strategyMetricsPlan 战略衡量指标规划表
    * @return 结果
    */
    int updateStrategyMetricsPlan(@Param("strategyMetricsPlan")StrategyMetricsPlan strategyMetricsPlan);

    /**
    * 批量修改战略衡量指标规划表
    *
    * @param strategyMetricsPlanList 战略衡量指标规划表
    * @return 结果
    */
    int updateStrategyMetricsPlans(@Param("strategyMetricsPlanList")List<StrategyMetricsPlan> strategyMetricsPlanList);
    /**
    * 逻辑删除战略衡量指标规划表
    *
    * @param strategyMetricsPlan
    * @return 结果
    */
    int logicDeleteStrategyMetricsPlanByStrategyMetricsPlanId(@Param("strategyMetricsPlan")StrategyMetricsPlan strategyMetricsPlan);

    /**
    * 逻辑批量删除战略衡量指标规划表
    *
    * @param strategyMetricsPlanIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteStrategyMetricsPlanByStrategyMetricsPlanIds(@Param("strategyMetricsPlanIds")List<Long> strategyMetricsPlanIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除战略衡量指标规划表
    *
    * @param strategyMetricsPlanId 战略衡量指标规划表主键
    * @return 结果
    */
    int deleteStrategyMetricsPlanByStrategyMetricsPlanId(@Param("strategyMetricsPlanId")Long strategyMetricsPlanId);

    /**
    * 物理批量删除战略衡量指标规划表
    *
    * @param strategyMetricsPlanIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteStrategyMetricsPlanByStrategyMetricsPlanIds(@Param("strategyMetricsPlanIds")List<Long> strategyMetricsPlanIds);

    /**
    * 批量新增战略衡量指标规划表
    *
    * @param strategyMetricsPlans 战略衡量指标规划表列表
    * @return 结果
    */
    int batchStrategyMetricsPlan(@Param("strategyMetricsPlans")List<StrategyMetricsPlan> strategyMetricsPlans);
}
