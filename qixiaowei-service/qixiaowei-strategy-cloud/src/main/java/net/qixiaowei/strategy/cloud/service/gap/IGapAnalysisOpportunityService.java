package net.qixiaowei.strategy.cloud.service.gap;

import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOperateDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOpportunityDTO;

import java.util.List;


/**
 * GapAnalysisOpportunityService接口
 *
 * @author Graves
 * @since 2023-02-24
 */
public interface IGapAnalysisOpportunityService {
    /**
     * 查询机会差距表
     *
     * @param gapAnalysisOpportunityId 机会差距表主键
     * @return 机会差距表
     */
    GapAnalysisOpportunityDTO selectGapAnalysisOpportunityByGapAnalysisOpportunityId(Long gapAnalysisOpportunityId);

    /**
     * 查询机会差距表列表
     *
     * @param gapAnalysisOpportunityDTO 机会差距表
     * @return 机会差距表集合
     */
    List<GapAnalysisOpportunityDTO> selectGapAnalysisOpportunityList(GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO);

    /**
     * 新增机会差距表
     *
     * @param gapAnalysisOpportunityDTO 机会差距表
     * @return 结果
     */
    GapAnalysisOpportunityDTO insertGapAnalysisOpportunity(GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO);

    /**
     * 修改机会差距表
     *
     * @param gapAnalysisOpportunityDTO 机会差距表
     * @return 结果
     */
    int updateGapAnalysisOpportunity(GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO);

    /**
     * 批量修改机会差距表
     *
     * @param gapAnalysisOpportunityDtos 机会差距表
     * @return 结果
     */
    int updateGapAnalysisOpportunitys(List<GapAnalysisOpportunityDTO> gapAnalysisOpportunityDtos);

    /**
     * 批量新增机会差距表
     *
     * @param gapAnalysisOpportunityDtos 机会差距表
     * @return 结果
     */
    int insertGapAnalysisOpportunitys(List<GapAnalysisOpportunityDTO> gapAnalysisOpportunityDtos);

    /**
     * 逻辑批量删除机会差距表
     *
     * @param gapAnalysisOpportunityIds 需要删除的机会差距表集合
     * @return 结果
     */
    int logicDeleteGapAnalysisOpportunityByGapAnalysisOpportunityIds(List<Long> gapAnalysisOpportunityIds);

    /**
     * 逻辑删除机会差距表信息
     *
     * @param gapAnalysisOpportunityDTO
     * @return 结果
     */
    int logicDeleteGapAnalysisOpportunityByGapAnalysisOpportunityId(GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO);

    /**
     * 批量删除机会差距表
     *
     * @param GapAnalysisOpportunityDtos
     * @return 结果
     */
    int deleteGapAnalysisOpportunityByGapAnalysisOpportunityIds(List<GapAnalysisOpportunityDTO> GapAnalysisOpportunityDtos);

    /**
     * 逻辑删除机会差距表信息
     *
     * @param gapAnalysisOpportunityDTO
     * @return 结果
     */
    int deleteGapAnalysisOpportunityByGapAnalysisOpportunityId(GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO);


    /**
     * 删除机会差距表信息
     *
     * @param gapAnalysisOpportunityId 机会差距表主键
     * @return 结果
     */
    int deleteGapAnalysisOpportunityByGapAnalysisOpportunityId(Long gapAnalysisOpportunityId);

    /**
     * 根据差距分析ID查找机会差距
     *
     * @param gapAnalysisId 差距分析ID
     * @return 差距分析DTO
     */
    List<GapAnalysisOpportunityDTO> selectGapAnalysisOpportunityByGapAnalysisId(Long gapAnalysisId);

    /**
     * 根据差距分析ID集合查找机会差距
     *
     * @param gapAnalysisIds 差距分析ID集合
     * @return 差距分析DTO
     */
    List<GapAnalysisOpportunityDTO> selectGapAnalysisOpportunityByGapAnalysisIds(List<Long> gapAnalysisIds);
}
