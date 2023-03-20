package net.qixiaowei.strategy.cloud.api.remote.marketInsight;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightOpponentDTO;
import net.qixiaowei.strategy.cloud.api.factory.businessDesign.RemoteBusinessDesignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 看对手远程调用
 */
@FeignClient(contextId = "remoteMarketInsightOpponentService", value = ServiceNameConstants.STRATEGY_CLOUD_SERVICE, fallbackFactory = RemoteBusinessDesignFallbackFactory.class)
public interface RemoteMarketInsightOpponentService {

    String API_MARKET_INSIGHT_OPPONENT = "/marketInsightOpponent";


    /**
     *看对手远程查询列表是否被引用
     */
    @PostMapping(API_MARKET_INSIGHT_OPPONENT + "/remoteMarketInsightOpponentList")
    R<List<MarketInsightOpponentDTO>> remoteMarketInsightOpponentList(@RequestBody MarketInsightOpponentDTO marketInsightOpponentDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
