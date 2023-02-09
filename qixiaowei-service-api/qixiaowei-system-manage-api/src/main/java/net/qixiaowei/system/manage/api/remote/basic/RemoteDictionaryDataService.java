package net.qixiaowei.system.manage.api.remote.basic;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryTypeDTO;
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
    /**
     * 根据枚举查询产品应用字典名称数据
     */
    @GetMapping(API_PREFIX_DICTIONARYDATA+"/selectDictionaryTypeByProduct")
    public R<DictionaryTypeDTO> selectDictionaryTypeByProduct(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据typeId查询字典数据
     */
    @GetMapping(API_PREFIX_DICTIONARYDATA+"/selectDictionaryDataByProduct")
    public R<List<DictionaryDataDTO>> selectDictionaryDataByProduct(@RequestParam("dictionaryTypeId") Long dictionaryTypeId,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据实体类远程查询字典数据
     */
    @PostMapping(API_PREFIX_DICTIONARYDATA+"/remoteDictionaryDataId")
     R<List<DictionaryDataDTO>> remoteDictionaryDataId(@RequestBody DictionaryDataDTO dictionaryDataDTO,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
