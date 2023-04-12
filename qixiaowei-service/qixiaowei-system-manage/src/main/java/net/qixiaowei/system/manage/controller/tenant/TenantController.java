package net.qixiaowei.system.manage.controller.tenant;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.data.DataFormatData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.excel.tenant.TenantExcel;
import net.qixiaowei.system.manage.service.tenant.ITenantService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author TANGMICHI
 * @since 2022-09-24
 */
@RestController
@RequestMapping("tenant")
public class TenantController extends BaseController {


    @Autowired
    private ITenantService tenantService;


    //==============================管理租户的管理员==================================//

    /**
     * 分页查询租户表列表
     */
    @RequiresPermissions("system:manage:tenant:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TenantDTO tenantDTO) {
        startPage();
        List<TenantDTO> list = tenantService.selectTenantList(tenantDTO);
        tenantService.handleResult(list);
        return getDataTable(list);
    }

    /**
     * 生成租户编码
     *
     * @return 租户编码
     */
    @RequiresPermissions(value = {"system:manage:tenant:add", "system:manage:tenant:edit"}, logical = Logical.OR)
    @GetMapping("/generate/tenantCode")
    public AjaxResult generateTenantCode() {
        return AjaxResult.success("操作成功", tenantService.generateTenantCode());
    }

    /**
     * 新增租户表
     */
    @Log(title = "新增租户", businessType = BusinessType.TENANT, businessId = "tenantId", operationType = OperationType.INSERT)
    @RequiresPermissions("system:manage:tenant:add")
    @PostMapping("/add")
    public AjaxResult addSave(@Validated(TenantDTO.AddTenantDTO.class) @RequestBody TenantDTO tenantDTO) {
        return AjaxResult.success(tenantService.insertTenant(tenantDTO));
    }

    /**
     * 修改租户表
     */
    @Log(title = "编辑租户", businessType = BusinessType.TENANT, businessId = "tenantId", operationType = OperationType.UPDATE)
    @RequiresPermissions(value = {"system:manage:tenant:edit", "system:manage:tenant:info"}, logical = Logical.OR)
    @PostMapping("/edit")
    public AjaxResult editSave(@Validated(TenantDTO.UpdateTenantDTO.class) @RequestBody TenantDTO tenantDTO) {
        return toAjax(tenantService.updateTenant(tenantDTO));
    }

    /**
     * 查询单个租户
     */
    @RequiresPermissions("system:manage:tenant:info")
    @GetMapping("/info/{tenantId}")
    public AjaxResult queryTenantDTO(@PathVariable Long tenantId) {
        return AjaxResult.success(tenantService.selectTenantByTenantId(tenantId));
    }

    /**
     * 导出租户
     */
    @SneakyThrows
    @RequiresPermissions("system:manage:tenant:export")
    @GetMapping("export")
    public void exportTenant(@RequestParam Map<String, Object> tenant, TenantDTO tenantDTO, HttpServletResponse response) {
        List<TenantExcel> tenantExcelList = tenantService.exportTenant(tenantDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("租户列表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TenantExcel.class)
                .inMemory(true)
                .useDefaultStyle(false)
                .sheet("租户列表")
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
                            cellData.setWriteCellStyle(writeCellStyle);

                            cellData.setOriginCellStyle(xssfCellColorStyle);
                            cell.setCellStyle(cellStyle);
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
                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            headWriteFont.setFontName("微软雅黑");
                            writeCellStyle.setWriteFont(headWriteFont);
                        }

                    }
                })
                .doWrite(tenantExcelList);
    }

    /**
     * 逻辑删除租户表
     */
    @RequiresPermissions("system:manage:tenant:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(TenantDTO.DeleteTenantDTO.class) TenantDTO tenantDTO) {
        return toAjax(tenantService.logicDeleteTenantByTenantId(tenantDTO));
    }

    /**
     * 逻辑批量删除租户表
     */
    @RequiresPermissions("system:manage:tenant:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody @Validated(TenantDTO.DeleteTenantDTO.class) List<TenantDTO> TenantDtos) {
        return toAjax(tenantService.logicDeleteTenantByTenantIds(TenantDtos));
    }

    //==============================租户本身==================================//

    /**
     * 租户查询自己的登录界面信息
     */
    @GetMapping("/loginForm")
    public AjaxResult queryTenantLoginForm(HttpServletRequest request) {
        return AjaxResult.success(tenantService.queryTenantLoginForm(request));
    }

    /**
     * 租户查询自己的企业信息
     */
    @RequiresPermissions(value = {"system:manage:tenant:info:self", "system:manage:tenant:edit:self"}, logical = Logical.OR)
    @GetMapping("/info")
    public AjaxResult queryTenantInfoOfSelf() {
        return AjaxResult.success(tenantService.queryTenantInfoOfSelf());
    }

    /**
     * 租户修改自己的企业信息
     */
    @RequiresPermissions("system:manage:tenant:edit:self")
    @PostMapping("/updateInfo")
    public AjaxResult updateMyTenant(@RequestBody @Validated(TenantDTO.UpdateTenantInfoDTO.class) TenantDTO tenantDTO) {
        return AjaxResult.success(tenantService.updateMyTenant(tenantDTO));
    }

}
