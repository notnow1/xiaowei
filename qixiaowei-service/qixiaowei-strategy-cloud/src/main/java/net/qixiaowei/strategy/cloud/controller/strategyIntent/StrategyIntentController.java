package net.qixiaowei.strategy.cloud.controller.strategyIntent;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentDTO;
import net.qixiaowei.strategy.cloud.service.strategyIntent.IStrategyIntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;



/**
*
* @author TANGMICHI
* @since 2023-02-23
*/
@RestController
@RequestMapping("strategyIntent")
public class StrategyIntentController extends BaseController
{


    @Autowired
    private IStrategyIntentService strategyIntentService;



    /**
    * 查询战略意图表详情
    */
    @RequiresPermissions("strategy:cloud:strategyIntent:info")
    @GetMapping("/info/{strategyIntentId}")
    public AjaxResult info(@PathVariable Long strategyIntentId){
    StrategyIntentDTO strategyIntentDTO = strategyIntentService.selectStrategyIntentByStrategyIntentId(strategyIntentId);
        return AjaxResult.success(strategyIntentDTO);
    }

    /**
    * 分页查询战略意图表列表
    */
    @RequiresPermissions("strategy:cloud:strategyIntent:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(StrategyIntentDTO strategyIntentDTO){
    startPage();
    List<StrategyIntentDTO> list = strategyIntentService.selectStrategyIntentList(strategyIntentDTO);
    return getDataTable(list);
    }

    /**
    * 查询战略意图表列表
    */
    @RequiresPermissions("strategy:cloud:strategyIntent:list")
    @GetMapping("/list")
    public AjaxResult list(StrategyIntentDTO strategyIntentDTO){
    List<StrategyIntentDTO> list = strategyIntentService.selectStrategyIntentList(strategyIntentDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增战略意图表
    */
    @RequiresPermissions("strategy:cloud:strategyIntent:add")
    @Log(title = "新增战略意图表", businessType = BusinessType.STRATEGY_INTENT, businessId = "strategyIntentId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(StrategyIntentDTO.AddStrategyIntentDTO.class) StrategyIntentDTO strategyIntentDTO) {
    return AjaxResult.success(strategyIntentService.insertStrategyIntent(strategyIntentDTO));
    }


    /**
    * 修改战略意图表
    */
    @RequiresPermissions("strategy:cloud:strategyIntent:edit")
    @Log(title = "修改战略意图表", businessType = BusinessType.STRATEGY_INTENT, businessId = "strategyIntentId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(StrategyIntentDTO.UpdateStrategyIntentDTO.class) StrategyIntentDTO strategyIntentDTO)
    {
    return toAjax(strategyIntentService.updateStrategyIntent(strategyIntentDTO));
    }

    /**
    * 逻辑删除战略意图表
    */
    @RequiresPermissions("strategy:cloud:strategyIntent:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(StrategyIntentDTO.DeleteStrategyIntentDTO.class) StrategyIntentDTO strategyIntentDTO)
    {
    return toAjax(strategyIntentService.logicDeleteStrategyIntentByStrategyIntentId(strategyIntentDTO));
    }
    /**
    * 逻辑批量删除战略意图表
    */
    @RequiresPermissions("strategy:cloud:strategyIntent:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  strategyIntentIds)
    {
    return toAjax(strategyIntentService.logicDeleteStrategyIntentByStrategyIntentIds(strategyIntentIds));
    }
}
