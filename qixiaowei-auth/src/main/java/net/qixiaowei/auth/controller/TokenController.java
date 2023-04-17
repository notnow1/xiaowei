package net.qixiaowei.auth.controller;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.date.DateUtil;
import io.jsonwebtoken.Claims;
import net.qixiaowei.auth.form.RegisterBody;
import net.qixiaowei.auth.form.ResetPasswordBody;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.sign.SalesSignUtils;
import net.qixiaowei.sales.cloud.api.remote.sync.RemoteSyncAdminService;
import net.qixiaowei.sales.cloud.api.remote.sync.RemoteSyncAuthorizationService;
import net.qixiaowei.sales.cloud.api.vo.sync.SalesLoginVO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import net.qixiaowei.auth.form.LoginBody;
import net.qixiaowei.auth.service.SysLoginService;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.JwtUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.security.auth.AuthUtil;
import net.qixiaowei.integration.security.service.TokenService;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;

import java.util.Date;
import java.util.Map;

/**
 * token 控制
 */
@RestController
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private RemoteSyncAdminService remoteSyncAdminService;

    @Autowired
    private RemoteSyncAuthorizationService remoteSyncAuthorizationService;

    @PostMapping("/login")
    public R<?> login(HttpServletRequest request, @RequestBody LoginBody loginBody) {
        // 用户登录
        LoginUserVO userInfo = sysLoginService.login(request, loginBody.getUserAccount(), loginBody.getPassword());
        UserDTO userDTO = userInfo.getUserDTO();
        Boolean salesCloudFlag = userDTO.getSalesCloudFlag();
        String sales_token = "";
        if (salesCloudFlag) {
            String userAccount = userDTO.getUserAccount();
            Long tenantId = userDTO.getTenantId();
            Date nowDate = DateUtils.getNowDate();
            String time = DateUtil.formatDateTime(nowDate);
            String saleSign = SalesSignUtils.buildSaleSign(userAccount, time);
            R<SalesLoginVO> r = remoteSyncAdminService.syncLogin(userAccount, tenantId, time, saleSign);
            if (0 != r.getCode()) {
                throw new ServiceException("系统异常");
            }
            SalesLoginVO salesLoginVO = r.getData();
            if (StringUtils.isNotNull(salesLoginVO)) {
                sales_token = salesLoginVO.getAdminToken();
            }
        }
        userInfo.setSalesCloudToken(sales_token);
        // 获取登录token
        Map<String, Object> token = tokenService.createToken(userInfo);
        return R.ok(token);
    }

    @DeleteMapping("/logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.parseToken(token);
            String userSalesToken = JwtUtils.getUserSalesToken(claims);
            //如果销售云token不为空，做销售云的退出
            if (StringUtils.isNotEmpty(userSalesToken)) {
                R<?> r = remoteSyncAuthorizationService.syncUserLogout(userSalesToken);
                if (0 != r.getCode()) {
                    throw new ServiceException("退出异常");
                }
            }
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志 String
//             userAccount = JwtUtils.getUserAccount(token);
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

    /**
     * @description: 重置密码
     * @Author: hzk
     * @date: 2023/1/11 17:09
     * @param: [request, resetPasswordBody]
     * @return: net.qixiaowei.integration.common.domain.R<?>
     **/
    @PostMapping("/resetPwd")
    public R<?> resetPwd(HttpServletRequest request, @RequestBody ResetPasswordBody resetPasswordBody) {
        sysLoginService.resetPwd(request, resetPasswordBody);
        return R.ok();
    }

    /**
     * @description: 发送验证码
     * @Author: hzk
     * @date: 2023/2/20 14:46
     * @param: [loginBody]
     * @return: net.qixiaowei.integration.common.domain.R<?>
     **/
    @PostMapping("/sendSms")
    public R<?> sendSms(@RequestBody LoginBody loginBody) {
        sysLoginService.sendSms(loginBody.getUserAccount());
        return R.ok();
    }

    /**
     * @description: 注册
     * @Author: hzk
     * @date: 2023/2/24 19:24
     * @param: [registerBody]
     * @return: net.qixiaowei.integration.common.domain.R<?>
     **/
    @PostMapping("/register")
    public R<?> register(@Validated @RequestBody RegisterBody registerBody) {
        return R.ok(sysLoginService.register(registerBody));
    }
}
