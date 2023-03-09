package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMetrics;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDTO;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMetricsMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * StrategyMetricsService业务层处理
 *
 * @author Graves
 * @since 2023-03-07
 */
@Service
public class StrategyMetricsServiceImpl implements IStrategyMetricsService {
    @Autowired
    private StrategyMetricsMapper strategyMetricsMapper;

    /**
     * 查询战略衡量指标表
     *
     * @param strategyMetricsId 战略衡量指标表主键
     * @return 战略衡量指标表
     */
    @Override
    public StrategyMetricsDTO selectStrategyMetricsByStrategyMetricsId(Long strategyMetricsId) {
        return strategyMetricsMapper.selectStrategyMetricsByStrategyMetricsId(strategyMetricsId);
    }

    /**
     * 查询战略衡量指标表列表
     *
     * @param strategyMetricsDTO 战略衡量指标表
     * @return 战略衡量指标表
     */
    @Override
    public List<StrategyMetricsDTO> selectStrategyMetricsList(StrategyMetricsDTO strategyMetricsDTO) {
        StrategyMetrics strategyMetrics = new StrategyMetrics();
        BeanUtils.copyProperties(strategyMetricsDTO, strategyMetrics);
        return strategyMetricsMapper.selectStrategyMetricsList(strategyMetrics);
    }

    /**
     * 新增战略衡量指标表
     *
     * @param strategyMetricsDTO 战略衡量指标表
     * @return 结果
     */
    @Override
    public Long insertStrategyMetrics(StrategyMetricsDTO strategyMetricsDTO) {
        StrategyMetrics strategyMetrics = new StrategyMetrics();
        BeanUtils.copyProperties(strategyMetricsDTO, strategyMetrics);
        strategyMetrics.setCreateBy(SecurityUtils.getUserId());
        strategyMetrics.setCreateTime(DateUtils.getNowDate());
        strategyMetrics.setUpdateTime(DateUtils.getNowDate());
        strategyMetrics.setUpdateBy(SecurityUtils.getUserId());
        strategyMetrics.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        strategyMetricsMapper.insertStrategyMetrics(strategyMetrics);
        return strategyMetrics.getStrategyMetricsId();
    }

    /**
     * 修改战略衡量指标表
     *
     * @param strategyMetricsDTO 战略衡量指标表
     * @return 结果
     */
    @Override
    public int updateStrategyMetrics(StrategyMetricsDTO strategyMetricsDTO) {
        StrategyMetrics strategyMetrics = new StrategyMetrics();
        BeanUtils.copyProperties(strategyMetricsDTO, strategyMetrics);
        strategyMetrics.setUpdateTime(DateUtils.getNowDate());
        strategyMetrics.setUpdateBy(SecurityUtils.getUserId());
        return strategyMetricsMapper.updateStrategyMetrics(strategyMetrics);
    }

    /**
     * 逻辑批量删除战略衡量指标表
     *
     * @param strategyMetricsIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMetricsByStrategyMetricsIds(List<Long> strategyMetricsIds) {
        return strategyMetricsMapper.logicDeleteStrategyMetricsByStrategyMetricsIds(strategyMetricsIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除战略衡量指标表信息
     *
     * @param strategyMetricsId 战略衡量指标表主键
     * @return 结果
     */
    @Override
    public int deleteStrategyMetricsByStrategyMetricsId(Long strategyMetricsId) {
        return strategyMetricsMapper.deleteStrategyMetricsByStrategyMetricsId(strategyMetricsId);
    }

    /**
     * 根据清单ID查询战略衡量指标
     *
     * @param strategyMeasureId 清单ID
     * @return 战略衡量指标
     */
    @Override
    public StrategyMetricsDTO selectStrategyMetricsByStrategyMeasureId(Long strategyMeasureId) {
        return strategyMetricsMapper.selectStrategyMetricsByStrategyMeasureId(strategyMeasureId);
    }

