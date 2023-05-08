package net.qixiaowei.system.manage.api.domain.user;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
 * 用户表
 *
 * @author hzk
 * @since 2022-10-05
 */
@Data
@Accessors(chain = true)
public class User extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long userId;
    /**
     * 用户类型:0其他;1系统管理员
     */
    private Integer userType;
    /**
     * 员工ID
     */
    private Long employeeId;
    /**
     * 用户帐号
     */
    private String userAccount;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 手机号码
     */
    private String mobilePhone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 状态:0失效;1生效;2未激活
     */
    private Integer status;

}

