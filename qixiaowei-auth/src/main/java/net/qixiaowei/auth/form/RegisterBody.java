package net.qixiaowei.auth.form;

import lombok.Data;

/**
 * 用户注册对象
 */
@Data
public class RegisterBody extends LoginBody {

    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 租户行业
     */
    private Long industryId;
    /**
     * 验证码
     */
    private String code;
    /**
     * 邮箱
     */
    private String email;

}
