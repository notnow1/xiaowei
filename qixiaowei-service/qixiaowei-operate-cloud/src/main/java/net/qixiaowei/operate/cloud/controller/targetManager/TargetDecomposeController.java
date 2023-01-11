package net.qixiaowei.operate.cloud.controller.targetManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.DataFormatData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.AbstractVerticalCellStyleStrategy;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.common.utils.excel.CustomVerticalCellStyleStrategy;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.targetManager.DecomposeDetailCyclesDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.excel.product.ProductExcel;
import net.qixiaowei.operate.cloud.excel.product.ProductImportListener;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeDetailsExcel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import com.alibaba.excel.EasyExcel;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeExcel;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeImportListener;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * @author TANGMICHI
 * @since 2022-10-27
 */
@RestController
@RequestMapping("targetDecompose")
public class TargetDecomposeController extends BaseController {


    @Autowired
    private ITargetDecomposeService targetDecomposeService;


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
        EasyExcel.write(response.getOutputStream(), TargetDecomposeExcel.class).sheet("销售订单目标分解详情").doWrite(targetDecomposeExcelList);
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
        EasyExcel.write(response.getOutputStream(), TargetDecomposeExcel.class).sheet("销售收入目标分解详情").doWrite(targetDecomposeExcelList);
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
        EasyExcel.write(response.getOutputStream(), TargetDecomposeExcel.class).sheet("销售回款目标分解详情").doWrite(targetDecomposeExcelList);
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
                .head(head)// 设置表头
                .sheet("自定义目标分解详情")// 设置 sheet 的名字
                // 自适应列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(TargetDecomposeImportListener.dataList(targetDecomposeExcelList));// 写入数据
    }

    //==============================经营结果分析报表==================================//

    /**
     * 查询经营结果分析报表列表
     */
    @RequiresPermissions("operate:cloud:targetSetting:analyse")
    @GetMapping("/result/list")
    public AjaxResult resultList(TargetDecomposeDTO targetDecomposeDTO) {
        List<TargetDecomposeDTO> list = targetDecomposeService.resultList(targetDecomposeDTO);
        return AjaxResult.success(list);
    }


    //==============================滚动预测管理==================================//

    /**
     * 查询滚动预测表详情
     */
    @RequiresPermissions(value = {"operate:cloud:targetDecompose:roll:info", "operate:cloud:targetDecompose:roll:edit"}, logical = Logical.OR)
    @GetMapping("/roll/info/{targetDecomposeId}")
    public AjaxResult rollInfo(@PathVariable Long targetDecomposeId, @RequestParam(required = false) Long backlogId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectRollTargetDecomposeByTargetDecomposeId(targetDecomposeId, backlogId);
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
    @Log(title = "移交预测负责人", businessType = BusinessType.TARGET_DECOMPOSE_ROLL, businessId = "targetDecomposeId", operationType = OperationType.UPDATE)
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
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectRollTargetDecomposeByTargetDecomposeId(targetDecomposeId, backlogId);
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
                .head(head)// 设置表头
                .sheet("滚动预测详情")// 设置 sheet 的名字
                // 自适应列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .registerWriteHandler(new AbstractVerticalCellStyleStrategy() {
                    //设置标题栏的列样式
                    @Override
                    protected WriteCellStyle headCellStyle(Head head) {
                        //内容样式策略
                        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
                        //居中
                        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                        //设置 自动换行
                        contentWriteCellStyle.setWrapped(true);
                        // 字体策略
                        WriteFont contentWriteFont = new WriteFont();
                        // 字体大小
                        contentWriteFont.setFontHeightInPoints((short) 15);
                        contentWriteCellStyle.setWriteFont(contentWriteFont);
                        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        return contentWriteCellStyle;
                    }
                })
                .useDefaultStyle(false)
                //设置文本
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        DataFormatData dataFormatData = new DataFormatData();
                        dataFormatData.setIndex((short) 49);
                        writeCellStyle.setDataFormatData(dataFormatData);
                    }
                })
                .doWrite(TargetDecomposeImportListener.detailsRollDataList(targetDecomposeDetailsDTOS, targetDecomposeDTO,true));
    }

    /**
     * 滚动预测导入 下载模板Excel
     */
    @SneakyThrows
    @GetMapping("/exportRoll-details-template/info/{targetDecomposeId}")
    public void exportRollTargetDecomposeDetailsTemplate(@PathVariable Long targetDecomposeId, @RequestParam(required = false) Long backlogId, HttpServletResponse response) {
        //查询详情
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectRollTargetDecomposeByTargetDecomposeId(targetDecomposeId, backlogId);
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
                .sheet("滚动预测详情")// 设置 sheet 的名字
                .registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                    @Override
                    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
                        for (WriteCellData<?> writeCellData : list) {
                            Row row = cell.getRow();
                            row.setHeightInPoints(50);
                        }
                    }
                })
                .registerWriteHandler(new AbstractVerticalCellStyleStrategy() {
                    //设置标题栏的列样式
                    @Override
                    protected WriteCellStyle headCellStyle(Head head) {
                        //内容样式策略
                        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
                        //居中
                        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                        //设置 自动换行
                        contentWriteCellStyle.setWrapped(true);
                        // 字体策略
                        WriteFont contentWriteFont = new WriteFont();
                        // 字体大小
                        contentWriteFont.setFontHeightInPoints((short) 15);
                        contentWriteCellStyle.setWriteFont(contentWriteFont);
                        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        return contentWriteCellStyle;
                    }
                })
                .useDefaultStyle(false)
                //设置文本
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        DataFormatData dataFormatData = new DataFormatData();
                        dataFormatData.setIndex((short) 49);
                        writeCellStyle.setDataFormatData(dataFormatData);
                    }
                })
                .doWrite(TargetDecomposeImportListener.detailsRollDataList(targetDecomposeDetailsDTOS, targetDecomposeDTO,false));
    }
    /**
     * 导入解析滚动预测
     */
