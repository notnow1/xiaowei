package net.qixiaowei.strategy.cloud.mapper.plan;

import net.qixiaowei.strategy.cloud.api.domain.plan.PlanBusinessUnit;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * PlanBusinessUnitMapper接口
 *
 * @author wangchen
 * @since 2023-02-17
 */
public interface PlanBusinessUnitMapper {
    /**
     * 查询规划业务单元
     *
     * @param planBusinessUnitId 规划业务单元主键
     * @return 规划业务单元
     */
    PlanBusinessUnitDTO selectPlanBusinessUnitByPlanBusinessUnitId(@Param("planBusinessUnitId") Long planBusinessUnitId);


    /**
     * 批量查询规划业务单元
     *
     * @param planBusinessUnitIds 规划业务单元主键集合
     * @return 规划业务单元
     */
    List<PlanBusinessUnitDTO> selectPlanBusinessUnitByPlanBusinessUnitIds(@Param("planBusinessUnitIds") List<Long> planBusinessUnitIds);

    /**
     * 查询规划业务单元列表
     *
     * @param planBusinessUnit 规划业务单元
     * @return 规划业务单元集合
     */
    List<PlanBusinessUnitDTO> selectPlanBusinessUnitList(@Param("planBusinessUnit") PlanBusinessUnit planBusinessUnit);

    /**
     * 新增规划业务单元
     *
     * @param planBusinessUnit 规划业务单元
     * @return 结果
     */
    int insertPlanBusinessUnit(@Param("planBusinessUnit") PlanBusinessUnit planBusinessUnit);

    /**
     * 修改规划业务单元
     *
     * @param planBusinessUnit 规划业务单元
     * @return 结果
     */
    int updatePlanBusinessUnit(@Param("planBusinessUnit") PlanBusinessUnit planBusinessUnit);

    /**
     * 批量修改规划业务单元
     *
     * @param planBusinessUnitList 规划业务单元
     * @return 结果
     */
    int updatePlanBusinessUnits(@Param("planBusinessUnitList") List<PlanBusinessUnit> planBusinessUnitList);

    /**
     * 逻辑删除规划业务单元
     *
     * @param planBusinessUnit
     * @return 结果
     */
    int logicDeletePlanBusinessUnitByPlanBusinessUnitId(@Param("planBusinessUnit") PlanBusinessUnit planBusinessUnit);

    /**
     * 逻辑批量删除规划业务单元
     *
     * @param planBusinessUnitIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeletePlanBusinessUnitByPlanBusinessUnitIds(@Param("planBusinessUnitIds") List<Long> planBusinessUnitIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除规划业务单元
     *
     * @param planBusinessUnitId 规划业务单元主键
     * @return 结果
     */
    int deletePlanBusinessUnitByPlanBusinessUnitId(@Param("planBusinessUnitId") Long planBusinessUnitId);

    /**
     * 物理批量删除规划业务单元
     *
     * @param planBusinessUnitIds 需要删除的数据主键集合
     * @return 结果
     */
    int deletePlanBusinessUnitByPlanBusinessUnitIds(@Param("planBusinessUnitIds") List<Long> planBusinessUnitIds);

    /**
     * 批量新增规划业务单元
     *
     * @param planBusinessUnits 规划业务单元列表
     * @return 结果
     */
    int batchPlanBusinessUnit(@Param("planBusinessUnits") List<PlanBusinessUnit> planBusinessUnits);

    /**
     * 重复校验
     *
     * @param planBusinessUnit 规划业务单元dto
     * @return list
     */
    List<PlanBusinessUnitDTO> selectPlanBusinessUnitRepeat(@Param("planBusinessUnit") PlanBusinessUnit planBusinessUnit);

    /**
     * 查询规划业务单元编码集合
     *
     * @param prefixCodeRule 编码规则
     * @return List
     */
    List<String> getPlanBusinessUnitCode(@Param("prefixCodeRule") String prefixCodeRule);
}
