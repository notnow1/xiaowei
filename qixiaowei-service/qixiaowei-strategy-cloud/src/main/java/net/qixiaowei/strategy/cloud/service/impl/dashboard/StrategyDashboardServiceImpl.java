package net.qixiaowei.strategy.cloud.service.impl.dashboard;


import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightIndustry;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.dashboard.StrategyDashboardDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightIndustryDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentDTO;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MarketInsightIndustryMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyIntent.StrategyIntentMapper;
import net.qixiaowei.strategy.cloud.service.businessDesign.IBusinessDesignService;
import net.qixiaowei.strategy.cloud.service.dashboard.IStrategyDashboardService;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightIndustryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyDashboardServiceImpl implements IStrategyDashboardService {

    @Autowired
    private StrategyIntentMapper strategyIntentMapper;
    @Autowired
    private MarketInsightIndustryMapper marketInsightIndustryMapper;
    @Autowired
    private IMarketInsightIndustryService marketInsightIndustryService;
    @Autowired
    private IBusinessDesignService businessDesignService;

    /**
     * 战略意图仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     *
     * @param strategyDashboardDTO
     * @return
     */
    @Override
    public StrategyIntentDTO dashboardStrategyIntent(StrategyDashboardDTO strategyDashboardDTO) {
        return strategyIntentMapper.selectStrategyIntentByPlanYear(strategyDashboardDTO.getPlanYear());
    }

    /**
     * 看行业仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     *
     * @param strategyDashboardDTO
     * @return
     */
    @Override
    public MarketInsightIndustryDTO dashboardMiIndustryDetailList(StrategyDashboardDTO strategyDashboardDTO) {
        MarketInsightIndustryDTO marketInsightIndustryDTOData = new MarketInsightIndustryDTO();
        Long planBusinessUnitId = strategyDashboardDTO.getPlanBusinessUnitId();
        MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
        if (StringUtils.isNull(planBusinessUnitId)) {
            marketInsightIndustry.setPlanYear(strategyDashboardDTO.getPlanYear());
            MarketInsightIndustryDTO marketInsightIndustryDTO = marketInsightIndustryMapper.dashboardMiIndustryDetailList(marketInsightIndustry);
            if (StringUtils.isNotNull(marketInsightIndustryDTO)) {
                marketInsightIndustryDTOData = marketInsightIndustryService.selectMarketInsightIndustryByMarketInsightIndustryId(marketInsightIndustryDTO.getMarketInsightIndustryId());
            }
        } else {
            BeanUtils.copyProperties(strategyDashboardDTO, marketInsightIndustry);
            List<MarketInsightIndustryDTO> marketInsightIndustryDTOS = marketInsightIndustryMapper.selectMarketInsightIndustryList(marketInsightIndustry);
            if (StringUtils.isNotEmpty(marketInsightIndustryDTOS)) {
                marketInsightIndustryDTOData = marketInsightIndustryService.selectMarketInsightIndustryByMarketInsightIndustryId(marketInsightIndustryDTOS.get(0).getMarketInsightIndustryId());
            }
        }

        return marketInsightIndustryDTOData;
    }

    /**
     * 业务设计九宫格仪表盘查询(无数据返回空)第一次进来返回最近一次数据
     *
     * @param strategyDashboardDTO 业务设计
     * @return 业务设计
     */
    @Override
    public BusinessDesignDTO dashboardTargetLeaderboardList(StrategyDashboardDTO strategyDashboardDTO) {
        Long businessDesignId = strategyDashboardDTO.getBusinessDesignId();
        BusinessDesignDTO businessDesignDTO;
        if (StringUtils.isNull(businessDesignId)) {// 查询最近一次
            businessDesignDTO = businessDesignService.selectBusinessDesignRecently();
            if (StringUtils.isNull(businessDesignDTO)) {
                return null;
            }
        } else {
            businessDesignDTO = businessDesignService.selectBusinessDesignByBusinessDesignId(businessDesignId);
        }
        

        return null;
    }
}
