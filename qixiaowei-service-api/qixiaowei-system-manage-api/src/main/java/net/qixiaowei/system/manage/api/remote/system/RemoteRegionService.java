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
    @PostMapping(API_PREFIX_REGION + "/getRegionsByIds")
    R<List<RegionDTO>> getRegionsByIds(@RequestBody Set<Long> regionIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过省份名称集合集合查询省份信息
     *
     * @param regionNames 省份集合
     * @return 结果
     */
    @PostMapping(API_PREFIX_REGION + "/codeList")
    R<List<RegionDTO>> selectCodeList(@RequestBody List<String> regionNames, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 远程查询excel省份下拉框
     *
     * @param regionDTO
     * @return 结果
     */
    @PostMapping(API_PREFIX_REGION + "/getDropList")
    R<List<RegionDTO>> getDropList(@RequestBody RegionDTO regionDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
