package net.qixiaowei.strategy.cloud.mapper.strategyDecode;

import java.util.List;

import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMeasureTask;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureTaskDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * StrategyMeasureTaskMapper接口
 *
 * @author Graves
 * @since 2023-03-07
 */
public interface StrategyMeasureTaskMapper {
    /**
     * 查询战略举措清单任务表
     *
     * @param strategyMeasureTaskId 战略举措清单任务表主键
     * @return 战略举措清单任务表
     */
    StrategyMeasureTaskDTO selectStrategyMeasureTaskByStrategyMeasureTaskId(@Param("strategyMeasureTaskId") Long strategyMeasureTaskId);


    /**
     * 批量查询战略举措清单任务表
     *
     * @param strategyMeasureTaskIds 战略举措清单任务表主键集合
     * @return 战略举措清单任务表
     */
    List<StrategyMeasureTaskDTO> selectStrategyMeasureTaskByStrategyMeasureTaskIds(@Param("strategyMeasureTaskIds") List<Long> strategyMeasureTaskIds);

    /**
     * 查询战略举措清单任务表列表
     *
     * @param strategyMeasureTask 战略举措清单任务表
     * @return 战略举措清单任务表集合
     */
    List<StrategyMeasureTaskDTO> selectStrategyMeasureTaskList(@Param("strategyMeasureTask") StrategyMeasureTask strategyMeasureTask);

    /**
     * 新增战略举措清单任务表
     *
     * @param strategyMeasureTask 战略举措清单任务表
     * @return 结果
     */
    int insertStrategyMeasureTask(@Param("strategyMeasureTask") StrategyMeasureTask strategyMeasureTask);

    /**
     * 修改战略举措清单任务表
     *
     * @param strategyMeasureTask 战略举措清单任务表
     * @return 结果
     */
    int updateStrategyMeasureTask(@Param("strategyMeasureTask") StrategyMeasureTask strategyMeasureTask);

    /**
     * 批量修改战略举措清单任务表
     *
     * @param strategyMeasureTaskList 战略举措清单任务表
     * @return 结果
     */
    int updateStrategyMeasureTasks(@Param("strategyMeasureTaskList") List<StrategyMeasureTask> strategyMeasureTaskList);

    /**
     * 逻辑删除战略举措清单任务表
     *
     * @param strategyMeasureTask
     * @return 结果
     */
    int logicDeleteStrategyMeasureTaskByStrategyMeasureTaskId(@Param("strategyMeasureTask") StrategyMeasureTask strategyMeasureTask);

    /**
     * 逻辑批量删除战略举措清单任务表
     *
     * @param strategyMeasureTaskIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteStrategyMeasureTaskByStrategyMeasureTaskIds(@Param("strategyMeasureTaskIds") List<Long> strategyMeasureTaskIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除战略举措清单任务表
     *
     * @param strategyMeasureTaskId 战略举措清单任务表主键
     * @return 结果
     */
    int deleteStrategyMeasureTaskByStrategyMeasureTaskId(@Param("strategyMeasureTaskId") Long strategyMeasureTaskId);

    /**
     * 物理批量删除战略举措清单任务表
     *
     * @param strategyMeasureTaskIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteStrategyMeasureTaskByStrategyMeasureTaskIds(@Param("strategyMeasureTaskIds") List<Long> strategyMeasureTaskIds);

    /**
     * 批量新增战略举措清单任务表
     *
     * @param strategyMeasureTasks 战略举措清单任务表列表
     * @return 结果
     */
    int batchStrategyMeasureTask(@Param("strategyMeasureTasks") List<StrategyMeasureTask> strategyMeasureTasks);

    /**
     * 根据战略清单ID集合查询战略举措清单任务表信息
     *
     * @param strategyMeasureDetailIds 根据战略清单ID集合
     * @return List
     */
    List<StrategyMeasureTaskDTO> selectStrategyMeasureTaskByStrategyMeasureIds(@Param("strategyMeasureDetailIds") List<Long> strategyMeasureDetailIds);

    /**
     * 根据详情ID集合查找任务表
     *
     * @param strategyMeasureDetailId 战略清单详情ID集合
     * @return List
     */
    List<StrategyMeasureTaskDTO> selectStrategyMeasureTaskByStrategyMeasureId(@Param("strategyMeasureDetailId") Long strategyMeasureDetailId);
}
