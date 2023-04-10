package net.qixiaowei.operate.cloud.controller.targetManager;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.*;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetSettingService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author Graves
 * @since 2022-10-27
 */
@RestController
@RequestMapping("targetSetting")
public class TargetSettingController extends BaseController {


    @Autowired
    private ITargetSettingService targetSettingService;

    //==============================销售订单==============================//

    /**
     * 查询销售订单目标制定列表
     */
    @RequiresPermissions(value = {"operate:cloud:targetSetting:order:info", "operate:cloud:targetSetting:order:save"}, logical = Logical.OR)
    @GetMapping("/order/info")
    public AjaxResult listOrder(@Validated(TargetSettingDTO.QueryTargetSettingDTO.class) TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectOrderTargetSettingList(targetSettingDTO));
    }

    /**
     * 保存销售订单目标制定
     */
    @Log(title = "保存", businessType = BusinessType.TARGET_SETTING_ORDER, businessId = "targetSettingId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:targetSetting:order:save")
    @PostMapping("/order/save")
    public AjaxResult saveOrder(@RequestBody @Validated(TargetSettingDTO.UpdateTargetSettingDTO.class) TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success("编辑成功", targetSettingService.saveOrderTargetSetting(targetSettingDTO));
    }

    /**
     * 导出销售订单目标制定
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetSetting:order:export")
    @GetMapping("/order/export")
    public void exportOrder(@RequestParam Map<String, Object> targetSetting, TargetSettingDTO targetSettingDTO, HttpServletResponse response) {
        List<TargetSettingOrderExcel> targetSettingExcelList = targetSettingService.exportOrderTargetSetting(targetSettingDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("销售订单目标制定" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TargetSettingOrderExcel.class)
                .inMemory(true)
                .useDefaultStyle(false)
                .sheet("销售订单目标制定")
                // 自适应列宽
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        Cell cell = context.getCell();
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        //居中
                        writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                        //设置 自动换行
                        writeCellStyle.setWrapped(true);
                        //设置边框
                        writeCellStyle.setBorderLeft(BorderStyle.THIN);
                        writeCellStyle.setBorderTop(BorderStyle.THIN);
                        writeCellStyle.setBorderRight(BorderStyle.THIN);
                        writeCellStyle.setBorderBottom(BorderStyle.THIN);
                        cellData.setWriteCellStyle(writeCellStyle);
                        if (context.getRowIndex() < 2) {
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
                            // 由于这里没有指定dataformat 最后展示的数据 格式可能会不太正确
                            // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                            // cell里面去 会导致自己设置的不一样（很关键）
                            cellData.setOriginCellStyle(xssfCellColorStyle);
                            cell.setCellStyle(cellStyle);
                        } else if (context.getRowIndex() >= 2 && context.getColumnIndex() > 0) {
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                        }
                    }
                })
                .doWrite(targetSettingExcelList);
    }

    /**
     * 查询销售订单目标制定-历史年度数据
     */
    @RequiresPermissions(value = {"operate:cloud:targetSetting:order:info", "operate:cloud:targetSetting:order:save"}, logical = Logical.OR)
    @GetMapping("/orderDrop/info")
    public AjaxResult listOrderDrop(TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectOrderDropTargetSettingList(targetSettingDTO));
    }


    //==============================销售收入==============================//

    /**
     * 查询销售收入目标制定列表
     */
    @RequiresPermissions(value = {"operate:cloud:targetSetting:income:info", "operate:cloud:targetSetting:income:save"}, logical = Logical.OR)
    @GetMapping("/income/info")
    public AjaxResult listIncome(@Validated(TargetSettingDTO.QueryTargetSettingDTO.class) @RequestParam("targetYear") Integer targetYear) {
        return AjaxResult.success(targetSettingService.selectIncomeTargetSettingList(targetYear));
    }

    /**
     * 新增目标制定
     */
    @Log(title = "保存", businessType = BusinessType.TARGET_SETTING_INCOME, businessId = "targetSettingId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:targetSetting:income:save")
    @PostMapping("/income/save")
    public AjaxResult saveIncome(@RequestBody @Validated(TargetSettingDTO.UpdateTargetSettingDTO.class) TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success("编辑成功", targetSettingService.saveIncomeTargetSetting(targetSettingDTO));
    }

    /**
     * 导出销售收入目标制定
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetSetting:income:export")
    @GetMapping("/income/export")
    public void exportIncome(@RequestParam Map<String, Object> targetSetting, TargetSettingDTO targetSettingDTO, HttpServletResponse response) {
        List<TargetSettingIncomeExcel> targetSettingExcelList = targetSettingService.exportIncomeTargetSetting(targetSettingDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("销售收入目标制定" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TargetSettingIncomeExcel.class)
                .inMemory(true)
                .useDefaultStyle(false)
                .sheet("销售收入目标制定")
                // 自适应列宽
                // 自适应列宽
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        Cell cell = context.getCell();
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        //居中
                        writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                        //设置边框
                        writeCellStyle.setBorderLeft(BorderStyle.THIN);
                        writeCellStyle.setBorderTop(BorderStyle.THIN);
                        writeCellStyle.setBorderRight(BorderStyle.THIN);
                        writeCellStyle.setBorderBottom(BorderStyle.THIN);
                        cellData.setWriteCellStyle(writeCellStyle);
                        if (context.getRowIndex() < 2) {
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
                            // 由于这里没有指定dataformat 最后展示的数据 格式可能会不太正确
                            // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                            // cell里面去 会导致自己设置的不一样（很关键）
                            cellData.setOriginCellStyle(xssfCellColorStyle);
                            cell.setCellStyle(cellStyle);
                        } else if (context.getRowIndex() >= 2 && context.getColumnIndex() > 0) {
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                        }
                    }
                }).doWrite(targetSettingExcelList);
    }

    //==============================回款==============================//

    /**
     * 查询销售收入目标制定列表
     */
    @RequiresPermissions(value = {"operate:cloud:targetSetting:recovery:info", "operate:cloud:targetSetting:recovery:save"}, logical = Logical.OR)
    @GetMapping("/recovery/info")
    public AjaxResult listRecovery(@Validated(TargetSettingDTO.QueryTargetSettingDTO.class) TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectRecoveryTargetSettingList(targetSettingDTO));
    }

    /**
     * 新增目标制定
     */
    @Log(title = "保存", businessType = BusinessType.TARGET_SETTING_RECOVERY, businessId = "targetSettingId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:targetSetting:recovery:save")
    @PostMapping("/recovery/save")
    public AjaxResult saveRecoveries(@RequestBody @Validated(TargetSettingDTO.UpdateTargetSettingDTO.class) TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success("编辑成功", targetSettingService.saveRecoveryTargetSetting(targetSettingDTO));
    }

    /**
     * 导出销售回款目标制定
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetSetting:recovery:export")
    @GetMapping("/recovery/export")
    public void exportRecovery(@RequestParam Map<String, Object> targetSetting, TargetSettingDTO targetSettingDTO, HttpServletResponse response) {
        List<List<String>> headRecovery = TargetSettingImportListener.headRecovery();
        List<TargetSettingRecoveriesExcel> targetSettingRecoveriesExcels = targetSettingService.exportRecoveryTargetSetting(targetSettingDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("销售回款目标制定" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(headRecovery)
                .inMemory(true)
                .useDefaultStyle(false)
                .sheet("销售回款目标制定")
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        Cell cell = context.getCell();
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        // 设置字体
                        WriteFont headWriteFont = new WriteFont();
                        headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                        headWriteFont.setFontHeightInPoints((short) 11);
                        headWriteFont.setFontName("微软雅黑");
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                        //居中(第二行第2列第17行居中)
                        //居中(第三行第2列剧中)
                        if (context.getRowIndex() == 1) {
                            if (context.getColumnIndex() > 0 && context.getColumnIndex() < 17)
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                            else
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                        } else if (context.getRowIndex() == 2 && context.getColumnIndex() == 1) {
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                        } else {
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                        }
                        //设置 自动换行
//                        writeCellStyle.setWrapped(true);
                        //设置边框
                        writeCellStyle.setBorderLeft(BorderStyle.THIN);
                        writeCellStyle.setBorderTop(BorderStyle.THIN);
                        writeCellStyle.setBorderRight(BorderStyle.THIN);
                        writeCellStyle.setBorderBottom(BorderStyle.THIN);
                        cellData.setWriteCellStyle(writeCellStyle);
                        if (context.getRowIndex() < 3) {
                            //加粗
                            headWriteFont.setBold(true);
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
                            // 由于这里没有指定 data format 最后展示的数据 格式可能会不太正确
                            // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                            // cell里面去 会导致自己设置的不一样（很关键）
                            cellData.setOriginCellStyle(xssfCellColorStyle);
                            cell.setCellStyle(cellStyle);
                        } else if (context.getRowIndex() >= 3 && context.getColumnIndex() > 0) {
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                        }
                        writeCellStyle.setWriteFont(headWriteFont);
                    }
                })
                .registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                    @Override
                    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
                        Sheet sheet = writeSheetHolder.getSheet();
                        int columnIndex = cell.getColumnIndex();
                        // 列宽16
                        sheet.setColumnWidth(columnIndex, (270 * 10));
                        if (columnIndex == 1) {
                            sheet.setColumnWidth(columnIndex, (270 * 20));
                        }
                        // 行高7
                        sheet.setDefaultRowHeight((short) (20 * 16));
                    }
                })
                .doWrite(TargetSettingImportListener.dataList(targetSettingRecoveriesExcels));
    }

    //==============================经营目标==============================//

    /**
     * 分页查询目标制定列表
     */
    @RequiresPermissions(value = {"operate:cloud:targetSetting:info", "operate:cloud:targetSetting:edit"}, logical = Logical.OR)
    @GetMapping("/treeList")
    public AjaxResult treeList(TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectTargetSettingTreeList(targetSettingDTO));
    }

    /**
     * 修改目标制定
     */
    @RequiresPermissions("operate:cloud:targetSetting:edit")
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<TargetSettingDTO> targetSettingDTOS) {
        return AjaxResult.success("编辑成功", targetSettingService.saveTargetSettings(targetSettingDTOS));
    }

    /**
     * 添加指标时-获取指标列表
     */
    @RequiresPermissions(value = {"operate:cloud:targetSetting:info", "operate:cloud:targetSetting:edit"}, logical = Logical.OR)
    @GetMapping("/indicator")
    public AjaxResult indicator(TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectIndicatorList(targetSettingDTO));
    }

    /**
     * 添加指标时-获取指标树列表
     */
    @RequiresPermissions(value = {"operate:cloud:targetSetting:info", "operate:cloud:targetSetting:edit"}, logical = Logical.OR)
    @GetMapping("/indicatorTree")
    public AjaxResult indicatorTree(TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectIndicatorTree(targetSettingDTO));
    }

    /**
     * 导出目标制定模板
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetSetting:edit")
    @GetMapping("/export-template")
    public void exportTemplate(@RequestParam Map<String, Object> targetSetting, TargetSettingDTO targetSettingDTO, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(CharsetKit.UTF_8);
            String fileName = URLEncoder.encode("经营云-关键经营目标制定导入" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                    , CharsetKit.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), TargetSettingIncomeExcel.class)
                    .inMemory(true)
                    .useDefaultStyle(false)
                    .sheet("关键经营目标制定")
                    .registerWriteHandler(new CellWriteHandler() {
                        @Override
                        public void afterCellDispose(CellWriteHandlerContext context) {
                            Cell cell = context.getCell();
                            // 3.0 设置单元格为文本
                            WriteCellData<?> cellData = context.getFirstCellData();
                            WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                            //居中parseAnalysisExcelDate
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            //设置 自动换行
                            writeCellStyle.setWrapped(true);
                            if (context.getRowIndex() < 2) {
                                //设置边框
                                writeCellStyle.setBorderLeft(BorderStyle.THIN);
                                writeCellStyle.setBorderTop(BorderStyle.THIN);
                                writeCellStyle.setBorderRight(BorderStyle.THIN);
                                writeCellStyle.setBorderBottom(BorderStyle.THIN);
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
                            }
                        }
                    });
        } catch (IOException e) {
            throw new ServiceException("导出失败");
        }
    }

    /**
     * 解析目标制定
     */
    @RequiresPermissions("operate:cloud:targetSetting:edit")
    @PostMapping("/excelParseObject")
    public AjaxResult importEmployee(MultipartFile file, Integer targetYear) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        try {
            TargetSettingImportListener<TargetSettingExcel> listener = new TargetSettingImportListener<>(1);
            Map<String, List<TargetSettingExcel>> targetSettingExcelMaps = listener.getData(file.getInputStream(), TargetSettingExcel.class);
            List<TargetSettingDTO> targetSettingDTOS = targetSettingService.importTargetSetting(targetSettingExcelMaps, targetYear);
            return AjaxResult.success(targetSettingDTOS);
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }
    }

    /**
     * 导出目标制定
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetSetting:export")
    @GetMapping("/export")
    public void export(@RequestParam Map<String, Object> targetSetting, TargetSettingDTO targetSettingDTO, HttpServletResponse response) {
        List<List<TargetSettingExcel>> targetSettingExcelList = targetSettingService.exportTargetSetting(targetSettingDTO);
//        List<List<String>> head = TargetSettingImportListener.head();
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(CharsetKit.UTF_8);
            String fileName = URLEncoder.encode("目标制定" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                    , CharsetKit.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), TargetSettingExcel.class).inMemory(true).useDefaultStyle(false).build();
            for (int i = 0; i < targetSettingExcelList.size(); i++) {
                Integer targetYear = targetSettingExcelList.get(i).get(0).getTargetYear();
                WriteSheet sheet = EasyExcel.writerSheet(i, targetYear + "年")
                        .useDefaultStyle(false)
                        .registerWriteHandler(new SheetWriteHandler() {
                            @Override
                            public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                                for (int i = 0; i < 5; i++) {
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
                                //居中parseAnalysisExcelDate
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                                //设置 自动换行
                                writeCellStyle.setWrapped(true);
                                //设置边框
                                writeCellStyle.setBorderLeft(BorderStyle.THIN);
                                writeCellStyle.setBorderTop(BorderStyle.THIN);
                                writeCellStyle.setBorderRight(BorderStyle.THIN);
                                writeCellStyle.setBorderBottom(BorderStyle.THIN);
                                cellData.setWriteCellStyle(writeCellStyle);
                                if (context.getRowIndex() < 2) {
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
                                    cellData.setOriginCellStyle(xssfCellColorStyle);
                                    cell.setCellStyle(cellStyle);
                                } else if (context.getRowIndex() >= 2 && context.getColumnIndex() > 1) {
                                    writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                }
                            }
                        })
                        .head(TargetSettingExcel.class)
                        .build();
                excelWriter.write(targetSettingExcelList.get(i), sheet);
            }
            excelWriter.finish();
        } catch (IOException e) {
            throw new ServiceException("导出失败");
        }
    }

    //==============================经营结果分析报表==================================//

    /**
     * 查询经营分析报表列表
     */
    @RequiresPermissions("operate:cloud:targetSetting:analyse")
    @GetMapping("/analyse/list")
    public AjaxResult analyseList(TargetSettingDTO targetSettingDTO) {
        List<TargetSettingDTO> list = targetSettingService.analyseList(targetSettingDTO);
        return AjaxResult.success(list);
    }

    /**
     * 查询经营分析报表指标列表
     */
    //@RequiresPermissions("operate:cloud:targetSetting:analyse")
    @GetMapping("/analyseIndicator/list")
    public AjaxResult analyseIndicator(TargetSettingDTO targetSettingDTO) {
        List<TargetSettingDTO> list = targetSettingService.analyseIndicator(targetSettingDTO);
        return AjaxResult.success(list);
    }

    //==============================其他==============================//

    /**
     * 逻辑删除目标制定
     */
    @RequiresPermissions("operate:cloud:targetSetting:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody TargetSettingDTO targetSettingDTO) {
        return toAjax(targetSettingService.logicDeleteTargetSettingByTargetSettingId(targetSettingDTO));
    }


}
