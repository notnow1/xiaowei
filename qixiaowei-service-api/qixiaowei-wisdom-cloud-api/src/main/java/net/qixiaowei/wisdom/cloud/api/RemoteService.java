package net.qixiaowei.wisdom.cloud.api;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.wisdom.cloud.api.factory.RemoteFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * 服务
 */
@FeignClient(contextId = "remoteWisdomCloudService", value = ServiceNameConstants.WISDOM_CLOUD_SERVICE, fallbackFactory = RemoteFallbackFactory.class)
public interface RemoteService {


}
