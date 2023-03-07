package net.qixiaowei.strategy.cloud.controller.marketInsight;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightIndustryDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentDTO;
import net.qixiaowei.strategy.cloud.service.industry.IIndustryAttractionService;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;



/**
*
* @author TANGMICHI
* @since 2023-03-03
*/
@RestController
@RequestMapping("marketInsightIndustry")
public class MarketInsightIndustryController extends BaseController
{


    @Autowired
    private IMarketInsightIndustryService marketInsightIndustryService;
    @Autowired
    private IIndustryAttractionService industryAttractionService;


    /**
    * 查询市场洞察行业表详情
    */
    @RequiresPermissions("strategy:cloud:marketInsightIndustry:info")
    @GetMapping("/info/{marketInsightIndustryId}")
    public AjaxResult info(@PathVariable Long marketInsightIndustryId){
    MarketInsightIndustryDTO marketInsightIndustryDTO = marketInsightIndustryService.selectMarketInsightIndustryByMarketInsightIndustryId(marketInsightIndustryId);
        return AjaxResult.success(marketInsightIndustryDTO);
    }

    /**
    * 分页查询市场洞察行业表列表
    */
    @RequiresPermissions("strategy:cloud:marketInsightIndustry:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(MarketInsightIndustryDTO marketInsightIndustryDTO){
    startPage();
    List<MarketInsightIndustryDTO> list = marketInsightIndustryService.selectMarketInsightIndustryList(marketInsightIndustryDTO);
    return getDataTable(list);
    }

    /**
    * 查询市场洞察行业表列表
    */
    @RequiresPermissions("strategy:cloud:marketInsightIndustry:list")
    @GetMapping("/list")
    public AjaxResult list(MarketInsightIndustryDTO marketInsightIndustryDTO){
    List<MarketInsightIndustryDTO> list = marketInsightIndustryService.selectMarketInsightIndustryList(marketInsightIndustryDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增市场洞察行业表
    */
    @RequiresPermissions("strategy:cloud:marketInsightIndustry:add")
    @Log(title = "新增市场洞察行业表", businessType = BusinessType.MARKET_INSIGHT_INDUSTRY, businessId = "marketInsightIndustryId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated({MarketInsightIndustryDTO.AddMarketInsightIndustryDTO.class}) MarketInsightIndustryDTO marketInsightIndustryDTO) {
    return AjaxResult.success(marketInsightIndustryService.insertMarketInsightIndustry(marketInsightIndustryDTO));
    }


    /**
    * 修改市场洞察行业表
    */
    @RequiresPermissions("strategy:cloud:marketInsightIndustry:edit")
    @Log(title = "修改市场洞察行业表", businessType = BusinessType.MARKET_INSIGHT_INDUSTRY, businessId = "marketInsightIndustryId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated({MarketInsightIndustryDTO.UpdateMarketInsightIndustryDTO.class}) MarketInsightIndustryDTO marketInsightIndustryDTO)
    {
    return toAjax(marketInsightIndustryService.updateMarketInsightIndustry(marketInsightIndustryDTO));
    }

    /**
    * 逻辑删除市场洞察行业表
    */
    @RequiresPermissions("strategy:cloud:marketInsightIndustry:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated({MarketInsightIndustryDTO.DeleteMarketInsightIndustryDTO.class}) MarketInsightIndustryDTO marketInsightIndustryDTO)
    {
    return toAjax(marketInsightIndustryService.logicDeleteMarketInsightIndustryByMarketInsightIndustryId(marketInsightIndustryDTO));
    }
    /**
    * 逻辑批量删除市场洞察行业表
    */
    @RequiresPermissions("strategy:cloud:marketInsightIndustry:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  marketInsightIndustryIds)
    {
    return toAjax(marketInsightIndustryService.logicDeleteMarketInsightIndustryByMarketInsightIndustryIds(marketInsightIndustryIds));
    }

    /**
     * 预制表头查询行业吸引力表列表
     */
    @RequiresPermissions("strategy:cloud:marketInsightIndustry:prefabricateAdd")
    @GetMapping("/prefabricateAdd")
    public AjaxResult list(){
        IndustryAttractionDTO industryAttractionDTO = new IndustryAttractionDTO();
        List<IndustryAttractionDTO> list = industryAttractionService.selectIndustryAttractionList(industryAttractionDTO);
        return AjaxResult.success(list);
    }
}
