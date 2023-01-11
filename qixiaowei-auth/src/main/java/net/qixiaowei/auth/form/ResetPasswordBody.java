package net.qixiaowei.auth.form;

import lombok.Data;

/**
 * 重置密码对象
 */
@Data
public class ResetPasswordBody {

    /**
     * 用户帐号
     */
    private String userAccount;

    /**
     * 邮箱
     */
    private String email;
}
