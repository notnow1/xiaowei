package net.qixiaowei.strategy.cloud.service.impl.plan;

import java.util.List;

import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.plan.PlanBusinessUnit;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.mapper.plan.PlanBusinessUnitMapper;
import net.qixiaowei.strategy.cloud.service.plan.IPlanBusinessUnitService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


/**
 * PlanBusinessUnitService业务层处理
 *
 * @author wangchen
 * @since 2023-02-17
 */
@Service
public class PlanBusinessUnitServiceImpl implements IPlanBusinessUnitService {
    @Autowired
    private PlanBusinessUnitMapper planBusinessUnitMapper;

    /**
     * 查询规划业务单元
     *
     * @param planBusinessUnitId 规划业务单元主键
     * @return 规划业务单元
     */
    @Override
    public PlanBusinessUnitDTO selectPlanBusinessUnitByPlanBusinessUnitId(Long planBusinessUnitId) {
        return planBusinessUnitMapper.selectPlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
    }

    /**
     * 查询规划业务单元列表
     *
     * @param planBusinessUnitDTO 规划业务单元
     * @return 规划业务单元
     */
    @Override
    public List<PlanBusinessUnitDTO> selectPlanBusinessUnitList(PlanBusinessUnitDTO planBusinessUnitDTO) {
        PlanBusinessUnit planBusinessUnit = new PlanBusinessUnit();
        BeanUtils.copyProperties(planBusinessUnitDTO, planBusinessUnit);
        return planBusinessUnitMapper.selectPlanBusinessUnitList(planBusinessUnit);
    }

    /**
     * 新增规划业务单元
     *
     * @param planBusinessUnitDTO 规划业务单元
     * @return 结果
     */
    @Override
    public PlanBusinessUnitDTO insertPlanBusinessUnit(PlanBusinessUnitDTO planBusinessUnitDTO) {
        PlanBusinessUnit planBusinessUnit = new PlanBusinessUnit();
        BeanUtils.copyProperties(planBusinessUnitDTO, planBusinessUnit);
        planBusinessUnit.setCreateBy(SecurityUtils.getUserId());
        planBusinessUnit.setCreateTime(DateUtils.getNowDate());
        planBusinessUnit.setUpdateTime(DateUtils.getNowDate());
        planBusinessUnit.setUpdateBy(SecurityUtils.getUserId());
        planBusinessUnit.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        planBusinessUnitMapper.insertPlanBusinessUnit(planBusinessUnit);
        planBusinessUnitDTO.setPlanBusinessUnitId(planBusinessUnit.getPlanBusinessUnitId());
        return planBusinessUnitDTO;
    }

    /**
     * 修改规划业务单元
     *
     * @param planBusinessUnitDTO 规划业务单元
     * @return 结果
     */
    @Override
    public int updatePlanBusinessUnit(PlanBusinessUnitDTO planBusinessUnitDTO) {
        PlanBusinessUnit planBusinessUnit = new PlanBusinessUnit();
        BeanUtils.copyProperties(planBusinessUnitDTO, planBusinessUnit);
        planBusinessUnit.setUpdateTime(DateUtils.getNowDate());
        planBusinessUnit.setUpdateBy(SecurityUtils.getUserId());
        return planBusinessUnitMapper.updatePlanBusinessUnit(planBusinessUnit);
    }

    /**
     * 逻辑批量删除规划业务单元
     *
     * @param planBusinessUnitIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeletePlanBusinessUnitByPlanBusinessUnitIds(List<Long> planBusinessUnitIds) {
        return planBusinessUnitMapper.logicDeletePlanBusinessUnitByPlanBusinessUnitIds(planBusinessUnitIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除规划业务单元信息
     *
     * @param planBusinessUnitId 规划业务单元主键
     * @return 结果
     */
    @Override
    public int deletePlanBusinessUnitByPlanBusinessUnitId(Long planBusinessUnitId) {
        return planBusinessUnitMapper.deletePlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
    }

