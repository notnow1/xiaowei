package net.qixiaowei.system.manage.controller.basic;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.nacos.shaded.com.google.common.base.Charsets;
import java.net.URLEncoder;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.excel.EmployeeExcel;
import net.qixiaowei.system.manage.excel.EmployeeImportListener;
import net.qixiaowei.system.manage.service.basic.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


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
     * 导入人员
     */
    @PostMapping("import")
    public AjaxResult importEmployee(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        InputStream inputStream;
        try {
            EmployeeImportListener importListener = new EmployeeImportListener(employeeService);
            inputStream = new BufferedInputStream(file.getInputStream());
            ExcelReaderBuilder builder = EasyExcel.read(inputStream, EmployeeExcel.class, importListener);
            builder.doReadAll();
        } catch (IOException e) {
            throw new ServiceException("导入人员Excel失败");
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 导出用户
     */
    @SneakyThrows
    @GetMapping("export")
    public void exportUser(@RequestParam Map<String, Object> user, EmployeeDTO employeeDTO, HttpServletResponse response) {
        List<EmployeeDTO> employeeDTOList = employeeService.selectEmployeeList(employeeDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(Charsets.UTF_8.name());
        String fileName = URLEncoder.encode("人员数据导出", Charsets.UTF_8.name());
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), EmployeeExcel.class).sheet("人员数据表").doWrite(employeeDTOList);
    }

    /**
     * 分页查询员工表列表
     */
    //@RequiresPermissions("system:manage:employee:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmployeeDTO employeeDTO) {
        startPage();
        List<EmployeeDTO> list = employeeService.selectEmployeeList(employeeDTO);
        return getDataTable(list);
    }

    /**
     * 查询员工表列表
     */
    //@RequiresPermissions("system:manage:employee:list")
    @GetMapping("/list")
    public AjaxResult list(EmployeeDTO employeeDTO) {
        List<EmployeeDTO> list = employeeService.selectEmployeeList(employeeDTO);
        return AjaxResult.success(list);
    }

    /**
     * 查询员工单条信息
     */
    //@RequiresPermissions("system:manage:employee:list")
    @GetMapping("/info/{employeeId}")
    public AjaxResult info(@PathVariable Long employeeId) {
        EmployeeDTO employeeDTO = employeeService.selectEmployeeByEmployeeId(employeeId);
        return AjaxResult.success(employeeDTO);
    }

    /**
     * 新增员工表
     */
    //@RequiresPermissions("system:manage:employee:add")
    //@Log(title = "新增员工表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(EmployeeDTO.AddEmployeeDTO.class) EmployeeDTO employeeDTO) {
        return AjaxResult.success(employeeService.insertEmployee(employeeDTO));
    }


    /**
     * 修改员工表
     */
    //@RequiresPermissions("system:manage:employee:edit")
    //@Log(title = "修改员工表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(EmployeeDTO.UpdateEmployeeDTO.class) EmployeeDTO employeeDTO) {
        return toAjax(employeeService.updateEmployee(employeeDTO));
    }

    /**
     * 逻辑删除员工表
     */
    //@RequiresPermissions("system:manage:employee:remove")
    //@Log(title = "删除员工表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(EmployeeDTO.DeleteEmployeeDTO.class) EmployeeDTO employeeDTO) {
        return toAjax(employeeService.logicDeleteEmployeeByEmployeeId(employeeDTO));
    }

    /**
     * 批量修改员工表
     */
    //@RequiresPermissions("system:manage:employee:edits")
    //@Log(title = "批量修改员工表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody @Validated(EmployeeDTO.DeleteEmployeeDTO.class) List<EmployeeDTO> employeeDtos) {
        return toAjax(employeeService.updateEmployees(employeeDtos));
    }

    /**
     * 批量新增员工表
     */
    //@RequiresPermissions("system:manage:employee:insertEmployees")
    //@Log(title = "批量新增员工表", businessType = BusinessType.INSERT)
    @PostMapping("/insertEmployees")
    public AjaxResult insertEmployees(@RequestBody List<EmployeeDTO> employeeDtos) {
        return toAjax(employeeService.insertEmployees(employeeDtos));
    }

    /**
     * 逻辑批量删除员工表
     */
    //@RequiresPermissions("system:manage:employee:removes")
    //@Log(title = "批量删除员工表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> employeeIds) {
        return toAjax(employeeService.logicDeleteEmployeeByEmployeeIds(employeeIds));
    }
}
