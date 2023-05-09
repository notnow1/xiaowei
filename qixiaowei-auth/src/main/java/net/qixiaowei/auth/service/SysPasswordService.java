package net.qixiaowei.auth.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.UserConstants;
import net.qixiaowei.integration.common.enums.user.UserStatus;
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
     * @param domain      域名
     * @return 缓存键key
     */
    public String getLoginCacheKeyContainDomain(String userAccount, String domain) {
        return this.getLoginCacheKey(userAccount) + ":" + domain;
    }

    /**
     * 重置密码错误次数缓存键名
     *
     * @param userAccount 用户名
     * @param domain      域名
     * @return 缓存键key
     */
    private String getResetCacheKeyContainDomain(String userAccount, String domain) {
        return this.getResetCacheKey(userAccount) + ":" + domain;
    }

    /**
     * 重置密码间隔缓存键名
     *
     * @param userAccount 用户名
     * @param domain      域名
     * @return 缓存键key
     */
    public String getResetIntervalCacheKeyContainDomain(String userAccount, String domain) {
        return this.getResetIntervalCacheKey(userAccount) + ":" + domain;
    }

    /**
     * @description: 前置校验-帐号、密码规范
     * @Author: hzk
     * @date: 2023/4/27 17:59
     * @param: [cacheKey, retryCount, userAccount, password]
     * @return: void
     **/
    public void preValidate(String cacheKey, Integer retryCount, String userAccount, String password) {
        this.checkErrorCount(cacheKey, retryCount);
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(userAccount, password)) {
            this.incrementRetryCount(cacheKey, retryCount);
            throw new ServiceException("账号或密码有误，请重新输入");
        }
        // 密码如果不在指定范围内 错误
        if (this.checkPasswordLength(password)) {
            this.incrementRetryCount(cacheKey, retryCount);
            throw new ServiceException("账号或密码有误，请重新输入");
        }
        // 用户名不在指定范围内 错误 20230111 需求更改，帐号长度范围改为1-20
        if (this.checkAccountLength(userAccount)) {
            this.incrementRetryCount(cacheKey, retryCount);
            throw new ServiceException("账号或密码有误，请重新输入");
        }
    }

    /**
     * @description: 校验-帐号状态、密码情况
     * @Author: hzk
     * @date: 2023/4/27 17:59
     * @param: [user, password, cacheKey, retryCount]
     * @return: void
     **/
    public void validate(UserDTO user, String password, String cacheKey, Integer retryCount) {
        this.checkErrorCount(cacheKey, retryCount);
        if (!matches(user, password)) {
            this.incrementRetryCount(cacheKey, retryCount);
            throw new ServiceException("账号或密码有误，请重新输入");
        } else {
            if (DBDeleteFlagConstants.DELETE_FLAG_ONE.equals(user.getDeleteFlag()) || !UserStatus.OK.getCode().equals(user.getStatus())) {
                this.incrementRetryCount(cacheKey, retryCount);
                throw new ServiceException("账号或密码有误，请重新输入");
            }
            clearLoginRecordCache(cacheKey);
        }
    }

    /**
     * @description: 重置密码校验
     * @Author: hzk
     * @date: 2023/1/12 11:59
     * @param: [userAccount, domain]
     * @return: void
     **/
    public void validateOfReset(String userAccount, String domain) {
        String cacheKey = this.getResetCacheKeyContainDomain(userAccount, domain);
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

    /**
     * @description: 校验重置密码间隔
     * @Author: hzk
     * @date: 2023/4/27 18:01
     * @param: [intervalCacheKey]
     * @return: void
     **/
    public void checkResetInterval(String intervalCacheKey) {
        if (redisService.hasKey(intervalCacheKey)) {
            long expire = redisService.getExpire(intervalCacheKey);
            String errorTimeDesc = this.getErrorTimeDesc(expire);
            String errMsg = String.format("请等待%s后再重置密码。", errorTimeDesc);
            throw new ServiceException(errMsg);
        }
    }

    /**
     * @description: 累加重试次数
     * @Author: hzk
     * @date: 2023/4/27 18:02
     * @param: [cacheKey, retryCount]
     * @return: void
     **/
    public void incrementRetryCount(String cacheKey, Integer retryCount) {
        retryCount = retryCount + 1;
        setRetryCount(cacheKey, retryCount);
    }

    /**
     * @description: 校验密码长度
     * @Author: hzk
     * @date: 2023/4/27 18:02
     * @param: [password]
     * @return: boolean
     **/
    private boolean checkPasswordLength(String password) {
        return password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH;
    }

    /**
     * @description: 校验帐号长度
     * @Author: hzk
     * @date: 2023/4/27 18:03
     * @param: [userAccount]
     * @return: boolean
     **/
    private boolean checkAccountLength(String userAccount) {
        return userAccount.length() > UserConstants.USERNAME_MAX_LENGTH;
    }

    /**
     * @description: 设置重置次数
     * @Author: hzk
     * @date: 2023/4/27 18:03
     * @param: [cacheKey, retryCount]
     * @return: void
     **/
    private void setRetryCount(String cacheKey, Integer retryCount) {
        long expireTime = lockTime;
        if (retryCount < 10) {
            Date nowDate = DateUtils.getNowDate();
            Date endOfDay = DateUtil.endOfDay(nowDate);
            expireTime = DateUtil.between(nowDate, endOfDay, DateUnit.SECOND);
        }
        redisService.setCacheObject(cacheKey, retryCount, expireTime, TimeUnit.SECONDS);
    }

    /**
     * @description: 获取重试次数
     * @Author: hzk
     * @date: 2023/4/27 18:04
     * @param: [cacheKey]
     * @return: java.lang.Integer
     **/
    public Integer getRetryCount(String cacheKey) {
        Integer retryCount = redisService.getCacheObject(cacheKey);
        if (retryCount == null) {
            retryCount = 0;
        }
        return retryCount;
    }

    /**
     * @description: 校验错误次数
     * @Author: hzk
     * @date: 2023/4/27 18:04
     * @param: [cacheKey, retryCount]
     * @return: void
     **/
    private void checkErrorCount(String cacheKey, Integer retryCount) {
        if (retryCount >= Integer.valueOf(maxRetryCount).intValue()) {
            long expireTime = redisService.getExpire(cacheKey);
            String errorTimeDesc = this.getErrorTimeDesc(expireTime);
            String errMsg = String.format("账号或密码多次输入错误，请在%s后重试或找回密码", errorTimeDesc);
            throw new ServiceException(errMsg);
        }
    }

}
