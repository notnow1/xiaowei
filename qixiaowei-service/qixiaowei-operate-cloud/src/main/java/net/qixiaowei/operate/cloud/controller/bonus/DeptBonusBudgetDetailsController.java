package net.qixiaowei.operate.cloud.controller.bonus;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptBonusBudgetDetailsDTO;
import net.qixiaowei.operate.cloud.service.bonus.IDeptBonusBudgetDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
*
* @author TANGMICHI
* @since 2022-11-29
*/
@RestController
@RequestMapping("deptBonusBudgetDetails")
public class DeptBonusBudgetDetailsController extends BaseController
{


    @Autowired
    private IDeptBonusBudgetDetailsService deptBonusBudgetDetailsService;



    /**
    * 查询部门奖金预算明细表详情
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetDetails:info")
    @GetMapping("/info/{deptBonusBudgetDetailsId}")
    public AjaxResult info(@PathVariable Long deptBonusBudgetDetailsId){
    DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO = deptBonusBudgetDetailsService.selectDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(deptBonusBudgetDetailsId);
        return AjaxResult.success(deptBonusBudgetDetailsDTO);
    }

    /**
    * 分页查询部门奖金预算明细表列表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetDetails:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO){
    startPage();
    List<DeptBonusBudgetDetailsDTO> list = deptBonusBudgetDetailsService.selectDeptBonusBudgetDetailsList(deptBonusBudgetDetailsDTO);
    return getDataTable(list);
    }

    /**
    * 查询部门奖金预算明细表列表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetDetails:list")
    @GetMapping("/list")
    public AjaxResult list(DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO){
    List<DeptBonusBudgetDetailsDTO> list = deptBonusBudgetDetailsService.selectDeptBonusBudgetDetailsList(deptBonusBudgetDetailsDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增部门奖金预算明细表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetDetails:add")
    @Log(title = "新增部门奖金预算明细表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO) {
    return AjaxResult.success(deptBonusBudgetDetailsService.insertDeptBonusBudgetDetails(deptBonusBudgetDetailsDTO));
    }


    /**
    * 修改部门奖金预算明细表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetDetails:edit")
    @Log(title = "修改部门奖金预算明细表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO)
    {
    return toAjax(deptBonusBudgetDetailsService.updateDeptBonusBudgetDetails(deptBonusBudgetDetailsDTO));
    }

    /**
    * 逻辑删除部门奖金预算明细表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetDetails:remove")
    @Log(title = "删除部门奖金预算明细表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO)
    {
    return toAjax(deptBonusBudgetDetailsService.logicDeleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsId(deptBonusBudgetDetailsDTO));
    }
    /**
    * 批量修改部门奖金预算明细表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetDetails:edits")
    @Log(title = "批量修改部门奖金预算明细表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDtos)
    {
    return toAjax(deptBonusBudgetDetailsService.updateDeptBonusBudgetDetailss(deptBonusBudgetDetailsDtos));
    }

    /**
    * 批量新增部门奖金预算明细表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetDetails:insertDeptBonusBudgetDetailss")
    @Log(title = "批量新增部门奖金预算明细表", businessType = BusinessType.INSERT)
    @PostMapping("/insertDeptBonusBudgetDetailss")
    public AjaxResult insertDeptBonusBudgetDetailss(@RequestBody List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDtos)
    {
    return toAjax(deptBonusBudgetDetailsService.insertDeptBonusBudgetDetailss(deptBonusBudgetDetailsDtos));
    }

    /**
    * 逻辑批量删除部门奖金预算明细表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetDetails:removes")
    @Log(title = "批量删除部门奖金预算明细表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  deptBonusBudgetDetailsIds)
    {
    return toAjax(deptBonusBudgetDetailsService.logicDeleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsIds(deptBonusBudgetDetailsIds));
    }
}
