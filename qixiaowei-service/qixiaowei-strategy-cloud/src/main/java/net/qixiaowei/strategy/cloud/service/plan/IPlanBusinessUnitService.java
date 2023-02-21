package net.qixiaowei.strategy.cloud.service.plan;

import java.util.List;

import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;


/**
 * PlanBusinessUnitService接口
 *
 * @author wangchen
 * @since 2023-02-17
 */
public interface IPlanBusinessUnitService {
    /**
     * 查询规划业务单元
     *
     * @param planBusinessUnitId 规划业务单元主键
     * @return 规划业务单元
     */
    PlanBusinessUnitDTO selectPlanBusinessUnitByPlanBusinessUnitId(Long planBusinessUnitId);

    /**
     * 查询规划业务单元列表
     *
     * @param planBusinessUnitDTO 规划业务单元
     * @return 规划业务单元集合
     */
    List<PlanBusinessUnitDTO> selectPlanBusinessUnitList(PlanBusinessUnitDTO planBusinessUnitDTO);

    /**
     * 新增规划业务单元
     *
     * @param planBusinessUnitDTO 规划业务单元
     * @return 结果
     */
    PlanBusinessUnitDTO insertPlanBusinessUnit(PlanBusinessUnitDTO planBusinessUnitDTO);

    /**
     * 修改规划业务单元
     *
     * @param planBusinessUnitDTO 规划业务单元
     * @return 结果
     */
    int updatePlanBusinessUnit(PlanBusinessUnitDTO planBusinessUnitDTO);

    /**
     * 批量修改规划业务单元
     *
     * @param planBusinessUnitDtos 规划业务单元
     * @return 结果
     */
    int updatePlanBusinessUnits(List<PlanBusinessUnitDTO> planBusinessUnitDtos);

    /**
     * 批量新增规划业务单元
     *
     * @param planBusinessUnitDtos 规划业务单元
     * @return 结果
     */
    int insertPlanBusinessUnits(List<PlanBusinessUnitDTO> planBusinessUnitDtos);

    /**
     * 逻辑批量删除规划业务单元
     *
     * @param planBusinessUnitIds 需要删除的规划业务单元集合
     * @return 结果
     */
    int logicDeletePlanBusinessUnitByPlanBusinessUnitIds(List<Long> planBusinessUnitIds);

    /**
     * 逻辑删除规划业务单元信息
     *
     * @param planBusinessUnitDTO
     * @return 结果
     */
    int logicDeletePlanBusinessUnitByPlanBusinessUnitId(PlanBusinessUnitDTO planBusinessUnitDTO);

    /**
     * 批量删除规划业务单元
     *
     * @param PlanBusinessUnitDtos
     * @return 结果
     */
    int deletePlanBusinessUnitByPlanBusinessUnitIds(List<PlanBusinessUnitDTO> PlanBusinessUnitDtos);

    /**
     * 逻辑删除规划业务单元信息
     *
     * @param planBusinessUnitDTO
     * @return 结果
     */
    int deletePlanBusinessUnitByPlanBusinessUnitId(PlanBusinessUnitDTO planBusinessUnitDTO);


    /**
     * 删除规划业务单元信息
     *
     * @param planBusinessUnitId 规划业务单元主键
     * @return 结果
     */
    int deletePlanBusinessUnitByPlanBusinessUnitId(Long planBusinessUnitId);

    /**
     * 生成规划业务单元编码
     *
     * @return String
     */
    String generatePlanBusinessUnitCode();
}
