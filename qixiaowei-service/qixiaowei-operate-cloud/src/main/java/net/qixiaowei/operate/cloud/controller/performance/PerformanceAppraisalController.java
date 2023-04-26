package net.qixiaowei.operate.cloud.controller.performance;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.DataFormatData;
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
import net.qixiaowei.integration.common.utils.excel.SelectSheetWriteHandler;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalImportListener;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalColumnsService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalService;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Graves
 * @since 2022-11-23
 */
@RestController
@RequestMapping("performanceAppraisal")
public class PerformanceAppraisalController extends BaseController {


    @Autowired
    private IPerformanceAppraisalService performanceAppraisalService;

    @Autowired
    private IPerformanceAppraisalColumnsService performanceAppraisalColumnsService;

    @Autowired
    private RedisService redisService;

    /**
     * 分页查询绩效考核表列表
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:pageList")
    @DataScope(businessAlias = "pa")
    @GetMapping("/pageList")
    public TableDataInfo pageList(PerformanceAppraisalDTO performanceAppraisalDTO) {
        startPage();
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectPerformanceAppraisalList(performanceAppraisalDTO);
        return getDataTable(list);
    }

    //==============================绩效考核任务==================================//

    /**
     * 新增绩效考核表
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(PerformanceAppraisalDTO.AddPerformanceAppraisalDTO.class) PerformanceAppraisalDTO performanceAppraisalDTO) {
        return AjaxResult.success("新增成功", performanceAppraisalService.insertPerformanceAppraisal(performanceAppraisalDTO));
    }

    /**
     * 查询绩效考核表详情
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:info")
    @GetMapping("/info/{performanceAppraisalId}")
    public AjaxResult info(@PathVariable Long performanceAppraisalId) {
        PerformanceAppraisalDTO performanceAppraisalDTO = performanceAppraisalService.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        return AjaxResult.success(performanceAppraisalDTO);
    }

    /**
     * 逻辑删除绩效考核表
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody PerformanceAppraisalDTO performanceAppraisalDTO) {
        return toAjax(performanceAppraisalService.logicDeletePerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalDTO));
    }

    /**
     * 逻辑批量删除绩效考核表
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> performanceAppraisalIds) {
        return toAjax(performanceAppraisalService.logicDeletePerformanceAppraisalByPerformanceAppraisalIds(performanceAppraisalIds));
    }

    //==============================组织绩效管理==================================//
    //==============================组织绩效制定==================================//

    /**
     * 查询绩效考核表列表-组织-制定
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgDevelop:pageList")
    @DataScope(businessAlias = "pa")
    @GetMapping("/orgDevelop/pageList")
    public TableDataInfo listOrgDevelop(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        return performanceAppraisalService.selectOrgAppraisalDevelopList(performanceAppraisalObjectsDTO);
    }

    /**
     * 编辑组织绩效考核制定表
     */
    @Log(title = "保存组织绩效制定", businessType = BusinessType.PERFORMANCE_APPRAISAL_ORG_SETTING, businessId = "performAppraisalObjectsId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgDevelop:edit")
    @PostMapping("/orgDevelop/edit")
    public AjaxResult editOrgDevelop(@RequestBody @Validated(PerformanceAppraisalObjectsDTO.UpdatePerformanceAppraisalObjectsDTO.class) PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        return AjaxResult.success("保存成功", performanceAppraisalService.updateOrgDevelopPerformanceAppraisal(performanceAppraisalObjectsDTO));
    }

    /**
     * 查询组织绩效考核表详情-制定
     */
    @RequiresPermissions(value = {"operate:cloud:performanceAppraisal:orgDevelop:info", "operate:cloud:performanceAppraisal:orgDevelop:edit"}, logical = Logical.OR)
    @GetMapping("/orgDevelop/info/{performAppraisalObjectsId}")
    public AjaxResult infoOrgDevelop(@PathVariable Long performAppraisalObjectsId) {
        return AjaxResult.success(performanceAppraisalService.selectOrgAppraisalDevelopById(performAppraisalObjectsId));
    }

    //==============================组织绩效评议==================================//

