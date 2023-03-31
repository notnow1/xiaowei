package net.qixiaowei.strategy.cloud.mapper.gap;

import java.util.List;

import net.qixiaowei.strategy.cloud.api.domain.gap.GapAnalysis;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * GapAnalysisMapper接口
 *
 * @author Graves
 * @since 2023-02-24
 */
public interface GapAnalysisMapper {
    /**
     * 查询差距分析表
     *
     * @param gapAnalysisId 差距分析表主键
     * @return 差距分析表
     */
    GapAnalysisDTO selectGapAnalysisByGapAnalysisId(@Param("gapAnalysisId") Long gapAnalysisId);


    /**
     * 批量查询差距分析表
     *
     * @param gapAnalysisIds 差距分析表主键集合
     * @return 差距分析表
     */
    List<GapAnalysisDTO> selectGapAnalysisByGapAnalysisIds(@Param("gapAnalysisIds") List<Long> gapAnalysisIds);

    /**
     * 查询差距分析表列表
     *
     * @param gapAnalysis 差距分析表
     * @return 差距分析表集合
     */
    List<GapAnalysisDTO> selectGapAnalysisList(@Param("gapAnalysis") GapAnalysis gapAnalysis);

    /**
     * 新增差距分析表
     *
     * @param gapAnalysis 差距分析表
     * @return 结果
     */
    int insertGapAnalysis(@Param("gapAnalysis") GapAnalysis gapAnalysis);

    /**
     * 修改差距分析表
     *
     * @param gapAnalysis 差距分析表
     * @return 结果
     */
    int updateGapAnalysis(@Param("gapAnalysis") GapAnalysis gapAnalysis);

    /**
     * 批量修改差距分析表
     *
     * @param gapAnalysisList 差距分析表
     * @return 结果
     */
    int updateGapAnalysiss(@Param("gapAnalysisList") List<GapAnalysis> gapAnalysisList);

    /**
     * 逻辑删除差距分析表
     *
     * @param gapAnalysis
     * @return 结果
     */
    int logicDeleteGapAnalysisByGapAnalysisId(@Param("gapAnalysis") GapAnalysis gapAnalysis);

    /**
     * 逻辑批量删除差距分析表
     *
     * @param gapAnalysisIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteGapAnalysisByGapAnalysisIds(@Param("gapAnalysisIds") List<Long> gapAnalysisIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除差距分析表
     *
     * @param gapAnalysisId 差距分析表主键
     * @return 结果
     */
    int deleteGapAnalysisByGapAnalysisId(@Param("gapAnalysisId") Long gapAnalysisId);

    /**
     * 物理批量删除差距分析表
     *
     * @param gapAnalysisIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteGapAnalysisByGapAnalysisIds(@Param("gapAnalysisIds") List<Long> gapAnalysisIds);

    /**
     * 批量新增差距分析表
     *
     * @param gapAnalysiss 差距分析表列表
     * @return 结果
     */
    int batchGapAnalysis(@Param("gapAnalysiss") List<GapAnalysis> gapAnalysiss);

    /**
     * 根据维度ID集合查找
     *
     * @param planBusinessUnitIds 维度ID集合
     * @return 结果
     */
    List<GapAnalysisDTO> selectGapAnalysisByPlanBusinessUnitIds(@Param("planBusinessUnitIds") List<Long> planBusinessUnitIds);
}
