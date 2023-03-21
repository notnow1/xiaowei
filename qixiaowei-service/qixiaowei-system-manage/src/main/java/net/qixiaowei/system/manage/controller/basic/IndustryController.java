package net.qixiaowei.system.manage.controller.basic;

import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.service.basic.IIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    @RequiresPermissions("system:manage:industry:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(IndustryDTO industryDTO) {
        startPage();
        List<IndustryDTO> list = industryService.selectIndustryPageList(industryDTO);
        return getDataTable(list);
    }

    /**
     * 查询行业树结构列表
     */
    @RequiresPermissions(value = {"system:manage:industry:treeList", "system:manage:industry:pageList"}, logical = Logical.OR)
    @GetMapping("/treeList")
    public AjaxResult treeList(IndustryDTO industryDTO) {
        if (!CheckObjectIsNullUtils.isNull(industryDTO)) {
            return AjaxResult.success(industryService.selectIndustryList(industryDTO));
        }
        return AjaxResult.success(industryService.selectIndustryTreeList(industryDTO));
    }

    /**
     * 获取上级行业
     */
    @RequiresPermissions(value = {"system:manage:industry:treeList", "system:manage:industry:pageList"}, logical = Logical.OR)
    @GetMapping("/getSuperIndustry/{industryId}")
    public AjaxResult getSuperIndustry(@PathVariable Long industryId) {
        return AjaxResult.success(industryService.getSuperIndustry(industryId));
    }

    /**
     * 获取启用行业列表
     */
    @RequiresPermissions(value = {"system:manage:industry:treeList", "system:manage:industry:pageList"}, logical = Logical.OR)
    @GetMapping("/enableList")
    public AjaxResult enableList(IndustryDTO industryDTO) {
        return AjaxResult.success(industryService.getEnableList(industryDTO));
    }

    /**
     * 获取启用行业类型
     */
    @RequiresPermissions(value = {"system:manage:industry:treeList", "system:manage:industry:pageList"}, logical = Logical.OR)
    @GetMapping("/enableType")
    public AjaxResult enableType(IndustryDTO industryDTO) {
        return AjaxResult.success(industryService.getEnableType(industryDTO));
    }

    /**
     * 修改启用行业类型
     */
    @RequiresPermissions(value = {"system:manage:industry:treeList", "system:manage:industry:pageList"}, logical = Logical.OR)
    @PostMapping("/enableEdit")
    public AjaxResult enableEdit(@RequestBody IndustryDTO industryDTO) {
        Integer configValue = industryDTO.getConfigValue();
        return AjaxResult.success("切换成功", industryService.updateEnableType(configValue));
    }

    /**
     * 生成行业编码
     *
     * @return 行业编码
     */
    @RequiresPermissions(value = {"system:manage:industry:add", "system:manage:industry:edit"}, logical = Logical.OR)
    @GetMapping("/generate/industryCode")
    public AjaxResult generateIndustryCode() {
        return AjaxResult.success("操作成功", industryService.generateIndustryCode());
    }

    /**
     * 新增行业
     */
    @RequiresPermissions("system:manage:industry:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody IndustryDTO industryDTO) {
        return AjaxResult.success("新增成功", industryService.insertIndustry(industryDTO));
    }

    /**
     * 修改行业
     */
    @RequiresPermissions("system:manage:industry:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody IndustryDTO industryDTO) {
        return toAjax(industryService.updateIndustry(industryDTO));
    }

    /**
     * 行业配置详情
     */
    @RequiresPermissions(value = {"system:manage:industry:info", "system:manage:industry:edit"}, logical = Logical.OR)
    @GetMapping("/info/{industryId}")
    public AjaxResult info(@PathVariable Long industryId) {
        return AjaxResult.success(industryService.detailIndustry(industryId));
    }

    /**
     * 逻辑删除行业
     */
    @RequiresPermissions("system:manage:industry:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody IndustryDTO industryDTO) {
        Long industryId = industryDTO.getIndustryId();
        if (StringUtils.isNull(industryId)) {
            return AjaxResult.error("行业id不能为空！");
        }
        return toAjax(industryService.logicDeleteIndustryByIndustryId(industryId));
    }

    /**
     * 逻辑批量删除行业
     */
    @RequiresPermissions("system:manage:industry:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> industryIds) {
        return toAjax(industryService.logicDeleteIndustryByIndustryIds(industryIds));
    }

    /**
     * 获取指标最大层级
     */
    @RequiresPermissions(value = {"system:manage:industry:treeList", "system:manage:industry:pageList"}, logical = Logical.OR)
    @GetMapping("/selectLevel")
    public AjaxResult level() {
        return AjaxResult.success(industryService.getLevel());
    }


    /**
     * 查询行业列表
     */
    @GetMapping("/list")
    public AjaxResult list(IndustryDTO industryDTO) {
        industryDTO.setStatus(1);
        List<IndustryDTO> list = industryService.selectIndustryList(industryDTO);
        return AjaxResult.success(list);
    }
}