    /**
     * 查询绩效考核表列表-组织-评议
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgReview:pageList")
    @DataScope(businessAlias = "pa")
    @GetMapping("/orgReview/pageList")
    public TableDataInfo listOrgReview(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        return performanceAppraisalService.selectOrgAppraisalReviewList(performanceAppraisalObjectsDTO);
    }

    /**
     * 编辑组织绩效考核评议表
     */
    @Log(title = "保存组织绩效评议", businessType = BusinessType.PERFORMANCE_APPRAISAL_ORG_REVIEW, businessId = "performAppraisalObjectsId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgReview:edit")
    @PostMapping("/orgReview/edit")
    public AjaxResult editOrgReview(@RequestBody @Validated(PerformanceAppraisalObjectsDTO.UpdatePerformanceAppraisalObjectsDTO.class) PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        return AjaxResult.success("保存成功", performanceAppraisalService.updateOrgReviewPerformanceAppraisal(performanceAppraisalObjectsDTO));
    }

    /**
     * 查询组织绩效考核表详情-评议
     */
    @RequiresPermissions(value = {"operate:cloud:performanceAppraisal:orgReview:info", "operate:cloud:performanceAppraisal:orgReview:edit"}, logical = Logical.OR)
    @GetMapping("/orgReview/info/{performAppraisalObjectsId}")
    public AjaxResult infoOrgReview(@PathVariable Long performAppraisalObjectsId) {
        return AjaxResult.success(performanceAppraisalService.selectOrgAppraisalReviewById(performAppraisalObjectsId));
    }

    //==============================组织绩效排名==================================//

    /**
     * 查询绩效考核表列表-组织-排名
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgRanking:pageList")
    @DataScope(businessAlias = "pa")
    @GetMapping("/orgRanking/pageList")
    public TableDataInfo listOrgRanking(PerformanceAppraisalDTO performanceAppraisalDTO) {
        startPage();
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectOrgAppraisalRankingList(performanceAppraisalDTO);
        return getDataTable(list);
    }

    /**
     * 查询绩效考核详情-组织-排名
     */
    @RequiresPermissions(value = {"operate:cloud:performanceAppraisal:orgRanking:info", "operate:cloud:performanceAppraisal:orgRanking:edit"}, logical = Logical.OR)
    @GetMapping("/orgRanking/info/{performanceAppraisalId}")
    public AjaxResult infoOrgRanking(@PathVariable Long performanceAppraisalId) {
        return AjaxResult.success(performanceAppraisalService.selectOrgAppraisalRankingById(performanceAppraisalId));
    }

    /**
     * 编辑绩效考核-组织-排名
     */
    @Log(title = "保存组织绩效排名", businessType = BusinessType.PERFORMANCE_APPRAISAL_ORG_RANKING, businessId = "performanceAppraisalId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgRanking:edit")
    @PostMapping("/orgRanking/edit")
    public AjaxResult editOrgRanking(@RequestBody @Validated(PerformanceAppraisalDTO.UpdatePerformanceAppraisalDTO.class) PerformanceAppraisalDTO performanceAppraisalDTO) {
        return toAjax(performanceAppraisalService.updateOrgRankingPerformanceAppraisal(performanceAppraisalDTO));
    }

    //==============================组织绩效归档==================================//

    /**
     * 查询绩效考核表列表-组织-归档
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgArchive:pageList")
    @DataScope(businessAlias = "pa")
    @GetMapping("/orgArchive/pageList")
    public TableDataInfo listOrgArchive(PerformanceAppraisalDTO performanceAppraisalDTO) {
        startPage();
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectOrgAppraisalArchiveList(performanceAppraisalDTO);
        return getDataTable(list);
    }

    /**
     * 查询绩效考核表详情-组织-归档
     */
    @RequiresPermissions(value = {"operate:cloud:performanceAppraisal:orgArchive:info", "operate:cloud:performanceAppraisal:orgArchive:edit"}, logical = Logical.OR)
    @GetMapping("/orgArchive/info/{performanceAppraisalId}")
    public AjaxResult infoOrgArchive(@PathVariable Long performanceAppraisalId) {
        return AjaxResult.success(performanceAppraisalService.selectOrgAppraisalArchiveById(performanceAppraisalId));
    }

