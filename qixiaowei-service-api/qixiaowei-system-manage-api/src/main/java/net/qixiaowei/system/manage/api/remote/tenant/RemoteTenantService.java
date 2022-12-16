package net.qixiaowei.system.manage.api.remote.tenant;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.factory.tenant.RemoteTenantFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;


/**
 * 租户服务
 */
@FeignClient(contextId = "remoteTenantService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteTenantFallbackFactory.class)
public interface RemoteTenantService {

    String API_PREFIX_TENANT = "/tenant";


    /**
     * 查找生效的租户ID集合
     *
     * @return 结果
     */
    @GetMapping(API_PREFIX_TENANT + "/getTenantIds")
    R<List<Long>> getTenantIds(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
