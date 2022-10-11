package net.qixiaowei.auth.service;

import java.util.concurrent.TimeUnit;

import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.qixiaowei.integration.common.constant.CacheConstants;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.integration.security.utils.SecurityUtils;

/**
 * 登录密码方法
 */
@Component
public class SysPasswordService {
    @Autowired
    private RedisService redisService;

    private int maxRetryCount = CacheConstants.PASSWORD_MAX_RETRY_COUNT;

    private Long lockTime = CacheConstants.PASSWORD_LOCK_TIME;

    @Autowired
    private SysRecordLogService recordLogService;

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param userAccount 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String userAccount) {
        return CacheConstants.PWD_ERR_CNT_KEY + userAccount;
    }

    public void validate(UserDTO user, String password) {
        String userAccount = user.getUserAccount();

        Integer retryCount = redisService.getCacheObject(getCacheKey(userAccount));

        if (retryCount == null) {
            retryCount = 0;
        }

        if (retryCount >= Integer.valueOf(maxRetryCount).intValue()) {
            String errMsg = String.format("密码输入错误%s次，帐户锁定%s分钟", maxRetryCount, lockTime);
//            recordLogService.recordLoginInfo(userAccount, Constants.LOGIN_FAIL, errMsg);
            throw new ServiceException(errMsg);
        }

        if (!matches(user, password)) {
            retryCount = retryCount + 1;
//            recordLogService.recordLoginInfo(userAccount, Constants.LOGIN_FAIL, String.format("密码输入错误%s次", retryCount));
            redisService.setCacheObject(getCacheKey(userAccount), retryCount, lockTime, TimeUnit.MINUTES);
            throw new ServiceException("用户不存在/密码错误");
        } else {
            clearLoginRecordCache(userAccount);
        }
    }

    public boolean matches(UserDTO user, String rawPassword) {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName) {
        if (redisService.hasKey(getCacheKey(loginName))) {
            redisService.deleteObject(getCacheKey(loginName));
        }
    }
}
