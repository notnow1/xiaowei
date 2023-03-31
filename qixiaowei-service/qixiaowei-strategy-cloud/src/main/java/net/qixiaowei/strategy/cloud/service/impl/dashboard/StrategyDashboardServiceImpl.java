package net.qixiaowei.strategy.cloud.service.impl.dashboard;


import net.qixiaowei.integration.common.enums.strategy.PlanBusinessUnitCode;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightIndustry;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignAxisConfigDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.dashboard.StrategyDashboardDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightIndustryDTO;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentDTO;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MarketInsightIndustryMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyIntent.StrategyIntentMapper;
import net.qixiaowei.strategy.cloud.service.businessDesign.IBusinessDesignAxisConfigService;
import net.qixiaowei.strategy.cloud.service.businessDesign.IBusinessDesignParamService;
import net.qixiaowei.strategy.cloud.service.businessDesign.IBusinessDesignService;
import net.qixiaowei.strategy.cloud.service.dashboard.IStrategyDashboardService;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightIndustryService;
import net.qixiaowei.strategy.cloud.service.plan.IPlanBusinessUnitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private IBusinessDesignParamService businessDesignParamService;

    @Autowired
    private IBusinessDesignAxisConfigService businessDesignAxisConfigService;

    @Autowired
    private IPlanBusinessUnitService planBusinessUnitService;

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
     * @param strategyDashboardDTO 业务设计DTO
     * @return 结果
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
        Integer planYear = strategyDashboardDTO.getPlanYear();
        Long planBusinessUnitId = strategyDashboardDTO.getPlanBusinessUnitId();
        BusinessDesignDTO businessDesignDTO;
        if (StringUtils.isNull(planBusinessUnitId)) {// 查询最近一次
            businessDesignDTO = businessDesignService.selectBusinessDesignRecently(planYear);
            if (StringUtils.isNull(businessDesignDTO)) {
                businessDesignDTO = new BusinessDesignDTO();
                businessDesignDTO.setBusinessDesignParamDTOS(new ArrayList<>());
                businessDesignDTO.setBusinessDesignAxisConfigMap(new ArrayList<>());
                return businessDesignDTO;
            }
        } else {
            PlanBusinessUnitDTO planBusinessUnitDTO = planBusinessUnitService.selectPlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
            String businessUnitDecompose = planBusinessUnitDTO.getBusinessUnitDecompose();
            BusinessDesignDTO businessDesignDTOParams = new BusinessDesignDTO();
            if (businessUnitDecompose.equals("compare")) {
                businessDesignDTOParams.setPlanYear(planYear);
                businessDesignDTOParams.setPlanBusinessUnitId(planBusinessUnitId);
                businessDesignDTO = businessDesignService.selectBusinessDesignList(businessDesignDTOParams).get(0);
            } else {
                businessDesignDTOParams.setPlanYear(planYear);
                businessDesignDTOParams.setPlanBusinessUnitId(planBusinessUnitId);
                businessDesignDTOParams.setAreaId(strategyDashboardDTO.getAreaId());
                businessDesignDTOParams.setProductId(strategyDashboardDTO.getProductId());
                businessDesignDTOParams.setIndustryId(strategyDashboardDTO.getIndustryId());
                businessDesignDTOParams.setDepartmentId(strategyDashboardDTO.getDepartmentId());
                List<BusinessDesignDTO> businessDesignDTOS = businessDesignService.selectBusinessDesignList(businessDesignDTOParams);
                if (StringUtils.isEmpty(businessDesignDTOS)) {
                    businessDesignDTO = new BusinessDesignDTO();
                    businessDesignDTO.setBusinessDesignParamDTOS(new ArrayList<>());
                    businessDesignDTO.setBusinessDesignAxisConfigMap(new ArrayList<>());
                    return businessDesignDTO;
                } else {
                    businessDesignDTO = businessDesignDTOS.get(0);
                }
            }
        }
        Long businessDesignId = businessDesignDTO.getBusinessDesignId();
        List<BusinessDesignParamDTO> businessDesignParamDTOS = businessDesignParamService.selectBusinessDesignParamByBusinessDesignId(businessDesignId);
        // 综合毛利率综合订单额计算
        if (StringUtils.isNotEmpty(businessDesignParamDTOS)) {
            for (BusinessDesignParamDTO businessDesignParamDTO : businessDesignParamDTOS) {
                BigDecimal synthesizeGrossMargin = businessDesignParamDTO.getHistoryAverageRate().multiply(businessDesignParamDTO.getHistoryWeight().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                        .add(businessDesignParamDTO.getForecastRate().multiply(businessDesignParamDTO.getForecastWeight()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                businessDesignParamDTO.setSynthesizeGrossMargin(synthesizeGrossMargin);
                BigDecimal synthesizeOrderAmount = businessDesignParamDTO.getHistoryOrderAmount().multiply(businessDesignParamDTO.getHistoryOrderWeight().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                        .add(businessDesignParamDTO.getForecastOrderAmount().multiply(businessDesignParamDTO.getForecastOrderWeight()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                businessDesignParamDTO.setSynthesizeOrderAmount(synthesizeOrderAmount);
                Integer paramDimension = businessDesignParamDTO.getParamDimension();
                if (StringUtils.isNotNull(paramDimension)) {
                    switch (paramDimension) {
                        case 1:
                            businessDesignParamDTO.setParamDimensionName("产品");
                            break;
                        case 2:
                            businessDesignParamDTO.setParamDimensionName("客户");
                            break;
                        case 3:
                            businessDesignParamDTO.setParamDimensionName("区域");
                            break;
                    }
                }
            }
            businessDesignDTO.setBusinessDesignParamDTOS(businessDesignParamDTOS);
        } else {
            businessDesignDTO.setBusinessDesignParamDTOS(new ArrayList<>());
        }

        // 业务设计轴配置表
        List<BusinessDesignAxisConfigDTO> businessDesignAxisConfigDTOS = businessDesignAxisConfigService.selectBusinessDesignAxisConfigByBusinessDesignId(businessDesignId);
        if (StringUtils.isNotEmpty(businessDesignAxisConfigDTOS)) {
            Map<Integer, Map<Integer, List<BusinessDesignAxisConfigDTO>>> groupBusinessDesignAxisConfigDTOS = businessDesignAxisConfigDTOS.stream().
                    collect(Collectors.groupingBy(BusinessDesignAxisConfigDTO::getParamDimension, Collectors.groupingBy(BusinessDesignAxisConfigDTO::getCoordinateAxis)));
            List<Map<String, Object>> businessDesignAxisConfigMap = new ArrayList<>();
            for (Integer groupDesign : groupBusinessDesignAxisConfigDTOS.keySet()) {
                Map<String, Object> businessDesignAxisMap = new HashMap<>();
                businessDesignAxisMap.put("paramDimension", groupDesign);
                Map<Integer, List<BusinessDesignAxisConfigDTO>> groupLargeAxisConfigDTOS = groupBusinessDesignAxisConfigDTOS.get(groupDesign);
                for (Integer groupLargeDesign : groupLargeAxisConfigDTOS.keySet()) {
                    BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO = groupLargeAxisConfigDTOS.get(groupLargeDesign).get(0);
                    if (groupLargeDesign == 1) {
                        businessDesignAxisMap.put("upperValueX", businessDesignAxisConfigDTO.getUpperValue());
                        businessDesignAxisMap.put("lowerValueX", businessDesignAxisConfigDTO.getLowerValue());
                    } else {
                        businessDesignAxisMap.put("upperValueY", businessDesignAxisConfigDTO.getUpperValue());
                        businessDesignAxisMap.put("lowerValueY", businessDesignAxisConfigDTO.getLowerValue());
                    }
                }
                businessDesignAxisConfigMap.add(businessDesignAxisMap);
            }
            businessDesignDTO.setBusinessDesignAxisConfigMap(businessDesignAxisConfigMap);
        } else {
            businessDesignDTO.setBusinessDesignAxisConfigMap(new ArrayList<>());
        }
        return businessDesignDTO;
    }

    /**
     * 最近一次的业务设计仪表盘列表
     *
     * @param planYear 规划年份
     * @return 结果
     */
    @Override
    public Map<String, Object> recentBusinessDesignList(Integer planYear) {
        if (StringUtils.isNull(planYear)) {
            throw new ServiceException("请传入年份");
        }
        BusinessDesignDTO businessDesignDTO = businessDesignService.selectBusinessDesignRecently(planYear);
        Map<String, Object> recentMap = new HashMap<>();
        if (StringUtils.isNull(businessDesignDTO)) {
            recentMap.put("businessUnitDecomposes", new ArrayList<>());
            return recentMap;
        }
        List<Map<String, Object>> businessUnitDecomposes = PlanBusinessUnitCode.getDropList(businessDesignDTO.getBusinessUnitDecompose());
        recentMap.put("areaId", businessDesignDTO.getAreaId());
        recentMap.put("productId", businessDesignDTO.getProductId());
        recentMap.put("industryId", businessDesignDTO.getIndustryId());
        recentMap.put("departmentId", businessDesignDTO.getDepartmentId());
        recentMap.put("businessUnitDecomposes", businessUnitDecomposes);
        recentMap.put("planBusinessUnitId", businessDesignDTO.getPlanBusinessUnitId());
        return recentMap;
    }
}
