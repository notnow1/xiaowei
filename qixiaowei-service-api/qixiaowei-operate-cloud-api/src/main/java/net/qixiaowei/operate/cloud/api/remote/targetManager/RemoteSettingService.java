package net.qixiaowei.operate.cloud.api.remote.targetManager;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecompose;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSetting;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.api.factory.targetManager.RemoteDecomposeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 目标制定主表服务
 */
@FeignClient(contextId = "remoteSettingService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteDecomposeFallbackFactory.class)
public interface RemoteSettingService {

    /**
     * 根据指标ID查询目标制定是否引用
     *
     * @param indicatorIds 指标ID集合
     * @param source
     * @return
     */
    @PostMapping("/targetSetting/remote/queryIndicatorSetting")
    R<List<TargetSettingDTO>> queryIndicatorSetting(@RequestBody List<Long> indicatorIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}