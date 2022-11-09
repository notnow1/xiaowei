package net.qixiaowei.operate.cloud.controller.targetManager;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetOutcomeExcel;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetOutcomeImportListener;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2022-11-07
 */
@RestController
@RequestMapping("targetOutcome")
public class TargetOutcomeController extends BaseController {


    @Autowired
    private ITargetOutcomeService targetOutcomeService;


    /**
     * 查询目标结果表详情
     */
    //@RequiresPermissions("operate:cloud:targetOutcome:info")
    @GetMapping("/info/{targetOutcomeId}")
    public AjaxResult info(@PathVariable Long targetOutcomeId) {
        TargetOutcomeDTO targetOutcomeDTO = targetOutcomeService.selectTargetOutcomeByTargetOutcomeId(targetOutcomeId);
        return AjaxResult.success(targetOutcomeDTO);
    }

    /**
     * 分页查询目标结果表列表
     */
    //@RequiresPermissions("operate:cloud:targetOutcome:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TargetOutcomeDTO targetOutcomeDTO) {
        startPage();
        List<TargetOutcomeDTO> list = targetOutcomeService.selectTargetOutcomeList(targetOutcomeDTO);
        return getDataTable(list);
    }

    /**
     * 查询目标结果表列表
     */
    //@RequiresPermissions("operate:cloud:targetOutcome:list")
    @GetMapping("/list")
    public AjaxResult list(TargetOutcomeDTO targetOutcomeDTO) {
        List<TargetOutcomeDTO> list = targetOutcomeService.selectTargetOutcomeList(targetOutcomeDTO);
        return AjaxResult.success(list);
    }

    /**
     * 修改目标结果表
     */
    //@RequiresPermissions("operate:cloud:targetOutcome:edit")
    @Log(title = "修改目标结果表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody TargetOutcomeDTO targetOutcomeDTO) {
        return toAjax(targetOutcomeService.updateTargetOutcome(targetOutcomeDTO));
    }

    /**
     * 导入目标结果表
     */
    @PostMapping("import")
    public AjaxResult importTargetOutcome(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        InputStream inputStream;
        try {
            TargetOutcomeImportListener importListener = new TargetOutcomeImportListener(targetOutcomeService);
            inputStream = new BufferedInputStream(file.getInputStream());
            ExcelReaderBuilder builder = EasyExcel.read(inputStream, TargetOutcomeExcel.class, importListener);
            builder.doReadAll();
        } catch (IOException e) {
            throw new ServiceException("导入目标结果表Excel失败");
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 导出目标结果表
     */
    @SneakyThrows
    @GetMapping("export")
    public void exportTargetOutcome(@RequestParam Map<String, Object> targetOutcome, TargetOutcomeDTO targetOutcomeDTO, HttpServletResponse response) {
        List<TargetOutcomeExcel> targetOutcomeExcelList = targetOutcomeService.exportTargetOutcome(targetOutcomeDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("目标结果表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TargetOutcomeExcel.class).sheet("目标结果表").doWrite(targetOutcomeExcelList);
    }
}
