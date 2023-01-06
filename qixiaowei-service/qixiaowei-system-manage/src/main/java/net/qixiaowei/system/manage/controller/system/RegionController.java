package net.qixiaowei.system.manage.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.service.system.IRegionService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author hzk
 * @since 2022-10-20
 */
@RestController
@RequestMapping("region")
public class RegionController extends BaseController {


    @Autowired
    private IRegionService regionService;


    /**
     * 查询区域表详情
     */
    @RequiresPermissions("system:manage:region:info")
    @GetMapping("/info/{regionId}")
    public AjaxResult info(@PathVariable Long regionId) {
        RegionDTO regionDTO = regionService.selectRegionByRegionId(regionId);
        return AjaxResult.success(regionDTO);
    }

    /**
     * 分页查询区域表列表
     */
    @RequiresPermissions("system:manage:region:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(RegionDTO regionDTO) {
        startPage();
        List<RegionDTO> list = regionService.selectRegionList(regionDTO);
        return getDataTable(list);
    }

    /**
     * 查询区域表树表
     */
    @GetMapping("/treeList")
    public AjaxResult treeList(RegionDTO regionDTO) {
        return AjaxResult.success(regionService.selectRegionTreeList(regionDTO));
    }

    /**
     * 查询区域表列表
     */
    @GetMapping("/list")
    public AjaxResult list(RegionDTO regionDTO) {
        return AjaxResult.success(regionService.selectRegions(regionDTO));
    }


    /**
     * 新增区域表
     */
    @RequiresPermissions("system:manage:region:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody RegionDTO regionDTO) {
        return AjaxResult.success(regionService.insertRegion(regionDTO));
    }


    /**
     * 修改区域表
     */
    @RequiresPermissions("system:manage:region:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody RegionDTO regionDTO) {
        return toAjax(regionService.updateRegion(regionDTO));
    }

    /**
     * 逻辑删除区域表
     */
    @RequiresPermissions("system:manage:region:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody RegionDTO regionDTO) {
        return toAjax(regionService.logicDeleteRegionByRegionId(regionDTO));
    }

    /**
     * 逻辑批量删除区域表
     */
    @RequiresPermissions("system:manage:region:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> regionIds) {
        return toAjax(regionService.logicDeleteRegionByRegionIds(regionIds));
    }
}
