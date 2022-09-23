package net.qixiaowei.strategy.cloud.api;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.strategy.cloud.api.factory.RemoteFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * 服务
 */
@FeignClient(contextId = "remoteStrategyCloudService", value = ServiceNameConstants.STRATEGY_CLOUD_SERVICE, fallbackFactory = RemoteFallbackFactory.class)
public interface RemoteService {


}
