package net.qixiaowei.system.manage.controller.basic;

import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
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
    @RequiresPermissions(value = {"system:manage:industryDefault:pageList", "system:manage:industry:treeList", "system:manage:industry:pageList"}, logical = Logical.OR)
    @GetMapping("/pageList")
    public TableDataInfo pageList(IndustryDefaultDTO industryDefaultDTO) {
        startPage();
        List<IndustryDefaultDTO> list = industryDefaultService.selectIndustryDefaultPageList(industryDefaultDTO);
        return getDataTable(list);
    }

    /**
     * 查询默认行业列表
     */
    @RequiresPermissions(value = {"system:manage:industryDefault:treeList", "system:manage:industryDefault:pageList", "system:manage:industry:treeList", "system:manage:industry:pageList","system:manage:tenant:info:self", "system:manage:tenant:edit:self"}, logical = Logical.OR)
    @GetMapping("/treeList")
    public AjaxResult treeList(IndustryDefaultDTO industryDefaultDTO) {
        if (!CheckObjectIsNullUtils.isNull(industryDefaultDTO) || StringUtils.isNotEmpty(industryDefaultDTO.getParams())) {
            return AjaxResult.success(industryDefaultService.selectIndustryDefaultList(industryDefaultDTO));
        }
        return AjaxResult.success(industryDefaultService.selectIndustryDefaultTreeList(industryDefaultDTO));
    }

    /**
     * 查询默认行业列表
     */
    @GetMapping("/list")
    public AjaxResult list(IndustryDefaultDTO industryDefaultDTO) {
        List<IndustryDefaultDTO> list = industryDefaultService.selectIndustryDefaultList(industryDefaultDTO);
        return AjaxResult.success(list);
    }

    /**
     * 新增默认行业
     */
    @RequiresPermissions("system:manage:industryDefault:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody IndustryDefaultDTO industryDefaultDTO) {
        return toAjax(industryDefaultService.insertIndustryDefault(industryDefaultDTO));
    }

    /**
     * 修改默认行业
     */
    @RequiresPermissions("system:manage:industryDefault:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody IndustryDefaultDTO industryDefaultDTO) {
        return toAjax(industryDefaultService.updateIndustryDefault(industryDefaultDTO));
    }

    /**
     * 逻辑删除默认行业
     */
    @RequiresPermissions("system:manage:industryDefault:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody IndustryDefaultDTO industryDefaultDTO) {
        Long industryId = industryDefaultDTO.getIndustryId();
        if (StringUtils.isNull(industryId)) {
            return AjaxResult.error("默认行业id不能为空！");
        }
        return toAjax(industryDefaultService.logicDeleteIndustryDefaultByIndustryId(industryId));
    }

    /**
     * 逻辑批量删除默认行业
     */
    @RequiresPermissions("system:manage:industryDefault:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> industryIds) {
        return toAjax(industryDefaultService.logicDeleteIndustryDefaultByIndustryIds(industryIds));
    }

    /**
     * 行业配置详情
     */
    @RequiresPermissions(value = {"system:manage:industryDefault:info", "system:manage:industryDefault:edit"}, logical = Logical.OR)
    @GetMapping("/info/{industryId}")
    public AjaxResult info(@PathVariable Long industryId) {
        return AjaxResult.success(industryDefaultService.detailIndustryDefault(industryId));
    }

    /**
     * 获取指标最大层级
     */
    @GetMapping("/selectLevel")
    public AjaxResult level() {
        return AjaxResult.success(industryDefaultService.getLevel());
    }
}
