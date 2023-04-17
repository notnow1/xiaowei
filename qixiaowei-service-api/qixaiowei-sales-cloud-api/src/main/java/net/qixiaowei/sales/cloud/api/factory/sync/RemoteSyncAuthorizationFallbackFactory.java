package net.qixiaowei.sales.cloud.api.factory.sync;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.sales.cloud.api.remote.sync.RemoteSyncAuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


/**
 * 悟空Authorization服务降级处理
 */
@Component
public class RemoteSyncAuthorizationFallbackFactory implements FallbackFactory<RemoteSyncAuthorizationService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteSyncAuthorizationFallbackFactory.class);

    @Override
    public RemoteSyncAuthorizationService create(Throwable throwable) {
        log.error("悟空Authorization服务调用失败:{}", throwable.getMessage());
        return new RemoteSyncAuthorizationService() {

            @Override
            public R<?> syncUserLogout(String salesToken) {
                return R.fail("远程同步用户退出失败:" + throwable.getMessage());
            }

        };
    }
}
