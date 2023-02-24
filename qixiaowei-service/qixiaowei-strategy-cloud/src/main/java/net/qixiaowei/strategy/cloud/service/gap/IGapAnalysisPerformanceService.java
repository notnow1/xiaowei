package net.qixiaowei.strategy.cloud.service.gap;

import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisPerformanceDTO;

import java.util.List;


/**
 * GapAnalysisPerformanceService接口
 *
 * @author Graves
 * @since 2023-02-24
 */
public interface IGapAnalysisPerformanceService {
    /**
     * 查询业绩差距表
     *
     * @param gapAnalysisPerformanceId 业绩差距表主键
     * @return 业绩差距表
     */
    GapAnalysisPerformanceDTO selectGapAnalysisPerformanceByGapAnalysisPerformanceId(Long gapAnalysisPerformanceId);

    /**
     * 查询业绩差距表列表
     *
     * @param gapAnalysisPerformanceDTO 业绩差距表
     * @return 业绩差距表集合
     */
    List<GapAnalysisPerformanceDTO> selectGapAnalysisPerformanceList(GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO);

    /**
     * 新增业绩差距表
     *
     * @param gapAnalysisPerformanceDTO 业绩差距表
     * @return 结果
     */
    GapAnalysisPerformanceDTO insertGapAnalysisPerformance(GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO);

    /**
     * 修改业绩差距表
     *
     * @param gapAnalysisPerformanceDTO 业绩差距表
     * @return 结果
     */
    int updateGapAnalysisPerformance(GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO);

    /**
     * 批量修改业绩差距表
     *
     * @param gapAnalysisPerformanceDtos 业绩差距表
     * @return 结果
     */
    int updateGapAnalysisPerformances(List<GapAnalysisPerformanceDTO> gapAnalysisPerformanceDtos);

    /**
     * 批量新增业绩差距表
     *
     * @param gapAnalysisPerformanceDtos 业绩差距表
     * @return 结果
     */
    int insertGapAnalysisPerformances(List<GapAnalysisPerformanceDTO> gapAnalysisPerformanceDtos);

    /**
     * 逻辑批量删除业绩差距表
     *
     * @param gapAnalysisPerformanceIds 需要删除的业绩差距表集合
     * @return 结果
     */
    int logicDeleteGapAnalysisPerformanceByGapAnalysisPerformanceIds(List<Long> gapAnalysisPerformanceIds);

    /**
     * 逻辑删除业绩差距表信息
     *
     * @param gapAnalysisPerformanceDTO
     * @return 结果
     */
    int logicDeleteGapAnalysisPerformanceByGapAnalysisPerformanceId(GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO);

    /**
     * 批量删除业绩差距表
     *
     * @param GapAnalysisPerformanceDtos
     * @return 结果
     */
    int deleteGapAnalysisPerformanceByGapAnalysisPerformanceIds(List<GapAnalysisPerformanceDTO> GapAnalysisPerformanceDtos);

    /**
     * 逻辑删除业绩差距表信息
     *
     * @param gapAnalysisPerformanceDTO
     * @return 结果
     */
    int deleteGapAnalysisPerformanceByGapAnalysisPerformanceId(GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO);


    /**
     * 删除业绩差距表信息
     *
     * @param gapAnalysisPerformanceId 业绩差距表主键
     * @return 结果
     */
    int deleteGapAnalysisPerformanceByGapAnalysisPerformanceId(Long gapAnalysisPerformanceId);

}
