package net.qixiaowei.strategy.cloud.api.factory.marketInsight;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightMacroDTO;
import net.qixiaowei.strategy.cloud.api.remote.marketInsight.RemoteMarketInsightMacroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * 看宏观
 */
public class RemoteMarketInsightMacroFallbackFactory implements FallbackFactory<RemoteMarketInsightMacroService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteMarketInsightMacroFallbackFactory.class);

    @Override
    public RemoteMarketInsightMacroService create(Throwable throwable) {
        log.error("看宏观远程服务调用失败:{}", throwable.getMessage());
        return new RemoteMarketInsightMacroService() {


            @Override
            public R<List<MarketInsightMacroDTO>> remoteMarketInsightMacroList(MarketInsightMacroDTO marketInsightMacroDTO, String source) {
                return R.fail("远程调用看宏观列表是否引用失败:" + throwable.getMessage());
            }
        };
    }
}
