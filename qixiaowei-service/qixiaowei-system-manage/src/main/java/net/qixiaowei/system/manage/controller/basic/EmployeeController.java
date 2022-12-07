package net.qixiaowei.system.manage.controller.basic;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.excel.SelectSheetWriteHandler;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.excel.basic.EmployeeExcel;
import net.qixiaowei.system.manage.excel.basic.EmployeeImportListener;
import net.qixiaowei.system.manage.service.basic.IEmployeeService;
import net.qixiaowei.system.manage.service.impl.basic.EmployeeServiceImpl;
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
import java.util.*;


/**
 * @author TANGMICHI
 * @since 2022-09-30
 */
@RestController
@RequestMapping("employee")
public class EmployeeController extends BaseController {


    @Autowired
    private IEmployeeService employeeService;


    /**
     * 分页查询员工表列表
     */
    @RequiresPermissions("system:manage:employee:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmployeeDTO employeeDTO) {
        startPage();
        List<EmployeeDTO> list = employeeService.selectEmployeeList(employeeDTO);
        return getDataTable(list);
    }

    /**
     * 新增员工表
     */
    @RequiresPermissions("system:manage:employee:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(EmployeeDTO.AddEmployeeDTO.class) EmployeeDTO employeeDTO) {
        return AjaxResult.success(employeeService.insertEmployee(employeeDTO));
    }

    /**
     * 修改员工表
     */
    @RequiresPermissions("system:manage:employee:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(EmployeeDTO.UpdateEmployeeDTO.class) EmployeeDTO employeeDTO) {
        return toAjax(employeeService.updateEmployee(employeeDTO));
    }

    /**
     * 查询员工单条信息
     */
    @RequiresPermissions(value = {"system:manage:employee:info", "system:manage:employee:edit"}, logical = Logical.OR)
    @GetMapping("/info/{employeeId}")
    public AjaxResult info(@PathVariable Long employeeId) {
        EmployeeDTO employeeDTO = employeeService.selectEmployeeByEmployeeId(employeeId);
        return AjaxResult.success(employeeDTO);
    }

    /**
     * 逻辑删除员工表
     */
    @RequiresPermissions("system:manage:employee:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(EmployeeDTO.DeleteEmployeeDTO.class) EmployeeDTO employeeDTO) {
        return toAjax(employeeService.logicDeleteEmployeeByEmployeeId(employeeDTO));
    }

    /**
     * 逻辑批量删除员工表
     */
    @RequiresPermissions("system:manage:employee:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> employeeIds) {
        return toAjax(employeeService.logicDeleteEmployeeByEmployeeIds(employeeIds));
    }

    /**
     * 导入人员
     */
    @RequiresPermissions("system:manage:employee:import")
    @PostMapping("import")
    public AjaxResult importEmployee(MultipartFile file) {
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
                    .registerReadListener(new EmployeeImportListener(employeeService)).doRead();
        } catch (IOException e) {
            throw new ServiceException("导入人员信息配置Excel失败");
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 导出人员
     */
    @SneakyThrows
    @RequiresPermissions("system:manage:employee:export")
    @GetMapping("export")
    public void exportEmployee(@RequestParam Map<String, Object> employee, EmployeeDTO employeeDTO, HttpServletResponse response) {
        Map<Integer, List<String>> selectMap = new HashMap<>();
        //自定义表头
        List<List<String>> head = EmployeeImportListener.head(selectMap);
        List<EmployeeExcel> employeeExcelList = employeeService.exportEmployee(employeeDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("人员信息配置" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap))
                .head(head)// 设置表头
                .sheet("人员信息配置")// 设置 sheet 的名字
                // 自适应列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(EmployeeImportListener.dataList(employeeExcelList));
    }

    /**
     * 导出模板
     */
    @SneakyThrows
    @RequiresPermissions("system:manage:employee:import")
    @GetMapping("/export-template")
    public void exportUser(HttpServletResponse response) {
        //示例数据
        List<EmployeeExcel> employeeExcelList = new ArrayList<>();
        EmployeeServiceImpl.packEmployeeExcel(employeeExcelList);
        Map<Integer, List<String>> selectMap = new HashMap<>();
        //自定义表头
        List<List<String>> head = EmployeeImportListener.head(selectMap);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("人员信息配置模板" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap))
                .excelType(ExcelTypeEnum.XLSX)
                .head(head)// 设置表头
                .sheet("人员信息配置")// 设置 sheet 的名字
                // 自适应列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).doWrite(EmployeeImportListener.dataList(employeeExcelList));
    }


    //==============================岗位薪酬报表==================================//

    /**
     * 分页查询岗位薪酬报表
     */
    @RequiresPermissions("system:manage:employee:pagePostSalaryReportList")
    @GetMapping("/pagePostSalaryReportList")
    public TableDataInfo pagePostSalaryReportList(EmployeeDTO employeeDTO) {
        startPage();
        List<EmployeeDTO> list = employeeService.pagePostSalaryReportList(employeeDTO);
        return getDataTable(list);
    }

    //==============================其他==================================//

    /**
     * 查询员工表列表(下拉框)
     */
    @GetMapping("/list")
    public AjaxResult list(EmployeeDTO employeeDTO) {
        List<EmployeeDTO> list = employeeService.selectDropEmployeeList(employeeDTO);
        return AjaxResult.success(list);
    }

    /**
     * 根据部门id查询员工表列表
     */
    @GetMapping("/queryEmployeeByDept/{employeeDepartmentId}")
    public AjaxResult queryEmployeeByDept(@PathVariable Long employeeDepartmentId) {
        List<EmployeeDTO> list = employeeService.queryEmployeeByDept(employeeDepartmentId);
        return AjaxResult.success(list);
    }

    /**
     * 新增人力预算上年期末数集合预制数据
     */
    @PostMapping("/amountLastYear")
    public AjaxResult selecTamountLastYearList(@RequestBody EmployeeDTO employeeDTO) {
        return AjaxResult.success(employeeService.selecTamountLastYearList(employeeDTO));
    }


}
