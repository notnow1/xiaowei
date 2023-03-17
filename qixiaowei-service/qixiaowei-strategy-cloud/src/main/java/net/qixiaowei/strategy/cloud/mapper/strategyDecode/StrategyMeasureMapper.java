package net.qixiaowei.strategy.cloud.mapper.strategyDecode;

import java.util.List;

import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMeasure;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * StrategyMeasureMapper接口
 *
 * @author Graves
 * @since 2023-03-07
 */
public interface StrategyMeasureMapper {
    /**
     * 查询战略举措清单表
     *
     * @param strategyMeasureId 战略举措清单表主键
     * @return 战略举措清单表
     */
    StrategyMeasureDTO selectStrategyMeasureByStrategyMeasureId(@Param("strategyMeasureId") Long strategyMeasureId);


    /**
     * 批量查询战略举措清单表
     *
     * @param strategyMeasureIds 战略举措清单表主键集合
     * @return 战略举措清单表
     */
    List<StrategyMeasureDTO> selectStrategyMeasureByStrategyMeasureIds(@Param("strategyMeasureIds") List<Long> strategyMeasureIds);

    /**
     * 查询战略举措清单表列表
     *
     * @param strategyMeasure 战略举措清单表
     * @return 战略举措清单表集合
     */
    List<StrategyMeasureDTO> selectStrategyMeasureList(@Param("strategyMeasure") StrategyMeasure strategyMeasure);

    /**
     * 新增战略举措清单表
     *
     * @param strategyMeasure 战略举措清单表
     * @return 结果
     */
    int insertStrategyMeasure(@Param("strategyMeasure") StrategyMeasure strategyMeasure);

    /**
     * 修改战略举措清单表
     *
     * @param strategyMeasure 战略举措清单表
     * @return 结果
     */
    int updateStrategyMeasure(@Param("strategyMeasure") StrategyMeasure strategyMeasure);

    /**
     * 批量修改战略举措清单表
     *
     * @param strategyMeasureList 战略举措清单表
     * @return 结果
     */
    int updateStrategyMeasures(@Param("strategyMeasureList") List<StrategyMeasure> strategyMeasureList);

    /**
     * 逻辑删除战略举措清单表
     *
     * @param strategyMeasure
     * @return 结果
     */
    int logicDeleteStrategyMeasureByStrategyMeasureId(@Param("strategyMeasure") StrategyMeasure strategyMeasure);

    /**
     * 逻辑批量删除战略举措清单表
     *
     * @param strategyMeasureIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteStrategyMeasureByStrategyMeasureIds(@Param("strategyMeasureIds") List<Long> strategyMeasureIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除战略举措清单表
     *
     * @param strategyMeasureId 战略举措清单表主键
     * @return 结果
     */
    int deleteStrategyMeasureByStrategyMeasureId(@Param("strategyMeasureId") Long strategyMeasureId);

    /**
     * 物理批量删除战略举措清单表
     *
     * @param strategyMeasureIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteStrategyMeasureByStrategyMeasureIds(@Param("strategyMeasureIds") List<Long> strategyMeasureIds);

    /**
     * 批量新增战略举措清单表
     *
     * @param strategyMeasures 战略举措清单表列表
     * @return 结果
     */
    int batchStrategyMeasure(@Param("strategyMeasures") List<StrategyMeasure> strategyMeasures);

    /**
     * 根据维度ID集合查询
     *
     * @param planBusinessUnitIds 维度ID集合
     * @return list
     */
    List<StrategyMeasureDTO> selectStrategyMeasureByPlanBusinessUnitIds(@Param("planBusinessUnitIds") List<Long> planBusinessUnitIds);

    /**
     * 根据举措清单ID集合查找举措清单列表
     *
     * @param strategyIndexDimensionIds 举措清单ID集合
     * @return 结果
     */
    List<StrategyMeasureDTO> selectStrategyMeasureByStrategyIndexDimensionIds(@Param("strategyIndexDimensionIds") List<Long> strategyIndexDimensionIds);
}
