package net.qixiaowei.system.manage.service.system;

import java.util.Set;

import net.qixiaowei.system.manage.api.dto.user.UserDTO;


/**
 * RoleMenuService接口
 *
 * @author hzk
 * @since 2022-10-07
 */
public interface IRoleMenuService {
    /**
     * 获取菜单数据权限
     *
     * @param userDTO 用户
     * @return 菜单权限信息
     */
    Set<String> getMenuPermission(UserDTO userDTO);
}
