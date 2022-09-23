package net.qixiaowei.operate.cloud.api;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.operate.cloud.api.factory.RemoteFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * 服务
 */
@FeignClient(contextId = "remoteOperateCloudService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteFallbackFactory.class)
public interface RemoteService {


}
