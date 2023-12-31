package net.qixiaowei.operate.cloud.controller.salary;

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
import net.qixiaowei.integration.common.constant.CacheConstants;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryStructureDTO;
import net.qixiaowei.operate.cloud.excel.salary.OfficialRankEmolumentImportListener;
import net.qixiaowei.operate.cloud.excel.salary.SalaryPayExcel;
import net.qixiaowei.operate.cloud.excel.salary.SalaryPayImportListener;
import net.qixiaowei.operate.cloud.service.salary.ISalaryItemService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryPayDetailsService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryPayService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author Graves
 * @since 2022-11-17
 */
@RestController
@RequestMapping("salaryPay")
public class SalaryPayController extends BaseController {


    @Autowired
    private ISalaryPayService salaryPayService;

    @Autowired
    private ISalaryItemService salaryItemService;

    @Autowired
    private ISalaryPayDetailsService salaryPayDetailsService;

    @Autowired
    private RedisService redisService;

    //==============================月度工资数据管理==================================//

    /**
     * 分页查询工资发薪表列表
     */
    @RequiresPermissions("operate:cloud:salaryPay:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(SalaryPayDTO salaryPayDTO) {
        startPage();
        List<SalaryPayDTO> list = salaryPayService.selectSalaryPayList(salaryPayDTO);
        return getDataTable(list);
    }

    /**
     * 新增工资发薪表
     */
    @Log(title = "新增月度工资数据", businessType = BusinessType.SALARY_PAY, businessId = "salaryPayId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:salaryPay:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody SalaryPayDTO salaryPayDTO) {
        return AjaxResult.success(salaryPayService.insertSalaryPay(salaryPayDTO));
    }

    /**
     * 修改工资发薪表
     */
    @Log(title = "保存月度工资数据", businessType = BusinessType.SALARY_PAY, businessId = "salaryPayId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:salaryPay:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody SalaryPayDTO salaryPayDTO) {
        return toAjax(salaryPayService.updateSalaryPay(salaryPayDTO));
    }

    /**
     * 查询工资发薪表详情
     */
    @RequiresPermissions(value = {"operate:cloud:salaryPay:info", "operate:cloud:salaryPay:edit"}, logical = Logical.OR)
    @GetMapping("/info/{salaryPayId}")
    public AjaxResult info(@PathVariable Long salaryPayId) {
        SalaryPayDTO salaryPayDTO = salaryPayService.selectSalaryPayBySalaryPayId(salaryPayId);
        return AjaxResult.success(salaryPayDTO);
    }

    /**
     * 逻辑删除工资发薪表
     */
    @RequiresPermissions("operate:cloud:salaryPay:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody SalaryPayDTO salaryPayDTO) {
        return toAjax(salaryPayService.logicDeleteSalaryPayBySalaryPayId(salaryPayDTO));
    }

    /**
     * 逻辑批量删除工资发薪表
     */
    @RequiresPermissions("operate:cloud:salaryPay:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> salaryPayIds) {
        return toAjax(salaryPayService.logicDeleteSalaryPayBySalaryPayIds(salaryPayIds));
    }

    /**
     * 导入工资发薪表
     */
    @RequiresPermissions("operate:cloud:salaryPay:import")
    @PostMapping("import")
    public AjaxResult importSalaryPay(MultipartFile file) {
        String filename = file.getOriginalFilename();
//        月度工资条导入错误报告
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        Map<Object, Object> data;
        try {
            //构建读取器
            SalaryPayImportListener salaryPayImportListener = new SalaryPayImportListener(salaryPayService, salaryItemService);
            ExcelReaderBuilder builder = EasyExcel.read(new BufferedInputStream(file.getInputStream()), salaryPayImportListener).headRowNumber(1);
            builder.sheet().doRead();
            data = salaryPayImportListener.getData();
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("导入人员信息配置Excel失败");
        }
        return AjaxResult.successExcel(data, "操作成功");
    }

