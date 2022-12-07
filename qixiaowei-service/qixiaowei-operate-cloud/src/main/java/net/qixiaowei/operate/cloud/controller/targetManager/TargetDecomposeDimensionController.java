package net.qixiaowei.operate.cloud.controller.targetManager;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDimensionDTO;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeDimensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Graves
 * @since 2022-09-26
 */
@RestController
@RequestMapping("targetDecomposeDimension")
public class TargetDecomposeDimensionController extends BaseController {

    @Autowired
    private ITargetDecomposeDimensionService targetDecomposeDimensionService;

    /**
     * 分页查询目标分解维度配置列表
     */
    @RequiresPermissions("operate:cloud:targetDecomposeDimension:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        startPage();
        return getDataTable(targetDecomposeDimensionService.selectTargetDecomposeDimensionList(targetDecomposeDimensionDTO));
    }

    /**
     * 查询目标分解维度配置列表
     */
    @RequiresPermissions(value = {"operate:cloud:targetDecomposeDimension:list", "operate:cloud:targetDecomposeDimension:pageList"}, logical = Logical.OR)
    @GetMapping("/list")
    public AjaxResult list(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        return AjaxResult.success(targetDecomposeDimensionService.selectTargetDecomposeDimensionList(targetDecomposeDimensionDTO));
    }

    /**
     * 新增目标分解维度配置
     */
    @RequiresPermissions("operate:cloud:targetDecomposeDimension:add")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        return toAjax(targetDecomposeDimensionService.insertTargetDecomposeDimension(targetDecomposeDimensionDTO));
    }

    /**
     * 逻辑删除目标分解维度配置
     */
    @RequiresPermissions("operate:cloud:targetDecomposeDimension:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody TargetDecomposeDimensionDTO targetDecomposeDimensionDto) {
        return toAjax(targetDecomposeDimensionService.logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionId(targetDecomposeDimensionDto));
    }

    /**
     * 逻辑批量删除目标分解维度配置
     */
    @RequiresPermissions("operate:cloud:targetDecomposeDimension:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> targetDecomposeDimensionIds) {
        return toAjax(targetDecomposeDimensionService.logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionIds(targetDecomposeDimensionIds));
    }

    /**
     * 批量修改目标分解维度配置
     */
    @RequiresPermissions("operate:cloud:targetDecomposeDimension:edits")
    @PostMapping("/sort")
    public AjaxResult sort(@RequestBody List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDtos) {
        return toAjax(targetDecomposeDimensionService.updateTargetDecomposeDimensions(targetDecomposeDimensionDtos));
    }


}
