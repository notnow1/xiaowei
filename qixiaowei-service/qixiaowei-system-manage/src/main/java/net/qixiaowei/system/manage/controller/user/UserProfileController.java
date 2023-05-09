package net.qixiaowei.system.manage.controller.user;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.dto.user.UserUpdatePasswordDTO;
import net.qixiaowei.system.manage.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息 业务处理
 *
 * @author hzk
 */
@RestController
@RequestMapping("/user/profile")
public class UserProfileController extends BaseController {

    @Autowired
    private IUserService userService;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult getProfile() {
        return AjaxResult.success(userService.getProfile());

    }

    /**
     * 修改用户资料
     */
    @PostMapping("/edit")
    public AjaxResult editProfile(@Validated(UserDTO.UpdateUserOfSelfDTO.class) @RequestBody UserDTO userDTO) {
        return toAjax(userService.editProfile(userDTO));
    }

    /**
     * 重置密码
     */
    @PostMapping("/updatePwd")
    public AjaxResult updatePwd(@Validated @RequestBody UserUpdatePasswordDTO userUpdatePasswordDTO) {
        return toAjax(userService.resetUserPwd(userUpdatePasswordDTO));
    }

    /**
     * 修改用户头像
     **/
    @PostMapping("/avatar")
    public AjaxResult editProfile(@RequestParam(value = "avatarFile") MultipartFile avatarFile) {
        return AjaxResult.success(userService.editAvatar(avatarFile));
    }


}
