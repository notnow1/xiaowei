package net.qixiaowei.operate.cloud.service.dashboard;

import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveAnalysisDTO;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveRateDTO;

import java.util.List;
import java.util.Map;

/**
 * IDashboardService接口
 *
 * @author Graves
 * @since 2022-10-27
 */
public interface IDashboardService {

    /**
     * 关键经营指标目标达成率看板
     *
     * @param targetYear 目标年度
     * @return List
     */
    List<TargetAchieveRateDTO> targetAchieveRateList(Integer targetYear);

    /**
     * 目标达成获取指标列表
     *
     * @return List
     */
    Map<String, Object> getDropList();

    /**
     * 关键经营指标月度达成分析列表
     *
     * @return List
     */
    List<TargetAchieveAnalysisDTO> targetAchieveAnalysisList(TargetAchieveAnalysisDTO targetAchieveAnalysisDTO);
}
