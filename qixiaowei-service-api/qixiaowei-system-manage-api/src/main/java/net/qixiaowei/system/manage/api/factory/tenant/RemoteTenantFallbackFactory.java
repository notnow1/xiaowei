package net.qixiaowei.system.manage.api.factory.tenant;

import net.qixiaowei.system.manage.api.remote.tenant.RemoteTenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


/**
 * 租户服务降级处理
 */
@Component
public class RemoteTenantFallbackFactory implements FallbackFactory<RemoteTenantService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteTenantFallbackFactory.class);

    @Override
    public RemoteTenantService create(Throwable throwable) {
        log.error("租户服务调用失败:{}", throwable.getMessage());
        return new RemoteTenantService() {

        };
    }
}
