package net.qixiaowei.strategy.cloud.service.gap;

import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOperateDTO;
import net.qixiaowei.strategy.cloud.excel.gap.GapAnalysisOperateExcel;

import java.util.List;


/**
 * GapAnalysisOperateService接口
 *
 * @author Graves
 * @since 2023-02-24
 */
public interface IGapAnalysisOperateService {
    /**
     * 查询差距分析经营情况表
     *
     * @param gapAnalysisOperateId 差距分析经营情况表主键
     * @return 差距分析经营情况表
     */
    GapAnalysisOperateDTO selectGapAnalysisOperateByGapAnalysisOperateId(Long gapAnalysisOperateId);

    /**
     * 查询差距分析经营情况表列表
     *
     * @param gapAnalysisOperateDTO 差距分析经营情况表
     * @return 差距分析经营情况表集合
     */
    List<GapAnalysisOperateDTO> selectGapAnalysisOperateList(GapAnalysisOperateDTO gapAnalysisOperateDTO);

    /**
     * 新增差距分析经营情况表
     *
     * @param gapAnalysisOperateDTO 差距分析经营情况表
     * @return 结果
     */
    GapAnalysisOperateDTO insertGapAnalysisOperate(GapAnalysisOperateDTO gapAnalysisOperateDTO);

    /**
     * 修改差距分析经营情况表
     *
     * @param gapAnalysisOperateDTO 差距分析经营情况表
     * @return 结果
     */
    int updateGapAnalysisOperate(GapAnalysisOperateDTO gapAnalysisOperateDTO);

    /**
     * 批量修改差距分析经营情况表
     *
     * @param gapAnalysisOperateDtos 差距分析经营情况表
     * @return 结果
     */
    int updateGapAnalysisOperates(List<GapAnalysisOperateDTO> gapAnalysisOperateDtos);

    /**
     * 批量新增差距分析经营情况表
     *
     * @param gapAnalysisOperateDtos 差距分析经营情况表
     * @return 结果
     */
    int insertGapAnalysisOperates(List<GapAnalysisOperateDTO> gapAnalysisOperateDtos);

    /**
     * 逻辑批量删除差距分析经营情况表
     *
     * @param gapAnalysisOperateIds 需要删除的差距分析经营情况表集合
     * @return 结果
     */
    int logicDeleteGapAnalysisOperateByGapAnalysisOperateIds(List<Long> gapAnalysisOperateIds);

    /**
     * 逻辑删除差距分析经营情况表信息
     *
     * @param gapAnalysisOperateDTO
     * @return 结果
     */
    int logicDeleteGapAnalysisOperateByGapAnalysisOperateId(GapAnalysisOperateDTO gapAnalysisOperateDTO);

    /**
     * 批量删除差距分析经营情况表
     *
     * @param GapAnalysisOperateDtos
     * @return 结果
     */
    int deleteGapAnalysisOperateByGapAnalysisOperateIds(List<GapAnalysisOperateDTO> GapAnalysisOperateDtos);

    /**
     * 逻辑删除差距分析经营情况表信息
     *
     * @param gapAnalysisOperateDTO
     * @return 结果
     */
    int deleteGapAnalysisOperateByGapAnalysisOperateId(GapAnalysisOperateDTO gapAnalysisOperateDTO);


    /**
     * 删除差距分析经营情况表信息
     *
     * @param gapAnalysisOperateId 差距分析经营情况表主键
     * @return 结果
     */
    int deleteGapAnalysisOperateByGapAnalysisOperateId(Long gapAnalysisOperateId);

    /**
     * 导入Excel
     *
     * @param list
     */
    void importGapAnalysisOperate(List<GapAnalysisOperateExcel> list);

    /**
     * 导出Excel
     *
     * @param gapAnalysisOperateDTO
     * @return
     */
    List<GapAnalysisOperateExcel> exportGapAnalysisOperate(GapAnalysisOperateDTO gapAnalysisOperateDTO);
}
