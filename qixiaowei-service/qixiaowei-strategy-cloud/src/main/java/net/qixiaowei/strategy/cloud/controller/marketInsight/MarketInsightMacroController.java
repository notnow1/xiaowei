package net.qixiaowei.strategy.cloud.controller.marketInsight;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.DataFormatData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.AbstractVerticalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightMacroDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentDTO;
import net.qixiaowei.strategy.cloud.excel.marketInsight.MarketInsightMacroExcel;
import net.qixiaowei.strategy.cloud.excel.marketInsight.MarketInsightMacroImportListener;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightMacroService;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author TANGMICHI
 * @since 2023-02-28
 */
@RestController
@RequestMapping("marketInsightMacro")
public class MarketInsightMacroController extends BaseController {


    @Autowired
    private IMarketInsightMacroService marketInsightMacroService;


    /**
     * 查询市场洞察宏观表详情
     */
    @RequiresPermissions("strategy:cloud:marketInsightMacro:info")
    @GetMapping("/info/{marketInsightMacroId}")
    public AjaxResult info(@PathVariable Long marketInsightMacroId) {
        MarketInsightMacroDTO marketInsightMacroDTO = marketInsightMacroService.selectMarketInsightMacroByMarketInsightMacroId(marketInsightMacroId);
        return AjaxResult.success(marketInsightMacroDTO);
    }

    /**
     * 分页查询市场洞察宏观表列表
     */
    @RequiresPermissions("strategy:cloud:marketInsightMacro:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(MarketInsightMacroDTO marketInsightMacroDTO) {
        startPage();
        List<MarketInsightMacroDTO> list = marketInsightMacroService.selectMarketInsightMacroList(marketInsightMacroDTO);
        return getDataTable(list);
    }

    /**
     * 查询市场洞察宏观表列表
     */
    @RequiresPermissions("strategy:cloud:marketInsightMacro:list")
    @GetMapping("/list")
    public AjaxResult list(MarketInsightMacroDTO marketInsightMacroDTO) {
        List<MarketInsightMacroDTO> list = marketInsightMacroService.selectMarketInsightMacroList(marketInsightMacroDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增市场洞察宏观表
     */
    @RequiresPermissions("strategy:cloud:marketInsightMacro:add")
    @Log(title = "新增市场洞察宏观表", businessType = BusinessType.MARKET_INSIGHT_MACRO, businessId = "marketInsightMacroId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated({StrategyIntentDTO.DeleteStrategyIntentDTO.class}) MarketInsightMacroDTO marketInsightMacroDTO) {
        return AjaxResult.success(marketInsightMacroService.insertMarketInsightMacro(marketInsightMacroDTO));
    }


    /**
     * 修改市场洞察宏观表
     */
    @RequiresPermissions("strategy:cloud:marketInsightMacro:edit")
    @Log(title = "修改市场洞察宏观表", businessType = BusinessType.MARKET_INSIGHT_MACRO, businessId = "marketInsightMacroId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated({StrategyIntentDTO.UpdateStrategyIntentDTO.class}) MarketInsightMacroDTO marketInsightMacroDTO) {
        return toAjax(marketInsightMacroService.updateMarketInsightMacro(marketInsightMacroDTO));
    }

    /**
     * 逻辑删除市场洞察宏观表
     */
    @RequiresPermissions("strategy:cloud:marketInsightMacro:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody  @Validated({StrategyIntentDTO.DeleteStrategyIntentDTO.class}) MarketInsightMacroDTO marketInsightMacroDTO) {
        return toAjax(marketInsightMacroService.logicDeleteMarketInsightMacroByMarketInsightMacroId(marketInsightMacroDTO));
    }

    /**
     * 逻辑批量删除市场洞察宏观表
     */
    @RequiresPermissions("strategy:cloud:marketInsightMacro:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> marketInsightMacroIds) {
        return toAjax(marketInsightMacroService.logicDeleteMarketInsightMacroByMarketInsightMacroIds(marketInsightMacroIds));
    }


    /**
     * 导出市场洞察宏观表
     */
    @SneakyThrows
    @GetMapping("export")
    public void exportMarketInsightMacro(@RequestParam Map<String, Object> marketInsightMacro, MarketInsightMacroDTO marketInsightMacroDTO, HttpServletResponse response) {
        MarketInsightMacroDTO marketInsightMacroDTO1 = marketInsightMacroService.selectMarketInsightMacroByMarketInsightMacroId(marketInsightMacroDTO.getMarketInsightMacroId());
        List<List<String>> head = MarketInsightMacroImportListener.head(marketInsightMacroDTO1);
        List<MarketInsightMacroExcel> marketInsightMacroExcelList = marketInsightMacroService.exportMarketInsightMacro(marketInsightMacroDTO1);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("市场洞察宏观表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(head)// 设置表头
                .sheet("市场洞察宏观详情")// 设置 sheet 的名字
                // 自适应列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .registerWriteHandler(new AbstractVerticalCellStyleStrategy() {
                    //设置标题栏的列样式
                    @Override
                    protected WriteCellStyle headCellStyle(Head head) {
                        //内容样式策略
                        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
                        //居中
                        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                        //设置 自动换行
                        contentWriteCellStyle.setWrapped(true);
                        // 字体策略
                        WriteFont contentWriteFont = new WriteFont();
                        // 字体大小
                        contentWriteFont.setFontHeightInPoints((short) 15);
                        contentWriteCellStyle.setWriteFont(contentWriteFont);
                        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        return contentWriteCellStyle;
                    }
                })
                .useDefaultStyle(false)
                //设置文本
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        DataFormatData dataFormatData = new DataFormatData();
                        dataFormatData.setIndex((short) 49);
                        writeCellStyle.setDataFormatData(dataFormatData);
                    }
                })
                .doWrite(marketInsightMacroExcelList);
    }
}
