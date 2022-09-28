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
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.service.basic.IIndicatorService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author TANGMICHI
 * @since 2022-09-28
 */
@RestController
@RequestMapping("indicator")
public class IndicatorController extends BaseController {


    @Autowired
    private IIndicatorService indicatorService;

    /**
     * 分页查询指标表列表
     */
//    @RequiresPermissions("system:manage:indicator:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(IndicatorDTO indicatorDTO) {
        startPage();
        List<IndicatorDTO> list = indicatorService.selectIndicatorList(indicatorDTO);
        return getDataTable(list);
    }

    /**
     * 查询指标表列表
     */
//    @RequiresPermissions("system:manage:indicator:list")
    @GetMapping("/list")
    public AjaxResult list(IndicatorDTO indicatorDTO) {
        List<IndicatorDTO> list = indicatorService.selectIndicatorList(indicatorDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增指标表
     */
//    @RequiresPermissions("system:manage:indicator:add")
    @Log(title = "新增指标表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody IndicatorDTO indicatorDTO) {
        return toAjax(indicatorService.insertIndicator(indicatorDTO));
    }


    /**
     * 修改指标表
     */
//    @RequiresPermissions("system:manage:indicator:edit")
    @Log(title = "修改指标表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody IndicatorDTO indicatorDTO) {
        return toAjax(indicatorService.updateIndicator(indicatorDTO));
    }

    /**
     * 逻辑删除指标表
     */
//    @RequiresPermissions("system:manage:indicator:remove")
    @Log(title = "删除指标表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody IndicatorDTO indicatorDTO) {
        return toAjax(indicatorService.logicDeleteIndicatorByIndicatorId(indicatorDTO));
    }

    /**
     * 批量修改指标表
     */
//    @RequiresPermissions("system:manage:indicator:edits")
    @Log(title = "批量修改指标表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<IndicatorDTO> indicatorDtos) {
        return toAjax(indicatorService.updateIndicators(indicatorDtos));
    }

    /**
     * 批量新增指标表
     */
//    @RequiresPermissions("system:manage:indicator:insertIndicators")
    @Log(title = "批量新增指标表", businessType = BusinessType.INSERT)
    @PostMapping("/insertIndicators")
    public AjaxResult insertIndicators(@RequestBody List<IndicatorDTO> indicatorDtos) {
        return toAjax(indicatorService.insertIndicators(indicatorDtos));
    }

    /**
     * 逻辑批量删除指标表
     */
//    @RequiresPermissions("system:manage:indicator:removes")
    @Log(title = "批量删除指标表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<IndicatorDTO> IndicatorDtos) {
        return toAjax(indicatorService.logicDeleteIndicatorByIndicatorIds(IndicatorDtos));
    }
}
