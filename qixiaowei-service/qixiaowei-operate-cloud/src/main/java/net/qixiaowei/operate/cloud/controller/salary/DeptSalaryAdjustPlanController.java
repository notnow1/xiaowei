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
import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.excel.salary.DeptSalaryAdjustPlanExcel;
import net.qixiaowei.operate.cloud.excel.salary.DeptSalaryAdjustPlanImportListener;
import net.qixiaowei.operate.cloud.service.salary.IDeptSalaryAdjustPlanService;
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
 * @since 2022-12-11
 */
@RestController
@RequestMapping("deptSalaryAdjustPlan")
public class DeptSalaryAdjustPlanController extends BaseController {


    @Autowired
    private IDeptSalaryAdjustPlanService deptSalaryAdjustPlanService;


    /**
     * 查询部门调薪计划表列表
     */
    @RequiresPermissions("operate:cloud:deptSalaryAdjustPlan:pageList")
    @GetMapping("/pageList")
    public TableDataInfo list(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        startPage();
        List<DeptSalaryAdjustPlanDTO> list = deptSalaryAdjustPlanService.selectDeptSalaryAdjustPlanList(deptSalaryAdjustPlanDTO);
        return getDataTable(list);
    }

    /**
     * 新增部门调薪计划表
     */
    @Log(title = "新增部门调薪计划", businessType = BusinessType.DEPT_SALARY_ADJUST_PLAN, businessId = "deptSalaryAdjustPlanId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:deptSalaryAdjustPlan:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        return toAjax(deptSalaryAdjustPlanService.insertDeptSalaryAdjustPlan(deptSalaryAdjustPlanDTO));
    }

    /**
     * 修改部门调薪计划表
     */
    @Log(title = "保存部门调薪计划", businessType = BusinessType.DEPT_SALARY_ADJUST_PLAN, businessId = "deptSalaryAdjustPlanId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:deptSalaryAdjustPlan:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        return toAjax(deptSalaryAdjustPlanService.editDeptSalaryAdjustPlan(deptSalaryAdjustPlanDTO));
    }

    /**
     * 查询部门调薪计划表详情
     */
    @RequiresPermissions(value = {"operate:cloud:deptSalaryAdjustPlan:info", "operate:cloud:deptSalaryAdjustPlan:edit"}, logical = Logical.OR)
    @GetMapping("/info/{deptSalaryAdjustPlanId}")
    public AjaxResult info(@PathVariable Long deptSalaryAdjustPlanId) {
        DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO = deptSalaryAdjustPlanService.selectDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlanId);
        return AjaxResult.success(deptSalaryAdjustPlanDTO);
    }

    /**
     * 获取上年工资包
     */
    @RequiresPermissions("operate:cloud:deptSalaryAdjustPlan:info")
    @GetMapping("/getLastSalary")
    public AjaxResult getLastSalary(@RequestParam("departmentId") Long departmentId, @RequestParam("planYear") Integer planYear) {
        return AjaxResult.success(deptSalaryAdjustPlanService.getLastSalary(departmentId, planYear));
    }

    /**
     * 逻辑删除部门调薪计划表
     */
    @RequiresPermissions("operate:cloud:deptSalaryAdjustPlan:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        return toAjax(deptSalaryAdjustPlanService.logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlanDTO));
    }

    /**
     * 逻辑批量删除部门调薪计划表
     */
    @RequiresPermissions("operate:cloud:deptSalaryAdjustPlan:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> deptSalaryAdjustPlanIds) {
        return toAjax(deptSalaryAdjustPlanService.logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(deptSalaryAdjustPlanIds));
    }

    /**
     * 获取涨薪包预算
     */
    @RequiresPermissions("operate:cloud:deptSalaryAdjustPlan:add")
    @GetMapping("/getRaiseSalary")
    public AjaxResult getRaiseSalary(@RequestParam("planYear") Integer planYear) {
        return AjaxResult.success("nihao", deptSalaryAdjustPlanService.getRaiseSalary(planYear));
    }

    /**
     * 获取已有数据的最大年份
     */
    @RequiresPermissions("operate:cloud:deptSalaryAdjustPlan:add")
    @GetMapping("/getMaxYear")
    public AjaxResult getMaxYear() {
        return AjaxResult.success(deptSalaryAdjustPlanService.getMaxYear());
    }

    /**
     * 获取已存在的年份
     */
    @GetMapping("/getExistYear")
    public AjaxResult getExistYear() {
        return AjaxResult.success(deptSalaryAdjustPlanService.getExistYear());
    }


    //==============================其他==================================//

    /**
     * 导入部门调薪计划表
     */
    @PostMapping("import")
    public AjaxResult importDeptSalaryAdjustPlan(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        InputStream inputStream;
        try {
            DeptSalaryAdjustPlanImportListener importListener = new DeptSalaryAdjustPlanImportListener(deptSalaryAdjustPlanService);
            inputStream = new BufferedInputStream(file.getInputStream());
            ExcelReaderBuilder builder = EasyExcel.read(inputStream, DeptSalaryAdjustPlanExcel.class, importListener);
            builder.doReadAll();
        } catch (IOException e) {
            throw new ServiceException("导入部门调薪计划表Excel失败");
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 导出部门调薪计划表
     */
    @SneakyThrows
    @GetMapping("export")
    public void exportDeptSalaryAdjustPlan(@RequestParam Map<String, Object> deptSalaryAdjustPlan, DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO, HttpServletResponse response) {
        List<DeptSalaryAdjustPlanExcel> deptSalaryAdjustPlanExcelList = deptSalaryAdjustPlanService.exportDeptSalaryAdjustPlan(deptSalaryAdjustPlanDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("部门调薪计划表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), DeptSalaryAdjustPlanExcel.class).sheet("部门调薪计划表").doWrite(deptSalaryAdjustPlanExcelList);
    }
}
