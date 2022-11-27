package net.qixiaowei.operate.cloud.controller.salary;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.salary.BonusBudgetDTO;
import net.qixiaowei.operate.cloud.service.salary.IBonusBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



/**
*
* @author TANGMICHI
* @since 2022-11-26
*/
@RestController
@RequestMapping("bonusBudget")
public class BonusBudgetController extends BaseController
{


    @Autowired
    private IBonusBudgetService bonusBudgetService;

    /**
     * 新增奖金预算预制数据
     */
    @RequiresPermissions("operate:cloud:bonusBudget:info")
    @GetMapping("/add/{budgetYear}")
    public AjaxResult addBonusBudgetTamount(@PathVariable int budgetYear){
        BonusBudgetDTO bonusBudgetDTO = bonusBudgetService.addBonusBudgetTamount(budgetYear);
        return AjaxResult.success(bonusBudgetDTO);
    }

    /**
    * 查询奖金预算表详情
    */
    @RequiresPermissions("operate:cloud:bonusBudget:info")
    @GetMapping("/info/{bonusBudgetId}")
    public AjaxResult info(@PathVariable Long bonusBudgetId){
    BonusBudgetDTO bonusBudgetDTO = bonusBudgetService.selectBonusBudgetByBonusBudgetId(bonusBudgetId);
        return AjaxResult.success(bonusBudgetDTO);
    }

    /**
    * 分页查询奖金预算表列表
    */
    @RequiresPermissions("operate:cloud:bonusBudget:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(BonusBudgetDTO bonusBudgetDTO){
    startPage();
    List<BonusBudgetDTO> list = bonusBudgetService.selectBonusBudgetList(bonusBudgetDTO);
    return getDataTable(list);
    }

    /**
    * 查询奖金预算表列表
    */
    @RequiresPermissions("operate:cloud:bonusBudget:list")
    @GetMapping("/list")
    public AjaxResult list(BonusBudgetDTO bonusBudgetDTO){
    List<BonusBudgetDTO> list = bonusBudgetService.selectBonusBudgetList(bonusBudgetDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增奖金预算表
    */
    @RequiresPermissions("operate:cloud:bonusBudget:add")
    @Log(title = "新增奖金预算表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody BonusBudgetDTO bonusBudgetDTO) {
    return AjaxResult.success(bonusBudgetService.insertBonusBudget(bonusBudgetDTO));
    }


    /**
    * 修改奖金预算表
    */
    @RequiresPermissions("operate:cloud:bonusBudget:edit")
    @Log(title = "修改奖金预算表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody BonusBudgetDTO bonusBudgetDTO)
    {
    return toAjax(bonusBudgetService.updateBonusBudget(bonusBudgetDTO));
    }

    /**
    * 逻辑删除奖金预算表
    */
    @RequiresPermissions("operate:cloud:bonusBudget:remove")
    @Log(title = "删除奖金预算表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody BonusBudgetDTO bonusBudgetDTO)
    {
    return toAjax(bonusBudgetService.logicDeleteBonusBudgetByBonusBudgetId(bonusBudgetDTO));
    }
    /**
    * 批量修改奖金预算表
    */
    @RequiresPermissions("operate:cloud:bonusBudget:edits")
    @Log(title = "批量修改奖金预算表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<BonusBudgetDTO> bonusBudgetDtos)
    {
    return toAjax(bonusBudgetService.updateBonusBudgets(bonusBudgetDtos));
    }

    /**
    * 批量新增奖金预算表
    */
    @RequiresPermissions("operate:cloud:bonusBudget:insertBonusBudgets")
    @Log(title = "批量新增奖金预算表", businessType = BusinessType.INSERT)
    @PostMapping("/insertBonusBudgets")
    public AjaxResult insertBonusBudgets(@RequestBody List<BonusBudgetDTO> bonusBudgetDtos)
    {
    return toAjax(bonusBudgetService.insertBonusBudgets(bonusBudgetDtos));
    }

    /**
    * 逻辑批量删除奖金预算表
    */
    @RequiresPermissions("operate:cloud:bonusBudget:removes")
    @Log(title = "批量删除奖金预算表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  bonusBudgetIds)
    {
    return toAjax(bonusBudgetService.logicDeleteBonusBudgetByBonusBudgetIds(bonusBudgetIds));
    }
}