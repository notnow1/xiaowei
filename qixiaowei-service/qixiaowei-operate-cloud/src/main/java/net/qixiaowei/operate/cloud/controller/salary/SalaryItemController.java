package net.qixiaowei.operate.cloud.controller.salary;

import java.util.List;

import net.qixiaowei.integration.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
import net.qixiaowei.operate.cloud.service.salary.ISalaryItemService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author Graves
 * @since 2022-10-05
 */
@RestController
@RequestMapping("salaryItem")
public class SalaryItemController extends BaseController {


    @Autowired
    private ISalaryItemService salaryItemService;

    /**
     * 分页查询工资项列表
     */
//    @RequiresPermissions("operate:cloud:salaryItem:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(SalaryItemDTO salaryItemDTO) {
        startPage();
        List<SalaryItemDTO> list = salaryItemService.selectSalaryItemList(salaryItemDTO);
        return getDataTable(list);
    }

    /**
     * 查询工资项列表
     */
//    @RequiresPermissions("operate:cloud:salaryItem:list")
    @GetMapping("/list")
    public AjaxResult list(SalaryItemDTO salaryItemDTO) {
        List<SalaryItemDTO> list = salaryItemService.selectSalaryItemList(salaryItemDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增工资项
     */
//    @RequiresPermissions("operate:cloud:salaryItem:add")
    @Log(title = "新增工资项", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody SalaryItemDTO salaryItemDTO) {
        return toAjax(salaryItemService.insertSalaryItem(salaryItemDTO));
    }


    /**
     * 修改工资项
     */
//    @RequiresPermissions("operate:cloud:salaryItem:edit")
    @Log(title = "修改工资项", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody SalaryItemDTO salaryItemDTO) {
        return toAjax(salaryItemService.updateSalaryItem(salaryItemDTO));
    }

    /**
     * 逻辑删除工资项
     */
//    @RequiresPermissions("operate:cloud:salaryItem:remove")
    @Log(title = "删除工资项", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody SalaryItemDTO salaryItemDTO) {
        Long salaryItemId = salaryItemDTO.getSalaryItemId();
        if (StringUtils.isNull(salaryItemId)) {
            return AjaxResult.error("工资条配置id不能为空！");
        }
        return toAjax(salaryItemService.logicDeleteSalaryItemBySalaryItemId(salaryItemDTO));
    }

    /**
     * 批量修改工资项
     */
//    @RequiresPermissions("operate:cloud:salaryItem:edits")
    @Log(title = "批量修改工资项", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<SalaryItemDTO> salaryItemDtos) {
        return toAjax(salaryItemService.updateSalaryItems(salaryItemDtos));
    }

    /**
     * 批量新增工资项
     */
//    @RequiresPermissions("operate:cloud:salaryItem:insertSalaryItems")
    @Log(title = "批量新增工资项", businessType = BusinessType.INSERT)
    @PostMapping("/insertSalaryItems")
    public AjaxResult insertSalaryItems(@RequestBody List<SalaryItemDTO> salaryItemDtos) {
        return toAjax(salaryItemService.insertSalaryItems(salaryItemDtos));
    }

    /**
     * 逻辑批量删除工资项
     */
//    @RequiresPermissions("operate:cloud:salaryItem:removes")
    @Log(title = "批量删除工资项", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> salaryItemIds) {
        return toAjax(salaryItemService.logicDeleteSalaryItemBySalaryItemIds(salaryItemIds));
    }
}
