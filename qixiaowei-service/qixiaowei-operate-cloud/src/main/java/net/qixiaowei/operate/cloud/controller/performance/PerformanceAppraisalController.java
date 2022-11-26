package net.qixiaowei.operate.cloud.controller.performance;

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
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalExcel;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalImportListener;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
 * @since 2022-11-23
 */
@RestController
@RequestMapping("performanceAppraisal")
public class PerformanceAppraisalController extends BaseController {


    @Autowired
    private IPerformanceAppraisalService performanceAppraisalService;


    /**
     * 查询绩效考核表详情
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:info")
    @GetMapping("/info/{performanceAppraisalId}")
    public AjaxResult info(@PathVariable Long performanceAppraisalId) {
        PerformanceAppraisalDTO performanceAppraisalDTO = performanceAppraisalService.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        return AjaxResult.success(performanceAppraisalDTO);
    }

    /**
     * 分页查询绩效考核表列表
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(PerformanceAppraisalDTO performanceAppraisalDTO) {
        startPage();
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectPerformanceAppraisalList(performanceAppraisalDTO);
        return getDataTable(list);
    }

    /**
     * 查询绩效考核表列表
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @GetMapping("/list")
    public AjaxResult list(PerformanceAppraisalDTO performanceAppraisalDTO) {
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectPerformanceAppraisalList(performanceAppraisalDTO);
        return AjaxResult.success(list);
    }

    /**
     * 查询绩效考核表列表
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @GetMapping("/list/orgArchive")
    public AjaxResult listArchive(PerformanceAppraisalDTO performanceAppraisalDTO) {
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectOrgAppraisalArchiveList(performanceAppraisalDTO);
        return AjaxResult.success(list);
    }

    /**
     * 查询绩效考核表列表
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @GetMapping("/info/orgArchive/{performanceAppraisalId}")
    public AjaxResult infoOrgArchive(@PathVariable Long performanceAppraisalId) {
        return AjaxResult.success(performanceAppraisalService.selectOrgAppraisalArchiveById(performanceAppraisalId));
    }

    /**
     * 查询绩效结果排名
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @GetMapping("/info/orgRank/{performanceAppraisalId}")
    public AjaxResult infoOrgRank(@PathVariable Long performanceAppraisalId) {
        return AjaxResult.success(performanceAppraisalService.selectOrgAppraisalArchiveById(performanceAppraisalId));
    }


    /**
     * 新增绩效考核表
     */
    ////@RequiresPermissions("operate:cloud:performanceAppraisal:add")
    @Log(title = "新增绩效考核表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(PerformanceAppraisalDTO.AddPerformanceAppraisalDTO.class) PerformanceAppraisalDTO performanceAppraisalDTO) {
        return AjaxResult.success(performanceAppraisalService.insertPerformanceAppraisal(performanceAppraisalDTO));
    }

    /**
     * 逻辑删除绩效考核表
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:remove")
    @Log(title = "删除绩效考核表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody PerformanceAppraisalDTO performanceAppraisalDTO) {
        return toAjax(performanceAppraisalService.logicDeletePerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalDTO));
    }

    /**
     * 逻辑批量删除绩效考核表
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:removes")
    @Log(title = "批量删除绩效考核表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> performanceAppraisalIds) {
        return toAjax(performanceAppraisalService.logicDeletePerformanceAppraisalByPerformanceAppraisalIds(performanceAppraisalIds));
    }

    /**
     * 导入绩效考核表
     */
    @PostMapping("import")
    public AjaxResult importPerformanceAppraisal(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        InputStream inputStream;
        try {
            PerformanceAppraisalImportListener importListener = new PerformanceAppraisalImportListener(performanceAppraisalService);
            inputStream = new BufferedInputStream(file.getInputStream());
            ExcelReaderBuilder builder = EasyExcel.read(inputStream, PerformanceAppraisalExcel.class, importListener);
            builder.doReadAll();
        } catch (IOException e) {
            throw new ServiceException("导入绩效考核表Excel失败");
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 导出绩效考核表
     */
    @SneakyThrows
    @GetMapping("export")
    public void exportPerformanceAppraisal(@RequestParam Map<String, Object> performanceAppraisal, PerformanceAppraisalDTO performanceAppraisalDTO, HttpServletResponse response) {
        List<PerformanceAppraisalExcel> performanceAppraisalExcelList = performanceAppraisalService.exportPerformanceAppraisal(performanceAppraisalDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("绩效考核表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), PerformanceAppraisalExcel.class).sheet("绩效考核表").doWrite(performanceAppraisalExcelList);
    }
}
