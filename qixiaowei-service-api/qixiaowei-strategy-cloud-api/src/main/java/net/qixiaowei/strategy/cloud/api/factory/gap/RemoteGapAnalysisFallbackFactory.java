package net.qixiaowei.strategy.cloud.api.factory.gap;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOperateDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOpportunityDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisPerformanceDTO;
import net.qixiaowei.strategy.cloud.api.remote.gap.RemoteGapAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;


public class RemoteGapAnalysisFallbackFactory implements FallbackFactory<RemoteGapAnalysisService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteGapAnalysisFallbackFactory.class);

    @Override
    public RemoteGapAnalysisService create(Throwable throwable) {
        log.error("差距分析远程服务调用失败:{}", throwable.getMessage());
        return new RemoteGapAnalysisService() {

            @Override
            public R<List<GapAnalysisDTO>> remoteGapAnalysis(GapAnalysisDTO gapAnalysisDTO, String source) {
                return R.fail("远程调用差距分析列表失败:" + throwable.getMessage());
            }

            @Override
            public R<List<GapAnalysisOperateDTO>> remoteOperateList(GapAnalysisDTO gapAnalysisDTO, String source) {
                return R.fail("远程调用差距分析历史经营情况引用失败:" + throwable.getMessage());
            }

            @Override
            public R<List<GapAnalysisOpportunityDTO>> remoteOpportunityList(GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO, String source) {
                return R.fail("远程调用差距机会差距表引用失败:" + throwable.getMessage());
            }

            @Override
            public R<List<GapAnalysisPerformanceDTO>> remotePerformanceList(GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO, String source) {
                return R.fail("远程调用差距业绩差距表引用失败:" + throwable.getMessage());
            }

        };
    }
}
