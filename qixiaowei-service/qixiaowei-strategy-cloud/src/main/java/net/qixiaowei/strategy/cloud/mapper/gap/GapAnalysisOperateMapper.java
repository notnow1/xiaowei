package net.qixiaowei.strategy.cloud.mapper.gap;

import java.util.List;

import net.qixiaowei.strategy.cloud.api.domain.gap.GapAnalysisOperate;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOperateDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * GapAnalysisOperateMapper接口
 *
 * @author Graves
 * @since 2023-02-24
 */
public interface GapAnalysisOperateMapper {
    /**
     * 查询差距分析经营情况表
     *
     * @param gapAnalysisOperateId 差距分析经营情况表主键
     * @return 差距分析经营情况表
     */
    GapAnalysisOperateDTO selectGapAnalysisOperateByGapAnalysisOperateId(@Param("gapAnalysisOperateId") Long gapAnalysisOperateId);


    /**
     * 批量查询差距分析经营情况表
     *
     * @param gapAnalysisOperateIds 差距分析经营情况表主键集合
     * @return 差距分析经营情况表
     */
    List<GapAnalysisOperateDTO> selectGapAnalysisOperateByGapAnalysisOperateIds(@Param("gapAnalysisOperateIds") List<Long> gapAnalysisOperateIds);

    /**
     * 查询差距分析经营情况表列表
     *
     * @param gapAnalysisOperate 差距分析经营情况表
     * @return 差距分析经营情况表集合
     */
    List<GapAnalysisOperateDTO> selectGapAnalysisOperateList(@Param("gapAnalysisOperate") GapAnalysisOperate gapAnalysisOperate);

    /**
     * 新增差距分析经营情况表
     *
     * @param gapAnalysisOperate 差距分析经营情况表
     * @return 结果
     */
    int insertGapAnalysisOperate(@Param("gapAnalysisOperate") GapAnalysisOperate gapAnalysisOperate);

    /**
     * 修改差距分析经营情况表
     *
     * @param gapAnalysisOperate 差距分析经营情况表
     * @return 结果
     */
    int updateGapAnalysisOperate(@Param("gapAnalysisOperate") GapAnalysisOperate gapAnalysisOperate);

    /**
     * 批量修改差距分析经营情况表
     *
     * @param gapAnalysisOperateList 差距分析经营情况表
     * @return 结果
     */
    int updateGapAnalysisOperates(@Param("gapAnalysisOperateList") List<GapAnalysisOperate> gapAnalysisOperateList);

    /**
     * 逻辑删除差距分析经营情况表
     *
     * @param gapAnalysisOperate
     * @return 结果
     */
    int logicDeleteGapAnalysisOperateByGapAnalysisOperateId(@Param("gapAnalysisOperate") GapAnalysisOperate gapAnalysisOperate);

    /**
     * 逻辑批量删除差距分析经营情况表
     *
     * @param gapAnalysisOperateIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteGapAnalysisOperateByGapAnalysisOperateIds(@Param("gapAnalysisOperateIds") List<Long> gapAnalysisOperateIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除差距分析经营情况表
     *
     * @param gapAnalysisOperateId 差距分析经营情况表主键
     * @return 结果
     */
    int deleteGapAnalysisOperateByGapAnalysisOperateId(@Param("gapAnalysisOperateId") Long gapAnalysisOperateId);

    /**
     * 物理批量删除差距分析经营情况表
     *
     * @param gapAnalysisOperateIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteGapAnalysisOperateByGapAnalysisOperateIds(@Param("gapAnalysisOperateIds") List<Long> gapAnalysisOperateIds);

    /**
     * 批量新增差距分析经营情况表
     *
     * @param gapAnalysisOperates 差距分析经营情况表列表
     * @return 结果
     */
    int batchGapAnalysisOperate(@Param("gapAnalysisOperates") List<GapAnalysisOperate> gapAnalysisOperates);

    /**
     * 根据差距分析ID查询分析经营情况
     *
     * @param gapAnalysisId 差距分析ID
     * @return List
     */
    List<GapAnalysisOperateDTO> selectGapAnalysisOperateByGapAnalysisId(@Param("gapAnalysisId") Long gapAnalysisId);

    /**
     * 根据差距分析ID集合查询分析经营情况
     *
     * @param gapAnalysisIds 差距分析ID集合
     * @return List
     */
    List<GapAnalysisOperateDTO> selectGapAnalysisOperateByGapAnalysisIds(@Param("gapAnalysisIds") List<Long> gapAnalysisIds);
}