    /**
     * 逻辑删除战略衡量指标表信息
     *
     * @param strategyMetricsDTO 战略衡量指标表
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMetricsByStrategyMetricsId(StrategyMetricsDTO strategyMetricsDTO) {
        StrategyMetrics strategyMetrics = new StrategyMetrics();
        strategyMetrics.setStrategyMetricsId(strategyMetricsDTO.getStrategyMetricsId());
        strategyMetrics.setUpdateTime(DateUtils.getNowDate());
        strategyMetrics.setUpdateBy(SecurityUtils.getUserId());
        return strategyMetricsMapper.logicDeleteStrategyMetricsByStrategyMetricsId(strategyMetrics);
    }

    /**
     * 物理删除战略衡量指标表信息
     *
     * @param strategyMetricsDTO 战略衡量指标表
     * @return 结果
     */

    @Override
    public int deleteStrategyMetricsByStrategyMetricsId(StrategyMetricsDTO strategyMetricsDTO) {
        StrategyMetrics strategyMetrics = new StrategyMetrics();
        BeanUtils.copyProperties(strategyMetricsDTO, strategyMetrics);
        return strategyMetricsMapper.deleteStrategyMetricsByStrategyMetricsId(strategyMetrics.getStrategyMetricsId());
    }

    /**
     * 物理批量删除战略衡量指标表
     *
     * @param strategyMetricsDtos 需要删除的战略衡量指标表主键
     * @return 结果
     */

    @Override
    public int deleteStrategyMetricsByStrategyMetricsIds(List<StrategyMetricsDTO> strategyMetricsDtos) {
        List<Long> stringList = new ArrayList<>();
        for (StrategyMetricsDTO strategyMetricsDTO : strategyMetricsDtos) {
            stringList.add(strategyMetricsDTO.getStrategyMetricsId());
        }
        return strategyMetricsMapper.deleteStrategyMetricsByStrategyMetricsIds(stringList);
    }

    /**
     * 批量新增战略衡量指标表信息
     *
     * @param strategyMetricsDtos 战略衡量指标表对象
     */

    public int insertStrategyMetricss(List<StrategyMetricsDTO> strategyMetricsDtos) {
        List<StrategyMetrics> strategyMetricsList = new ArrayList<>();

        for (StrategyMetricsDTO strategyMetricsDTO : strategyMetricsDtos) {
            StrategyMetrics strategyMetrics = new StrategyMetrics();
            BeanUtils.copyProperties(strategyMetricsDTO, strategyMetrics);
            strategyMetrics.setCreateBy(SecurityUtils.getUserId());
            strategyMetrics.setCreateTime(DateUtils.getNowDate());
            strategyMetrics.setUpdateTime(DateUtils.getNowDate());
            strategyMetrics.setUpdateBy(SecurityUtils.getUserId());
            strategyMetrics.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyMetricsList.add(strategyMetrics);
        }
        return strategyMetricsMapper.batchStrategyMetrics(strategyMetricsList);
    }

    /**
     * 批量修改战略衡量指标表信息
     *
     * @param strategyMetricsDtos 战略衡量指标表对象
     */

    public int updateStrategyMetricss(List<StrategyMetricsDTO> strategyMetricsDtos) {
        List<StrategyMetrics> strategyMetricsList = new ArrayList<>();

        for (StrategyMetricsDTO strategyMetricsDTO : strategyMetricsDtos) {
            StrategyMetrics strategyMetrics = new StrategyMetrics();
            BeanUtils.copyProperties(strategyMetricsDTO, strategyMetrics);
            strategyMetrics.setCreateBy(SecurityUtils.getUserId());
            strategyMetrics.setCreateTime(DateUtils.getNowDate());
            strategyMetrics.setUpdateTime(DateUtils.getNowDate());
            strategyMetrics.setUpdateBy(SecurityUtils.getUserId());
            strategyMetricsList.add(strategyMetrics);
        }
        return strategyMetricsMapper.updateStrategyMetricss(strategyMetricsList);
    }
}

