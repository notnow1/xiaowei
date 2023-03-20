package net.qixiaowei.strategy.cloud.remote.strategyDecode;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDetailDTO;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteStrategyMetricsService;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @description: 战略衡量指标配置远程实现类
 * @Author: Graves
 * @date: 2023/02/20 14:07
 **/
@RestController
@RequestMapping("/strategyMetrics")
public class RemoteStrategyMetrics implements RemoteStrategyMetricsService {

    @Autowired
    private IStrategyMetricsService iStrategyMetricsService;

    /**
     * 年度重点工作
     *
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteStrategyMetrics")
    public R<List<StrategyMetricsDTO>> remoteStrategyMetrics(@RequestBody StrategyMetricsDTO strategyMetricsDTO, String source) {
        return R.ok(iStrategyMetricsService.remoteStrategyMetrics(strategyMetricsDTO));
    }

    /**
     * 远程根据指标ID集合查找战略衡量指标
     *
     * @param strategyMetricsDTO 战略衡量指标
     * @param source             根源
     * @return 结果
     */
    @Override
    public R<List<StrategyMetricsDetailDTO>> remoteListByIndicator(StrategyMetricsDTO strategyMetricsDTO, String source) {
        return R.ok(iStrategyMetricsService.remoteListByIndicator(strategyMetricsDTO));
    }
}
