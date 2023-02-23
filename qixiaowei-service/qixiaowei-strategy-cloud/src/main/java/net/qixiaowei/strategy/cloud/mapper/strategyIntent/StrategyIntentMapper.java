package net.qixiaowei.strategy.cloud.mapper.strategyIntent;

import net.qixiaowei.strategy.cloud.api.domain.strategyIntent.StrategyIntent;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
* StrategyIntentMapper接口
* @author TANGMICHI
* @since 2023-02-23
*/
public interface StrategyIntentMapper{
    /**
    * 查询战略意图表
    *
    * @param strategyIntentId 战略意图表主键
    * @return 战略意图表
    */
    StrategyIntentDTO selectStrategyIntentByStrategyIntentId(@Param("strategyIntentId")Long strategyIntentId);


    /**
    * 批量查询战略意图表
    *
    * @param strategyIntentIds 战略意图表主键集合
    * @return 战略意图表
    */
    List<StrategyIntentDTO> selectStrategyIntentByStrategyIntentIds(@Param("strategyIntentIds") List<Long> strategyIntentIds);

    /**
    * 查询战略意图表列表
    *
    * @param strategyIntent 战略意图表
    * @return 战略意图表集合
    */
    List<StrategyIntentDTO> selectStrategyIntentList(@Param("strategyIntent")StrategyIntent strategyIntent);

    /**
    * 新增战略意图表
    *
    * @param strategyIntent 战略意图表
    * @return 结果
    */
    int insertStrategyIntent(@Param("strategyIntent")StrategyIntent strategyIntent);

    /**
    * 修改战略意图表
    *
    * @param strategyIntent 战略意图表
    * @return 结果
    */
    int updateStrategyIntent(@Param("strategyIntent")StrategyIntent strategyIntent);

    /**
    * 批量修改战略意图表
    *
    * @param strategyIntentList 战略意图表
    * @return 结果
    */
    int updateStrategyIntents(@Param("strategyIntentList")List<StrategyIntent> strategyIntentList);
    /**
    * 逻辑删除战略意图表
    *
    * @param strategyIntent
    * @return 结果
    */
    int logicDeleteStrategyIntentByStrategyIntentId(@Param("strategyIntent")StrategyIntent strategyIntent);

    /**
    * 逻辑批量删除战略意图表
    *
    * @param strategyIntentIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteStrategyIntentByStrategyIntentIds(@Param("strategyIntentIds")List<Long> strategyIntentIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除战略意图表
    *
    * @param strategyIntentId 战略意图表主键
    * @return 结果
    */
    int deleteStrategyIntentByStrategyIntentId(@Param("strategyIntentId")Long strategyIntentId);

    /**
    * 物理批量删除战略意图表
    *
    * @param strategyIntentIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteStrategyIntentByStrategyIntentIds(@Param("strategyIntentIds")List<Long> strategyIntentIds);

    /**
    * 批量新增战略意图表
    *
    * @param strategyIntents 战略意图表列表
    * @return 结果
    */
    int batchStrategyIntent(@Param("strategyIntents")List<StrategyIntent> strategyIntents);
}
