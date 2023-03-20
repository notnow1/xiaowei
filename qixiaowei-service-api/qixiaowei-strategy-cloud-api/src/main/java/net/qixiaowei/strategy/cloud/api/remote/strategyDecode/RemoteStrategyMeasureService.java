package net.qixiaowei.strategy.cloud.api.remote.strategyDecode;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureTaskDTO;
import net.qixiaowei.strategy.cloud.api.factory.industry.RemoteIndustryAttractionFallbackFactory;
import net.qixiaowei.strategy.cloud.api.vo.strategyDecode.StrategyMeasureDetailVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 战略举措清单程调用
 */
@FeignClient(contextId = "remoteStrategyMeasureService", value = ServiceNameConstants.STRATEGY_CLOUD_SERVICE, fallbackFactory = RemoteIndustryAttractionFallbackFactory.class)
public interface RemoteStrategyMeasureService {

    String API_PREFIX_STRATEGY_MEASURE = "/strategyMeasure";


    /**
     * 战略举措清单列表
     */
    @PostMapping(API_PREFIX_STRATEGY_MEASURE + "/remoteStrategyMeasure")
    R<List<StrategyMeasureDTO>> remoteStrategyMeasure(@RequestBody StrategyMeasureDTO strategyMeasureDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 远程引用查询详情
     */
    @PostMapping(API_PREFIX_STRATEGY_MEASURE + "/remoteDutyMeasure")
    R<List<StrategyMeasureTaskDTO>> remoteDutyMeasure(StrategyMeasureDetailVO strategyMeasureDetailVO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
