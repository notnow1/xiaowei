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
import net.qixiaowei.system.manage.api.dto.basic.IndustryDefaultDTO;
import net.qixiaowei.system.manage.service.basic.IIndustryDefaultService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author Graves
 * @since 2022-09-26
 */
@RestController
@RequestMapping("industryDefault")
public class IndustryDefaultController extends BaseController {


    @Autowired
    private IIndustryDefaultService industryDefaultService;

    /**
     * 分页查询默认行业列表
     */
    @RequiresPermissions("system:manage:industryDefault:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(IndustryDefaultDTO industryDefaultDTO) {
        startPage();
        List<IndustryDefaultDTO> list = industryDefaultService.selectIndustryDefaultList(industryDefaultDTO);
        return getDataTable(list);
    }

    /**
     * 查询默认行业列表
     */
    @RequiresPermissions("system:manage:industryDefault:list")
    @GetMapping("/list")
    public AjaxResult list(IndustryDefaultDTO industryDefaultDTO) {
        List<IndustryDefaultDTO> list = industryDefaultService.selectIndustryDefaultList(industryDefaultDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增默认行业
     */
    @RequiresPermissions("system:manage:industryDefault:add")
    @Log(title = "新增默认行业", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody IndustryDefaultDTO industryDefaultDTO) {
        int i = industryDefaultService.insertIndustryDefault(industryDefaultDTO);
        if (i == 0) {
            return AjaxResult.error(400, "行业编码不能重复");
        }
        return toAjax(i);

    }


    /**
     * 修改默认行业
     */
    @RequiresPermissions("system:manage:industryDefault:edit")
    @Log(title = "修改默认行业", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody IndustryDefaultDTO industryDefaultDTO) {
        return toAjax(industryDefaultService.updateIndustryDefault(industryDefaultDTO));
    }

    /**
     * 逻辑删除默认行业
     */
    @RequiresPermissions("system:manage:industryDefault:remove")
    @Log(title = "删除默认行业", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody IndustryDefaultDTO industryDefaultDTO) {
        return toAjax(industryDefaultService.logicDeleteIndustryDefaultByIndustryId(industryDefaultDTO));
    }

    /**
     * 批量修改默认行业
     */
    @RequiresPermissions("system:manage:industryDefault:edits")
    @Log(title = "批量修改默认行业", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<IndustryDefaultDTO> industryDefaultDtos) {
        return toAjax(industryDefaultService.updateIndustryDefaults(industryDefaultDtos));
    }

    /**
     * 批量新增默认行业
     */
    @RequiresPermissions("system:manage:industryDefault:insertIndustryDefaults")
    @Log(title = "批量新增默认行业", businessType = BusinessType.INSERT)
    @PostMapping("/insertIndustryDefaults")
    public AjaxResult insertIndustryDefaults(@RequestBody List<IndustryDefaultDTO> industryDefaultDtos) {
        return toAjax(industryDefaultService.insertIndustryDefaults(industryDefaultDtos));
    }

    /**
     * 逻辑批量删除默认行业
     */
    @RequiresPermissions("system:manage:industryDefault:removes")
    @Log(title = "批量删除默认行业", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<IndustryDefaultDTO> IndustryDefaultDtos) {
        return toAjax(industryDefaultService.logicDeleteIndustryDefaultByIndustryIds(IndustryDefaultDtos));
    }
}
