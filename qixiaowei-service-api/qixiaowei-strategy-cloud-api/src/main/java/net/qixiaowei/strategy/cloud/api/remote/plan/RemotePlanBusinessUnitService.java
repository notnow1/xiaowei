package net.qixiaowei.strategy.cloud.api.remote.plan;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.api.factory.industry.RemoteIndustryAttractionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 规划业务单元远程调用
 */
@FeignClient(contextId = "remotePlanBusinessUnitService", value = ServiceNameConstants.STRATEGY_CLOUD_SERVICE, fallbackFactory = RemoteIndustryAttractionFallbackFactory.class)
public interface RemotePlanBusinessUnitService {

    String API_PREFIX_PLAN_BUSINESS_UNIT = "/planBusinessUnit";


    /**
     * 规划业务单元列表
     */
    @PostMapping(API_PREFIX_PLAN_BUSINESS_UNIT + "/remotePlanBusinessUnit")
    R<List<PlanBusinessUnitDTO>> remotePlanBusinessUnit(@RequestBody PlanBusinessUnitDTO planBusinessUnitDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
