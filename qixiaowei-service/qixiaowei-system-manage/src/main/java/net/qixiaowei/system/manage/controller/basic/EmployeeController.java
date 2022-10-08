package net.qixiaowei.system.manage.controller.basic;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.service.basic.IEmployeeService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-09-30
*/
@RestController
@RequestMapping("employee")
public class EmployeeController extends BaseController
{


    @Autowired
    private IEmployeeService employeeService;

    /**
    * 分页查询员工表列表
    */
    //@RequiresPermissions("system:manage:employee:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmployeeDTO employeeDTO){
    startPage();
    List<EmployeeDTO> list = employeeService.selectEmployeeList(employeeDTO);
    return getDataTable(list);
    }

    /**
    * 查询员工表列表
    */
    //@RequiresPermissions("system:manage:employee:list")
    @GetMapping("/list")
    public AjaxResult list(EmployeeDTO employeeDTO){
    List<EmployeeDTO> list = employeeService.selectEmployeeList(employeeDTO);
    return AjaxResult.success(list);
    }

    /**
     * 查询员工单条信息
     */
    //@RequiresPermissions("system:manage:employee:list")
    @GetMapping("/info")
    public AjaxResult info(EmployeeDTO employeeDTO){
       EmployeeDTO employeeDTO1 = employeeService.selectEmployee(employeeDTO);
        return AjaxResult.success(employeeDTO1);
    }
    /**
    * 新增员工表
    */
    //@RequiresPermissions("system:manage:employee:add")
    //@Log(title = "新增员工表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(EmployeeDTO.AddEmployeeDTO.class) EmployeeDTO employeeDTO) {
    return toAjax(employeeService.insertEmployee(employeeDTO));
    }


    /**
    * 修改员工表
    */
    //@RequiresPermissions("system:manage:employee:edit")
    //@Log(title = "修改员工表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(EmployeeDTO.UpdateEmployeeDTO.class) EmployeeDTO employeeDTO)
    {
    return toAjax(employeeService.updateEmployee(employeeDTO));
    }

    /**
    * 逻辑删除员工表
    */
    //@RequiresPermissions("system:manage:employee:remove")
    //@Log(title = "删除员工表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(EmployeeDTO.DeleteEmployeeDTO.class) EmployeeDTO employeeDTO)
    {
    return toAjax(employeeService.logicDeleteEmployeeByEmployeeId(employeeDTO));
    }
    /**
    * 批量修改员工表
    */
    //@RequiresPermissions("system:manage:employee:edits")
    //@Log(title = "批量修改员工表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody @Validated(EmployeeDTO.DeleteEmployeeDTO.class) List<EmployeeDTO> employeeDtos)
    {
    return toAjax(employeeService.updateEmployees(employeeDtos));
    }

    /**
    * 批量新增员工表
    */
    //@RequiresPermissions("system:manage:employee:insertEmployees")
    //@Log(title = "批量新增员工表", businessType = BusinessType.INSERT)
    @PostMapping("/insertEmployees")
    public AjaxResult insertEmployees(@RequestBody List<EmployeeDTO> employeeDtos)
    {
    return toAjax(employeeService.insertEmployees(employeeDtos));
    }

    /**
    * 逻辑批量删除员工表
    */
    //@RequiresPermissions("system:manage:employee:removes")
    //@Log(title = "批量删除员工表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<EmployeeDTO>  EmployeeDtos)
    {
    return toAjax(employeeService.logicDeleteEmployeeByEmployeeIds(EmployeeDtos));
    }
}