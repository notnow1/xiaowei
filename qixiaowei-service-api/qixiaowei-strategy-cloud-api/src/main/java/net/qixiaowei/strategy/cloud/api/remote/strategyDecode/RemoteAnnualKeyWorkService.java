package net.qixiaowei.strategy.cloud.api.remote.strategyDecode;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDetailDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureTaskDTO;
import net.qixiaowei.strategy.cloud.api.factory.strategyDecode.RemoteAnnualKeyWorkFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 规划业务单元远程调用
 */
@FeignClient(contextId = "remoteAnnualKeyWorkService", value = ServiceNameConstants.STRATEGY_CLOUD_SERVICE, fallbackFactory = RemoteAnnualKeyWorkFallbackFactory.class)
public interface RemoteAnnualKeyWorkService {

    String API_PREFIX_ANNUAL_KEY_WORK = "/annualKeyWork";


    /**
     * 规划业务单元列表
     */
    @PostMapping(API_PREFIX_ANNUAL_KEY_WORK + "/remoteAnnualKeyWork")
    R<List<AnnualKeyWorkDTO>> remoteAnnualKeyWork(@RequestBody AnnualKeyWorkDTO annualKeyWorkDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 规划业务单元列表
     */
    @PostMapping(API_PREFIX_ANNUAL_KEY_WORK + "/remoteAnnualKeyWork")
    R<List<AnnualKeyWorkDetailDTO>> remoteAnnualKeyWorkDepartment(@RequestBody AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
