package net.qixiaowei.system.manage.controller.basic;

import java.util.List;

import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
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
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.service.basic.IDepartmentService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-09-27
*/
@RestController
@RequestMapping("department")
public class DepartmentController extends BaseController
{


    @Autowired
    private IDepartmentService departmentService;

    /**
    * 分页查询部门表列表
    */
    //@RequiresPermissions("system:manage:department:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(DepartmentDTO departmentDTO){
    startPage();
    List<DepartmentDTO> list = departmentService.selectDepartmentList(departmentDTO);
    return getDataTable(list);
    }

    /**
    * 查询部门表列表
    */
    //@RequiresPermissions("system:manage:department:list")
    @GetMapping("/list")
    public AjaxResult list(DepartmentDTO departmentDTO){
    List<DepartmentDTO> list = departmentService.selectDepartmentList(departmentDTO);
    return AjaxResult.success(list);
    }

    /**
     *部门岗位详情
     */
    //@RequiresPermissions("system:manage:department:list")
    @GetMapping("/deptParticulars/{departmentId}")
    public AjaxResult deptParticulars(@PathVariable Long departmentId){
        DepartmentDTO departmentDTO1 = departmentService.deptParticulars(departmentId);
        return AjaxResult.success(departmentDTO1);
    }

    /**
     * 分页查询部门人员表列表
     */
    //@RequiresPermissions("system:manage:department:pageList")
    @GetMapping("/queryDeptEmployee")
    public TableDataInfo queryDeptEmployee(DepartmentDTO departmentDTO){
        startPage();
        List<EmployeeDTO>  employeeDTOList = departmentService.queryDeptEmployee(departmentDTO);
        return getDataTable(employeeDTOList);
    }
    /**
     * 查询上级组织
     */
    //@RequiresPermissions("system:manage:department:pageList")
    @GetMapping("/queryparent")
    public AjaxResult queryparent(){
        List<DepartmentDTO> list = departmentService.queryparent();
        return AjaxResult.success(list);
    }
    /**
    * 新增部门表
    */
    //@RequiresPermissions("system:manage:department:add")
    //@Log(title = "新增部门表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(DepartmentDTO.AddDepartmentDTO.class)DepartmentDTO departmentDTO) {
    return toAjax(departmentService.insertDepartment(departmentDTO));
    }


    /**
    * 修改部门表
    */
    //@RequiresPermissions("system:manage:department:edit")
    //@Log(title = "修改部门表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(DepartmentDTO.UpdateDepartmentDTO.class)DepartmentDTO departmentDTO)
    {
    return toAjax(departmentService.updateDepartment(departmentDTO));
    }

    /**
    * 逻辑删除部门表
    */
    //@RequiresPermissions("system:manage:department:remove")
    //@Log(title = "删除部门表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(DepartmentDTO.DeleteDepartmentDTO.class) DepartmentDTO departmentDTO)
    {
    return toAjax(departmentService.logicDeleteDepartmentByDepartmentIds(departmentDTO));
    }

    /**
    * 批量修改部门表
    */
    //@RequiresPermissions("system:manage:department:edits")
    //@Log(title = "批量修改部门表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<DepartmentDTO> departmentDtos)
    {
    return toAjax(departmentService.updateDepartments(departmentDtos));
    }

    /**
    * 批量新增部门表
    */
    //@RequiresPermissions("system:manage:department:insertDepartments")
    //@Log(title = "批量新增部门表", businessType = BusinessType.INSERT)
    @PostMapping("/insertDepartments")
    public AjaxResult insertDepartments(@RequestBody List<DepartmentDTO> departmentDtos)
    {
    return toAjax(departmentService.insertDepartments(departmentDtos));
    }

    /**
    * 逻辑批量删除部门表
    */
    //@RequiresPermissions("system:manage:department:removes")
    //@Log(title = "批量删除部门表", businessType = BusinessType.DELETE)
    @PostMapping("/removesList")
    public AjaxResult removesList(@RequestBody List<Long>  departmentIds)
    {
    return toAjax(departmentService.logicDeleteDepartmentByDepartmentIds(departmentIds));
    }
}
