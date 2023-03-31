package net.qixiaowei.strategy.cloud.api.factory.marketInsight;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightSelfDTO;
import net.qixiaowei.strategy.cloud.api.remote.marketInsight.RemoteMarketInsightSelfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * 看自身
 */
public class RemoteMarketInsightSelfFallbackFactory implements FallbackFactory<RemoteMarketInsightSelfService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteMarketInsightSelfFallbackFactory.class);

    @Override
    public RemoteMarketInsightSelfService create(Throwable throwable) {
        log.error("看自身远程服务调用失败:{}", throwable.getMessage());
        return new RemoteMarketInsightSelfService() {

            @Override
            public R<List<MarketInsightSelfDTO>> remoteMarketInsightSelfList(MarketInsightSelfDTO marketInsightSelfDTO, String source) {
                return R.fail("看自身远程调用列表查询是否被引用失败:" + throwable.getMessage());
            }
        };
    }
}
