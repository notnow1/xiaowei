package net.qixiaowei.system.manage.controller.basic;

import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.service.basic.IIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Graves
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
    @RequiresPermissions("system:manage:indicator:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(IndicatorDTO indicatorDTO) {
        startPage();
        Integer indicatorType = indicatorDTO.getIndicatorType();
        if (StringUtils.isNull(indicatorType)) {
            indicatorDTO.setIndicatorType(1);
        }
        List<IndicatorDTO> list = indicatorService.selectIndicatorList(indicatorDTO);
        return getDataTable(list);
    }

    /**
     * 查询指标表树状图
     */
    @RequiresPermissions(value = {"system:manage:indicator:pageList", "system:manage:indicator:treeList"}, logical = Logical.OR)
    @GetMapping("/treeList")
    public AjaxResult treeList(IndicatorDTO indicatorDTO) {
        Integer indicatorType = indicatorDTO.getIndicatorType();
        indicatorDTO.setIndicatorType(null);
        if (!CheckObjectIsNullUtils.isNull(indicatorDTO)) {
            indicatorDTO.setIndicatorType(indicatorType);
            return AjaxResult.success(indicatorService.selectIndicatorList(indicatorDTO));
        }
        indicatorDTO.setIndicatorType(indicatorType);
        return AjaxResult.success(indicatorService.selectTreeList(indicatorDTO));
    }

    /**
     * 查询绩效的指标表树状图下拉
     */
    @RequiresPermissions(value = {"system:manage:indicator:pageList", "system:manage:indicator:treeList"}, logical = Logical.OR)
    @GetMapping("/performanceAppraisal/treeList")
    public AjaxResult performanceTreeList(IndicatorDTO indicatorDTO) {
        return AjaxResult.success(indicatorService.performanceTreeList(indicatorDTO));
    }

    /**
     * 新增指标表
     */
    @RequiresPermissions("system:manage:indicator:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody IndicatorDTO indicatorDTO) {
        return AjaxResult.success(indicatorService.insertIndicator(indicatorDTO));
    }

    /**
     * 修改指标表
     */
    @RequiresPermissions("system:manage:indicator:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody IndicatorDTO indicatorDTO) {
        return toAjax(indicatorService.updateIndicator(indicatorDTO));
    }

    /**
     * 指标详情
     */
    @RequiresPermissions(value = {"system:manage:indicator:info", "system:manage:indicator:edit"}, logical = Logical.OR)
    @GetMapping("/info/{indicatorId}")
    public AjaxResult info(@PathVariable Long indicatorId) {
        return AjaxResult.success(indicatorService.detailIndicator(indicatorId));
    }

    /**
     * 逻辑删除指标表
     */
    @RequiresPermissions("system:manage:indicator:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody IndicatorDTO indicatorDTO) {
        Long indicatorId = indicatorDTO.getIndicatorId();
        if (StringUtils.isNull(indicatorId)) {
            return AjaxResult.error("指标id不能为空！");
        }
        return toAjax(indicatorService.logicDeleteIndicatorByIndicatorId(indicatorId));
    }

    /**
     * 逻辑批量删除指标表
     */
    @RequiresPermissions("system:manage:indicator:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> indicatorIds) {
        return toAjax(indicatorService.logicDeleteIndicatorByIndicatorIds(indicatorIds));
    }

    /**
     * 获取指标最大层级
     */
    @RequiresPermissions(value = {"system:manage:indicator:pageList", "system:manage:indicator:treeList"}, logical = Logical.OR)
    @GetMapping("/selectLevel")
    public AjaxResult level() {
        return AjaxResult.success(indicatorService.getLevel());
    }


    /**
     * 查询指标表列表
     */
    @GetMapping("/list")
    public AjaxResult list(IndicatorDTO indicatorDTO) {
        List<IndicatorDTO> list = indicatorService.selectIndicatorList(indicatorDTO);
        return AjaxResult.success(list);
    }


}
