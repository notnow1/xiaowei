package net.qixiaowei.system.manage.api.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 更新用户密码
 *
 * @author hzk
 * @since 2023-01-13
 */
@Data
@Accessors(chain = true)
public class UserUpdatePasswordDTO {

    /**
     * 旧密码
     */
    @Size(min = 6, max = 120, message = "密码长度最低6位，且不能超过120个字符")
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    /**
     * 新密码
     */
    @Size(min = 6, max = 120, message = "密码长度最低6位，且不能超过120个字符")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

}

