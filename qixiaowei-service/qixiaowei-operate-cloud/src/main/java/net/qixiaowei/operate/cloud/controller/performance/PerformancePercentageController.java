package net.qixiaowei.operate.cloud.controller.performance;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDTO;
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
     * 分页查询绩效比例表列表
     */
    @RequiresPermissions("operate:cloud:performancePercentage:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(PerformancePercentageDTO performancePercentageDTO) {
        startPage();
        List<PerformancePercentageDTO> list = performancePercentageService.selectPerformancePercentageList(performancePercentageDTO);
        return getDataTable(list);
    }

    /**
     * 新增绩效比例表
     */
    @RequiresPermissions("operate:cloud:performancePercentage:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(PerformancePercentageDTO.AddPerformancePercentageDTO.class) PerformancePercentageDTO performancePercentageDTO) {
        return toAjax(performancePercentageService.insertPerformancePercentage(performancePercentageDTO));
    }

    /**
     * 修改绩效比例表
     */
    @RequiresPermissions("operate:cloud:performancePercentage:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(PerformancePercentageDTO.UpdatePerformancePercentageDTO.class) PerformancePercentageDTO performancePercentageDTO) {
        return toAjax(performancePercentageService.updatePerformancePercentage(performancePercentageDTO));
    }

    /**
     * 查询绩效比例表详情
     */
    @RequiresPermissions(value = {"operate:cloud:performancePercentage:info", "operate:cloud:performancePercentage:edit"}, logical = Logical.OR)
    @GetMapping("/info/{performancePercentageId}")
    public AjaxResult info(@PathVariable @Validated(PerformancePercentageDTO.QueryPerformancePercentageDTO.class) Long performancePercentageId) {
        PerformancePercentageDTO performancePercentageDTO = performancePercentageService.selectPerformancePercentageByPerformancePercentageId(performancePercentageId);
        return AjaxResult.success(performancePercentageDTO);
    }

    /**
     * 逻辑删除绩效比例表
     */
    @RequiresPermissions("operate:cloud:performancePercentage:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(PerformancePercentageDTO.DeletePerformancePercentageDTO.class) PerformancePercentageDTO performancePercentageDTO) {
        return toAjax(performancePercentageService.logicDeletePerformancePercentageByPerformancePercentageId(performancePercentageDTO));
    }

    /**
     * 逻辑批量删除绩效比例表
     */
    @RequiresPermissions("operate:cloud:performancePercentage:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody @Validated(PerformancePercentageDTO.DeletePerformancePercentageDTO.class) List<Long> performancePercentageIds) {
        return toAjax(performancePercentageService.logicDeletePerformancePercentageByPerformancePercentageIds(performancePercentageIds));
    }

    /**
     * 组织绩效等级系数信息
     */
    @RequiresPermissions(value = {"operate:cloud:performancePercentage:info", "operate:cloud:performancePercentage:edit"}, logical = Logical.OR)
    @GetMapping("/levelInfo")
    public AjaxResult levelInfo() {
        return AjaxResult.success(performanceRankService.detailLevelInfo());
    }


    /**
     * 查询绩效比例表列表
     */
    @GetMapping("/list")
    public AjaxResult list(PerformancePercentageDTO performancePercentageDTO) {
        List<PerformancePercentageDTO> list = performancePercentageService.selectPerformancePercentageList(performancePercentageDTO);
        return AjaxResult.success(list);
    }


}
