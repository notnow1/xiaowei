package net.qixiaowei.system.manage.controller.basic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorCategoryDTO;
import net.qixiaowei.system.manage.service.basic.IIndicatorCategoryService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author Graves
 * @since 2022-09-28
 */
@RestController
@RequestMapping("indicatorCategory")
public class IndicatorCategoryController extends BaseController {


    @Autowired
    private IIndicatorCategoryService indicatorCategoryService;

    /**
     * 分页查询指标分类表列表
     */
//    @RequiresPermissions("system:manage:indicatorCategory:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(IndicatorCategoryDTO indicatorCategoryDTO) {
        startPage();
        List<IndicatorCategoryDTO> list = indicatorCategoryService.selectIndicatorCategoryList(indicatorCategoryDTO);
        return getDataTable(list);
    }

    /**
     * 查询指标分类表列表
     */
//    @RequiresPermissions("system:manage:indicatorCategory:list")
    @GetMapping("/list")
    public AjaxResult list(IndicatorCategoryDTO indicatorCategoryDTO) {
        List<IndicatorCategoryDTO> list = indicatorCategoryService.selectIndicatorCategoryList(indicatorCategoryDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增指标分类表
     */
//    @RequiresPermissions("system:manage:indicatorCategory:add")
    @Log(title = "新增指标分类表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody IndicatorCategoryDTO indicatorCategoryDTO) {
        return toAjax(indicatorCategoryService.insertIndicatorCategory(indicatorCategoryDTO));
    }


    /**
     * 修改指标分类表
     */
//    @RequiresPermissions("system:manage:indicatorCategory:edit")
    @Log(title = "修改指标分类表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody IndicatorCategoryDTO indicatorCategoryDTO) {
        return toAjax(indicatorCategoryService.updateIndicatorCategory(indicatorCategoryDTO));
    }

    /**
     * 逻辑删除指标分类表
     */
//    @RequiresPermissions("system:manage:indicatorCategory:remove")
    @Log(title = "删除指标分类表", businessType = BusinessType.DELETE)
    @GetMapping("/remove")
    public AjaxResult remove(Long indicatorId) {
        return toAjax(indicatorCategoryService.logicDeleteIndicatorCategoryByIndicatorCategoryId(indicatorId));
    }

    /**
     * 批量修改指标分类表
     */
//    @RequiresPermissions("system:manage:indicatorCategory:edits")
    @Log(title = "批量修改指标分类表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<IndicatorCategoryDTO> indicatorCategoryDtos) {
        return toAjax(indicatorCategoryService.updateIndicatorCategorys(indicatorCategoryDtos));
    }

    /**
     * 批量新增指标分类表
     */
//    @RequiresPermissions("system:manage:indicatorCategory:insertIndicatorCategorys")
    @Log(title = "批量新增指标分类表", businessType = BusinessType.INSERT)
    @PostMapping("/insertIndicatorCategorys")
    public AjaxResult insertIndicatorCategorys(@RequestBody List<IndicatorCategoryDTO> indicatorCategoryDtos) {
        return toAjax(indicatorCategoryService.insertIndicatorCategorys(indicatorCategoryDtos));
    }

    /**
     * 逻辑批量删除指标分类表
     */
//    @RequiresPermissions("system:manage:indicatorCategory:removes")
    @Log(title = "批量删除指标分类表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> indicatorCategoryIds) {
        return toAjax(indicatorCategoryService.logicDeleteIndicatorCategoryByIndicatorCategoryIds(indicatorCategoryIds));
    }
}
