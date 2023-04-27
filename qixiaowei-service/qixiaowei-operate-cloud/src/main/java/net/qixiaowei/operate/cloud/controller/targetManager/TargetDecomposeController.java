package net.qixiaowei.operate.cloud.controller.targetManager;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.DataFormatData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.excel.SelectSheetWriteHandler;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.DecomposeDetailCyclesDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeDetailsExcel;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeExcel;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeImportListener;
import net.qixiaowei.operate.cloud.service.product.IProductService;
import net.qixiaowei.operate.cloud.service.targetManager.IAreaService;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeService;
import net.qixiaowei.system.manage.api.domain.basic.Department;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import net.qixiaowei.system.manage.api.remote.system.RemoteRegionService;
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
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author TANGMICHI
 * @since 2022-10-27
 */
@RestController
@RequestMapping("targetDecompose")
public class TargetDecomposeController extends BaseController {


    @Autowired
    private ITargetDecomposeService targetDecomposeService;
    @Autowired
    private RemoteDepartmentService remoteDepartmentService;
    @Autowired
    private RemoteEmployeeService remoteEmployeeService;
    @Autowired
    private RemoteIndustryService remoteIndustryService;
    @Autowired
    private IProductService productService;
    @Autowired
    private IAreaService areaService;
    @Autowired
    private RemoteRegionService remoteRegionService;

    //==============================销售订单目标分解==================================//

    /**
     * 分页查询目标分解(销售订单)表列表
     */
    @RequiresPermissions("operate:cloud:targetDecompose:order:pageList")
    @GetMapping("/order/pageList")
    public TableDataInfo orderPageList(TargetDecomposeDTO targetDecomposeDTO) {
        startPage();
        List<TargetDecomposeDTO> list = targetDecomposeService.selectOrderList(targetDecomposeDTO);
        return getDataTable(list);
    }

