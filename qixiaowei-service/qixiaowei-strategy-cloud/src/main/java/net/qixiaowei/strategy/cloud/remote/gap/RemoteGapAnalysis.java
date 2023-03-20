package net.qixiaowei.strategy.cloud.remote.gap;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOperateDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOpportunityDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisPerformanceDTO;
import net.qixiaowei.strategy.cloud.api.remote.gap.RemoteGapAnalysisService;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisOpportunityService;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisPerformanceService;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @description: 年度重点工作配置远程实现类
 * @Author: Graves
 * @date: 2023/02/20 14:07
 **/
@RestController
@RequestMapping("/gapAnalysis")
public class RemoteGapAnalysis implements RemoteGapAnalysisService {

    @Autowired
    private IGapAnalysisService iGapAnalysisService;

    @Autowired
    private IGapAnalysisOpportunityService gapAnalysisOpportunityService;

    @Autowired
    private IGapAnalysisPerformanceService gapAnalysisPerformanceService;

    /**
     * 差距分析
     *
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteGapAnalysis")
    public R<List<GapAnalysisDTO>> remoteGapAnalysis(@RequestBody GapAnalysisDTO gapAnalysisDTO, String source) {
        return R.ok(iGapAnalysisService.remoteGapAnalysis(gapAnalysisDTO));
    }

    /**
     * 差距分析远程指标引用
     *
     * @param source 根源
     * @return 结果
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteOperateList")
    public R<List<GapAnalysisOperateDTO>> remoteOperateList(@RequestBody GapAnalysisDTO gapAnalysisDTO, String source) {
        return R.ok(iGapAnalysisService.remoteOperateList(gapAnalysisDTO));
    }

    /**
     * 远程调用差距机会差距表
     *
     * @param source 根源
     * @return 结果
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteOpportunityList")
    public R<List<GapAnalysisOpportunityDTO>> remoteOpportunityList(@RequestBody GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO, String source) {
        return R.ok(gapAnalysisOpportunityService.selectGapAnalysisOpportunityList(gapAnalysisOpportunityDTO));
    }

    /**
     * 远程调用差距业绩差距表
     *
     * @param source 根源
     * @return 结果
     */
    @Override
    @InnerAuth
    @PostMapping("/remotePerformanceList")
    public R<List<GapAnalysisPerformanceDTO>> remotePerformanceList(@RequestBody GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO, String source) {
        return R.ok(gapAnalysisPerformanceService.selectGapAnalysisPerformanceList(gapAnalysisPerformanceDTO));
    }
}
