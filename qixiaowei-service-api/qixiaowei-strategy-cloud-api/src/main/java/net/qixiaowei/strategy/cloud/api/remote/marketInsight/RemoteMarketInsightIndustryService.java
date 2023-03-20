package net.qixiaowei.strategy.cloud.api.remote.marketInsight;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightIndustryDTO;
import net.qixiaowei.strategy.cloud.api.factory.businessDesign.RemoteBusinessDesignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 看行业远程调用
 */
@FeignClient(contextId = "remoteMarketInsightIndustryService", value = ServiceNameConstants.STRATEGY_CLOUD_SERVICE, fallbackFactory = RemoteBusinessDesignFallbackFactory.class)
public interface RemoteMarketInsightIndustryService {

    String API_MARKET_INSIGHT_INDUSTRY = "/marketInsightIndustry";


    /**
     * 远程查询看行业列表是否被引用
     */
    @PostMapping(API_MARKET_INSIGHT_INDUSTRY + "/remoteMarketInsightIndustryList")
    R<List<MarketInsightIndustryDTO>> remoteMarketInsightIndustryList(@RequestBody MarketInsightIndustryDTO marketInsightIndustryDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
