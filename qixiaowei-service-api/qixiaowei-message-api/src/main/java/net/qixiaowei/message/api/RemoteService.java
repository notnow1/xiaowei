package net.qixiaowei.message.api;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.message.api.factory.RemoteFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * 服务
 */
@FeignClient(contextId = "remoteMessageService", value = ServiceNameConstants.MESSAGE_SERVICE, fallbackFactory = RemoteFallbackFactory.class)
public interface RemoteService {


}
