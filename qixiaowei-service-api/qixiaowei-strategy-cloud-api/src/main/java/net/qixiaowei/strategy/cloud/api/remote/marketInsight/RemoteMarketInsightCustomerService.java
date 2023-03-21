package net.qixiaowei.strategy.cloud.api.remote.marketInsight;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightCustomerDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiCustomerChoiceDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiCustomerInvestDetailDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiCustomerInvestPlanDTO;
import net.qixiaowei.strategy.cloud.api.factory.businessDesign.RemoteBusinessDesignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 看客户远程调用
 */
@FeignClient(contextId = "remoteMarketInsightCustomerService", value = ServiceNameConstants.STRATEGY_CLOUD_SERVICE, fallbackFactory = RemoteBusinessDesignFallbackFactory.class)
public interface RemoteMarketInsightCustomerService {

    String API_MARKET_INSIGHT_CUSTOMER = "/marketInsightCustomer";


    /**
     * 看客户远程查询是否被引用
     */
    @PostMapping(API_MARKET_INSIGHT_CUSTOMER + "/remoteMarketInsightCustomerList")
    R<List<MarketInsightCustomerDTO>> remoteMarketInsightCustomerList(@RequestBody MarketInsightCustomerDTO marketInsightCustomerDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 远程查询看客户投资计划详情是否被引用（产品）
     */
    @PostMapping(API_MARKET_INSIGHT_CUSTOMER + "/remoteMiCustomerInvestDetailList")
    R<List<MiCustomerInvestDetailDTO>> remoteMiCustomerInvestDetailList(@RequestBody MiCustomerInvestDetailDTO miCustomerInvestDetailDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 远程查询看市场洞察客户选择集合是否被引用(行业)
     */
    @PostMapping(API_MARKET_INSIGHT_CUSTOMER + "/remoteMiCustomerChoiceList")
    R<List<MiCustomerChoiceDTO>> remoteMiCustomerChoiceList(@RequestBody MiCustomerChoiceDTO miCustomerChoiceDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 远程查询市场洞察客户投资计划集合是否被引用（行业）
     */
    @PostMapping(API_MARKET_INSIGHT_CUSTOMER + "/remoteMiCustomerInvestPlanList")
    R<List<MiCustomerInvestPlanDTO>> remoteMiCustomerInvestPlanList(@RequestBody MiCustomerInvestPlanDTO miCustomerInvestPlanDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
