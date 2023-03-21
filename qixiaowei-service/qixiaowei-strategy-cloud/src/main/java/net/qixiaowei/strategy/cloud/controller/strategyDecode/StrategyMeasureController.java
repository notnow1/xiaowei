package net.qixiaowei.strategy.cloud.controller.strategyDecode;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDTO;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Graves
 * @since 2023-03-07
 */
@RestController
@RequestMapping("strategyMeasure")
public class StrategyMeasureController extends BaseController {


    @Autowired
    private IStrategyMeasureService strategyMeasureService;


    /**
     * 查询战略举措清单表详情
     */
    @RequiresPermissions("strategy:cloud:strategyMeasure:info")
    @GetMapping("/info/{strategyMeasureId}")
    public AjaxResult info(@PathVariable Long strategyMeasureId) {
        StrategyMeasureDTO strategyMeasureDTO = strategyMeasureService.selectStrategyMeasureByStrategyMeasureId(strategyMeasureId);
        return AjaxResult.success(strategyMeasureDTO);
    }

    /**
     * 分页查询战略举措清单表列表
     */
    @RequiresPermissions("strategy:cloud:strategyMeasure:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(StrategyMeasureDTO strategyMeasureDTO) {
        startPage();
        List<StrategyMeasureDTO> list = strategyMeasureService.selectStrategyMeasureList(strategyMeasureDTO);
        return getDataTable(list);
    }

    /**
     * 查询战略举措清单表列表
     */
    @GetMapping("/list")
    public AjaxResult list(StrategyMeasureDTO strategyMeasureDTO) {
        List<StrategyMeasureDTO> list = strategyMeasureService.selectStrategyMeasureList(strategyMeasureDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增战略举措清单表
     */
    @RequiresPermissions("strategy:cloud:strategyMeasure:add")
    @Log(title = "新增战略举措清单表", businessType = BusinessType.STRATEGY_MEASURE, businessId = "strategyMeasureId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody StrategyMeasureDTO strategyMeasureDTO) {
        return AjaxResult.success(strategyMeasureService.insertStrategyMeasure(strategyMeasureDTO));
    }


    /**
     * 修改战略举措清单表
     */
    @RequiresPermissions("strategy:cloud:strategyMeasure:edit")
    @Log(title = "修改战略举措清单表", businessType = BusinessType.STRATEGY_MEASURE, businessId = "strategyMeasureId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody StrategyMeasureDTO strategyMeasureDTO) {
        return toAjax(strategyMeasureService.updateStrategyMeasure(strategyMeasureDTO));
    }

    /**
     * 逻辑删除战略举措清单表
     */
    @RequiresPermissions("strategy:cloud:strategyMeasure:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody StrategyMeasureDTO strategyMeasureDTO) {
        return toAjax(strategyMeasureService.logicDeleteStrategyMeasureByStrategyMeasureId(strategyMeasureDTO));
    }

    /**
     * 逻辑批量删除战略举措清单表
     */
    @RequiresPermissions("strategy:cloud:strategyMeasure:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> strategyMeasureIds) {
        return toAjax(strategyMeasureService.logicDeleteStrategyMeasureByStrategyMeasureIds(strategyMeasureIds));
    }

}
