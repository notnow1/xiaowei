package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMeasureTask;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureTaskDTO;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMeasureTaskMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyMeasureTaskService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * StrategyMeasureTaskService业务层处理
 *
 * @author Graves
 * @since 2023-03-07
 */
@Service
public class StrategyMeasureTaskServiceImpl implements IStrategyMeasureTaskService {
    @Autowired
    private StrategyMeasureTaskMapper strategyMeasureTaskMapper;

    /**
     * 查询战略举措清单任务表
     *
     * @param strategyMeasureTaskId 战略举措清单任务表主键
     * @return 战略举措清单任务表
     */
    @Override
    public StrategyMeasureTaskDTO selectStrategyMeasureTaskByStrategyMeasureTaskId(Long strategyMeasureTaskId) {
        return strategyMeasureTaskMapper.selectStrategyMeasureTaskByStrategyMeasureTaskId(strategyMeasureTaskId);
    }

    /**
     * 查询战略举措清单任务表列表
     *
     * @param strategyMeasureTaskDTO 战略举措清单任务表
     * @return 战略举措清单任务表
     */
    @Override
    public List<StrategyMeasureTaskDTO> selectStrategyMeasureTaskList(StrategyMeasureTaskDTO strategyMeasureTaskDTO) {
        StrategyMeasureTask strategyMeasureTask = new StrategyMeasureTask();
        BeanUtils.copyProperties(strategyMeasureTaskDTO, strategyMeasureTask);
        return strategyMeasureTaskMapper.selectStrategyMeasureTaskList(strategyMeasureTask);
    }

    /**
     * 新增战略举措清单任务表
     *
     * @param strategyMeasureTaskDTO 战略举措清单任务表
     * @return 结果
     */
    @Override
    public StrategyMeasureTaskDTO insertStrategyMeasureTask(StrategyMeasureTaskDTO strategyMeasureTaskDTO) {
        StrategyMeasureTask strategyMeasureTask = new StrategyMeasureTask();
        BeanUtils.copyProperties(strategyMeasureTaskDTO, strategyMeasureTask);
        strategyMeasureTask.setCreateBy(SecurityUtils.getUserId());
        strategyMeasureTask.setCreateTime(DateUtils.getNowDate());
        strategyMeasureTask.setUpdateTime(DateUtils.getNowDate());
        strategyMeasureTask.setUpdateBy(SecurityUtils.getUserId());
        strategyMeasureTask.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        strategyMeasureTaskMapper.insertStrategyMeasureTask(strategyMeasureTask);
        strategyMeasureTaskDTO.setStrategyMeasureTaskId(strategyMeasureTask.getStrategyMeasureTaskId());
        return strategyMeasureTaskDTO;
    }

    /**
     * 修改战略举措清单任务表
     *
     * @param strategyMeasureTaskDTO 战略举措清单任务表
     * @return 结果
     */
    @Override
    public int updateStrategyMeasureTask(StrategyMeasureTaskDTO strategyMeasureTaskDTO) {
        StrategyMeasureTask strategyMeasureTask = new StrategyMeasureTask();
        BeanUtils.copyProperties(strategyMeasureTaskDTO, strategyMeasureTask);
        strategyMeasureTask.setUpdateTime(DateUtils.getNowDate());
        strategyMeasureTask.setUpdateBy(SecurityUtils.getUserId());
        return strategyMeasureTaskMapper.updateStrategyMeasureTask(strategyMeasureTask);
    }

    /**
     * 逻辑批量删除战略举措清单任务表
     *
     * @param strategyMeasureTaskIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMeasureTaskByStrategyMeasureTaskIds(List<Long> strategyMeasureTaskIds) {
        return strategyMeasureTaskMapper.logicDeleteStrategyMeasureTaskByStrategyMeasureTaskIds(strategyMeasureTaskIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除战略举措清单任务表信息
     *
     * @param strategyMeasureTaskId 战略举措清单任务表主键
     * @return 结果
     */
    @Override
    public int deleteStrategyMeasureTaskByStrategyMeasureTaskId(Long strategyMeasureTaskId) {
        return strategyMeasureTaskMapper.deleteStrategyMeasureTaskByStrategyMeasureTaskId(strategyMeasureTaskId);
    }

    /**
     * 根据战略清单ID集合查询战略举措清单任务表信息
     *
     * @param strategyMeasureDetailIds 根据战略清单ID集合
     * @return List
     */
    @Override
    public List<StrategyMeasureTaskDTO> selectStrategyMeasureTaskByStrategyMeasureIds(List<Long> strategyMeasureDetailIds) {
        return strategyMeasureTaskMapper.selectStrategyMeasureTaskByStrategyMeasureIds(strategyMeasureDetailIds);
    }

