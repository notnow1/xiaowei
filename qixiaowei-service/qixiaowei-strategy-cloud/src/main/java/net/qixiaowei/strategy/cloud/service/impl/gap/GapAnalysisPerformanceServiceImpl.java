package net.qixiaowei.strategy.cloud.service.impl.gap;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.gap.GapAnalysisPerformance;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisPerformanceDTO;
import net.qixiaowei.strategy.cloud.mapper.gap.GapAnalysisPerformanceMapper;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * GapAnalysisPerformanceService业务层处理
 *
 * @author Graves
 * @since 2023-02-24
 */
@Service
public class GapAnalysisPerformanceServiceImpl implements IGapAnalysisPerformanceService {
    @Autowired
    private GapAnalysisPerformanceMapper gapAnalysisPerformanceMapper;

    /**
     * 查询业绩差距表
     *
     * @param gapAnalysisPerformanceId 业绩差距表主键
     * @return 业绩差距表
     */
    @Override
    public GapAnalysisPerformanceDTO selectGapAnalysisPerformanceByGapAnalysisPerformanceId(Long gapAnalysisPerformanceId) {
        return gapAnalysisPerformanceMapper.selectGapAnalysisPerformanceByGapAnalysisPerformanceId(gapAnalysisPerformanceId);
    }

    /**
     * 查询业绩差距表列表
     *
     * @param gapAnalysisPerformanceDTO 业绩差距表
     * @return 业绩差距表
     */
    @Override
    public List<GapAnalysisPerformanceDTO> selectGapAnalysisPerformanceList(GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO) {
        GapAnalysisPerformance gapAnalysisPerformance = new GapAnalysisPerformance();
        BeanUtils.copyProperties(gapAnalysisPerformanceDTO, gapAnalysisPerformance);
        return gapAnalysisPerformanceMapper.selectGapAnalysisPerformanceList(gapAnalysisPerformance);
    }

    /**
     * 新增业绩差距表
     *
     * @param gapAnalysisPerformanceDTO 业绩差距表
     * @return 结果
     */
    @Override
    public GapAnalysisPerformanceDTO insertGapAnalysisPerformance(GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO) {
        GapAnalysisPerformance gapAnalysisPerformance = new GapAnalysisPerformance();
        BeanUtils.copyProperties(gapAnalysisPerformanceDTO, gapAnalysisPerformance);
        gapAnalysisPerformance.setCreateBy(SecurityUtils.getUserId());
        gapAnalysisPerformance.setCreateTime(DateUtils.getNowDate());
        gapAnalysisPerformance.setUpdateTime(DateUtils.getNowDate());
        gapAnalysisPerformance.setUpdateBy(SecurityUtils.getUserId());
        gapAnalysisPerformance.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        gapAnalysisPerformanceMapper.insertGapAnalysisPerformance(gapAnalysisPerformance);
        gapAnalysisPerformanceDTO.setGapAnalysisPerformanceId(gapAnalysisPerformance.getGapAnalysisPerformanceId());
        return gapAnalysisPerformanceDTO;
    }

    /**
     * 修改业绩差距表
     *
     * @param gapAnalysisPerformanceDTO 业绩差距表
     * @return 结果
     */
    @Override
    public int updateGapAnalysisPerformance(GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO) {
        GapAnalysisPerformance gapAnalysisPerformance = new GapAnalysisPerformance();
        BeanUtils.copyProperties(gapAnalysisPerformanceDTO, gapAnalysisPerformance);
        gapAnalysisPerformance.setUpdateTime(DateUtils.getNowDate());
        gapAnalysisPerformance.setUpdateBy(SecurityUtils.getUserId());
        return gapAnalysisPerformanceMapper.updateGapAnalysisPerformance(gapAnalysisPerformance);
    }

    /**
     * 逻辑批量删除业绩差距表
     *
     * @param gapAnalysisPerformanceIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteGapAnalysisPerformanceByGapAnalysisPerformanceIds(List<Long> gapAnalysisPerformanceIds) {
        return gapAnalysisPerformanceMapper.logicDeleteGapAnalysisPerformanceByGapAnalysisPerformanceIds(gapAnalysisPerformanceIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除业绩差距表信息
     *
     * @param gapAnalysisPerformanceId 业绩差距表主键
     * @return 结果
     */
    @Override
    public int deleteGapAnalysisPerformanceByGapAnalysisPerformanceId(Long gapAnalysisPerformanceId) {
        return gapAnalysisPerformanceMapper.deleteGapAnalysisPerformanceByGapAnalysisPerformanceId(gapAnalysisPerformanceId);
    }

    /**
     * 根据差距分析ID查找业绩差距
     *
     * @param gapAnalysisId 差距分析ID
     * @return List
     */
    @Override
    public List<GapAnalysisPerformanceDTO> selectGapAnalysisPerformanceByGapAnalysisId(Long gapAnalysisId) {
        return gapAnalysisPerformanceMapper.selectGapAnalysisPerformanceByGapAnalysisId(gapAnalysisId);
    }