    /**
     * 导入组织绩效归档表
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgArchive:import")
    @PostMapping("orgImport")
    public AjaxResult importOrgPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file) {
//        Integer importType = performanceAppraisalDTO.getImportType();
//        if (importType.equals(1)) {
        Map<Object, Object> objectObjectMap = performanceAppraisalService.importSysOrgPerformanceAppraisal(performanceAppraisalDTO, file);
//        } else {
//            performanceAppraisalService.importCustomOrgPerformanceAppraisal(performanceAppraisalDTO, file);
//        }
        return AjaxResult.successExcel(objectObjectMap, "导入成功");
    }

    /**
     * 导出组织模板表
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgArchive:import")
    @GetMapping("export-org-template")
    public void exportOrgTemplate(@RequestParam(required = false) Integer importType, @RequestParam Long performanceAppraisalId, HttpServletResponse response) {
//        if (StringUtils.isNull(importType)) {
//            throw new ServiceException("请选择考核流程");
//        }
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalService.selectPerformanceRankFactor(performanceAppraisalId);
        List<EmployeeDTO> employeeData = performanceAppraisalService.getEmployeeData();
        Map<Integer, List<String>> selectMap = new HashMap<>();
        List<List<String>> head;
        String fileName;
//        if (importType.equals(1)) {//系统流程
        head = PerformanceAppraisalImportListener.headOrgSystemTemplate(selectMap, performanceRankFactorDTOS, employeeData, false);
        fileName = URLEncoder.encode("组织绩效归档导入" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
//        } else {
//            head = PerformanceAppraisalImportListener.headOrgCustomTemplate(selectMap, performanceRankFactorDTOS);
//            fileName = URLEncoder.encode("组织绩效归档导入自定义模板" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
//        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        PerformanceAppraisalDTO appraisalDTO = performanceAppraisalService.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalService.selectOrgAppraisalObjectList(performanceAppraisalId);
        EasyExcel.write(response.getOutputStream())
                .inMemory(true)
                .useDefaultStyle(false)
                .head(head)
                .sheet("组织绩效归档导入")// 设置 sheet 的名字
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap, 1, 65533))
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
                .doWrite(PerformanceAppraisalImportListener.dataTemplateList(performanceAppraisalObjectsDTOList, null, appraisalDTO));
    }

    /**
     * 导出组织模板表
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgArchive:import")
    @GetMapping("export-org-error")
    public void exportOrgError(@RequestParam String errorExcelId, @RequestParam Long performanceAppraisalId, HttpServletResponse response) {
//        if (StringUtils.isNull(importType)) {
//            throw new ServiceException("请选择考核流程");
//        }
        List<List<Object>> errorList = redisService.getCacheObject(CacheConstants.ERROR_EXCEL_KEY + errorExcelId);
        if (StringUtils.isEmpty(errorList)) {
            throw new ServiceException("当前错误报告已过期");
        }
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalService.selectPerformanceRankFactor(performanceAppraisalId);
        List<EmployeeDTO> employeeData = performanceAppraisalService.getEmployeeData();
        Map<Integer, List<String>> selectMap = new HashMap<>();
        List<List<String>> head;
        String fileName;
//        if (importType.equals(1)) {//系统流程
        head = PerformanceAppraisalImportListener.headOrgSystemTemplate(selectMap, performanceRankFactorDTOS, employeeData, true);
        fileName = URLEncoder.encode("组织绩效归档导入" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
//        } else {
//            head = PerformanceAppraisalImportListener.headOrgCustomTemplate(selectMap, performanceRankFactorDTOS);
//            fileName = URLEncoder.encode("组织绩效归档导入自定义模板" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
//        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        PerformanceAppraisalDTO appraisalDTO = performanceAppraisalService.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalService.selectOrgAppraisalObjectList(performanceAppraisalId);
        EasyExcel.write(response.getOutputStream())
                .inMemory(true)
                .useDefaultStyle(false)
                .head(head)
                .sheet("组织绩效归档导入错误报告")// 设置 sheet 的名字
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap, 1, 65533))
                .registerWriteHandler(new SheetWriteHandler() {
                    @Override
                    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                        for (int i = 0; i < 7; i++) {
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
                            cellData.setWriteCellStyle(writeCellStyle);
                            if (context.getColumnIndex() == 1) {
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
                        if (context.getColumnIndex() == 0) {
                            headWriteFont.setColor(IndexedColors.RED.getIndex());
                        }
                        writeCellStyle.setWriteFont(headWriteFont);
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
                .doWrite(PerformanceAppraisalImportListener.dataTemplateList(performanceAppraisalObjectsDTOList, errorList, appraisalDTO));
    }

    /**
     * 导出组织表
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgArchive:info")
    @GetMapping("exportOrg")
    public void exportOrg(@RequestParam Long performanceAppraisalId, HttpServletResponse response) {
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalService.selectPerformanceRankFactor(performanceAppraisalId);
//        Integer selfDefinedColumnsFlag = performanceRankFactorDTOS.get(0).getSelfDefinedColumnsFlag();
        Map<Integer, List<String>> selectMap = new HashMap<>();
        Collection<List<Object>> list = performanceAppraisalService.dataOrgSysList(performanceAppraisalId, performanceRankFactorDTOS);
        List<EmployeeDTO> employeeData = performanceAppraisalService.getEmployeeData();
//        if (selfDefinedColumnsFlag == 0) {
        List<List<String>> head = PerformanceAppraisalImportListener.headOrgSystemTemplate(selectMap, performanceRankFactorDTOS, employeeData, false);
//        } else {
//            List<PerformanceAppraisalColumnsDTO> appraisalColumnsDTOList = performanceAppraisalColumnsService.selectAppraisalColumnsByAppraisalId(performanceAppraisalId);
//            head = PerformanceAppraisalImportListener.headOrgCustom(selectMap, performanceRankFactorDTOS, appraisalColumnsDTOList);
//            list = performanceAppraisalService.dataOrgCustomList(performanceAppraisalId, performanceRankFactorDTOS);
//        }
        String fileName = URLEncoder.encode("组织绩效归档导出" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .inMemory(true)
                .useDefaultStyle(false)
                .head(head)
                .sheet("组织绩效归档导入")// 设置 sheet 的名字
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap, 1, 65533))
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
                .doWrite(list);
    }

    //==============================个人绩效管理==================================//
    //==============================个人绩效制定==================================//

    /**
     * 查询绩效考核表列表-个人-制定
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:perDevelop:pageList")
    @DataScope(businessAlias = "pa")
    @GetMapping("/perDevelop/pageList")
    public TableDataInfo listPerDevelop(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        return performanceAppraisalService.selectPerAppraisalDevelopList(performanceAppraisalObjectsDTO);
    }

    /**
     * 查询个人绩效考核表详情-制定
     */
    @RequiresPermissions(value = {"operate:cloud:performanceAppraisal:perDevelop:info", "operate:cloud:performanceAppraisal:perDevelop:edit"}, logical = Logical.OR)
    @GetMapping("/perDevelop/info/{performAppraisalObjectsId}")
    public AjaxResult infoPerDevelop(@PathVariable Long performAppraisalObjectsId) {
        return AjaxResult.success(performanceAppraisalService.selectPerAppraisalDevelopById(performAppraisalObjectsId));
    }

