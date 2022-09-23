package net.qixiaowei.talents.cloud.api;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.talents.cloud.api.factory.RemoteFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * 服务
 */
@FeignClient(contextId = "remoteTalentsCloudService", value = ServiceNameConstants.TALENTS_CLOUD_SERVICE, fallbackFactory = RemoteFallbackFactory.class)
public interface RemoteService {


}
