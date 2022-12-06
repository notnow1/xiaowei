package net.qixiaowei.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import net.qixiaowei.auth.form.LoginBody;
import net.qixiaowei.auth.form.RegisterBody;
import net.qixiaowei.auth.service.SysLoginService;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.JwtUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.security.auth.AuthUtil;
import net.qixiaowei.integration.security.service.TokenService;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;

/**
 * token 控制
 */
@RestController
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("/login")
    public R<?> login(HttpServletRequest request, @RequestBody LoginBody loginBody) {
        // 用户登录
        LoginUserVO userInfo = sysLoginService.login(request, loginBody.getUserAccount(), loginBody.getPassword());
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }

    @DeleteMapping("/logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String userAccount = JwtUtils.getUserAccount(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
//            sysLoginService.logout(userAccount);
        }
        return R.ok();
    }

    @PostMapping("/refresh")
    public R<?> refresh(HttpServletRequest request) {
        LoginUserVO loginUserVO = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUserVO)) {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUserVO);
            return R.ok();
        }
        return R.ok();
    }

}
