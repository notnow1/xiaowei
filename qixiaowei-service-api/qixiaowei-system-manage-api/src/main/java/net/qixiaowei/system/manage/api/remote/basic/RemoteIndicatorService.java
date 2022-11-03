package net.qixiaowei.system.manage.api.remote.basic;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.factory.basic.RemoteIndicatorFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 区域服务
 */
@FeignClient(contextId = "remoteIndicatorService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteIndicatorFallbackFactory.class)
public interface RemoteIndicatorService {

    String API_PREFIX_INDICATOR = "/indicator";

    /**
     * 通过Code查找指标列表
     *
     * @param indicatorCode 区域ids
     * @return 结果
     */
    @GetMapping(API_PREFIX_INDICATOR + "/listByCode")
    R<IndicatorDTO> selectIndicatorByCode(@RequestParam("indicatorCode") String indicatorCode);

    /**
     * 通过CodeList查找指标列表
     *
     * @param indicatorCodes 区域ids
     * @return 结果
     */
    @GetMapping(API_PREFIX_INDICATOR + "/listByCodes")
    R<List<IndicatorDTO>> selectIndicatorByCodeList(@RequestParam("indicatorCodes") List<String> indicatorCodes);

}
