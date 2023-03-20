package net.qixiaowei.strategy.cloud.api.remote.strategyDecode;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDetailDTO;
import net.qixiaowei.strategy.cloud.api.factory.industry.RemoteIndustryAttractionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 规划业务单元远程调用
 */
@FeignClient(contextId = "remoteStrategyMetricsService", value = ServiceNameConstants.STRATEGY_CLOUD_SERVICE, fallbackFactory = RemoteIndustryAttractionFallbackFactory.class)
public interface RemoteStrategyMetricsService {

    String API_PREFIX_STRATEGY_METRICS = "/strategyMetrics";


    /**
     * 战略衡量指标列表
     */
    @PostMapping(API_PREFIX_STRATEGY_METRICS + "/remoteStrategyMetrics")
    R<List<StrategyMetricsDTO>> remoteStrategyMetrics(@RequestBody StrategyMetricsDTO strategyMetricsDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 远程根据指标ID集合查找战略衡量指标
     *
     * @param strategyMetricsDTO 战略衡量指标
     * @param source             根源
     * @return 结果
     */
    @PostMapping(API_PREFIX_STRATEGY_METRICS + "/remoteListByIndicator")
    R<List<StrategyMetricsDetailDTO>> remoteListByIndicator(@RequestBody StrategyMetricsDTO strategyMetricsDTO, String source);
}
