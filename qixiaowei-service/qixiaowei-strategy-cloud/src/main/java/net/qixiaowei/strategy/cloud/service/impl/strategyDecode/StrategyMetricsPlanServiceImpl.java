package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMetricsPlan;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsPlanDTO;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMetricsPlanMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyMetricsPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * StrategyMetricsPlanService业务层处理
 *
 * @author Graves
 * @since 2023-03-07
 */
@Service
public class StrategyMetricsPlanServiceImpl implements IStrategyMetricsPlanService {
    @Autowired
    private StrategyMetricsPlanMapper strategyMetricsPlanMapper;

    /**
     * 查询战略衡量指标规划表
     *
     * @param strategyMetricsPlanId 战略衡量指标规划表主键
     * @return 战略衡量指标规划表
     */
    @Override
    public StrategyMetricsPlanDTO selectStrategyMetricsPlanByStrategyMetricsPlanId(Long strategyMetricsPlanId) {
        return strategyMetricsPlanMapper.selectStrategyMetricsPlanByStrategyMetricsPlanId(strategyMetricsPlanId);
    }

    /**
     * 查询战略衡量指标规划表列表
     *
     * @param strategyMetricsPlanDTO 战略衡量指标规划表
     * @return 战略衡量指标规划表
     */
    @Override
    public List<StrategyMetricsPlanDTO> selectStrategyMetricsPlanList(StrategyMetricsPlanDTO strategyMetricsPlanDTO) {
        StrategyMetricsPlan strategyMetricsPlan = new StrategyMetricsPlan();
        BeanUtils.copyProperties(strategyMetricsPlanDTO, strategyMetricsPlan);
        return strategyMetricsPlanMapper.selectStrategyMetricsPlanList(strategyMetricsPlan);
    }

    /**
     * 新增战略衡量指标规划表
     *
     * @param strategyMetricsPlanDTO 战略衡量指标规划表
     * @return 结果
     */
    @Override
    public StrategyMetricsPlanDTO insertStrategyMetricsPlan(StrategyMetricsPlanDTO strategyMetricsPlanDTO) {
        StrategyMetricsPlan strategyMetricsPlan = new StrategyMetricsPlan();
        BeanUtils.copyProperties(strategyMetricsPlanDTO, strategyMetricsPlan);
        strategyMetricsPlan.setCreateBy(SecurityUtils.getUserId());
        strategyMetricsPlan.setCreateTime(DateUtils.getNowDate());
        strategyMetricsPlan.setUpdateTime(DateUtils.getNowDate());
        strategyMetricsPlan.setUpdateBy(SecurityUtils.getUserId());
        strategyMetricsPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        strategyMetricsPlanMapper.insertStrategyMetricsPlan(strategyMetricsPlan);
        strategyMetricsPlanDTO.setStrategyMetricsPlanId(strategyMetricsPlan.getStrategyMetricsPlanId());
        return strategyMetricsPlanDTO;
    }

    /**
     * 修改战略衡量指标规划表
     *
     * @param strategyMetricsPlanDTO 战略衡量指标规划表
     * @return 结果
     */
    @Override
    public int updateStrategyMetricsPlan(StrategyMetricsPlanDTO strategyMetricsPlanDTO) {
        StrategyMetricsPlan strategyMetricsPlan = new StrategyMetricsPlan();
        BeanUtils.copyProperties(strategyMetricsPlanDTO, strategyMetricsPlan);
        strategyMetricsPlan.setUpdateTime(DateUtils.getNowDate());
        strategyMetricsPlan.setUpdateBy(SecurityUtils.getUserId());
        return strategyMetricsPlanMapper.updateStrategyMetricsPlan(strategyMetricsPlan);
    }

    /**
     * 逻辑批量删除战略衡量指标规划表
     *
     * @param strategyMetricsPlanIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMetricsPlanByStrategyMetricsPlanIds(List<Long> strategyMetricsPlanIds) {
        return strategyMetricsPlanMapper.logicDeleteStrategyMetricsPlanByStrategyMetricsPlanIds(strategyMetricsPlanIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除战略衡量指标规划表信息
     *
     * @param strategyMetricsPlanId 战略衡量指标规划表主键
     * @return 结果
     */
    @Override
    public int deleteStrategyMetricsPlanByStrategyMetricsPlanId(Long strategyMetricsPlanId) {
        return strategyMetricsPlanMapper.deleteStrategyMetricsPlanByStrategyMetricsPlanId(strategyMetricsPlanId);
    }

    @Override
    public List<StrategyMetricsPlanDTO> selectStrategyMetricsPlanByStrategyMetricsDetailIds(List<Long> editStrategyMetricsDetailIds) {
        return strategyMetricsPlanMapper.selectStrategyMetricsPlanByStrategyMetricsDetailIds(editStrategyMetricsDetailIds);
    }

