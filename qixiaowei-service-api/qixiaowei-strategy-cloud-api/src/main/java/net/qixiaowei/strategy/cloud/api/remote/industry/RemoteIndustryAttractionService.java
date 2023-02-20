package net.qixiaowei.strategy.cloud.api.remote.industry;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.factory.industry.RemoteIndustryAttractionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 行业吸引力远程调用
 */
@FeignClient(contextId = "remoteIndustryAttractionService", value = ServiceNameConstants.STRATEGY_CLOUD_SERVICE, fallbackFactory = RemoteIndustryAttractionFallbackFactory.class)
public interface RemoteIndustryAttractionService {

    String API_PREFIX_INDUSTRY_ATTRACTION = "/industryAttraction";

    /**
     * 初始化行业吸引力配置
     */
    @PostMapping(API_PREFIX_INDUSTRY_ATTRACTION + "/initIndustryAttraction")
    R<Boolean> initIndustryAttraction(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);


}