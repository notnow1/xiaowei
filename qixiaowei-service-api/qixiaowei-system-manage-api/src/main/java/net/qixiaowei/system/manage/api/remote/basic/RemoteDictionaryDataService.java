package net.qixiaowei.system.manage.api.remote.basic;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;
import net.qixiaowei.system.manage.api.factory.basic.RemoteDictionaryDataFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典服务
 */
@FeignClient(contextId = "remoteDictionaryDataService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteDictionaryDataFallbackFactory.class)
public interface RemoteDictionaryDataService {
    String API_PREFIX_DICTIONARYDATA = "/dictionaryData/remote";

    /**
     * 根据字典id查询字典数据表详情
     */
    @GetMapping(API_PREFIX_DICTIONARYDATA+"/dictionaryDataId")
    public R<DictionaryDataDTO> info(@RequestParam("dictionaryDataId") Long dictionaryDataId,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据字典id集合查询字典数据表详情
     */
    @PostMapping(API_PREFIX_DICTIONARYDATA+"/selectDictionaryDataIds")
    public R<List<DictionaryDataDTO>> selectDictionaryDataByDictionaryDataIds(@RequestBody List<Long> dictionaryDataIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
