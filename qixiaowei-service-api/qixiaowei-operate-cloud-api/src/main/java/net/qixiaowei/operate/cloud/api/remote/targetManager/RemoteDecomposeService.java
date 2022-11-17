package net.qixiaowei.operate.cloud.api.remote.targetManager;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.factory.targetManager.RemoteDecomposeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
/**
 * 名表分解主表服务
 */
@FeignClient(contextId = "remoteDecomposeService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteDecomposeFallbackFactory.class)
public interface RemoteDecomposeService {
    /**
     *
     */
    @PostMapping("/targetDecompose/remote/{targetDecomposeId}")
    R<TargetDecomposeDTO> info(@PathVariable Long targetDecomposeId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}