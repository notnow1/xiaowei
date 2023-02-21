package net.qixiaowei.system.manage.controller.basic;

import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorCategoryDTO;
import net.qixiaowei.system.manage.service.basic.IIndicatorCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    @RequiresPermissions("system:manage:indicatorCategory:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(IndicatorCategoryDTO indicatorCategoryDTO) {
        startPage();
        List<IndicatorCategoryDTO> list = indicatorCategoryService.selectIndicatorCategoryList(indicatorCategoryDTO);
        return getDataTable(list);
    }

    /**
     * 生成指标分类编码
     *
     * @return 指标分类编码
     */
    @RequiresPermissions(value = {"system:manage:indicatorCategory:add", "system:manage:indicatorCategory:edit"}, logical = Logical.OR)
    @GetMapping("/generate/indicatorCategoryCode")
    public AjaxResult generateIndicatorCategoryCode(@RequestParam(value = "indicatorType", required = false, defaultValue = "1") Integer indicatorType) {
        return AjaxResult.success("操作成功",indicatorCategoryService.generateIndicatorCategoryCode(indicatorType));
    }

    /**
     * 新增指标分类表
     */
    @RequiresPermissions("system:manage:indicatorCategory:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody IndicatorCategoryDTO indicatorCategoryDTO) {
        return AjaxResult.success(indicatorCategoryService.insertIndicatorCategory(indicatorCategoryDTO));
    }

    /**
     * 修改指标分类表
     */
    @RequiresPermissions("system:manage:indicatorCategory:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody IndicatorCategoryDTO indicatorCategoryDTO) {
        return toAjax(indicatorCategoryService.updateIndicatorCategory(indicatorCategoryDTO));
    }

    /**
     * 指标类型详情
     */
    @RequiresPermissions(value = {"operate:cloud:performanceRank:info", "operate:cloud:performanceRank:edit"}, logical = Logical.OR)
    @GetMapping("/info/{indicatorCategoryId}")
    public AjaxResult info(@PathVariable Long indicatorCategoryId) {
        return AjaxResult.success(indicatorCategoryService.detailIndicatorCategory(indicatorCategoryId));
    }

    /**
     * 逻辑删除指标分类表
     */
    @RequiresPermissions("system:manage:indicatorCategory:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody IndicatorCategoryDTO indicatorCategoryDTO) {
        Long indicatorCategoryId = indicatorCategoryDTO.getIndicatorCategoryId();
        if (StringUtils.isNull(indicatorCategoryId)) {
            return AjaxResult.error("指标分类id不能为空！");
        }
        return toAjax(indicatorCategoryService.logicDeleteIndicatorCategoryByIndicatorCategoryId(indicatorCategoryId));
    }

    /**
     * 逻辑批量删除指标分类表
     */
    @RequiresPermissions("system:manage:indicatorCategory:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> indicatorCategoryIds) {
        return toAjax(indicatorCategoryService.logicDeleteIndicatorCategoryByIndicatorCategoryIds(indicatorCategoryIds));
    }


    /**
     * 查询指标分类表列表
     */
    @GetMapping("/list")
    public AjaxResult list(IndicatorCategoryDTO indicatorCategoryDTO) {
        List<IndicatorCategoryDTO> list = indicatorCategoryService.selectIndicatorCategoryList(indicatorCategoryDTO);
        return AjaxResult.success(list);
    }


}