    /**
     * 根据差距分析ID集合查找业绩差距
     *
     * @param gapAnalysisIds 差距分析ID集合
     * @return 结果
     */
    @Override
    public List<GapAnalysisPerformanceDTO> selectGapAnalysisPerformanceByGapAnalysisIds(List<Long> gapAnalysisIds) {
        return gapAnalysisPerformanceMapper.selectGapAnalysisPerformanceByGapAnalysisIds(gapAnalysisIds);
    }

    /**
     * 逻辑删除业绩差距表信息
     *
     * @param gapAnalysisPerformanceDTO 业绩差距表
     * @return 结果
     */
    @Override
    public int logicDeleteGapAnalysisPerformanceByGapAnalysisPerformanceId(GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO) {
        GapAnalysisPerformance gapAnalysisPerformance = new GapAnalysisPerformance();
        gapAnalysisPerformance.setGapAnalysisPerformanceId(gapAnalysisPerformanceDTO.getGapAnalysisPerformanceId());
        gapAnalysisPerformance.setUpdateTime(DateUtils.getNowDate());
        gapAnalysisPerformance.setUpdateBy(SecurityUtils.getUserId());
        return gapAnalysisPerformanceMapper.logicDeleteGapAnalysisPerformanceByGapAnalysisPerformanceId(gapAnalysisPerformance);
    }

    /**
     * 物理删除业绩差距表信息
     *
     * @param gapAnalysisPerformanceDTO 业绩差距表
     * @return 结果
     */

    @Override
    public int deleteGapAnalysisPerformanceByGapAnalysisPerformanceId(GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO) {
        GapAnalysisPerformance gapAnalysisPerformance = new GapAnalysisPerformance();
        BeanUtils.copyProperties(gapAnalysisPerformanceDTO, gapAnalysisPerformance);
        return gapAnalysisPerformanceMapper.deleteGapAnalysisPerformanceByGapAnalysisPerformanceId(gapAnalysisPerformance.getGapAnalysisPerformanceId());
    }

    /**
     * 物理批量删除业绩差距表
     *
     * @param gapAnalysisPerformanceDtos 需要删除的业绩差距表主键
     * @return 结果
     */

    @Override
    public int deleteGapAnalysisPerformanceByGapAnalysisPerformanceIds(List<GapAnalysisPerformanceDTO> gapAnalysisPerformanceDtos) {
        List<Long> stringList = new ArrayList<>();
        for (GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO : gapAnalysisPerformanceDtos) {
            stringList.add(gapAnalysisPerformanceDTO.getGapAnalysisPerformanceId());
        }
        return gapAnalysisPerformanceMapper.deleteGapAnalysisPerformanceByGapAnalysisPerformanceIds(stringList);
    }

    /**
     * 批量新增业绩差距表信息
     *
     * @param gapAnalysisPerformanceDtos 业绩差距表对象
     */

    public int insertGapAnalysisPerformances(List<GapAnalysisPerformanceDTO> gapAnalysisPerformanceDtos) {
        List<GapAnalysisPerformance> gapAnalysisPerformanceList = new ArrayList<>();

        for (GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO : gapAnalysisPerformanceDtos) {
            GapAnalysisPerformance gapAnalysisPerformance = new GapAnalysisPerformance();
            BeanUtils.copyProperties(gapAnalysisPerformanceDTO, gapAnalysisPerformance);
            gapAnalysisPerformance.setCreateBy(SecurityUtils.getUserId());
            gapAnalysisPerformance.setCreateTime(DateUtils.getNowDate());
            gapAnalysisPerformance.setUpdateTime(DateUtils.getNowDate());
            gapAnalysisPerformance.setUpdateBy(SecurityUtils.getUserId());
            gapAnalysisPerformance.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            gapAnalysisPerformanceList.add(gapAnalysisPerformance);
        }
        return gapAnalysisPerformanceMapper.batchGapAnalysisPerformance(gapAnalysisPerformanceList);
    }

    /**
     * 批量修改业绩差距表信息
     *
     * @param gapAnalysisPerformanceDtos 业绩差距表对象
     */

    public int updateGapAnalysisPerformances(List<GapAnalysisPerformanceDTO> gapAnalysisPerformanceDtos) {
        List<GapAnalysisPerformance> gapAnalysisPerformanceList = new ArrayList<>();

        for (GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO : gapAnalysisPerformanceDtos) {
            GapAnalysisPerformance gapAnalysisPerformance = new GapAnalysisPerformance();
            BeanUtils.copyProperties(gapAnalysisPerformanceDTO, gapAnalysisPerformance);
            gapAnalysisPerformance.setCreateBy(SecurityUtils.getUserId());
            gapAnalysisPerformance.setCreateTime(DateUtils.getNowDate());
            gapAnalysisPerformance.setUpdateTime(DateUtils.getNowDate());
            gapAnalysisPerformance.setUpdateBy(SecurityUtils.getUserId());
            gapAnalysisPerformanceList.add(gapAnalysisPerformance);
        }
        return gapAnalysisPerformanceMapper.updateGapAnalysisPerformances(gapAnalysisPerformanceList);
    }

}

