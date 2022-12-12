package net.qixiaowei.operate.cloud.controller.salary;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
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
     * 查询部门调薪计划表详情
     */
    @RequiresPermissions("operate:cloud:deptSalaryAdjustPlan:info")
    @GetMapping("/info/{deptSalaryAdjustPlanId}")
    public AjaxResult info(@PathVariable Long deptSalaryAdjustPlanId) {
        DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO = deptSalaryAdjustPlanService.selectDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlanId);
        return AjaxResult.success(deptSalaryAdjustPlanDTO);
    }

    /**
     * 查询部门调薪计划表详情
     */
    @RequiresPermissions("operate:cloud:deptSalaryAdjustPlan:info")
    @GetMapping("/getLastSalary/{departmentId}")
    public AjaxResult getLastSalary(@PathVariable Long departmentId) {
        return AjaxResult.success(deptSalaryAdjustPlanService.getLastSalary(departmentId));
    }

    /**
     * 查询部门调薪计划表列表
     */
    @RequiresPermissions("operate:cloud:deptSalaryAdjustPlan:list")
    @GetMapping("/list")
    public AjaxResult list(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        List<DeptSalaryAdjustPlanDTO> list = deptSalaryAdjustPlanService.selectDeptSalaryAdjustPlanList(deptSalaryAdjustPlanDTO);
        return AjaxResult.success(list);
    }

    /**
     * 修改部门调薪计划表
     */
    @RequiresPermissions("operate:cloud:deptSalaryAdjustPlan:edit")
    @Log(title = "修改部门调薪计划表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        return AjaxResult.success(deptSalaryAdjustPlanService.editDeptSalaryAdjustPlan(deptSalaryAdjustPlanDTO));
    }

    /**
     * 逻辑删除部门调薪计划表
     */
    @RequiresPermissions("operate:cloud:deptSalaryAdjustPlan:remove")
    @Log(title = "删除部门调薪计划表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        return toAjax(deptSalaryAdjustPlanService.logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlanDTO));
    }

    /**
     * 逻辑批量删除部门调薪计划表
     */
    @RequiresPermissions("operate:cloud:deptSalaryAdjustPlan:removes")
    @Log(title = "批量删除部门调薪计划表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> deptSalaryAdjustPlanIds) {
        return toAjax(deptSalaryAdjustPlanService.logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(deptSalaryAdjustPlanIds));
    }

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
