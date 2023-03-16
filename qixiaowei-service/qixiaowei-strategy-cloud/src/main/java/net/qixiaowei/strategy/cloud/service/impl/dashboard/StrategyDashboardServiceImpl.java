package net.qixiaowei.strategy.cloud.service.impl.dashboard;


import net.qixiaowei.strategy.cloud.api.dto.dashboard.StrategyDashboardDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentOperateDTO;
import net.qixiaowei.strategy.cloud.service.dashboard.IStrategyDashboardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyDashboardServiceImpl implements IStrategyDashboardService {


    /**
     * 战略意图仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     * @param strategyDashboardDTO
     * @return
     */
    @Override
    public List<StrategyIntentOperateDTO> dashboardStrategyIntent(StrategyDashboardDTO strategyDashboardDTO) {
        return null;
    }

    /**
     * 看行业仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     * @param strategyDashboardDTO
     * @return
     */
    @Override
    public Object dashboardMiIndustryDetailList(StrategyDashboardDTO strategyDashboardDTO) {
        return null;
    }

    /**
     * 业务设计九宫格仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     * @param strategyDashboardDTO
     * @return
     */
    @Override
    public Object dashboardTargetLeaderboardList(StrategyDashboardDTO strategyDashboardDTO) {
        return null;
    }
}
