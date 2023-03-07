package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMeasure;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDTO;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMeasureMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * StrategyMeasureService业务层处理
 *
 * @author Graves
 * @since 2023-03-07
 */
@Service
public class StrategyMeasureServiceImpl implements IStrategyMeasureService {
    @Autowired
    private StrategyMeasureMapper strategyMeasureMapper;

    /**
     * 查询战略举措清单表
     *
     * @param strategyMeasureId 战略举措清单表主键
     * @return 战略举措清单表
     */
    @Override
    public StrategyMeasureDTO selectStrategyMeasureByStrategyMeasureId(Long strategyMeasureId) {
        return strategyMeasureMapper.selectStrategyMeasureByStrategyMeasureId(strategyMeasureId);
    }

    /**
     * 查询战略举措清单表列表
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 战略举措清单表
     */
    @Override
    public List<StrategyMeasureDTO> selectStrategyMeasureList(StrategyMeasureDTO strategyMeasureDTO) {
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
        return strategyMeasureMapper.selectStrategyMeasureList(strategyMeasure);
    }

    /**
     * 新增战略举措清单表
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 结果
     */
    @Override
    public StrategyMeasureDTO insertStrategyMeasure(StrategyMeasureDTO strategyMeasureDTO) {
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
        strategyMeasure.setCreateBy(SecurityUtils.getUserId());
        strategyMeasure.setCreateTime(DateUtils.getNowDate());
        strategyMeasure.setUpdateTime(DateUtils.getNowDate());
        strategyMeasure.setUpdateBy(SecurityUtils.getUserId());
        strategyMeasure.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        strategyMeasureMapper.insertStrategyMeasure(strategyMeasure);
        strategyMeasureDTO.setStrategyMeasureId(strategyMeasure.getStrategyMeasureId());
        return strategyMeasureDTO;
    }

    /**
     * 修改战略举措清单表
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 结果
     */
    @Override
    public int updateStrategyMeasure(StrategyMeasureDTO strategyMeasureDTO) {
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
        strategyMeasure.setUpdateTime(DateUtils.getNowDate());
        strategyMeasure.setUpdateBy(SecurityUtils.getUserId());
        return strategyMeasureMapper.updateStrategyMeasure(strategyMeasure);
    }

    /**
     * 逻辑批量删除战略举措清单表
     *
     * @param strategyMeasureIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMeasureByStrategyMeasureIds(List<Long> strategyMeasureIds) {
        return strategyMeasureMapper.logicDeleteStrategyMeasureByStrategyMeasureIds(strategyMeasureIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除战略举措清单表信息
     *
     * @param strategyMeasureId 战略举措清单表主键
     * @return 结果
     */
    @Override
    public int deleteStrategyMeasureByStrategyMeasureId(Long strategyMeasureId) {
        return strategyMeasureMapper.deleteStrategyMeasureByStrategyMeasureId(strategyMeasureId);
    }

    /**
     * 逻辑删除战略举措清单表信息
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMeasureByStrategyMeasureId(StrategyMeasureDTO strategyMeasureDTO) {
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        strategyMeasure.setStrategyMeasureId(strategyMeasureDTO.getStrategyMeasureId());
        strategyMeasure.setUpdateTime(DateUtils.getNowDate());
        strategyMeasure.setUpdateBy(SecurityUtils.getUserId());
        return strategyMeasureMapper.logicDeleteStrategyMeasureByStrategyMeasureId(strategyMeasure);
    }

    /**
     * 物理删除战略举措清单表信息
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 结果
     */

    @Override
    public int deleteStrategyMeasureByStrategyMeasureId(StrategyMeasureDTO strategyMeasureDTO) {
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
        return strategyMeasureMapper.deleteStrategyMeasureByStrategyMeasureId(strategyMeasure.getStrategyMeasureId());
    }

    /**
     * 物理批量删除战略举措清单表
     *
     * @param strategyMeasureDtos 需要删除的战略举措清单表主键
     * @return 结果
     */

    @Override
    public int deleteStrategyMeasureByStrategyMeasureIds(List<StrategyMeasureDTO> strategyMeasureDtos) {
        List<Long> stringList = new ArrayList<>();
        for (StrategyMeasureDTO strategyMeasureDTO : strategyMeasureDtos) {
            stringList.add(strategyMeasureDTO.getStrategyMeasureId());
        }
        return strategyMeasureMapper.deleteStrategyMeasureByStrategyMeasureIds(stringList);
    }

    /**
     * 批量新增战略举措清单表信息
     *
     * @param strategyMeasureDtos 战略举措清单表对象
     */

    public int insertStrategyMeasures(List<StrategyMeasureDTO> strategyMeasureDtos) {
        List<StrategyMeasure> strategyMeasureList = new ArrayList<>();

        for (StrategyMeasureDTO strategyMeasureDTO : strategyMeasureDtos) {
            StrategyMeasure strategyMeasure = new StrategyMeasure();
            BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
            strategyMeasure.setCreateBy(SecurityUtils.getUserId());
            strategyMeasure.setCreateTime(DateUtils.getNowDate());
            strategyMeasure.setUpdateTime(DateUtils.getNowDate());
            strategyMeasure.setUpdateBy(SecurityUtils.getUserId());
            strategyMeasure.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyMeasureList.add(strategyMeasure);
        }
        return strategyMeasureMapper.batchStrategyMeasure(strategyMeasureList);
    }

    /**
     * 批量修改战略举措清单表信息
     *
     * @param strategyMeasureDtos 战略举措清单表对象
     */

    public int updateStrategyMeasures(List<StrategyMeasureDTO> strategyMeasureDtos) {
        List<StrategyMeasure> strategyMeasureList = new ArrayList<>();

        for (StrategyMeasureDTO strategyMeasureDTO : strategyMeasureDtos) {
            StrategyMeasure strategyMeasure = new StrategyMeasure();
            BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
            strategyMeasure.setCreateBy(SecurityUtils.getUserId());
            strategyMeasure.setCreateTime(DateUtils.getNowDate());
            strategyMeasure.setUpdateTime(DateUtils.getNowDate());
            strategyMeasure.setUpdateBy(SecurityUtils.getUserId());
            strategyMeasureList.add(strategyMeasure);
        }
        return strategyMeasureMapper.updateStrategyMeasures(strategyMeasureList);
    }

}

