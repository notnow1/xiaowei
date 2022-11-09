package net.qixiaowei.system.manage.api.remote.system;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.api.factory.system.RemoteRegionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 区域服务
 */
@FeignClient(contextId = "remoteRegionService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteRegionFallbackFactory.class)
public interface RemoteRegionService {

    String API_PREFIX_REGION = "/region";

    /**
     * 通过区域ids查询区域信息
     *
     * @param regionIds 区域ids
     * @return 结果
     */
    @GetMapping(API_PREFIX_REGION + "/getRegionsByIds")
    R<List<RegionDTO>> getRegionsByIds(@RequestParam("regionIds") Set<Long> regionIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


}
