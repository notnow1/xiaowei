package net.qixiaowei.operate.cloud.controller.targetManager;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
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


    /**
     * 查询经营结果分析报表详情
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:info")
    @GetMapping("/result/info/{targetDecomposeId}")
    public AjaxResult resultInfo(@PathVariable Long targetDecomposeId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectResultTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        return AjaxResult.success(targetDecomposeDTO);
    }

    /**
     * 修改经营结果分析报表详情
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:edit")
    //@Log(title = "修改目标分解(销售订单)表", businessType = BusinessType.UPDATE)
    @PostMapping("/result/edit")
    public AjaxResult resultEditSave(@RequestBody @Validated(TargetDecomposeDTO.UpdateTargetDecomposeDTO.class)TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.updateResultTargetDecompose(targetDecomposeDTO));
    }
    /**
     * 查询经营结果分析报表列表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:list")
    @GetMapping("/result/list")
    public AjaxResult resultList(TargetDecomposeDTO targetDecomposeDTO) {
        List<TargetDecomposeDTO> list = targetDecomposeService.resultList(targetDecomposeDTO);
        return AjaxResult.success(list);
    }
    /**
     * 修改滚动预测详情
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:edit")
    //@Log(title = "修改目标分解(销售订单)表", businessType = BusinessType.UPDATE)
    @PostMapping("/roll/edit")
    public AjaxResult rollEditSave(@RequestBody @Validated(TargetDecomposeDTO.UpdateTargetDecomposeDTO.class)TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.updateRollTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 查询滚动预测表详情
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:info")
    @GetMapping("/roll/info/{targetDecomposeId}")
    public AjaxResult rollInfo(@PathVariable Long targetDecomposeId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectRollTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        return AjaxResult.success(targetDecomposeDTO);
    }
    /**
     * 分页查询滚动预测表列表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:pageList")
    @GetMapping("/roll/pageList")
    public TableDataInfo rollPageList(TargetDecomposeDTO targetDecomposeDTO) {
        startPage();
        List<TargetDecomposeDTO> list = targetDecomposeService.rollPageList(targetDecomposeDTO);
        return getDataTable(list);
    }

    /**
     * 移交预测负责人
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:edit")
    //@Log(title = "移交预测负责人", businessType = BusinessType.UPDATE)
    @PostMapping("/turnOver/edit")
    public AjaxResult turnOverPrincipalEmployee(@RequestBody @Validated(TargetDecomposeDTO.RollUpdateTargetDecomposeDTO.class)TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.turnOverPrincipalEmployee(targetDecomposeDTO));
    }
    /**
     * 查询目标分解(销售订单)表详情
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:info")
    @GetMapping("/order/info/{targetDecomposeId}")
    public AjaxResult orderInfo(@PathVariable Long targetDecomposeId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectOrderTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        return AjaxResult.success(targetDecomposeDTO);
    }

    /**
     * 查询目标分解(销售收入)表详情
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:info")
    @GetMapping("/income/info/{targetDecomposeId}")
    public AjaxResult incomeInfo(@PathVariable Long targetDecomposeId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectIncomeTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        return AjaxResult.success(targetDecomposeDTO);
    }

    /**
     * 查询目标分解(销售回款)表详情
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:info")
    @GetMapping("/returned/info/{targetDecomposeId}")
    public AjaxResult returnedInfo(@PathVariable Long targetDecomposeId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectReturnedTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        return AjaxResult.success(targetDecomposeDTO);
    }

    /**
     * 查询目标分解(自定义)表详情
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:info")
    @GetMapping("/custom/info/{targetDecomposeId}")
    public AjaxResult customInfo(@PathVariable Long targetDecomposeId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeService.selectCustomTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        return AjaxResult.success(targetDecomposeDTO);
    }

    /**
     * 分页查询目标分解(销售订单)表列表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:pageList")
    @GetMapping("/order/pageList")
    public TableDataInfo orderPageList(TargetDecomposeDTO targetDecomposeDTO) {
        startPage();
        List<TargetDecomposeDTO> list = targetDecomposeService.selectOrderList(targetDecomposeDTO);
        return getDataTable(list);
    }


    /**
     * 查询目标分解(销售订单)表列表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:list")
    @GetMapping("/order/list")
    public AjaxResult orderList(TargetDecomposeDTO targetDecomposeDTO) {
        List<TargetDecomposeDTO> list = targetDecomposeService.selectOrderList(targetDecomposeDTO);
        return AjaxResult.success(list);
    }

    /**
     * 分页查询目标分解(销售收入)表列表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:pageList")
    @GetMapping("/income/pageList")
    public TableDataInfo incomePageList(TargetDecomposeDTO targetDecomposeDTO) {
        startPage();
        List<TargetDecomposeDTO> list = targetDecomposeService.selectIncomeList(targetDecomposeDTO);
        return getDataTable(list);
    }


    /**
     * 查询目标分解(销售收入)表列表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:list")
    @GetMapping("/income/list")
    public AjaxResult incomeList(TargetDecomposeDTO targetDecomposeDTO) {
        List<TargetDecomposeDTO> list = targetDecomposeService.selectIncomeList(targetDecomposeDTO);
        return AjaxResult.success(list);
    }


    /**
     * 分页查询目标分解(销售回款)表列表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:pageList")
    @GetMapping("/returned/pageList")
    public TableDataInfo returnedPageList(TargetDecomposeDTO targetDecomposeDTO) {
        startPage();
        List<TargetDecomposeDTO> list = targetDecomposeService.selectReturnedList(targetDecomposeDTO);
        return getDataTable(list);
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
     * 分页查询目标分解(自定义)表列表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:pageList")
    @GetMapping("/custom/pageList")
    public TableDataInfo customPageList(TargetDecomposeDTO targetDecomposeDTO) {
        startPage();
        List<TargetDecomposeDTO> list = targetDecomposeService.selectCustomList(targetDecomposeDTO);
        return getDataTable(list);
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
     * 新增目标分解(销售订单)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:add")
    //@Log(title = "新增目标分解(销售订单)表", businessType = BusinessType.INSERT)
    @PostMapping("/order/add")
    public AjaxResult orderAddSave(@RequestBody @Validated({TargetDecomposeDTO.AddTargetDecomposeDTO.class}) TargetDecomposeDTO targetDecomposeDTO) {
        return AjaxResult.success(targetDecomposeService.insertOrderTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 新增目标分解(销售收入)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:add")
    //@Log(title = "新增目标分解(销售收入)表", businessType = BusinessType.INSERT)
    @PostMapping("/income/add")
    public AjaxResult incomeAddSave(@RequestBody @Validated(TargetDecomposeDTO.AddTargetDecomposeDTO.class)TargetDecomposeDTO targetDecomposeDTO) {
        return AjaxResult.success(targetDecomposeService.insertIncomeTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 新增目标分解(销售回款)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:add")
    //@Log(title = "新增目标分解(销售回款)表", businessType = BusinessType.INSERT)
    @PostMapping("/returned/add")
    public AjaxResult returnedAddSave(@RequestBody @Validated(TargetDecomposeDTO.AddTargetDecomposeDTO.class)TargetDecomposeDTO targetDecomposeDTO) {
        return AjaxResult.success(targetDecomposeService.insertReturnedTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 新增目标分解(自定义)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:add")
    //@Log(title = "新增目标分解(自定义)表", businessType = BusinessType.INSERT)
    @PostMapping("/custom/add")
    public AjaxResult customAddSave(@RequestBody @Validated(TargetDecomposeDTO.AddTargetDecomposeDTO.class)TargetDecomposeDTO targetDecomposeDTO) {
        return AjaxResult.success(targetDecomposeService.insertCustomTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 修改目标分解(销售订单)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:edit")
    //@Log(title = "修改目标分解(销售订单)表", businessType = BusinessType.UPDATE)
    @PostMapping("/order/edit")
    public AjaxResult orderEditSave(@RequestBody @Validated(TargetDecomposeDTO.UpdateTargetDecomposeDTO.class)TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.updateOrderTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 修改目标分解(销售收入)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:edit")
    //@Log(title = "修改目标分解(销售收入)表", businessType = BusinessType.UPDATE)
    @PostMapping("/income/edit")
    public AjaxResult incomeEditSave(@RequestBody @Validated(TargetDecomposeDTO.UpdateTargetDecomposeDTO.class)TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.updateIncomeTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 修改目标分解(销售回款)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:edit")
    //@Log(title = "修改目标分解(销售回款)表", businessType = BusinessType.UPDATE)
    @PostMapping("/returned/edit")
    public AjaxResult returnedEditSave(@RequestBody @Validated(TargetDecomposeDTO.UpdateTargetDecomposeDTO.class)TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.updateReturnedTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 修改目标分解(自定义)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:edit")
    //@Log(title = "修改目标分解(自定义)表", businessType = BusinessType.UPDATE)
    @PostMapping("/custom/edit")
    public AjaxResult customEditSave(@RequestBody @Validated(TargetDecomposeDTO.UpdateTargetDecomposeDTO.class)TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.updateCustomTargetDecompose(targetDecomposeDTO));
    }

    /**
     * 逻辑删除目标分解(销售订单)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:remove")
    //@Log(title = "删除目标分解(销售订单)表", businessType = BusinessType.DELETE)
    @PostMapping("/order/remove")
    public AjaxResult orderRemove(@RequestBody @Validated(TargetDecomposeDTO.DeleteTargetDecomposeDTO.class)TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.logicDeleteOrderTargetDecomposeByTargetDecomposeId(targetDecomposeDTO));
    }

    /**
     * 逻辑删除目标分解(销售收入)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:remove")
    //@Log(title = "删除目标分解(销售收入)表", businessType = BusinessType.DELETE)
    @PostMapping("/income/remove")
    public AjaxResult incomeRemove(@RequestBody @Validated(TargetDecomposeDTO.DeleteTargetDecomposeDTO.class)TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.logicDeleteIncomeTargetDecomposeByTargetDecomposeId(targetDecomposeDTO));
    }

    /**
     * 逻辑删除目标分解(销售回款)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:remove")
    //@Log(title = "删除目标分解(销售回款)表", businessType = BusinessType.DELETE)
    @PostMapping("/returned/remove")
    public AjaxResult returnedRemove(@RequestBody @Validated(TargetDecomposeDTO.DeleteTargetDecomposeDTO.class)TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.logicDeleteReturnedTargetDecomposeByTargetDecomposeId(targetDecomposeDTO));
    }

    /**
     * 逻辑删除目标分解(自定义)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:remove")
    //@Log(title = "删除目标分解(自定义)表", businessType = BusinessType.DELETE)
    @PostMapping("/custom/remove")
    public AjaxResult customRemove(@RequestBody @Validated(TargetDecomposeDTO.DeleteTargetDecomposeDTO.class)TargetDecomposeDTO targetDecomposeDTO) {
        return toAjax(targetDecomposeService.logicDeleteCustomTargetDecomposeByTargetDecomposeId(targetDecomposeDTO));
    }

    /**
     * 逻辑批量删除目标分解(销售订单)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:removes")
    //@Log(title = "批量删除目标分解(销售订单)表", businessType = BusinessType.DELETE)
    @PostMapping("/order/removes")
    public AjaxResult orderRemoves(@RequestBody List<Long> targetDecomposeIds) {
        return toAjax(targetDecomposeService.logicDeleteOrderTargetDecomposeByTargetDecomposeIds(targetDecomposeIds));
    }

    /**
     * 逻辑批量删除目标分解(销售收入)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:removes")
    //@Log(title = "批量删除目标分解(销售收入)表", businessType = BusinessType.DELETE)
    @PostMapping("/income/removes")
    public AjaxResult incomeRemoves(@RequestBody List<Long> targetDecomposeIds) {
        return toAjax(targetDecomposeService.logicDeleteIncomeTargetDecomposeByTargetDecomposeIds(targetDecomposeIds));
    }

    /**
     * 逻辑批量删除目标分解(销售回款)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:removes")
    //@Log(title = "批量删除目标分解(销售回款)表", businessType = BusinessType.DELETE)
    @PostMapping("/returned/removes")
    public AjaxResult returnedRemoves(@RequestBody List<Long> targetDecomposeIds) {
        return toAjax(targetDecomposeService.logicDeleteReturnedTargetDecomposeByTargetDecomposeIds(targetDecomposeIds));
    }

    /**
     * 逻辑批量删除目标分解(自定义)表
     */
    //@RequiresPermissions("operate:cloud:targetDecompose:removes")
    //@Log(title = "批量删除目标分解(销售回款)表", businessType = BusinessType.DELETE)
    @PostMapping("/custom/removes")
    public AjaxResult customRemoves(@RequestBody List<Long> targetDecomposeIds) {
        return toAjax(targetDecomposeService.logicDeleteCustomTargetDecomposeByTargetDecomposeIds(targetDecomposeIds));
    }

    /**
     * 目标分解导出列表模板Excel
     */
    @SneakyThrows
    @GetMapping("/export-template")
    public void importTargetDecompose(@RequestParam Map<String, Object> targetDecompose, TargetDecomposeDTO targetDecomposeDTO, HttpServletResponse response) {
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
                .doWrite(new ArrayList<>());
    }
    /**
     * 解析Excel
     */
    @PostMapping("/excelParseObject")
    public AjaxResult excelParseObject(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        return AjaxResult.success(targetDecomposeService.excelParseObject(file));
    }

    /**
     * 目标分解(销售订单)导出列表Excel
     */
    @SneakyThrows
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

    /**
     * 目标分解(销售收入)导出列表Excel
     */
    @SneakyThrows
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

    /**
     * 目标分解(销售回款)导出列表Excel
     */
    @SneakyThrows
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

    /**
     * 目标分解(自定义)导出列表Excel
     */
    @SneakyThrows
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



}
