package net.qixiaowei.operate.cloud.controller.employee;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDetailsDTO;
import net.qixiaowei.operate.cloud.excel.employee.EmployeeBudgetDetailsExcel;
import net.qixiaowei.operate.cloud.excel.employee.EmployeeBudgetExcel;
import net.qixiaowei.operate.cloud.excel.employee.EmployeeBudgetImportListener;
import net.qixiaowei.operate.cloud.service.employee.IEmployeeBudgetService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author TANGMICHI
 * @since 2022-11-18
 */
@RestController
@RequestMapping("employeeBudget")
public class EmployeeBudgetController extends BaseController {


    @Autowired
    private IEmployeeBudgetService employeeBudgetService;

    //==============================人力预算调控==================================//

    /**
     * 分页查询人力预算表列表
     */
    @RequiresPermissions("operate:cloud:employeeBudget:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmployeeBudgetDTO employeeBudgetDTO) {
        startPage();
        List<EmployeeBudgetDTO> list = employeeBudgetService.selectEmployeeBudgetList(employeeBudgetDTO);
        return getDataTable(list);
    }

    /**
     * 新增人力预算表
     */
    @Log(title = "新增人力预算调控", businessType = BusinessType.EMPLOYEE_BUDGET, businessId = "employeeBudgetId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:employeeBudget:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(EmployeeBudgetDTO.AddEmployeeBudgetDTO.class) EmployeeBudgetDTO employeeBudgetDTO) {
        return AjaxResult.success(employeeBudgetService.insertEmployeeBudget(employeeBudgetDTO));
    }

    /**
     * 修改人力预算表
     */
    @Log(title = "保存人力预算调控", businessType = BusinessType.EMPLOYEE_BUDGET, businessId = "employeeBudgetId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:employeeBudget:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(EmployeeBudgetDTO.UpdateEmployeeBudgetDTO.class) EmployeeBudgetDTO employeeBudgetDTO) {
        return toAjax(employeeBudgetService.updateEmployeeBudget(employeeBudgetDTO));
    }

    /**
     * 查询人力预算表详情
     */
    @RequiresPermissions(value = {"operate:cloud:employeeBudget:info", "operate:cloud:employeeBudget:edit"}, logical = Logical.OR)
    @GetMapping("/info/{employeeBudgetId}")
    public AjaxResult info(@PathVariable Long employeeBudgetId) {
        EmployeeBudgetDTO employeeBudgetDTO = employeeBudgetService.selectEmployeeBudgetByEmployeeBudgetId(employeeBudgetId);
        return AjaxResult.success(employeeBudgetDTO);
    }

    /**
     * 逻辑删除人力预算表
     */
    @RequiresPermissions("operate:cloud:employeeBudget:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody EmployeeBudgetDTO employeeBudgetDTO) {
        return toAjax(employeeBudgetService.logicDeleteEmployeeBudgetByEmployeeBudgetId(employeeBudgetDTO));
    }

    /**
     * 逻辑批量删除人力预算表
     */
    @RequiresPermissions("operate:cloud:employeeBudget:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> employeeBudgetIds) {
        return toAjax(employeeBudgetService.logicDeleteEmployeeBudgetByEmployeeBudgetIds(employeeBudgetIds));
    }

    //==============================增人/减人工资包==================================//

    /**
     * 查询增人/减人工资包列表
     */
    @RequiresPermissions("operate:cloud:employeeBudget:salaryPackageList")
    @GetMapping("/salaryPackageList")
    public AjaxResult salaryPackageList(EmployeeBudgetDTO employeeBudgetDTO) {
        List<EmployeeBudgetDetailsDTO> list = employeeBudgetService.salaryPackageList(employeeBudgetDTO);
        return AjaxResult.success(list);
    }

    /**
     * 导出增人/减人工资包列表
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:employeeBudget:export:details")
    @PostMapping("/export/details")
    public void export(@RequestBody EmployeeBudgetDTO employeeBudgetDTO, HttpServletResponse response) {
        List<EmployeeBudgetDetailsExcel> employeeBudgetDetailsExcelList = employeeBudgetService.export(employeeBudgetDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("增人减人工资包列表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), EmployeeBudgetDetailsExcel.class)
                .inMemory(true)
                .useDefaultStyle(false)
                .sheet("增人减人工资包列表")
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        Cell cell = context.getCell();
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        //靠左
                        writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                        if (context.getRowIndex() == 0) {
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
                            cellData.setWriteCellStyle(writeCellStyle);
                            cellData.setOriginCellStyle(xssfCellColorStyle);
                            cell.setCellStyle(cellStyle);
                        }
                        if (context.getRowIndex() > 0 && context.getColumnIndex() > 2) {
                            //靠右边
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                        }
                    }
                })
                .doWrite(employeeBudgetDetailsExcelList);
    }


    //==============================其他==================================//

    /**
     * 查询人力预算表列表
     */
    @GetMapping("/list")
    public AjaxResult list(EmployeeBudgetDTO employeeBudgetDTO) {
        List<EmployeeBudgetDTO> list = employeeBudgetService.selectEmployeeBudgetList(employeeBudgetDTO);
        return AjaxResult.success(list);
    }

    /**
     * 导入人力预算表
     */
    @PostMapping("import")
    public AjaxResult importEmployeeBudget(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        InputStream inputStream;
        try {
            EmployeeBudgetImportListener importListener = new EmployeeBudgetImportListener(employeeBudgetService);
            inputStream = new BufferedInputStream(file.getInputStream());
            ExcelReaderBuilder builder = EasyExcel.read(inputStream, EmployeeBudgetExcel.class, importListener);
            builder.doReadAll();
        } catch (IOException e) {
            throw new ServiceException("导入人力预算表Excel失败");
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 导出人力预算表
     */
    @SneakyThrows
    @GetMapping("export")
    public void exportEmployeeBudget(@RequestParam Map<String, Object> employeeBudget, EmployeeBudgetDTO employeeBudgetDTO, HttpServletResponse response) {
        List<EmployeeBudgetExcel> employeeBudgetExcelList = employeeBudgetService.exportEmployeeBudget(employeeBudgetDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("人力预算表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), EmployeeBudgetExcel.class).sheet("人力预算表").doWrite(employeeBudgetExcelList);
    }
}