    /**
     * 编辑组织绩效考核制定表
     */
    @Log(title = "保存个人绩效指定", businessType = BusinessType.PERFORMANCE_APPRAISAL_PERSON_SETTING, businessId = "performAppraisalObjectsId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:performanceAppraisal:perDevelop:edit")
    @PostMapping("/perDevelop/edit")
    public AjaxResult editPerDevelop(@RequestBody @Validated(PerformanceAppraisalObjectsDTO.UpdatePerformanceAppraisalObjectsDTO.class) PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        return AjaxResult.success("保存成功", performanceAppraisalService.updatePerDevelopPerformanceAppraisal(performanceAppraisalObjectsDTO));
    }
    //==============================个人绩效评议==================================//

    /**
     * 查询绩效考核表列表-个人-评议
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:perReview:pageList")
    @DataScope(businessAlias = "pa")
    @GetMapping("/perReview/pageList")
    public TableDataInfo listPerReview(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        return performanceAppraisalService.selectPerAppraisalReviewList(performanceAppraisalObjectsDTO);
    }

    /**
     * 查询个人绩效考核表详情-评议
     */
    @RequiresPermissions(value = {"operate:cloud:performanceAppraisal:perReview:info", "operate:cloud:performanceAppraisal:perReview:edit"}, logical = Logical.OR)
    @GetMapping("/perReview/info/{performAppraisalObjectsId}")
    public AjaxResult infoPerReview(@PathVariable Long performAppraisalObjectsId) {
        return AjaxResult.success(performanceAppraisalService.selectPerAppraisalReviewById(performAppraisalObjectsId));
    }

