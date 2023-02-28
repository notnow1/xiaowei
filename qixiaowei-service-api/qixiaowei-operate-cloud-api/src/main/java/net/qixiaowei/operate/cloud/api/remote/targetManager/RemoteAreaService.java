package net.qixiaowei.operate.cloud.api.remote.targetManager;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.factory.targetManager.RemoteAreaFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(contextId = "remoteAreaService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteAreaFallbackFactory.class)
public interface RemoteAreaService {
    /**
     * 查询区域配置列表（ID+NAME）
     */
    @PostMapping("/area/dropList")
    R<List<AreaDTO>> dropList(@RequestBody AreaDTO areaDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据区域ID查找数据
     */
    @PostMapping("/area/getByIds")
    R<List<AreaDTO>> selectAreaListByAreaIds(@RequestBody List<Long> areaIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据区域ID查找数据
     */
    @GetMapping("/area/getById")
    R<AreaDTO> getById(@RequestParam("areaId") Long areaId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}