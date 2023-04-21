package net.qixiaowei.strategy.cloud.controller.gap;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.excel.CustomVerticalCellStyleStrategy;
import net.qixiaowei.integration.common.utils.excel.SelectSheetWriteHandler;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisDTO;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Graves
 * @since 2023-02-24
 */
@RestController
@RequestMapping("gapAnalysis")
public class GapAnalysisController extends BaseController {


    @Autowired
    private IGapAnalysisService gapAnalysisService;


    /**
     * 查询差距分析表详情
     */
    @RequiresPermissions(value = {"strategy:cloud:gapAnalysis:info", "strategy:cloud:gapAnalysis:edit"}, logical = Logical.OR)
    @GetMapping("/info/{gapAnalysisId}")
    public AjaxResult info(@PathVariable Long gapAnalysisId) {
        GapAnalysisDTO gapAnalysisDTO = gapAnalysisService.selectGapAnalysisByGapAnalysisId(gapAnalysisId);
        return AjaxResult.success(gapAnalysisDTO);
    }

    /**
     * 分页查询差距分析表列表
     */
    @RequiresPermissions("strategy:cloud:gapAnalysis:pageList")
    @DataScope(businessAlias = "ga")
    @GetMapping("/pageList")
    public TableDataInfo pageList(GapAnalysisDTO gapAnalysisDTO) {
        startPage();
        List<GapAnalysisDTO> list = gapAnalysisService.selectGapAnalysisList(gapAnalysisDTO);
        return getDataTable(list);
    }

    /**
     * 查询差距分析表列表
     */
    @GetMapping("/list")
    public AjaxResult list(GapAnalysisDTO gapAnalysisDTO) {
        List<GapAnalysisDTO> list = gapAnalysisService.selectGapAnalysisList(gapAnalysisDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增差距分析表
     */
    @RequiresPermissions("strategy:cloud:gapAnalysis:add")
    @Log(title = "保存差距分析表", businessType = BusinessType.GAP_ANALYSIS, businessId = "gapAnalysisId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody GapAnalysisDTO gapAnalysisDTO) {
        return AjaxResult.success(gapAnalysisService.insertGapAnalysis(gapAnalysisDTO));
    }


    /**
     * 修改差距分析表
     */
    @RequiresPermissions("strategy:cloud:gapAnalysis:edit")
    @Log(title = "保存差距分析表", businessType = BusinessType.GAP_ANALYSIS, businessId = "gapAnalysisId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody GapAnalysisDTO gapAnalysisDTO) {
        return toAjax(gapAnalysisService.updateGapAnalysis(gapAnalysisDTO));
    }

    /**
     * 逻辑删除差距分析表
     */
    @RequiresPermissions("strategy:cloud:gapAnalysis:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody GapAnalysisDTO gapAnalysisDTO) {
        return toAjax(gapAnalysisService.logicDeleteGapAnalysisByGapAnalysisId(gapAnalysisDTO));
    }

    /**
     * 逻辑批量删除差距分析表
     */
    @RequiresPermissions("strategy:cloud:gapAnalysis:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> gapAnalysisIds) {
        return toAjax(gapAnalysisService.logicDeleteGapAnalysisByGapAnalysisIds(gapAnalysisIds));
    }

    /**
     * 下载模板表
     */
    @SneakyThrows
    @RequiresPermissions(value = {"strategy:cloud:gapAnalysis:edit", "strategy:cloud:gapAnalysis:add"}, logical = Logical.OR)
    @GetMapping("export-template")
    public void exportTemplate(@RequestParam("operateHistoryYear") Integer operateHistoryYear, @RequestParam("operateYear") Integer operateYear, HttpServletResponse response) {
        if (StringUtils.isNull(operateHistoryYear)) {
            throw new ServiceException("请传入历史年度");
        }
        Map<Integer, List<String>> selectMap = new HashMap<>();
        List<List<String>> head = gapAnalysisService.headTemplate(operateHistoryYear, operateYear, selectMap);
        String fileName = URLEncoder.encode("差距分析导入系统模板" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
        CustomVerticalCellStyleStrategy levelStrategy = new CustomVerticalCellStyleStrategy(head);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(head)
                .sheet("Sheet1")// 设置 sheet 的名字
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap,1,65533))
                .registerWriteHandler(levelStrategy)
                .registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                    @Override
                    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head,
                                                  Integer relativeRowIndex, Boolean isHead) {
                        Sheet sheet = writeSheetHolder.getSheet();
                        sheet.setColumnWidth(cell.getColumnIndex(), 6000);
                        cell.setCellType(CellType.STRING);
                        int rows = cell.getStringCellValue().split("\n").length;
                        cell.getRow().setHeightInPoints(rows * 20);
                    }
                })
                .doWrite(new ArrayList<>());
    }

    /**
     * 解析Excel
     */
    @RequiresPermissions(value = {"strategy:cloud:gapAnalysis:edit", "strategy:cloud:gapAnalysis:add"}, logical = Logical.OR)
    @PostMapping("/excelParseObject")
    public AjaxResult excelParseObject(@RequestParam("operateHistoryYear") Integer operateHistoryYear, @RequestParam("operateYear") Integer operateYear, MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        return AjaxResult.success(gapAnalysisService.excelParseObject(operateHistoryYear, operateYear, file));
    }

}
