package net.qixiaowei.integration.common.constant;

/**
 * 通用常量信息
 */
public interface Constants {
    /**
     * UTF-8 字符集
     */
    String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    String GBK = "GBK";

    /**
     * www主域
     */
    String WWW = "www.";

    /**
     * RMI 远程方法调用
     */
    String LOOKUP_RMI = "rmi:";

    /**
     * LDAP 远程方法调用
     */
    String LOOKUP_LDAP = "ldap:";

    /**
     * LDAPS 远程方法调用
     */
    String LOOKUP_LDAPS = "ldaps:";

    /**
     * http请求
     */
    String HTTP = "http://";

    /**
     * https请求
     */
    String HTTPS = "https://";

    /**
     * 成功标记
     */
    Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    Integer FAIL = 500;

    /**
     * 登录成功状态
     */
    String LOGIN_SUCCESS_STATUS = "0";

    /**
     * 登录失败状态
     */
    String LOGIN_FAIL_STATUS = "1";

    /**
     * 登录成功
     */
    String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    String LOGOUT = "Logout";

    /**
     * 注册
     */
    String REGISTER = "Register";

    /**
     * 登录失败
     */
    String LOGIN_FAIL = "Error";

    /**
     * 当前记录起始索引
     */
    String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    String IS_ASC = "isAsc";

    /**
     * 验证码有效期（分钟）
     */
    long CAPTCHA_EXPIRATION = 2;

    /**
     * 资源映射路径 前缀
     */
    String RESOURCE_PREFIX = "/profile";

    /**
     * 顶层父级ID
     */
    Long TOP_PARENT_ID = 0L;
    /**
     * 顶层部门父级ID
     */
    Long TOP_DEPT_PARENT_ID = 1L;
    /**
     * 标记：是
     */
    Integer FLAG_YES = 1;

    /**
     * 标记：否
     */
    Integer FLAG_NO = 0;

    /**
     * 英文冒号
     */
    String COLON_EN = ":";

    /**
     * 英文逗号
     */
    String COMMA_EN = ",";
    /**
     * 中文冒号
     */
    String China_Colon = "；";

    /**
     * 数字常量0
     */
    Integer ZERO = 0;
    /**
     * 数字常量1
     */
    Integer ONE = 1;

    /**
     * 数字常量2
     */
    Integer TWO = 2;

    /**
     * 数字常量3
     */
    Integer THREE = 3;

    /**
     * 数字常量4
     */
    Integer FOUR = 4;

    /**
     * 数字常量5
     */
    Integer FIVE = 5;

    /**
     * 数字常量6
     */
    Integer SIX = 6;
}
