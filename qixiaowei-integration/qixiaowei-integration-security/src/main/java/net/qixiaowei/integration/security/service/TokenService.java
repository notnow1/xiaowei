package net.qixiaowei.integration.security.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.qixiaowei.integration.common.constant.CacheConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.utils.JwtUtils;
import net.qixiaowei.integration.common.utils.ServletUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.ip.IpUtils;
import net.qixiaowei.integration.common.utils.uuid.IdUtils;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;

/**
 * token验证处理
 */
@Component
public class TokenService {
    @Autowired
    private RedisService redisService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 创建令牌
     */
    public Map<String, Object> createToken(LoginUserVO loginUserVO) {
        String token = IdUtils.fastUUID();
        UserDTO userDTO = loginUserVO.getUserDTO();
        String salesCloudToken = loginUserVO.getSalesCloudToken();
        Long userId = userDTO.getUserId();
        String userAccount = userDTO.getUserAccount();
        String userName = userDTO.getUserName();
        Long employeeId = userDTO.getEmployeeId();
        Long tenantId = userDTO.getTenantId();
        loginUserVO.setToken(token);
        loginUserVO.setUserid(userId);
        loginUserVO.setUsername(userName);
        loginUserVO.setUserAccount(userAccount);
        loginUserVO.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        refreshToken(loginUserVO);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USER_ACCOUNT, userAccount);
        claimsMap.put(SecurityConstants.DETAILS_TENANT_ID, tenantId);
        claimsMap.put(SecurityConstants.DETAILS_EMPLOYEE_ID, employeeId);
        claimsMap.put(SecurityConstants.SALES_TOKEN_NAME,salesCloudToken);

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("sales_token", salesCloudToken);
        rspMap.put("expires_in", expireTime);
        return rspMap;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUserVO getLoginUser() {
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUserVO getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUserVO getLoginUser(String token) {
        LoginUserVO user = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userkey = JwtUtils.getUserKey(token);
                user = redisService.getCacheObject(getTokenKey(userkey));
                return user;
            }
        } catch (Exception e) {
        }
        return user;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUserVO loginUserVO) {
        if (StringUtils.isNotNull(loginUserVO) && StringUtils.isNotEmpty(loginUserVO.getToken())) {
            refreshToken(loginUserVO);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userkey = JwtUtils.getUserKey(token);
            redisService.deleteObject(getTokenKey(userkey));
        }
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUserVO
     */
    public void verifyToken(LoginUserVO loginUserVO) {
        long expireTime = loginUserVO.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUserVO);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUserVO 登录信息
     */
    public void refreshToken(LoginUserVO loginUserVO) {
        loginUserVO.setLoginTime(System.currentTimeMillis());
        loginUserVO.setExpireTime(loginUserVO.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUserVO.getToken());
        redisService.setCacheObject(userKey, loginUserVO, expireTime, TimeUnit.MINUTES);
    }

    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }
}