package net.qixiaowei.operate.cloud.controller.targetManager;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
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
//    @RequiresPermissions("operate:cloud:targetDecomposeDimension:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        startPage();
        return getDataTable(targetDecomposeDimensionService.selectTargetDecomposeDimensionList(targetDecomposeDimensionDTO));
    }

    /**
     * 查询目标分解维度配置列表
     */
//    @RequiresPermissions("operate:cloud:targetDecomposeDimension:list")
    @GetMapping("/list")
    public AjaxResult list(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        return AjaxResult.success(targetDecomposeDimensionService.selectTargetDecomposeDimensionList(targetDecomposeDimensionDTO));
    }

    /**
     * 新增目标分解维度配置
     */
//    @RequiresPermissions("operate:cloud:targetDecomposeDimension:add")
    @Log(title = "新增目标分解维度配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        return toAjax(targetDecomposeDimensionService.insertTargetDecomposeDimension(targetDecomposeDimensionDTO));
    }

    /**
     * 逻辑删除目标分解维度配置
     */
//    @RequiresPermissions("operate:cloud:targetDecomposeDimension:remove")
    @Log(title = "删除目标分解维度配置", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> targetDecomposeDimensionIds) {
        return toAjax(targetDecomposeDimensionService.logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionIds(targetDecomposeDimensionIds));
    }


    /**
     * 批量修改目标分解维度配置
     */
//    @RequiresPermissions("operate:cloud:targetDecomposeDimension:edits")
    @Log(title = "批量修改目标分解维度配置", businessType = BusinessType.UPDATE)
    @PostMapping("/sort")
    public AjaxResult sort(@RequestBody List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDtos) {
        return toAjax(targetDecomposeDimensionService.updateTargetDecomposeDimensions(targetDecomposeDimensionDtos));
    }

}
