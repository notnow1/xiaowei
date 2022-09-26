package net.qixiaowei.operate.cloud.controller.targetManager;

import java.util.ArrayList;
import java.util.List;

import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecomposeDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDimensionDTO;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeDimensionService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


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
        List<TargetDecomposeDimensionDTO> list = targetDecomposeDimensionService.selectTargetDecomposeDimensionList(targetDecomposeDimensionDTO);
        return getDataTable(list);
    }

    /**
     * 查询目标分解维度配置列表
     */
//    @RequiresPermissions("operate:cloud:targetDecomposeDimension:list")
    @GetMapping("/list")
    public AjaxResult list(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        List<TargetDecomposeDimensionDTO> list = targetDecomposeDimensionService.selectTargetDecomposeDimensionList(targetDecomposeDimensionDTO);
        return AjaxResult.success(list);
    }

    /**
     * 新增目标分解维度配置
     */
//    @RequiresPermissions("operate:cloud:targetDecomposeDimension:add")
    @Log(title = "新增目标分解维度配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        //校验
        if (StringUtils.isEmpty(targetDecomposeDimensionDTO.getDecompositionDimension())) {
            return AjaxResult.error("分解维度不能为空");
        }
        String dimension = targetDecomposeDimensionDTO.getDecompositionDimension();
        TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
        if (targetDecomposeDimensionService.checkUnique(dimension, targetDecomposeDimension)) {
            return AjaxResult.error("分解维度不能重复");
        }
        return toAjax(targetDecomposeDimensionService.insertTargetDecomposeDimension(targetDecomposeDimensionDTO));
    }

    /**
     * 修改目标分解维度配置
     */
//    @RequiresPermissions("operate:cloud:targetDecomposeDimension:edit")
    @Log(title = "修改目标分解维度配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        //校验
        if (StringUtils.isEmpty(targetDecomposeDimensionDTO.getDecompositionDimension())) {
            return AjaxResult.error("分解维度不能为空");
        }
        String dimension = targetDecomposeDimensionDTO.getDecompositionDimension();
        TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
        if (targetDecomposeDimensionService.checkUnique(dimension, targetDecomposeDimension)) {
            return AjaxResult.error("分解维度不能重复");
        }
        return toAjax(targetDecomposeDimensionService.updateTargetDecomposeDimension(targetDecomposeDimensionDTO));
    }

    /**
     * 逻辑删除目标分解维度配置
     */
//    @RequiresPermissions("operate:cloud:targetDecomposeDimension:remove")
    @Log(title = "删除目标分解维度配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        if (targetDecomposeDimensionDTO.getTargetDecomposeDimensionId() == null) {
            return AjaxResult.error("分解维度id为空");
        }
        Long targetDecomposeDimensionId = targetDecomposeDimensionDTO.getTargetDecomposeDimensionId();
        if (targetDecomposeDimensionService.isQuote(targetDecomposeDimensionId)) {
            return AjaxResult.error(400, "该分解维度已被引用");
        }
        return toAjax(targetDecomposeDimensionService.logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionId(targetDecomposeDimensionDTO));
    }

    /**
     * 批量修改目标分解维度配置
     */
//    @RequiresPermissions("operate:cloud:targetDecomposeDimension:edits")
    @Log(title = "批量修改目标分解维度配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDtos) {
        ArrayList<Integer> sorts = new ArrayList<>();
        for (TargetDecomposeDimensionDTO targetDecomposeDimensionDTO : targetDecomposeDimensionDtos) {
            //校验
            if (targetDecomposeDimensionDTO.getSort() == null && targetDecomposeDimensionDTO.getTargetDecomposeDimensionId() == null) {
                return AjaxResult.error("sort与id不能为空");
            }
            if (sorts.contains(targetDecomposeDimensionDTO.getSort())) {
                return AjaxResult.error("sort存在重复");
            }
            sorts.add(targetDecomposeDimensionDTO.getSort());
        }
        return toAjax(targetDecomposeDimensionService.updateTargetDecomposeDimensions(targetDecomposeDimensionDtos));
    }

    /**
     * 批量新增目标分解维度配置
     */
//    @RequiresPermissions("operate:cloud:targetDecomposeDimension:insertTargetDecomposeDimensions")
    @Log(title = "批量新增目标分解维度配置", businessType = BusinessType.INSERT)
    @PostMapping("/insertTargetDecomposeDimensions")
    public AjaxResult insertTargetDecomposeDimensions(@RequestBody List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDtos) {
        return toAjax(targetDecomposeDimensionService.insertTargetDecomposeDimensions(targetDecomposeDimensionDtos));
    }

    /**
     * 逻辑批量删除目标分解维度配置
     */
//    @RequiresPermissions("operate:cloud:targetDecomposeDimension:removes")
    @Log(title = "批量删除目标分解维度配置", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<TargetDecomposeDimensionDTO> TargetDecomposeDimensionDtos) {
        return toAjax(targetDecomposeDimensionService.logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionIds(TargetDecomposeDimensionDtos));
    }
}
