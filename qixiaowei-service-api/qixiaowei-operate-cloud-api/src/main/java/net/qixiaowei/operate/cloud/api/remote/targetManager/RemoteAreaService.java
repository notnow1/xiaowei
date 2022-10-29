package net.qixiaowei.operate.cloud.api.remote.targetManager;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.factory.RemoteAreaFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(contextId = "remoteAreaService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteAreaFallbackFactory.class)
public interface RemoteAreaService {
    /**
     * 查询区域配置列表（ID+NAME）
     */
    @PostMapping("/area/dropList")
    R<List<AreaDTO>> dropList(@RequestBody AreaDTO areaDTO);

    @PostMapping("/area/getName")
    R<List<AreaDTO>> getName(@RequestBody List<Long> areaIds);
}