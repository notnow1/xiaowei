package net.qixiaowei.operate.cloud.controller.dashboard;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveAnalysisDTO;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveRateDTO;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetLeaderboardDTO;
import net.qixiaowei.operate.cloud.service.dashboard.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author Graves
 * @since 2022-11-26
 */
@RestController
@RequestMapping("dashboard")
public class DashboardController extends BaseController {


    @Autowired
    private IDashboardService dashboardService;

    //==============================关键经营仪表盘==================================//

    /**
     * 查询奖金预算表列表
     */
    @RequiresPermissions("operate:cloud:dashboard:target")
    @PostMapping("/targetAchieveRate/list")
    public AjaxResult targetAchieveRateList(@RequestBody TargetAchieveRateDTO targetAchieveRateDTO) {
        List<TargetAchieveRateDTO> list = dashboardService.targetAchieveRateList(targetAchieveRateDTO);
        return AjaxResult.success(list);
    }

    /**
     * 关键经营指标月度达成分析列表
     */
    @RequiresPermissions("operate:cloud:dashboard:target")
    @PostMapping("/targetAchieveAnalysis/list")
    public AjaxResult targetAchieveAnalysisList(@RequestBody TargetAchieveAnalysisDTO targetAchieveAnalysisDTO) {
        return AjaxResult.success(dashboardService.targetAchieveAnalysisList(targetAchieveAnalysisDTO));
    }

    /**
     * 关键经营指标排行榜
     */
    @RequiresPermissions("operate:cloud:dashboard:target")
    @PostMapping("/targetLeaderboard/list")
    public AjaxResult targetLeaderboardList(@RequestBody TargetLeaderboardDTO targetLeaderboardDTO) {
        return AjaxResult.success(dashboardService.targetLeaderboardList(targetLeaderboardDTO));
    }

    /**
     * 目标达成获取指标下拉列表
     */
    @RequiresPermissions("operate:cloud:dashboard:target")
    @GetMapping("/targetAchieveAnalysis/getDropList")
    public AjaxResult getIndicatorList() {
        return AjaxResult.success(dashboardService.getDropList());
    }

    /**
     * 根据指标ID获取分解维度下拉框
     */
    @RequiresPermissions("operate:cloud:dashboard:target")
    @GetMapping("/targetLeaderboard/getTargetList")
    public AjaxResult getTargetList(@RequestParam("indicatorId") Long indicatorId) {
        return AjaxResult.success(dashboardService.targetDropList(indicatorId));
    }

    /**
     * 获取时间维度下拉框
     */
    @RequiresPermissions("operate:cloud:dashboard:target")
    @GetMapping("/targetLeaderboard/getTimeDropList")
    public AjaxResult getTimeDropList(@RequestParam("indicatorId") Long indicatorId, @RequestParam("targetDecomposeDimensionId") Long targetDecomposeDimensionId) {
        return AjaxResult.success(dashboardService.timeDropList(indicatorId, targetDecomposeDimensionId));
    }

    /**
     * 最近一次的分解维度信息
     */
    @RequiresPermissions("operate:cloud:dashboard:target")
    @GetMapping("/targetLeaderboard/getLastTimeDecompose")
    public AjaxResult getLastTimeDecompose() {
        return AjaxResult.success(dashboardService.getLastTimeDecompose());
    }

}
