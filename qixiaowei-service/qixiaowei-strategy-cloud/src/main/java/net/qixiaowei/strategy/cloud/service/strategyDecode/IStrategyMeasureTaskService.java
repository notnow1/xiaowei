package net.qixiaowei.strategy.cloud.service.strategyDecode;

import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureTaskDTO;

import java.util.List;


/**
 * StrategyMeasureTaskService接口
 *
 * @author Graves
 * @since 2023-03-07
 */
public interface IStrategyMeasureTaskService {
    /**
     * 查询战略举措清单任务表
     *
     * @param strategyMeasureTaskId 战略举措清单任务表主键
     * @return 战略举措清单任务表
     */
    StrategyMeasureTaskDTO selectStrategyMeasureTaskByStrategyMeasureTaskId(Long strategyMeasureTaskId);

    /**
     * 查询战略举措清单任务表列表
     *
     * @param strategyMeasureTaskDTO 战略举措清单任务表
     * @return 战略举措清单任务表集合
     */
    List<StrategyMeasureTaskDTO> selectStrategyMeasureTaskList(StrategyMeasureTaskDTO strategyMeasureTaskDTO);

    /**
     * 新增战略举措清单任务表
     *
     * @param strategyMeasureTaskDTO 战略举措清单任务表
     * @return 结果
     */
    StrategyMeasureTaskDTO insertStrategyMeasureTask(StrategyMeasureTaskDTO strategyMeasureTaskDTO);

    /**
     * 修改战略举措清单任务表
     *
     * @param strategyMeasureTaskDTO 战略举措清单任务表
     * @return 结果
     */
    int updateStrategyMeasureTask(StrategyMeasureTaskDTO strategyMeasureTaskDTO);

    /**
     * 批量修改战略举措清单任务表
     *
     * @param strategyMeasureTaskDtos 战略举措清单任务表
     * @return 结果
     */
    int updateStrategyMeasureTasks(List<StrategyMeasureTaskDTO> strategyMeasureTaskDtos);

    /**
     * 批量新增战略举措清单任务表
     *
     * @param strategyMeasureTaskDtos 战略举措清单任务表
     * @return 结果
     */
    int insertStrategyMeasureTasks(List<StrategyMeasureTaskDTO> strategyMeasureTaskDtos);

    /**
     * 逻辑批量删除战略举措清单任务表
     *
     * @param strategyMeasureTaskIds 需要删除的战略举措清单任务表集合
     * @return 结果
     */
    int logicDeleteStrategyMeasureTaskByStrategyMeasureTaskIds(List<Long> strategyMeasureTaskIds);

    /**
     * 逻辑删除战略举措清单任务表信息
     *
     * @param strategyMeasureTaskDTO
     * @return 结果
     */
    int logicDeleteStrategyMeasureTaskByStrategyMeasureTaskId(StrategyMeasureTaskDTO strategyMeasureTaskDTO);

    /**
     * 批量删除战略举措清单任务表
     *
     * @param StrategyMeasureTaskDtos
     * @return 结果
     */
    int deleteStrategyMeasureTaskByStrategyMeasureTaskIds(List<StrategyMeasureTaskDTO> StrategyMeasureTaskDtos);

    /**
     * 逻辑删除战略举措清单任务表信息
     *
     * @param strategyMeasureTaskDTO
     * @return 结果
     */
    int deleteStrategyMeasureTaskByStrategyMeasureTaskId(StrategyMeasureTaskDTO strategyMeasureTaskDTO);


    /**
     * 删除战略举措清单任务表信息
     *
     * @param strategyMeasureTaskId 战略举措清单任务表主键
     * @return 结果
     */
    int deleteStrategyMeasureTaskByStrategyMeasureTaskId(Long strategyMeasureTaskId);

}
