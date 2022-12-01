package net.qixiaowei.system.manage.api.remote.basic;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.system.manage.api.factory.basic.RemoteOfficialRankSystemFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(contextId = "RemotePostService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteOfficialRankSystemFallbackFactory.class)
public interface RemotePostService {

}
