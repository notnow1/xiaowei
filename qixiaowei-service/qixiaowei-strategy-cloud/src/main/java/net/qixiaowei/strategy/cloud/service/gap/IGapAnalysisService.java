package net.qixiaowei.strategy.cloud.service.gap;

import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOperateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
 * GapAnalysisService接口
 *
 * @author Graves
 * @since 2023-02-24
 */
public interface IGapAnalysisService {
    /**
     * 查询差距分析表
     *
     * @param gapAnalysisId 差距分析表主键
     * @return 差距分析表
     */
    GapAnalysisDTO selectGapAnalysisByGapAnalysisId(Long gapAnalysisId);

    /**
     * 查询差距分析表列表
     *
     * @param gapAnalysisDTO 差距分析表
     * @return 差距分析表集合
     */
    List<GapAnalysisDTO> selectGapAnalysisList(GapAnalysisDTO gapAnalysisDTO);

    /**
     * 新增差距分析表
     *
     * @param gapAnalysisDTO 差距分析表
     * @return 结果
     */
    GapAnalysisDTO insertGapAnalysis(GapAnalysisDTO gapAnalysisDTO);

    /**
     * 修改差距分析表
     *
     * @param gapAnalysisDTO 差距分析表
     * @return 结果
     */
    int updateGapAnalysis(GapAnalysisDTO gapAnalysisDTO);

    /**
     * 批量修改差距分析表
     *
     * @param gapAnalysisDtoS 差距分析表
     * @return 结果
     */
    int insertGapAnalysisS(List<GapAnalysisDTO> gapAnalysisDtoS);

    /**
     * 批量新增差距分析表
     *
     * @param gapAnalysisDtos 差距分析表
     * @return 结果
     */
    int insertGapAnalysiss(List<GapAnalysisDTO> gapAnalysisDtos);

    /**
     * 逻辑批量删除差距分析表
     *
     * @param gapAnalysisIds 需要删除的差距分析表集合
     * @return 结果
     */
    int logicDeleteGapAnalysisByGapAnalysisIds(List<Long> gapAnalysisIds);

    /**
     * 逻辑删除差距分析表信息
     *
     * @param gapAnalysisDTO
     * @return 结果
     */
    int logicDeleteGapAnalysisByGapAnalysisId(GapAnalysisDTO gapAnalysisDTO);

    /**
     * 批量删除差距分析表
     *
     * @param GapAnalysisDtos
     * @return 结果
     */
    int deleteGapAnalysisByGapAnalysisIds(List<GapAnalysisDTO> GapAnalysisDtos);

    /**
     * 逻辑删除差距分析表信息
     *
     * @param gapAnalysisDTO
     * @return 结果
     */
    int deleteGapAnalysisByGapAnalysisId(GapAnalysisDTO gapAnalysisDTO);


    /**
     * 删除差距分析表信息
     *
     * @param gapAnalysisId 差距分析表主键
     * @return 结果
     */
    int deleteGapAnalysisByGapAnalysisId(Long gapAnalysisId);

    /**
     * 定义表头模板
     *
     * @param operateHistoryYear 历史经营年份
     * @param operateYear        年度
     * @return List
     */
    List<List<String>> headTemplate(Integer operateHistoryYear, Integer operateYear, Map<Integer, List<String>> selectMap);

    /**
     * 解析EXCEL
     *
     * @param operateHistoryYear 历史经营年份
     * @param operateYear        经营年度
     * @param file               excel文件
     */
    List<GapAnalysisOperateDTO> excelParseObject(Integer operateHistoryYear, Integer operateYear, MultipartFile file);
}
