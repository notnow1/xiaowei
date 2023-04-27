package net.qixiaowei.system.manage.controller.basic;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.DataFormatData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.excel.ExcelUtils;
import net.qixiaowei.integration.common.utils.excel.SelectSheetWriteHandler;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.excel.basic.EmployeeExcel;
import net.qixiaowei.system.manage.excel.basic.EmployeeImportListener;
import net.qixiaowei.system.manage.service.basic.IDepartmentService;
import net.qixiaowei.system.manage.service.basic.IEmployeeService;
import net.qixiaowei.system.manage.service.basic.IPostService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author TANGMICHI
 * @since 2022-09-30
 */
@RestController
@RequestMapping("employee")
public class EmployeeController extends BaseController {


    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IPostService postService;
    @Autowired
    private RedisService redisService;


    /**
     * 分页查询员工表列表
     */
    @RequiresPermissions("system:manage:employee:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmployeeDTO employeeDTO) {
        startPage();
        List<EmployeeDTO> list = employeeService.selectEmployeeList(employeeDTO);
        return getDataTable(list);
    }

    /**
     * 生成员工编码
     *
     * @return 员工编码
     */
    @RequiresPermissions(value = {"system:manage:employee:add", "system:manage:employee:edit"}, logical = Logical.OR)
    @GetMapping("/generate/employeeCode")
    public AjaxResult generateEmployeeCode() {
        return AjaxResult.success("操作成功", employeeService.generateEmployeeCode());
    }

    /**
     * 新增员工表
     */
    @Log(title = "新增员工", businessType = BusinessType.EMPLOYEE, businessId = "employeeId", operationType = OperationType.INSERT)
    @RequiresPermissions("system:manage:employee:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(EmployeeDTO.AddEmployeeDTO.class) EmployeeDTO employeeDTO) {
        return AjaxResult.success(employeeService.insertEmployee(employeeDTO));
    }

    /**
     * 修改员工表
     */
    @Log(title = "编辑员工", businessType = BusinessType.EMPLOYEE, businessId = "employeeId", operationType = OperationType.UPDATE)
    @RequiresPermissions("system:manage:employee:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(EmployeeDTO.UpdateEmployeeDTO.class) EmployeeDTO employeeDTO) {
        return toAjax(employeeService.updateEmployee(employeeDTO));
    }

    /**
     * 查询员工单条信息
     */
    @RequiresPermissions(value = {"system:manage:employee:info", "system:manage:employee:edit"}, logical = Logical.OR)
    @GetMapping("/info/{employeeId}")
    public AjaxResult info(@PathVariable Long employeeId) {
        EmployeeDTO employeeDTO = employeeService.selectEmployeeByEmployeeId(employeeId);
        return AjaxResult.success(employeeDTO);
    }

    /**
     * 逻辑删除员工表
     */
    @RequiresPermissions("system:manage:employee:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(EmployeeDTO.DeleteEmployeeDTO.class) EmployeeDTO employeeDTO) {
        return toAjax(employeeService.logicDeleteEmployeeByEmployeeId(employeeDTO));
    }

    /**
     * 逻辑批量删除员工表
     */
    @RequiresPermissions("system:manage:employee:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> employeeIds) {
        return toAjax(employeeService.logicDeleteEmployeeByEmployeeIds(employeeIds));
    }

