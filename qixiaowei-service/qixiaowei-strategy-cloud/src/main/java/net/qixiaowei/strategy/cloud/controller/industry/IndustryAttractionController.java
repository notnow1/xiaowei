package net.qixiaowei.strategy.cloud.controller.industry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;
import net.qixiaowei.strategy.cloud.service.industry.IIndustryAttractionService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author TANGMICHI
 * @since 2023-02-17
 */
@RestController
@RequestMapping("industryAttraction")
public class IndustryAttractionController extends BaseController {


    @Autowired
    private IIndustryAttractionService industryAttractionService;


    /**
     * 查询行业吸引力表详情
     */
    @RequiresPermissions("strategy:cloud:industryAttraction:info")
    @GetMapping("/info/{industryAttractionId}")
    public AjaxResult info(@PathVariable Long industryAttractionId) {
        IndustryAttractionDTO industryAttractionDTO = industryAttractionService.selectIndustryAttractionByIndustryAttractionId(industryAttractionId);
        return AjaxResult.success(industryAttractionDTO);
    }

    /**
     * 分页查询行业吸引力表列表
     */
    @RequiresPermissions("strategy:cloud:industryAttraction:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(IndustryAttractionDTO industryAttractionDTO) {
        startPage();
        List<IndustryAttractionDTO> list = industryAttractionService.selectIndustryAttractionList(industryAttractionDTO);
        return getDataTable(list);
    }

    /**
     * 查询行业吸引力表列表
     */
    @RequiresPermissions("strategy:cloud:industryAttraction:list")
    @GetMapping("/list")
    public AjaxResult list(IndustryAttractionDTO industryAttractionDTO) {
        List<IndustryAttractionDTO> list = industryAttractionService.selectIndustryAttractionList(industryAttractionDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增行业吸引力表
     */
    @RequiresPermissions("strategy:cloud:industryAttraction:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody IndustryAttractionDTO industryAttractionDTO) {
        return AjaxResult.success(industryAttractionService.insertIndustryAttraction(industryAttractionDTO));
    }


    /**
     * 修改行业吸引力表
     */
    @RequiresPermissions("strategy:cloud:industryAttraction:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody IndustryAttractionDTO industryAttractionDTO) {
        return toAjax(industryAttractionService.updateIndustryAttraction(industryAttractionDTO));
    }

    /**
     * 逻辑删除行业吸引力表
     */
    @RequiresPermissions("strategy:cloud:industryAttraction:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody IndustryAttractionDTO industryAttractionDTO) {
        return toAjax(industryAttractionService.logicDeleteIndustryAttractionByIndustryAttractionId(industryAttractionDTO));
    }

    /**
     * 批量修改行业吸引力表
     */
    @RequiresPermissions("strategy:cloud:industryAttraction:edits")
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<IndustryAttractionDTO> industryAttractionDtos) {
        return toAjax(industryAttractionService.updateIndustryAttractions(industryAttractionDtos));
    }

    /**
     * 批量新增行业吸引力表
     */
    @RequiresPermissions("strategy:cloud:industryAttraction:insertIndustryAttractions")
    @PostMapping("/insertIndustryAttractions")
    public AjaxResult insertIndustryAttractions(@RequestBody List<IndustryAttractionDTO> industryAttractionDtos) {
        return toAjax(industryAttractionService.insertIndustryAttractions(industryAttractionDtos));
    }

    /**
     * 逻辑批量删除行业吸引力表
     */
    @RequiresPermissions("strategy:cloud:industryAttraction:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> industryAttractionIds) {
        return toAjax(industryAttractionService.logicDeleteIndustryAttractionByIndustryAttractionIds(industryAttractionIds));
    }

}
