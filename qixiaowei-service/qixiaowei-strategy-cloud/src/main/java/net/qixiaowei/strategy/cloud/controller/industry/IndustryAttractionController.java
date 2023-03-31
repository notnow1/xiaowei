package net.qixiaowei.strategy.cloud.controller.industry;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionElementDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightCustomerDTO;
import net.qixiaowei.strategy.cloud.service.industry.IIndustryAttractionService;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author TANGMICHI
 * @since 2023-02-20
 */
@RestController
@RequestMapping("industryAttraction")
public class IndustryAttractionController extends BaseController {


    @Autowired
    private IIndustryAttractionService industryAttractionService;


    /**
     * 查询行业吸引力表详情
     */
    @RequiresPermissions(value = {"strategy:cloud:industryAttraction:info", "strategy:cloud:industryAttraction:edit"}, logical = Logical.OR)
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
        if (StringUtils.isNotEmpty(list)) {
            Set<Long> userIds = list.stream().map(IndustryAttractionDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            list.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
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
    @Log(title = "保存行业吸引力配置", businessType = BusinessType.INDUSTRY_ATTRACTION, businessId = "industryAttractionId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(IndustryAttractionDTO.AddIndustryAttractionDTO.class) IndustryAttractionDTO industryAttractionDTO) {
        return AjaxResult.success(industryAttractionService.insertIndustryAttraction(industryAttractionDTO));
    }


    /**
     * 修改行业吸引力表
     */
    @RequiresPermissions("strategy:cloud:industryAttraction:edit")
    @Log(title = "保存行业吸引力配置", businessType = BusinessType.INDUSTRY_ATTRACTION, businessId = "industryAttractionId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(IndustryAttractionDTO.UpdateIndustryAttractionDTO.class) IndustryAttractionDTO industryAttractionDTO) {
        return toAjax(industryAttractionService.updateIndustryAttraction(industryAttractionDTO));
    }

    /**
     * 逻辑删除行业吸引力表
     */
    @RequiresPermissions("strategy:cloud:industryAttraction:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(IndustryAttractionDTO.DeleteIndustryAttractionDTO.class) IndustryAttractionDTO industryAttractionDTO) {
        return toAjax(industryAttractionService.logicDeleteIndustryAttractionByIndustryAttractionId(industryAttractionDTO));
    }

    /**
     * 逻辑批量删除行业吸引力表
     */
    @RequiresPermissions("strategy:cloud:industryAttraction:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> industryAttractionIds) {
        return toAjax(industryAttractionService.logicDeleteIndustryAttractionByIndustryAttractionIds(industryAttractionIds));
    }

    /**
     * 逻辑删除行业吸引力要素
     */
    @RequiresPermissions("strategy:cloud:industryAttraction:remove")
    @PostMapping("/removeElement")
    public AjaxResult removeElement(@RequestBody @Validated(IndustryAttractionElementDTO.DeleteIndustryAttractionElementDTO.class) IndustryAttractionElementDTO industryAttractionElementDTO) {
        return AjaxResult.success(industryAttractionService.logicDeleteIndustryAttractionElementByIndustryAttractionElementId(industryAttractionElementDTO));
    }

}
