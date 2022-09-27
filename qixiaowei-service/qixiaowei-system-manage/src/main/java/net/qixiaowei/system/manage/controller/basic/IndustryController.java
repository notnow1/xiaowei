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
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.service.basic.IIndustryService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author Graves
 * @since 2022-09-26
 */
@RestController
@RequestMapping("industry")
public class IndustryController extends BaseController {


    @Autowired
    private IIndustryService industryService;

    /**
     * 分页查询行业列表
     */
//    @RequiresPermissions("system:manage:industry:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(IndustryDTO industryDTO) {
        startPage();
        List<IndustryDTO> list = industryService.selectIndustryList(industryDTO);
        return getDataTable(list);
    }

    /**
     * 查询行业列表
     */
//    @RequiresPermissions("system:manage:industry:list")
    @GetMapping("/list")
    public AjaxResult list(IndustryDTO industryDTO) {
        List<IndustryDTO> list = industryService.selectIndustryList(industryDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增行业
     */
//    @RequiresPermissions("system:manage:industry:add")
    @Log(title = "新增行业", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody IndustryDTO industryDTO) {
        return toAjax(industryService.insertIndustry(industryDTO));
    }


    /**
     * 修改行业
     */
//    @RequiresPermissions("system:manage:industry:edit")
    @Log(title = "修改行业", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody IndustryDTO industryDTO) {
        return toAjax(industryService.updateIndustry(industryDTO));
    }

    /**
     * 逻辑删除行业
     */
//    @RequiresPermissions("system:manage:industry:remove")
    @Log(title = "删除行业", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody IndustryDTO industryDTO) {
        return toAjax(industryService.logicDeleteIndustryByIndustryId(industryDTO));
    }

    /**
     * 批量修改行业
     */
//    @RequiresPermissions("system:manage:industry:edits")
    @Log(title = "批量修改行业", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<IndustryDTO> industryDtos) {
        return toAjax(industryService.updateIndustrys(industryDtos));
    }

    /**
     * 批量新增行业
     */
//    @RequiresPermissions("system:manage:industry:insertIndustrys")
    @Log(title = "批量新增行业", businessType = BusinessType.INSERT)
    @PostMapping("/insertIndustrys")
    public AjaxResult insertIndustrys(@RequestBody List<IndustryDTO> industryDtos) {
        return toAjax(industryService.insertIndustrys(industryDtos));
    }

    /**
     * 逻辑批量删除行业
     */
//    @RequiresPermissions("system:manage:industry:removes")
    @Log(title = "批量删除行业", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<IndustryDTO> IndustryDtos) {
        return toAjax(industryService.logicDeleteIndustryByIndustryIds(IndustryDtos));
    }
}
