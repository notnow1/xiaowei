package net.qixiaowei.strategy.cloud.api.remote.marketInsight;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightMacroDTO;
import net.qixiaowei.strategy.cloud.api.factory.businessDesign.RemoteBusinessDesignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 看宏观远程调用
 */
@FeignClient(contextId = "remoteMarketInsightMacroService", value = ServiceNameConstants.STRATEGY_CLOUD_SERVICE, fallbackFactory = RemoteBusinessDesignFallbackFactory.class)
public interface RemoteMarketInsightMacroService {

    String API_MARKET_INSIGHT_MACRO = "/marketInsightMacro";


    /**
     * 看宏观远程调用列表查询是否被引用
     */
    @PostMapping(API_MARKET_INSIGHT_MACRO + "/remoteMarketInsightMacroList")
    R<List<MarketInsightMacroDTO>> remoteMarketInsightMacroList(@RequestBody MarketInsightMacroDTO marketInsightMacroDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
