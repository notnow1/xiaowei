package net.qixiaowei.strategy.cloud.controller.strategyDecode;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDTO;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IAnnualKeyWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Graves
 * @since 2023-03-14
 */
@RestController
@RequestMapping("annualKeyWork")
public class AnnualKeyWorkController extends BaseController {


    @Autowired
    private IAnnualKeyWorkService annualKeyWorkService;


    /**
     * 查询年度重点工作表详情
     */
    @RequiresPermissions(value = {"strategy:cloud:annualKeyWork:info", "strategy:cloud:annualKeyWork:edit"}, logical = Logical.OR)
    @GetMapping("/info/{annualKeyWorkId}")
    public AjaxResult info(@PathVariable Long annualKeyWorkId) {
        AnnualKeyWorkDTO annualKeyWorkDTO = annualKeyWorkService.selectAnnualKeyWorkByAnnualKeyWorkId(annualKeyWorkId);
        return AjaxResult.success(annualKeyWorkDTO);
    }

    /**
     * 分页查询年度重点工作表列表
     */
    @RequiresPermissions("strategy:cloud:annualKeyWork:pageList")
    @GetMapping("/pageList")
    @DataScope(businessAlias = "akw")
    public TableDataInfo pageList(AnnualKeyWorkDTO annualKeyWorkDTO) {
        startPage();
        List<AnnualKeyWorkDTO> list = annualKeyWorkService.selectAnnualKeyWorkList(annualKeyWorkDTO);
        return getDataTable(list);
    }

    /**
     * 查询年度重点工作表列表
     */
    @GetMapping("/list")
    public AjaxResult list(AnnualKeyWorkDTO annualKeyWorkDTO) {
        List<AnnualKeyWorkDTO> list = annualKeyWorkService.selectAnnualKeyWorkList(annualKeyWorkDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增年度重点工作表
     */
    @RequiresPermissions("strategy:cloud:annualKeyWork:add")
    @Log(title = "保存年度重点工作表", businessType = BusinessType.ANNUAL_KEY_WORK, businessId = "annualKeyWorkId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody AnnualKeyWorkDTO annualKeyWorkDTO) {
        return AjaxResult.success(annualKeyWorkService.insertAnnualKeyWork(annualKeyWorkDTO));
    }


    /**
     * 修改年度重点工作表
     */
    @RequiresPermissions("strategy:cloud:annualKeyWork:edit")
    @Log(title = "保存年度重点工作表", businessType = BusinessType.ANNUAL_KEY_WORK, businessId = "annualKeyWorkId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody AnnualKeyWorkDTO annualKeyWorkDTO) {
        return toAjax(annualKeyWorkService.updateAnnualKeyWork(annualKeyWorkDTO));
    }

    /**
     * 逻辑删除年度重点工作表
     */
    @RequiresPermissions("strategy:cloud:annualKeyWork:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody AnnualKeyWorkDTO annualKeyWorkDTO) {
        return toAjax(annualKeyWorkService.logicDeleteAnnualKeyWorkByAnnualKeyWorkId(annualKeyWorkDTO));
    }

    /**
     * 逻辑批量删除年度重点工作表
     */
    @RequiresPermissions("strategy:cloud:annualKeyWork:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> annualKeyWorkIds) {
        return toAjax(annualKeyWorkService.logicDeleteAnnualKeyWorkByAnnualKeyWorkIds(annualKeyWorkIds));
    }

}
