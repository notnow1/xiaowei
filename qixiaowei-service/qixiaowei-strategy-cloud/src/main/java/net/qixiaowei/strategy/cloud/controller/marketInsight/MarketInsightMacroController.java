package net.qixiaowei.strategy.cloud.controller.marketInsight;

import com.alibaba.excel.EasyExcel;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightMacroDTO;
import net.qixiaowei.strategy.cloud.excel.marketInsight.MarketInsightMacroExcel;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightMacroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
*
* @author TANGMICHI
* @since 2023-02-28
*/
@RestController
@RequestMapping("marketInsightMacro")
public class MarketInsightMacroController extends BaseController
{


    @Autowired
    private IMarketInsightMacroService marketInsightMacroService;



    /**
    * 查询市场洞察宏观表详情
    */
    @RequiresPermissions("strategy:cloud:marketInsightMacro:info")
    @GetMapping("/info/{marketInsightMacroId}")
    public AjaxResult info(@PathVariable Long marketInsightMacroId){
    MarketInsightMacroDTO marketInsightMacroDTO = marketInsightMacroService.selectMarketInsightMacroByMarketInsightMacroId(marketInsightMacroId);
        return AjaxResult.success(marketInsightMacroDTO);
    }

    /**
    * 分页查询市场洞察宏观表列表
    */
    @RequiresPermissions("strategy:cloud:marketInsightMacro:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(MarketInsightMacroDTO marketInsightMacroDTO){
    startPage();
    List<MarketInsightMacroDTO> list = marketInsightMacroService.selectMarketInsightMacroList(marketInsightMacroDTO);
    return getDataTable(list);
    }

    /**
    * 查询市场洞察宏观表列表
    */
    @RequiresPermissions("strategy:cloud:marketInsightMacro:list")
    @GetMapping("/list")
    public AjaxResult list(MarketInsightMacroDTO marketInsightMacroDTO){
    List<MarketInsightMacroDTO> list = marketInsightMacroService.selectMarketInsightMacroList(marketInsightMacroDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增市场洞察宏观表
    */
    @RequiresPermissions("strategy:cloud:marketInsightMacro:add")
    @Log(title = "新增市场洞察宏观表", businessType = BusinessType.MARKET_INSIGHT_MACRO, businessId = "marketInsightMacroId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody MarketInsightMacroDTO marketInsightMacroDTO) {
    return AjaxResult.success(marketInsightMacroService.insertMarketInsightMacro(marketInsightMacroDTO));
    }


    /**
    * 修改市场洞察宏观表
    */
    @RequiresPermissions("strategy:cloud:marketInsightMacro:edit")
    @Log(title = "修改市场洞察宏观表", businessType = BusinessType.MARKET_INSIGHT_MACRO, businessId = "marketInsightMacroId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody MarketInsightMacroDTO marketInsightMacroDTO)
    {
    return toAjax(marketInsightMacroService.updateMarketInsightMacro(marketInsightMacroDTO));
    }

    /**
    * 逻辑删除市场洞察宏观表
    */
    @RequiresPermissions("strategy:cloud:marketInsightMacro:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody MarketInsightMacroDTO marketInsightMacroDTO)
    {
    return toAjax(marketInsightMacroService.logicDeleteMarketInsightMacroByMarketInsightMacroId(marketInsightMacroDTO));
    }
    /**
    * 逻辑批量删除市场洞察宏观表
    */
    @RequiresPermissions("strategy:cloud:marketInsightMacro:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  marketInsightMacroIds)
    {
    return toAjax(marketInsightMacroService.logicDeleteMarketInsightMacroByMarketInsightMacroIds(marketInsightMacroIds));
    }



    /**
    * 导出市场洞察宏观表
    */
    @SneakyThrows
    @GetMapping("export")
    public void exportMarketInsightMacro(@RequestParam Map<String, Object> marketInsightMacro,MarketInsightMacroDTO marketInsightMacroDTO, HttpServletResponse response) {
    List<MarketInsightMacroExcel> marketInsightMacroExcelList = marketInsightMacroService.exportMarketInsightMacro(marketInsightMacroDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("市场洞察宏观表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
        , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), MarketInsightMacroExcel.class).sheet("市场洞察宏观表").doWrite(marketInsightMacroExcelList);
        }
}
