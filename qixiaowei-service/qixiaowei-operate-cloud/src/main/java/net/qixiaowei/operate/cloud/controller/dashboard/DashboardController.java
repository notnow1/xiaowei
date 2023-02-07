package net.qixiaowei.operate.cloud.controller.dashboard;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveAnalysisDTO;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveRateDTO;
import net.qixiaowei.operate.cloud.service.dashboard.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/targetAchieveRate/list")
    public AjaxResult targetAchieveRateList(Integer targetYear) {
        List<TargetAchieveRateDTO> list = dashboardService.targetAchieveRateList(targetYear);
        return AjaxResult.success(list);
    }

    /**
     * 目标达成获取指标列表
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
    @GetMapping("/targetAchieveAnalysis/list")
    public AjaxResult targetAchieveAnalysisList(TargetAchieveAnalysisDTO targetAchieveAnalysisDTO) {
        return AjaxResult.success(dashboardService.targetAchieveAnalysisList(targetAchieveAnalysisDTO));
    }

}
