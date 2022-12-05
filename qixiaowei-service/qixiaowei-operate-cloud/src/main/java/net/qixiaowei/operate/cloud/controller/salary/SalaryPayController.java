package net.qixiaowei.operate.cloud.controller.salary;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.excel.SelectSheetWriteHandler;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryStructureDTO;
import net.qixiaowei.operate.cloud.excel.salary.SalaryPayExcel;
import net.qixiaowei.operate.cloud.excel.salary.SalaryPayImportListener;
import net.qixiaowei.operate.cloud.service.salary.ISalaryItemService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryPayDetailsService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Graves
 * @since 2022-11-17
 */
@RestController
@RequestMapping("salaryPay")
public class SalaryPayController extends BaseController {


    @Autowired
    private ISalaryPayService salaryPayService;

    @Autowired
    private ISalaryItemService salaryItemService;

    @Autowired
    private ISalaryPayDetailsService salaryPayDetailsService;


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
     * 查询薪酬架构报表列表
     */
    //@RequiresPermissions("operate:cloud:salaryPay:list")
    @GetMapping("/structure")
    public AjaxResult structure(SalaryStructureDTO salaryStructureDTO) {
        return AjaxResult.success(salaryPayService.selectSalaryPayStructure(salaryStructureDTO));
    }

    /**
     * 查询薪酬架构报表列表
     */
    //@RequiresPermissions("operate:cloud:salaryPay:list")
    @GetMapping("/pageList/structure")
    public AjaxResult listStructure(SalaryStructureDTO salaryStructureDTO) {
        return AjaxResult.success(salaryPayService.selectSalaryPayStructureList(salaryStructureDTO));
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
    public AjaxResult importSalaryPay(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        try {
            //构建读取器
            ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
            read.sheet()
                    .registerReadListener(new SalaryPayImportListener(salaryPayService)).doRead();
        } catch (IOException e) {
            throw new ServiceException("导入人员信息配置Excel失败");
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 导出工资发薪表
     */
    @SneakyThrows
    @PostMapping("export")
    public void exportSalaryPay(@RequestBody SalaryPayDTO salaryPayDTO, HttpServletResponse response) {
        List<SalaryPayDTO> salaryPayDTOS = salaryPayService.selectSalaryPayBySalaryPay(salaryPayDTO.getIsSelect(), salaryPayDTO.getSalaryPayIds());
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("工资发薪表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .sheet("工资发薪表")// 设置 sheet 的名字
                // 自适应列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(SalaryPayImportListener.dataList(salaryPayDTOS, salaryPayDetailsService, salaryItemService));
    }

    /**
     * 导出工资发薪模板
     */
    @SneakyThrows
//    @RequiresPermissions("operate:cloud:targetSetting:list")
    @GetMapping("/export-template")
    public void exportTemplate(@RequestParam Map<String, Object> salaryPay, SalaryPayExcel salaryPayExcel, HttpServletResponse response) {
        try {
            List<SalaryItemDTO> salaryItemDTOS = salaryItemService.selectSalaryItemList(new SalaryItemDTO());
            List<List<String>> headTemplate = SalaryPayImportListener.headTemplate(salaryItemDTOS);
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(CharsetKit.UTF_8);
            String fileName = URLEncoder.encode("经营云-月度工资数据管理导入" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                    , CharsetKit.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            WriteSheet sheet = EasyExcel.writerSheet(0, "Sheet1").head(headTemplate).build();
            excelWriter.write(new ArrayList<>(), sheet);
            excelWriter.finish();
        } catch (IOException e) {
            throw new ServiceException("导出失败");
        }
    }

}
