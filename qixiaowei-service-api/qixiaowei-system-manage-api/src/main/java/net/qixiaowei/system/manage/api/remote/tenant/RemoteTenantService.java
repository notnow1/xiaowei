package net.qixiaowei.system.manage.api.remote.tenant;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.system.manage.api.factory.tenant.RemoteTenantFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * 租户服务
 */
@FeignClient(contextId = "remoteTenantService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteTenantFallbackFactory.class)
public interface RemoteTenantService {

    String API_PREFIX_TENANT = "/tenant";



}
