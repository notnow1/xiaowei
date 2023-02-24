package net.qixiaowei.strategy.cloud.controller.gap;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisDTO;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Graves
 * @since 2023-02-24
 */
@RestController
@RequestMapping("gapAnalysis")
public class GapAnalysisController extends BaseController {


    @Autowired
    private IGapAnalysisService gapAnalysisService;


    /**
     * 查询差距分析表详情
     */
    @RequiresPermissions("strategy:cloud:gapAnalysis:info")
    @GetMapping("/info/{gapAnalysisId}")
    public AjaxResult info(@PathVariable Long gapAnalysisId) {
        GapAnalysisDTO gapAnalysisDTO = gapAnalysisService.selectGapAnalysisByGapAnalysisId(gapAnalysisId);
        return AjaxResult.success(gapAnalysisDTO);
    }

    /**
     * 分页查询差距分析表列表
     */
    @RequiresPermissions("strategy:cloud:gapAnalysis:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(GapAnalysisDTO gapAnalysisDTO) {
        startPage();
        List<GapAnalysisDTO> list = gapAnalysisService.selectGapAnalysisList(gapAnalysisDTO);
        return getDataTable(list);
    }

    /**
     * 查询差距分析表列表
     */
    @RequiresPermissions("strategy:cloud:gapAnalysis:list")
    @GetMapping("/list")
    public AjaxResult list(GapAnalysisDTO gapAnalysisDTO) {
        List<GapAnalysisDTO> list = gapAnalysisService.selectGapAnalysisList(gapAnalysisDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增差距分析表
     */
    @RequiresPermissions("strategy:cloud:gapAnalysis:add")
    @Log(title = "新增差距分析表", businessType = BusinessType.GAP_ANALYSIS, businessId = "gapAnalysisId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody GapAnalysisDTO gapAnalysisDTO) {
        return AjaxResult.success(gapAnalysisService.insertGapAnalysis(gapAnalysisDTO));
    }


    /**
     * 修改差距分析表
     */
    @RequiresPermissions("strategy:cloud:gapAnalysis:edit")
    @Log(title = "修改差距分析表", businessType = BusinessType.GAP_ANALYSIS, businessId = "gapAnalysisId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody GapAnalysisDTO gapAnalysisDTO) {
        return toAjax(gapAnalysisService.updateGapAnalysis(gapAnalysisDTO));
    }

    /**
     * 逻辑删除差距分析表
     */
    @RequiresPermissions("strategy:cloud:gapAnalysis:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody GapAnalysisDTO gapAnalysisDTO) {
        return toAjax(gapAnalysisService.logicDeleteGapAnalysisByGapAnalysisId(gapAnalysisDTO));
    }

    /**
     * 逻辑批量删除差距分析表
     */
    @RequiresPermissions("strategy:cloud:gapAnalysis:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> gapAnalysisIds) {
        return toAjax(gapAnalysisService.logicDeleteGapAnalysisByGapAnalysisIds(gapAnalysisIds));
    }


}
