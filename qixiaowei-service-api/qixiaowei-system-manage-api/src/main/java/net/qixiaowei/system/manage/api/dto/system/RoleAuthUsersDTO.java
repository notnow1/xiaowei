package net.qixiaowei.system.manage.api.dto.system;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @Author hzk
 * @Date 2022-10-18 10:36
 **/
@Data
public class RoleAuthUsersDTO {

    /**
     * 要授权的用户ID列表
     */
    @NotEmpty(message = "用户ID集合不能为空")
    private Set<Long> userIds;

    /**
     * 要授权的角色ID
     */
    @NotNull(message = "角色ID不能为空")
    private Long roleId;
}
