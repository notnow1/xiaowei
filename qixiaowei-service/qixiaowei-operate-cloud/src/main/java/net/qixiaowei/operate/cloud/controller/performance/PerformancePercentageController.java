package net.qixiaowei.operate.cloud.controller.performance;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.service.performance.IPerformancePercentageService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceRankFactorService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Graves
 * @since 2022-10-10
 */
@RestController
@RequestMapping("performancePercentage")
public class PerformancePercentageController extends BaseController {


    @Autowired
    private IPerformancePercentageService performancePercentageService;

    @Autowired
    private IPerformanceRankService performanceRankService;

    @Autowired
    private IPerformanceRankFactorService performanceRankFactorService;


    /**
     * 查询绩效比例表详情
     */
//    @RequiresPermissions("operate:cloud:performancePercentage:info")
    @GetMapping("/info/{performancePercentageId}")
    public AjaxResult info(@PathVariable @Validated(PerformancePercentageDTO.QueryPerformancePercentageDTO.class) Long performancePercentageId) {
        PerformancePercentageDTO performancePercentageDTO = performancePercentageService.selectPerformancePercentageByPerformancePercentageId(performancePercentageId);
        return AjaxResult.success(performancePercentageDTO);
    }

    /**
     * 分页查询绩效比例表列表
     */
//    @RequiresPermissions("operate:cloud:performancePercentage:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(PerformancePercentageDTO performancePercentageDTO) {
        startPage();
        List<PerformancePercentageDTO> list = performancePercentageService.selectPerformancePercentageList(performancePercentageDTO);
        return getDataTable(list);
    }

    /**
     * 查询绩效比例表列表
     */
//    @RequiresPermissions("operate:cloud:performancePercentage:list")
    @GetMapping("/list")
    public AjaxResult list(PerformancePercentageDTO performancePercentageDTO) {
        List<PerformancePercentageDTO> list = performancePercentageService.selectPerformancePercentageList(performancePercentageDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增绩效比例表
     */
//    @RequiresPermissions("operate:cloud:performancePercentage:add")
    @Log(title = "新增绩效比例表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(PerformancePercentageDTO.AddPerformancePercentageDTO.class) PerformancePercentageDTO performancePercentageDTO) {
        return toAjax(performancePercentageService.insertPerformancePercentage(performancePercentageDTO));
    }

    /**
     * 修改绩效比例表
     */
//    @RequiresPermissions("operate:cloud:performancePercentage:edit")
    @Log(title = "修改绩效比例表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(PerformancePercentageDTO.UpdatePerformancePercentageDTO.class) PerformancePercentageDTO performancePercentageDTO) {
        return toAjax(performancePercentageService.updatePerformancePercentage(performancePercentageDTO));
    }

    /**
     * 逻辑删除绩效比例表
     */
//    @RequiresPermissions("operate:cloud:performancePercentage:remove")
    @Log(title = "删除绩效比例表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(PerformancePercentageDTO.DeletePerformancePercentageDTO.class) PerformancePercentageDTO performancePercentageDTO) {
        return toAjax(performancePercentageService.logicDeletePerformancePercentageByPerformancePercentageId(performancePercentageDTO));
    }

    /**
     * 逻辑批量删除绩效比例表
     */
//    @RequiresPermissions("operate:cloud:performancePercentage:removes")
    @Log(title = "批量删除绩效比例表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody @Validated(PerformancePercentageDTO.DeletePerformancePercentageDTO.class) List<Long> performancePercentageIds) {
        return toAjax(performancePercentageService.logicDeletePerformancePercentageByPerformancePercentageIds(performancePercentageIds));
    }

    /**
     * 组织绩效等级系数信息
     */
//    @RequiresPermissions("operate:cloud:performanceRank:edit")
    @Log(title = "组织绩效等级系数信息")
    @GetMapping("/levelInfo")
    public AjaxResult levelInfo() {
        return AjaxResult.success(performanceRankService.detailLevelInfo());
    }

    /**
     * 查询绩效比例表详情
     */
//    @RequiresPermissions("operate:cloud:performancePercentage:info")
    @GetMapping("/factors/{performanceRankId}")
    public AjaxResult factors(@PathVariable Long performanceRankId) {
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceRankFactorService.selectPerformanceRankFactorByPerformanceRankId(performanceRankId);
        return AjaxResult.success(performanceRankFactorDTOS);
    }

}
