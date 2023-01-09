package net.qixiaowei.system.manage.service.impl.system;

import java.util.*;
import java.util.stream.Collectors;

import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.enums.PrefixCodeRule;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.domain.system.RoleMenu;
import net.qixiaowei.system.manage.api.domain.system.UserRole;
import net.qixiaowei.system.manage.api.domain.user.User;
import net.qixiaowei.system.manage.api.dto.system.RoleAuthUsersDTO;
import net.qixiaowei.system.manage.api.dto.system.RoleMenuDTO;
import net.qixiaowei.system.manage.api.dto.system.UserRoleDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.mapper.system.MenuMapper;
import net.qixiaowei.system.manage.mapper.system.RoleMenuMapper;
import net.qixiaowei.system.manage.mapper.system.UserRoleMapper;
import net.qixiaowei.system.manage.mapper.user.UserMapper;
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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

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
     * 获取角色编码
     *
     * @return 角色信息
     */
    @Override
    public String getRoleCode() {
        String roleCode;
        int number = 1;
        List<String> roleCodes = roleMapper.getRoleCodes();
        String prefixCodeRule = PrefixCodeRule.ROLE.getCode();
        for (String code : roleCodes) {
            if (StringUtils.isEmpty(code) || code.length() != 5 || !code.startsWith(prefixCodeRule)) {
                continue;
            }
            code = code.replaceFirst(prefixCodeRule, "");
            try {
                int codeOfNumber = Integer.parseInt(code);
                if (number != codeOfNumber) {
                    break;
                }
                number++;
            } catch (Exception ignored) {
            }
        }
        if (number > 1000) {
            throw new ServiceException("流水号溢出，请联系管理员");
        }
        roleCode = "000" + number;
        roleCode = prefixCodeRule + roleCode.substring(roleCode.length() - 3);
        return roleCode;
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
        role.setSort(1);
        role.setStatus(1);
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
    @Transactional(rollbackFor = Exception.class)
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
            Map<Long, Long> roleMenuMap = new HashMap<>();
            roleMenuDTOS.forEach(roleMenuDTO -> roleMenuMap.put(roleMenuDTO.getMenuId(), roleMenuDTO.getRoleMenuId()));
            this.updateRoleMenu(roleId, menuIds, roleMenuMap);
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setUpdateTime(DateUtils.getNowDate());
        role.setUpdateBy(SecurityUtils.getUserId());
        return roleMapper.updateRole(role);
    }

    /**
     * 角色授权用户
     */
    @Override
    public void authUsers(RoleAuthUsersDTO roleAuthUsersDTO) {
        //todo 数据权限校验
        Set<Long> userIds = roleAuthUsersDTO.getUserIds();
        Long roleId = roleAuthUsersDTO.getRoleId();
        List<UserRoleDTO> userRoleDTOS = userRoleMapper.selectUserRoleListByUserIds(new ArrayList<>(userIds));
        Set<String> userRoleSet = new HashSet<>();
        if (StringUtils.isNotEmpty(userRoleDTOS)) {
            userRoleDTOS.forEach(userRoleDTO -> userRoleSet.add(userRoleDTO.getUserId() + Constants.COLON_EN + userRoleDTO.getRoleId()));
        }
        List<UserRole> userRoles = new ArrayList<>();
        for (Long userId : userIds) {
            //仅添加不存在的用户角色
            if (!userRoleSet.contains(userId + Constants.COLON_EN + roleId)) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(userId);
                userRole.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                userRoles.add(userRole);
            }
        }
        //批量新增用户角色
        if (StringUtils.isNotEmpty(userRoles)) {
            userRoleMapper.batchUserRole(userRoles);
        }
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param userDTO 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<UserDTO> selectAllocatedList(UserDTO userDTO) {
        Long roleId = userDTO.getRoleId();
        if (StringUtils.isNull(roleId)) {
            throw new ServiceException("角色ID不能为空");
        }
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        String employeeName = userDTO.getEmployeeName();
        Map<String, Object> params = user.getParams();
        params.put("roleId", roleId);
        if (StringUtils.isNotEmpty(employeeName)) {
            params.put("employeeName", employeeName);
        }
        user.setParams(params);
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 逻辑批量删除角色表
     *
     * @param roleIds 需要删除的角色表主键
     * @return 结果
     */
    @Override
    public int logicDeleteRoleByRoleIds(List<Long> roleIds) {
        for (Long roleId : roleIds) {
            this.checkRoleAllowed(new RoleDTO(roleId));
        }
        List<RoleDTO> roleDTOS = roleMapper.selectRolesByRoleIds(roleIds);
        if (StringUtils.isEmpty(roleDTOS)) {
            throw new ServiceException("删除失败，角色不存在");
        }
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        if (roleIds.size() != roleDTOS.size()) {
            roleIds = roleDTOS.stream().map(RoleDTO::getRoleId).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(roleIds)) {
            List<Long> delList = roleMenuMapper.selectRoleMenuIdsByRoleIds(roleIds);
            if (StringUtils.isNotEmpty(delList)) {
                roleMenuMapper.logicDeleteRoleMenuByRoleMenuIds(delList, userId, nowDate);
            }
        }
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
        Long roleId = roleDTO.getRoleId();
        Long userId = SecurityUtils.getUserId();
        this.checkRoleAllowed(roleDTO);
        RoleDTO roleByRoleId = roleMapper.selectRoleByRoleId(roleId);
        if (StringUtils.isNull(roleByRoleId)) {
            throw new ServiceException("删除失败，当前角色不存在");
        }
        Date nowDate = DateUtils.getNowDate();
        List<RoleMenuDTO> roleMenuDTOS = roleMenuMapper.selectRoleMenuListByRoleId(roleId);
        if (StringUtils.isNotEmpty(roleMenuDTOS)) {
            //逻辑删除角色菜单关系
            List<Long> delRoleMenuIds = roleMenuDTOS.stream().map(RoleMenuDTO::getRoleMenuId).collect(Collectors.toList());
            roleMenuMapper.logicDeleteRoleMenuByRoleMenuIds(delRoleMenuIds, userId, nowDate);
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        return roleMapper.logicDeleteRoleByRoleId(role, userId, nowDate);
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
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        // 新增用户与角色管理
        List<RoleMenu> list = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenu rm = new RoleMenu();
            rm.setRoleId(roleId);
            rm.setMenuId(menuId);
            rm.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            rm.setCreateBy(userId);
            rm.setCreateTime(nowDate);
            rm.setUpdateBy(userId);
            rm.setUpdateTime(nowDate);
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
     * @param roleId      角色ID
     * @param menuIds     菜单集合
     * @param roleMenuMap 角色旧菜单集合
     */
    public void updateRoleMenu(Long roleId, Set<Long> menuIds, Map<Long, Long> roleMenuMap) {
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        Set<Long> insertMenuIds = new HashSet<>();
        if (StringUtils.isNotEmpty(menuIds)) {
            for (Long menuId : menuIds) {
                if (roleMenuMap.containsKey(menuId)) {
                    roleMenuMap.remove(menuId);
                } else {
                    insertMenuIds.add(menuId);
                }
            }
        }
        //新增
        if (StringUtils.isNotEmpty(insertMenuIds)) {
            this.insertRoleMenu(roleId, insertMenuIds);
        }
        if (StringUtils.isNotEmpty(roleMenuMap)) {
            Set<Long> removeRoleMenuIds = new HashSet<>(roleMenuMap.values());
            if (StringUtils.isNotEmpty(removeRoleMenuIds)) {
                //执行假删除
                roleMenuMapper.logicDeleteRoleMenuByRoleMenuIds(new ArrayList<>(removeRoleMenuIds), userId, nowDate);
            }
        }
    }

}

