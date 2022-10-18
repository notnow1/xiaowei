package net.qixiaowei.system.manage.controller.basic;

import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDefaultDTO;
import net.qixiaowei.system.manage.service.basic.IIndustryDefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
//    @RequiresPermissions("system:manage:industryDefault:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(IndustryDefaultDTO industryDefaultDTO) {
        startPage();
        List<IndustryDefaultDTO> list = industryDefaultService.selectIndustryDefaultPageList(industryDefaultDTO);
        return getDataTable(list);
    }

    /**
     * 查询默认行业列表
     */
//    @RequiresPermissions("system:manage:industryDefault:list")
    @GetMapping("/list")
    public AjaxResult list(IndustryDefaultDTO industryDefaultDTO) {
        List<IndustryDefaultDTO> list = industryDefaultService.selectIndustryDefaultList(industryDefaultDTO);
        return AjaxResult.success(list);
    }

    /**
     * 查询默认行业列表
     */
//    @RequiresPermissions("system:manage:industryDefault:list")
    @GetMapping("/treeList")
    public AjaxResult treeList(IndustryDefaultDTO industryDefaultDTO) {
        List<IndustryDefaultDTO> list = industryDefaultService.selectIndustryDefaultTreeList(industryDefaultDTO);
        return AjaxResult.success(list);
    }

    /**
     * 新增默认行业
     */
//    @RequiresPermissions("system:manage:industryDefault:add")
    @Log(title = "新增默认行业", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody IndustryDefaultDTO industryDefaultDTO) {
        return toAjax(industryDefaultService.insertIndustryDefault(industryDefaultDTO));
    }


    /**
     * 修改默认行业
     */
//    @RequiresPermissions("system:manage:industryDefault:edit")
    @Log(title = "修改默认行业", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody IndustryDefaultDTO industryDefaultDTO) {
        return toAjax(industryDefaultService.updateIndustryDefault(industryDefaultDTO));
    }

    /**
     * 逻辑删除默认行业
     */
//    @RequiresPermissions("system:manage:industryDefault:remove")
    @Log(title = "删除默认行业", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody IndustryDefaultDTO industryDefaultDTO) {
        Long industryId = industryDefaultDTO.getIndustryId();
        if (StringUtils.isNull(industryId)) {
            return AjaxResult.error("默认行业id不能为空！");
        }
        return toAjax(industryDefaultService.logicDeleteIndustryDefaultByIndustryId(industryId));
    }

    /**
     * 批量修改默认行业
     */
//    @RequiresPermissions("system:manage:industryDefault:edits")
    @Log(title = "批量修改默认行业", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<IndustryDefaultDTO> industryDefaultDtos) {
        return toAjax(industryDefaultService.updateIndustryDefaults(industryDefaultDtos));
    }

    /**
     * 批量新增默认行业
     */
//    @RequiresPermissions("system:manage:industryDefault:insertIndustryDefaults")
    @Log(title = "批量新增默认行业", businessType = BusinessType.INSERT)
    @PostMapping("/insertIndustryDefaults")
    public AjaxResult insertIndustryDefaults(@RequestBody List<IndustryDefaultDTO> industryDefaultDtos) {
        return toAjax(industryDefaultService.insertIndustryDefaults(industryDefaultDtos));
    }

    /**
     * 逻辑批量删除默认行业
     */
//    @RequiresPermissions("system:manage:industryDefault:removes")
    @Log(title = "批量删除默认行业", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> industryIds) {
        return toAjax(industryDefaultService.logicDeleteIndustryDefaultByIndustryIds(industryIds));
    }

    /**
     * 行业配置详情
     */
//    @RequiresPermissions("operate:cloud:performanceRank:edit")
    @Log(title = "默认行业配置详情")
    @GetMapping("/info/{industryId}")
    public AjaxResult info(@PathVariable Long industryId) {
        return AjaxResult.success(industryDefaultService.detailIndustryDefault(industryId));
    }

    /**
     * 获取指标最大层级
     */
//    @RequiresPermissions("system:manage:indicator:list")
    @GetMapping("/selectLevel")
    public AjaxResult level() {
        return AjaxResult.success(industryDefaultService.getLevel());
    }
}