    /**
     * 导入人员
     */
    @SneakyThrows
    @RequiresPermissions("system:manage:employee:import")
    @PostMapping("import")
    public AjaxResult importEmployee(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        if (StringUtils.isBlank(fileName)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(fileName, ".xls") && !StringUtils.endsWithIgnoreCase(fileName, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }

        List<EmployeeExcel> list = new ArrayList<>();
        List<Map<Integer, String>> listMap = new ArrayList<>();
        try {
            String sheetName = EasyExcel.read(file.getInputStream()).build().excelExecutor().sheetList().get(0).getSheetName();
            if (StringUtils.equals("人员信息配置", sheetName)) {
                //构建读取器
                ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
                listMap = read.sheet("人员信息配置").doReadSync();
                if (StringUtils.isNotEmpty(listMap)) {
                    if (listMap.get(0).size() != 24) {
                        throw new ServerException("导入模板被修改，请重新下载模板进行导入!");
                    }
                }
                ExcelUtils.mapToListModel(1, 0, listMap, new EmployeeExcel(), list, true);
            } else if (StringUtils.equals("人员信息错误报告", sheetName)) {
                //构建读取器
                ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
                listMap = read.sheet("人员信息错误报告").doReadSync();
                if (StringUtils.isNotEmpty(listMap)) {
                    if (listMap.get(0).size() == 25 && !listMap.get(0).get(0).equals("错误信息")) {
                        throw new ServerException("导入模板被修改，请重新下载模板进行导入!");
                    }
                }
                ExcelUtils.mapToListModel(1, 0, listMap, new EmployeeExcel(), list, false);
            } else {
                throw new ServerException("模板sheet名称不正确！");
            }
        } catch (IOException e) {
            throw new ServerException("模板sheet名称不正确！");
        }
        if (listMap.size() > 10000) {
            throw new ServerException("数据超过1万条,请减少数据后，重新上传");
        }

        return AjaxResult.successExcel(employeeService.importEmployee(list), null);
    }

    /**
     * 导出人员
     */
    @SneakyThrows
    @RequiresPermissions("system:manage:employee:export")
    @GetMapping("export")
    public void exportEmployee(@RequestParam Map<String, Object> employee, EmployeeDTO employeeDTO, HttpServletResponse response) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setStatus(1);
        //部门名称集合
        List<String> parentDepartmentExcelNames = departmentService.selectDepartmentExcelListName(departmentDTO);
        //岗位名称
        List<String> postNames = new ArrayList<>();
        PostDTO postDTO = new PostDTO();
        postDTO.setStatus(1);
        //查询岗位信息
        List<PostDTO> postDTOS = postService.selectPostList(postDTO);
        if (StringUtils.isNotEmpty(postDTOS)) {
            postNames = postDTOS.stream().map(PostDTO::getPostName).collect(Collectors.toList());
        }

        //自定义表头
        List<List<String>> head = EmployeeImportListener.exportHead(parentDepartmentExcelNames, postNames);
        List<EmployeeExcel> employeeExcelList = employeeService.exportEmployee(employeeDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("人员信息配置" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .inMemory(true)
                .useDefaultStyle(false)
                .excelType(ExcelTypeEnum.XLSX)
                .head(head)
                .sheet("人员信息配置")// 设置 sheet 的名字
                .registerWriteHandler(new SheetWriteHandler() {
                    @Override
                    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                        for (int i = 0; i < 100; i++) {
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
                //设置文本
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
                        if (context.getRowIndex() == 0) {
                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            //加粗
                            headWriteFont.setBold(true);
                            headWriteFont.setFontName("微软雅黑");
                            writeCellStyle.setWriteFont(headWriteFont);
                            //靠左
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

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
                            // 由于这里没有指定dataformat 最后展示的数据 格式可能会不太正确
                            // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                            // cell里面去 会导致自己设置的不一样（很关键）
                            cellData.setOriginCellStyle(xssfCellColorStyle);
                            cell.setCellStyle(cellStyle);
                        } else {
                            //居左
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            headWriteFont.setFontName("微软雅黑");
                            writeCellStyle.setWriteFont(headWriteFont);
                        }
                        cellData.setWriteCellStyle(writeCellStyle);
                    }
                })
                .registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                    @Override
                    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
                        Sheet sheet = writeSheetHolder.getSheet();
                        int columnIndex = cell.getColumnIndex();
                        // 列宽16
                        sheet.setColumnWidth(columnIndex, (270 * 16));
                        if (columnIndex == 4) {
                            sheet.setColumnWidth(columnIndex, (270 * 22));
                        }
                        // 行高7
                        sheet.setDefaultRowHeight((short) (20 * 15));
                    }
                })
                .doWrite(EmployeeImportListener.dataList(employeeExcelList, null));
    }

    /**
     * 导出模板
     */
    @SneakyThrows
    @RequiresPermissions("system:manage:employee:import")
    @GetMapping("/export-template")
    public void exportEmployeeTemplate(HttpServletResponse response, @RequestParam(required = false) String errorExcelId) {
        String sheetName = null;
        List<EmployeeExcel> employeeExcelList = new ArrayList<>();
        if (StringUtils.isNotBlank(errorExcelId)) {
            sheetName = "人员信息错误报告";
            try {
                employeeExcelList = redisService.getCacheObject(errorExcelId);
            } catch (Exception e) {
                throw new ServiceException("转化错误数据异常 请联系管理员");
            }
        } else {
            sheetName = "人员信息配置";

        }
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setStatus(1);
        //部门名称集合
        List<String> parentDepartmentExcelNames = departmentService.selectDepartmentExcelListName(departmentDTO);
        //岗位名称
        List<String> postNames = new ArrayList<>();
        PostDTO postDTO = new PostDTO();
        //查询岗位信息
        List<PostDTO> postDTOS = postService.selectPostList(postDTO);
        if (StringUtils.isNotEmpty(postDTOS)) {
            postNames = postDTOS.stream().map(PostDTO::getPostName).collect(Collectors.toList());
        }
        Map<Integer, List<String>> selectMap = new HashMap<>();
        //自定义表头
        List<List<String>> head = EmployeeImportListener.importHead(selectMap, parentDepartmentExcelNames, postNames, errorExcelId);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("人员信息配置" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");


        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .inMemory(true)
                .useDefaultStyle(false)
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap, 2, 65533))
                .head(head)
                .sheet(sheetName)// 设置 sheet 的名字
                .registerWriteHandler(new SheetWriteHandler() {
                    @Override
                    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                        for (int i = 0; i < 24; i++) {
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
                //设置文本
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

                        if (cell.getRowIndex() == 0) {
                            Sheet sheet = writeSheetHolder.getSheet();
                            Workbook workbook = sheet.getWorkbook();
                            // xlsx格式，如果是老版本格式的话就用 HSSFRichTextString
                            XSSFRichTextString richString = new XSSFRichTextString(cell.getStringCellValue());
                            Font font1 = workbook.createFont();
                            font1.setFontName("微软雅黑");
                            font1.setFontHeightInPoints((short) 11);
                            font1.setColor(IndexedColors.RED.getIndex());
                            font1.setBold(true);
                            richString.applyFont(0, 3, font1);
                            Font font2 = workbook.createFont();
                            font2.setFontName("微软雅黑");
                            font2.setFontHeightInPoints((short) 11);
                            font2.setColor(IndexedColors.BLACK.getIndex());
                            // 从哪到哪，你想设置成什么样的字体都行startIndex，endIndex
                            richString.applyFont(3, 99, font2);
                            // 再设置回每个单元格里
                            cell.setCellValue(richString);
                        }
                    }
                })
                //设置文本
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        Cell cell = context.getCell();
                        int rowIndex = cell.getRowIndex();
                        int columnIndex = cell.getColumnIndex();
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        //设置文本
                        DataFormatData dataFormatData = new DataFormatData();
                        dataFormatData.setIndex((short) 49);
                        writeCellStyle.setDataFormatData(dataFormatData);
                        // 设置字体
                        WriteFont headWriteFont = new WriteFont();
                        if (rowIndex == 0) {
                            //headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            //headWriteFont.setFontHeightInPoints((short) 11);
                            //加粗
                            //headWriteFont.setBold(true);
                            //headWriteFont.setFontName("微软雅黑");
                            //writeCellStyle.setWriteFont(headWriteFont);
                            //靠左
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            //设置 自动换行
                            writeCellStyle.setWrapped(true);
                            ;
                        } else {
                            //设置边框
                            writeCellStyle.setBorderLeft(BorderStyle.THIN);
                            writeCellStyle.setBorderTop(BorderStyle.THIN);
                            writeCellStyle.setBorderRight(BorderStyle.THIN);
                            writeCellStyle.setBorderBottom(BorderStyle.THIN);
                            //居左
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            if (StringUtils.isNotBlank(errorExcelId)) {
                                if (context.getRowIndex() == 1) {
                                    if (columnIndex == 0 || columnIndex == 1 || columnIndex == 2 || columnIndex == 3 || columnIndex == 5 || columnIndex == 12 || columnIndex == 14 || columnIndex == 15 || columnIndex == 16 || columnIndex == 18 || columnIndex == 24) {
                                        //设置 自动换行
                                        writeCellStyle.setWrapped(true);
                                        headWriteFont.setColor(IndexedColors.RED.getIndex());
                                        headWriteFont.setFontHeightInPoints((short) 11);
                                        headWriteFont.setFontName("微软雅黑");
                                        writeCellStyle.setWriteFont(headWriteFont);
                                    } else {
                                        //设置 自动换行
                                        writeCellStyle.setWrapped(true);
                                        headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                        headWriteFont.setFontHeightInPoints((short) 11);
                                        headWriteFont.setFontName("微软雅黑");
                                        writeCellStyle.setWriteFont(headWriteFont);
                                    }
                                } else if (context.getRowIndex() > 1) {
                                    if (columnIndex == 0) {
                                        //设置 自动换行
                                        writeCellStyle.setWrapped(true);
                                        headWriteFont.setColor(IndexedColors.RED.getIndex());
                                        headWriteFont.setFontHeightInPoints((short) 11);
                                        headWriteFont.setFontName("微软雅黑");
                                        writeCellStyle.setWriteFont(headWriteFont);
                                    } else {
                                        //设置 自动换行
                                        writeCellStyle.setWrapped(true);
                                        headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                        headWriteFont.setFontHeightInPoints((short) 11);
                                        headWriteFont.setFontName("微软雅黑");
                                        writeCellStyle.setWriteFont(headWriteFont);
                                    }
                                }
                            } else {
                                if (columnIndex == 0 || columnIndex == 1 || columnIndex == 2 || columnIndex == 4 || columnIndex == 11 || columnIndex == 13 || columnIndex == 14 || columnIndex == 15 || columnIndex == 17 || columnIndex == 23) {
                                    //设置 自动换行
                                    writeCellStyle.setWrapped(true);
                                    headWriteFont.setColor(IndexedColors.RED.getIndex());
                                    headWriteFont.setFontHeightInPoints((short) 11);
                                    headWriteFont.setFontName("微软雅黑");
                                    writeCellStyle.setWriteFont(headWriteFont);
                                } else {
                                    //设置 自动换行
                                    writeCellStyle.setWrapped(true);
                                    headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                    headWriteFont.setFontHeightInPoints((short) 11);
                                    headWriteFont.setFontName("微软雅黑");
                                    writeCellStyle.setWriteFont(headWriteFont);
                                }
                            }

                        }
                        cellData.setWriteCellStyle(writeCellStyle);
                    }
                })
                .registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                    @Override
                    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
                        Sheet sheet = writeSheetHolder.getSheet();
                        int columnIndex = cell.getColumnIndex();
                        int rowIndex = cell.getRowIndex();
                        Row row = cell.getRow();
                        if (rowIndex == 0) {
                            // 行高100
                            row.setHeight((short) (20 * 100));
                        } else {
                            // 行高16
                            row.setHeight((short) (20 * 16));
                        }
                        // 列宽16
                        sheet.setColumnWidth(columnIndex, (270 * 16));
                        if (StringUtils.isNotBlank(errorExcelId)) {
                            if (columnIndex == 5) {
                                sheet.setColumnWidth(columnIndex, (270 * 22));
                            }
                        } else {
                            if (columnIndex == 4) {
                                sheet.setColumnWidth(columnIndex, (270 * 22));
                            }
                        }
                    }
                })
                .doWrite(EmployeeImportListener.dataTemplateList(errorExcelId, employeeExcelList));
    }


    //==============================岗位薪酬报表==================================//

    /**
     * 分页查询岗位薪酬报表
     */
    @RequiresPermissions("system:manage:employee:pagePostSalaryReportList")
    @GetMapping("/pagePostSalaryReportList")
    public TableDataInfo pagePostSalaryReportList(EmployeeDTO employeeDTO) {
        startPage();
        List<EmployeeDTO> list = employeeService.pagePostSalaryReportList(employeeDTO);
        return getDataTable(list);
    }

    //==============================其他==================================//

    /**
     * 查询员工表列表(下拉框)
     */
    @GetMapping("/list")
    public AjaxResult list(EmployeeDTO employeeDTO) {
        return AjaxResult.success(employeeService.selectDropEmployeeList(employeeDTO));
    }

    /**
     * 根据部门id查询员工表列表
     */
    @GetMapping("/queryEmployeeByDept")
    public AjaxResult queryEmployeeByDept(EmployeeDTO employeeDTO) {
        List<EmployeeDTO> list = employeeService.queryEmployeeByDept(employeeDTO);
        return AjaxResult.success(list);
    }

    /**
     * 新增人力预算上年期末数集合预制数据
     */
    @PostMapping("/amountLastYear")
    public AjaxResult selecTamountLastYearList(@RequestBody EmployeeDTO employeeDTO) {
        return AjaxResult.success(employeeService.selecTamountLastYearList(employeeDTO));
    }

    /**
     * 根据部门id查询员工表列表
     */
    @GetMapping("/empSalaryAdjustPlanById/{employeeId}")
    public AjaxResult empSalaryAdjustPlanById(@PathVariable Long employeeId) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employeeId);
        return AjaxResult.success(employeeService.empSalaryAdjustPlan(employeeDTO));
    }

    /**
     * 查询有账号的员工
     */
    @RequiresPermissions("system:manage:employee:getUseEmployeeUser")
    @GetMapping("/getUseEmployeeUser")
    public AjaxResult getUseEmployeeUser() {
        return AjaxResult.success(employeeService.getUseEmployeeUser());
    }

}
