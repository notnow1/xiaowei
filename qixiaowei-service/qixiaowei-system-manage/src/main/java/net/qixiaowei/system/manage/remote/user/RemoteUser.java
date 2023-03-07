package net.qixiaowei.system.manage.remote.user;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.integration.tenant.annotation.IgnoreTenant;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;
import net.qixiaowei.system.manage.api.vo.tenant.TenantRegisterResponseVO;
import net.qixiaowei.system.manage.service.tenant.ITenantService;
import net.qixiaowei.system.manage.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @Author hzk
 * @Date 2022-10-06 16:39
 **/
@RestController
@RequestMapping("/user")
public class RemoteUser implements RemoteUserService {

    @Autowired
    private IUserService userService;

    @Autowired
    private ITenantService tenantService;

    @Override
    @InnerAuth
    @GetMapping("/info")
    public R<LoginUserVO> getUserInfo(String userAccount, String domain, String source) {
        return R.ok(userService.getUserByUserAccount(userAccount, domain));
    }

    @Override
    @InnerAuth
    @GetMapping("/infoByUserId")
    public R<UserDTO> getUserInfoByUserId(Long UserId, String source) {
        return R.ok(userService.selectUserByUserId(UserId));
    }

    @Override
    @InnerAuth
    @PostMapping("/usersByUserIds")
    public R<List<UserDTO>> getUsersByUserIds(@RequestBody Set<Long> userIds, String source) {
        return R.ok(userService.getUsersByUserIds(userIds));
    }

    @Override
    @InnerAuth
    @IgnoreTenant
    @PostMapping("/resetPwdOfUserId")
    public R<?> resetPwdOfUserId(Long userId, String password, String source) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setPassword(password);
        return R.ok(userService.resetPwd(userDTO));
    }

    /**
     * 校验用户是否存在
     *
     * @param userAccount
     * @return 结果
     */
    @Override
    @InnerAuth
    @PostMapping("/checkUserAccountExists")
    public R<Boolean> checkUserAccountExists(String userAccount, String source) {
        return R.ok(userService.checkUserAccountExists(userAccount));
    }

    @Override
    @InnerAuth
    @PostMapping("/register")
    public R<TenantRegisterResponseVO> registerUserInfo(TenantDTO tenantDTO, String source) {
        return R.ok(tenantService.registerUserInfo(tenantDTO));
    }

    /**
     * 通过人员ID集合查询用户id
     *
     * @param employeeIds 人员IDs
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/selectByemployeeIds")
    public R<List<UserDTO>> selectByemployeeIds(List<Long> employeeIds, String source) {
        return R.ok(userService.selectByemployeeIds(employeeIds));
    }

    /**
     * 远程查询用户信息 支持模糊查询等等
     * @param userDTO
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteSelectUserList")
    public R<List<UserDTO>> remoteSelectUserList(UserDTO userDTO, String source) {
        return R.ok(userService.selectUserList(userDTO));
    }
}
