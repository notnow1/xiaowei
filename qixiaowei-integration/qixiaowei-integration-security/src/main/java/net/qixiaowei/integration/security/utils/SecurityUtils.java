package net.qixiaowei.integration.security.utils;

import javax.servlet.http.HttpServletRequest;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.TokenConstants;
import net.qixiaowei.integration.common.context.SecurityContextHolder;
import net.qixiaowei.integration.common.utils.ServletUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 权限获取工具类
 */
public class SecurityUtils {
    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return SecurityContextHolder.getUserId();
    }

    /**
     * 获取用户帐号
     */
    public static String getUserAccount() {
        return SecurityContextHolder.getUserAccount();
    }

    /**
     * 获取租户ID
     */
    public static Long getTenantId() {
        return SecurityContextHolder.getTenantId();
    }

    /**
     * 获取员工ID
     */
    public static Long getEmployeeId() {
        return SecurityContextHolder.getEmployeeId();
    }

    /**
     * 获取销售云token
     */
    public static String getSalesToken() {
        return SecurityContextHolder.getSalesToken();
    }

    /**
     * 获取登录用户信息
     */
    public static LoginUserVO getLoginUser() {
        return SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUserVO.class);
    }

    /**
     * 获取请求token
     */
    public static String getToken() {
        return getToken(ServletUtils.getRequest());
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request) {
        // 从header获取token标识
        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        return replaceTokenPrefix(token);
    }

    /**
     * 裁剪token前缀
     */
    public static String replaceTokenPrefix(String token) {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    /**
     * 是否为管理员
     *
     * @return 结果
     */
    public static boolean isAdmin() {
        return 1L == getUserId();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
