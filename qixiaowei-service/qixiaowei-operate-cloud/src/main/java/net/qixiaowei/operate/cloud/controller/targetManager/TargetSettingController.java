package net.qixiaowei.operate.cloud.controller.targetManager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingExcel;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingImportListener;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetSettingService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
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


    /**
     * 查询目标制定详情
     */
//    @RequiresPermissions("operate:cloud:targetSetting:info")
    @GetMapping("/info/{targetSettingId}")
    public AjaxResult info(@PathVariable Long targetSettingId) {
        TargetSettingDTO targetSettingDTO = targetSettingService.selectTargetSettingByTargetSettingId(targetSettingId);
        return AjaxResult.success(targetSettingDTO);
    }

    /**
     * 分页查询目标制定列表
     */
//    @RequiresPermissions("operate:cloud:targetSetting:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TargetSettingDTO targetSettingDTO) {
        startPage();
        List<TargetSettingDTO> list = targetSettingService.selectTargetSettingList(targetSettingDTO);
        return getDataTable(list);
    }

    /**
     * 查询目标制定列表
     */
//    @RequiresPermissions("operate:cloud:targetSetting:list")
    @GetMapping("/list")
    public AjaxResult list(TargetSettingDTO targetSettingDTO) {
        List<TargetSettingDTO> list = targetSettingService.selectTargetSettingList(targetSettingDTO);
        return AjaxResult.success(list);
    }

    /**
     * 查询销售订单目标制定列表
     */
//    @RequiresPermissions("operate:cloud:targetSetting:list")
    @GetMapping("/list/order")
    public AjaxResult listOrder(TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectOrderTargetSettingList(targetSettingDTO));
    }

    /**
     * 查询销售收入目标制定列表
     */
//    @RequiresPermissions("operate:cloud:targetSetting:list")
    @GetMapping("/list/income")
    public AjaxResult listIncome(TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectIncomeTargetSettingList(targetSettingDTO));
    }

    /**
     * 查询销售订单目标制定-不带主表玩
     */
//    @RequiresPermissions("operate:cloud:targetSetting:list")
    @GetMapping("/list/orderDrop")
    public AjaxResult listOrderDrop(TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.selectOrderDropTargetSettingList(targetSettingDTO));
    }

    /**
     * 保存销售订单目标制定
     */
//    @RequiresPermissions("operate:cloud:targetSetting:add")
    @Log(title = "保存销售订单目标制定", businessType = BusinessType.UPDATE)
    @PostMapping("/save/order")
    public AjaxResult saveOrder(@RequestBody TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.saveOrderTargetSetting(targetSettingDTO));
    }

    /**
     * 新增目标制定
     */
//    @RequiresPermissions("operate:cloud:targetSetting:add")
    @Log(title = "保存销售收入目标制定", businessType = BusinessType.INSERT)
    @PostMapping("/save/income")
    public AjaxResult saveIncome(@RequestBody TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.saveIncomeTargetSetting(targetSettingDTO));
    }

    /**
     * 新增目标制定
     */
//    @RequiresPermissions("operate:cloud:targetSetting:add")
    @Log(title = "保存销售回款目标制定", businessType = BusinessType.INSERT)
    @PostMapping("/save/recoveries")
    public AjaxResult saveRecoveries(@RequestBody TargetSettingDTO targetSettingDTO) {
        return AjaxResult.success(targetSettingService.insertTargetSetting(targetSettingDTO));
    }


    /**
     * 修改目标制定
     */
//    @RequiresPermissions("operate:cloud:targetSetting:edit")
    @Log(title = "修改目标制定", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody TargetSettingDTO targetSettingDTO) {
        return toAjax(targetSettingService.updateTargetSetting(targetSettingDTO));
    }

    /**
     * 逻辑删除目标制定
     */
//    @RequiresPermissions("operate:cloud:targetSetting:remove")
    @Log(title = "删除目标制定", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody TargetSettingDTO targetSettingDTO) {
        return toAjax(targetSettingService.logicDeleteTargetSettingByTargetSettingId(targetSettingDTO));
    }

    /**
     * 批量修改目标制定
     */
//    @RequiresPermissions("operate:cloud:targetSetting:edits")
    @Log(title = "批量修改目标制定", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<TargetSettingDTO> targetSettingDtos) {
        return toAjax(targetSettingService.updateTargetSettings(targetSettingDtos));
    }

    /**
     * 批量新增目标制定
     */
//    @RequiresPermissions("operate:cloud:targetSetting:insertTargetSettings")
    @Log(title = "批量新增目标制定", businessType = BusinessType.INSERT)
    @PostMapping("/insertTargetSettings")
    public AjaxResult insertTargetSettings(@RequestBody List<TargetSettingDTO> targetSettingDtos) {
        return toAjax(targetSettingService.insertTargetSettings(targetSettingDtos));
    }

    /**
     * 逻辑批量删除目标制定
     */
//    @RequiresPermissions("operate:cloud:targetSetting:removes")
    @Log(title = "批量删除目标制定", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> targetSettingIds) {
        return toAjax(targetSettingService.logicDeleteTargetSettingByTargetSettingIds(targetSettingIds));
    }

    /**
     * 导入目标制定
     */
    @PostMapping("import")
    public AjaxResult importEmployee(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        InputStream inputStream;
        try {
            TargetSettingImportListener importListener = new TargetSettingImportListener(targetSettingService);
            inputStream = new BufferedInputStream(file.getInputStream());
            ExcelReaderBuilder builder = EasyExcel.read(inputStream, TargetSettingExcel.class, importListener);
            builder.doReadAll();
        } catch (IOException e) {
            throw new ServiceException("导入目标制定Excel失败");
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 导出目标制定
     */
    @SneakyThrows
    @GetMapping("export")
    public void exportUser(@RequestParam Map<String, Object> targetSetting, TargetSettingDTO targetSettingDTO, HttpServletResponse response) {
        List<TargetSettingExcel> targetSettingExcelList = targetSettingService.exportTargetSetting(targetSettingDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("目标制定" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TargetSettingExcel.class).sheet("目标制定").doWrite(targetSettingExcelList);
    }
}
