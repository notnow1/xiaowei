package net.qixiaowei.operate.cloud.api.remote.targetManager;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.factory.targetManager.RemoteTargetDecomposeHistoryFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(contextId = "remoteTargetDecomposeHistoryService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteTargetDecomposeHistoryFallbackFactory.class)
public interface RemoteTargetDecomposeHistoryService {
    /**
     * 定时任务生成历史数据
     */
    @PostMapping("/targetDecomposeHistory/cron")
    R<?> cronCreateHistoryList(@RequestBody Integer timeDimension, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}