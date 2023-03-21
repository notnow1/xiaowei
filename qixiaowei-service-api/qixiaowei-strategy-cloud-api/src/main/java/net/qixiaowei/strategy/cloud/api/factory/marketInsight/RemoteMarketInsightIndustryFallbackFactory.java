package net.qixiaowei.strategy.cloud.api.factory.marketInsight;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightIndustryDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiIndustryDetailDTO;
import net.qixiaowei.strategy.cloud.api.remote.marketInsight.RemoteMarketInsightIndustryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * 看行业
 */
public class RemoteMarketInsightIndustryFallbackFactory implements FallbackFactory<RemoteMarketInsightIndustryService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteMarketInsightIndustryFallbackFactory.class);

    @Override
    public RemoteMarketInsightIndustryService create(Throwable throwable) {
        log.error("看行业远程服务调用失败:{}", throwable.getMessage());
        return new RemoteMarketInsightIndustryService() {

            @Override
            public R<List<MarketInsightIndustryDTO>> remoteMarketInsightIndustryList(MarketInsightIndustryDTO marketInsightIndustryDTO, String source) {
                return R.fail("远程调用看行业列表是否引用失败:" + throwable.getMessage());
            }

            @Override
            public R<List<MiIndustryDetailDTO>> remoteMiIndustryDetailList(MiIndustryDetailDTO miIndustryDetailDTO, String source) {
                return R.fail("远程查询市场洞察行业详情是否被引用失败:" + throwable.getMessage());
            }
        };
    }
}
