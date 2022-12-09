package net.qixiaowei.operate.cloud.controller.bonus;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.operate.cloud.api.dto.bonus.DeptBonusBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.DeptBonusBudgetDetailsDTO;
import net.qixiaowei.operate.cloud.service.bonus.IDeptBonusBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;



/**
*
* @author TANGMICHI
* @since 2022-11-29
*/
@RestController
@RequestMapping("deptBonusBudget")
public class DeptBonusBudgetController extends BaseController
{


    @Autowired
    private IDeptBonusBudgetService deptBonusBudgetService;


    /**
     * 返回部门奖金预算最大年份
     */
    //@RequiresPermissions("operate:cloud:bonusBudget:info")
    @GetMapping("/queryDeptBonusBudgetYear")
    public AjaxResult queryDeptBonusBudgetYear(DeptBonusBudgetDTO deptBonusBudgetDTO){
        return AjaxResult.success(deptBonusBudgetService.queryDeptBonusBudgetYear(deptBonusBudgetDTO));
    }
    /**
     *实时查询部门奖金包预算明细参考值数据
     */
    //@RequiresPermissions("operate:cloud:deptBonusBudget:add")
    //@Log(title = "新增部门奖金包预算表", businessType = BusinessType.INSERT)
    @PostMapping("/realTime")
    public AjaxResult realTimeQueryDeptBonusBudget(@RequestBody DeptBonusBudgetDTO deptBonusBudgetDTO) {
        List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOList = deptBonusBudgetService.realTimeQueryDeptBonusBudget(deptBonusBudgetDTO);
        return AjaxResult.success(deptBonusBudgetDetailsDTOList);
    }

    /**
     * 新增部门奖金包预算预制数据
     */
    //@RequiresPermissions("operate:cloud:deptBonusBudget:add")
    //@Log(title = "新增部门奖金包预算表", businessType = BusinessType.INSERT)
    @GetMapping("/add/{budgetYear}")
    public AjaxResult addDeptBonusBudgetTamount(@PathVariable int budgetYear) {
        DeptBonusBudgetDTO deptBonusBudgetDTO = deptBonusBudgetService.addDeptBonusBudgetTamount(budgetYear);
        return AjaxResult.success(deptBonusBudgetDTO);
    }

    /**
    * 查询部门奖金包预算表详情
    */
    //@RequiresPermissions("operate:cloud:deptBonusBudget:info")
    @GetMapping("/info/{deptBonusBudgetId}")
    public AjaxResult info(@PathVariable Long deptBonusBudgetId){
    DeptBonusBudgetDTO deptBonusBudgetDTO = deptBonusBudgetService.selectDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudgetId);
        return AjaxResult.success(deptBonusBudgetDTO);
    }

    /**
    * 分页查询部门奖金包预算表列表
    */
    //@RequiresPermissions("operate:cloud:deptBonusBudget:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(DeptBonusBudgetDTO deptBonusBudgetDTO){
    startPage();
    List<DeptBonusBudgetDTO> list = deptBonusBudgetService.selectDeptBonusBudgetList(deptBonusBudgetDTO);
    return getDataTable(list);
    }

    /**
    * 查询部门奖金包预算表列表
    */
    //@RequiresPermissions("operate:cloud:deptBonusBudget:list")
    @GetMapping("/list")
    public AjaxResult list(DeptBonusBudgetDTO deptBonusBudgetDTO){
    List<DeptBonusBudgetDTO> list = deptBonusBudgetService.selectDeptBonusBudgetList(deptBonusBudgetDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增部门奖金包预算表
    */
    //@RequiresPermissions("operate:cloud:deptBonusBudget:add")
    //@Log(title = "新增部门奖金包预算表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody DeptBonusBudgetDTO deptBonusBudgetDTO) {
    return AjaxResult.success(deptBonusBudgetService.insertDeptBonusBudget(deptBonusBudgetDTO));
    }


    /**
    * 修改部门奖金包预算表
    */
    //@RequiresPermissions("operate:cloud:deptBonusBudget:edit")
    //@Log(title = "修改部门奖金包预算表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(DeptBonusBudgetDTO.UpdateDeptBonusBudgetDTO.class) DeptBonusBudgetDTO deptBonusBudgetDTO)
    {
    return toAjax(deptBonusBudgetService.updateDeptBonusBudget(deptBonusBudgetDTO));
    }

    /**
    * 逻辑删除部门奖金包预算表
    */
    //@RequiresPermissions("operate:cloud:deptBonusBudget:remove")
    //@Log(title = "删除部门奖金包预算表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(DeptBonusBudgetDTO.DeleteDeptBonusBudgetDTO.class) DeptBonusBudgetDTO deptBonusBudgetDTO)
    {
    return toAjax(deptBonusBudgetService.logicDeleteDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudgetDTO));
    }
    /**
    * 批量修改部门奖金包预算表
    */
    //@RequiresPermissions("operate:cloud:deptBonusBudget:edits")
    //@Log(title = "批量修改部门奖金包预算表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<DeptBonusBudgetDTO> deptBonusBudgetDtos)
    {
    return toAjax(deptBonusBudgetService.updateDeptBonusBudgets(deptBonusBudgetDtos));
    }

    /**
    * 批量新增部门奖金包预算表
    */
    //@RequiresPermissions("operate:cloud:deptBonusBudget:insertDeptBonusBudgets")
    //@Log(title = "批量新增部门奖金包预算表", businessType = BusinessType.INSERT)
    @PostMapping("/insertDeptBonusBudgets")
    public AjaxResult insertDeptBonusBudgets(@RequestBody List<DeptBonusBudgetDTO> deptBonusBudgetDtos)
    {
    return toAjax(deptBonusBudgetService.insertDeptBonusBudgets(deptBonusBudgetDtos));
    }

    /**
    * 逻辑批量删除部门奖金包预算表
    */
    //@RequiresPermissions("operate:cloud:deptBonusBudget:removes")
    //@Log(title = "批量删除部门奖金包预算表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  deptBonusBudgetIds)
    {
    return toAjax(deptBonusBudgetService.logicDeleteDeptBonusBudgetByDeptBonusBudgetIds(deptBonusBudgetIds));
    }
}
