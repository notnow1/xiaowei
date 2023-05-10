package net.qixiaowei.auth.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.qixiaowei.integration.common.constant.CacheConstants;
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

    private int maxResetRetryCount = CacheConstants.PASSWORD_MAX_RESET_COUNT;

    private Long lockTime = CacheConstants.PASSWORD_LOCK_TIME;

    @Autowired
    private SysRecordLogService recordLogService;

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param userAccount 用户帐号
     * @return 缓存键key
     */
    private String getLoginCacheKey(String userAccount) {
        return CacheConstants.PWD_ERR_CNT_KEY + userAccount;
    }

    /**
     * 重置密码错误次数缓存键名
     *
     * @param userAccount 用户帐号
     * @return 缓存键key
     */
    private String getResetCacheKey(String userAccount) {
        return CacheConstants.PWD_RESET_ERR_CNT_KEY + userAccount;
    }

    /**
     * 重置密码错误间隔缓存键名
     *
     * @param userAccount 用户帐号
     * @return 缓存键key
     */
    private String getResetIntervalCacheKey(String userAccount) {
        return CacheConstants.PWD_RESET_INTERVAL_KEY + userAccount;
    }

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param userAccount 用户名
     * @param tenantId    租户ID
     * @return 缓存键key
     */
    public String getLoginCacheKeyContainTenantId(String userAccount, Long tenantId) {
        return this.getLoginCacheKey(userAccount) + ":" + tenantId;
    }

    /**
     * 重置密码错误次数缓存键名
     *
     * @param userAccount 用户名
     * @param tenantId    租户ID
     * @return 缓存键key
     */
    private String getResetCacheKeyContainTenantId(String userAccount, Long tenantId) {
        return this.getResetCacheKey(userAccount) + ":" + tenantId;
    }

    /**
     * 重置密码间隔缓存键名
     *
     * @param userAccount 用户名
     * @param tenantId    租户ID
     * @return 缓存键key
     */
    public String getResetIntervalCacheKeyContainTenantId(String userAccount, Long tenantId) {
        return this.getResetIntervalCacheKey(userAccount) + ":" + tenantId;
    }

    public void validate(UserDTO user, String password) {
        String userAccount = user.getUserAccount();
        Long tenantId = user.getTenantId();
        String cacheKey = this.getLoginCacheKeyContainTenantId(userAccount, tenantId);
        Integer retryCount = redisService.getCacheObject(cacheKey);
        if (retryCount == null) {
            retryCount = 0;
        }
        if (retryCount >= Integer.valueOf(maxRetryCount).intValue()) {
            long expireTime = redisService.getExpire(cacheKey);
            String errorTimeDesc = this.getErrorTimeDesc(expireTime);
            String errMsg = String.format("账号或密码多次输入错误，请在%s后重试或找回密码", errorTimeDesc);
            throw new ServiceException(errMsg);
        }
        if (!matches(user, password)) {
            retryCount = retryCount + 1;
            long expireTime = lockTime;
            if (retryCount < 10) {
                Date nowDate = DateUtils.getNowDate();
                Date endOfDay = DateUtil.endOfDay(nowDate);
                expireTime = DateUtil.between(nowDate, endOfDay, DateUnit.SECOND);
            }
            redisService.setCacheObject(cacheKey, retryCount, expireTime, TimeUnit.SECONDS);
            throw new ServiceException("账号或密码有误，请重新输入");
        } else {
            clearLoginRecordCache(cacheKey);
        }
    }

    /**
     * @description: 重置密码校验
     * @Author: hzk
     * @date: 2023/1/12 11:59
     * @param: [userAccount, tenantId]
     * @return: void
     **/
    public void validateOfReset(String userAccount, Long tenantId) {
        boolean tenantNull = StringUtils.isNull(tenantId);
        String cacheKey = tenantNull ? this.getResetCacheKey(userAccount) : this.getResetCacheKeyContainTenantId(userAccount, tenantId);
        Integer resetRetryCount = redisService.getCacheObject(cacheKey);
        if (null == resetRetryCount) {
            resetRetryCount = 0;
        }
        if (resetRetryCount >= Integer.valueOf(maxResetRetryCount).intValue()) {
            throw new ServiceException("今日忘记密码次数已用完。");
        } else {
            resetRetryCount = resetRetryCount + 1;
            Date nowDate = DateUtils.getNowDate();
            Date endOfDay = DateUtil.endOfDay(nowDate);
            long betweenMinute = DateUtil.between(nowDate, endOfDay, DateUnit.MINUTE);
            redisService.setCacheObject(cacheKey, resetRetryCount, betweenMinute, TimeUnit.MINUTES);
        }
    }

    public boolean matches(UserDTO user, String rawPassword) {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String cacheKey) {
        if (redisService.hasKey(cacheKey)) {
            redisService.deleteObject(cacheKey);
        }
    }

    /**
     * @description: 获取倒数秒数的中文时间错误信息
     * @Author: hzk
     * @date: 2023/4/21 15:57
     * @param: [second]
     * @return: java.lang.String
     **/
    public String getErrorTimeDesc(long second) {
        String errorTimeDesc;
        if (second < 60) {
            errorTimeDesc = second + "秒";
        } else if (second % 60 == 0) {
            errorTimeDesc = second / 60 + "分";
        } else {
            errorTimeDesc = second / 60 + "分" + second % 60 + "秒";
        }
        return errorTimeDesc;
    }

}
