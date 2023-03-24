package net.qixiaowei.strategy.cloud.api.remote;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.factory.RemoteStrategyCloudInitDataFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(contextId = "remoteStrategyCloudInitDataService", value = ServiceNameConstants.STRATEGY_CLOUD_SERVICE, fallbackFactory = RemoteStrategyCloudInitDataFallbackFactory.class)
public interface RemoteStrategyCloudInitDataService {
    /**
     * 初始化数据
     */
    @PostMapping("/strategy-cloud/initData")
    R<Boolean> initData(@RequestParam("userId") Long userId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}