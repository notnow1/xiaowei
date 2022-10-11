package net.qixiaowei.auth.form;

import lombok.Data;

/**
 * 用户登录对象
 */
@Data
public class LoginBody {
    /**
     * 用户帐号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String password;

}
