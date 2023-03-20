package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMetricsDetail;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDetailDTO;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMetricsDetailMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyMetricsDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * StrategyMetricsDetailService业务层处理
 *
 * @author Graves
 * @since 2023-03-07
 */
@Service
public class StrategyMetricsDetailServiceImpl implements IStrategyMetricsDetailService {
    @Autowired
    private StrategyMetricsDetailMapper strategyMetricsDetailMapper;

    /**
     * 查询战略衡量指标详情表
     *
     * @param strategyMetricsDetailId 战略衡量指标详情表主键
     * @return 战略衡量指标详情表
     */
    @Override
    public StrategyMetricsDetailDTO selectStrategyMetricsDetailByStrategyMetricsDetailId(Long strategyMetricsDetailId) {
        return strategyMetricsDetailMapper.selectStrategyMetricsDetailByStrategyMetricsDetailId(strategyMetricsDetailId);
    }

    /**
     * 查询战略衡量指标详情表列表
     *
     * @param strategyMetricsDetailDTO 战略衡量指标详情表
     * @return 战略衡量指标详情表
     */
    @Override
    public List<StrategyMetricsDetailDTO> selectStrategyMetricsDetailList(StrategyMetricsDetailDTO strategyMetricsDetailDTO) {
        StrategyMetricsDetail strategyMetricsDetail = new StrategyMetricsDetail();
        Map<String, Object> params = strategyMetricsDetailDTO.getParams();
        strategyMetricsDetail.setParams(params);
        BeanUtils.copyProperties(strategyMetricsDetailDTO, strategyMetricsDetail);
        return strategyMetricsDetailMapper.selectStrategyMetricsDetailList(strategyMetricsDetail);
    }

    /**
     * 新增战略衡量指标详情表
     *
     * @param strategyMetricsDetailDTO 战略衡量指标详情表
     * @return 结果
     */
    @Override
    public StrategyMetricsDetailDTO insertStrategyMetricsDetail(StrategyMetricsDetailDTO strategyMetricsDetailDTO) {
        StrategyMetricsDetail strategyMetricsDetail = new StrategyMetricsDetail();
        BeanUtils.copyProperties(strategyMetricsDetailDTO, strategyMetricsDetail);
        strategyMetricsDetail.setCreateBy(SecurityUtils.getUserId());
        strategyMetricsDetail.setCreateTime(DateUtils.getNowDate());
        strategyMetricsDetail.setUpdateTime(DateUtils.getNowDate());
        strategyMetricsDetail.setUpdateBy(SecurityUtils.getUserId());
        strategyMetricsDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        strategyMetricsDetailMapper.insertStrategyMetricsDetail(strategyMetricsDetail);
        strategyMetricsDetailDTO.setStrategyMetricsDetailId(strategyMetricsDetail.getStrategyMetricsDetailId());
        return strategyMetricsDetailDTO;
    }

    /**
     * 修改战略衡量指标详情表
     *
     * @param strategyMetricsDetailDTO 战略衡量指标详情表
     * @return 结果
     */
    @Override
    public int updateStrategyMetricsDetail(StrategyMetricsDetailDTO strategyMetricsDetailDTO) {
        StrategyMetricsDetail strategyMetricsDetail = new StrategyMetricsDetail();
        BeanUtils.copyProperties(strategyMetricsDetailDTO, strategyMetricsDetail);
        strategyMetricsDetail.setUpdateTime(DateUtils.getNowDate());
        strategyMetricsDetail.setUpdateBy(SecurityUtils.getUserId());
        return strategyMetricsDetailMapper.updateStrategyMetricsDetail(strategyMetricsDetail);
    }

    /**
     * 逻辑批量删除战略衡量指标详情表
     *
     * @param strategyMetricsDetailIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMetricsDetailByStrategyMetricsDetailIds(List<Long> strategyMetricsDetailIds) {
        return strategyMetricsDetailMapper.logicDeleteStrategyMetricsDetailByStrategyMetricsDetailIds(strategyMetricsDetailIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除战略衡量指标详情表信息
     *
     * @param strategyMetricsDetailId 战略衡量指标详情表主键
     * @return 结果
     */
    @Override
    public int deleteStrategyMetricsDetailByStrategyMetricsDetailId(Long strategyMetricsDetailId) {
        return strategyMetricsDetailMapper.deleteStrategyMetricsDetailByStrategyMetricsDetailId(strategyMetricsDetailId);
    }

    /**
     * 根据主键ID查询分表
     *
     * @param strategyMetricsId 主键ID
     * @return List
     */
    @Override
    public List<StrategyMetricsDetailDTO> selectStrategyMetricsDetailByStrategyMetricsId(Long strategyMetricsId) {
        return strategyMetricsDetailMapper.selectStrategyMetricsDetailByStrategyMetricsId(strategyMetricsId);
    }