    /**
     * 编辑个人绩效考核评议表
     */
    @Log(title = "保存个人绩效评议", businessType = BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW, businessId = "performAppraisalObjectsId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:performanceAppraisal:perReview:edit")
    @PostMapping("/perReview/edit")
    public AjaxResult editPerReview(@RequestBody @Validated(PerformanceAppraisalObjectsDTO.UpdatePerformanceAppraisalObjectsDTO.class) PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        return AjaxResult.success("保存成功", performanceAppraisalService.updatePerReviewPerformanceAppraisal(performanceAppraisalObjectsDTO));
    }

    //==============================个人绩效排名==================================//

    /**
     * 查询绩效考核表列表-组织-排名
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:perRanking:pageList")
    @DataScope(businessAlias = "pa")
    @GetMapping("/perRanking/pageList")
    public TableDataInfo listPerRanking(PerformanceAppraisalDTO performanceAppraisalDTO) {
        startPage();
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectPerAppraisalRankingList(performanceAppraisalDTO);
        return getDataTable(list);
    }

    /**
     * 查询绩效考核详情-人员-排名
     */
    @RequiresPermissions(value = {"operate:cloud:performanceAppraisal:perRanking:info", "operate:cloud:performanceAppraisal:perRanking:edit"}, logical = Logical.OR)
    @GetMapping("/perRanking/info/{performanceAppraisalId}")
    public AjaxResult infoPerRanking(@PathVariable Long performanceAppraisalId) {
        return AjaxResult.success(performanceAppraisalService.selectPerAppraisalRankingById(performanceAppraisalId));
    }

    /**
     * 编辑绩效考核-个人-排名
     */
    @Log(title = "保存个人绩效排名", businessType = BusinessType.PERFORMANCE_APPRAISAL_PERSON_RANKING, businessId = "performanceAppraisalId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:performanceAppraisal:perRanking:edit")
    @PostMapping("/perRanking/edit")
    public AjaxResult editPerRanking(@RequestBody @Validated(PerformanceAppraisalDTO.UpdatePerformanceAppraisalDTO.class) PerformanceAppraisalDTO performanceAppraisalDTO) {
        return toAjax(performanceAppraisalService.updatePerRankingPerformanceAppraisal(performanceAppraisalDTO));
    }

    //==============================个人绩效归档==================================//

    /**
     * 查询绩效考核表列表-个人-归档
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:perArchive:pageList")
    @DataScope(businessAlias = "pa")
    @GetMapping("/perArchive/pageList")
    public TableDataInfo listPerArchive(PerformanceAppraisalDTO performanceAppraisalDTO) {
        startPage();
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectPerAppraisalArchiveList(performanceAppraisalDTO);
        return getDataTable(list);
    }

    /**
     * 查询绩效考核详情-归档-个人
     */
    @RequiresPermissions(value = {"operate:cloud:performanceAppraisal:perArchive:info", "operate:cloud:performanceAppraisal:perArchive:edit"}, logical = Logical.OR)
    @GetMapping("/perArchive/info/{performanceAppraisalId}")
    public AjaxResult infoPerArchive(@PathVariable Long performanceAppraisalId) {
        return AjaxResult.success(performanceAppraisalService.selectPerAppraisalArchiveById(performanceAppraisalId));
    }

