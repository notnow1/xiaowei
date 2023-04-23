package net.qixiaowei.operate.cloud.controller.targetManager;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.DataFormatData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDetailsDTO;
import net.qixiaowei.operate.cloud.api.vo.strategyIntent.StrategyIntentOperateVO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetOutcomeExcel;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetOutcomeImportListener;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeDetailsService;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author TANGMICHI
 * @since 2022-11-07
 */
@RestController
@RequestMapping("targetOutcome")
public class TargetOutcomeController extends BaseController {


    @Autowired
    private ITargetOutcomeService targetOutcomeService;

    @Autowired
    private RemoteIndicatorService indicatorService;

    @Autowired
    private ITargetOutcomeDetailsService targetOutcomeDetailsService;


    /**
     * 分页查询目标结果表列表
     */
    @RequiresPermissions("operate:cloud:targetOutcome:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TargetOutcomeDTO targetOutcomeDTO) {
        startPage();
        List<TargetOutcomeDTO> list = targetOutcomeService.selectTargetOutcomeList(targetOutcomeDTO);
        return getDataTable(list);
    }

    /**
     * 修改目标结果表
     */
    @RequiresPermissions("operate:cloud:targetOutcome:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody TargetOutcomeDTO targetOutcomeDTO) {
        return toAjax(targetOutcomeService.updateTargetOutcome(targetOutcomeDTO));
    }

    /**
     * 查询目标结果表详情
     */
    @RequiresPermissions(value = {"operate:cloud:targetOutcome:info", "operate:cloud:targetOutcome:edit"}, logical = Logical.OR)
    @GetMapping("/info/{targetOutcomeId}")
    public AjaxResult info(@PathVariable Long targetOutcomeId) {
        TargetOutcomeDTO targetOutcomeDTO = targetOutcomeService.selectTargetOutcomeByTargetOutcomeId(targetOutcomeId);
        return AjaxResult.success(targetOutcomeDTO);
    }

    /**
     * 解析目标结果表
     */
    @RequiresPermissions("operate:cloud:targetOutcome:edit")
    @PostMapping("excelParseObject")
    public AjaxResult importTargetOutcome(Long targetOutSettingId, MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
        List<Map<Integer, String>> targetSettingExcelList = read.doReadAllSync();
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = targetOutcomeService.importTargetOutcome(targetSettingExcelList, targetOutSettingId);

        return AjaxResult.successExcel(new HashMap<>(),null);
    }

