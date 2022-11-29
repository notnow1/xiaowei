package net.qixiaowei.system.manage.api.domain.system;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
 * 用户角色表
 *
 * @author hzk
 * @since 2022-10-07
 */
@Data
@Accessors(chain = true)
public class UserRole extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long userRoleId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 角色ID
     */
    private Long roleId;

}

