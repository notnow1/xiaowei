package net.qixiaowei.system.manage.api.dto.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @Author hzk
 * @Date 2023-03-15 17:36
 **/
@Data
public class UserStatusDTO {
    /**
     * 要更改状态的用户ID列表
     */
    @NotEmpty(message = "用户ID集合不能为空")
    private Set<Long> userIds;

    /**
     * 状态:0失效;1生效
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
}
