package net.qixiaowei.operate.cloud.controller.salary;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.salary.OfficialRankEmolumentDTO;
import net.qixiaowei.operate.cloud.excel.salary.OfficialRankEmolumentExcel;
import net.qixiaowei.operate.cloud.excel.salary.OfficialRankEmolumentImportListener;
import net.qixiaowei.operate.cloud.service.salary.IOfficialRankEmolumentService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author Graves
 * @since 2022-11-30
 */
@RestController
@RequestMapping("officialRankEmolument")
public class OfficialRankEmolumentController extends BaseController {


    @Autowired
    private IOfficialRankEmolumentService officialRankEmolumentService;


    /**
     * 查询职级薪酬表详情
     */
    @RequiresPermissions("operate:cloud:officialRankEmolument:info")
    @GetMapping("/info")
    public AjaxResult info(OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        return AjaxResult.success(officialRankEmolumentService.selectOfficialRankEmolumentByOfficialRankEmolumentId(officialRankEmolumentDTO));
    }

    /**
     * 查看该职级的分解信息
     */
    @RequiresPermissions("operate:cloud:officialRankEmolument:info")
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
    @RequiresPermissions("operate:cloud:officialRankEmolument:info")
    @GetMapping("/selectByPostId/{postId}")
    public AjaxResult selectByPostId(@PathVariable Long postId) {
        return AjaxResult.success(officialRankEmolumentService.selectByPostId(postId));
    }

    /**
     * 导出工资发薪模板
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:officialRankEmolument:import")
    @GetMapping("/export-template")
    public void exportTemplate(OfficialRankEmolumentDTO officialRankEmolumentDTO, HttpServletResponse response) {
        try {
            OfficialRankEmolumentDTO emolumentDTO = officialRankEmolumentService.selectOfficialRankEmolumentByOfficialRankEmolumentId(officialRankEmolumentDTO);
            List<OfficialRankEmolumentDTO> officialRankEmolumentDTOList = emolumentDTO.getOfficialRankEmolumentDTOList();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(CharsetKit.UTF_8);
            String fileName = URLEncoder.encode("职级确定薪酬导入" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                    , CharsetKit.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), OfficialRankEmolumentExcel.class)
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
                            if (context.getRowIndex() < 3) {
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
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                        }
                    }).doWrite(OfficialRankEmolumentImportListener.dataList(officialRankEmolumentDTOList));
        } catch (IOException e) {
            throw new ServiceException("导出失败");
        }
    }

}
