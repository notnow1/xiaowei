package net.qixiaowei.strategy.cloud.mapper.strategyDecode;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMetrics;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* StrategyMetricsMapper接口
* @author Graves
* @since 2023-03-07
*/
public interface StrategyMetricsMapper{
    /**
    * 查询战略衡量指标表
    *
    * @param strategyMetricsId 战略衡量指标表主键
    * @return 战略衡量指标表
    */
    StrategyMetricsDTO selectStrategyMetricsByStrategyMetricsId(@Param("strategyMetricsId")Long strategyMetricsId);


    /**
    * 批量查询战略衡量指标表
    *
    * @param strategyMetricsIds 战略衡量指标表主键集合
    * @return 战略衡量指标表
    */
    List<StrategyMetricsDTO> selectStrategyMetricsByStrategyMetricsIds(@Param("strategyMetricsIds") List<Long> strategyMetricsIds);

    /**
    * 查询战略衡量指标表列表
    *
    * @param strategyMetrics 战略衡量指标表
    * @return 战略衡量指标表集合
    */
    List<StrategyMetricsDTO> selectStrategyMetricsList(@Param("strategyMetrics")StrategyMetrics strategyMetrics);

    /**
    * 新增战略衡量指标表
    *
    * @param strategyMetrics 战略衡量指标表
    * @return 结果
    */
    int insertStrategyMetrics(@Param("strategyMetrics")StrategyMetrics strategyMetrics);

    /**
    * 修改战略衡量指标表
    *
    * @param strategyMetrics 战略衡量指标表
    * @return 结果
    */
    int updateStrategyMetrics(@Param("strategyMetrics")StrategyMetrics strategyMetrics);

    /**
    * 批量修改战略衡量指标表
    *
    * @param strategyMetricsList 战略衡量指标表
    * @return 结果
    */
    int updateStrategyMetricss(@Param("strategyMetricsList")List<StrategyMetrics> strategyMetricsList);
    /**
    * 逻辑删除战略衡量指标表
    *
    * @param strategyMetrics
    * @return 结果
    */
    int logicDeleteStrategyMetricsByStrategyMetricsId(@Param("strategyMetrics")StrategyMetrics strategyMetrics);

    /**
    * 逻辑批量删除战略衡量指标表
    *
    * @param strategyMetricsIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteStrategyMetricsByStrategyMetricsIds(@Param("strategyMetricsIds")List<Long> strategyMetricsIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除战略衡量指标表
    *
    * @param strategyMetricsId 战略衡量指标表主键
    * @return 结果
    */
    int deleteStrategyMetricsByStrategyMetricsId(@Param("strategyMetricsId")Long strategyMetricsId);

    /**
    * 物理批量删除战略衡量指标表
    *
    * @param strategyMetricsIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteStrategyMetricsByStrategyMetricsIds(@Param("strategyMetricsIds")List<Long> strategyMetricsIds);

    /**
    * 批量新增战略衡量指标表
    *
    * @param strategyMetricss 战略衡量指标表列表
    * @return 结果
    */
    int batchStrategyMetrics(@Param("strategyMetricss")List<StrategyMetrics> strategyMetricss);
}
