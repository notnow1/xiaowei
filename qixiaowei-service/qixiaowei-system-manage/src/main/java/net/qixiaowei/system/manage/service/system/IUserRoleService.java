package net.qixiaowei.system.manage.service.system;

import java.util.Set;

import net.qixiaowei.system.manage.api.dto.user.UserDTO;


/**
 * UserRoleService接口
 *
 * @author hzk
 * @since 2022-10-07
 */
public interface IUserRoleService {

    /**
     * 获取角色数据权限
     *
     * @param userDTO 用户
     * @return 角色编码集合
     */
    Set<String> getRoleCodes(UserDTO userDTO);
}
