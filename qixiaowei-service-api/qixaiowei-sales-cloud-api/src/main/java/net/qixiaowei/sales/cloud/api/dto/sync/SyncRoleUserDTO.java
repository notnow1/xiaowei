package net.qixiaowei.sales.cloud.api.dto.sync;

import lombok.Data;

import java.util.Set;

/**
 * @author hzk
 * 销售云同步角色用户
 */
@Data
public class SyncRoleUserDTO {
    /**
     * 人员ID列表
     */
    private Set<Long> userIds;

    /**
     * 角色ID列表
     */
    private Set<Long> roleIds;

}
