package net.qixiaowei.system.manage.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.system.RoleDTO;
import net.qixiaowei.system.manage.service.system.IRoleService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
*
* @author hzk
* @since 2022-10-07
*/
@RestController
@RequestMapping("role")
public class RoleController extends BaseController
{


    @Autowired
    private IRoleService roleService;

    /**
    * 分页查询角色表列表
    */
    @RequiresPermissions("system:manage:role:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(RoleDTO roleDTO){
    startPage();
    List<RoleDTO> list = roleService.selectRoleList(roleDTO);
    return getDataTable(list);
    }

    /**
    * 查询角色表列表
    */
    @RequiresPermissions("system:manage:role:list")
    @GetMapping("/list")
    public AjaxResult list(RoleDTO roleDTO){
    List<RoleDTO> list = roleService.selectRoleList(roleDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增角色表
    */
    @RequiresPermissions("system:manage:role:add")
    @Log(title = "新增角色表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody RoleDTO roleDTO) {
    return toAjax(roleService.insertRole(roleDTO));
    }


    /**
    * 修改角色表
    */
    @RequiresPermissions("system:manage:role:edit")
    @Log(title = "修改角色表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody RoleDTO roleDTO)
    {
    return toAjax(roleService.updateRole(roleDTO));
    }

    /**
    * 逻辑删除角色表
    */
    @RequiresPermissions("system:manage:role:remove")
    @Log(title = "删除角色表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody RoleDTO roleDTO)
    {
    return toAjax(roleService.logicDeleteRoleByRoleId(roleDTO));
    }
    /**
    * 批量修改角色表
    */
    @RequiresPermissions("system:manage:role:edits")
    @Log(title = "批量修改角色表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<RoleDTO> roleDtos)
    {
    return toAjax(roleService.updateRoles(roleDtos));
    }

    /**
    * 批量新增角色表
    */
    @RequiresPermissions("system:manage:role:insertRoles")
    @Log(title = "批量新增角色表", businessType = BusinessType.INSERT)
    @PostMapping("/insertRoles")
    public AjaxResult insertRoles(@RequestBody List<RoleDTO> roleDtos)
    {
    return toAjax(roleService.insertRoles(roleDtos));
    }

    /**
    * 逻辑批量删除角色表
    */
    @RequiresPermissions("system:manage:role:removes")
    @Log(title = "批量删除角色表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<RoleDTO>  RoleDtos)
    {
    return toAjax(roleService.logicDeleteRoleByRoleIds(RoleDtos));
    }
}
