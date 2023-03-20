package net.qixiaowei.strategy.cloud.api.remote.marketInsight;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightCustomerDTO;
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
     * 战略衡量指标列表
     */
    @PostMapping(API_MARKET_INSIGHT_CUSTOMER + "/remoteMarketInsightCustomerList")
    R<List<MarketInsightCustomerDTO>> remoteMarketInsightCustomerList(@RequestBody MarketInsightCustomerDTO marketInsightCustomerDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
