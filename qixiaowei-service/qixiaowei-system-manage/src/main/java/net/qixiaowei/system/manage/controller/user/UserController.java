package net.qixiaowei.system.manage.controller.user;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
    public AjaxResult addSave(@RequestBody UserDTO userDTO) {
        return toAjax(userService.insertUser(userDTO));
    }


    /**
     * 修改用户表
     */
    @RequiresPermissions("system:manage:user:edit")
    @Log(title = "修改用户表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody UserDTO userDTO) {
        return toAjax(userService.updateUser(userDTO));
    }

    /**
     * 逻辑删除用户表
     */
    @RequiresPermissions("system:manage:user:remove")
    @Log(title = "删除用户表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody UserDTO userDTO) {
        return toAjax(userService.logicDeleteUserByUserId(userDTO));
    }

    /**
     * 批量修改用户表
     */
    @RequiresPermissions("system:manage:user:edits")
    @Log(title = "批量修改用户表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<UserDTO> userDtos) {
        return toAjax(userService.updateUsers(userDtos));
    }

    /**
     * 批量新增用户表
     */
    @RequiresPermissions("system:manage:user:insertUsers")
    @Log(title = "批量新增用户表", businessType = BusinessType.INSERT)
    @PostMapping("/insertUsers")
    public AjaxResult insertUsers(@RequestBody List<UserDTO> userDtos) {
        return toAjax(userService.insertUsers(userDtos));
    }

    /**
     * 逻辑批量删除用户表
     */
    @RequiresPermissions("system:manage:user:removes")
    @Log(title = "批量删除用户表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<UserDTO> UserDtos) {
        return toAjax(userService.logicDeleteUserByUserIds(UserDtos));
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
