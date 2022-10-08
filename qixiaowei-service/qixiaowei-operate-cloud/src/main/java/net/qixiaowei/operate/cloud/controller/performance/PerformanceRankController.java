package net.qixiaowei.operate.cloud.controller.performance;

import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankDTO;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Graves
 * @since 2022-10-06
 */
@RestController
@RequestMapping("performanceRank")
public class PerformanceRankController extends BaseController {


    @Autowired
    private IPerformanceRankService performanceRankService;

    /**
     * 分页查询绩效等级表列表
     */
//    @RequiresPermissions("operate:cloud:performanceRank:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(PerformanceRankDTO performanceRankDTO) {
        startPage();
        List<PerformanceRankDTO> list = performanceRankService.selectPerformanceRankList(performanceRankDTO);
        return getDataTable(list);
    }

    /**
     * 查询绩效等级表列表
     */
//    @RequiresPermissions("operate:cloud:performanceRank:list")
    @GetMapping("/list")
    public AjaxResult list(PerformanceRankDTO performanceRankDTO) {
        List<PerformanceRankDTO> list = performanceRankService.selectPerformanceRankList(performanceRankDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增绩效等级表
     */
//    @RequiresPermissions("operate:cloud:performanceRank:add")
    @Log(title = "新增绩效等级表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody PerformanceRankDTO performanceRankDTO) {
        return toAjax(performanceRankService.insertPerformanceRank(performanceRankDTO));
    }


    /**
     * 修改绩效等级表
     */
//    @RequiresPermissions("operate:cloud:performanceRank:edit")
    @Log(title = "修改绩效等级表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody PerformanceRankDTO performanceRankDTO) {
        return toAjax(performanceRankService.updatePerformanceRank(performanceRankDTO));
    }

    /**
     * 修改绩效等级表
     */
//    @RequiresPermissions("operate:cloud:performanceRank:edit")
    @GetMapping("/detail")
    public AjaxResult detail(Long performanceRankId) {
        if (StringUtils.isNull(performanceRankId)) {
            return AjaxResult.error("绩效等级配置id不能为空");
        }
        return AjaxResult.success(performanceRankService.detailPerformanceRank(performanceRankId));
    }

    /**
     * 逻辑删除绩效等级表
     */
//    @RequiresPermissions("operate:cloud:performanceRank:remove")
    @Log(title = "删除绩效等级表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody PerformanceRankDTO performanceRankDTO) {
        return toAjax(performanceRankService.logicDeletePerformanceRankByPerformanceRankId(performanceRankDTO));
    }

    /**
     * 批量修改绩效等级表
     */
//    @RequiresPermissions("operate:cloud:performanceRank:edits")
    @Log(title = "批量修改绩效等级表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<PerformanceRankDTO> performanceRankDtos) {
        return toAjax(performanceRankService.updatePerformanceRanks(performanceRankDtos));
    }

    /**
     * 批量新增绩效等级表
     */
//    @RequiresPermissions("operate:cloud:performanceRank:insertPerformanceRanks")
    @Log(title = "批量新增绩效等级表", businessType = BusinessType.INSERT)
    @PostMapping("/insertPerformanceRanks")
    public AjaxResult insertPerformanceRanks(@RequestBody List<PerformanceRankDTO> performanceRankDtos) {
        return toAjax(performanceRankService.insertPerformanceRanks(performanceRankDtos));
    }

    /**
     * 逻辑批量删除绩效等级表
     */
//    @RequiresPermissions("operate:cloud:performanceRank:removes")
    @Log(title = "批量删除绩效等级表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> PerformanceRankIds) {
        return toAjax(performanceRankService.logicDeletePerformanceRankByPerformanceRankIds(PerformanceRankIds));
    }
}