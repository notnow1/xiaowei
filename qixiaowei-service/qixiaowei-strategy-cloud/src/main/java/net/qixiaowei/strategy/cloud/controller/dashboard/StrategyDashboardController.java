package net.qixiaowei.strategy.cloud.controller.dashboard;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.dashboard.StrategyDashboardDTO;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentDTO;
import net.qixiaowei.strategy.cloud.mapper.industry.IndustryAttractionElementMapper;
import net.qixiaowei.strategy.cloud.mapper.industry.IndustryAttractionMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MarketInsightIndustryMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiIndustryAttractionMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiIndustryDetailMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiIndustryEstimateMapper;
import net.qixiaowei.strategy.cloud.service.dashboard.IStrategyDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
    public AjaxResult dashboardStrategyIntent(@RequestBody @Validated(StrategyDashboardDTO.QueryStrategyIntentDTO.class) StrategyDashboardDTO strategyDashboardDTO) {
        StrategyIntentDTO strategyIntentDTO = iStrategyDashboardService.dashboardStrategyIntent(strategyDashboardDTO);
        return AjaxResult.success(strategyIntentDTO);
    }

    /**
     * 看行业仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     */
    @RequiresPermissions("strategy:cloud:dashboard:miIndustryDetailList")
    @PostMapping("/miIndustryDetailList")
    public AjaxResult targetAchieveAnalysisList(@RequestBody @Validated(StrategyDashboardDTO.QueryStrategyIntentDTO.class) StrategyDashboardDTO strategyDashboardDTO) {
        return AjaxResult.success(iStrategyDashboardService.dashboardMiIndustryDetailList(strategyDashboardDTO));
    }


    /**
     * 业务设计九宫格仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     */
    @RequiresPermissions("strategy:cloud:dashboard:businessDesign")
    @PostMapping("/businessDesignList")
    public AjaxResult targetLeaderboardList(@RequestBody @Validated(StrategyDashboardDTO.QueryStrategyIntentDTO.class) StrategyDashboardDTO strategyDashboardDTO) {
        return AjaxResult.success(iStrategyDashboardService.dashboardTargetLeaderboardList(strategyDashboardDTO));
    }

    /**
     * 业务设计九宫格仪表盘查询下拉最近一次数据
     */
    @RequiresPermissions("strategy:cloud:dashboard:businessDesign")
    @GetMapping("/recentBusinessDesignList")
    public AjaxResult recentBusinessDesignList(@RequestParam("planYear") Integer planYear) {
        return AjaxResult.success(iStrategyDashboardService.recentBusinessDesignList(planYear));
    }

}