    /**
     * 导出目标结果模板
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetOutcome:edit")
    @GetMapping("/export-template")
    public void exportTemplate(@RequestParam Integer targetYear, HttpServletResponse response) {
        try {
            TargetOutcomeDTO targetOutcomeDTO = targetOutcomeService.selectTargetOutcomeByTargetYear(targetYear);
            if (StringUtils.isNull(targetOutcomeDTO)) {
                throw new ServiceException("当前年度的目标结果不存在");
            }
            List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = targetOutcomeDetailsService.selectTargetOutcomeDetailsByOutcomeId(targetOutcomeDTO.getTargetOutcomeId());
            List<List<String>> headTemplate = TargetOutcomeImportListener.headTemplate(targetYear, targetOutcomeDetailsDTOList);
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(CharsetKit.UTF_8);
            String fileName = URLEncoder.encode("关键经营结果导入" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                    , CharsetKit.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream())
                    .inMemory(true)
                    .useDefaultStyle(false)
                    .head(headTemplate)
                    .sheet("关键经营结果导入")
                    .registerWriteHandler(new SheetWriteHandler() {
                        @Override
                        public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                            for (int i = 0; i < 16; i++) {
                                // 设置为文本格式
                                Sheet sheet = writeSheetHolder.getSheet();
                                CellStyle cellStyle = writeWorkbookHolder.getCachedWorkbook().createCellStyle();
                                // 49为文本格式
                                cellStyle.setDataFormat((short) 49);
                                // i为列，一整列设置为文本格式
                                sheet.setDefaultColumnStyle(i, cellStyle);
                            }
                        }
                    })
                    .registerWriteHandler(new CellWriteHandler() {
                        @Override
                        public void afterCellDispose(CellWriteHandlerContext context) {
                            Cell cell = context.getCell();
                            // 3.0 设置单元格为文本
                            WriteCellData<?> cellData = context.getFirstCellData();
                            WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                            //设置文本
                            DataFormatData dataFormatData = new DataFormatData();
                            dataFormatData.setIndex((short) 49);
                            writeCellStyle.setDataFormatData(dataFormatData);
                            // 设置字体
                            WriteFont headWriteFont = new WriteFont();
                            if ((context.getRowIndex() < 2 && context.getColumnIndex() < 2) || (context.getRowIndex() > 2 && context.getRowIndex() < 5 && context.getColumnIndex() < headTemplate.size())) {
                                headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
                                //加粗
                                headWriteFont.setBold(true);
                                headWriteFont.setFontName("微软雅黑");
                                writeCellStyle.setWriteFont(headWriteFont);
                                if (context.getColumnIndex() < headTemplate.size() && context.getColumnIndex() > 0 && context.getRowIndex() == 3) {
                                    writeCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                                } else {
                                    writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                                }
                                // 拿到poi的workbook
                                Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                                // 这里千万记住 想办法能复用的地方把他缓存起来 一个表格最多创建6W个样式
                                // 不同单元格尽量传同一个 cellStyle
                                //设置rgb颜色
                                byte[] rgb = new byte[]{(byte) 221, (byte) 235, (byte) 247};
                                CellStyle cellStyle = workbook.createCellStyle();
                                XSSFCellStyle xssfCellColorStyle = (XSSFCellStyle) cellStyle;
                                xssfCellColorStyle.setFillForegroundColor(new XSSFColor(rgb, null));
                                // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND
                                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                                // 由于这里没有指定data format 最后展示的数据 格式可能会不太正确
                                // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                                // cell里面去 会导致自己设置的不一样（很关键）
                                cellData.setWriteCellStyle(writeCellStyle);
                                cellData.setOriginCellStyle(xssfCellColorStyle);
                                cell.setCellStyle(cellStyle);
                            } else {
                                //居左
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                                //设置 自动换行
                                writeCellStyle.setWrapped(true);
                                headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
                                headWriteFont.setFontName("微软雅黑");
                                writeCellStyle.setWriteFont(headWriteFont);
                            }
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            //设置 自动换行
                            writeCellStyle.setWrapped(true);
                            //设置边框
                            writeCellStyle.setBorderLeft(BorderStyle.THIN);
                            writeCellStyle.setBorderTop(BorderStyle.THIN);
                            writeCellStyle.setBorderRight(BorderStyle.THIN);
                            writeCellStyle.setBorderBottom(BorderStyle.THIN);
                        }
                    })
                    .registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                        @Override
                        protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
                            Sheet sheet = writeSheetHolder.getSheet();
                            int columnIndex = cell.getColumnIndex();
                            // 列宽16
                            sheet.setColumnWidth(columnIndex, (270 * 16));
                            if (columnIndex > 0) {
                                sheet.setColumnWidth(columnIndex, (270 * 10));
                            }
                            // 行高7
                            sheet.setDefaultRowHeight((short) (20 * 16));
                        }
                    })
                    .doWrite(TargetOutcomeImportListener.dataTemplateList(targetOutcomeDTO, targetOutcomeDetailsDTOList, indicatorService));
        } catch (IOException e) {
            throw new ServiceException("导出失败");
        }
    }

    /**
     * 导出目标结果表
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetOutcome:export")
    @GetMapping("export")
    public void exportTargetOutcome(@RequestParam Map<String, Object> targetOutcome, TargetOutcomeDTO targetOutcomeDTO, HttpServletResponse response) {
        List<TargetOutcomeExcel> targetOutcomeExcelList = targetOutcomeService.exportTargetOutcome(targetOutcomeDTO);
        List<List<String>> head = TargetOutcomeImportListener.head(targetOutcomeDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("关键经营结果导出" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .inMemory(true)
                .useDefaultStyle(false)
                .head(head)
                .sheet("关键经营结果")// 设置 sheet 的名字
                .registerWriteHandler(new SheetWriteHandler() {
                    @Override
                    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                        for (int i = 0; i < 16; i++) {
                            // 设置为文本格式
                            Sheet sheet = writeSheetHolder.getSheet();
                            CellStyle cellStyle = writeWorkbookHolder.getCachedWorkbook().createCellStyle();
                            // 49为文本格式
                            cellStyle.setDataFormat((short) 49);
                            // i为列，一整列设置为文本格式
                            sheet.setDefaultColumnStyle(i, cellStyle);
                        }
                    }
                })
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        Cell cell = context.getCell();
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        //设置文本
                        DataFormatData dataFormatData = new DataFormatData();
                        dataFormatData.setIndex((short) 49);
                        writeCellStyle.setDataFormatData(dataFormatData);
                        // 设置字体
                        WriteFont headWriteFont = new WriteFont();
                        if ((context.getRowIndex() < 2 && context.getColumnIndex() < 2) || (context.getRowIndex() > 2 && context.getRowIndex() < 5 && context.getColumnIndex() < head.size())) {
                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            //加粗
                            headWriteFont.setBold(true);
                            headWriteFont.setFontName("微软雅黑");
                            writeCellStyle.setWriteFont(headWriteFont);
                            //设置 自动换行
                            writeCellStyle.setWrapped(true);
                            if (context.getColumnIndex() < head.size() && context.getColumnIndex() > 4 && context.getRowIndex() == 3) {
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                            } else {
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            }
                            // 拿到poi的workbook
                            Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                            // 这里千万记住 想办法能复用的地方把他缓存起来 一个表格最多创建6W个样式
                            // 不同单元格尽量传同一个 cellStyle
                            //设置rgb颜色
                            byte[] rgb = new byte[]{(byte) 221, (byte) 235, (byte) 247};
                            CellStyle cellStyle = workbook.createCellStyle();
                            XSSFCellStyle xssfCellColorStyle = (XSSFCellStyle) cellStyle;
                            xssfCellColorStyle.setFillForegroundColor(new XSSFColor(rgb, null));
                            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND
                            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            // 由于这里没有指定data format 最后展示的数据 格式可能会不太正确
                            // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                            // cell里面去 会导致自己设置的不一样（很关键）
                            cellData.setWriteCellStyle(writeCellStyle);
                            cellData.setOriginCellStyle(xssfCellColorStyle);
                            cell.setCellStyle(cellStyle);
                        } else {
                            if (context.getColumnIndex() > 1) {
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                            } else {
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            }
                            //设置 自动换行
                            writeCellStyle.setWrapped(true);
                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            headWriteFont.setFontName("微软雅黑");
                            writeCellStyle.setWriteFont(headWriteFont);
                        }
                        //垂直居中
                        writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                        //设置边框
                        writeCellStyle.setBorderLeft(BorderStyle.THIN);
                        writeCellStyle.setBorderTop(BorderStyle.THIN);
                        writeCellStyle.setBorderRight(BorderStyle.THIN);
                        writeCellStyle.setBorderBottom(BorderStyle.THIN);
                    }
                })
                .registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                    @Override
                    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
                        Sheet sheet = writeSheetHolder.getSheet();
                        int columnIndex = cell.getColumnIndex();
                        // 列宽16
                        sheet.setColumnWidth(columnIndex, (270 * 16));
                        if (columnIndex > 4) {
                            sheet.setColumnWidth(columnIndex, (270 * 10));
                        }
                        // 行高7
                        sheet.setDefaultRowHeight((short) (20 * 16));
                    }
                })
                .doWrite(TargetOutcomeImportListener.dataList(targetOutcomeExcelList, targetOutcomeDTO));
    }

    /**
     * 查询目标结果表列表
     */
    @GetMapping("/list")
    public AjaxResult list(TargetOutcomeDTO targetOutcomeDTO) {
        List<TargetOutcomeDTO> list = targetOutcomeService.selectTargetOutcomeList(targetOutcomeDTO);
        return AjaxResult.success(list);
    }

    /**
     * 战略云获取指标实际值
     */
    //@RequiresPermissions("operate:cloud:targetOutcome:getResultIndicator")
    @PostMapping("/getResultIndicator")
    public AjaxResult getResultIndicator(@RequestBody List<StrategyIntentOperateVO> strategyIntentOperateVOS) {
        return AjaxResult.success(targetOutcomeService.getResultIndicator(strategyIntentOperateVOS));
    }

    /**
     * 迁移数据
     */
    @RequiresPermissions("operate:cloud:targetOutcome:edit")
    @GetMapping("/migrationData")
    public AjaxResult migrationData(@RequestParam("targetYear") Integer targetYear) {
        return AjaxResult.success(targetOutcomeService.migrationData(targetYear));
    }

}
