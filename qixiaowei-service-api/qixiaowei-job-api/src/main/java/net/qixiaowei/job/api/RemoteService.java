package net.qixiaowei.job.api;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.job.api.factory.RemoteFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * 服务
 */
@FeignClient(contextId = "remoteJobService", value = ServiceNameConstants.JOB_SERVICE, fallbackFactory = RemoteFallbackFactory.class)
public interface RemoteService {


}
