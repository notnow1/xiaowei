package net.qixiaowei.strategy.cloud.service.impl.gap;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.gap.GapAnalysisOpportunity;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOpportunityDTO;
import net.qixiaowei.strategy.cloud.mapper.gap.GapAnalysisOpportunityMapper;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * GapAnalysisOpportunityService业务层处理
 *
 * @author Graves
 * @since 2023-02-24
 */
@Service
public class GapAnalysisOpportunityServiceImpl implements IGapAnalysisOpportunityService {
    @Autowired
    private GapAnalysisOpportunityMapper gapAnalysisOpportunityMapper;

    /**
     * 查询机会差距表
     *
     * @param gapAnalysisOpportunityId 机会差距表主键
     * @return 机会差距表
     */
    @Override
    public GapAnalysisOpportunityDTO selectGapAnalysisOpportunityByGapAnalysisOpportunityId(Long gapAnalysisOpportunityId) {
        return gapAnalysisOpportunityMapper.selectGapAnalysisOpportunityByGapAnalysisOpportunityId(gapAnalysisOpportunityId);
    }

    /**
     * 查询机会差距表列表
     *
     * @param gapAnalysisOpportunityDTO 机会差距表
     * @return 机会差距表
     */
    @Override
    public List<GapAnalysisOpportunityDTO> selectGapAnalysisOpportunityList(GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO) {
        GapAnalysisOpportunity gapAnalysisOpportunity = new GapAnalysisOpportunity();
        BeanUtils.copyProperties(gapAnalysisOpportunityDTO, gapAnalysisOpportunity);
        Map<String, Object> params = gapAnalysisOpportunityDTO.getParams();
        gapAnalysisOpportunity.setParams(params);
        return gapAnalysisOpportunityMapper.selectGapAnalysisOpportunityList(gapAnalysisOpportunity);
    }

    /**
     * 新增机会差距表
     *
     * @param gapAnalysisOpportunityDTO 机会差距表
     * @return 结果
     */
    @Override
    public GapAnalysisOpportunityDTO insertGapAnalysisOpportunity(GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO) {
        GapAnalysisOpportunity gapAnalysisOpportunity = new GapAnalysisOpportunity();
        BeanUtils.copyProperties(gapAnalysisOpportunityDTO, gapAnalysisOpportunity);
        gapAnalysisOpportunity.setCreateBy(SecurityUtils.getUserId());
        gapAnalysisOpportunity.setCreateTime(DateUtils.getNowDate());
        gapAnalysisOpportunity.setUpdateTime(DateUtils.getNowDate());
        gapAnalysisOpportunity.setUpdateBy(SecurityUtils.getUserId());
        gapAnalysisOpportunity.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        gapAnalysisOpportunityMapper.insertGapAnalysisOpportunity(gapAnalysisOpportunity);
        gapAnalysisOpportunityDTO.setGapAnalysisOpportunityId(gapAnalysisOpportunity.getGapAnalysisOpportunityId());
        return gapAnalysisOpportunityDTO;
    }

    /**
     * 修改机会差距表
     *
     * @param gapAnalysisOpportunityDTO 机会差距表
     * @return 结果
     */
    @Override
    public int updateGapAnalysisOpportunity(GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO) {
        GapAnalysisOpportunity gapAnalysisOpportunity = new GapAnalysisOpportunity();
        BeanUtils.copyProperties(gapAnalysisOpportunityDTO, gapAnalysisOpportunity);
        gapAnalysisOpportunity.setUpdateTime(DateUtils.getNowDate());
        gapAnalysisOpportunity.setUpdateBy(SecurityUtils.getUserId());
        return gapAnalysisOpportunityMapper.updateGapAnalysisOpportunity(gapAnalysisOpportunity);
    }

    /**
     * 逻辑批量删除机会差距表
     *
     * @param gapAnalysisOpportunityIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteGapAnalysisOpportunityByGapAnalysisOpportunityIds(List<Long> gapAnalysisOpportunityIds) {
        return gapAnalysisOpportunityMapper.logicDeleteGapAnalysisOpportunityByGapAnalysisOpportunityIds(gapAnalysisOpportunityIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除机会差距表信息
     *
     * @param gapAnalysisOpportunityId 机会差距表主键
     * @return 结果
     */
    @Override
    public int deleteGapAnalysisOpportunityByGapAnalysisOpportunityId(Long gapAnalysisOpportunityId) {
        return gapAnalysisOpportunityMapper.deleteGapAnalysisOpportunityByGapAnalysisOpportunityId(gapAnalysisOpportunityId);
    }

    /**
     * 根据差距分析ID查找机会差距
     *
     * @param gapAnalysisId 差距分析ID
     * @return List
     */
    @Override
    public List<GapAnalysisOpportunityDTO> selectGapAnalysisOpportunityByGapAnalysisId(Long gapAnalysisId) {
        return gapAnalysisOpportunityMapper.selectGapAnalysisOpportunityByGapAnalysisId(gapAnalysisId);
    }

