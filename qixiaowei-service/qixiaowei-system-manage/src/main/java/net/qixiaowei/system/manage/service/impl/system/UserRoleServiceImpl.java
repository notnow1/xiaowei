package net.qixiaowei.system.manage.service.impl.system;

import java.util.HashSet;
import java.util.List;

import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.dto.system.RoleDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.service.system.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

import net.qixiaowei.system.manage.service.system.IUserRoleService;


/**
 * UserRoleService业务层处理
 *
 * @author hzk
 * @since 2022-10-07
 */
@Service
public class UserRoleServiceImpl implements IUserRoleService {

    @Autowired
    private IRoleService roleService;

    /**
     * 获取角色数据权限
     *
     * @param userDTO 用户
     * @return 角色编码集合
     */
    @Override
    public Set<String> getRoleCodes(UserDTO userDTO) {
        Set<String> roles = new HashSet<String>();
        Long userId = userDTO.getUserId();
        List<RoleDTO> roleList = roleService.selectRolesByUserId(userId);
        userDTO.setRoles(roleList);
        Set<String> roleCodesSet = new HashSet<>();
        roleList.forEach(roleDTO -> roleCodesSet.add(roleDTO.getRoleCode()));
        if (StringUtils.isNotEmpty(roleCodesSet)) {
            roles.addAll(roleCodesSet);
        }
        return roles;
    }
}

