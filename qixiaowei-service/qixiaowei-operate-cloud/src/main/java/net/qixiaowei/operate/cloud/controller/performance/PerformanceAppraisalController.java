package net.qixiaowei.operate.cloud.controller.performance;

import com.alibaba.excel.EasyExcel;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.excel.SelectSheetWriteHandler;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalColumnsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalImportListener;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalColumnsService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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

    //==============================绩效考核任务==================================//

    /**
     * 分页查询绩效考核表列表
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(PerformanceAppraisalDTO performanceAppraisalDTO) {
        startPage();
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectPerformanceAppraisalList(performanceAppraisalDTO);
        return getDataTable(list);
    }

    /**
     * 新增绩效考核表
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(PerformanceAppraisalDTO.AddPerformanceAppraisalDTO.class) PerformanceAppraisalDTO performanceAppraisalDTO) {
        return AjaxResult.success(performanceAppraisalService.insertPerformanceAppraisal(performanceAppraisalDTO));
    }

    /**
     * 查询绩效考核表详情
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:info")
    @GetMapping("/info/{performanceAppraisalId}")
    public AjaxResult info(@PathVariable Long performanceAppraisalId) {
        PerformanceAppraisalDTO performanceAppraisalDTO = performanceAppraisalService.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        return AjaxResult.success(performanceAppraisalDTO);
    }

    /**
     * 逻辑删除绩效考核表
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody PerformanceAppraisalDTO performanceAppraisalDTO) {
        return toAjax(performanceAppraisalService.logicDeletePerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalDTO));
    }

    /**
     * 逻辑批量删除绩效考核表
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> performanceAppraisalIds) {
        return toAjax(performanceAppraisalService.logicDeletePerformanceAppraisalByPerformanceAppraisalIds(performanceAppraisalIds));
    }

    //==============================组织绩效管理==================================//
    //==============================组织绩效制定==================================//

    /**
     * 查询绩效考核表列表-组织-制定
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgDevelop:pageList")
    @GetMapping("/orgDevelop/pageList")
    public TableDataInfo listOrgDevelop(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        startPage();
        List<PerformanceAppraisalObjectsDTO> list = performanceAppraisalService.selectOrgAppraisalDevelopList(performanceAppraisalObjectsDTO);
        return getDataTable(list);
    }

    /**
     * 编辑组织绩效考核制定表
     */
    @Log(title = "保存组织绩效制定", businessType = BusinessType.PERFORMANCE_APPRAISAL_ORG_SETTING, businessId = "performAppraisalObjectsId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgDevelop:edit")
    @PostMapping("/orgDevelop/edit")
    public AjaxResult editOrgDevelop(@RequestBody @Validated(PerformanceAppraisalObjectsDTO.UpdatePerformanceAppraisalObjectsDTO.class) PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        return AjaxResult.success(performanceAppraisalService.updateOrgDevelopPerformanceAppraisal(performanceAppraisalObjectsDTO));
    }

    /**
     * 查询组织绩效考核表详情-制定
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgDevelop:info")
    @GetMapping("/orgDevelop/info/{performAppraisalObjectsId}")
    public AjaxResult infoOrgDevelop(@PathVariable Long performAppraisalObjectsId) {
        return AjaxResult.success(performanceAppraisalService.selectOrgAppraisalDevelopById(performAppraisalObjectsId));
    }
    //==============================组织绩效评议==================================//

    /**
     * 查询绩效考核表列表-组织-评议
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgReview:pageList")
    @GetMapping("/orgReview/pageList")
    public TableDataInfo listOrgReview(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        startPage();
        List<PerformanceAppraisalObjectsDTO> list = performanceAppraisalService.selectOrgAppraisalReviewList(performanceAppraisalObjectsDTO);
        return getDataTable(list);
    }

    /**
     * 编辑组织绩效考核评议表
     */
    @Log(title = "保存组织绩效评议", businessType = BusinessType.PERFORMANCE_APPRAISAL_ORG_REVIEW, businessId = "performAppraisalObjectsId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgReview:edit")
    @PostMapping("/orgReview/edit")
    public AjaxResult editOrgReview(@RequestBody @Validated(PerformanceAppraisalObjectsDTO.UpdatePerformanceAppraisalObjectsDTO.class) PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        return AjaxResult.success(performanceAppraisalService.updateOrgReviewPerformanceAppraisal(performanceAppraisalObjectsDTO));
    }

    /**
     * 查询组织绩效考核表详情-评议
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgReview:info")
    @GetMapping("/orgReview/info/{performAppraisalObjectsId}")
    public AjaxResult infoOrgReview(@PathVariable Long performAppraisalObjectsId) {
        return AjaxResult.success(performanceAppraisalService.selectOrgAppraisalReviewById(performAppraisalObjectsId));
    }

    //==============================组织绩效排名==================================//

    /**
     * 查询绩效考核表列表-组织-排名
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgRanking:pageList")
    @GetMapping("/orgRanking/pageList")
    public TableDataInfo listOrgRanking(PerformanceAppraisalDTO performanceAppraisalDTO) {
        startPage();
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectOrgAppraisalRankingList(performanceAppraisalDTO);
        return getDataTable(list);
    }

    /**
     * 查询绩效考核详情-组织-排名
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgRanking:info")
    @GetMapping("/orgRanking/info/{performanceAppraisalId}")
    public AjaxResult infoOrgRanking(@PathVariable Long performanceAppraisalId) {
        return AjaxResult.success(performanceAppraisalService.selectOrgAppraisalRankingById(performanceAppraisalId));
    }

    /**
     * 编辑绩效考核-组织-排名
     */
    @Log(title = "保存组织绩效排名", businessType = BusinessType.PERFORMANCE_APPRAISAL_ORG_RANKING, businessId = "performanceAppraisalId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgRanking:edit")
    @PostMapping("/orgRanking/edit")
    public AjaxResult editOrgRanking(@RequestBody @Validated(PerformanceAppraisalDTO.UpdatePerformanceAppraisalDTO.class) PerformanceAppraisalDTO performanceAppraisalDTO) {
        return toAjax(performanceAppraisalService.updateOrgRankingPerformanceAppraisal(performanceAppraisalDTO));
    }

    //==============================组织绩效归档==================================//

    /**
     * 查询绩效考核表列表-组织-归档
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgArchive:pageList")
    @GetMapping("/orgArchive/pageList")
    public TableDataInfo listOrgArchive(PerformanceAppraisalDTO performanceAppraisalDTO) {
        startPage();
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectOrgAppraisalArchiveList(performanceAppraisalDTO);
        return getDataTable(list);
    }

    /**
     * 查询绩效考核表详情-组织-归档
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgArchive:info")
    @GetMapping("/orgArchive/info/{performanceAppraisalId}")
    public AjaxResult infoOrgArchive(@PathVariable Long performanceAppraisalId) {
        return AjaxResult.success(performanceAppraisalService.selectOrgAppraisalArchiveById(performanceAppraisalId));
    }

    /**
     * 导入组织绩效归档表
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgArchive:import")
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
     * 导出组织模板表
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgArchive:import")
    @GetMapping("export-org-template")
    public void exportOrgTemplate(@RequestParam Integer importType, @RequestParam Long performanceAppraisalId, HttpServletResponse response) {
        if (StringUtils.isNull(importType)) {
            throw new ServiceException("请选择考核流程");
        }
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalService.selectPerformanceRankFactor(performanceAppraisalId);
        if (StringUtils.isEmpty(performanceRankFactorDTOS)) {
            throw new ServiceException("当前的绩效等级未配置 请先配置绩效等级");
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
        PerformanceAppraisalDTO appraisalDTO = performanceAppraisalService.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalService.selectOrgAppraisalObjectList(performanceAppraisalId);
        EasyExcel.write(response.getOutputStream())
                .head(head)
                .sheet("Sheet1")// 设置 sheet 的名字
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap))
//                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) 自定义表头
                .doWrite(PerformanceAppraisalImportListener.dataTemplateList(performanceAppraisalObjectsDTOList, appraisalDTO));
    }

    /**
     * 导出组织表
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:performanceAppraisal:orgArchive:info")
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
        Collection<List<Object>> list;
        if (selfDefinedColumnsFlag == 0) {
            head = PerformanceAppraisalImportListener.headOrgSystemTemplate(selectMap, performanceRankFactorDTOS);
            list = performanceAppraisalService.dataOrgSysList(performanceAppraisalId, performanceRankFactorDTOS);
        } else {
            List<PerformanceAppraisalColumnsDTO> appraisalColumnsDTOList = performanceAppraisalColumnsService.selectAppraisalColumnsByAppraisalId(performanceAppraisalId);
            head = PerformanceAppraisalImportListener.headOrgCustom(selectMap, performanceRankFactorDTOS, appraisalColumnsDTOList);
            list = performanceAppraisalService.dataOrgCustomList(performanceAppraisalId, performanceRankFactorDTOS);
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
                .doWrite(list);
    }

    //==============================个人绩效管理==================================//
    //==============================个人绩效制定==================================//

    /**
     * 查询绩效考核表列表-个人-制定
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:perDevelop:pageList")
    @GetMapping("/perDevelop/pageList")
    public TableDataInfo listPerDevelop(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        startPage();
        List<PerformanceAppraisalObjectsDTO> list = performanceAppraisalService.selectPerAppraisalDevelopList(performanceAppraisalObjectsDTO);
        return getDataTable(list);
    }

    /**
     * 查询个人绩效考核表详情-制定
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:perDevelop:info")
    @GetMapping("/perDevelop/info/{performAppraisalObjectsId}")
    public AjaxResult infoPerDevelop(@PathVariable Long performAppraisalObjectsId) {
        return AjaxResult.success(performanceAppraisalService.selectPerAppraisalDevelopById(performAppraisalObjectsId));
    }

    /**
     * 编辑组织绩效考核制定表
     */
    @Log(title = "保存个人绩效指定", businessType = BusinessType.PERFORMANCE_APPRAISAL_PERSON_SETTING, businessId = "performAppraisalObjectsId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:performanceAppraisal:perDevelop:edit")
    @PostMapping("/perDevelop/edit")
    public AjaxResult editPerDevelop(@RequestBody @Validated(PerformanceAppraisalObjectsDTO.UpdatePerformanceAppraisalObjectsDTO.class) PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        return AjaxResult.success(performanceAppraisalService.updatePerDevelopPerformanceAppraisal(performanceAppraisalObjectsDTO));
    }
    //==============================个人绩效评议==================================//

    /**
     * 查询绩效考核表列表-个人-评议
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:perReview:pageList")
    @GetMapping("/perReview/pageList")
    public TableDataInfo listPerReview(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        startPage();
        List<PerformanceAppraisalObjectsDTO> list = performanceAppraisalService.selectPerAppraisalReviewList(performanceAppraisalObjectsDTO);
        return getDataTable(list);
    }

    /**
     * 查询组织绩效考核表详情-评议
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:perReview:info")
    @GetMapping("/perReview/info/{performAppraisalObjectsId}")
    public AjaxResult infoOPerReview(@PathVariable Long performAppraisalObjectsId) {
        return AjaxResult.success(performanceAppraisalService.selectPerAppraisalReviewById(performAppraisalObjectsId));
    }

    /**
     * 编辑组织绩效考核评议表
     */
    @Log(title = "保存个人绩效评议", businessType = BusinessType.PERFORMANCE_APPRAISAL_PERSON_REVIEW, businessId = "performAppraisalObjectsId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:performanceAppraisal:perReview:edit")
    @PostMapping("/perReview/edit")
    public AjaxResult editPerReview(@RequestBody @Validated(PerformanceAppraisalObjectsDTO.UpdatePerformanceAppraisalObjectsDTO.class) PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        return AjaxResult.success(performanceAppraisalService.updatePerReviewPerformanceAppraisal(performanceAppraisalObjectsDTO));
    }

    //==============================个人绩效排名==================================//

    /**
     * 查询绩效考核表列表-组织-排名
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:perRanking:pageList")
    @GetMapping("/perRanking/pageList")
    public TableDataInfo listPerRanking(PerformanceAppraisalDTO performanceAppraisalDTO) {
        startPage();
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectPerAppraisalRankingList(performanceAppraisalDTO);
        return getDataTable(list);
    }

    /**
     * 查询绩效考核详情-人员-排名
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:perRanking:info")
    @GetMapping("/perRanking/info/{performanceAppraisalId}")
    public AjaxResult infoPerRanking(@PathVariable Long performanceAppraisalId) {
        return AjaxResult.success(performanceAppraisalService.selectPerAppraisalRankingById(performanceAppraisalId));
    }

    /**
     * 编辑绩效考核-个人-排名
     */
    @Log(title = "保存个人绩效排名", businessType = BusinessType.PERFORMANCE_APPRAISAL_PERSON_RANKING, businessId = "performanceAppraisalId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:performanceAppraisal:perRanking:edit")
    @PostMapping("/perRanking/edit")
    public AjaxResult editPerRanking(@RequestBody @Validated(PerformanceAppraisalDTO.UpdatePerformanceAppraisalDTO.class) PerformanceAppraisalDTO performanceAppraisalDTO) {
        return toAjax(performanceAppraisalService.updatePerRankingPerformanceAppraisal(performanceAppraisalDTO));
    }

    //==============================个人绩效归档==================================//

    /**
     * 查询绩效考核表列表-个人-归档
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:perArchive:pageList")
    @GetMapping("/perArchive/pageList")
    public TableDataInfo listPerArchive(PerformanceAppraisalDTO performanceAppraisalDTO) {
        startPage();
        List<PerformanceAppraisalDTO> list = performanceAppraisalService.selectPerAppraisalArchiveList(performanceAppraisalDTO);
        return getDataTable(list);
    }

    /**
     * 查询绩效考核详情-归档-个人
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:perArchive:info")
    @GetMapping("/perArchive/info/{performanceAppraisalId}")
    public AjaxResult infoPerArchive(@PathVariable Long performanceAppraisalId) {
        return AjaxResult.success(performanceAppraisalService.selectPerAppraisalArchiveById(performanceAppraisalId));
    }

    /**
     * 导入个人绩效归档表
     */
    @RequiresPermissions("operate:cloud:performanceAppraisal:perArchive:import")
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
     * 导出个人模板表
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:performanceAppraisal:perArchive:import")
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
        PerformanceAppraisalDTO appraisalDTO = performanceAppraisalService.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalService.selectPerAppraisalObjectList(performanceAppraisalId);
        EasyExcel.write(response.getOutputStream())
                .head(head)
                .sheet("Sheet1")// 设置 sheet 的名字
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap))
//                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) 自定义表头
                .doWrite(PerformanceAppraisalImportListener.dataTemplateList(performanceAppraisalObjectsDTOList, appraisalDTO));
    }

    /**
     * 导出个人模板表
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:performanceAppraisal:perArchive:info")
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

    //==============================组织绩效管理-个人绩效管理-归档、撤回==================================//

    /**
     * 归档
     */
    @RequiresPermissions(value = {"operate:cloud:performanceAppraisal:archive:org", "operate:cloud:performanceAppraisal:archive:per"}, logical = Logical.OR)
    @GetMapping("/archive/{performanceAppraisalId}")
    public AjaxResult archive(@PathVariable Long performanceAppraisalId) {
        return toAjax(performanceAppraisalService.archive(performanceAppraisalId));
    }

    /**
     * 撤回组织、个人绩效考核评议
     */
    @RequiresPermissions(value = {"operate:cloud:performanceAppraisal:orgReview:withdraw", "operate:cloud:performanceAppraisal:perReview:withdraw"}, logical = Logical.OR)
    @GetMapping("/withdraw/{performAppraisalObjectsId}")
    public AjaxResult withdraw(@PathVariable Long performAppraisalObjectsId) {
        return AjaxResult.success(performanceAppraisalService.withdraw(performAppraisalObjectsId));
    }

    //==============================其他==================================//

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
     * 根据绩效考核ID查找比例
     *
     * @param performanceAppraisalId 绩效考核id
     * @return AjaxResult
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @GetMapping("/list/performancePercentage/{performanceAppraisalId}")
    public AjaxResult listPercentage(@PathVariable Long performanceAppraisalId) {
        return AjaxResult.success(performanceAppraisalService.selectPerformancePercentageByPerformanceAppraisalId(performanceAppraisalId));
    }

    /**
     * 查询组织绩效结果排名
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @PostMapping("/orgProportion")
    public AjaxResult orgProportion(@RequestBody PerformanceAppraisalDTO performanceAppraisalDTO) {
        List<Long> appraisalObjectsIds = performanceAppraisalDTO.getAppraisalObjectsIds();
        Integer queryType = performanceAppraisalDTO.getQueryType();
        Long performanceAppraisalId = performanceAppraisalDTO.getPerformanceAppraisalId();
        return AjaxResult.success(performanceAppraisalService.selectOrgAppraisalRankByDTO(appraisalObjectsIds, performanceAppraisalId, queryType));
    }

    /**
     * 查询个人绩效结果排名
     */
    //@RequiresPermissions("operate:cloud:performanceAppraisal:list")
    @PostMapping("/perProportion")
    public AjaxResult perProportion(@RequestBody Map<String, List<Long>> performanceAppraisalDTO) {
        return AjaxResult.success(performanceAppraisalService.selectPerAppraisalRankByDTO(performanceAppraisalDTO));
    }


}
