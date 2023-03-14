package net.qixiaowei.system.manage.controller.user;

import java.util.List;
import java.util.Set;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.system.manage.api.dto.user.AuthRolesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.service.user.IUserService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author hzk
 * @since 2022-10-05
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController {


    @Autowired
    private IUserService userService;


    /**
     * 分页查询用户表列表
     */
    @RequiresPermissions("system:manage:user:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(UserDTO userDTO) {
        startPage();
        List<UserDTO> list = userService.selectUserList(userDTO);
        userService.handleResult(list);
        return getDataTable(list);
    }

    /**
     * 新增用户表
     */
    @Log(title = "新增账号", businessType = BusinessType.USER, businessId = "userId", operationType = OperationType.INSERT)
    @RequiresPermissions("system:manage:user:add")
    @PostMapping("/add")
    public AjaxResult addSave(@Validated(UserDTO.AddUserDTO.class) @RequestBody UserDTO userDTO) {
        return AjaxResult.success(userService.insertUser(userDTO));
    }

    /**
     * 修改用户表
     */
    @Log(title = "编辑账号", businessType = BusinessType.USER, businessId = "userId", operationType = OperationType.UPDATE)
    @RequiresPermissions("system:manage:user:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@Validated(UserDTO.UpdateUserDTO.class) @RequestBody UserDTO userDTO) {
        return toAjax(userService.updateUser(userDTO));
    }

    /**
     * 查看用户信息
     *
     * @return 用户信息
     */
    @RequiresPermissions(value = {"system:manage:user:info", "system:manage:user:edit"}, logical = Logical.OR)
    @GetMapping("/info/{userId}")
    public AjaxResult info(@PathVariable Long userId) {
        return AjaxResult.success(userService.selectUserByUserId(userId));
    }

    /**
     * 逻辑删除用户表
     */
    @RequiresPermissions("system:manage:user:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@Validated(UserDTO.DeleteUserDTO.class) @RequestBody UserDTO userDTO) {
        return toAjax(userService.logicDeleteUserByUserId(userDTO));
    }

    /**
     * 逻辑批量删除用户表
     */
    @RequiresPermissions("system:manage:user:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody Set<Long> userIds) {
        return toAjax(userService.logicDeleteUserByUserIds(userIds));
    }

    /**
     * 重置密码
     */
    @Log(title = "重置密码", businessType = BusinessType.USER, businessId = "userId", operationType = OperationType.UPDATE)
    @RequiresPermissions("system:manage:user:resetPwd")
    @PostMapping("/resetPwd")
    public AjaxResult resetPwd(@Validated(UserDTO.DeleteUserDTO.class) @RequestBody UserDTO userDTO) {
        return toAjax(userService.resetPwd(userDTO));
    }

    /**
     * 用户授权角色
     */
    @RequiresPermissions("system:manage:user:authRoles")
    @PostMapping("/authRoles")
    public AjaxResult authRoles(@Validated @RequestBody AuthRolesDTO authRolesDTO) {
        userService.authRoles(authRolesDTO);
        return success();
    }

    /**
     * 查询未分配用户员工列表
     */
    @RequiresPermissions(value = {"system:manage:user:add", "system:manage:user:edit"}, logical = Logical.OR)
    @GetMapping("/unallocatedEmployees")
    public AjaxResult unallocatedEmployees(@RequestParam(value = "userId", required = false) Long userId) {
        return AjaxResult.success(userService.unallocatedEmployees(userId));
    }

    /**
     * 查看用户授权角色列表
     *
     * @return 用户信息
     */
    @GetMapping("/allocatedRoles/{userId}")
    public AjaxResult userRoles(@PathVariable Long userId) {
        return AjaxResult.success(userService.selectUserRolesByUserId(userId));
    }

    /**
     * 查询用户表列表
     */
    @GetMapping("/list")
    public AjaxResult list(UserDTO userDTO) {
        List<UserDTO> list = userService.selectUserList(userDTO);
        return AjaxResult.success(list);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        return AjaxResult.success(userService.getInfo());
    }

}
