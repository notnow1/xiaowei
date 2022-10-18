package net.qixiaowei.system.manage.controller.system;

import java.util.List;

import net.qixiaowei.system.manage.api.dto.system.RoleAuthUsersDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.system.RoleDTO;
import net.qixiaowei.system.manage.service.system.IRoleService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author hzk
 * @since 2022-10-07
 */
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {


    @Autowired
    private IRoleService roleService;

    /**
     * 分页查询角色表列表
     */
    @RequiresPermissions("system:manage:role:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(RoleDTO roleDTO) {
        startPage();
        List<RoleDTO> list = roleService.selectRoleList(roleDTO);
        return getDataTable(list);
    }

    /**
     * 查询角色表列表
     */
    @RequiresPermissions("system:manage:role:list")
    @GetMapping("/list")
    public AjaxResult list(RoleDTO roleDTO) {
        List<RoleDTO> list = roleService.selectRoleList(roleDTO);
        return AjaxResult.success(list);
    }

    /**
     * 新增角色表
     */
    @RequiresPermissions("system:manage:role:add")
    @Log(title = "新增角色表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@Validated(RoleDTO.AddRoleDTO.class) @RequestBody RoleDTO roleDTO) {
        return AjaxResult.success(roleService.insertRole(roleDTO));
    }

    /**
     * 查看角色信息
     *
     * @return 角色信息
     */
    @GetMapping("/info/{roleId}")
    public AjaxResult info(@PathVariable Long roleId) {
        return AjaxResult.success(roleService.selectRoleByRoleId(roleId));
    }

    /**
     * 修改角色表
     */
    @RequiresPermissions("system:manage:role:edit")
    @Log(title = "修改角色表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@Validated(RoleDTO.UpdateRoleDTO.class) @RequestBody RoleDTO roleDTO) {
        return toAjax(roleService.updateRole(roleDTO));
    }

    /**
     * 角色授权用户
     */
    @RequiresPermissions("system:manage:role:authUsers")
    @Log(title = "角色授权用户", businessType = BusinessType.GRANT)
    @PostMapping("/authUsers")
    public AjaxResult authUsers(@Validated @RequestBody RoleAuthUsersDTO roleAuthUsersDTO) {
        roleService.authUsers(roleAuthUsersDTO);
        return success();
    }

    /**
     * 查询已分配用户角色列表
     */
    @RequiresPermissions("system:manage:role:pageList")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(UserDTO userDTO) {
        startPage();
        List<UserDTO> list = roleService.selectAllocatedList(userDTO);
        return getDataTable(list);
    }

    /**
     * 逻辑删除角色表
     */
    @RequiresPermissions("system:manage:role:remove")
    @Log(title = "删除角色表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@Validated(RoleDTO.DeleteRoleDTO.class) @RequestBody RoleDTO roleDTO) {
        return toAjax(roleService.logicDeleteRoleByRoleId(roleDTO));
    }

    /**
     * 逻辑批量删除角色表
     */
    @RequiresPermissions("system:manage:role:removes")
    @Log(title = "批量删除角色表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> roleIds) {
        return toAjax(roleService.logicDeleteRoleByRoleIds(roleIds));
    }
}