    /**
     * 新增目标分解(销售订单)表
     */
    @Log(title = "保存目标分解", businessType = BusinessType.TARGET_DECOMPOSE_ORDER, businessId = "targetDecomposeId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:targetDecompose:order:add")
    @PostMapping("/order/add")
    public AjaxResult orderAddSave(@RequestBody @Validated({TargetDecomposeDTO.AddTargetDecomposeDTO.class}) TargetDecomposeDTO targetDecomposeDTO) {
        return AjaxResult.success(targetDecomposeService.insertOrderTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 修改目标分解(销售订单)表
     */
    @Log(title = "保存目标分解", businessType = BusinessType.TARGET_DECOMPOSE_ORDER, businessId = "targetDecomposeId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:targetDecompose:order:edit")
    @PostMapping("/order/edit")
    public AjaxResult orderEditSave(@RequestBody @Validated(TargetDecomposeDTO.UpdateTargetDecomposeDTO.class) TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.updateOrderTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 查询目标分解(销售订单)表详情
     */
    @RequiresPermissions(value = {"operate:cloud:targetDecompose:order:info", "operate:cloud:targetDecompose:order:edit"}, logical = Logical.OR)
    @GetMapping("/order/info/{targetDecomposeId}")
    public AjaxResult orderInfo(@PathVariable Long targetDecomposeId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectOrderTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        return AjaxResult.success(targetDecomposeDTO);
    }

    /**
     * 逻辑删除目标分解(销售订单)表
     */
    @RequiresPermissions("operate:cloud:targetDecompose:order:remove")
    @PostMapping("/order/remove")
    public AjaxResult orderRemove(@RequestBody @Validated(TargetDecomposeDTO.DeleteTargetDecomposeDTO.class) TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.logicDeleteOrderTargetDecomposeByTargetDecomposeId(targetDecomposeDTO));
    }

    /**
     * 逻辑批量删除目标分解(销售订单)表
     */
    @RequiresPermissions("operate:cloud:targetDecompose:order:remove")
    @PostMapping("/order/removes")
    public AjaxResult orderRemoves(@RequestBody List<Long> targetDecomposeIds) {
        return toAjax(targetDecomposeService.logicDeleteOrderTargetDecomposeByTargetDecomposeIds(targetDecomposeIds));
    }

    /**
     * 目标分解(销售订单)导出列表Excel
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetDecompose:order:export")
    @GetMapping("/order/export")
    public void exportOrderTargetDecompose(@RequestParam Map<String, Object> targetDecompose, TargetDecomposeDTO targetDecomposeDTO, HttpServletResponse response) {
        List<TargetDecomposeExcel> targetDecomposeExcelList = targetDecomposeService.exportOrderTargetDecompose(targetDecomposeDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("销售订单目标分解详情" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TargetDecomposeExcel.class)
                .excelType(ExcelTypeEnum.XLSX)
                .inMemory(true)
                .useDefaultStyle(false)
                .sheet("销售订单目标分解详情")
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        Cell cell = context.getCell();
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        //靠左
                        writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
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
                            // 由于这里没有指定dataformat 最后展示的数据 格式可能会不太正确
                            // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                            // cell里面去 会导致自己设置的不一样（很关键）
                            cellData.setWriteCellStyle(writeCellStyle);
                            cellData.setOriginCellStyle(xssfCellColorStyle);
                            cell.setCellStyle(cellStyle);
                        }
                        if (context.getRowIndex() > 1 && context.getColumnIndex() > 2) {
                            //靠右边
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                        }
                    }
                })
                .doWrite(targetDecomposeExcelList);
    }


    //==============================销售收入目标分解==================================//

    /**
     * 分页查询目标分解(销售收入)表列表
     */
    @RequiresPermissions("operate:cloud:targetDecompose:income:pageList")
    @GetMapping("/income/pageList")
    public TableDataInfo incomePageList(TargetDecomposeDTO targetDecomposeDTO) {
        startPage();
        List<TargetDecomposeDTO> list = targetDecomposeService.selectIncomeList(targetDecomposeDTO);
        return getDataTable(list);
    }

    /**
     * 新增目标分解(销售收入)表
     */
    @Log(title = "保存目标分解", businessType = BusinessType.TARGET_DECOMPOSE_INCOME, businessId = "targetDecomposeId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:targetDecompose:income:add")
    @PostMapping("/income/add")
    public AjaxResult incomeAddSave(@RequestBody @Validated(TargetDecomposeDTO.AddTargetDecomposeDTO.class) TargetDecomposeDTO targetDecomposeDTO) {
        return AjaxResult.success(targetDecomposeService.insertIncomeTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 修改目标分解(销售收入)表
     */
    @Log(title = "保存目标分解", businessType = BusinessType.TARGET_DECOMPOSE_INCOME, businessId = "targetDecomposeId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:targetDecompose:income:edit")
    @PostMapping("/income/edit")
    public AjaxResult incomeEditSave(@RequestBody @Validated(TargetDecomposeDTO.UpdateTargetDecomposeDTO.class) TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.updateIncomeTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 查询目标分解(销售收入)表详情
     */
    @RequiresPermissions(value = {"operate:cloud:targetDecompose:income:info", "operate:cloud:targetDecompose:income:edit"}, logical = Logical.OR)
    @GetMapping("/income/info/{targetDecomposeId}")
    public AjaxResult incomeInfo(@PathVariable Long targetDecomposeId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectIncomeTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        return AjaxResult.success(targetDecomposeDTO);
    }

    /**
     * 逻辑删除目标分解(销售收入)表
     */
    @RequiresPermissions("operate:cloud:targetDecompose:income:remove")
    @PostMapping("/income/remove")
    public AjaxResult incomeRemove(@RequestBody @Validated(TargetDecomposeDTO.DeleteTargetDecomposeDTO.class) TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.logicDeleteIncomeTargetDecomposeByTargetDecomposeId(targetDecomposeDTO));
    }

    /**
     * 逻辑批量删除目标分解(销售收入)表
     */
    @RequiresPermissions("operate:cloud:targetDecompose:income:remove")

    @PostMapping("/income/removes")
    public AjaxResult incomeRemoves(@RequestBody List<Long> targetDecomposeIds) {
        return toAjax(targetDecomposeService.logicDeleteIncomeTargetDecomposeByTargetDecomposeIds(targetDecomposeIds));
    }

    /**
     * 目标分解(销售收入)导出列表Excel
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetDecompose:income:export")
    @GetMapping("/income/export")
    public void exportIncomeTargetDecompose(@RequestParam Map<String, Object> targetDecompose, TargetDecomposeDTO targetDecomposeDTO, HttpServletResponse response) {
        List<TargetDecomposeExcel> targetDecomposeExcelList = targetDecomposeService.exportIncomeTargetDecompose(targetDecomposeDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("销售收入目标分解详情" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TargetDecomposeExcel.class)
                .excelType(ExcelTypeEnum.XLSX)
                .inMemory(true)
                .useDefaultStyle(false)
                .sheet("销售收入目标分解列表")
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        Cell cell = context.getCell();
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        //靠左
                        writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                        //设置 自动换行
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
                            // 由于这里没有指定dataformat 最后展示的数据 格式可能会不太正确
                            // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                            // cell里面去 会导致自己设置的不一样（很关键）
                            cellData.setWriteCellStyle(writeCellStyle);
                            cellData.setOriginCellStyle(xssfCellColorStyle);
                            cell.setCellStyle(cellStyle);
                        }
                        if (context.getRowIndex() > 1 && context.getColumnIndex() > 2) {
                            //靠右边
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                        }
                    }
                })
                .doWrite(targetDecomposeExcelList);
    }


    //==============================销售回款目标分解==================================//

    /**
     * 分页查询目标分解(销售回款)表列表
     */
    @RequiresPermissions("operate:cloud:targetDecompose:returned:pageList")
    @GetMapping("/returned/pageList")
    public TableDataInfo returnedPageList(TargetDecomposeDTO targetDecomposeDTO) {
        startPage();
        List<TargetDecomposeDTO> list = targetDecomposeService.selectReturnedList(targetDecomposeDTO);
        return getDataTable(list);
    }

    /**
     * 新增目标分解(销售回款)表
     */
    @Log(title = "保存目标分解", businessType = BusinessType.TARGET_DECOMPOSE_RECOVERY, businessId = "targetDecomposeId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:targetDecompose:returned:add")
    @PostMapping("/returned/add")
    public AjaxResult returnedAddSave(@RequestBody @Validated(TargetDecomposeDTO.AddTargetDecomposeDTO.class) TargetDecomposeDTO targetDecomposeDTO) {
        return AjaxResult.success(targetDecomposeService.insertReturnedTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 修改目标分解(销售回款)表
     */
    @Log(title = "保存目标分解", businessType = BusinessType.TARGET_DECOMPOSE_RECOVERY, businessId = "targetDecomposeId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:targetDecompose:returned:edit")
    @PostMapping("/returned/edit")
    public AjaxResult returnedEditSave(@RequestBody @Validated(TargetDecomposeDTO.UpdateTargetDecomposeDTO.class) TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.updateReturnedTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 查询目标分解(销售回款)表详情
     */
    @RequiresPermissions(value = {"operate:cloud:targetDecompose:returned:info", "operate:cloud:targetDecompose:returned:edit"}, logical = Logical.OR)
    @GetMapping("/returned/info/{targetDecomposeId}")
    public AjaxResult returnedInfo(@PathVariable Long targetDecomposeId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectReturnedTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        return AjaxResult.success(targetDecomposeDTO);
    }

    /**
     * 逻辑删除目标分解(销售回款)表
     */
    @RequiresPermissions("operate:cloud:targetDecompose:returned:remove")
    @PostMapping("/returned/remove")
    public AjaxResult returnedRemove(@RequestBody @Validated(TargetDecomposeDTO.DeleteTargetDecomposeDTO.class) TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.logicDeleteReturnedTargetDecomposeByTargetDecomposeId(targetDecomposeDTO));
    }

    /**
     * 逻辑批量删除目标分解(销售回款)表
     */
    @RequiresPermissions("operate:cloud:targetDecompose:returned:remove")

    @PostMapping("/returned/removes")
    public AjaxResult returnedRemoves(@RequestBody List<Long> targetDecomposeIds) {
        return toAjax(targetDecomposeService.logicDeleteReturnedTargetDecomposeByTargetDecomposeIds(targetDecomposeIds));
    }

    /**
     * 目标分解(销售回款)导出列表Excel
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetDecompose:returned:export")
    @GetMapping("/returned/export")
    public void exportReturnedTargetDecompose(@RequestParam Map<String, Object> targetDecompose, TargetDecomposeDTO targetDecomposeDTO, HttpServletResponse response) {
        List<TargetDecomposeExcel> targetDecomposeExcelList = targetDecomposeService.exportReturnedTargetDecompose(targetDecomposeDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("销售回款目标分解详情" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TargetDecomposeExcel.class)
                        .excelType(ExcelTypeEnum.XLSX)
                .inMemory(true)
                .useDefaultStyle(false)
                .sheet("回款金额（含税）目标分解导入")
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        Cell cell = context.getCell();
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        //靠左
                        writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
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
                            // 由于这里没有指定dataformat 最后展示的数据 格式可能会不太正确
                            // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                            // cell里面去 会导致自己设置的不一样（很关键）
                            cellData.setWriteCellStyle(writeCellStyle);
                            cellData.setOriginCellStyle(xssfCellColorStyle);
                            cell.setCellStyle(cellStyle);
                        }
                        if (context.getRowIndex() > 1 && context.getColumnIndex() > 2) {
                            //靠右边
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                        }
                    }
                })
                .doWrite(targetDecomposeExcelList);
    }

    //==============================自定义目标分解==================================//

    /**
     * 分页查询目标分解(自定义)表列表
     */
    @RequiresPermissions("operate:cloud:targetDecompose:custom:pageList")
    @GetMapping("/custom/pageList")
    public TableDataInfo customPageList(TargetDecomposeDTO targetDecomposeDTO) {
        startPage();
        List<TargetDecomposeDTO> list = targetDecomposeService.selectCustomList(targetDecomposeDTO);
        return getDataTable(list);
    }

    /**
     * 新增目标分解(自定义)表
     */
    @Log(title = "保存目标分解", businessType = BusinessType.TARGET_DECOMPOSE, businessId = "targetDecomposeId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:targetDecompose:custom:add")
    @PostMapping("/custom/add")
    public AjaxResult customAddSave(@RequestBody @Validated(TargetDecomposeDTO.AddTargetDecomposeDTO.class) TargetDecomposeDTO targetDecomposeDTO) {
        return AjaxResult.success(targetDecomposeService.insertCustomTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 修改目标分解(自定义)表
     */
    @Log(title = "保存目标分解", businessType = BusinessType.TARGET_DECOMPOSE, businessId = "targetDecomposeId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:targetDecompose:custom:edit")
    @PostMapping("/custom/edit")
    public AjaxResult customEditSave(@RequestBody @Validated(TargetDecomposeDTO.UpdateTargetDecomposeDTO.class) TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.updateCustomTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 查询目标分解(自定义)表详情
     */
    @RequiresPermissions(value = {"operate:cloud:targetDecompose:custom:info", "operate:cloud:targetDecompose:custom:edit"}, logical = Logical.OR)
    @GetMapping("/custom/info/{targetDecomposeId}")
    public AjaxResult customInfo(@PathVariable Long targetDecomposeId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectCustomTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        return AjaxResult.success(targetDecomposeDTO);
    }

    /**
     * 逻辑删除目标分解(自定义)表
     */
    @RequiresPermissions("operate:cloud:targetDecompose:custom:remove")
    @PostMapping("/custom/remove")
    public AjaxResult customRemove(@RequestBody @Validated(TargetDecomposeDTO.DeleteTargetDecomposeDTO.class) TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.logicDeleteCustomTargetDecomposeByTargetDecomposeId(targetDecomposeDTO));
    }

    /**
     * 逻辑批量删除目标分解(自定义)表
     */
    @RequiresPermissions("operate:cloud:targetDecompose:custom:remove")
    @PostMapping("/custom/removes")
    public AjaxResult customRemoves(@RequestBody List<Long> targetDecomposeIds) {
        return toAjax(targetDecomposeService.logicDeleteCustomTargetDecomposeByTargetDecomposeIds(targetDecomposeIds));
    }

    /**
     * 目标分解(自定义)导出列表Excel
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetDecompose:custom:export")
    @GetMapping("/custom/export")
    public void exportCustomTargetDecompose(@RequestParam Map<String, Object> targetDecompose, TargetDecomposeDTO targetDecomposeDTO, HttpServletResponse response) {
        //自定义表头
        List<List<String>> head = TargetDecomposeImportListener.head();
        List<TargetDecomposeExcel> targetDecomposeExcelList = targetDecomposeService.exportCustomTargetDecompose(targetDecomposeDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("自定义目标分解详情" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .head(head)// 设置表头
                .inMemory(true)
                .useDefaultStyle(false)
                .sheet("自定义目标分解详情")// 设置 sheet 的名字
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
                        // 设置字体
                        WriteFont headWriteFont = new WriteFont();
                        if (context.getRowIndex() < 2) {
                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
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

                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            headWriteFont.setFontName("微软雅黑");
                            writeCellStyle.setWriteFont(headWriteFont);
                            if (context.getColumnIndex() > 2) {
                                //靠右边
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                            } else {
                                //靠左
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            }
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            //设置边框
                            writeCellStyle.setBorderLeft(BorderStyle.THIN);
                            writeCellStyle.setBorderTop(BorderStyle.THIN);
                            writeCellStyle.setBorderRight(BorderStyle.THIN);
                            writeCellStyle.setBorderBottom(BorderStyle.THIN);

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
                        // 行高7
                        sheet.setDefaultRowHeight((short) (20 * 15));
                    }
                })
                .doWrite(TargetDecomposeImportListener.dataList(targetDecomposeExcelList));// 写入数据
    }

    //==============================经营结果分析报表==================================//

    /**
     * 查询经营结果分析报表列表
     */
    @RequiresPermissions("operate:cloud:targetSetting:analyse")
    @GetMapping("/result/list")
    public TableDataInfo resultList(TargetDecomposeDTO targetDecomposeDTO) {
        startPage();
        List<TargetDecomposeDTO> list = targetDecomposeService.resultList(targetDecomposeDTO);
        return getDataTable(list);
    }

    /**
     * 查询经营结果分析报表列表下拉框
     */
    @RequiresPermissions("operate:cloud:targetSetting:analyse")
    @GetMapping("/resultListDroBox")
    public AjaxResult resultListDroBox(TargetDecomposeDTO targetDecomposeDTO) {
        ArrayList<TargetDecomposeDTO> targetDecomposeList = new ArrayList<>();
        List<TargetDecomposeDTO> list = targetDecomposeService.rollPageList(targetDecomposeDTO);
        if (StringUtils.isNotEmpty(list)) {
            //根据属性去重
            targetDecomposeList = list.stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(
                                    TargetDecomposeDTO::getIndicatorId))), ArrayList::new));
        }
        return AjaxResult.success(targetDecomposeList);
    }

    //==============================滚动预测管理==================================//

    /**
     * 查询滚动预测表详情
     */
    @RequiresPermissions(value = {"operate:cloud:targetDecompose:roll:info", "operate:cloud:targetDecompose:roll:edit"}, logical = Logical.OR)
    @GetMapping("/roll/info/{targetDecomposeId}")
    public AjaxResult rollInfo(@PathVariable Long targetDecomposeId, @RequestParam(required = false) Long backlogId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectRollTargetDecomposeByTargetDecomposeId(targetDecomposeId, backlogId, false);
        return AjaxResult.success(targetDecomposeDTO);
    }

    /**
     * 分页查询滚动预测表列表
     */
    @RequiresPermissions("operate:cloud:targetDecompose:roll:pageList")
    @GetMapping("/roll/pageList")
    public TableDataInfo rollPageList(TargetDecomposeDTO targetDecomposeDTO) {
        startPage();
        List<TargetDecomposeDTO> list = targetDecomposeService.rollPageList(targetDecomposeDTO);
        return getDataTable(list);
    }

    /**
     * 修改滚动预测详情
     */
    @Log(title = "保存", businessType = BusinessType.TARGET_DECOMPOSE_ROLL, businessId = "targetDecomposeId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:targetDecompose:roll:edit")
    @PostMapping("/roll/edit")
    public AjaxResult rollEditSave(@RequestBody @Validated(TargetDecomposeDTO.UpdateTargetDecomposeDTO.class) TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.updateRollTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 移交预测负责人
     */
    @RequiresPermissions("operate:cloud:targetDecompose:roll:turnOver")
    @PostMapping("/turnOver/edit")
    public AjaxResult turnOverPrincipalEmployee(@RequestBody @Validated(TargetDecomposeDTO.RollUpdateTargetDecomposeDTO.class) TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.turnOverPrincipalEmployee(targetDecomposeDTO));
    }

    /**
     * 滚动预测导出详情Excel
     */
    @SneakyThrows
    @GetMapping("/exportRoll-details/info/{targetDecomposeId}")
    public void exportRollTargetDecomposeDetails(@PathVariable Long targetDecomposeId, @RequestParam(required = false) Long backlogId, HttpServletResponse response) {
        //查询详情
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectRollTargetDecomposeByTargetDecomposeId(targetDecomposeId, backlogId,true);
        if (StringUtils.isNull(targetDecomposeDTO)) {
            throw new ServiceException("数据不存在！ 请刷新重试！");
        }
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO.getTargetDecomposeDetailsDTOS();
        //自定义表头
        List<List<String>> head = TargetDecomposeImportListener.headRollDetails(targetDecomposeDTO);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("滚动预测详情" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .head(head)// 设置表头
                .inMemory(true)
                .useDefaultStyle(false)
                .sheet("滚动预测详情")// 设置 sheet 的名字
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
                        if (context.getRowIndex() < 5) {
                            if (context.getColumnIndex() < 2) {
                                headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
                                if (context.getRowIndex() == 0) {
                                    headWriteFont.setBold(true);
                                }//加粗
                                else if (context.getRowIndex() > 0 && context.getColumnIndex() == 0) {
                                    headWriteFont.setBold(true);
                                }

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
                            }
                        } else {

                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            if (context.getRowIndex() == 6 || context.getRowIndex() == 7 || context.getRowIndex() == 8) {
                                headWriteFont.setBold(true);
                            }
                            headWriteFont.setFontName("微软雅黑");
                            writeCellStyle.setWriteFont(headWriteFont);
                            if (context.getRowIndex() == 7) {
                                if (context.getColumnIndex() > 1) {
                                    //居中
                                    writeCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                                }
                            } else {
                                if (context.getRowIndex() > 8 && context.getColumnIndex() > targetDecomposeDTO.getFileNameList().size()) {
                                    //靠右
                                    writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                } else {
                                    //靠左
                                    writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                                }

                            }
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                            if (context.getRowIndex() > 6) {
                                //设置边框
                                writeCellStyle.setBorderLeft(BorderStyle.THIN);
                                writeCellStyle.setBorderTop(BorderStyle.THIN);
                                writeCellStyle.setBorderRight(BorderStyle.THIN);
                                writeCellStyle.setBorderBottom(BorderStyle.THIN);
                            }
                            if (context.getRowIndex() == 7 || context.getRowIndex() == 8) {
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
                        // 列宽16
                        sheet.setColumnWidth(columnIndex, (270 * 16));
                        // 行高20
                        sheet.setDefaultRowHeight((short) (20 * 15));

                    }
                })
                .doWrite(TargetDecomposeImportListener.detailsRollDataList(targetDecomposeDetailsDTOS, targetDecomposeDTO, true));
    }

    /**
     * 滚动预测导入 下载模板Excel
     */
    @SneakyThrows
    @GetMapping("/exportRoll-details-template/info/{targetDecomposeId}")
    public void exportRollTargetDecomposeDetailsTemplate(@PathVariable Long targetDecomposeId, @RequestParam(required = false) Long backlogId, HttpServletResponse response) {
        //查询详情
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectRollTargetDecomposeByTargetDecomposeId(targetDecomposeId, backlogId, true);
        if (StringUtils.isNull(targetDecomposeDTO)) {
            throw new ServiceException("数据不存在！ 请刷新重试！");
        }
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO.getTargetDecomposeDetailsDTOS();
        //自定义表头
        List<List<String>> head = TargetDecomposeImportListener.headRollDetailsTemplate(targetDecomposeDTO);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("滚动预测详情" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(head)// 设置表头
                .inMemory(true)
                .useDefaultStyle(false)
                .sheet("滚动预测详情")// 设置 sheet 的名字
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
                        if (context.getRowIndex() < 5) {
                            if (context.getColumnIndex() == 0) {
                                headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
                                if (context.getRowIndex() == 0) {
                                    headWriteFont.setBold(true);
                                }//加粗
                                else if (context.getRowIndex() > 0 && context.getColumnIndex() == 0) {
                                    headWriteFont.setBold(true);
                                }

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
                            }
                            if (context.getColumnIndex() == 1) {
                                headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
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
                                byte[] rgb = new byte[]{(byte) 217, (byte) 217, (byte) 217};
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
                            }
                        } else {

                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            if (context.getRowIndex() == 6 || context.getRowIndex() == 7 || context.getRowIndex() == 8) {
                                headWriteFont.setBold(true);
                            }
                            headWriteFont.setFontName("微软雅黑");
                            writeCellStyle.setWriteFont(headWriteFont);
                            if (context.getRowIndex() == 7) {
                                if (context.getColumnIndex() > 1) {
                                    //居中
                                    writeCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                                }
                            } else {
                                if (context.getRowIndex() > 8 && context.getColumnIndex() > targetDecomposeDTO.getFileNameList().size()) {
                                    //靠右
                                    writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                } else {
                                    //靠左
                                    writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                                }

                            }
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                            if (context.getRowIndex() > 6) {
                                //设置边框
                                writeCellStyle.setBorderLeft(BorderStyle.THIN);
                                writeCellStyle.setBorderTop(BorderStyle.THIN);
                                writeCellStyle.setBorderRight(BorderStyle.THIN);
                                writeCellStyle.setBorderBottom(BorderStyle.THIN);
                            }
                            if (context.getRowIndex() == 7 || context.getRowIndex() == 8) {
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
                            }
                            if ( context.getRowIndex() > 8 ) {
                                //时间维度:1年度;2半年度;3季度;4月度;5周
                                Integer timeDimension = targetDecomposeDTO.getTimeDimension();
                                //需要置灰列的集合
                                List<Integer> columnIndexList = new ArrayList<>();
                                List<Map<String, String>> fileNameList = targetDecomposeDTO.getFileNameList();
                                int size = fileNameList.size()+1;
                                 if (timeDimension ==2){
                                    columnIndexList.add(size+3);
                                }else if (timeDimension ==3){
                                    for (int i = 0; i < 3; i++) {
                                        size= size+3;
                                        columnIndexList.add(size);
                                    }

                                }else if (timeDimension ==4){
                                    for (int i = 0; i < 11; i++) {
                                        size= size+3;
                                        columnIndexList.add(size);
                                    }
                                }else if (timeDimension ==5){
                                    for (int i = 0; i < 51; i++) {
                                        size= size+3;
                                        columnIndexList.add(size);
                                    }
                                }
                              if (context.getColumnIndex() < (fileNameList.size()+2) || columnIndexList.contains(context.getColumnIndex())){
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
                                  // 由于这里没有指定dataformat 最后展示的数据 格式可能会不太正确
                                  // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                                  // cell里面去 会导致自己设置的不一样（很关键）
                                  cellData.setOriginCellStyle(xssfCellColorStyle);
                                  cell.setCellStyle(cellStyle);
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
                        // 列宽16
                        sheet.setColumnWidth(columnIndex, (270 * 16));
                        // 行高20
                        sheet.setDefaultRowHeight((short) (20 * 15));

                    }
                })
                .doWrite(TargetDecomposeImportListener.detailsRollDataList(targetDecomposeDetailsDTOS, targetDecomposeDTO, false));
    }

    /**
     * 导入解析滚动预测
     */
//    @RequiresPermissions("system:manage:employee:import")
    @PostMapping("import")
    public AjaxResult importProduct(Long targetDecomposeId, Long backlogId, MultipartFile file) throws IOException {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectRollTargetDecomposeByTargetDecomposeId(targetDecomposeId, backlogId, true);
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }

        List<DecomposeDetailCyclesDTO> list = new ArrayList<>();

        //构建读取器
        ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
        ExcelReaderSheetBuilder sheet = read.sheet(0);
        List<Map<Integer, String>> listMap = sheet.doReadSync();

        //导入解析滚动预测
        TargetDecomposeImportListener.mapToListModel(6, 0, listMap, list, targetDecomposeDTO);
        return AjaxResult.success(targetDecomposeService.importProduct(list, targetDecomposeDTO));
    }
    //==============================其他==================================//

    /**
     * 查询目标分解预制数据年份
     */
    @PostMapping("/getYear")
    public AjaxResult listOrder(@RequestBody TargetDecomposeDTO targetDecomposeDTO) {
        return AjaxResult.success(targetDecomposeService.selectMaxYear(targetDecomposeDTO));
    }

    /**
     * 查询经营结果分析报表详情
     */
    @GetMapping("/result/info/{targetDecomposeId}")
    public AjaxResult resultInfo(@PathVariable Long targetDecomposeId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectResultTargetDecomposeByTargetDecomposeId(targetDecomposeId,false);
        return AjaxResult.success(targetDecomposeDTO);
    }


    /**
     * 经营结果分析报表导出详情Excel
     */
    @SneakyThrows
    @GetMapping("/exportResult-details/info/{targetDecomposeId}")
    public void exportResultTargetDecomposeDetails(@PathVariable Long targetDecomposeId, HttpServletResponse response) {
        //查询详情
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectResultTargetDecomposeByTargetDecomposeId(targetDecomposeId,true);
        if (StringUtils.isNull(targetDecomposeDTO)) {
            throw new ServiceException("数据不存在！ 请刷新重试！");
        }
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO.getTargetDecomposeDetailsDTOS();
        //自定义表头
        List<List<String>> head = TargetDecomposeImportListener.headResultDetails(targetDecomposeDTO);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("经营结果分析报表详情" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .head(head)// 设置表头
                .inMemory(true)
                .useDefaultStyle(false)
                .sheet("经营结果分析报表详情")// 设置 sheet 的名字
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
                        if (context.getRowIndex() < 5) {
                            if (context.getColumnIndex() < 2) {
                                headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
                                if (context.getRowIndex() == 0) {
                                    headWriteFont.setBold(true);
                                }//加粗
                                else if (context.getRowIndex() > 0 && context.getColumnIndex() == 0) {
                                    headWriteFont.setBold(true);
                                }

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
                            }
                        } else {

                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            if (context.getRowIndex() == 6 || context.getRowIndex() == 7 || context.getRowIndex() == 8) {
                                headWriteFont.setBold(true);
                            }
                            headWriteFont.setFontName("微软雅黑");
                            writeCellStyle.setWriteFont(headWriteFont);
                            if (context.getRowIndex() == 7) {
                                if (context.getColumnIndex() > 1) {
                                    //居中
                                    writeCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                                }
                            } else {
                                if (context.getRowIndex() > 8 && context.getColumnIndex() > targetDecomposeDTO.getFileNameList().size()) {
                                    //靠左
                                    writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                } else {
                                    //靠左
                                    writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                                }

                            }
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                            if (context.getRowIndex() > 6) {
                                //设置边框
                                writeCellStyle.setBorderLeft(BorderStyle.THIN);
                                writeCellStyle.setBorderTop(BorderStyle.THIN);
                                writeCellStyle.setBorderRight(BorderStyle.THIN);
                                writeCellStyle.setBorderBottom(BorderStyle.THIN);
                            }
                            if (context.getRowIndex() == 7 || context.getRowIndex() == 8) {
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
                        // 列宽16
                        sheet.setColumnWidth(columnIndex, (270 * 16));
                        // 行高20
                        sheet.setDefaultRowHeight((short) (20 * 15));

                    }
                })
                .doWrite(TargetDecomposeImportListener.detailsResultDataList(targetDecomposeDetailsDTOS, targetDecomposeDTO, true));
    }

    /**
     * 修改经营结果分析报表详情
     */
    @PostMapping("/result/edit")
    public AjaxResult resultEditSave(@RequestBody @Validated(TargetDecomposeDTO.UpdateTargetDecomposeDTO.class) TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.updateResultTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 查询目标分解(销售订单)表列表
     */
    @GetMapping("/order/list")
    public AjaxResult orderList(TargetDecomposeDTO targetDecomposeDTO) {
        List<TargetDecomposeDTO> list = targetDecomposeService.selectOrderList(targetDecomposeDTO);
        return AjaxResult.success(list);
    }

    /**
     * 查询目标分解(销售收入)表列表
     */
    @GetMapping("/income/list")
    public AjaxResult incomeList(TargetDecomposeDTO targetDecomposeDTO) {
        List<TargetDecomposeDTO> list = targetDecomposeService.selectIncomeList(targetDecomposeDTO);
        return AjaxResult.success(list);
    }

    /**
     * 查询目标分解(销售回款)表列表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:list")
    @GetMapping("/returned/list")
    public AjaxResult returnedList(TargetDecomposeDTO targetDecomposeDTO) {
        List<TargetDecomposeDTO> list = targetDecomposeService.selectReturnedList(targetDecomposeDTO);
        return AjaxResult.success(list);
    }

    /**
     * 查询目标分解(自定义)表列表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:list")
    @GetMapping("/custom/list")
    public AjaxResult customList(TargetDecomposeDTO targetDecomposeDTO) {
        List<TargetDecomposeDTO> list = targetDecomposeService.selectCustomList(targetDecomposeDTO);
        return AjaxResult.success(list);
    }

    /**
     * 目标分解导出详情模板Excel
     */
    @SneakyThrows
    @PostMapping("/export-template")
    public void exportTargetDecomposeTemplate(@RequestBody TargetDecomposeDTO targetDecomposeDTO, HttpServletResponse response) {
        Department department = new Department();
        department.setStatus(1);
        R<List<DepartmentDTO>> departmentExcelList = remoteDepartmentService.selectDepartmentExcelAllListName(department, SecurityConstants.INNER);
        //部门名称集合
        List<DepartmentDTO>  parentDepartmentExcelNamesData = departmentExcelList.getData();
        List<String>  parentDepartmentExcelNames = new ArrayList<>();
        if (StringUtils.isNotEmpty(parentDepartmentExcelNamesData)){
            for (DepartmentDTO parentDepartmentExcelNamesDatum : parentDepartmentExcelNamesData) {
                parentDepartmentExcelNames.add(parentDepartmentExcelNamesDatum.getParentDepartmentExcelName());
            }

        }
        //销售员下拉框
        R<List<EmployeeDTO>> employeeExcelList = remoteEmployeeService.selectDropEmployeeList( new EmployeeDTO(), SecurityConstants.INNER);
        List<EmployeeDTO> employeeExcelListData = employeeExcelList.getData();
        List<String> employeeExcelExcelNames = new ArrayList<>();
        if (StringUtils.isNotEmpty(employeeExcelListData)){
            for (EmployeeDTO employeeExcelListDatum : employeeExcelListData) {
                employeeExcelExcelNames.add(employeeExcelListDatum.getEmployeeName()+"（"+employeeExcelListDatum.getEmployeeCode()+"）");
            }
        }
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeFlag("user");
        //滚动预测负责人下拉框
        R<List<EmployeeDTO>> principalEmployeeExcelList = remoteEmployeeService.selectDropEmployeeList(employeeDTO, SecurityConstants.INNER);
        List<EmployeeDTO> principalEmployeeListData = principalEmployeeExcelList.getData();
        List<String> principalEmployeeExcelNames = new ArrayList<>();
        if (StringUtils.isNotEmpty(principalEmployeeListData)){
            for (EmployeeDTO employeeExcelListDatum : principalEmployeeListData) {
                principalEmployeeExcelNames.add(employeeExcelListDatum.getEmployeeName()+"（"+employeeExcelListDatum.getEmployeeCode()+"）");
            }
        }
        IndustryDTO industryDTO = new IndustryDTO();
        industryDTO.setStatus(1);
        //行业下拉框
        R<List<IndustryDTO>> industryExcelList = remoteIndustryService.selectListByIndustry(industryDTO, SecurityConstants.INNER);
        List<String> parentIndustryExcelNames = new ArrayList<>();
        List<IndustryDTO> industryExcelListData = industryExcelList.getData();
        if (StringUtils.isNotEmpty(industryExcelListData)){
            parentIndustryExcelNames = industryExcelListData.stream().filter(f -> StringUtils.isNotBlank(f.getParentIndustryExcelName())).map(IndustryDTO::getParentIndustryExcelName).collect(Collectors.toList());

        }
        ProductDTO productDTO =  new ProductDTO();
        productDTO.setListingFlag(1);
        //产品下拉框
        List<ProductDTO> productDTOList = productService.selectDropList(productDTO);
        List<String> parentProductExcelNames = new ArrayList<>();
        if (StringUtils.isNotEmpty(productDTOList)){
            parentProductExcelNames = productDTOList.stream().filter(f -> StringUtils.isNotBlank(f.getParentProductExcelName())).map(ProductDTO::getParentProductExcelName).collect(Collectors.toList());
        }
        //区域下拉框
        List<AreaDTO> areaDTOList = areaService.selectAreaList(new AreaDTO());
        List<String> areaExcelNames = new ArrayList<>();
        if (StringUtils.isNotEmpty(areaDTOList)){
            areaExcelNames = areaDTOList.stream().filter(f -> StringUtils.isNotBlank(f.getAreaName())).map(AreaDTO::getAreaName).collect(Collectors.toList());
        }
        //省份下拉框
        R<List<RegionDTO>> regionExcelList = remoteRegionService.getDropList(new RegionDTO(), SecurityConstants.INNER);
        List<String> provinceExcelNames = new ArrayList<>();
        List<RegionDTO> regionExcelListData = regionExcelList.getData();
        if (StringUtils.isNotEmpty(regionExcelListData)){
            provinceExcelNames = regionExcelListData.stream().filter(f -> StringUtils.isNotBlank(f.getProvinceName())).map(RegionDTO::getProvinceName).collect(Collectors.toList());
        }
        Map<Integer, List<String>> selectMap = new HashMap<>();


        //自定义表头
        List<List<String>> head = TargetDecomposeImportListener.headTemplate(selectMap,targetDecomposeDTO,parentDepartmentExcelNames,employeeExcelExcelNames,principalEmployeeExcelNames,parentIndustryExcelNames,parentProductExcelNames,areaExcelNames,provinceExcelNames);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("自定义目标分解详情" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .head(head)// 设置表头
                .inMemory(true)
                .useDefaultStyle(false)
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap,12,65533))
                .sheet(targetDecomposeDTO.getIndicatorName() + "目标分解详情")// 设置 sheet 的名字
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
                        if (context.getRowIndex() < 5) {
                            if (context.getColumnIndex() < 2) {
                                headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
                                if (context.getRowIndex() == 0) {
                                    headWriteFont.setBold(true);
                                }//加粗
                                else if (context.getRowIndex() > 0 && context.getColumnIndex() == 0) {
                                    headWriteFont.setBold(true);
                                }

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
                            }
                        } else {

                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            if (context.getRowIndex() == 6 || context.getRowIndex() == 7 || context.getRowIndex() == 10) {
                                headWriteFont.setBold(true);
                            }
                            headWriteFont.setFontName("微软雅黑");

                            //靠左
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            if (context.getRowIndex() == 8 && context.getColumnIndex() > 0) {
                                //靠右
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);

                            }
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                            if (context.getRowIndex() == 7) {
                                if (context.getColumnIndex() < 5) {
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
                                }

                            }
                            if (context.getRowIndex() == 8) {
                                if (context.getColumnIndex() < 5) {
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
                                    byte[] rgb = new byte[]{(byte) 217, (byte) 217, (byte) 217};
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

                                }

                            }

                            if (context.getRowIndex() == 11) {

                                int num = 0;
                                if (StringUtils.isNotEmpty(targetDecomposeDTO.getTargetDecomposeDetailsDTOS())) {
                                    num = targetDecomposeDTO.getTargetDecomposeDetailsDTOS().get(0).getDecomposeDetailCyclesDTOS().size();
                                }
                                if (context.getColumnIndex() <(targetDecomposeDTO.getFileNameList().size()+1)){
                                    headWriteFont.setColor(IndexedColors.RED.getIndex());
                                }
                                if (context.getColumnIndex() < (targetDecomposeDTO.getFileNameList().size() + 1 + num)) {
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
                                }
                            }
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
                        // 行高7
                        sheet.setDefaultRowHeight((short) (20 * 15));


                    }
                })
                .doWrite(new ArrayList<>());
    }

    /**
     * 解析Excel
     */
    @PostMapping("/excelParseObject")
    public AjaxResult excelParseObject(TargetDecomposeDTO targetDecomposeDTO, MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        //产品下拉框
        List<ProductDTO> productDTOList = productService.selectDropList(new ProductDTO());

        //区域下拉框
        List<AreaDTO> areaDTOList = areaService.selectAreaList(new AreaDTO());

        return AjaxResult.success(targetDecomposeService.excelParseObject(targetDecomposeDTO, file,productDTOList,areaDTOList));
    }


    /**
     * 目标分解导出详情Excel
     */
    @SneakyThrows
    @GetMapping("/export-details/info/{targetDecomposeId}")
    public void exportTargetDecomposeDetails(@PathVariable Long targetDecomposeId, HttpServletResponse response) {
        //查询详情
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectOrderTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        if (StringUtils.isNull(targetDecomposeDTO)) {
            throw new ServiceException("数据不存在！ 请刷新重试！");
        }
        //目标分解操作列导出详情数据
        List<TargetDecomposeDetailsExcel> targetDecomposeDetailsExcels = targetDecomposeService.exportTargetDecomposeDetails(targetDecomposeId, targetDecomposeDTO);
        //自定义表头
        List<List<String>> head = TargetDecomposeImportListener.headDetails(targetDecomposeDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("自定义目标分解详情" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .head(head)// 设置表头
                .inMemory(true)
                .useDefaultStyle(false)
                .sheet(targetDecomposeDTO.getIndicatorName() + "目标分解详情")// 设置 sheet 的名字
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
                        if (context.getRowIndex() < 5) {
                            if (context.getColumnIndex() < 2) {
                                headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
                                if (context.getRowIndex() == 0) {
                                    headWriteFont.setBold(true);
                                }//加粗
                                else if (context.getRowIndex() > 0 && context.getColumnIndex() == 0) {
                                    headWriteFont.setBold(true);
                                }

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
                            }
                        } else {

                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            if (context.getRowIndex() == 6 || context.getRowIndex() == 7 || context.getRowIndex() == 10 || context.getRowIndex() == 11) {
                                headWriteFont.setBold(true);
                            }
                            headWriteFont.setFontName("微软雅黑");
                            writeCellStyle.setWriteFont(headWriteFont);
                            //靠左
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            if (context.getRowIndex() == 8 && context.getColumnIndex() > 0) {
                                //靠右
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);

                            }
                            if (context.getRowIndex() > 11 && context.getColumnIndex() > targetDecomposeDTO.getFileNameList().size()) {
                                //靠右
                                writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                            }
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                            if (context.getRowIndex() == 7) {
                                if (context.getColumnIndex() < 7) {
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
                                }

                            }
                            if (context.getRowIndex() == 8) {
                                if (context.getColumnIndex() < 7) {
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
                                    byte[] rgb = new byte[]{(byte) 217, (byte) 217, (byte) 217};
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
                                }

                            }
                            if (context.getRowIndex() > 10) {
                                int num = 0;
                                if (StringUtils.isNotEmpty(targetDecomposeDetailsExcels)) {
                                    num = targetDecomposeDetailsExcels.get(0).getCycleTargets().size();
                                }
                                if (context.getColumnIndex() < (targetDecomposeDTO.getFileNameList().size() + 2 + num)){
                                    //设置边框
                                    writeCellStyle.setBorderLeft(BorderStyle.THIN);
                                    writeCellStyle.setBorderTop(BorderStyle.THIN);
                                    writeCellStyle.setBorderRight(BorderStyle.THIN);
                                    writeCellStyle.setBorderBottom(BorderStyle.THIN);
                                }

                            }
                            if (context.getRowIndex() == 11) {
                                int num = 0;
                                if (StringUtils.isNotEmpty(targetDecomposeDetailsExcels)) {
                                    num = targetDecomposeDetailsExcels.get(0).getCycleTargets().size();
                                }
                                if (context.getColumnIndex() < (targetDecomposeDTO.getFileNameList().size() + 2 + num)) {
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
                        // 列宽16
                        sheet.setColumnWidth(columnIndex, (270 * 16));
                        // 行高7
                        sheet.setDefaultRowHeight((short) (20 * 15));
    /*                    //取消自动合并
                        if (relativeRowIndex == 8){
                            if (cell.getColumnIndex()>0 && cell.getColumnIndex()<5){
                                sheet.removeMergedRegion(sheet.getNumMergedRegions() - 1);
                            }
                        }*/
                    }
                })
                .doWrite(TargetDecomposeImportListener.detailsDataList(targetDecomposeDetailsExcels, targetDecomposeDTO));
    }
}
