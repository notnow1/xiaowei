package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMeasureDetail;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDetailDTO;
import net.qixiaowei.strategy.cloud.api.vo.strategyDecode.StrategyMeasureDetailVO;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMeasureDetailMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyMeasureDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * StrategyMeasureDetailService业务层处理
 *
 * @author Graves
 * @since 2023-03-07
 */
@Service
public class StrategyMeasureDetailServiceImpl implements IStrategyMeasureDetailService {
    @Autowired
    private StrategyMeasureDetailMapper strategyMeasureDetailMapper;

    /**
     * 查询战略举措清单详情表
     *
     * @param strategyMeasureDetailId 战略举措清单详情表主键
     * @return 战略举措清单详情表
     */
    @Override
    public StrategyMeasureDetailDTO selectStrategyMeasureDetailByStrategyMeasureDetailId(Long strategyMeasureDetailId) {
        return strategyMeasureDetailMapper.selectStrategyMeasureDetailByStrategyMeasureDetailId(strategyMeasureDetailId);
    }

    /**
     * 查询战略举措清单详情表列表
     *
     * @param strategyMeasureDetailDTO 战略举措清单详情表
     * @return 战略举措清单详情表
     */
    @Override
    public List<StrategyMeasureDetailDTO> selectStrategyMeasureDetailList(StrategyMeasureDetailDTO strategyMeasureDetailDTO) {
        StrategyMeasureDetail strategyMeasureDetail = new StrategyMeasureDetail();
        BeanUtils.copyProperties(strategyMeasureDetailDTO, strategyMeasureDetail);
        return strategyMeasureDetailMapper.selectStrategyMeasureDetailList(strategyMeasureDetail);
    }

    /**
     * 新增战略举措清单详情表
     *
     * @param strategyMeasureDetailDTO 战略举措清单详情表
     * @return 结果
     */
    @Override
    public StrategyMeasureDetailDTO insertStrategyMeasureDetail(StrategyMeasureDetailDTO strategyMeasureDetailDTO) {
        StrategyMeasureDetail strategyMeasureDetail = new StrategyMeasureDetail();
        BeanUtils.copyProperties(strategyMeasureDetailDTO, strategyMeasureDetail);
        strategyMeasureDetail.setCreateBy(SecurityUtils.getUserId());
        strategyMeasureDetail.setCreateTime(DateUtils.getNowDate());
        strategyMeasureDetail.setUpdateTime(DateUtils.getNowDate());
        strategyMeasureDetail.setUpdateBy(SecurityUtils.getUserId());
        strategyMeasureDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        strategyMeasureDetailMapper.insertStrategyMeasureDetail(strategyMeasureDetail);
        strategyMeasureDetailDTO.setStrategyMeasureDetailId(strategyMeasureDetail.getStrategyMeasureDetailId());
        return strategyMeasureDetailDTO;
    }

    /**
     * 修改战略举措清单详情表
     *
     * @param strategyMeasureDetailDTO 战略举措清单详情表
     * @return 结果
     */
    @Override
    public int updateStrategyMeasureDetail(StrategyMeasureDetailDTO strategyMeasureDetailDTO) {
        StrategyMeasureDetail strategyMeasureDetail = new StrategyMeasureDetail();
        BeanUtils.copyProperties(strategyMeasureDetailDTO, strategyMeasureDetail);
        strategyMeasureDetail.setUpdateTime(DateUtils.getNowDate());
        strategyMeasureDetail.setUpdateBy(SecurityUtils.getUserId());
        return strategyMeasureDetailMapper.updateStrategyMeasureDetail(strategyMeasureDetail);
    }

    /**
     * 逻辑批量删除战略举措清单详情表
     *
     * @param strategyMeasureDetailIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMeasureDetailByStrategyMeasureDetailIds(List<Long> strategyMeasureDetailIds) {
        return strategyMeasureDetailMapper.logicDeleteStrategyMeasureDetailByStrategyMeasureDetailIds(strategyMeasureDetailIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除战略举措清单详情表信息
     *
     * @param strategyMeasureDetailId 战略举措清单详情表主键
     * @return 结果
     */
    @Override
    public int deleteStrategyMeasureDetailByStrategyMeasureDetailId(Long strategyMeasureDetailId) {
        return strategyMeasureDetailMapper.deleteStrategyMeasureDetailByStrategyMeasureDetailId(strategyMeasureDetailId);
    }

    /**
     * 根据战略举措id查询战略举措清单详情表
     *
     * @param strategyMeasureId 战略举措id
     * @return List
     */
    @Override
    public List<StrategyMeasureDetailVO> selectStrategyMeasureDetailVOByStrategyMeasureId(Long strategyMeasureId) {
        return strategyMeasureDetailMapper.selectStrategyMeasureDetailVOByStrategyMeasureId(strategyMeasureId);
    }

