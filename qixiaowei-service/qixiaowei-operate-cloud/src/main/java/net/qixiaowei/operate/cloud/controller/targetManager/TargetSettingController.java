package net.qixiaowei.operate.cloud.controller.targetManager;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.*;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author Graves
 * @since 2022-10-27
 */
@RestController
@RequestMapping("targetSetting")
public class TargetSettingController extends BaseController {


    @Autowired
    private ITargetSettingService targetSettingService;

    //==============================销售订单==============================//

    /**
     * 查询销售订单目标制定列表
     */
    @RequiresPermissions(value = {"operate:cloud:targetSetting:order:info", "operate:cloud:targetSetting:order:save"}, logical = Logical.OR)
    @GetMapping("/info/order")
    public AjaxResult listOrder(TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectOrderTargetSettingList(targetSettingDTO));
    }

    /**
     * 保存销售订单目标制定
     */
    @RequiresPermissions("operate:cloud:targetSetting:order:save")
    @PostMapping("/save/order")
    public AjaxResult saveOrder(@RequestBody @Validated(TargetSettingDTO.UpdateTargetSettingDTO.class) TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.saveOrderTargetSetting(targetSettingDTO));
    }

    /**
     * 导出销售订单目标制定
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetSetting:order:export")
    @GetMapping("/export/order")
    public void exportOrder(@RequestParam Map<String, Object> targetSetting, TargetSettingDTO targetSettingDTO, HttpServletResponse response) {
        List<TargetSettingOrderExcel> targetSettingExcelList = targetSettingService.exportOrderTargetSetting(targetSettingDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("销售订单目标制定" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TargetSettingOrderExcel.class).sheet("销售订单目标制定")
                // 自适应列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(targetSettingExcelList);
    }

    /**
     * 查询销售订单目标制定-历史年度数据
     */
    @RequiresPermissions(value = {"operate:cloud:targetSetting:order:info", "operate:cloud:targetSetting:order:save"}, logical = Logical.OR)
    @GetMapping("/info/orderDrop")
    public AjaxResult listOrderDrop(TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectOrderDropTargetSettingList(targetSettingDTO));
    }


    //==============================销售收入==============================//

    /**
     * 查询销售收入目标制定列表
     */
    @RequiresPermissions(value = {"operate:cloud:targetSetting:income:info", "operate:cloud:targetSetting:income:save"}, logical = Logical.OR)
    @GetMapping("/info/income")
    public AjaxResult listIncome(@RequestParam Integer targetYear) {
        return AjaxResult.success(targetSettingService.selectIncomeTargetSettingList(targetYear));
    }

    /**
     * 新增目标制定
     */
    @RequiresPermissions("operate:cloud:targetSetting:income:save")
    @Log(title = "保存销售收入目标制定", businessType = BusinessType.UPDATE)
    @PostMapping("/save/income")
    public AjaxResult saveIncome(@RequestBody @Validated(TargetSettingDTO.UpdateTargetSettingDTO.class) TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.saveIncomeTargetSetting(targetSettingDTO));
    }

    /**
     * 导出销售收入目标制定
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetSetting:income:export")
    @GetMapping("/export/income")
    public void exportIncome(@RequestParam Map<String, Object> targetSetting, TargetSettingDTO targetSettingDTO, HttpServletResponse response) {
        List<TargetSettingIncomeExcel> targetSettingExcelList = targetSettingService.exportIncomeTargetSetting(targetSettingDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("销售收入目标制定" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TargetSettingIncomeExcel.class)
                // 自适应列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("销售收入目标制定").doWrite(targetSettingExcelList);
    }

    //==============================回款==============================//

    /**
     * 查询销售收入目标制定列表
     */
    @RequiresPermissions(value = {"operate:cloud:targetSetting:recovery:info", "operate:cloud:targetSetting:recovery:save"}, logical = Logical.OR)
    @GetMapping("/info/recovery")
    public AjaxResult listRecovery(TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectRecoveryTargetSettingList(targetSettingDTO));
    }

    /**
     * 新增目标制定
     */
    @RequiresPermissions("operate:cloud:targetSetting:recovery:save")
    @PostMapping("/save/recovery")
    public AjaxResult saveRecoveries(@RequestBody @Validated(TargetSettingDTO.UpdateTargetSettingDTO.class) TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.saveRecoveryTargetSetting(targetSettingDTO));
    }

    /**
     * 导出销售回款目标制定
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetSetting:recovery:export")
    @GetMapping("/export/recovery")
    public void exportRecovery(@RequestParam Map<String, Object> targetSetting, TargetSettingDTO targetSettingDTO, HttpServletResponse response) {
        List<List<String>> headRecovery = TargetSettingImportListener.headRecovery();
        List<TargetSettingRecoveriesExcel> targetSettingRecoveriesExcels = targetSettingService.exportRecoveryTargetSetting(targetSettingDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("销售回款目标制定" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(headRecovery)
                .sheet("销售回款目标制定")// 设置 sheet 的名字
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())// 自适应列宽
                .doWrite(TargetSettingImportListener.dataList(targetSettingRecoveriesExcels));
    }

    //==============================经营目标==============================//

    /**
     * 分页查询目标制定列表
     */
    @RequiresPermissions(value = {"operate:cloud:targetSetting:info", "operate:cloud:targetSetting:edit"}, logical = Logical.OR)
    @GetMapping("/treeList")
    public AjaxResult treeList(TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectTargetSettingTreeList(targetSettingDTO));
    }

    /**
     * 修改目标制定
     */
    @RequiresPermissions("operate:cloud:targetSetting:edit")
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<TargetSettingDTO> targetSettingDTOS) {
        return AjaxResult.success(targetSettingService.saveTargetSettings(targetSettingDTOS));
    }

    /**
     * 添加指标时-获取指标列表
     */
    @RequiresPermissions(value = {"operate:cloud:targetSetting:info", "operate:cloud:targetSetting:edit"}, logical = Logical.OR)
    @GetMapping("/indicator")
    public AjaxResult indicator(TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectIndicatorList(targetSettingDTO));
    }

    /**
     * 添加指标时-获取指标树列表
     */
    @RequiresPermissions(value = {"operate:cloud:targetSetting:info", "operate:cloud:targetSetting:edit"}, logical = Logical.OR)
    @GetMapping("/indicatorTree")
    public AjaxResult indicatorTree(TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectIndicatorTree(targetSettingDTO));
    }

    /**
     * 导出目标制定模板
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetSetting:edit")
    @GetMapping("/export-template")
    public void exportTemplate(@RequestParam Map<String, Object> targetSetting, TargetSettingDTO targetSettingDTO, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(CharsetKit.UTF_8);
            String fileName = URLEncoder.encode("经营云-关键经营目标制定导入" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                    , CharsetKit.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            WriteSheet sheet = EasyExcel.writerSheet(0, "关键经营目标制定").head(TargetSettingExcel.class).build();
            excelWriter.write(new ArrayList<>(), sheet);
            excelWriter.finish();
        } catch (IOException e) {
            throw new ServiceException("导出失败");
        }
    }

    /**
     * 解析目标制定
     */
    @RequiresPermissions("operate:cloud:targetSetting:edit")
    @PostMapping("/excelParseObject")
    public AjaxResult importEmployee(MultipartFile file, Integer targetYear) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        try {
            TargetSettingImportListener<TargetSettingExcel> listener = new TargetSettingImportListener<>(1);
            Map<String, List<TargetSettingExcel>> targetSettingExcelMaps = listener.getData(file.getInputStream(), TargetSettingExcel.class);
            List<TargetSettingDTO> targetSettingDTOS = targetSettingService.importTargetSetting(targetSettingExcelMaps, targetYear);
            return AjaxResult.success(targetSettingDTOS);
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }
    }

    /**
     * 导出目标制定
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetSetting:export")
    @GetMapping("/export")
    public void export(@RequestParam Map<String, Object> targetSetting, TargetSettingDTO targetSettingDTO, HttpServletResponse response) {
        List<List<TargetSettingExcel>> targetSettingExcelList = targetSettingService.exportTargetSetting(targetSettingDTO);
//        List<List<String>> head = TargetSettingImportListener.head();
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(CharsetKit.UTF_8);
            String fileName = URLEncoder.encode("目标制定" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                    , CharsetKit.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            for (int i = 0; i < targetSettingExcelList.size(); i++) {
                Integer targetYear = targetSettingExcelList.get(i).get(0).getTargetYear();
                WriteSheet sheet = EasyExcel.writerSheet(i, targetYear + "年").head(TargetSettingExcel.class).build();
                excelWriter.write(targetSettingExcelList.get(i), sheet);
            }
            excelWriter.finish();
        } catch (IOException e) {
            throw new ServiceException("导出失败");
        }
    }

    //==============================经营结果分析报表==================================//

    /**
     * 查询经营分析报表列表
     */
    @RequiresPermissions("operate:cloud:targetSetting:analyse")
    @GetMapping("/analyse/list")
    public AjaxResult analyseList(TargetSettingDTO targetSettingDTO) {
        List<TargetSettingDTO> list = targetSettingService.analyseList(targetSettingDTO);
        return AjaxResult.success(list);
    }

    //==============================其他==============================//

    /**
     * 逻辑删除目标制定
     */
    @RequiresPermissions("operate:cloud:targetSetting:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody TargetSettingDTO targetSettingDTO) {
        return toAjax(targetSettingService.logicDeleteTargetSettingByTargetSettingId(targetSettingDTO));
    }


}
