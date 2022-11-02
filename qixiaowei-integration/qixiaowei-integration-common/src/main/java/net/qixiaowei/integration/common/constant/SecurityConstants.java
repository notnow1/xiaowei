package net.qixiaowei.integration.common.constant;

/**
 * 权限相关通用常量
 */
public interface SecurityConstants {
    /**
     * 用户ID字段
     */
    String DETAILS_USER_ID = "user_id";

    /**
     * 租户ID字段
     */
    String DETAILS_TENANT_ID = "tenant_id";

    /**
     * 员工ID字段
     */
    String DETAILS_EMPLOYEE_ID = "employee_id";

    /**
     * 用户帐号字段
     */
    String DETAILS_USER_ACCOUNT = "user_account";

    /**
     * 授权信息字段
     */
    String AUTHORIZATION_HEADER = "accessToken";

    /**
     * 请求来源
     */
    String FROM_SOURCE = "from-source";

    /**
     * 内部请求
     */
    String INNER = "inner";

    /**
     * 用户标识
     */
    String USER_KEY = "user_key";

    /**
     * 登录用户
     */
    String LOGIN_USER = "login_user";

    /**
     * 角色权限
     */
    String ROLE_PERMISSION = "role_permission";

    /**
     * 角色权限-所有
     */
    String ROLE_PERMISSION_ALL = "*:*:*";

    /**
     * 角色权限-所有
     */
    String ADMIN_FLAG = "admin_flag";
}
