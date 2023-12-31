package net.qixiaowei.operate.cloud.controller.salary;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
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
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.salary.OfficialRankEmolumentDTO;
import net.qixiaowei.operate.cloud.excel.salary.OfficialRankEmolumentImportListener;
import net.qixiaowei.operate.cloud.service.salary.IOfficialRankEmolumentService;
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
import java.util.List;
import java.util.Map;


/**
 * @author Graves
 * @since 2022-11-30
 */
@RestController
@RequestMapping("officialRankEmolument")
public class OfficialRankEmolumentController extends BaseController {


    @Autowired
    private IOfficialRankEmolumentService officialRankEmolumentService;

    @Autowired
    private RedisService redisService;


    /**
     * 查询职级薪酬表详情
     */
    @RequiresPermissions(value = {"operate:cloud:officialRankEmolument:info", "operate:cloud:officialRankEmolument:edit"}, logical = Logical.OR)
    @GetMapping("/info")
    public AjaxResult info(OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        return AjaxResult.success(officialRankEmolumentService.selectOfficialRankEmolumentByOfficialRankEmolumentId(officialRankEmolumentDTO));
    }

    /**
     * 查看该职级的分解信息
     */
    @RequiresPermissions(value = {"operate:cloud:officialRankEmolument:info", "operate:cloud:officialRankEmolument:edit"}, logical = Logical.OR)
    @GetMapping("/decomposeInfo")
    public AjaxResult list(OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        return AjaxResult.success(officialRankEmolumentService.selectOfficialDecomposeList(officialRankEmolumentDTO));
    }

    /**
     * 修改职级薪酬表
     */
    @Log(title = "保存", businessType = BusinessType.OFFICIAL_RANK_EMOLUMENT, businessId = "officialRankSystemId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:officialRankEmolument:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        return toAjax(officialRankEmolumentService.updateOfficialRankEmolument(officialRankEmolumentDTO));
    }

    /**
     * 根据岗位ID获取职级确定薪酬提示
     */
    @RequiresPermissions(value = {"operate:cloud:officialRankEmolument:info", "operate:cloud:officialRankEmolument:edit"}, logical = Logical.OR)
    @GetMapping("/selectByPostId/{postId}")
    public AjaxResult selectByPostId(@PathVariable Long postId) {
        return AjaxResult.success(officialRankEmolumentService.selectByPostId(postId));
    }

    /**
     * 导入职级确定薪酬
     */
    @RequiresPermissions(value = {"operate:cloud:officialRankEmolument:info", "operate:cloud:officialRankEmolument:import"}, logical = Logical.OR)
    @PostMapping("import")
    public AjaxResult importOfficialRankEmolument(OfficialRankEmolumentDTO officialRankEmolumentDTO, MultipartFile file) {
        Map<Object, Object> objectObjectMap = officialRankEmolumentService.importOfficialRankEmolument(officialRankEmolumentDTO, file);
        return AjaxResult.successExcel(objectObjectMap, "操作成功");
    }

