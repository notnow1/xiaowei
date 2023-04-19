package net.qixiaowei.operate.cloud.controller.salary;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.domain.salary.EmpSalaryAdjustPlan;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.excel.salary.EmpSalaryAdjustPlanExcel;
import net.qixiaowei.operate.cloud.excel.salary.EmpSalaryAdjustPlanImportListener;
import net.qixiaowei.operate.cloud.service.salary.IEmpSalaryAdjustPlanService;
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
 * @since 2022-12-14
 */
@RestController
@RequestMapping("empSalaryAdjustPlan")
public class EmpSalaryAdjustPlanController extends BaseController {


    @Autowired
    private IEmpSalaryAdjustPlanService empSalaryAdjustPlanService;


    /**
     * 分页查询个人调薪计划表列表
     */
    @RequiresPermissions("operate:cloud:empSalaryAdjustPlan:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        startPage();
        List<EmpSalaryAdjustPlanDTO> list = empSalaryAdjustPlanService.selectEmpSalaryAdjustPlanList(empSalaryAdjustPlanDTO);
        return getDataTable(list);
    }

    /**
     * 新增个人调薪计划表
     */
    @Log(title = "新增个人调薪计划", businessType = BusinessType.EMP_SALARY_ADJUST_PLAN, businessId = "empSalaryAdjustPlanId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:empSalaryAdjustPlan:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        return AjaxResult.success(empSalaryAdjustPlanService.insertEmpSalaryAdjustPlan(empSalaryAdjustPlanDTO));
    }

    /**
     * 修改个人调薪计划表
     */
    @Log(title = "保存个人调薪计划", businessType = BusinessType.EMP_SALARY_ADJUST_PLAN, businessId = "empSalaryAdjustPlanId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:empSalaryAdjustPlan:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        return toAjax(empSalaryAdjustPlanService.updateEmpSalaryAdjustPlan(empSalaryAdjustPlanDTO));
    }

    /**
     * 查询个人调薪计划表详情
     */
    @RequiresPermissions(value = {"operate:cloud:empSalaryAdjustPlan:info", "operate:cloud:empSalaryAdjustPlan:edit"}, logical = Logical.OR)
    @GetMapping("/info/{empSalaryAdjustPlanId}")
    public AjaxResult info(@PathVariable Long empSalaryAdjustPlanId) {
        EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO = empSalaryAdjustPlanService.selectEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
        return AjaxResult.success(empSalaryAdjustPlanDTO);
    }

    /**
     * 逻辑删除个人调薪计划表
     */
    @RequiresPermissions("operate:cloud:empSalaryAdjustPlan:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        return toAjax(empSalaryAdjustPlanService.logicDeleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(empSalaryAdjustPlanDTO));
    }

    /**
     * 逻辑批量删除个人调薪计划表
     */
    @RequiresPermissions("operate:cloud:empSalaryAdjustPlan:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> empSalaryAdjustPlanIds) {
        return toAjax(empSalaryAdjustPlanService.logicDeleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanIds(empSalaryAdjustPlanIds));
    }

    /**
     * 查询个人调薪计划表列表
     */
    @RequiresPermissions("operate:cloud:empSalaryAdjustPlan:pageList")
    @GetMapping("/list")
    public AjaxResult list(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        List<EmpSalaryAdjustPlanDTO> list = empSalaryAdjustPlanService.selectEmpSalaryAdjustPlanList(empSalaryAdjustPlanDTO);
        return AjaxResult.success(list);
    }

    /**
     * 职级确定薪酬详情
     */
    @RequiresPermissions(value = {"operate:cloud:empSalaryAdjustPlan:info", "operate:cloud:empSalaryAdjustPlan:edit"}, logical = Logical.OR)
    @GetMapping("/officialRank/info")
    public AjaxResult officialRankInfo(Long postId, Integer officialRank) {
        String officialRankInfo = empSalaryAdjustPlanService.officialRankInfo(postId, officialRank);
        return AjaxResult.success(officialRankInfo);
    }

    /**
     * 导入个人调薪计划表
     */
    @PostMapping("import")
    public AjaxResult importEmpSalaryAdjustPlan(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        InputStream inputStream;
        try {
            EmpSalaryAdjustPlanImportListener importListener = new EmpSalaryAdjustPlanImportListener(empSalaryAdjustPlanService);
            inputStream = new BufferedInputStream(file.getInputStream());
            ExcelReaderBuilder builder = EasyExcel.read(inputStream, EmpSalaryAdjustPlanExcel.class, importListener);
            builder.doReadAll();
        } catch (IOException e) {
            throw new ServiceException("导入个人调薪计划表Excel失败");
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 导出个人调薪计划表
     */
    @SneakyThrows
    @GetMapping("export")
    public void exportEmpSalaryAdjustPlan(@RequestParam Map<String, Object> empSalaryAdjustPlan, EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO, HttpServletResponse response) {
        Map<String, Object> params = empSalaryAdjustPlanDTO.getParams();
        EmpSalaryAdjustPlan empSalaryAdjustPlan1 = new EmpSalaryAdjustPlan();
        empSalaryAdjustPlan1.setParams(params);
        List<EmpSalaryAdjustPlanExcel> empSalaryAdjustPlanExcelList = empSalaryAdjustPlanService.exportEmpSalaryAdjustPlan(empSalaryAdjustPlan1);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("个人调薪计划表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), EmpSalaryAdjustPlanExcel.class).sheet("个人调薪计划表").doWrite(empSalaryAdjustPlanExcelList);
    }
}