    /**
     * 导入个人绩效归档表
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:perArchive:import")
    @PostMapping("perImport")
    public AjaxResult importPerPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file) {
//        Integer importType = performanceAppraisalDTO.getImportType();
//        if (importType.equals(1)) {
        Map<Object, Object> objectObjectMap = performanceAppraisalService.importSysPerPerformanceAppraisal(performanceAppraisalDTO, file);
//        } else {
//            performanceAppraisalService.importCustomPerPerformanceAppraisal(performanceAppraisalDTO, file);
//        }
        return AjaxResult.successExcel(objectObjectMap, "导入成功");
    }

    /**
     * 导出组织模板表
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:performanceAppraisal:perArchive:import")
    @GetMapping("export-per-error")
    public void exportPerError(@RequestParam String errorExcelId, @RequestParam Long performanceAppraisalId, HttpServletResponse response) {
//        if (StringUtils.isNull(importType)) {
//            throw new ServiceException("请选择考核流程");
//        }
        List<List<Object>> errorList = redisService.getCacheObject(CacheConstants.ERROR_EXCEL_KEY + errorExcelId);
        if (StringUtils.isEmpty(errorList)) {
            throw new ServiceException("当前错误报告已过期");
        }
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalService.selectPerformanceRankFactor(performanceAppraisalId);
        List<EmployeeDTO> employeeData = performanceAppraisalService.getEmployeeData();
        Map<Integer, List<String>> selectMap = new HashMap<>();
        List<List<String>> head = PerformanceAppraisalImportListener.headPerSystemTemplate(selectMap, performanceRankFactorDTOS, employeeData, true);
        String fileName;
//        if (importType.equals(1)) {//系统流程

        fileName = URLEncoder.encode("组织绩效归档导入" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
//        } else {
//            head = PerformanceAppraisalImportListener.headOrgCustomTemplate(selectMap, performanceRankFactorDTOS);
//            fileName = URLEncoder.encode("组织绩效归档导入自定义模板" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
//        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        PerformanceAppraisalDTO appraisalDTO = performanceAppraisalService.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalService.selectOrgAppraisalObjectList(performanceAppraisalId);
        EasyExcel.write(response.getOutputStream())
                .inMemory(true)
                .useDefaultStyle(false)
                .head(head)
                .sheet("个人绩效归档导入错误报告")// 设置 sheet 的名字
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap, 1, 65533))
                .registerWriteHandler(new SheetWriteHandler() {
                    @Override
                    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                        for (int i = 0; i < 11; i++) {
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
                            cellData.setWriteCellStyle(writeCellStyle);
                            if (context.getColumnIndex() > 0 && context.getColumnIndex() < 6) {
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
                        if (context.getColumnIndex() == 0) {
                            headWriteFont.setColor(IndexedColors.RED.getIndex());
                        }
                        writeCellStyle.setWriteFont(headWriteFont);
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
                .doWrite(PerformanceAppraisalImportListener.dataTemplateList(performanceAppraisalObjectsDTOList, errorList, appraisalDTO));
    }

    /**
     * 导出个人模板表
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:performanceAppraisal:perArchive:import")
    @GetMapping("export-per-template")
    public void exportPerTemplate(@RequestParam(required = false) Integer importType, @RequestParam Long performanceAppraisalId, HttpServletResponse response) {
//        if (StringUtils.isNull(importType)) {
//            throw new ServiceException("请选择考核流程");
//        }
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalService.selectPerformanceRankFactor(performanceAppraisalId);
        Map<Integer, List<String>> selectMap = new HashMap<>();
        List<EmployeeDTO> employeeData = performanceAppraisalService.getEmployeeData();
//        if (importType.equals(1)) {//系统流程
        List<List<String>> head = PerformanceAppraisalImportListener.headPerSystemTemplate(selectMap, performanceRankFactorDTOS, employeeData, false);
        String fileName = URLEncoder.encode("个人绩效归档导入系统模板" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
//        } else {
//            head = PerformanceAppraisalImportListener.headPerCustomTemplate(selectMap, performanceRankFactorDTOS);
//            fileName = URLEncoder.encode("个人绩效归档导入自定义模板" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
//        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        PerformanceAppraisalDTO appraisalDTO = performanceAppraisalService.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalService.selectPerAppraisalObjectList(performanceAppraisalId);
        EasyExcel.write(response.getOutputStream())
                .inMemory(true)
                .useDefaultStyle(false)
                .head(head)
                .sheet("个人绩效归档导入")// 设置 sheet 的名字
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap, 1, 65533))
                .registerWriteHandler(new SheetWriteHandler() {
                    @Override
                    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                        for (int i = 0; i < 10; i++) {
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
                            if (context.getColumnIndex() == 6) {
                                headWriteFont.setColor(IndexedColors.RED.getIndex());
                            }
                            headWriteFont.setFontName("微软雅黑");
                            writeCellStyle.setWriteFont(headWriteFont);
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND
                            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            // 由于这里没有指定data format 最后展示的数据 格式可能会不太正确
                            // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                            // cell里面去 会导致自己设置的不一样（很关键）
                            cellData.setWriteCellStyle(writeCellStyle);
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
                            cellData.setWriteCellStyle(writeCellStyle);
                            if (context.getColumnIndex() < 5) {
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
                .doWrite(PerformanceAppraisalImportListener.dataTemplateList(performanceAppraisalObjectsDTOList, null, appraisalDTO));
    }

    /**
     * 导出个人模板表
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:performanceAppraisal:perArchive:info")
    @GetMapping("exportPer")
    public void exportPer(@RequestParam Long performanceAppraisalId, HttpServletResponse response) {
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalService.selectPerformanceRankFactor(performanceAppraisalId);
        List<EmployeeDTO> employeeData = performanceAppraisalService.getEmployeeData();
//        Integer selfDefinedColumnsFlag = performanceRankFactorDTOS.get(0).getSelfDefinedColumnsFlag();
        Map<Integer, List<String>> selectMap = new HashMap<>();
        List<List<String>> head;
        Collection<List<Object>> list = performanceAppraisalService.dataPerSysList(performanceAppraisalId, performanceRankFactorDTOS);
//        if (selfDefinedColumnsFlag == 0) {
        head = PerformanceAppraisalImportListener.headPerSystemTemplate(selectMap, performanceRankFactorDTOS, employeeData, false);
//        } else {
//            List<PerformanceAppraisalColumnsDTO> appraisalColumnsDTOList = performanceAppraisalColumnsService.selectAppraisalColumnsByAppraisalId(performanceAppraisalId);
//            head = PerformanceAppraisalImportListener.headPerCustom(selectMap, performanceRankFactorDTOS, appraisalColumnsDTOList);
//            lists = performanceAppraisalService.dataPerCustomList(performanceAppraisalId, performanceRankFactorDTOS);
//        }
        String fileName = URLEncoder.encode("个人绩效归档导出" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .inMemory(true)
                .useDefaultStyle(false)
                .head(head)
                .sheet("个人绩效归档导出")// 设置 sheet 的名字
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap, 1, 65533))
                .registerWriteHandler(new SheetWriteHandler() {
                    @Override
                    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                        for (int i = 0; i < 10; i++) {
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
                            if (context.getColumnIndex() == 6) {
                                headWriteFont.setColor(IndexedColors.RED.getIndex());
                            }
                            headWriteFont.setFontName("微软雅黑");
                            writeCellStyle.setWriteFont(headWriteFont);
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND
                            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            // 由于这里没有指定data format 最后展示的数据 格式可能会不太正确
                            // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                            // cell里面去 会导致自己设置的不一样（很关键）
                            cellData.setWriteCellStyle(writeCellStyle);
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
                            cellData.setWriteCellStyle(writeCellStyle);
                            if (context.getColumnIndex() < 5) {
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
                .doWrite(list);
    }

    //==============================组织绩效管理-个人绩效管理-归档、撤回、同步数据==================================//

    /**
     * 归档
     */
    @RequiresPermissions(value = {"operate:cloud:performanceAppraisal:archive:org", "operate:cloud:performanceAppraisal:archive:per"}, logical = Logical.OR)
    @GetMapping("/archive/{performanceAppraisalId}")
    public AjaxResult archive(@PathVariable Long performanceAppraisalId) {
        return toAjax(performanceAppraisalService.archive(performanceAppraisalId));
    }

