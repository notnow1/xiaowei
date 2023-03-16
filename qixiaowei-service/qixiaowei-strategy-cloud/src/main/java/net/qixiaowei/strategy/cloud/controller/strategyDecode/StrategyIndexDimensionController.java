package net.qixiaowei.strategy.cloud.controller.strategyDecode;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyIndexDimensionDTO;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyIndexDimensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Graves
 * @since 2023-03-03
 */
@RestController
@RequestMapping("strategyIndexDimension")
public class StrategyIndexDimensionController extends BaseController {


    @Autowired
    private IStrategyIndexDimensionService strategyIndexDimensionService;


    /**
     * 分页查询战略指标维度表列表
     */
    @RequiresPermissions("strategy:cloud:strategyIndexDimension:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        startPage();
        List<StrategyIndexDimensionDTO> list = strategyIndexDimensionService.selectStrategyIndexDimensionList(strategyIndexDimensionDTO);
        return getDataTable(list);
    }

    /**
     * 分页查询战略指标维度表列表
     */
    @RequiresPermissions("strategy:cloud:strategyIndexDimension:pageList")
    @GetMapping("/treeList")
    public AjaxResult treeList(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        if (!CheckObjectIsNullUtils.isNull(strategyIndexDimensionDTO)) {
            return AjaxResult.success(strategyIndexDimensionService.selectStrategyIndexDimensionList(strategyIndexDimensionDTO));
        }
        return AjaxResult.success(strategyIndexDimensionService.selectStrategyIndexDimensionTreeList(strategyIndexDimensionDTO));
    }

    /**
     * 查询战略指标维度表列表
     */
    @RequiresPermissions("strategy:cloud:strategyIndexDimension:list")
    @GetMapping("/list")
    public AjaxResult list(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        List<StrategyIndexDimensionDTO> list = strategyIndexDimensionService.selectStrategyIndexDimensionList(strategyIndexDimensionDTO);
        return AjaxResult.success(list);
    }

    /**
     * 获取层级列表
     */
    @RequiresPermissions("strategy:cloud:strategyIndexDimension:list")
    @GetMapping("/levelList")
    public AjaxResult levelList() {
        return AjaxResult.success(strategyIndexDimensionService.selectStrategyIndexDimensionLevelList());
    }

    /**
     * 查询战略指标维度表列表
     */
    @RequiresPermissions("strategy:cloud:strategyIndexDimension:list")
    @GetMapping("/rootList")
    public AjaxResult rootList() {
        return AjaxResult.success(strategyIndexDimensionService.selectStrategyIndexDimensionRootList());
    }

    /**
     * 规划业务单元列表-不带本身
     */
    @RequiresPermissions("strategy:cloud:strategyIndexDimension:list")
    @GetMapping("/otherList")
    public AjaxResult otherList(@RequestParam("strategyIndexDimensionId") Long strategyIndexDimensionId) {
        return AjaxResult.success(strategyIndexDimensionService.selectStrategyIndexDimensionOtherList(strategyIndexDimensionId));
    }

    /**
     * 新增战略指标维度表
     */
    @RequiresPermissions("strategy:cloud:strategyIndexDimension:add")
    @Log(title = "新增战略指标维度表", businessType = BusinessType.STRATEGY_INDEX_DIMENSION, businessId = "strategyIndexDimensionId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        return AjaxResult.success(strategyIndexDimensionService.insertStrategyIndexDimension(strategyIndexDimensionDTO));
    }

    /**
     * 修改战略指标维度表
     */
    @RequiresPermissions("strategy:cloud:strategyIndexDimension:edit")
    @Log(title = "修改战略指标维度表", businessType = BusinessType.STRATEGY_INDEX_DIMENSION, businessId = "strategyIndexDimensionId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        return toAjax(strategyIndexDimensionService.updateStrategyIndexDimension(strategyIndexDimensionDTO));
    }

    /**
     * 逻辑删除战略指标维度表
     */
    @RequiresPermissions("strategy:cloud:strategyIndexDimension:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        return toAjax(strategyIndexDimensionService.logicDeleteStrategyIndexDimensionByStrategyIndexDimensionId(strategyIndexDimensionDTO));
    }

    /**
     * 逻辑批量删除战略指标维度表
     */
    @RequiresPermissions("strategy:cloud:strategyIndexDimension:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> strategyIndexDimensionIds) {
        return toAjax(strategyIndexDimensionService.logicDeleteStrategyIndexDimensionByStrategyIndexDimensionIds(strategyIndexDimensionIds));
    }


}
