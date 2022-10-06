package net.qixiaowei.operate.cloud.controller.performance;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceRankFactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Graves
 * @since 2022-10-06
 */
@RestController
@RequestMapping("performanceRankFactor")
public class PerformanceRankFactorController extends BaseController {


    @Autowired
    private IPerformanceRankFactorService performanceRankFactorService;

    /**
     * 分页查询绩效等级系数列表
     */
//    @RequiresPermissions("operate:cloud:performanceRankFactor:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(PerformanceRankFactorDTO performanceRankFactorDTO) {
        startPage();
        List<PerformanceRankFactorDTO> list = performanceRankFactorService.selectPerformanceRankFactorList(performanceRankFactorDTO);
        return getDataTable(list);
    }

    /**
     * 查询绩效等级系数列表
     */
//    @RequiresPermissions("operate:cloud:performanceRankFactor:list")
    @GetMapping("/list")
    public AjaxResult list(PerformanceRankFactorDTO performanceRankFactorDTO) {
        List<PerformanceRankFactorDTO> list = performanceRankFactorService.selectPerformanceRankFactorList(performanceRankFactorDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增绩效等级系数
     */
//    @RequiresPermissions("operate:cloud:performanceRankFactor:add")
    @Log(title = "新增绩效等级系数", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody PerformanceRankFactorDTO performanceRankFactorDTO) {
        return toAjax(performanceRankFactorService.insertPerformanceRankFactor(performanceRankFactorDTO));
    }


    /**
     * 修改绩效等级系数
     */
//    @RequiresPermissions("operate:cloud:performanceRankFactor:edit")
    @Log(title = "修改绩效等级系数", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody PerformanceRankFactorDTO performanceRankFactorDTO) {
        return toAjax(performanceRankFactorService.updatePerformanceRankFactor(performanceRankFactorDTO));
    }

    /**
     * 逻辑删除绩效等级系数
     */
//    @RequiresPermissions("operate:cloud:performanceRankFactor:remove")
    @Log(title = "删除绩效等级系数", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody PerformanceRankFactorDTO performanceRankFactorDTO) {
        return toAjax(performanceRankFactorService.logicDeletePerformanceRankFactorByPerformanceRankFactorId(performanceRankFactorDTO));
    }

    /**
     * 批量新增绩效等级系数
     */
//    @RequiresPermissions("operate:cloud:performanceRankFactor:insertPerformanceRankFactors")
    @Log(title = "批量新增绩效等级系数", businessType = BusinessType.INSERT)
    @PostMapping("/insertPerformanceRankFactors")
    public AjaxResult insertPerformanceRankFactors(@RequestBody List<PerformanceRankFactorDTO> performanceRankFactorDtos) {
        return toAjax(performanceRankFactorService.insertPerformanceRankFactors(performanceRankFactorDtos));
    }

    /**
     * 逻辑批量删除绩效等级系数
     */
//    @RequiresPermissions("operate:cloud:performanceRankFactor:removes")
    @Log(title = "批量删除绩效等级系数", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<PerformanceRankFactorDTO> PerformanceRankFactorDtos) {
        return toAjax(performanceRankFactorService.logicDeletePerformanceRankFactorByPerformanceRankFactorIds(PerformanceRankFactorDtos));
    }
}
