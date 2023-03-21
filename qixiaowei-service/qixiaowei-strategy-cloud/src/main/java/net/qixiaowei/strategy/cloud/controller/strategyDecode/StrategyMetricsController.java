package net.qixiaowei.strategy.cloud.controller.strategyDecode;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDTO;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Graves
 * @since 2023-03-07
 */
@RestController
@RequestMapping("strategyMetrics")
public class StrategyMetricsController extends BaseController {


    @Autowired
    private IStrategyMetricsService strategyMetricsService;


    /**
     * 查询战略衡量指标表详情
     */
    @RequiresPermissions("strategy:cloud:strategyMetrics:info")
    @GetMapping("/info/{strategyMetricsId}")
    public AjaxResult info(@PathVariable Long strategyMetricsId) {
        StrategyMetricsDTO strategyMetricsDTO = strategyMetricsService.selectStrategyMetricsByStrategyMetricsId(strategyMetricsId);
        return AjaxResult.success(strategyMetricsDTO);
    }

    /**
     * 分页查询战略衡量指标表列表
     */
    @RequiresPermissions("strategy:cloud:strategyMetrics:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(StrategyMetricsDTO strategyMetricsDTO) {
        startPage();
        List<StrategyMetricsDTO> list = strategyMetricsService.selectStrategyMetricsList(strategyMetricsDTO);
        return getDataTable(list);
    }

    /**
     * 查询战略衡量指标表列表
     */
    @GetMapping("/list")
    public AjaxResult list(StrategyMetricsDTO strategyMetricsDTO) {
        List<StrategyMetricsDTO> list = strategyMetricsService.selectStrategyMetricsList(strategyMetricsDTO);
        return AjaxResult.success(list);
    }

    /**
     * 修改战略衡量指标表
     */
    @RequiresPermissions("strategy:cloud:strategyMetrics:edit")
    @Log(title = "修改战略衡量指标表", businessType = BusinessType.STRATEGY_METRICS, businessId = "strategyMetricsId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody StrategyMetricsDTO strategyMetricsDTO) {
        return toAjax(strategyMetricsService.updateStrategyMetrics(strategyMetricsDTO));
    }

}