//    @RequiresPermissions("system:manage:employee:import")
    @PostMapping("import")
    public AjaxResult importProduct(Long targetDecomposeId ,Long backlogId,MultipartFile file) throws IOException {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectRollTargetDecomposeByTargetDecomposeId(targetDecomposeId, backlogId);
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }

        List<DecomposeDetailCyclesDTO>  list = new ArrayList<>();

        //构建读取器
        ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
        ExcelReaderSheetBuilder sheet = read.sheet(0);
        List<Map<Integer, String>> listMap = sheet.doReadSync();

        //导入解析滚动预测
        TargetDecomposeImportListener.mapToListModel(3, 0, listMap, list,targetDecomposeDTO);
        // 调用importer方法
        TargetDecomposeDTO targetDecomposeDTO1 = targetDecomposeService.importProduct(list, targetDecomposeDTO);

        return AjaxResult.success(targetDecomposeDTO1);
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
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectResultTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        return AjaxResult.success(targetDecomposeDTO);
    }


    /**
     * 经营结果分析报表导出详情Excel
     */
    @SneakyThrows
    @GetMapping("/exportResult-details/info/{targetDecomposeId}")
    public void exportResultTargetDecomposeDetails(@PathVariable Long targetDecomposeId, HttpServletResponse response) {
        //查询详情
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectResultTargetDecomposeByTargetDecomposeId(targetDecomposeId);
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
                .head(head)// 设置表头
                .sheet("经营结果分析报表详情")// 设置 sheet 的名字
                // 自适应列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .registerWriteHandler(new AbstractVerticalCellStyleStrategy() {
                    //设置标题栏的列样式
                    @Override
                    protected WriteCellStyle headCellStyle(Head head) {
                        //内容样式策略
                        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
                        //居中
                        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                        //设置 自动换行
                        contentWriteCellStyle.setWrapped(true);
                        // 字体策略
                        WriteFont contentWriteFont = new WriteFont();
                        // 字体大小
                        contentWriteFont.setFontHeightInPoints((short) 15);
                        contentWriteCellStyle.setWriteFont(contentWriteFont);
                        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        return contentWriteCellStyle;
                    }
                })
                .useDefaultStyle(false)
                //设置文本
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        DataFormatData dataFormatData = new DataFormatData();
                        dataFormatData.setIndex((short) 49);
                        writeCellStyle.setDataFormatData(dataFormatData);
                    }
                })
                .doWrite(TargetDecomposeImportListener.detailsResultDataList(targetDecomposeDetailsDTOS, targetDecomposeDTO,true));
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
     * 目标分解导出列表模板Excel
     */
    @SneakyThrows
    @PostMapping("/export-template")
    public void exportTargetDecomposeTemplate(@RequestBody TargetDecomposeDTO targetDecomposeDTO, HttpServletResponse response) {
        //自定义表头
        List<List<String>> head = TargetDecomposeImportListener.headTemplate(targetDecomposeDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("自定义目标分解详情" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(head)// 设置表头
                .sheet("自定义目标分解详情")// 设置 sheet 的名字
                // 自适应列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(TargetDecomposeImportListener.excelParseObject(targetDecomposeDTO));
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
        return AjaxResult.success(targetDecomposeService.excelParseObject(targetDecomposeDTO, file));
    }


    /**
     * 目标分解导出详情Excel
     */
    @SneakyThrows
    @GetMapping("/export-details/info/{targetDecomposeId}")
    public void exportTargetDecomposeDetails(@PathVariable Long targetDecomposeId, HttpServletResponse response) {
        //查询详情
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
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
                .head(head)// 设置表头
                .sheet("自定义目标分解详情")// 设置 sheet 的名字
                // 自适应列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                //设置文本
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        DataFormatData dataFormatData = new DataFormatData();
                        dataFormatData.setIndex((short) 49);
                        writeCellStyle.setDataFormatData(dataFormatData);
                    }
                })
                .doWrite(TargetDecomposeImportListener.detailsDataList(targetDecomposeDetailsExcels, targetDecomposeDTO));
    }
}
