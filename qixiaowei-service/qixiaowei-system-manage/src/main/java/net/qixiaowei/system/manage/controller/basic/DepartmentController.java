package net.qixiaowei.system.manage.controller.basic;

import java.util.List;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.service.basic.IDepartmentService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author TANGMICHI
 * @since 2022-09-27
 */
@RestController
@RequestMapping("department")
public class DepartmentController extends BaseController {


    @Autowired
    private IDepartmentService departmentService;

    /**
     * 生成部门编码
     *
     * @return 部门编码
     */
    @RequiresPermissions(value = {"system:manage:department:add", "system:manage:department:edit"}, logical = Logical.OR)
    @GetMapping("/generate/departmentCode")
    public AjaxResult generateDepartmentCode() {
        return AjaxResult.success(departmentService.generateDepartmentCode());
    }


    /**
     * 新增部门表
     */
    @Log(title = "新增组织", businessType = BusinessType.DEPARTMENT, businessId = "departmentId", operationType = OperationType.INSERT)
    @RequiresPermissions("system:manage:department:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(DepartmentDTO.AddDepartmentDTO.class) DepartmentDTO departmentDTO) {
        return AjaxResult.success(departmentService.insertDepartment(departmentDTO));
    }

    /**
     * 修改部门表
     */
    @Log(title = "编辑组织", businessType = BusinessType.DEPARTMENT, businessId = "departmentId", operationType = OperationType.UPDATE)
    @RequiresPermissions("system:manage:department:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(DepartmentDTO.UpdateDepartmentDTO.class) DepartmentDTO departmentDTO) {
        return toAjax(departmentService.updateDepartment(departmentDTO));
    }

    /**
     * 部门岗位详情
     */
    @RequiresPermissions("system:manage:department:info")
    @GetMapping("/deptParticulars/{departmentId}")
    public AjaxResult deptParticulars(@PathVariable Long departmentId) {
        DepartmentDTO departmentDTO1 = departmentService.deptParticulars(departmentId);
        return AjaxResult.success(departmentDTO1);
    }

    /**
     * 逻辑删除部门表
     */
    @RequiresPermissions("system:manage:department:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(DepartmentDTO.DeleteDepartmentDTO.class) DepartmentDTO departmentDTO) {
        return toAjax(departmentService.logicDeleteDepartmentByDepartmentIds(departmentDTO));
    }

    /**
     * 逻辑批量删除部门表
     */
    @RequiresPermissions("system:manage:department:remove")
    @PostMapping("/removesList")
    public AjaxResult removesList(@RequestBody List<Long> departmentIds) {
        return toAjax(departmentService.logicDeleteDepartmentByDepartmentIds(departmentIds));
    }

    /**
     * 分页查询部门表列表
     */
//    @RequiresPermissions("system:manage:department:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(DepartmentDTO departmentDTO) {
        startPage();
        List<DepartmentDTO> list = departmentService.selectDepartmentList(departmentDTO);
        return getDataTable(list);
    }

    /**
     * 查询部门表列表
     */
    @GetMapping("/list")
    public AjaxResult list(DepartmentDTO departmentDTO) {
        List<DepartmentDTO> list = departmentService.selectDepartmentList(departmentDTO);
        return AjaxResult.success(list);
    }

    /**
     * 返回部门层级
     */
    @GetMapping("/selectLevel")
    public AjaxResult selectLevel() {
        return AjaxResult.success(departmentService.selectLevel());
    }

    /**
     * 分页查询部门人员表列表
     */
    @GetMapping("/queryDeptEmployee")
    public TableDataInfo queryDeptEmployee(DepartmentDTO departmentDTO) {
        startPage();
        List<EmployeeDTO> employeeDTOList = departmentService.queryDeptEmployee(departmentDTO);
        return getDataTable(employeeDTOList);
    }

    /**
     * 查询上级组织
     */
    @GetMapping("/queryparent")
    public AjaxResult queryparent(@RequestParam(required = false) Long departmentId) {
        List<DepartmentDTO> list = departmentService.queryparent(departmentId);
        return AjaxResult.success(list);
    }


}
