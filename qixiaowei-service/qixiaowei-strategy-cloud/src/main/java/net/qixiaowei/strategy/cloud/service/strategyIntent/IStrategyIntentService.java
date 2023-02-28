package net.qixiaowei.strategy.cloud.service.strategyIntent;

import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentOperateDTO;

import java.util.List;



/**
* StrategyIntentService接口
* @author TANGMICHI
* @since 2023-02-23
*/
public interface IStrategyIntentService{
    /**
    * 查询战略意图表
    *
    * @param strategyIntentId 战略意图表主键
    * @return 战略意图表
    */
    StrategyIntentDTO selectStrategyIntentByStrategyIntentId(Long strategyIntentId);

    /**
    * 查询战略意图表列表
    *
    * @param strategyIntentDTO 战略意图表
    * @return 战略意图表集合
    */
    List<StrategyIntentDTO> selectStrategyIntentList(StrategyIntentDTO strategyIntentDTO);

    /**
    * 新增战略意图表
    *
    * @param strategyIntentDTO 战略意图表
    * @return 结果
    */
    StrategyIntentDTO insertStrategyIntent(StrategyIntentDTO strategyIntentDTO);

    /**
    * 修改战略意图表
    *
    * @param strategyIntentDTO 战略意图表
    * @return 结果
    */
    int updateStrategyIntent(StrategyIntentDTO strategyIntentDTO);


    /**
    * 逻辑批量删除战略意图表
    *
    * @param strategyIntentIds 需要删除的战略意图表集合
    * @return 结果
    */
    int logicDeleteStrategyIntentByStrategyIntentIds(List<Long> strategyIntentIds);

    /**
    * 逻辑删除战略意图表信息
    *
    * @param strategyIntentDTO
    * @return 结果
    */
    int logicDeleteStrategyIntentByStrategyIntentId(StrategyIntentDTO strategyIntentDTO);
    /**
    * 批量删除战略意图表
    *
    * @param StrategyIntentDtos
    * @return 结果
    */
    int deleteStrategyIntentByStrategyIntentIds(List<StrategyIntentDTO> StrategyIntentDtos);

    /**
    * 逻辑删除战略意图表信息
    *
    * @param strategyIntentDTO
    * @return 结果
    */
    int deleteStrategyIntentByStrategyIntentId(StrategyIntentDTO strategyIntentDTO);


    /**
    * 删除战略意图表信息
    *
    * @param strategyIntentId 战略意图表主键
    * @return 结果
    */
    int deleteStrategyIntentByStrategyIntentId(Long strategyIntentId);
}
