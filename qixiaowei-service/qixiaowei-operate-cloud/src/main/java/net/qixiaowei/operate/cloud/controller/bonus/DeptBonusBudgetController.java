package net.qixiaowei.operate.cloud.controller.bonus;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.bonus.DeptBonusBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.DeptBonusBudgetDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.EmolumentPlanDTO;
import net.qixiaowei.operate.cloud.service.bonus.IDeptBonusBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author TANGMICHI
 * @since 2022-11-29
 */
@RestController
@RequestMapping("deptBonusBudget")
public class DeptBonusBudgetController extends BaseController {


    @Autowired
    private IDeptBonusBudgetService deptBonusBudgetService;


    /**
     * 分页查询部门奖金包预算表列表
     */
    @RequiresPermissions("operate:cloud:deptBonusBudget:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        startPage();
        List<DeptBonusBudgetDTO> list = deptBonusBudgetService.selectDeptBonusBudgetList(deptBonusBudgetDTO);
        return getDataTable(list);
    }

    /**
     * 新增部门奖金包预算表
     */
    @Log(title = "新增部门奖金包预算", businessType = BusinessType.DEPT_BONUS_BUDGET, businessId = "deptBonusBudgetId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:deptBonusBudget:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody DeptBonusBudgetDTO deptBonusBudgetDTO) {
        return AjaxResult.success(deptBonusBudgetService.insertDeptBonusBudget(deptBonusBudgetDTO));
    }

    /**
     * 修改部门奖金包预算表
     */
    @Log(title = "保存部门奖金包预算", businessType = BusinessType.DEPT_BONUS_BUDGET, businessId = "deptBonusBudgetId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:deptBonusBudget:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(DeptBonusBudgetDTO.UpdateDeptBonusBudgetDTO.class) DeptBonusBudgetDTO deptBonusBudgetDTO) {
        return toAjax(deptBonusBudgetService.updateDeptBonusBudget(deptBonusBudgetDTO));
    }

    /**
     * 查询部门奖金包预算表详情
     */
    @RequiresPermissions("operate:cloud:deptBonusBudget:info")
    @GetMapping("/info/{deptBonusBudgetId}")
    public AjaxResult info(@PathVariable Long deptBonusBudgetId) {
        DeptBonusBudgetDTO deptBonusBudgetDTO = deptBonusBudgetService.selectDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudgetId);
        return AjaxResult.success(deptBonusBudgetDTO);
    }

    /**
     * 逻辑删除部门奖金包预算表
     */
    @RequiresPermissions("operate:cloud:deptBonusBudget:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(DeptBonusBudgetDTO.DeleteDeptBonusBudgetDTO.class) DeptBonusBudgetDTO deptBonusBudgetDTO) {
        return toAjax(deptBonusBudgetService.logicDeleteDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudgetDTO));
    }

    /**
     * 逻辑批量删除部门奖金包预算表
     */
    @RequiresPermissions("operate:cloud:deptBonusBudget:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> deptBonusBudgetIds) {
        return toAjax(deptBonusBudgetService.logicDeleteDeptBonusBudgetByDeptBonusBudgetIds(deptBonusBudgetIds));
    }

    /**
     * 返回部门奖金预算最大年份
     */
    @RequiresPermissions(value = {"operate:cloud:deptBonusBudget:add", "operate:cloud:deptBonusBudget:edit"}, logical = Logical.OR)
    @GetMapping("/queryDeptBonusBudgetYear")
    public AjaxResult queryDeptBonusBudgetYear(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        return AjaxResult.success(deptBonusBudgetService.queryDeptBonusBudgetYear(deptBonusBudgetDTO));
    }

    /**
     * 实时查询部门奖金包预算明细参考值数据
     */
    @RequiresPermissions(value = {"operate:cloud:deptBonusBudget:add", "operate:cloud:deptBonusBudget:edit"}, logical = Logical.OR)
    @PostMapping("/realTime")
    public AjaxResult realTimeQueryDeptBonusBudget(@RequestBody DeptBonusBudgetDTO deptBonusBudgetDTO) {
        List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOList = deptBonusBudgetService.realTimeQueryDeptBonusBudget(deptBonusBudgetDTO);
        return AjaxResult.success(deptBonusBudgetDetailsDTOList);
    }

    /**
     * 新增部门奖金包预算预制数据
     */
    @RequiresPermissions(value = {"operate:cloud:deptBonusBudget:add", "operate:cloud:deptBonusBudget:edit"}, logical = Logical.OR)
    @GetMapping("/add/{budgetYear}")
    public AjaxResult addDeptBonusBudgetTamount(@PathVariable int budgetYear) {
        DeptBonusBudgetDTO deptBonusBudgetDTO = deptBonusBudgetService.addDeptBonusBudgetTamount(budgetYear);
        return AjaxResult.success(deptBonusBudgetDTO);
    }

    /**
     * 查询部门奖金包预算表列表
     */
    @RequiresPermissions("operate:cloud:deptBonusBudget:pageList")
    @GetMapping("/list")
    public AjaxResult list(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        List<DeptBonusBudgetDTO> list = deptBonusBudgetService.selectDeptBonusBudgetList(deptBonusBudgetDTO);
        return AjaxResult.success(list);
    }


    /**
     * 查询部门奖金包预算表列表(返回年份List)
     */
    @RequiresPermissions("operate:cloud:deptBonusBudget:add")
    @GetMapping("/getExistYear")
    public AjaxResult getExistYear(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        List<String> listYears = new ArrayList<>();
        List<DeptBonusBudgetDTO> list = deptBonusBudgetService.selectDeptBonusBudgetList(deptBonusBudgetDTO);
        if (StringUtils.isNotEmpty(list)){
            listYears = list.stream().filter(f -> f.getBudgetYear() != null).map(DeptBonusBudgetDTO::getBudgetYear).collect(Collectors.toList()).stream().map(String::valueOf).collect(Collectors.toList());
        }
        return AjaxResult.success(listYears);
    }

}
