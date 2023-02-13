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


/**
 * @author Graves
 * @since 2022-11-26
 */
@RestController
@RequestMapping("dashboard")
public class DashboardController extends BaseController {


    @Autowired
    private IDashboardService dashboardService;

    /**
     * 查询奖金预算表列表
     */
    @RequiresPermissions("operate:cloud:dashboard:list")
    @PostMapping("/targetAchieveRate/list")
    public AjaxResult targetAchieveRateList(@RequestBody TargetAchieveRateDTO targetAchieveRateDTO) {
        List<TargetAchieveRateDTO> list = dashboardService.targetAchieveRateList(targetAchieveRateDTO);
        return AjaxResult.success(list);
    }

    /**
     * 目标达成获取下拉列表
     */
    @RequiresPermissions("operate:cloud:dashboard:list")
    @GetMapping("/targetAchieveAnalysis/getDropList")
    public AjaxResult getIndicatorList() {
        return AjaxResult.success(dashboardService.getDropList());
    }

    /**
     * 关键经营指标月度达成分析列表
     */
    @RequiresPermissions("operate:cloud:dashboard:list")
    @PostMapping("/targetAchieveAnalysis/list")
    public AjaxResult targetAchieveAnalysisList(@RequestBody TargetAchieveAnalysisDTO targetAchieveAnalysisDTO) {
        return AjaxResult.success(dashboardService.targetAchieveAnalysisList(targetAchieveAnalysisDTO));
    }

    /**
     * 关键经营指标排行榜
     */
    @RequiresPermissions("operate:cloud:dashboard:list")
    @PostMapping("/targetLeaderboard/list")
    public AjaxResult targetLeaderboardList(@RequestBody TargetLeaderboardDTO targetLeaderboardDTO) {
        return AjaxResult.success(dashboardService.targetLeaderboardList(targetLeaderboardDTO));
    }

    /**
     * 关键经营指标排行榜下拉列表
     */
    @RequiresPermissions("operate:cloud:dashboard:list")
    @GetMapping("/targetLeaderboard/getDropList")
    public AjaxResult targetLeaderboardDropList() {
        return AjaxResult.success(dashboardService.targetLeaderboardDropList());
    }

}
