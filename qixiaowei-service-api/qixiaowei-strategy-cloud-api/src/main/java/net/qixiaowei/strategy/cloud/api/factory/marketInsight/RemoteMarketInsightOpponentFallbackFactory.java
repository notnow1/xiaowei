package net.qixiaowei.strategy.cloud.api.factory.marketInsight;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightOpponentDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiOpponentChoiceDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiOpponentFinanceDTO;
import net.qixiaowei.strategy.cloud.api.remote.marketInsight.RemoteMarketInsightOpponentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * 看对手
 */
public class RemoteMarketInsightOpponentFallbackFactory implements FallbackFactory<RemoteMarketInsightOpponentService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteMarketInsightOpponentFallbackFactory.class);

    @Override
    public RemoteMarketInsightOpponentService create(Throwable throwable) {
        log.error("看对手远程服务调用失败:{}", throwable.getMessage());
        return new RemoteMarketInsightOpponentService() {

            @Override
            public R<List<MarketInsightOpponentDTO>> remoteMarketInsightOpponentList(MarketInsightOpponentDTO marketInsightOpponentDTO, String source) {
                return R.fail("远程调用看对手列表是否被引用失败:" + throwable.getMessage());
            }

            @Override
            public R<List<MiOpponentFinanceDTO>> remoteMiOpponentFinanceList(MiOpponentFinanceDTO miOpponentFinanceDTO, String source) {
                return R.fail("看对手竞争对手财务详情远程查询列表是否被引用失败:" + throwable.getMessage());
            }

            @Override
            public R<List<MiOpponentChoiceDTO>> remoteMiOpponentChoiceList(MiOpponentChoiceDTO miOpponentChoiceDTO, String source) {
                return R.fail("市场洞察对手选择远程查询列表是否被引用失败:" + throwable.getMessage());
            }
        };
    }
}