    /**
     * 导入职级确定薪酬
     */
     @RequiresPermissions(value = {"operate:cloud:officialRankEmolument:info", "operate:cloud:officialRankEmolument:import"}, logical = Logical.OR)
    @GetMapping("export-error")
    public void exportErrorList(OfficialRankEmolumentDTO officialRankEmolumentDTO, HttpServletResponse response) {
        try {
            String errorExcelId = officialRankEmolumentDTO.getErrorExcelId();
            OfficialRankEmolumentDTO emolumentDTO = officialRankEmolumentService.selectOfficialRankEmolumentByOfficialRankEmolumentId(officialRankEmolumentDTO);
            List<List<String>> head = OfficialRankEmolumentImportListener.getHead(emolumentDTO.getOfficialRankSystemName(), true);
            List<List<String>> errorList = redisService.getCacheObject(CacheConstants.ERROR_EXCEL_KEY + errorExcelId);
            if (StringUtils.isEmpty(errorList)) {
                throw new ServiceException("当前错误报告已过期");
            }
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(CharsetKit.UTF_8);
            String fileName = URLEncoder.encode("职级确定薪酬导入错误报告" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream())
                    .head(head)
                    .inMemory(true)
                    .useDefaultStyle(false)
                    .sheet("职级确定薪酬导入错误报告")
                    .registerWriteHandler(new SheetWriteHandler() {
                        @Override
                        public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                            for (int i = 0; i < 6; i++) {
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
                            // 设置字体
                            WriteFont headWriteFont = new WriteFont();
                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            headWriteFont.setFontName("微软雅黑");
                            if (context.getRowIndex() < 3) {
                                headWriteFont.setBold(true);
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                                Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                                byte[] rgb = new byte[]{(byte) 221, (byte) 235, (byte) 247};
                                CellStyle cellStyle = workbook.createCellStyle();
                                XSSFCellStyle xssfCellColorStyle = (XSSFCellStyle) cellStyle;
                                xssfCellColorStyle.setFillForegroundColor(new XSSFColor(rgb, null));
                                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                                cellData.setOriginCellStyle(xssfCellColorStyle);
                                cell.setCellStyle(cellStyle);
                            } else if (context.getRowIndex() > 2 && context.getColumnIndex() < 2 && context.getColumnIndex() > 0) {
                                Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                                byte[] rgb = new byte[]{(byte) 217, (byte) 217, (byte) 217};
                                CellStyle cellStyle = workbook.createCellStyle();
                                XSSFCellStyle xssfCellColorStyle = (XSSFCellStyle) cellStyle;
                                xssfCellColorStyle.setFillForegroundColor(new XSSFColor(rgb, null));
                                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                                cellData.setOriginCellStyle(xssfCellColorStyle);
                                cell.setCellStyle(cellStyle);
                            } else if (context.getRowIndex() > 2 && context.getColumnIndex() > 1) {
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                            }
                            if (context.getRowIndex() > 1 && context.getColumnIndex() == 0) {
                                headWriteFont.setColor(IndexedColors.RED.getIndex());
                            }
                            writeCellStyle.setWriteFont(headWriteFont);
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
//                            if (columnIndex == 0) {
//                                sheet.setColumnWidth(columnIndex, (270 * 22));
//                            }
                            // 行高7
                            sheet.setDefaultRowHeight((short) (20 * 16));
                        }
                    })
                    .doWrite(OfficialRankEmolumentImportListener.dataErrorList(errorList));
        } catch (IOException e) {
            throw new ServiceException("导出失败");
        }
    }

    /**
     * 导出职级确定薪酬
     */
    @SneakyThrows
     @RequiresPermissions(value = {"operate:cloud:officialRankEmolument:info", "operate:cloud:officialRankEmolument:import"}, logical = Logical.OR)
    @GetMapping("/export-template")
    public void exportTemplate(OfficialRankEmolumentDTO officialRankEmolumentDTO, HttpServletResponse response) {
        try {
            OfficialRankEmolumentDTO emolumentDTO = officialRankEmolumentService.selectOfficialRankEmolumentByOfficialRankEmolumentId(officialRankEmolumentDTO);
            List<List<String>> head = OfficialRankEmolumentImportListener.getHead(emolumentDTO.getOfficialRankSystemName(), false);
            List<OfficialRankEmolumentDTO> officialRankEmolumentDTOList = emolumentDTO.getOfficialRankEmolumentDTOList();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(CharsetKit.UTF_8);
            String fileName = URLEncoder.encode("职级确定薪酬导入" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream())
                    .head(head)
                    .inMemory(true)
                    .useDefaultStyle(false)
                    .sheet("职级确定薪酬导入")
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
                            WriteFont headWriteFont = new WriteFont();
                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            headWriteFont.setFontName("微软雅黑");
                            if (context.getRowIndex() < 3) {
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
                                // 由于这里没有指定data format 最后展示的数据 格式可能会不太正确
                                // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                                // cell里面去 会导致自己设置的不一样（很关键）
                                cellData.setOriginCellStyle(xssfCellColorStyle);
                                cell.setCellStyle(cellStyle);
                            } else if (context.getRowIndex() > 2 && context.getColumnIndex() < 1) {
                                // 拿到poi的workbook
                                Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                                // 这里千万记住 想办法能复用的地方把他缓存起来 一个表格最多创建6W个样式
                                // 不同单元格尽量传同一个 cellStyle
                                //设置rgb颜色
                                byte[] rgb = new byte[]{(byte) 217, (byte) 217, (byte) 217};
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
                            } else if (context.getRowIndex() > 2 && context.getColumnIndex() > 0) {
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                            }
                            writeCellStyle.setWriteFont(headWriteFont);
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
                    .doWrite(OfficialRankEmolumentImportListener.dataList(officialRankEmolumentDTOList));
        } catch (IOException e) {
            throw new ServiceException("导出失败");
        }
    }

}