    /**
     * 根据详情ID集合批量删除任务列表
     *
     * @param strategyMeasureDetailBeforeIds 详情ID集合
     */
    @Override
    public void logicDeleteStrategyMeasureTaskByStrategyMeasureDetailIds(List<Long> strategyMeasureDetailBeforeIds) {
        List<StrategyMeasureTaskDTO> strategyMeasureTaskDTOS = strategyMeasureTaskMapper.selectStrategyMeasureTaskByStrategyMeasureTaskIds(strategyMeasureDetailBeforeIds);
        if (StringUtils.isNotEmpty(strategyMeasureTaskDTOS)) {
            List<Long> strategyMeasureTaskIds = strategyMeasureTaskDTOS.stream().map(StrategyMeasureTaskDTO::getStrategyMeasureTaskId).collect(Collectors.toList());
            strategyMeasureTaskMapper.logicDeleteStrategyMeasureTaskByStrategyMeasureTaskIds(strategyMeasureTaskIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        }
    }

    /**
     * 根据详情ID集合查找任务表
     *
     * @param strategyMeasureDetailId 战略清单详情ID集合
     * @return List
     */
    @Override
    public List<StrategyMeasureTaskDTO> selectStrategyMeasureTaskByStrategyMeasureId(@Param("strategyMeasureDetailId") Long strategyMeasureDetailId) {
        return strategyMeasureTaskMapper.selectStrategyMeasureTaskByStrategyMeasureId(strategyMeasureDetailId);
    }

    /**
     * 逻辑删除战略举措清单任务表信息
     *
     * @param strategyMeasureTaskDTO 战略举措清单任务表
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMeasureTaskByStrategyMeasureTaskId(StrategyMeasureTaskDTO strategyMeasureTaskDTO) {
        StrategyMeasureTask strategyMeasureTask = new StrategyMeasureTask();
        strategyMeasureTask.setStrategyMeasureTaskId(strategyMeasureTaskDTO.getStrategyMeasureTaskId());
        strategyMeasureTask.setUpdateTime(DateUtils.getNowDate());
        strategyMeasureTask.setUpdateBy(SecurityUtils.getUserId());
        return strategyMeasureTaskMapper.logicDeleteStrategyMeasureTaskByStrategyMeasureTaskId(strategyMeasureTask);
    }

    /**
     * 物理删除战略举措清单任务表信息
     *
     * @param strategyMeasureTaskDTO 战略举措清单任务表
     * @return 结果
     */

    @Override
    public int deleteStrategyMeasureTaskByStrategyMeasureTaskId(StrategyMeasureTaskDTO strategyMeasureTaskDTO) {
        StrategyMeasureTask strategyMeasureTask = new StrategyMeasureTask();
        BeanUtils.copyProperties(strategyMeasureTaskDTO, strategyMeasureTask);
        return strategyMeasureTaskMapper.deleteStrategyMeasureTaskByStrategyMeasureTaskId(strategyMeasureTask.getStrategyMeasureTaskId());
    }

    /**
     * 物理批量删除战略举措清单任务表
     *
     * @param strategyMeasureTaskDtos 需要删除的战略举措清单任务表主键
     * @return 结果
     */

    @Override
    public int deleteStrategyMeasureTaskByStrategyMeasureTaskIds(List<StrategyMeasureTaskDTO> strategyMeasureTaskDtos) {
        List<Long> stringList = new ArrayList<>();
        for (StrategyMeasureTaskDTO strategyMeasureTaskDTO : strategyMeasureTaskDtos) {
            stringList.add(strategyMeasureTaskDTO.getStrategyMeasureTaskId());
        }
        return strategyMeasureTaskMapper.deleteStrategyMeasureTaskByStrategyMeasureTaskIds(stringList);
    }

    /**
     * 批量新增战略举措清单任务表信息
     *
     * @param strategyMeasureTaskDtos 战略举措清单任务表对象
     */

    public int insertStrategyMeasureTasks(List<StrategyMeasureTaskDTO> strategyMeasureTaskDtos) {
        List<StrategyMeasureTask> strategyMeasureTaskList = new ArrayList<>();

        for (StrategyMeasureTaskDTO strategyMeasureTaskDTO : strategyMeasureTaskDtos) {
            StrategyMeasureTask strategyMeasureTask = new StrategyMeasureTask();
            BeanUtils.copyProperties(strategyMeasureTaskDTO, strategyMeasureTask);
            strategyMeasureTask.setCreateBy(SecurityUtils.getUserId());
            strategyMeasureTask.setCreateTime(DateUtils.getNowDate());
            strategyMeasureTask.setUpdateTime(DateUtils.getNowDate());
            strategyMeasureTask.setUpdateBy(SecurityUtils.getUserId());
            strategyMeasureTask.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyMeasureTaskList.add(strategyMeasureTask);
        }
        return strategyMeasureTaskMapper.batchStrategyMeasureTask(strategyMeasureTaskList);
    }

    /**
     * 批量修改战略举措清单任务表信息
     *
     * @param strategyMeasureTaskDtos 战略举措清单任务表对象
     */

    public int updateStrategyMeasureTasks(List<StrategyMeasureTaskDTO> strategyMeasureTaskDtos) {
        List<StrategyMeasureTask> strategyMeasureTaskList = new ArrayList<>();

        for (StrategyMeasureTaskDTO strategyMeasureTaskDTO : strategyMeasureTaskDtos) {
            StrategyMeasureTask strategyMeasureTask = new StrategyMeasureTask();
            BeanUtils.copyProperties(strategyMeasureTaskDTO, strategyMeasureTask);
            strategyMeasureTask.setCreateBy(SecurityUtils.getUserId());
            strategyMeasureTask.setCreateTime(DateUtils.getNowDate());
            strategyMeasureTask.setUpdateTime(DateUtils.getNowDate());
            strategyMeasureTask.setUpdateBy(SecurityUtils.getUserId());
            strategyMeasureTaskList.add(strategyMeasureTask);
        }
        return strategyMeasureTaskMapper.updateStrategyMeasureTasks(strategyMeasureTaskList);
    }

}

