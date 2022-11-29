package net.qixiaowei.system.manage.api.domain.system;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
 * 角色菜单表
 *
 * @author hzk
 * @since 2022-10-07
 */
@Data
@Accessors(chain = true)
public class RoleMenu extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long roleMenuId;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 菜单ID
     */
    private Long menuId;

}

