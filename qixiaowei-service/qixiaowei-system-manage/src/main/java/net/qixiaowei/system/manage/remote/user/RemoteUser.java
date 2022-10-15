package net.qixiaowei.system.manage.remote.user;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.RemoteUserService;
import net.qixiaowei.system.manage.api.vo.UserVO;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;
import net.qixiaowei.system.manage.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public R<LoginUserVO> getUserInfo(String userAccount, String source) {
        return R.ok(userService.getUserByUserAccount(userAccount));
    }

    @Override
    public R<Boolean> registerUserInfo(UserVO userVO, String source) {
        return null;
    }
}
