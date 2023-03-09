package net.qixiaowei.operate.cloud.controller.targetManager;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDetailsDTO;
import net.qixiaowei.operate.cloud.api.vo.strategyIntent.StrategyIntentOperateVO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetOutcomeExcel;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetOutcomeImportListener;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeDetailsService;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author TANGMICHI
 * @since 2022-11-07
 */
@RestController
@RequestMapping("targetOutcome")
public class TargetOutcomeController extends BaseController {


    @Autowired
    private ITargetOutcomeService targetOutcomeService;

    @Autowired
    private RemoteIndicatorService indicatorService;

    @Autowired
    private ITargetOutcomeDetailsService targetOutcomeDetailsService;


    /**
     * 分页查询目标结果表列表
     */
    @RequiresPermissions("operate:cloud:targetOutcome:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TargetOutcomeDTO targetOutcomeDTO) {
        startPage();
        List<TargetOutcomeDTO> list = targetOutcomeService.selectTargetOutcomeList(targetOutcomeDTO);
        return getDataTable(list);
    }

    /**
     * 修改目标结果表
     */
    @RequiresPermissions("operate:cloud:targetOutcome:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody TargetOutcomeDTO targetOutcomeDTO) {
        return toAjax(targetOutcomeService.updateTargetOutcome(targetOutcomeDTO));
    }

    /**
     * 查询目标结果表详情
     */
    @RequiresPermissions(value = {"operate:cloud:targetOutcome:info", "operate:cloud:targetOutcome:edit"}, logical = Logical.OR)
    @GetMapping("/info/{targetOutcomeId}")
    public AjaxResult info(@PathVariable Long targetOutcomeId) {
        TargetOutcomeDTO targetOutcomeDTO = targetOutcomeService.selectTargetOutcomeByTargetOutcomeId(targetOutcomeId);
        return AjaxResult.success(targetOutcomeDTO);
    }

    /**
     * 解析目标结果表
     */
    @RequiresPermissions("operate:cloud:targetOutcome:edit")
    @PostMapping("excelParseObject")
    public AjaxResult importTargetOutcome(Long targetOutSettingId, MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
        List<Map<Integer, String>> targetSettingExcelList = read.doReadAllSync();
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = targetOutcomeService.importTargetOutcome(targetSettingExcelList, targetOutSettingId);
        return AjaxResult.successExcel(targetOutcomeDetailsDTOList, null, null);
    }

    /**
     * 导出目标结果模板
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetOutcome:edit")
    @GetMapping("/export-template")
    public void exportTemplate(@RequestParam Integer targetYear, HttpServletResponse response) {
        try {
            List<List<String>> headTemplate = TargetOutcomeImportListener.headTemplate(targetYear);
            TargetOutcomeDTO targetOutcomeDTO = targetOutcomeService.selectTargetOutcomeByTargetYear(targetYear);
            if (StringUtils.isNull(targetOutcomeDTO)) {
                throw new ServiceException("当前年度的目标结果不存在");
            }
            List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = targetOutcomeDetailsService.selectTargetOutcomeDetailsByOutcomeId(targetOutcomeDTO.getTargetOutcomeId());
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(CharsetKit.UTF_8);
            String fileName = URLEncoder.encode("关键经营结果导入" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                    , CharsetKit.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream())
                    .head(headTemplate)
                    .sheet("Sheet1")// 设置 sheet 的名字
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .doWrite(TargetOutcomeImportListener.dataTemplateList(targetOutcomeDetailsDTOList, indicatorService));
        } catch (IOException e) {
            throw new ServiceException("导出失败");
        }
    }

    /**
     * 导出目标结果表
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:targetOutcome:export")
    @GetMapping("export")
    public void exportTargetOutcome(@RequestParam Map<String, Object> targetOutcome, TargetOutcomeDTO targetOutcomeDTO, HttpServletResponse response) {
        List<List<String>> head = TargetOutcomeImportListener.head();
        List<TargetOutcomeExcel> targetOutcomeExcelList = targetOutcomeService.exportTargetOutcome(targetOutcomeDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("关键经营结果导出" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .head(head)
                .sheet("关键经营结果")// 设置 sheet 的名字
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(TargetOutcomeImportListener.dataList(targetOutcomeExcelList));
    }


    /**
     * 查询目标结果表列表
     */
    @GetMapping("/list")
    public AjaxResult list(TargetOutcomeDTO targetOutcomeDTO) {
        List<TargetOutcomeDTO> list = targetOutcomeService.selectTargetOutcomeList(targetOutcomeDTO);
        return AjaxResult.success(list);
    }

    /**
     * 战略云获取指标实际值
     */
    //@RequiresPermissions("operate:cloud:targetOutcome:getResultIndicator")
    @PostMapping("/getResultIndicator")
    public AjaxResult getResultIndicator(@RequestBody StrategyIntentOperateVO strategyIntentOperateVO) {
        return AjaxResult.success(targetOutcomeService.getResultIndicator(strategyIntentOperateVO));
    }

}
