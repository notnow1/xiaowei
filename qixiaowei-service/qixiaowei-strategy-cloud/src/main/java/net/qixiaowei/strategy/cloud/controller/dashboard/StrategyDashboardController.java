package net.qixiaowei.strategy.cloud.controller.dashboard;

import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.dashboard.StrategyDashboardDTO;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightIndustryDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentDTO;
import net.qixiaowei.strategy.cloud.mapper.industry.IndustryAttractionElementMapper;
import net.qixiaowei.strategy.cloud.mapper.industry.IndustryAttractionMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MarketInsightIndustryMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiIndustryAttractionMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiIndustryDetailMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiIndustryEstimateMapper;
import net.qixiaowei.strategy.cloud.service.dashboard.IStrategyDashboardService;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;


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
    @Autowired
    private IMarketInsightIndustryService marketInsightIndustryService;

    //==============================战略云仪表盘==================================//

    /**
     * 战略意图仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     */
    @RequiresPermissions("strategy:cloud:dashboard:strategyMap")
    @PostMapping("/strategyIntent")
    public AjaxResult dashboardStrategyIntent(@RequestBody @Validated(StrategyDashboardDTO.QueryStrategyIntentDTO.class) StrategyDashboardDTO strategyDashboardDTO) {
        StrategyIntentDTO strategyIntentDTO = new StrategyIntentDTO();
         strategyIntentDTO = iStrategyDashboardService.dashboardStrategyIntent(strategyDashboardDTO);
        return AjaxResult.success(strategyIntentDTO);
    }

    /**
     * 看行业仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     */
    @RequiresPermissions("strategy:cloud:dashboard:strategyMap")
    @PostMapping("/miIndustryDetailList")
    public AjaxResult targetAchieveAnalysisList(@RequestBody @Validated(StrategyDashboardDTO.QueryStrategyIntentDTO.class) StrategyDashboardDTO strategyDashboardDTO) {
        return AjaxResult.success(iStrategyDashboardService.dashboardMiIndustryDetailList(strategyDashboardDTO));
    }
    /**
     * 看行业仪表盘下拉框
     */
    @RequiresPermissions("strategy:cloud:dashboard:strategyMap")
    @GetMapping("/miIndustryDetailBoxList")
    public AjaxResult miIndustryDetailBoxList(MarketInsightIndustryDTO marketInsightIndustryDTO) {
        //根据属性去重
        ArrayList<MarketInsightIndustryDTO> marketInsightIndustryDTOArrayList = new ArrayList<>();
        List<MarketInsightIndustryDTO> list = marketInsightIndustryService.selectMarketInsightIndustryList(marketInsightIndustryDTO);
        if (StringUtils.isNotEmpty(list)){
            //根据属性去重
            marketInsightIndustryDTOArrayList = list.stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(
                                    MarketInsightIndustryDTO::getPlanBusinessUnitName))), ArrayList::new));
        }


        return AjaxResult.success(marketInsightIndustryDTOArrayList);
    }

    /**
     * 业务设计九宫格仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     */
    @RequiresPermissions("strategy:cloud:dashboard:strategyMap")
    @PostMapping("/businessDesignList")
    public AjaxResult targetLeaderboardList(@RequestBody @Validated(StrategyDashboardDTO.QueryStrategyIntentDTO.class) StrategyDashboardDTO strategyDashboardDTO) {
        return AjaxResult.success(iStrategyDashboardService.dashboardTargetLeaderboardList(strategyDashboardDTO));
    }

    /**
     * 业务设计九宫格仪表盘查询下拉最近一次数据
     */
    @RequiresPermissions("strategy:cloud:dashboard:strategyMap")
    @GetMapping("/recentBusinessDesignList")
    public AjaxResult recentBusinessDesignList(@RequestParam("planYear") Integer planYear) {
        return AjaxResult.success(iStrategyDashboardService.recentBusinessDesignList(planYear));
    }

}