    /**
     * 逻辑删除战略衡量指标规划表信息
     *
     * @param strategyMetricsPlanDTO 战略衡量指标规划表
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMetricsPlanByStrategyMetricsPlanId(StrategyMetricsPlanDTO strategyMetricsPlanDTO) {
        StrategyMetricsPlan strategyMetricsPlan = new StrategyMetricsPlan();
        strategyMetricsPlan.setStrategyMetricsPlanId(strategyMetricsPlanDTO.getStrategyMetricsPlanId());
        strategyMetricsPlan.setUpdateTime(DateUtils.getNowDate());
        strategyMetricsPlan.setUpdateBy(SecurityUtils.getUserId());
        return strategyMetricsPlanMapper.logicDeleteStrategyMetricsPlanByStrategyMetricsPlanId(strategyMetricsPlan);
    }

    /**
     * 物理删除战略衡量指标规划表信息
     *
     * @param strategyMetricsPlanDTO 战略衡量指标规划表
     * @return 结果
     */

    @Override
    public int deleteStrategyMetricsPlanByStrategyMetricsPlanId(StrategyMetricsPlanDTO strategyMetricsPlanDTO) {
        StrategyMetricsPlan strategyMetricsPlan = new StrategyMetricsPlan();
        BeanUtils.copyProperties(strategyMetricsPlanDTO, strategyMetricsPlan);
        return strategyMetricsPlanMapper.deleteStrategyMetricsPlanByStrategyMetricsPlanId(strategyMetricsPlan.getStrategyMetricsPlanId());
    }

    /**
     * 物理批量删除战略衡量指标规划表
     *
     * @param strategyMetricsPlanDtos 需要删除的战略衡量指标规划表主键
     * @return 结果
     */

    @Override
    public int deleteStrategyMetricsPlanByStrategyMetricsPlanIds(List<StrategyMetricsPlanDTO> strategyMetricsPlanDtos) {
        List<Long> stringList = new ArrayList<>();
        for (StrategyMetricsPlanDTO strategyMetricsPlanDTO : strategyMetricsPlanDtos) {
            stringList.add(strategyMetricsPlanDTO.getStrategyMetricsPlanId());
        }
        return strategyMetricsPlanMapper.deleteStrategyMetricsPlanByStrategyMetricsPlanIds(stringList);
    }

    /**
     * 批量新增战略衡量指标规划表信息
     *
     * @param strategyMetricsPlanDtos 战略衡量指标规划表对象
     */

    public int insertStrategyMetricsPlans(List<StrategyMetricsPlanDTO> strategyMetricsPlanDtos) {
        List<StrategyMetricsPlan> strategyMetricsPlanList = new ArrayList<>();

        for (StrategyMetricsPlanDTO strategyMetricsPlanDTO : strategyMetricsPlanDtos) {
            StrategyMetricsPlan strategyMetricsPlan = new StrategyMetricsPlan();
            BeanUtils.copyProperties(strategyMetricsPlanDTO, strategyMetricsPlan);
            strategyMetricsPlan.setCreateBy(SecurityUtils.getUserId());
            strategyMetricsPlan.setCreateTime(DateUtils.getNowDate());
            strategyMetricsPlan.setUpdateTime(DateUtils.getNowDate());
            strategyMetricsPlan.setUpdateBy(SecurityUtils.getUserId());
            strategyMetricsPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyMetricsPlanList.add(strategyMetricsPlan);
        }
        return strategyMetricsPlanMapper.batchStrategyMetricsPlan(strategyMetricsPlanList);
    }

    /**
     * 批量修改战略衡量指标规划表信息
     *
     * @param strategyMetricsPlanDtos 战略衡量指标规划表对象
     */

    public int updateStrategyMetricsPlans(List<StrategyMetricsPlanDTO> strategyMetricsPlanDtos) {
        List<StrategyMetricsPlan> strategyMetricsPlanList = new ArrayList<>();

        for (StrategyMetricsPlanDTO strategyMetricsPlanDTO : strategyMetricsPlanDtos) {
            StrategyMetricsPlan strategyMetricsPlan = new StrategyMetricsPlan();
            BeanUtils.copyProperties(strategyMetricsPlanDTO, strategyMetricsPlan);
            strategyMetricsPlan.setCreateBy(SecurityUtils.getUserId());
            strategyMetricsPlan.setCreateTime(DateUtils.getNowDate());
            strategyMetricsPlan.setUpdateTime(DateUtils.getNowDate());
            strategyMetricsPlan.setUpdateBy(SecurityUtils.getUserId());
            strategyMetricsPlanList.add(strategyMetricsPlan);
        }
        return strategyMetricsPlanMapper.updateStrategyMetricsPlans(strategyMetricsPlanList);
    }

}

