package net.qixiaowei.system.manage.api.remote.basic;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.factory.basic.RemoteIndustryFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 行业服务
 */
@FeignClient(contextId = "remoteIndustryService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteIndustryFallbackFactory.class)
public interface RemoteIndustryService {
    String API_PREFIX_INDUSTRY = "/industry";

    /**
     * 通过Code集合查找行业列表
     *
     * @return 结果
     */
    @PostMapping(API_PREFIX_INDUSTRY + "/codeList")
    R<List<IndustryDTO>> selectCodeList(@RequestBody List<String> industryCodes, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过Id查找行业列表
     *
     * @return 结果
     */
    @GetMapping(API_PREFIX_INDUSTRY + "/selectById")
    R<IndustryDTO> selectById(@RequestParam("industryId") Long industryId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过ID集合查找行业列表
     *
     * @return 结果
     */
    @PostMapping(API_PREFIX_INDUSTRY + "/selectByIds")
    R<List<IndustryDTO>> selectByIds(@RequestBody List<Long> industryIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 查找行业列表
     *
     * @return 结果
     */
    @PostMapping(API_PREFIX_INDUSTRY + "/selectList")
    R<List<IndustryDTO>> selectListByIndustry(@RequestBody IndustryDTO industryDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String inner);
}