    /**
     * 逻辑删除规划业务单元信息
     *
     * @param planBusinessUnitDTO 规划业务单元
     * @return 结果
     */
    @Override
    public int logicDeletePlanBusinessUnitByPlanBusinessUnitId(PlanBusinessUnitDTO planBusinessUnitDTO) {
        PlanBusinessUnit planBusinessUnit = new PlanBusinessUnit();
        planBusinessUnit.setPlanBusinessUnitId(planBusinessUnitDTO.getPlanBusinessUnitId());
        planBusinessUnit.setUpdateTime(DateUtils.getNowDate());
        planBusinessUnit.setUpdateBy(SecurityUtils.getUserId());
        return planBusinessUnitMapper.logicDeletePlanBusinessUnitByPlanBusinessUnitId(planBusinessUnit);
    }

    /**
     * 物理删除规划业务单元信息
     *
     * @param planBusinessUnitDTO 规划业务单元
     * @return 结果
     */

    @Override
    public int deletePlanBusinessUnitByPlanBusinessUnitId(PlanBusinessUnitDTO planBusinessUnitDTO) {
        PlanBusinessUnit planBusinessUnit = new PlanBusinessUnit();
        BeanUtils.copyProperties(planBusinessUnitDTO, planBusinessUnit);
        return planBusinessUnitMapper.deletePlanBusinessUnitByPlanBusinessUnitId(planBusinessUnit.getPlanBusinessUnitId());
    }

    /**
     * 物理批量删除规划业务单元
     *
     * @param planBusinessUnitDtos 需要删除的规划业务单元主键
     * @return 结果
     */

    @Override
    public int deletePlanBusinessUnitByPlanBusinessUnitIds(List<PlanBusinessUnitDTO> planBusinessUnitDtos) {
        List<Long> stringList = new ArrayList();
        for (PlanBusinessUnitDTO planBusinessUnitDTO : planBusinessUnitDtos) {
            stringList.add(planBusinessUnitDTO.getPlanBusinessUnitId());
        }
        return planBusinessUnitMapper.deletePlanBusinessUnitByPlanBusinessUnitIds(stringList);
    }

    /**
     * 批量新增规划业务单元信息
     *
     * @param planBusinessUnitDtos 规划业务单元对象
     */

    @Override
    public int insertPlanBusinessUnits(List<PlanBusinessUnitDTO> planBusinessUnitDtos) {
        List<PlanBusinessUnit> planBusinessUnitList = new ArrayList();

        for (PlanBusinessUnitDTO planBusinessUnitDTO : planBusinessUnitDtos) {
            PlanBusinessUnit planBusinessUnit = new PlanBusinessUnit();
            BeanUtils.copyProperties(planBusinessUnitDTO, planBusinessUnit);
            planBusinessUnit.setCreateBy(SecurityUtils.getUserId());
            planBusinessUnit.setCreateTime(DateUtils.getNowDate());
            planBusinessUnit.setUpdateTime(DateUtils.getNowDate());
            planBusinessUnit.setUpdateBy(SecurityUtils.getUserId());
            planBusinessUnit.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            planBusinessUnitList.add(planBusinessUnit);
        }
        return planBusinessUnitMapper.batchPlanBusinessUnit(planBusinessUnitList);
    }

    /**
     * 批量修改规划业务单元信息
     *
     * @param planBusinessUnitDtos 规划业务单元对象
     */

    @Override
    public int updatePlanBusinessUnits(List<PlanBusinessUnitDTO> planBusinessUnitDtos) {
        List<PlanBusinessUnit> planBusinessUnitList = new ArrayList();

        for (PlanBusinessUnitDTO planBusinessUnitDTO : planBusinessUnitDtos) {
            PlanBusinessUnit planBusinessUnit = new PlanBusinessUnit();
            BeanUtils.copyProperties(planBusinessUnitDTO, planBusinessUnit);
            planBusinessUnit.setCreateBy(SecurityUtils.getUserId());
            planBusinessUnit.setCreateTime(DateUtils.getNowDate());
            planBusinessUnit.setUpdateTime(DateUtils.getNowDate());
            planBusinessUnit.setUpdateBy(SecurityUtils.getUserId());
            planBusinessUnitList.add(planBusinessUnit);
        }
        return planBusinessUnitMapper.updatePlanBusinessUnits(planBusinessUnitList);
    }
}

