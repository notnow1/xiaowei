package net.qixiaowei.sales.cloud.api.remote.sync;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.sales.cloud.api.factory.sync.RemoteSyncAuthorizationFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * 悟空Authorization服务
 */
@FeignClient(contextId = "remoteSyncAuthorizationService", value = "wk-authorization", fallbackFactory = RemoteSyncAuthorizationFallbackFactory.class)
public interface RemoteSyncAuthorizationService {

    /**
     * 用户-退出
     */
    @PostMapping("/logout")
    R<?> syncUserLogout(@RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);


}
