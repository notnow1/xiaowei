package net.qixiaowei.auth.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户注册对象
 */
@Data
public class RegisterBody extends LoginBody {

    /**
     * 租户名称
     */
    @NotBlank(message = "公司名称不能为空")
    private String tenantName;
    /**
     * 租户行业
     */
    @NotNull(message = "行业不能为空")
    private Long industryId;
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String name;
    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;
    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    private String email;

}
