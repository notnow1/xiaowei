package net.qixiaowei.integration.security.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.qixiaowei.integration.security.auth.AuthUtil;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.context.SecurityContextHolder;
import net.qixiaowei.integration.common.utils.ServletUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        SecurityContextHolder.setUserId(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_ID));
        SecurityContextHolder.setUserAccount(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_ACCOUNT));
        SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants.USER_KEY));
        SecurityContextHolder.setTenantId(ServletUtils.getHeader(request, SecurityConstants.DETAILS_TENANT_ID));
        SecurityContextHolder.setEmployeeId(ServletUtils.getHeader(request, SecurityConstants.DETAILS_EMPLOYEE_ID));
        SecurityContextHolder.setSalesToken(ServletUtils.getHeader(request, SecurityConstants.SALES_TOKEN_NAME));

        String token = SecurityUtils.getToken();
        if (StringUtils.isNotEmpty(token)) {
            LoginUserVO loginUserVO = AuthUtil.getLoginUser(token);
            if (StringUtils.isNotNull(loginUserVO)) {
                AuthUtil.verifyLoginUserExpire(loginUserVO);
                SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUserVO);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        SecurityContextHolder.remove();
    }
}
