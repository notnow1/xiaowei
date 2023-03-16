package net.qixiaowei.strategy.cloud.service.dashboard;

import net.qixiaowei.strategy.cloud.api.dto.dashboard.StrategyDashboardDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentOperateDTO;

import java.util.List;

/**
 * IDashboardService接口
 *
 * @author Graves
 * @since 2022-10-27
 */
public interface IStrategyDashboardService {

    /**
     * 战略意图仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     * @param strategyDashboardDTO
     * @return
     */
    List<StrategyIntentOperateDTO> dashboardStrategyIntent(StrategyDashboardDTO strategyDashboardDTO);

    /**
     * 看行业仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     * @param strategyDashboardDTO
     * @return
     */
    Object dashboardMiIndustryDetailList(StrategyDashboardDTO strategyDashboardDTO);

    /**
     * 业务设计九宫格仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     * @param strategyDashboardDTO
     * @return
     */
    Object dashboardTargetLeaderboardList(StrategyDashboardDTO strategyDashboardDTO);
}
