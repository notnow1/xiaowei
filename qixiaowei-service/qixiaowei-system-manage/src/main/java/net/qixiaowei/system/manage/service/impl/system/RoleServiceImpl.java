package net.qixiaowei.system.manage.service.impl.system;

import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.PrefixCodeRule;
import net.qixiaowei.integration.common.enums.system.RoleDataScope;
import net.qixiaowei.integration.common.enums.system.RoleType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.sales.cloud.api.dto.sync.SyncRoleDTO;
import net.qixiaowei.sales.cloud.api.dto.sync.SyncRoleUserDTO;
import net.qixiaowei.sales.cloud.api.remote.sync.RemoteSyncAdminService;
import net.qixiaowei.system.manage.api.domain.system.RoleMenu;
import net.qixiaowei.system.manage.api.domain.system.UserRole;
import net.qixiaowei.system.manage.api.domain.user.User;
import net.qixiaowei.system.manage.api.dto.system.RoleAuthUsersDTO;
import net.qixiaowei.system.manage.api.dto.system.RoleMenuDTO;
import net.qixiaowei.system.manage.api.dto.system.UserRoleDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.logic.tenant.TenantLogic;
import net.qixiaowei.system.manage.mapper.system.RoleMenuMapper;
import net.qixiaowei.system.manage.mapper.system.UserRoleMapper;
import net.qixiaowei.system.manage.mapper.user.UserMapper;
import net.qixiaowei.system.manage.service.system.IMenuService;
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
@Slf4j
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RemoteSyncAdminService remoteSyncAdminService;

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
        Set<Long> roleMenuIds = menuService.selectMenuListByRoleId(roleId);
        roleDTO.setMenuIds(roleMenuIds);
        return roleDTO;
    }

    /**
     * 查询角色表列表
     *
     * @param roleDTO 角色表
     * @return 角色表
     */
    @Override
    @DataScope(userAlias = "u")
    public List<RoleDTO> selectRoleList(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        return roleMapper.selectRoleList(role);
    }

    @Override
    public void handleResult(List<RoleDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(RoleDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }

    /**
     * 生成角色编码
     *
     * @return 角色编码
     */
    @Override
    public String generateRoleCode() {
        String roleCode;
        int number = 1;
        String prefixCodeRule = PrefixCodeRule.ROLE.getCode();
        List<String> roleCodes = roleMapper.getRoleCodes(prefixCodeRule);
        if (StringUtils.isNotEmpty(roleCodes)) {
            for (String code : roleCodes) {
                if (StringUtils.isEmpty(code) || code.length() != 5) {
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
        String roleName = roleDTO.getRoleName();
        Set<Long> menuIds = roleDTO.getMenuIds();
        List<RoleDTO> roleByRoleCodeOrName = roleMapper.selectRoleByRoleCodeOrName(roleCode, roleName);
        checkRoleUnique(roleCode, roleName, roleByRoleCodeOrName);
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setRoleType(RoleType.CUSTOM.getCode());
        role.setCreateBy(SecurityUtils.getUserId());
        role.setCreateTime(DateUtils.getNowDate());
        role.setUpdateTime(DateUtils.getNowDate());
        role.setUpdateBy(SecurityUtils.getUserId());
        role.setSort(2);
        role.setStatus(1);
        role.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        roleMapper.insertRole(role);
        this.insertRoleMenu(role.getRoleId(), menuIds);
        roleDTO.setRoleId(role.getRoleId());
        //销售云菜单同步
        this.syncSalesAddRole(roleDTO);
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
        RoleDTO roleByRoleId = roleMapper.selectRoleByRoleId(roleId);
        if (StringUtils.isNull(roleByRoleId)) {
            throw new ServiceException("角色不存在");
        }
        if (roleByRoleId.isAdmin()) {
            throw new ServiceException("系统管理员角色不可操作。");
        }
        Set<Long> menuIds = roleDTO.getMenuIds();
        String roleCode = roleDTO.getRoleCode();
        String roleName = roleDTO.getRoleName();
        List<RoleDTO> roleByRoleCodeOrName = roleMapper.selectRoleByRoleCodeOrName(roleCode, roleName);
        if (StringUtils.isNotEmpty(roleByRoleCodeOrName)) {
            //过滤掉自己本身
            roleByRoleCodeOrName = roleByRoleCodeOrName.stream().filter(role -> !roleId.equals(role.getRoleId())).collect(Collectors.toList());
            checkRoleUnique(roleCode, roleName, roleByRoleCodeOrName);
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
        //同步编辑销售云角色
        this.syncSalesEditRole(roleDTO);
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
        Date nowDate = DateUtils.getNowDate();
        Long userIdOfLogin = SecurityUtils.getUserId();
        for (Long userId : userIds) {
            //仅添加不存在的用户角色
            if (!userRoleSet.contains(userId + Constants.COLON_EN + roleId)) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(userId);
                userRole.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                userRole.setCreateBy(userIdOfLogin);
                userRole.setCreateTime(nowDate);
                userRole.setUpdateBy(userIdOfLogin);
                userRole.setUpdateTime(nowDate);
                userRoles.add(userRole);
            }
        }
        //批量新增用户角色
        if (StringUtils.isNotEmpty(userRoles)) {
            //销售云同步
            this.handleSalesRoleUser(userRoles);
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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int logicDeleteRoleByRoleIds(List<Long> roleIds) {
        List<RoleDTO> roleDTOS = roleMapper.selectRolesByRoleIds(roleIds);
        if (StringUtils.isEmpty(roleDTOS)) {
            throw new ServiceException("删除失败，角色不存在");
        }
        roleDTOS.forEach(roleDTO -> {
            if (roleDTO.isAdmin()) {
                throw new ServiceException("系统管理员角色不可删除，请重新勾选。");
            }
        });
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
        //销售云角色删除
        for (Long roleId : roleIds) {
            this.syncSalesDeleteRole(roleId);
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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int logicDeleteRoleByRoleId(RoleDTO roleDTO) {
        Long roleId = roleDTO.getRoleId();
        Long userId = SecurityUtils.getUserId();
        RoleDTO roleByRoleId = roleMapper.selectRoleByRoleId(roleId);
        if (StringUtils.isNull(roleByRoleId)) {
            throw new ServiceException("删除失败，当前角色不存在。");
        }
        if (roleByRoleId.isAdmin()) {
            throw new ServiceException("系统管理员角色不可删除。");
        }
        Date nowDate = DateUtils.getNowDate();
        List<RoleMenuDTO> roleMenuDTOS = roleMenuMapper.selectRoleMenuListByRoleId(roleId);
        if (StringUtils.isNotEmpty(roleMenuDTOS)) {
            //逻辑删除角色菜单关系
            List<Long> delRoleMenuIds = roleMenuDTOS.stream().map(RoleMenuDTO::getRoleMenuId).collect(Collectors.toList());
            roleMenuMapper.logicDeleteRoleMenuByRoleMenuIds(delRoleMenuIds, userId, nowDate);
        }
        this.syncSalesDeleteRole(roleId);
        return roleMapper.logicDeleteRoleByRoleId(roleId, userId, nowDate);
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

    /**
     * @description: 同步销售云新增角色
     * @Author: hzk
     * @date: 2023/4/10 18:43
     * @param: [roleDTO]
     * @return: void
     **/
    private void syncSalesAddRole(RoleDTO roleDTO) {
        String salesToken = SecurityUtils.getSalesToken();
        Set<Long> menuIds = roleDTO.getMenuIds();
        if (StringUtils.isNotEmpty(salesToken)) {
            SyncRoleDTO syncRoleDTO = new SyncRoleDTO();
            syncRoleDTO.setRoleId(roleDTO.getRoleId());
            syncRoleDTO.setRoleName(roleDTO.getRoleName());
            syncRoleDTO.setRoleType(2);
            syncRoleDTO.setDataType(RoleDataScope.convertSalesCode(roleDTO.getDataScope()));
            Set<Long> salesMenuIds = this.getSalesMenuIds(menuIds);
            syncRoleDTO.setMenuIds(salesMenuIds);
            R<?> r = remoteSyncAdminService.syncRoleAdd(syncRoleDTO, salesToken);
            if (0 != r.getCode()) {
                log.error("同步销售云角色新增失败:{}", r.getMsg());
                throw new ServiceException("角色新增失败");
            }
        }
    }

    /**
     * @description: 同步销售云编辑角色
     * @Author: hzk
     * @date: 2023/4/10 18:42
     * @param: [roleDTO]
     * @return: void
     **/
    private void syncSalesEditRole(RoleDTO roleDTO) {
        String salesToken = SecurityUtils.getSalesToken();
        Set<Long> menuIds = roleDTO.getMenuIds();
        if (StringUtils.isNotEmpty(salesToken)) {
            SyncRoleDTO syncRoleDTO = new SyncRoleDTO();
            syncRoleDTO.setRoleId(roleDTO.getRoleId());
            syncRoleDTO.setRoleName(roleDTO.getRoleName());
            syncRoleDTO.setRoleType(2);
            Set<Long> salesMenuIds = this.getSalesMenuIds(menuIds);
            syncRoleDTO.setDataType(RoleDataScope.convertSalesCode(roleDTO.getDataScope()));
            syncRoleDTO.setMenuIds(salesMenuIds);
            R<?> r = remoteSyncAdminService.syncRoleEdit(syncRoleDTO, salesToken);
            if (0 != r.getCode()) {
                log.error("同步销售云角色编辑失败:{}", r.getMsg());
                throw new ServiceException("角色编辑失败");
            }
        }
    }

    /**
     * @description: 同步销售云删除角色
     * @Author: hzk
     * @date: 2023/4/11 15:40
     * @param: [roleId]
     * @return: void
     **/
    private void syncSalesDeleteRole(Long roleId) {
        String salesToken = SecurityUtils.getSalesToken();
        if (StringUtils.isNotEmpty(salesToken)) {
            R<?> r = remoteSyncAdminService.syncRoleDelete(roleId, salesToken);
            if (0 != r.getCode()) {
                log.error("同步销售云角色删除失败:{}", r.getMsg());
                throw new ServiceException("角色删除失败");
            }
        }
    }

    /**
     * @description: 同步销售云
     * @Author: hzk
     * @date: 2023/4/11 17:42
     * @param: [userIds, roleIds]
     * @return: void
     **/
    private void syncSalesRoleRelatedUser(Set<Long> userIds, Set<Long> roleIds) {
        if (StringUtils.isEmpty(userIds) || StringUtils.isEmpty(roleIds)) {
            return;
        }
        String salesToken = SecurityUtils.getSalesToken();
        if (StringUtils.isNotEmpty(salesToken)) {
            SyncRoleUserDTO syncRoleUserDTO = new SyncRoleUserDTO();
            syncRoleUserDTO.setRoleIds(roleIds);
            syncRoleUserDTO.setUserIds(userIds);
            R<?> r = remoteSyncAdminService.syncRoleRelatedUser(syncRoleUserDTO, salesToken);
            if (0 != r.getCode()) {
                log.error("同步销售云角色用户失败:{}", r.getMsg());
                throw new ServiceException("角色授权失败");
            }
        }
    }

    /**
     * @description: 处理销售云角色用户
     * @Author: hzk
     * @date: 2023/4/11 17:53
     * @param: [userRoles]
     * @return: void
     **/
    public void handleSalesRoleUser(List<UserRole> userRoles) {
        Set<Long> userIdList = new HashSet<>();
        Set<Long> roleIdList = new HashSet<>();
        for (UserRole userRole : userRoles) {
            userIdList.add(userRole.getUserId());
            roleIdList.add(userRole.getRoleId());
        }
        this.syncSalesRoleRelatedUser(userIdList, roleIdList);
    }

    /**
     * @description: 获取销售云菜单ID集合
     * @Author: hzk
     * @date: 2023/4/10 18:42
     * @param: [menuIds]
     * @return: java.util.Set<java.lang.Long>
     **/
    private Set<Long> getSalesMenuIds(Set<Long> menuIds) {
        Set<Long> salesMenuIds = new HashSet<>();
        if (StringUtils.isNotEmpty(menuIds)) {
            for (Long menuId : menuIds) {
                if (TenantLogic.SALES_MENUS_MAPPING.containsKey(menuId)) {
                    List<Long> list = TenantLogic.SALES_MENUS_MAPPING.get(menuId);
                    salesMenuIds.addAll(list);
                }
            }
        }
        return salesMenuIds;
    }

    /**
     * @description: 校验角色唯一性
     * @Author: hzk
     * @date: 2023/5/9 14:17
     * @param: [roleCode, roleName, roleDTOS]
     * @return: void
     **/
    private static void checkRoleUnique(String roleCode, String roleName, List<RoleDTO> roleDTOS) {
        if (StringUtils.isNotEmpty(roleDTOS)) {
            roleDTOS.forEach(roleDTO -> {
                if (StringUtils.isNotEmpty(roleCode) && roleCode.equals(roleDTO.getRoleCode())) {
                    throw new ServiceException("角色编码已存在");
                }
                if (StringUtils.isNotEmpty(roleName) && roleName.equals(roleDTO.getRoleName())) {
                    throw new ServiceException("角色名称已存在");
                }
            });
        }
    }

}

