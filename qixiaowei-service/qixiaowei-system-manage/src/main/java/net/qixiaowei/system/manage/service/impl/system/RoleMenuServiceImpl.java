package net.qixiaowei.system.manage.service.impl.system;

import java.util.HashSet;
import java.util.List;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.dto.system.RoleDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.service.system.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

import net.qixiaowei.system.manage.service.system.IRoleMenuService;


/**
 * RoleMenuService业务层处理
 *
 * @author hzk
 * @since 2022-10-07
 */
@Service
public class RoleMenuServiceImpl implements IRoleMenuService {
    @Autowired
    private IMenuService menuService;

    /**
     * 获取菜单数据权限
     *
     * @param userDTO 用户
     * @return 菜单权限信息
     */
    @Override
    public Set<String> getMenuPermission(UserDTO userDTO) {
        Set<String> perms = new HashSet<String>();
        List<RoleDTO> roles = userDTO.getRoles();
        List<Long> roleIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(roles)) {
            roles.forEach(roleDTO -> roleIds.add(roleDTO.getRoleId()));
        }
        if (SecurityUtils.isAdmin(userDTO.getUserId())) {
            // 超级管理员拥有所有权限
            perms.add(SecurityConstants.ROLE_PERMISSION_ALL);
        } else {
            if (StringUtils.isNotEmpty(roleIds)) {
                Set<String> rolePerms = menuService.selectMenuPermsByRoleIds(roleIds);
                perms.addAll(rolePerms);
            }
        }
        return perms;
    }


}

