package net.qixiaowei.strategy.cloud.api.factory.strategyDecode;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDetailDTO;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteStrategyMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;


public class RemoteStrategyMetricsFallbackFactory implements FallbackFactory<RemoteStrategyMetricsService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteStrategyMetricsFallbackFactory.class);

    @Override
    public RemoteStrategyMetricsService create(Throwable throwable) {
        log.error("战略衡量指标远程服务调用失败:{}", throwable.getMessage());
        return new RemoteStrategyMetricsService() {

            @Override
            public R<List<StrategyMetricsDTO>> remoteStrategyMetrics(StrategyMetricsDTO strategyMetricsDTO, String source) {
                return R.fail("远程调用战略衡量指标列表失败:" + throwable.getMessage());
            }

            @Override
            public R<List<StrategyMetricsDetailDTO>> remoteListByIndicator(StrategyMetricsDTO strategyMetricsDTO, String source) {
                return R.fail("远程根据指标ID集合查找战略衡量指标失败:" + throwable.getMessage());
            }
        };
    }
}
