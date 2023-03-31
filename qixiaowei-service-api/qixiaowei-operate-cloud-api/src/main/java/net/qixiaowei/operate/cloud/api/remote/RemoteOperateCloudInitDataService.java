package net.qixiaowei.operate.cloud.api.remote;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.factory.RemoteOperateCloudInitDataFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(contextId = "remoteOperateCloudInitDataService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteOperateCloudInitDataFallbackFactory.class)
public interface RemoteOperateCloudInitDataService {
    /**
     * 初始化数据
     */
    @PostMapping("/operate-cloud/initData")
    R<Boolean> initData(@RequestParam("userId") Long userId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}