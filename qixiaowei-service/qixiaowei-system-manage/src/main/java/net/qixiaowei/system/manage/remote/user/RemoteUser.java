package net.qixiaowei.system.manage.remote.user;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import net.qixiaowei.system.manage.api.vo.UserVO;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;
import net.qixiaowei.system.manage.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @PostMapping("/resetPwdOfUserId")
    public R<?> resetPwdOfUserId(Long userId, String password, String source) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setPassword(password);
        return R.ok(userService.resetPwd(userDTO));
    }

    @Override
    public R<Boolean> registerUserInfo(UserVO userVO, String source) {
        return null;
    }
}
