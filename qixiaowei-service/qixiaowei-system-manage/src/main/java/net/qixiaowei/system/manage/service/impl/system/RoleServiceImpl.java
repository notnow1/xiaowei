package net.qixiaowei.system.manage.service.impl.system;

import java.util.*;

import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.system.Role;
import net.qixiaowei.system.manage.api.dto.system.RoleDTO;
import net.qixiaowei.system.manage.mapper.system.RoleMapper;
import net.qixiaowei.system.manage.service.system.IRoleService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * RoleService业务层处理
 *
 * @author hzk
 * @since 2022-10-07
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<RoleDTO> selectRolesByUserId(Long userId) {
        return roleMapper.selectRolesByUserId(userId);
    }


    /**
     * 查询角色表
     *
     * @param roleId 角色表主键
     * @return 角色表
     */
    @Override
    public RoleDTO selectRoleByRoleId(Long roleId) {
        return roleMapper.selectRoleByRoleId(roleId);
    }

    /**
     * 查询角色表列表
     *
     * @param roleDTO 角色表
     * @return 角色表
     */
    @Override
    public List<RoleDTO> selectRoleList(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        return roleMapper.selectRoleList(role);
    }

    /**
     * 新增角色表
     *
     * @param roleDTO 角色表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertRole(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setCreateBy(SecurityUtils.getUserId());
        role.setCreateTime(DateUtils.getNowDate());
        role.setUpdateTime(DateUtils.getNowDate());
        role.setUpdateBy(SecurityUtils.getUserId());
        role.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return roleMapper.insertRole(role);
    }

    /**
     * 修改角色表
     *
     * @param roleDTO 角色表
     * @return 结果
     */
    @Override
    public int updateRole(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setUpdateTime(DateUtils.getNowDate());
        role.setUpdateBy(SecurityUtils.getUserId());
        return roleMapper.updateRole(role);
    }

    /**
     * 逻辑批量删除角色表
     *
     * @param roleDtos 需要删除的角色表主键
     * @return 结果
     */

    @Override
    public int logicDeleteRoleByRoleIds(List<RoleDTO> roleDtos) {
        List<Long> stringList = new ArrayList();
        for (RoleDTO roleDTO : roleDtos) {
            stringList.add(roleDTO.getRoleId());
        }
        return roleMapper.logicDeleteRoleByRoleIds(stringList, roleDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
    }

    /**
     * 物理删除角色表信息
     *
     * @param roleId 角色表主键
     * @return 结果
     */

    @Override
    public int deleteRoleByRoleId(Long roleId) {
        return roleMapper.deleteRoleByRoleId(roleId);
    }

    /**
     * 逻辑删除角色表信息
     *
     * @param roleDTO 角色表
     * @return 结果
     */
    @Override
    public int logicDeleteRoleByRoleId(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        return roleMapper.logicDeleteRoleByRoleId(role, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除角色表信息
     *
     * @param roleDTO 角色表
     * @return 结果
     */
    @Override
    public int deleteRoleByRoleId(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        return roleMapper.deleteRoleByRoleId(role.getRoleId());
    }

    /**
     * 物理批量删除角色表
     *
     * @param roleDtos 需要删除的角色表主键
     * @return 结果
     */
    @Override
    public int deleteRoleByRoleIds(List<RoleDTO> roleDtos) {
        List<Long> stringList = new ArrayList();
        for (RoleDTO roleDTO : roleDtos) {
            stringList.add(roleDTO.getRoleId());
        }
        return roleMapper.deleteRoleByRoleIds(stringList);
    }

}