    /**
     * 逻辑删除战略举措清单详情表信息
     *
     * @param strategyMeasureDetailDTO 战略举措清单详情表
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMeasureDetailByStrategyMeasureDetailId(StrategyMeasureDetailDTO strategyMeasureDetailDTO) {
        StrategyMeasureDetail strategyMeasureDetail = new StrategyMeasureDetail();
        strategyMeasureDetail.setStrategyMeasureDetailId(strategyMeasureDetailDTO.getStrategyMeasureDetailId());
        strategyMeasureDetail.setUpdateTime(DateUtils.getNowDate());
        strategyMeasureDetail.setUpdateBy(SecurityUtils.getUserId());
        return strategyMeasureDetailMapper.logicDeleteStrategyMeasureDetailByStrategyMeasureDetailId(strategyMeasureDetail);
    }

    /**
     * 物理删除战略举措清单详情表信息
     *
     * @param strategyMeasureDetailDTO 战略举措清单详情表
     * @return 结果
     */

    @Override
    public int deleteStrategyMeasureDetailByStrategyMeasureDetailId(StrategyMeasureDetailDTO strategyMeasureDetailDTO) {
        StrategyMeasureDetail strategyMeasureDetail = new StrategyMeasureDetail();
        BeanUtils.copyProperties(strategyMeasureDetailDTO, strategyMeasureDetail);
        return strategyMeasureDetailMapper.deleteStrategyMeasureDetailByStrategyMeasureDetailId(strategyMeasureDetail.getStrategyMeasureDetailId());
    }

    /**
     * 物理批量删除战略举措清单详情表
     *
     * @param strategyMeasureDetailDtos 需要删除的战略举措清单详情表主键
     * @return 结果
     */

    @Override
    public int deleteStrategyMeasureDetailByStrategyMeasureDetailIds(List<StrategyMeasureDetailDTO> strategyMeasureDetailDtos) {
        List<Long> stringList = new ArrayList<>();
        for (StrategyMeasureDetailDTO strategyMeasureDetailDTO : strategyMeasureDetailDtos) {
            stringList.add(strategyMeasureDetailDTO.getStrategyMeasureDetailId());
        }
        return strategyMeasureDetailMapper.deleteStrategyMeasureDetailByStrategyMeasureDetailIds(stringList);
    }

    /**
     * 批量新增战略举措清单详情表信息
     *
     * @param strategyMeasureDetailDtos 战略举措清单详情表对象
     */

    public int insertStrategyMeasureDetails(List<StrategyMeasureDetailDTO> strategyMeasureDetailDtos) {
        List<StrategyMeasureDetail> strategyMeasureDetailList = new ArrayList<>();

        for (StrategyMeasureDetailDTO strategyMeasureDetailDTO : strategyMeasureDetailDtos) {
            StrategyMeasureDetail strategyMeasureDetail = new StrategyMeasureDetail();
            BeanUtils.copyProperties(strategyMeasureDetailDTO, strategyMeasureDetail);
            strategyMeasureDetail.setCreateBy(SecurityUtils.getUserId());
            strategyMeasureDetail.setCreateTime(DateUtils.getNowDate());
            strategyMeasureDetail.setUpdateTime(DateUtils.getNowDate());
            strategyMeasureDetail.setUpdateBy(SecurityUtils.getUserId());
            strategyMeasureDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyMeasureDetailList.add(strategyMeasureDetail);
        }
        return strategyMeasureDetailMapper.batchStrategyMeasureDetail(strategyMeasureDetailList);
    }

    /**
     * 批量修改战略举措清单详情表信息
     *
     * @param strategyMeasureDetailDtos 战略举措清单详情表对象
     */

    public int updateStrategyMeasureDetails(List<StrategyMeasureDetailDTO> strategyMeasureDetailDtos) {
        List<StrategyMeasureDetail> strategyMeasureDetailList = new ArrayList<>();

        for (StrategyMeasureDetailDTO strategyMeasureDetailDTO : strategyMeasureDetailDtos) {
            StrategyMeasureDetail strategyMeasureDetail = new StrategyMeasureDetail();
            BeanUtils.copyProperties(strategyMeasureDetailDTO, strategyMeasureDetail);
            strategyMeasureDetail.setCreateBy(SecurityUtils.getUserId());
            strategyMeasureDetail.setCreateTime(DateUtils.getNowDate());
            strategyMeasureDetail.setUpdateTime(DateUtils.getNowDate());
            strategyMeasureDetail.setUpdateBy(SecurityUtils.getUserId());
            strategyMeasureDetailList.add(strategyMeasureDetail);
        }
        return strategyMeasureDetailMapper.updateStrategyMeasureDetails(strategyMeasureDetailList);
    }

}