    /**
     * 根据差距分析ID集合查找机会差距
     *
     * @param gapAnalysisIds 差距分析ID集合
     * @return 差距分析DTO
     */
    @Override
    public List<GapAnalysisOpportunityDTO> selectGapAnalysisOpportunityByGapAnalysisIds(List<Long> gapAnalysisIds) {
        return gapAnalysisOpportunityMapper.selectGapAnalysisOpportunityByGapAnalysisIds(gapAnalysisIds);
    }

    /**
     * 逻辑删除机会差距表信息
     *
     * @param gapAnalysisOpportunityDTO 机会差距表
     * @return 结果
     */
    @Override
    public int logicDeleteGapAnalysisOpportunityByGapAnalysisOpportunityId(GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO) {
        GapAnalysisOpportunity gapAnalysisOpportunity = new GapAnalysisOpportunity();
        gapAnalysisOpportunity.setGapAnalysisOpportunityId(gapAnalysisOpportunityDTO.getGapAnalysisOpportunityId());
        gapAnalysisOpportunity.setUpdateTime(DateUtils.getNowDate());
        gapAnalysisOpportunity.setUpdateBy(SecurityUtils.getUserId());
        return gapAnalysisOpportunityMapper.logicDeleteGapAnalysisOpportunityByGapAnalysisOpportunityId(gapAnalysisOpportunity);
    }

    /**
     * 物理删除机会差距表信息
     *
     * @param gapAnalysisOpportunityDTO 机会差距表
     * @return 结果
     */

    @Override
    public int deleteGapAnalysisOpportunityByGapAnalysisOpportunityId(GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO) {
        GapAnalysisOpportunity gapAnalysisOpportunity = new GapAnalysisOpportunity();
        BeanUtils.copyProperties(gapAnalysisOpportunityDTO, gapAnalysisOpportunity);
        return gapAnalysisOpportunityMapper.deleteGapAnalysisOpportunityByGapAnalysisOpportunityId(gapAnalysisOpportunity.getGapAnalysisOpportunityId());
    }

    /**
     * 物理批量删除机会差距表
     *
     * @param gapAnalysisOpportunityDtos 需要删除的机会差距表主键
     * @return 结果
     */

    @Override
    public int deleteGapAnalysisOpportunityByGapAnalysisOpportunityIds(List<GapAnalysisOpportunityDTO> gapAnalysisOpportunityDtos) {
        List<Long> stringList = new ArrayList();
        for (GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO : gapAnalysisOpportunityDtos) {
            stringList.add(gapAnalysisOpportunityDTO.getGapAnalysisOpportunityId());
        }
        return gapAnalysisOpportunityMapper.deleteGapAnalysisOpportunityByGapAnalysisOpportunityIds(stringList);
    }

    /**
     * 批量新增机会差距表信息
     *
     * @param gapAnalysisOpportunityDtos 机会差距表对象
     */

    public int insertGapAnalysisOpportunitys(List<GapAnalysisOpportunityDTO> gapAnalysisOpportunityDtos) {
        List<GapAnalysisOpportunity> gapAnalysisOpportunityList = new ArrayList();

        for (GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO : gapAnalysisOpportunityDtos) {
            GapAnalysisOpportunity gapAnalysisOpportunity = new GapAnalysisOpportunity();
            BeanUtils.copyProperties(gapAnalysisOpportunityDTO, gapAnalysisOpportunity);
            gapAnalysisOpportunity.setCreateBy(SecurityUtils.getUserId());
            gapAnalysisOpportunity.setCreateTime(DateUtils.getNowDate());
            gapAnalysisOpportunity.setUpdateTime(DateUtils.getNowDate());
            gapAnalysisOpportunity.setUpdateBy(SecurityUtils.getUserId());
            gapAnalysisOpportunity.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            gapAnalysisOpportunityList.add(gapAnalysisOpportunity);
        }
        return gapAnalysisOpportunityMapper.batchGapAnalysisOpportunity(gapAnalysisOpportunityList);
    }

    /**
     * 批量修改机会差距表信息
     *
     * @param gapAnalysisOpportunityDtos 机会差距表对象
     */

    public int updateGapAnalysisOpportunitys(List<GapAnalysisOpportunityDTO> gapAnalysisOpportunityDtos) {
        List<GapAnalysisOpportunity> gapAnalysisOpportunityList = new ArrayList();

        for (GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO : gapAnalysisOpportunityDtos) {
            GapAnalysisOpportunity gapAnalysisOpportunity = new GapAnalysisOpportunity();
            BeanUtils.copyProperties(gapAnalysisOpportunityDTO, gapAnalysisOpportunity);
            gapAnalysisOpportunity.setCreateBy(SecurityUtils.getUserId());
            gapAnalysisOpportunity.setCreateTime(DateUtils.getNowDate());
            gapAnalysisOpportunity.setUpdateTime(DateUtils.getNowDate());
            gapAnalysisOpportunity.setUpdateBy(SecurityUtils.getUserId());
            gapAnalysisOpportunityList.add(gapAnalysisOpportunity);
        }
        return gapAnalysisOpportunityMapper.updateGapAnalysisOpportunitys(gapAnalysisOpportunityList);
    }

}

