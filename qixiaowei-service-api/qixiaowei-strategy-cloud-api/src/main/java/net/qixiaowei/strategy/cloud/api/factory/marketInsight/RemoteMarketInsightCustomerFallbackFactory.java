package net.qixiaowei.strategy.cloud.api.factory.marketInsight;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightCustomerDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiCustomerChoiceDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiCustomerInvestDetailDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiCustomerInvestPlanDTO;
import net.qixiaowei.strategy.cloud.api.remote.businessDesign.RemoteBusinessDesignService;
import net.qixiaowei.strategy.cloud.api.remote.marketInsight.RemoteMarketInsightCustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * 看客户
 */
public class RemoteMarketInsightCustomerFallbackFactory implements FallbackFactory<RemoteMarketInsightCustomerService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteMarketInsightCustomerFallbackFactory.class);

    @Override
    public RemoteMarketInsightCustomerService create(Throwable throwable) {
        log.error("看客户远程服务调用失败:{}", throwable.getMessage());
        return new RemoteMarketInsightCustomerService() {

            @Override
            public R<List<MarketInsightCustomerDTO>> remoteMarketInsightCustomerList(MarketInsightCustomerDTO marketInsightCustomerDTO, String source) {
                return R.fail("远程调用看客户列表失败:" + throwable.getMessage());
            }

            @Override
            public R<List<MiCustomerInvestDetailDTO>> remoteMiCustomerInvestDetailList(MiCustomerInvestDetailDTO miCustomerInvestDetailDTO, String source) {
                return R.fail("远程查询看客户投资计划详情是否被引用失败:" + throwable.getMessage());
            }

            @Override
            public R<List<MiCustomerChoiceDTO>> remoteMiCustomerChoiceList(MiCustomerChoiceDTO miCustomerChoiceDTO, String source) {
                return R.fail("远程查询看市场洞察客户选择集合是否被引用失败:" + throwable.getMessage());
            }

            @Override
            public R<List<MiCustomerInvestPlanDTO>> remoteMiCustomerInvestPlanList(MiCustomerInvestPlanDTO miCustomerInvestPlanDTO, String source) {
                return R.fail("远程查询市场洞察客户投资计划集合是否被引用失败:" + throwable.getMessage());
            }
        };
    }
}
