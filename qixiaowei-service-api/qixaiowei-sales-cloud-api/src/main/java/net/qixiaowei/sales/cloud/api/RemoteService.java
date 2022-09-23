package net.qixiaowei.job.cloud.api;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.job.cloud.api.factory.RemoteFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * 服务
 */
@FeignClient(contextId = "remoteSalesCloudService", value = ServiceNameConstants.SALES_CLOUD_SERVICE, fallbackFactory = RemoteFallbackFactory.class)
public interface RemoteService {


}
