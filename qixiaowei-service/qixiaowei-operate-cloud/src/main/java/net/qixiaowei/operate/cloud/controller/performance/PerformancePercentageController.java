package net.qixiaowei.operate.cloud.controller.performance;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceRankFactorService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDTO;
import net.qixiaowei.operate.cloud.service.performance.IPerformancePercentageService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


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
    public AjaxResult info(@PathVariable Long performancePercentageId) {
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
    public AjaxResult addSave(@RequestBody PerformancePercentageDTO performancePercentageDTO) {
        return toAjax(performancePercentageService.insertPerformancePercentage(performancePercentageDTO));
    }


    /**
     * 修改绩效比例表
     */
//    @RequiresPermissions("operate:cloud:performancePercentage:edit")
    @Log(title = "修改绩效比例表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody PerformancePercentageDTO performancePercentageDTO) {
        return toAjax(performancePercentageService.updatePerformancePercentage(performancePercentageDTO));
    }

    /**
     * 逻辑删除绩效比例表
     */
//    @RequiresPermissions("operate:cloud:performancePercentage:remove")
    @Log(title = "删除绩效比例表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody PerformancePercentageDTO performancePercentageDTO) {
        return toAjax(performancePercentageService.logicDeletePerformancePercentageByPerformancePercentageId(performancePercentageDTO));
    }

    /**
     * 逻辑批量删除绩效比例表
     */
//    @RequiresPermissions("operate:cloud:performancePercentage:removes")
    @Log(title = "批量删除绩效比例表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<PerformancePercentageDTO> PerformancePercentageDtos) {
        return toAjax(performancePercentageService.logicDeletePerformancePercentageByPerformancePercentageIds(PerformancePercentageDtos));
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