    /**
     * 撤回组织、个人绩效考核评议
     */
    @RequiresPermissions(value = {"operate:cloud:performanceAppraisal:orgReview:withdraw", "operate:cloud:performanceAppraisal:perReview:withdraw"}, logical = Logical.OR)
    @GetMapping("/withdraw/{performAppraisalObjectsId}")
    public AjaxResult withdraw(@PathVariable Long performAppraisalObjectsId) {
        return AjaxResult.success("撤回成功", performanceAppraisalService.withdraw(performAppraisalObjectsId));
    }

    /**
     * 同步数据-制定
     */
    @RequiresPermissions(value = {"operate:cloud:performanceAppraisal:perDevelop:edit", "operate:cloud:performanceAppraisal:orgDevelop:edit"}, logical = Logical.OR)
    @GetMapping("/migrationDevelopData")
    public AjaxResult migrationDevelopData(@RequestParam("performAppraisalObjectsId") Long performAppraisalObjectsId, @RequestParam("appraisalObject") Integer appraisalObject) {
        return AjaxResult.success("同步成功", performanceAppraisalService.migrationDevelopData(performAppraisalObjectsId, appraisalObject));
    }

    /**
     * 同步数据-评议
     */
    @RequiresPermissions(value = {"operate:cloud:performanceAppraisal:perDevelop:edit", "operate:cloud:performanceAppraisal:perDevelop:edit"}, logical = Logical.OR)
    @GetMapping("/migrationReviewData")
    public AjaxResult migrationReviewData(@RequestParam("performAppraisalObjectsId") Long performAppraisalObjectsId, @RequestParam("appraisalObject") Integer appraisalObject) {
        return AjaxResult.success("同步成功", performanceAppraisalService.migrationReviewData(performAppraisalObjectsId, appraisalObject));
    }

