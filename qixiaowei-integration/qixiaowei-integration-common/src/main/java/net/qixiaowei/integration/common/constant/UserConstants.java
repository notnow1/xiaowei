package net.qixiaowei.integration.common.constant;

/**
 * 用户常量信息
 *
 */
public interface UserConstants
{
    /**
     * 平台内系统用户的唯一标志
     */
    String SYS_USER = "SYS_USER";

    /**
     * 正常状态
     */
    String NORMAL = "0";

    /**
     * 异常状态
     */
    String EXCEPTION = "1";

    /**
     * 用户封禁状态
     */
    String USER_DISABLE = "1";

    /**
     * 角色封禁状态
     */
    String ROLE_DISABLE = "1";

    /**
     * 部门正常状态
     */
    String DEPT_NORMAL = "0";

    /**
     * 部门停用状态
     */
    String DEPT_DISABLE = "1";

    /**
     * 字典正常状态
     */
    String DICT_NORMAL = "0";

    /**
     * 是否为系统默认（是）
     */
    String YES = "Y";

    /**
     * 菜单类型（目录）
     */
    Integer TYPE_DIR = 1;

    /**
     * 菜单类型（菜单）
     */
    Integer TYPE_MENU = 2;

    /**
     * 菜单类型（按钮）
     */
    Integer TYPE_BUTTON = 3;

    /**
     * Layout组件标识
     */
    public final static String LAYOUT = "Layout";

    /**
     * ParentView组件标识
     */
    public final static String PARENT_VIEW = "ParentView";

    /**
     * InnerLink组件标识
     */
    public final static String INNER_LINK = "InnerLink";

    /**
     * 校验返回结果码
     */
    public final static String UNIQUE = "0";

    public final static String NOT_UNIQUE = "1";

    /**
     * 用户名长度限制
     */
    int USERNAME_MIN_LENGTH = 3;

    int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码长度限制
     */
    int PASSWORD_MIN_LENGTH = 6;

    int PASSWORD_MAX_LENGTH = 20;
}
