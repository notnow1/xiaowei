package net.qixiaowei.strategy.cloud.mapper.gap;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.gap.GapAnalysisPerformance;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisPerformanceDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* GapAnalysisPerformanceMapper接口
* @author Graves
* @since 2023-02-24
*/
public interface GapAnalysisPerformanceMapper{
    /**
    * 查询业绩差距表
    *
    * @param gapAnalysisPerformanceId 业绩差距表主键
    * @return 业绩差距表
    */
    GapAnalysisPerformanceDTO selectGapAnalysisPerformanceByGapAnalysisPerformanceId(@Param("gapAnalysisPerformanceId")Long gapAnalysisPerformanceId);


    /**
    * 批量查询业绩差距表
    *
    * @param gapAnalysisPerformanceIds 业绩差距表主键集合
    * @return 业绩差距表
    */
    List<GapAnalysisPerformanceDTO> selectGapAnalysisPerformanceByGapAnalysisPerformanceIds(@Param("gapAnalysisPerformanceIds") List<Long> gapAnalysisPerformanceIds);

    /**
    * 查询业绩差距表列表
    *
    * @param gapAnalysisPerformance 业绩差距表
    * @return 业绩差距表集合
    */
    List<GapAnalysisPerformanceDTO> selectGapAnalysisPerformanceList(@Param("gapAnalysisPerformance")GapAnalysisPerformance gapAnalysisPerformance);

    /**
    * 新增业绩差距表
    *
    * @param gapAnalysisPerformance 业绩差距表
    * @return 结果
    */
    int insertGapAnalysisPerformance(@Param("gapAnalysisPerformance")GapAnalysisPerformance gapAnalysisPerformance);

    /**
    * 修改业绩差距表
    *
    * @param gapAnalysisPerformance 业绩差距表
    * @return 结果
    */
    int updateGapAnalysisPerformance(@Param("gapAnalysisPerformance")GapAnalysisPerformance gapAnalysisPerformance);

    /**
    * 批量修改业绩差距表
    *
    * @param gapAnalysisPerformanceList 业绩差距表
    * @return 结果
    */
    int updateGapAnalysisPerformances(@Param("gapAnalysisPerformanceList")List<GapAnalysisPerformance> gapAnalysisPerformanceList);
    /**
    * 逻辑删除业绩差距表
    *
    * @param gapAnalysisPerformance
    * @return 结果
    */
    int logicDeleteGapAnalysisPerformanceByGapAnalysisPerformanceId(@Param("gapAnalysisPerformance")GapAnalysisPerformance gapAnalysisPerformance);

    /**
    * 逻辑批量删除业绩差距表
    *
    * @param gapAnalysisPerformanceIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteGapAnalysisPerformanceByGapAnalysisPerformanceIds(@Param("gapAnalysisPerformanceIds")List<Long> gapAnalysisPerformanceIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除业绩差距表
    *
    * @param gapAnalysisPerformanceId 业绩差距表主键
    * @return 结果
    */
    int deleteGapAnalysisPerformanceByGapAnalysisPerformanceId(@Param("gapAnalysisPerformanceId")Long gapAnalysisPerformanceId);

    /**
    * 物理批量删除业绩差距表
    *
    * @param gapAnalysisPerformanceIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteGapAnalysisPerformanceByGapAnalysisPerformanceIds(@Param("gapAnalysisPerformanceIds")List<Long> gapAnalysisPerformanceIds);

    /**
    * 批量新增业绩差距表
    *
    * @param gapAnalysisPerformances 业绩差距表列表
    * @return 结果
    */
    int batchGapAnalysisPerformance(@Param("gapAnalysisPerformances")List<GapAnalysisPerformance> gapAnalysisPerformances);
}
