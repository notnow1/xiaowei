package net.qixiaowei.strategy.cloud.api.remote.industry;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;

import net.qixiaowei.strategy.cloud.api.factory.industry.RemoteIndustryAttractionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * 行业吸引力远程调用
 */
@FeignClient(contextId = "remoteIndustryAttractionService", value = ServiceNameConstants.STRATEGY_CLOUD_SERVICE, fallbackFactory = RemoteIndustryAttractionFallbackFactory.class)
public interface RemoteIndustryAttractionService {

    String API_PREFIX_INDUSTRY_ATTRACTION = "/industryAttraction";



}