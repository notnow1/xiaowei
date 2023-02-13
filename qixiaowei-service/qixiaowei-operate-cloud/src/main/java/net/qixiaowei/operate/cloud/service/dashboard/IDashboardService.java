package net.qixiaowei.operate.cloud.service.dashboard;

import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveAnalysisDTO;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveRateDTO;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetLeaderboardDTO;

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
     * @param targetAchieveRateDTO dto
     * @return List
     */
    List<TargetAchieveRateDTO> targetAchieveRateList(TargetAchieveRateDTO targetAchieveRateDTO);

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
    Map<String, Object> targetAchieveAnalysisList(TargetAchieveAnalysisDTO targetAchieveAnalysisDTO);

    /**
     * 查询关键经营指标排行榜列表
     *
     * @param targetLeaderboardDTO 关键经营指标排行榜
     * @return Map
     */
    Map<String, Object> targetLeaderboardList(TargetLeaderboardDTO targetLeaderboardDTO);

    /**
     * 关键经营指标排行榜下拉列表
     *
     * @return Map
     */
    Map<String, Object> targetLeaderboardDropList();
}
