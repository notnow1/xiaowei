package net.qixiaowei.operate.cloud.controller.salary;

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
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.excel.salary.SalaryPayExcel;
import net.qixiaowei.operate.cloud.excel.salary.SalaryPayImportListener;
import net.qixiaowei.operate.cloud.service.salary.ISalaryPayService;
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
 * @author Graves
 * @since 2022-11-17
 */
@RestController
@RequestMapping("salaryPay")
public class SalaryPayController extends BaseController {


    @Autowired
    private ISalaryPayService salaryPayService;


    /**
     * 查询工资发薪表详情
     */
    //@RequiresPermissions("operate:cloud:salaryPay:info")
    @GetMapping("/info/{salaryPayId}")
    public AjaxResult info(@PathVariable Long salaryPayId) {
        SalaryPayDTO salaryPayDTO = salaryPayService.selectSalaryPayBySalaryPayId(salaryPayId);
        return AjaxResult.success(salaryPayDTO);
    }

    /**
     * 分页查询工资发薪表列表
     */
    //@RequiresPermissions("operate:cloud:salaryPay:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(SalaryPayDTO salaryPayDTO) {
        startPage();
        List<SalaryPayDTO> list = salaryPayService.selectSalaryPayList(salaryPayDTO);
        return getDataTable(list);
    }

    /**
     * 查询工资发薪表列表
     */
    //@RequiresPermissions("operate:cloud:salaryPay:list")
    @GetMapping("/list")
    public AjaxResult list(SalaryPayDTO salaryPayDTO) {
        List<SalaryPayDTO> list = salaryPayService.selectSalaryPayList(salaryPayDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增工资发薪表
     */
    //@RequiresPermissions("operate:cloud:salaryPay:add")
    @Log(title = "新增工资发薪表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody SalaryPayDTO salaryPayDTO) {
        return AjaxResult.success(salaryPayService.insertSalaryPay(salaryPayDTO));
    }


    /**
     * 修改工资发薪表
     */
    //@RequiresPermissions("operate:cloud:salaryPay:edit")
    @Log(title = "修改工资发薪表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody SalaryPayDTO salaryPayDTO) {
        return toAjax(salaryPayService.updateSalaryPay(salaryPayDTO));
    }

    /**
     * 逻辑删除工资发薪表
     */
    //@RequiresPermissions("operate:cloud:salaryPay:remove")
    @Log(title = "删除工资发薪表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody SalaryPayDTO salaryPayDTO) {
        return toAjax(salaryPayService.logicDeleteSalaryPayBySalaryPayId(salaryPayDTO));
    }

    /**
     * 批量修改工资发薪表
     */
    //@RequiresPermissions("operate:cloud:salaryPay:edits")
    @Log(title = "批量修改工资发薪表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<SalaryPayDTO> salaryPayDtos) {
        return toAjax(salaryPayService.updateSalaryPays(salaryPayDtos));
    }

    /**
     * 批量新增工资发薪表
     */
    //@RequiresPermissions("operate:cloud:salaryPay:insertSalaryPays")
    @Log(title = "批量新增工资发薪表", businessType = BusinessType.INSERT)
    @PostMapping("/insertSalaryPays")
    public AjaxResult insertSalaryPays(@RequestBody List<SalaryPayDTO> salaryPayDtos) {
        return toAjax(salaryPayService.insertSalaryPays(salaryPayDtos));
    }

    /**
     * 逻辑批量删除工资发薪表
     */
    //@RequiresPermissions("operate:cloud:salaryPay:removes")
    @Log(title = "批量删除工资发薪表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> salaryPayIds) {
        return toAjax(salaryPayService.logicDeleteSalaryPayBySalaryPayIds(salaryPayIds));
    }

    /**
     * 导入工资发薪表
     */
    @PostMapping("import")
    public AjaxResult importSalaryPay(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        InputStream inputStream;
        try {
            SalaryPayImportListener importListener = new SalaryPayImportListener(salaryPayService);
            inputStream = new BufferedInputStream(file.getInputStream());
            ExcelReaderBuilder builder = EasyExcel.read(inputStream, SalaryPayExcel.class, importListener);
            builder.doReadAll();
        } catch (IOException e) {
            throw new ServiceException("导入工资发薪表Excel失败");
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 导出工资发薪表
     */
    @SneakyThrows
    @GetMapping("export")
    public void exportSalaryPay(@RequestParam Map<String, Object> salaryPay, SalaryPayDTO salaryPayDTO, HttpServletResponse response) {
        List<SalaryPayExcel> salaryPayExcelList = salaryPayService.exportSalaryPay(salaryPayDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("工资发薪表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), SalaryPayExcel.class).sheet("工资发薪表").doWrite(salaryPayExcelList);
    }
}