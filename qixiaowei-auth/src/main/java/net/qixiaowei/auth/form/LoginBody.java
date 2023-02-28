package net.qixiaowei.auth.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录对象
 */
@Data
public class LoginBody {
    /**
     * 用户帐号
     */
    @NotBlank(message = "登录帐号不能为空")
    private String userAccount;

    /**
     * 用户密码
     */
    @NotBlank(message = "用户密码不能为空")
    private String password;

}
