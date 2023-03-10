package net.qixiaowei.strategy.cloud.mapper.strategyDecode;

import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyIndexDimension;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyIndexDimensionDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * StrategyIndexDimensionMapper接口
 *
 * @author Graves
 * @since 2023-03-03
 */
public interface StrategyIndexDimensionMapper {
    /**
     * 查询战略指标维度表
     *
     * @param strategyIndexDimensionId 战略指标维度表主键
     * @return 战略指标维度表
     */
    StrategyIndexDimensionDTO selectStrategyIndexDimensionByStrategyIndexDimensionId(@Param("strategyIndexDimensionId") Long strategyIndexDimensionId);


    /**
     * 批量查询战略指标维度表
     *
     * @param strategyIndexDimensionIds 战略指标维度表主键集合
     * @return 战略指标维度表
     */
    List<StrategyIndexDimensionDTO> selectStrategyIndexDimensionByStrategyIndexDimensionIds(@Param("strategyIndexDimensionIds") List<Long> strategyIndexDimensionIds);

    /**
     * 查询战略指标维度表列表
     *
     * @param strategyIndexDimension 战略指标维度表
     * @return 战略指标维度表集合
     */
    List<StrategyIndexDimensionDTO> selectStrategyIndexDimensionList(@Param("strategyIndexDimension") StrategyIndexDimension strategyIndexDimension);

    /**
     * 新增战略指标维度表
     *
     * @param strategyIndexDimension 战略指标维度表
     * @return 结果
     */
    int insertStrategyIndexDimension(@Param("strategyIndexDimension") StrategyIndexDimension strategyIndexDimension);

    /**
     * 修改战略指标维度表
     *
     * @param strategyIndexDimension 战略指标维度表
     * @return 结果
     */
    int updateStrategyIndexDimension(@Param("strategyIndexDimension") StrategyIndexDimension strategyIndexDimension);

    /**
     * 批量修改战略指标维度表
     *
     * @param strategyIndexDimensionList 战略指标维度表
     * @return 结果
     */
    int updateStrategyIndexDimensions(@Param("strategyIndexDimensionList") List<StrategyIndexDimension> strategyIndexDimensionList);

    /**
     * 逻辑删除战略指标维度表
     *
     * @param strategyIndexDimension
     * @return 结果
     */
    int logicDeleteStrategyIndexDimensionByStrategyIndexDimensionId(@Param("strategyIndexDimension") StrategyIndexDimension strategyIndexDimension);

    /**
     * 逻辑批量删除战略指标维度表
     *
     * @param strategyIndexDimensionIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteStrategyIndexDimensionByStrategyIndexDimensionIds(@Param("strategyIndexDimensionIds") List<Long> strategyIndexDimensionIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除战略指标维度表
     *
     * @param strategyIndexDimensionId 战略指标维度表主键
     * @return 结果
     */
    int deleteStrategyIndexDimensionByStrategyIndexDimensionId(@Param("strategyIndexDimensionId") Long strategyIndexDimensionId);

    /**
     * 物理批量删除战略指标维度表
     *
     * @param strategyIndexDimensionIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteStrategyIndexDimensionByStrategyIndexDimensionIds(@Param("strategyIndexDimensionIds") List<Long> strategyIndexDimensionIds);

    /**
     * 批量新增战略指标维度表
     *
     * @param strategyIndexDimensions 战略指标维度表列表
     * @return 结果
     */
    int batchStrategyIndexDimension(@Param("strategyIndexDimensions") List<StrategyIndexDimension> strategyIndexDimensions);

    /**
     * @param strategyIndexDimensionId 战略指标维度表ID
     * @return list
     */
    List<StrategyIndexDimensionDTO> selectStrategyIndexDimensionOtherList(@Param("strategyIndexDimensionId") Long strategyIndexDimensionId);
}
