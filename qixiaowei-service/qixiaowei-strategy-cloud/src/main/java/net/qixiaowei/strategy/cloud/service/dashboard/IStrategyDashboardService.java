package net.qixiaowei.strategy.cloud.service.dashboard;

import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.dashboard.StrategyDashboardDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightIndustryDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentDTO;

import java.util.Map;

/**
 * IDashboardService接口
 *
 * @author Graves
 * @since 2022-10-27
 */
public interface IStrategyDashboardService {

    /**
     * 战略意图仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     *
     * @param strategyDashboardDTO
     * @return
     */
    StrategyIntentDTO dashboardStrategyIntent(StrategyDashboardDTO strategyDashboardDTO);

    /**
     * 看行业仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     *
     * @param strategyDashboardDTO
     * @return
     */
    MarketInsightIndustryDTO dashboardMiIndustryDetailList(StrategyDashboardDTO strategyDashboardDTO);

    /**
     * 业务设计九宫格仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     *
     * @param strategyDashboardDTO 业务设计
     * @return 业务设计
     */
    BusinessDesignDTO dashboardTargetLeaderboardList(StrategyDashboardDTO strategyDashboardDTO);

    /**
     * 最近一次的业务设计仪表盘列表
     *
     * @param planYear 规划年份
     * @return 结果
     */
    Map<String, Object> recentBusinessDesignList(Integer planYear);
}
