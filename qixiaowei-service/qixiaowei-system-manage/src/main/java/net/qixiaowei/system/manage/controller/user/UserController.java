package net.qixiaowei.system.manage.controller.user;

import java.util.List;

import net.qixiaowei.system.manage.api.dto.user.AuthRolesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
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
        return getDataTable(list);
    }

    /**
     * 查询用户表列表
     */
    @RequiresPermissions("system:manage:user:list")
    @GetMapping("/list")
    public AjaxResult list(UserDTO userDTO) {
        List<UserDTO> list = userService.selectUserList(userDTO);
        return AjaxResult.success(list);
    }

    /**
     * 新增用户表
     */
    @RequiresPermissions("system:manage:user:add")
    @Log(title = "新增用户表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@Validated(UserDTO.AddUserDTO.class) @RequestBody UserDTO userDTO) {
        return AjaxResult.success(userService.insertUser(userDTO));
    }

    /**
     * 查看用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/info/{userId}")
    public AjaxResult info(@PathVariable Long userId) {
        return AjaxResult.success(userService.selectUserByUserId(userId));
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
     * 修改用户表
     */
    @RequiresPermissions("system:manage:user:edit")
    @Log(title = "修改用户表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@Validated(UserDTO.UpdateUserDTO.class) @RequestBody UserDTO userDTO) {
        return toAjax(userService.updateUser(userDTO));
    }

    /**
     * 逻辑删除用户表
     */
    @RequiresPermissions("system:manage:user:remove")
    @Log(title = "删除用户表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@Validated(UserDTO.DeleteUserDTO.class) @RequestBody UserDTO userDTO) {
        return toAjax(userService.logicDeleteUserByUserId(userDTO));
    }

    /**
     * 逻辑批量删除用户表
     */
    @RequiresPermissions("system:manage:user:removes")
    @Log(title = "批量删除用户表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> userIds) {
        return toAjax(userService.logicDeleteUserByUserIds(userIds));
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

    /**
     * 重置密码
     */
    @RequiresPermissions("system:manage:user:resetPwd")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    public AjaxResult resetPwd(@Validated(UserDTO.DeleteUserDTO.class) @RequestBody UserDTO userDTO) {
        return toAjax(userService.resetPwd(userDTO));
    }

    /**
     * 用户授权角色
     */
    @RequiresPermissions("system:manage:user:authRoles")
    @Log(title = "用户授权角色", businessType = BusinessType.GRANT)
    @PostMapping("/authRoles")
    public AjaxResult authRoles(@Validated @RequestBody AuthRolesDTO authRolesDTO) {
        userService.authRoles(authRolesDTO);
        return success();
    }

}
