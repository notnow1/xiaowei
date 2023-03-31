package net.qixiaowei.strategy.cloud.api.remote.businessDesign;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.factory.businessDesign.RemoteBusinessDesignFallbackFactory;
import net.qixiaowei.strategy.cloud.api.factory.industry.RemoteIndustryAttractionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 业务设计远程调用
 */
@FeignClient(contextId = "remoteBusinessDesignService", value = ServiceNameConstants.STRATEGY_CLOUD_SERVICE, fallbackFactory = RemoteBusinessDesignFallbackFactory.class)
public interface RemoteBusinessDesignService {

    String API_PREFIX_STRATEGY_METRICS = "/businessDesign";


    /**
     * 战略衡量指标列表
     */
    @PostMapping(API_PREFIX_STRATEGY_METRICS + "/remoteBusinessDesign")
    R<List<BusinessDesignDTO>> remoteBusinessDesign(@RequestBody BusinessDesignDTO businessDesignDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 战略衡量指标详情列表
     */
    @PostMapping(API_PREFIX_STRATEGY_METRICS + "/remoteBusinessDesignParams")
    R<List<BusinessDesignParamDTO>> remoteBusinessDesignParams(@RequestBody BusinessDesignParamDTO businessDesignParamDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
