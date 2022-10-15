package net.qixiaowei.system.manage.service.impl.system;

import java.util.*;
import java.util.stream.Collectors;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.domain.system.RoleMenu;
import net.qixiaowei.system.manage.api.dto.system.RoleMenuDTO;
import net.qixiaowei.system.manage.api.dto.system.UserRoleDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.mapper.system.MenuMapper;
import net.qixiaowei.system.manage.mapper.system.RoleMenuMapper;
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

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private MenuMapper menuMapper;

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
        RoleDTO roleDTO = roleMapper.selectRoleByRoleId(roleId);
        List<Long> roleMenuIds = menuMapper.selectMenuListByRoleId(roleId);
        if (StringUtils.isNotEmpty(roleMenuIds)) {
            roleDTO.setMenuIds(new HashSet<>(roleMenuIds));
        }
        return roleDTO;
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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoleDTO insertRole(RoleDTO roleDTO) {
        String roleCode = roleDTO.getRoleCode();
        Set<Long> menuIds = roleDTO.getMenuIds();
        RoleDTO roleByCode = roleMapper.selectRoleByRoleCode(roleCode);
        if (StringUtils.isNotNull(roleByCode)) {
            throw new ServiceException("新增角色" + roleDTO.getRoleName() + "失败，角色编码已存在");
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setCreateBy(SecurityUtils.getUserId());
        role.setCreateTime(DateUtils.getNowDate());
        role.setUpdateTime(DateUtils.getNowDate());
        role.setUpdateBy(SecurityUtils.getUserId());
        role.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        roleMapper.insertRole(role);
        this.insertRoleMenu(role.getRoleId(), menuIds);
        roleDTO.setRoleId(role.getRoleId());
        return roleDTO;
    }

    /**
     * 修改角色表
     *
     * @param roleDTO 角色表
     * @return 结果
     */
    @Override
    public int updateRole(RoleDTO roleDTO) {
        Long roleId = roleDTO.getRoleId();
        Set<Long> menuIds = roleDTO.getMenuIds();
        this.checkRoleAllowed(roleDTO);
        String roleCode = roleDTO.getRoleCode();
        RoleDTO roleByCode = roleMapper.selectRoleByRoleCode(roleCode);
        if (StringUtils.isNotNull(roleByCode) && !roleByCode.getRoleId().equals(roleId)) {
            throw new ServiceException("修改角色" + roleDTO.getRoleName() + "失败，角色编码已存在");
        }
        //查找当前角色菜单
        List<RoleMenuDTO> roleMenuDTOS = roleMenuMapper.selectRoleMenuListByRoleId(roleId);
        if (StringUtils.isEmpty(roleMenuDTOS)) {
            this.insertRoleMenu(roleId, menuIds);
        } else { //更新角色菜单
            List<Long> oldRoleMenus = roleMenuDTOS.stream().map(RoleMenuDTO::getRoleMenuId).collect(Collectors.toList());
            this.updateRoleMenu(roleId, menuIds, oldRoleMenus);
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setUpdateTime(DateUtils.getNowDate());
        role.setUpdateBy(SecurityUtils.getUserId());
        return roleMapper.updateRole(role);
    }

    /**
     * 逻辑批量删除角色表
     *
     * @param roleIds 需要删除的角色表主键
     * @return 结果
     */
    @Override
    public int logicDeleteRoleByRoleIds(List<Long> roleIds) {
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        return roleMapper.logicDeleteRoleByRoleIds(roleIds, userId, nowDate);
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

    /**
     * 新增角色菜单信息
     *
     * @param roleId  角色ID
     * @param menuIds 菜单集合
     */
    public void insertRoleMenu(Long roleId, Set<Long> menuIds) {
        if (StringUtils.isEmpty(menuIds) || StringUtils.isNull(roleId)) {
            return;
        }
        // 新增用户与角色管理
        List<RoleMenu> list = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenu rm = new RoleMenu();
            rm.setRoleId(roleId);
            rm.setMenuId(menuId);
            rm.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            list.add(rm);
        }
        if (list.size() > 0) {
            roleMenuMapper.batchRoleMenu(list);
        }
    }

    /**
     * 校验角色是否允许操作
     *
     * @param roleDTO 角色信息
     */
    public void checkRoleAllowed(RoleDTO roleDTO) {
        if (StringUtils.isNotNull(roleDTO.getRoleId()) && roleDTO.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }

    /**
     * 更新角色菜单信息
     *
     * @param roleId       角色ID
     * @param menuIds      菜单集合
     * @param oldRoleMenus 角色旧菜单集合
     */
    public void updateRoleMenu(Long roleId, Set<Long> menuIds, List<Long> oldRoleMenus) {
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        Set<Long> insertUserRoleIds = new HashSet<>();
        if (StringUtils.isNotEmpty(menuIds)) {
            for (Long menuId : menuIds) {
                if (oldRoleMenus.contains(menuId)) {
                    oldRoleMenus.remove(menuId);
                } else {
                    insertUserRoleIds.add(menuId);
                }
            }
        }
        //新增
        if (StringUtils.isNotEmpty(insertUserRoleIds)) {
            this.insertRoleMenu(roleId, insertUserRoleIds);
        }
        //执行假删除
        if (StringUtils.isNotEmpty(oldRoleMenus)) {
            roleMenuMapper.logicDeleteRoleMenuByRoleMenuIds(oldRoleMenus, userId, nowDate);
        }
    }

}

