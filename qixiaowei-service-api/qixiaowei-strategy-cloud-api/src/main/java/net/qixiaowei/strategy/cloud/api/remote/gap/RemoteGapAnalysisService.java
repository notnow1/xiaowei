package net.qixiaowei.strategy.cloud.api.remote.gap;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOperateDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOpportunityDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisPerformanceDTO;
import net.qixiaowei.strategy.cloud.api.factory.businessDesign.RemoteBusinessDesignFallbackFactory;
import net.qixiaowei.strategy.cloud.api.factory.gap.RemoteGapAnalysisFallbackFactory;
import net.qixiaowei.strategy.cloud.api.factory.industry.RemoteIndustryAttractionFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 差距分析远程调用
 */
@FeignClient(contextId = "remoteGapAnalysisService", value = ServiceNameConstants.STRATEGY_CLOUD_SERVICE, fallbackFactory = RemoteGapAnalysisFallbackFactory.class)
public interface RemoteGapAnalysisService {

    String API_PREFIX_GAP_ANALYSIS = "/gapAnalysis";


    /**
     * 战略衡量指标列表
     */
    @PostMapping(API_PREFIX_GAP_ANALYSIS + "/remoteGapAnalysis")
    R<List<GapAnalysisDTO>> remoteGapAnalysis(@RequestBody GapAnalysisDTO gapAnalysisDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 战略衡量指标列表
     */
    @PostMapping(API_PREFIX_GAP_ANALYSIS + "/remoteOperateList")
    R<List<GapAnalysisOperateDTO>> remoteOperateList(@RequestBody GapAnalysisDTO gapAnalysisDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 远程调用差距机会差距表
     */
    @PostMapping(API_PREFIX_GAP_ANALYSIS + "/remoteOpportunityList")
    R<List<GapAnalysisOpportunityDTO>> remoteOpportunityList(@RequestBody GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 远程调用差距业绩差距表
     */
    @PostMapping(API_PREFIX_GAP_ANALYSIS + "/remotePerformanceList")
    R<List<GapAnalysisPerformanceDTO>> remotePerformanceList(@RequestBody GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
