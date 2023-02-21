package net.qixiaowei.system.manage.controller.system;

import java.util.List;
import java.util.stream.Collectors;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.dto.system.RoleAuthUsersDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
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
     * 生成角色编码
     *
     * @return 角色编码
     */
    @RequiresPermissions(value = {"system:manage:role:add", "system:manage:role:edit"}, logical = Logical.OR)
    @GetMapping("/generate/roleCode")
    public AjaxResult generateRoleCode() {
        return AjaxResult.success("操作成功",roleService.generateRoleCode());
    }

    /**
     * 新增角色表
     */
    @Log(title = "新增角色", businessType = BusinessType.ROLE, businessId = "roleId", operationType = OperationType.INSERT)
    @RequiresPermissions("system:manage:role:add")
    @PostMapping("/add")
    public AjaxResult addSave(@Validated(RoleDTO.AddRoleDTO.class) @RequestBody RoleDTO roleDTO) {
        return AjaxResult.success(roleService.insertRole(roleDTO));
    }

    /**
     * 修改角色表
     */
    @Log(title = "编辑角色", businessType = BusinessType.ROLE, businessId = "roleId", operationType = OperationType.UPDATE)
    @RequiresPermissions("system:manage:role:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@Validated(RoleDTO.UpdateRoleDTO.class) @RequestBody RoleDTO roleDTO) {
        return toAjax(roleService.updateRole(roleDTO));
    }

    /**
     * 查看角色信息
     *
     * @return 角色信息
     */
    @RequiresPermissions(value = {"system:manage:role:info", "system:manage:role:edit"}, logical = Logical.OR)
    @GetMapping("/info/{roleId}")
    public AjaxResult info(@PathVariable Long roleId) {
        return AjaxResult.success(roleService.selectRoleByRoleId(roleId));
    }

    /**
     * 逻辑删除角色表
     */
    @RequiresPermissions("system:manage:role:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@Validated(RoleDTO.DeleteRoleDTO.class) @RequestBody RoleDTO roleDTO) {
        return toAjax(roleService.logicDeleteRoleByRoleId(roleDTO));
    }

    /**
     * 逻辑批量删除角色表
     */
    @RequiresPermissions("system:manage:role:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> roleIds) {
        return toAjax(roleService.logicDeleteRoleByRoleIds(roleIds));
    }

    /**
     * 角色授权用户
     */
    @RequiresPermissions("system:manage:role:authUsers")
    @PostMapping("/authUsers")
    public AjaxResult authUsers(@Validated @RequestBody RoleAuthUsersDTO roleAuthUsersDTO) {
        roleService.authUsers(roleAuthUsersDTO);
        return success();
    }

    /**
     * 查询已分配用户角色列表
     */
    @RequiresPermissions("system:manage:role:info")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(UserDTO userDTO) {
        startPage();
        List<UserDTO> list = roleService.selectAllocatedList(userDTO);
        return getDataTable(list);
    }

    /**
     * 查询角色表列表
     */
    @GetMapping("/list")
    public AjaxResult list(RoleDTO roleDTO) {
        List<RoleDTO> list = roleService.selectRoleList(roleDTO);
        list = SecurityUtils.isAdmin() ? list.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()) : list;
        return AjaxResult.success(list);
    }
}
