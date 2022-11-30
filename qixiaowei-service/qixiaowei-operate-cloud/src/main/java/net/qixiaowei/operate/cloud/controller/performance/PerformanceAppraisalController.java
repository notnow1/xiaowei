package net.qixiaowei.operate.cloud.controller.performance;

import com.alibaba.excel.EasyExcel;
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
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalColumnsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalExcel;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalImportListener;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalColumnsService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Graves
 * @since 2022-11-23
 */
@RestController
@RequestMapping("performanceAppraisal")
public class PerformanceAppraisalController extends BaseController {


    @Autowired
    private IPerformanceAppraisalService performanceAppraisalService;

    @Autowired
    private IPerformanceAppraisalColumnsService performanceAppraisalColumnsService;


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
    @GetMapping("/pageList/orgArchive")
    public TableDataInfo listOrgArchive(PerformanceAppraisalDTO performanceAppraisalDTO) {
        startPage();
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectOrgAppraisalArchiveList(performanceAppraisalDTO);
        return getDataTable(list);
    }

    /**
     * 查询绩效考核表列表
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @GetMapping("/pageList/perArchive")
    public TableDataInfo listPerArchive(PerformanceAppraisalDTO performanceAppraisalDTO) {
        startPage();
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectPerAppraisalArchiveList(performanceAppraisalDTO);
        return getDataTable(list);
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
     * 查询绩效考核表列表
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @GetMapping("/info/perArchive/{performanceAppraisalId}")
    public AjaxResult infoPerArchive(@PathVariable Long performanceAppraisalId) {
        return AjaxResult.success(performanceAppraisalService.selectPerAppraisalArchiveById(performanceAppraisalId));
    }

    /**
     * 查询组织绩效结果排名
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @PostMapping("/list/orgRank")
    public AjaxResult listOrgRank(@RequestBody Map<String, List<Long>> map) {
        List<Long> appraisalObjectsIds = map.get("appraisalObjectsIds");
        Long performanceAppraisalId = map.get("performanceAppraisalId").get(0);
        return AjaxResult.success(performanceAppraisalService.selectOrgAppraisalRankByDTO(appraisalObjectsIds, performanceAppraisalId));
    }

    /**
     * 查询绩效考核表列表
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @GetMapping("/archive/{performanceAppraisalId}")
    public AjaxResult archive(@PathVariable Long performanceAppraisalId) {
        return toAjax(performanceAppraisalService.archive(performanceAppraisalId));
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
//        try {
//            PerformanceAppraisalImportListener importListener = new PerformanceAppraisalImportListener(1);
//            inputStream = new BufferedInputStream(file.getInputStream());
//            ExcelReaderBuilder builder = EasyExcel.read(inputStream, PerformanceAppraisalExcel.class, importListener);
//            builder.doReadAll();
//        } catch (IOException e) {
//            throw new ServiceException("导入绩效考核表Excel失败");
//        }
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

    /**
     * 导入组织绩效归档表
     */
    @PostMapping("orgImport")
    public AjaxResult importOrgPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file) {
        Integer importType = performanceAppraisalDTO.getImportType();
        if (importType.equals(1)) {
            performanceAppraisalService.importSysOrgPerformanceAppraisal(performanceAppraisalDTO, file);
        } else {
            performanceAppraisalService.importCustomOrgPerformanceAppraisal(performanceAppraisalDTO, file);
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 导入个人绩效归档表
     */
    @PostMapping("perImport")
    public AjaxResult importPerPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file) {
        Integer importType = performanceAppraisalDTO.getImportType();
        if (importType.equals(1)) {
            performanceAppraisalService.importSysPerPerformanceAppraisal(performanceAppraisalDTO, file);
        } else {
            performanceAppraisalService.importCustomPerPerformanceAppraisal(performanceAppraisalDTO, file);
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 导出组织模板表
     */
    @SneakyThrows
    @GetMapping("export-org-template")
    public void exportOrgTemplate(@RequestParam Integer importType, @RequestParam Long performanceAppraisalId, HttpServletResponse response) {
        if (StringUtils.isNull(importType)) {
            throw new ServiceException("请选择考核流程");
        }
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalService.selectPerformanceRankFactor(performanceAppraisalId);
        if (StringUtils.isEmpty(performanceRankFactorDTOS)) {
            throw new ServiceException("当前的绩效等级不存在 请联系管理员");
        }
        Map<Integer, List<String>> selectMap = new HashMap<>();
        List<List<String>> head;
        String fileName;
        if (importType.equals(1)) {//系统流程
            head = PerformanceAppraisalImportListener.headOrgSystemTemplate(selectMap, performanceRankFactorDTOS);
            fileName = URLEncoder.encode("组织绩效归档导入系统模板" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
        } else {
            head = PerformanceAppraisalImportListener.headOrgCustomTemplate(selectMap, performanceRankFactorDTOS);
            fileName = URLEncoder.encode("组织绩效归档导入自定义模板" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalService.selectOrgAppraisalObjectList(performanceAppraisalId);
        EasyExcel.write(response.getOutputStream())
                .head(head)
                .sheet("Sheet1")// 设置 sheet 的名字
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap))
//                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) 自定义表头
                .doWrite(PerformanceAppraisalImportListener.dataTemplateList(performanceAppraisalObjectsDTOList));
    }

    /**
     * 导出个人模板表
     */
    @SneakyThrows
    @GetMapping("export-per-template")
    public void exportPerTemplate(@RequestParam Integer importType, @RequestParam Long performanceAppraisalId, HttpServletResponse response) {
        if (StringUtils.isNull(importType)) {
            throw new ServiceException("请选择考核流程");
        }
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalService.selectPerformanceRankFactor(performanceAppraisalId);
        if (StringUtils.isEmpty(performanceRankFactorDTOS)) {
            throw new ServiceException("当前的绩效等级不存在 请联系管理员");
        }
        Map<Integer, List<String>> selectMap = new HashMap<>();
        List<List<String>> head;
        String fileName;
        if (importType.equals(1)) {//系统流程
            head = PerformanceAppraisalImportListener.headPerSystemTemplate(selectMap, performanceRankFactorDTOS);
            fileName = URLEncoder.encode("个人绩效归档导入系统模板" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
        } else {
            head = PerformanceAppraisalImportListener.headPerCustomTemplate(selectMap, performanceRankFactorDTOS);
            fileName = URLEncoder.encode("个人绩效归档导入自定义模板" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalService.selectPerAppraisalObjectList(performanceAppraisalId);
        EasyExcel.write(response.getOutputStream())
                .head(head)
                .sheet("Sheet1")// 设置 sheet 的名字
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap))
//                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) 自定义表头
                .doWrite(PerformanceAppraisalImportListener.dataTemplateList(performanceAppraisalObjectsDTOList));
    }

    /**
     * 导出组织模板表
     */
    @SneakyThrows
    @GetMapping("exportOrg")
    public void exportOrg(@RequestParam Long performanceAppraisalId, HttpServletResponse response) {
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalService.selectPerformanceRankFactor(performanceAppraisalId);
        if (StringUtils.isEmpty(performanceRankFactorDTOS)) {
            throw new ServiceException("当前的绩效等级不存在 请联系管理员,检查绩效等级");
        }
        Integer selfDefinedColumnsFlag = performanceRankFactorDTOS.get(0).getSelfDefinedColumnsFlag();
        Map<Integer, List<String>> selectMap = new HashMap<>();
        List<List<String>> head;
        String fileName;
        Collection<List<Object>> lists;
        if (selfDefinedColumnsFlag == 0) {
            head = PerformanceAppraisalImportListener.headOrgSystemTemplate(selectMap, performanceRankFactorDTOS);
            lists = performanceAppraisalService.dataOrgSysList(performanceAppraisalId, performanceRankFactorDTOS);
        } else {
            List<PerformanceAppraisalColumnsDTO> appraisalColumnsDTOList = performanceAppraisalColumnsService.selectAppraisalColumnsByAppraisalId(performanceAppraisalId);
            head = PerformanceAppraisalImportListener.headOrgCustom(selectMap, performanceRankFactorDTOS, appraisalColumnsDTOList);
            lists = performanceAppraisalService.dataOrgCustomList(performanceAppraisalId, performanceRankFactorDTOS);
        }
        fileName = URLEncoder.encode("组织绩效归档导出" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(head)
                .sheet("Sheet1")// 设置 sheet 的名字
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap))
//                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) 自定义表头
                .doWrite(lists);
    }

    /**
     * 导出个人模板表
     */
    @SneakyThrows
    @GetMapping("exportPer")
    public void exportPer(@RequestParam Long performanceAppraisalId, HttpServletResponse response) {
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalService.selectPerformanceRankFactor(performanceAppraisalId);
        if (StringUtils.isEmpty(performanceRankFactorDTOS)) {
            throw new ServiceException("当前的绩效等级不存在 请联系管理员,检查绩效等级");
        }
        Integer selfDefinedColumnsFlag = performanceRankFactorDTOS.get(0).getSelfDefinedColumnsFlag();
        Map<Integer, List<String>> selectMap = new HashMap<>();
        List<List<String>> head;
        String fileName;
        Collection<List<Object>> lists;
        if (selfDefinedColumnsFlag == 0) {
            head = PerformanceAppraisalImportListener.headPerSystemTemplate(selectMap, performanceRankFactorDTOS);
            lists = performanceAppraisalService.dataPerSysList(performanceAppraisalId, performanceRankFactorDTOS);
        } else {
            List<PerformanceAppraisalColumnsDTO> appraisalColumnsDTOList = performanceAppraisalColumnsService.selectAppraisalColumnsByAppraisalId(performanceAppraisalId);
            head = PerformanceAppraisalImportListener.headPerCustom(selectMap, performanceRankFactorDTOS, appraisalColumnsDTOList);
            lists = performanceAppraisalService.dataPerCustomList(performanceAppraisalId, performanceRankFactorDTOS);
        }
        fileName = URLEncoder.encode("组织绩效归档导出" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000), CharsetKit.UTF_8);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(head)
                .sheet("Sheet1")// 设置 sheet 的名字
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap))
//                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) 自定义表头
                .doWrite(lists);
    }
}