    //==============================其他==================================//

    /**
     * 查询绩效考核表列表
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @GetMapping("/list")
    public AjaxResult list(PerformanceAppraisalDTO performanceAppraisalDTO) {
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectPerformanceAppraisalList(performanceAppraisalDTO);
        return AjaxResult.success(list);
    }

    /**
     * 根据绩效考核ID查找比例
     *
     * @param performanceAppraisalId 绩效考核id
     * @return AjaxResult
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @GetMapping("/list/performancePercentage/{performanceAppraisalId}")
    public AjaxResult listPercentage(@PathVariable Long performanceAppraisalId) {
        return AjaxResult.success(performanceAppraisalService.selectPerformancePercentageByPerformanceAppraisalId(performanceAppraisalId));
    }

    /**
     * 查询组织绩效结果排名
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @PostMapping("/orgProportion")
    public AjaxResult orgProportion(@RequestBody PerformanceAppraisalDTO performanceAppraisalDTO) {
        List<Long> appraisalObjectsIds = performanceAppraisalDTO.getAppraisalObjectsIds();
        Integer queryType = performanceAppraisalDTO.getQueryType();
        Long performanceAppraisalId = performanceAppraisalDTO.getPerformanceAppraisalId();
        return AjaxResult.success(performanceAppraisalService.selectOrgAppraisalRankByDTO(appraisalObjectsIds, performanceAppraisalId, queryType));
    }

    /**
     * 查询个人绩效结果排名
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @PostMapping("/perProportion")
    public AjaxResult perProportion(@RequestBody Map<String, List<Long>> performanceAppraisalDTO) {
        return AjaxResult.success(performanceAppraisalService.selectPerAppraisalRankByDTO(performanceAppraisalDTO));
    }


}
