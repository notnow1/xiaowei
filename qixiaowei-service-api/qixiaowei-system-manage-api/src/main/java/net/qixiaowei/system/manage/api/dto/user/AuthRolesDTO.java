package net.qixiaowei.system.manage.api.dto.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * @Author hzk
 * @Date 2022-10-15 10:36
 **/
@Data
public class AuthRolesDTO {

    /**
     * 要授权的用户ID列表
     */
    @NotEmpty(message = "用户ID集合不能为空")
    private Set<Long> userIds;

    /**
     * 要授权的角色ID列表
     */
    @NotEmpty(message = "角色ID集合不能为空")
    private Set<Long> roleIds;
}