    /**
     * 根据主键ID查询分表
     *
     * @param strategyMetricsIds 主键ID
     * @return List
     */
    @Override
    public List<StrategyMetricsDetailDTO> selectStrategyMetricsDetailByStrategyMetricsIds(List<Long> strategyMetricsIds) {
        return strategyMetricsDetailMapper.selectStrategyMetricsDetailByStrategyMetricsIds(strategyMetricsIds);
    }

    /**
     * 逻辑删除战略衡量指标详情表信息
     *
     * @param strategyMetricsDetailDTO 战略衡量指标详情表
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMetricsDetailByStrategyMetricsDetailId(StrategyMetricsDetailDTO strategyMetricsDetailDTO) {
        StrategyMetricsDetail strategyMetricsDetail = new StrategyMetricsDetail();
        strategyMetricsDetail.setStrategyMetricsDetailId(strategyMetricsDetailDTO.getStrategyMetricsDetailId());
        strategyMetricsDetail.setUpdateTime(DateUtils.getNowDate());
        strategyMetricsDetail.setUpdateBy(SecurityUtils.getUserId());
        return strategyMetricsDetailMapper.logicDeleteStrategyMetricsDetailByStrategyMetricsDetailId(strategyMetricsDetail);
    }

    /**
     * 物理删除战略衡量指标详情表信息
     *
     * @param strategyMetricsDetailDTO 战略衡量指标详情表
     * @return 结果
     */

    @Override
    public int deleteStrategyMetricsDetailByStrategyMetricsDetailId(StrategyMetricsDetailDTO strategyMetricsDetailDTO) {
        StrategyMetricsDetail strategyMetricsDetail = new StrategyMetricsDetail();
        BeanUtils.copyProperties(strategyMetricsDetailDTO, strategyMetricsDetail);
        return strategyMetricsDetailMapper.deleteStrategyMetricsDetailByStrategyMetricsDetailId(strategyMetricsDetail.getStrategyMetricsDetailId());
    }

    /**
     * 物理批量删除战略衡量指标详情表
     *
     * @param strategyMetricsDetailDtos 需要删除的战略衡量指标详情表主键
     * @return 结果
     */

    @Override
    public int deleteStrategyMetricsDetailByStrategyMetricsDetailIds(List<StrategyMetricsDetailDTO> strategyMetricsDetailDtos) {
        List<Long> stringList = new ArrayList<>();
        for (StrategyMetricsDetailDTO strategyMetricsDetailDTO : strategyMetricsDetailDtos) {
            stringList.add(strategyMetricsDetailDTO.getStrategyMetricsDetailId());
        }
        return strategyMetricsDetailMapper.deleteStrategyMetricsDetailByStrategyMetricsDetailIds(stringList);
    }

    /**
     * 批量新增战略衡量指标详情表信息
     *
     * @param strategyMetricsDetailDtos 战略衡量指标详情表对象
     */

    public List<StrategyMetricsDetail> insertStrategyMetricsDetails(List<StrategyMetricsDetailDTO> strategyMetricsDetailDtos) {
        List<StrategyMetricsDetail> strategyMetricsDetailList = new ArrayList<>();

        for (StrategyMetricsDetailDTO strategyMetricsDetailDTO : strategyMetricsDetailDtos) {
            StrategyMetricsDetail strategyMetricsDetail = new StrategyMetricsDetail();
            BeanUtils.copyProperties(strategyMetricsDetailDTO, strategyMetricsDetail);
            strategyMetricsDetail.setCreateBy(SecurityUtils.getUserId());
            strategyMetricsDetail.setCreateTime(DateUtils.getNowDate());
            strategyMetricsDetail.setUpdateTime(DateUtils.getNowDate());
            strategyMetricsDetail.setUpdateBy(SecurityUtils.getUserId());
            strategyMetricsDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyMetricsDetailList.add(strategyMetricsDetail);
        }
        strategyMetricsDetailMapper.batchStrategyMetricsDetail(strategyMetricsDetailList);
        return strategyMetricsDetailList;
    }

    /**
     * 批量修改战略衡量指标详情表信息
     *
     * @param strategyMetricsDetailDtos 战略衡量指标详情表对象
     */

    public int updateStrategyMetricsDetails(List<StrategyMetricsDetailDTO> strategyMetricsDetailDtos) {
        List<StrategyMetricsDetail> strategyMetricsDetailList = new ArrayList<>();

        for (StrategyMetricsDetailDTO strategyMetricsDetailDTO : strategyMetricsDetailDtos) {
            StrategyMetricsDetail strategyMetricsDetail = new StrategyMetricsDetail();
            BeanUtils.copyProperties(strategyMetricsDetailDTO, strategyMetricsDetail);
            strategyMetricsDetail.setCreateBy(SecurityUtils.getUserId());
            strategyMetricsDetail.setCreateTime(DateUtils.getNowDate());
            strategyMetricsDetail.setUpdateTime(DateUtils.getNowDate());
            strategyMetricsDetail.setUpdateBy(SecurityUtils.getUserId());
            strategyMetricsDetailList.add(strategyMetricsDetail);
        }
        return strategyMetricsDetailMapper.updateStrategyMetricsDetails(strategyMetricsDetailList);
    }

}

