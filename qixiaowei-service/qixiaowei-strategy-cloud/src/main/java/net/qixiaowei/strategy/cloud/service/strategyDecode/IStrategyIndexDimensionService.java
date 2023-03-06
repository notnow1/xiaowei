package net.qixiaowei.strategy.cloud.service.strategyDecode;

import cn.hutool.core.lang.tree.Tree;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyIndexDimension;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyIndexDimensionDTO;

import java.util.List;


/**
 * StrategyIndexDimensionService接口
 *
 * @author Graves
 * @since 2023-03-03
 */
public interface IStrategyIndexDimensionService {
    /**
     * 查询战略指标维度表
     *
     * @param strategyIndexDimensionId 战略指标维度表主键
     * @return 战略指标维度表
     */
    StrategyIndexDimensionDTO selectStrategyIndexDimensionByStrategyIndexDimensionId(Long strategyIndexDimensionId);

    /**
     * 查询战略指标维度表列表
     *
     * @param strategyIndexDimensionDTO 战略指标维度表
     * @return 战略指标维度表集合
     */
    List<StrategyIndexDimensionDTO> selectStrategyIndexDimensionList(StrategyIndexDimensionDTO strategyIndexDimensionDTO);

    /**
     * 新增战略指标维度表
     *
     * @param strategyIndexDimensionDTO 战略指标维度表
     * @return 结果
     */
    StrategyIndexDimensionDTO insertStrategyIndexDimension(StrategyIndexDimensionDTO strategyIndexDimensionDTO);

    /**
     * 修改战略指标维度表
     *
     * @param strategyIndexDimensionDTOS 战略指标维度表
     * @return 结果
     */
    int updateStrategyIndexDimension(List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS);

    /**
     * 批量修改战略指标维度表
     *
     * @param strategyIndexDimensionDtos 战略指标维度表
     * @return 结果
     */
    int updateStrategyIndexDimensions(List<StrategyIndexDimensionDTO> strategyIndexDimensionDtos);

    /**
     * 批量新增战略指标维度表
     *
     * @param strategyIndexDimensionDtos 战略指标维度表
     * @return 结果
     */
    List<StrategyIndexDimension> insertStrategyIndexDimensions(List<StrategyIndexDimensionDTO> strategyIndexDimensionDtos);

    /**
     * 逻辑批量删除战略指标维度表
     *
     * @param strategyIndexDimensionIds 需要删除的战略指标维度表集合
     * @return 结果
     */
    int logicDeleteStrategyIndexDimensionByStrategyIndexDimensionIds(List<Long> strategyIndexDimensionIds);

    /**
     * 逻辑删除战略指标维度表信息
     *
     * @param strategyIndexDimensionDTO
     * @return 结果
     */
    int logicDeleteStrategyIndexDimensionByStrategyIndexDimensionId(StrategyIndexDimensionDTO strategyIndexDimensionDTO);

    /**
     * 批量删除战略指标维度表
     *
     * @param StrategyIndexDimensionDtos
     * @return 结果
     */
    int deleteStrategyIndexDimensionByStrategyIndexDimensionIds(List<StrategyIndexDimensionDTO> StrategyIndexDimensionDtos);

    /**
     * 逻辑删除战略指标维度表信息
     *
     * @param strategyIndexDimensionDTO
     * @return 结果
     */
    int deleteStrategyIndexDimensionByStrategyIndexDimensionId(StrategyIndexDimensionDTO strategyIndexDimensionDTO);


    /**
     * 删除战略指标维度表信息
     *
     * @param strategyIndexDimensionId 战略指标维度表主键
     * @return 结果
     */
    int deleteStrategyIndexDimensionByStrategyIndexDimensionId(Long strategyIndexDimensionId);

    /**
     * 获取战略指标维度表信息
     *
     * @param strategyIndexDimensionDTO 战略指标维度表DTO
     * @return 结果
     */
    List<Tree<Long>> selectStrategyIndexDimensionTreeList(StrategyIndexDimensionDTO strategyIndexDimensionDTO);
}
