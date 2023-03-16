package net.qixiaowei.strategy.cloud.controller.dashboard;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveAnalysisDTO;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveRateDTO;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetLeaderboardDTO;
import net.qixiaowei.strategy.cloud.api.dto.dashboard.StrategyDashboardDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentOperateDTO;
import net.qixiaowei.strategy.cloud.service.dashboard.IStrategyDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author Graves
 * @since 2022-11-26
 */
@RestController
@Validated
@RequestMapping("dashboard")
public class StrategyDashboardController extends BaseController {


    @Autowired
    private IStrategyDashboardService iStrategyDashboardService;

    //==============================战略云仪表盘==================================//

    /**
     * 战略意图仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     */
    @RequiresPermissions("strategy:cloud:dashboard:strategyIntent")
    @PostMapping("/strategyIntent")
    public AjaxResult dashboardStrategyIntent(@RequestBody StrategyDashboardDTO strategyDashboardDTO) {
        List<StrategyIntentOperateDTO> list = iStrategyDashboardService.dashboardStrategyIntent(strategyDashboardDTO);
        return AjaxResult.success(list);
    }

    /**
     * 看行业仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     */
    @RequiresPermissions("strategy:cloud:dashboard:miIndustryDetailList")
    @PostMapping("/miIndustryDetailList")
    public AjaxResult targetAchieveAnalysisList(@RequestBody StrategyDashboardDTO strategyDashboardDTO) {
        return AjaxResult.success(iStrategyDashboardService.dashboardMiIndustryDetailList(strategyDashboardDTO));
    }


    /**
     * 业务设计九宫格仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     */
    @RequiresPermissions("strategy:cloud:dashboard:businessDesign")
    @PostMapping("/businessDesignList")
    public AjaxResult targetLeaderboardList(@RequestBody StrategyDashboardDTO strategyDashboardDTO) {
        return AjaxResult.success(iStrategyDashboardService.dashboardTargetLeaderboardList(strategyDashboardDTO));
    }
    
}