    /**
     * 导出工资发薪表
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:salaryPay:export")
    @PostMapping("export")
    public void exportSalaryPay(@RequestBody SalaryPayDTO salaryPayDTO, HttpServletResponse response) {
        Integer templateType = salaryPayDTO.getTemplateType();
        List<SalaryPayDTO> salaryPayDTOS = salaryPayService.selectSalaryPayBySalaryPay(salaryPayDTO.getIsSelect(), salaryPayDTO.getSalaryPayIds());
        int size = salaryPayDTOS.size();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        if (StringUtils.isNull(templateType) || templateType == 1) { // 非月度工资
            String fileName = URLEncoder.encode("工资条导出" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                    , CharsetKit.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream())
                    .inMemory(true)
                    .useDefaultStyle(false)
                    .sheet("工资条导出")
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
                            if (context.getRowIndex() % 3 == 0) {
                                headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
                                //加粗
                                headWriteFont.setBold(true);
                                headWriteFont.setFontName("微软雅黑");
                                writeCellStyle.setWriteFont(headWriteFont);
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                                //设置 自动换行
                                writeCellStyle.setWrapped(true);
                            } else {
                                if (context.getColumnIndex() > 3) {
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
                            if (context.getRowIndex() % 3 == 0 || context.getRowIndex() % 3 == 1) {
                                //设置边框
                                writeCellStyle.setBorderLeft(BorderStyle.THIN);
                                writeCellStyle.setBorderTop(BorderStyle.THIN);
                                writeCellStyle.setBorderRight(BorderStyle.THIN);
                                writeCellStyle.setBorderBottom(BorderStyle.THIN);
                                // 这里千万记住 想办法能复用的地方把他缓存起来 一个表格最多创建6W个样式
                                // 不同单元格尽量传同一个 cellStyle
                                cellData.setWriteCellStyle(writeCellStyle);
                            }
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                        }
                    })
                    .registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                        @Override
                        protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
                            Sheet sheet = writeSheetHolder.getSheet();
                            int columnIndex = cell.getColumnIndex();
                            // 列宽16
                            sheet.setColumnWidth(columnIndex, (270 * 16));
                            // 行高7
                            sheet.setDefaultRowHeight((short) (20 * 16));
                        }
                    })
                    .doWrite(SalaryPayImportListener.dataMonthList(salaryPayDTOS));
        } else { // 月度工资
            String fileName = URLEncoder.encode("月度工资数据导出" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                    , CharsetKit.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            List<SalaryItemDTO> salaryItemDTOS = salaryItemService.selectSalaryItemList(new SalaryItemDTO());
            List<List<String>> head = SalaryPayImportListener.head(salaryItemDTOS);
            EasyExcel.write(response.getOutputStream())
                    .inMemory(true)
                    .useDefaultStyle(false)
                    .sheet("月度工资数据导出")
                    .head(head)
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
                            if (context.getRowIndex() < 1) {
                                headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
                                headWriteFont.setBold(true);
                                headWriteFont.setFontName("微软雅黑");
                                writeCellStyle.setWriteFont(headWriteFont);
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                                writeCellStyle.setWrapped(true);
                                writeCellStyle.setBorderLeft(BorderStyle.THIN);
                                writeCellStyle.setBorderTop(BorderStyle.THIN);
                                writeCellStyle.setBorderRight(BorderStyle.THIN);
                                writeCellStyle.setBorderBottom(BorderStyle.THIN);
                                // 拿到poi的workbook
                                Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                                byte[] rgb = new byte[]{(byte) 221, (byte) 235, (byte) 247};
                                CellStyle cellStyle = workbook.createCellStyle();
                                XSSFCellStyle xssfCellColorStyle = (XSSFCellStyle) cellStyle;
                                xssfCellColorStyle.setFillForegroundColor(new XSSFColor(rgb, null));
                                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                                cellData.setWriteCellStyle(writeCellStyle);
                                cellData.setOriginCellStyle(xssfCellColorStyle);
                                cell.setCellStyle(cellStyle);
                            } else {
                                if (context.getColumnIndex() > 4) {
                                    writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                } else {
                                    writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                                }
                                headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
                                headWriteFont.setFontName("微软雅黑");
                                writeCellStyle.setWriteFont(headWriteFont);
                            }
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                        }
                    })
                    .registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                        @Override
                        protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
                            Sheet sheet = writeSheetHolder.getSheet();
                            int columnIndex = cell.getColumnIndex();
                            // 列宽16
                            sheet.setColumnWidth(columnIndex, (270 * 16));
                            // 行高7
                            sheet.setDefaultRowHeight((short) (20 * 16));
                        }
                    })
                    .doWrite(SalaryPayImportListener.dataList(salaryPayDTOS, salaryItemDTOS));
        }

    }

    /**
     * 导出工资发薪模板
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:salaryPay:import")
    @GetMapping("/export-template")
    public void exportTemplate(SalaryPayExcel salaryPayExcel, HttpServletResponse response) {
        try {
            List<SalaryItemDTO> salaryItemDTOS1 = salaryItemService.selectSalaryItemList(new SalaryItemDTO());
            List<List<String>> headTemplate = SalaryPayImportListener.headTemplate(salaryItemDTOS1, false);
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(CharsetKit.UTF_8);
            String fileName = URLEncoder.encode("经营云-月度工资数据管理导入" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                    , CharsetKit.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream())
                    .inMemory(true)
                    .sheet(0, "月度工资条导入")
                    .useDefaultStyle(false)
                    .head(headTemplate)
                    .registerWriteHandler(new SheetWriteHandler() {
                        @Override
                        public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                            for (int i = 0; i < salaryItemDTOS1.size() + 3; i++) {
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
                            // 拿到poi的workbook
                            // 这里千万记住 想办法能复用的地方把他缓存起来 一个表格最多创建6W个样式
                            // 不同单元格尽量传同一个 cellStyle
                            Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                            CellStyle cellStyle = workbook.createCellStyle();
                            if (context.getRowIndex() < 1) {
                                headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
                                //加粗
                                headWriteFont.setBold(true);
                                if (context.getColumnIndex() == 2) {
                                    headWriteFont.setColor(IndexedColors.RED.getIndex());
                                }
                                headWriteFont.setFontName("微软雅黑");
                                writeCellStyle.setWriteFont(headWriteFont);
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                                cellData.setWriteCellStyle(writeCellStyle);
                            } else {
                                //居左
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                                //设置 自动换行
                                writeCellStyle.setWrapped(true);
                                headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
                                headWriteFont.setFontName("微软雅黑");
                                writeCellStyle.setWriteFont(headWriteFont);
                                cellData.setWriteCellStyle(writeCellStyle);
                                if (context.getColumnIndex() == 0) {
                                    //设置rgb颜色
                                    byte[] rgb = new byte[]{(byte) 217, (byte) 217, (byte) 217};
                                    XSSFCellStyle xssfCellColorStyle = (XSSFCellStyle) cellStyle;
                                    xssfCellColorStyle.setFillForegroundColor(new XSSFColor(rgb, null));
                                    // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND
                                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                                    cellData.setOriginCellStyle(xssfCellColorStyle);
                                }
                                cell.setCellStyle(cellStyle);
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
                            // 行高16
                            sheet.setDefaultRowHeight((short) (20 * 16));
                        }
                    })
                    .doWrite(new ArrayList<>());
        } catch (IOException e) {
            throw new ServiceException("导出失败");
        }
    }

    /**
     * 导出工资发薪模板
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:salaryPay:import")
    @GetMapping("/export-error")
    public void exportError(SalaryPayExcel salaryPayExcel, HttpServletResponse response) {
        try {
            List<SalaryItemDTO> salaryItemDTOS1 = salaryItemService.selectSalaryItemList(new SalaryItemDTO());
            List<List<String>> headTemplate = SalaryPayImportListener.headTemplate(salaryItemDTOS1, true);

            String errorExcelId = salaryPayExcel.getErrorExcelId();
            List<List<String>> errorList = redisService.getCacheObject(CacheConstants.ERROR_EXCEL_KEY + errorExcelId);
            if (StringUtils.isEmpty(errorList)) {
                throw new ServiceException("当前错误报告已过期");
            }
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(CharsetKit.UTF_8);
            String fileName = URLEncoder.encode("经营云-月度工资数据管理导入" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                    , CharsetKit.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream())
                    .inMemory(true)
                    .sheet(0, "月度工资条导入错误报告")
                    .useDefaultStyle(false)
                    .head(headTemplate)
                    .registerWriteHandler(new SheetWriteHandler() {
                        @Override
                        public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                            for (int i = 0; i < salaryItemDTOS1.size() + 4; i++) {
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
                            // 拿到poi的workbook
                            // 这里千万记住 想办法能复用的地方把他缓存起来 一个表格最多创建6W个样式
                            // 不同单元格尽量传同一个 cellStyle
                            Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                            CellStyle cellStyle = workbook.createCellStyle();
                            if (context.getRowIndex() < 1) {
                                if (context.getColumnIndex() == 1) {
                                    headWriteFont.setColor(IndexedColors.RED.getIndex());
                                } else {
                                    headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                }
                                headWriteFont.setFontHeightInPoints((short) 11);
                                //加粗
                                headWriteFont.setBold(true);
                                if (context.getColumnIndex() == 0 && context.getColumnIndex() == 3) {
                                    headWriteFont.setColor(IndexedColors.RED.getIndex());
                                }
                                headWriteFont.setFontName("微软雅黑");
                                writeCellStyle.setWriteFont(headWriteFont);
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                                cellData.setWriteCellStyle(writeCellStyle);
                            } else {
                                //居左
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                                //设置 自动换行
                                writeCellStyle.setWrapped(true);
                                if (context.getColumnIndex() == 0) {
                                    headWriteFont.setColor(IndexedColors.RED.getIndex());
                                } else {
                                    headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                }
                                headWriteFont.setFontHeightInPoints((short) 11);
                                headWriteFont.setFontName("微软雅黑");
                                writeCellStyle.setWriteFont(headWriteFont);
                                cellData.setWriteCellStyle(writeCellStyle);
                                if (context.getColumnIndex() == 0) {
                                    //设置rgb颜色
                                    byte[] rgb = new byte[]{(byte) 217, (byte) 217, (byte) 217};
                                    XSSFCellStyle xssfCellColorStyle = (XSSFCellStyle) cellStyle;
                                    xssfCellColorStyle.setFillForegroundColor(new XSSFColor(rgb, null));
                                    // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND
                                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                                    cellData.setOriginCellStyle(xssfCellColorStyle);
                                }
                                cell.setCellStyle(cellStyle);
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
                            // 行高16
                            sheet.setDefaultRowHeight((short) (20 * 16));
                        }
                    })
                    .doWrite(OfficialRankEmolumentImportListener.dataErrorList(errorList));
        } catch (IOException e) {
            throw new ServiceException("导出失败");
        }
    }

    //==============================薪酬架构报表==================================//

    /**
     * 查询薪酬架构报表列表
     */
    @RequiresPermissions("operate:cloud:salaryPay:structure")
    @GetMapping("/structure")
    public AjaxResult structure(SalaryStructureDTO salaryStructureDTO) {
        return AjaxResult.success(salaryPayService.selectSalaryPayStructure(salaryStructureDTO));
    }

    /**
     * 查询薪酬架构报表列表
     */
    @RequiresPermissions("operate:cloud:salaryPay:structure")
    @GetMapping("/pageList/structure")
    public AjaxResult listStructure(SalaryStructureDTO salaryStructureDTO) {
        return AjaxResult.success(salaryPayService.selectSalaryPayStructureList(salaryStructureDTO));
    }

    //==============================其他==================================//

    /**
     * 查询工资发薪表列表
     */
    @GetMapping("/list")
    public AjaxResult list(SalaryPayDTO salaryPayDTO) {
        List<SalaryPayDTO> list = salaryPayService.selectSalaryPayList(salaryPayDTO);
        return AjaxResult.success(list);
    }

}
