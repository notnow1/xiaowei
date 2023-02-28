package net.qixiaowei.strategy.cloud.mapper.gap;

import java.util.List;

import net.qixiaowei.strategy.cloud.api.domain.gap.GapAnalysisOpportunity;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOpportunityDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * GapAnalysisOpportunityMapper接口
 *
 * @author Graves
 * @since 2023-02-24
 */
public interface GapAnalysisOpportunityMapper {
    /**
     * 查询机会差距表
     *
     * @param gapAnalysisOpportunityId 机会差距表主键
     * @return 机会差距表
     */
    GapAnalysisOpportunityDTO selectGapAnalysisOpportunityByGapAnalysisOpportunityId(@Param("gapAnalysisOpportunityId") Long gapAnalysisOpportunityId);


    /**
     * 批量查询机会差距表
     *
     * @param gapAnalysisOpportunityIds 机会差距表主键集合
     * @return 机会差距表
     */
    List<GapAnalysisOpportunityDTO> selectGapAnalysisOpportunityByGapAnalysisOpportunityIds(@Param("gapAnalysisOpportunityIds") List<Long> gapAnalysisOpportunityIds);

    /**
     * 查询机会差距表列表
     *
     * @param gapAnalysisOpportunity 机会差距表
     * @return 机会差距表集合
     */
    List<GapAnalysisOpportunityDTO> selectGapAnalysisOpportunityList(@Param("gapAnalysisOpportunity") GapAnalysisOpportunity gapAnalysisOpportunity);

    /**
     * 新增机会差距表
     *
     * @param gapAnalysisOpportunity 机会差距表
     * @return 结果
     */
    int insertGapAnalysisOpportunity(@Param("gapAnalysisOpportunity") GapAnalysisOpportunity gapAnalysisOpportunity);

    /**
     * 修改机会差距表
     *
     * @param gapAnalysisOpportunity 机会差距表
     * @return 结果
     */
    int updateGapAnalysisOpportunity(@Param("gapAnalysisOpportunity") GapAnalysisOpportunity gapAnalysisOpportunity);

    /**
     * 批量修改机会差距表
     *
     * @param gapAnalysisOpportunityList 机会差距表
     * @return 结果
     */
    int updateGapAnalysisOpportunitys(@Param("gapAnalysisOpportunityList") List<GapAnalysisOpportunity> gapAnalysisOpportunityList);

    /**
     * 逻辑删除机会差距表
     *
     * @param gapAnalysisOpportunity
     * @return 结果
     */
    int logicDeleteGapAnalysisOpportunityByGapAnalysisOpportunityId(@Param("gapAnalysisOpportunity") GapAnalysisOpportunity gapAnalysisOpportunity);

    /**
     * 逻辑批量删除机会差距表
     *
     * @param gapAnalysisOpportunityIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteGapAnalysisOpportunityByGapAnalysisOpportunityIds(@Param("gapAnalysisOpportunityIds") List<Long> gapAnalysisOpportunityIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除机会差距表
     *
     * @param gapAnalysisOpportunityId 机会差距表主键
     * @return 结果
     */
    int deleteGapAnalysisOpportunityByGapAnalysisOpportunityId(@Param("gapAnalysisOpportunityId") Long gapAnalysisOpportunityId);

    /**
     * 物理批量删除机会差距表
     *
     * @param gapAnalysisOpportunityIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteGapAnalysisOpportunityByGapAnalysisOpportunityIds(@Param("gapAnalysisOpportunityIds") List<Long> gapAnalysisOpportunityIds);

    /**
     * 批量新增机会差距表
     *
     * @param gapAnalysisOpportunitys 机会差距表列表
     * @return 结果
     */
    int batchGapAnalysisOpportunity(@Param("gapAnalysisOpportunitys") List<GapAnalysisOpportunity> gapAnalysisOpportunitys);

    /**
     * 根据差距分析ID查找机会差距
     *
     * @param gapAnalysisId 差距分析ID
     * @return List
     */
    List<GapAnalysisOpportunityDTO> selectGapAnalysisOpportunityByGapAnalysisId(@Param("gapAnalysisId") Long gapAnalysisId);

    /**
     * 根据差距分析ID集合查找机会差距
     *
     * @param gapAnalysisIds 差距分析ID集合
     * @return 差距分析DTO
     */
    List<GapAnalysisOpportunityDTO> selectGapAnalysisOpportunityByGapAnalysisIds(@Param("gapAnalysisIds") List<Long> gapAnalysisIds);
}
