package net.qixiaowei.integration.common.constant;

/**
 * 缓存常量信息
 */
public interface CacheConstants {
    /**
     * 缓存有效期，默认720（分钟）
     */
    long EXPIRATION = 720;

    /**
     * 缓存刷新时间，默认120（分钟）
     */
    long REFRESH_TIME = 120;

    /**
     * 密码最大错误次数
     */
    int PASSWORD_MAX_RETRY_COUNT = 5;

    /**
     * 密码重置最大错误次数
     */
    int PASSWORD_MAX_RESET_COUNT = 5;

    /**
     * 密码锁定时间，默认10（分钟）
     */
    long PASSWORD_LOCK_TIME = 10;

    /**
     * 权限缓存前缀
     */
    String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码 redis key
     */
    String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 参数管理 cache key
     */
    String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    String SYS_DICT_KEY = "sys_dict:";

    /**
     * 登录账户密码错误次数 redis key
     */
    String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 重置账户密码错误次数 redis key
     */
    String PWD_RESET_ERR_CNT_KEY = "pwd_reset_err_cnt:";

    /**
     * 租户ID集合 redis key
     */
    String TENANT_IDS_KEY = "tenant_ids:";
}
