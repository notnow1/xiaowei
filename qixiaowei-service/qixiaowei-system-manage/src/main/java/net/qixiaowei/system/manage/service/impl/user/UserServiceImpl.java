package net.qixiaowei.system.manage.service.impl.user;

import java.io.InputStream;
import java.util.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.file.api.RemoteFileService;
import net.qixiaowei.file.api.dto.FileDTO;
import net.qixiaowei.integration.common.config.FileConfig;
import net.qixiaowei.integration.common.constant.*;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.system.RoleDataScope;
import net.qixiaowei.integration.common.enums.tenant.TenantStatus;
import net.qixiaowei.integration.common.enums.user.UserType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.file.FileTypeUtils;
import net.qixiaowei.integration.common.utils.file.MimeTypeUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.integration.security.service.TokenService;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.integration.tenant.annotation.IgnoreTenant;
import net.qixiaowei.sales.cloud.api.remote.sync.RemoteSyncAdminService;
import net.qixiaowei.system.manage.api.domain.basic.Employee;
import net.qixiaowei.system.manage.api.domain.system.UserRole;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.system.RoleDTO;
import net.qixiaowei.system.manage.api.dto.system.UserRoleDTO;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.api.dto.user.AuthRolesDTO;
import net.qixiaowei.system.manage.api.dto.user.UserStatusDTO;
import net.qixiaowei.system.manage.api.dto.user.UserUpdatePasswordDTO;
import net.qixiaowei.system.manage.api.vo.UserVO;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;
import net.qixiaowei.system.manage.api.vo.user.UserInfoVO;
import net.qixiaowei.system.manage.api.vo.user.UserPageCountVO;
import net.qixiaowei.system.manage.api.vo.user.UserProfileVO;
import net.qixiaowei.system.manage.config.tenant.TenantConfig;
import net.qixiaowei.system.manage.logic.user.UserLogic;
import net.qixiaowei.system.manage.mapper.system.RoleMapper;
import net.qixiaowei.system.manage.mapper.system.UserRoleMapper;
import net.qixiaowei.system.manage.mapper.tenant.TenantMapper;
import net.qixiaowei.system.manage.service.system.IRoleMenuService;
import net.qixiaowei.system.manage.service.system.IRoleService;
import net.qixiaowei.system.manage.service.system.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.user.User;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.mapper.user.UserMapper;
import net.qixiaowei.system.manage.service.user.IUserService;
import org.springframework.web.multipart.MultipartFile;


/**
 * UserService业务层处理
 *
 * @author hzk
 * @since 2022-10-05
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleMenuService roleMenuService;

    @Autowired
    private UserLogic userLogic;

    @Autowired
    private TenantConfig tenantConfig;

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private FileConfig fileConfig;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RemoteFileService remoteFileService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RemoteSyncAdminService remoteSyncAdminService;


    @Override
    @IgnoreTenant
    public LoginUserVO getUserByUserAccount(String userAccount, String domain) {
        Long tenantId = 0L;
        if (StringUtils.isEmpty(domain)) {
            throw new ServiceException("账号或密码有误，请重新输入");
        }
        if (StringUtils.isNotEmpty(domain)) {
            domain = domain.replace("." + tenantConfig.getMainDomain(), "");
            if (StringUtils.isNotEmpty(domain) && !"www".equals(domain)) {
                String adminDomainPrefix = tenantConfig.getAdminDomainPrefix();
                if (!domain.equals(adminDomainPrefix)) {
                    TenantDTO tenantDTO = tenantMapper.selectTenantByDomain(domain);
                    if (StringUtils.isNotNull(tenantDTO)) {
                        tenantId = tenantDTO.getTenantId();
                        Integer tenantStatus = tenantDTO.getTenantStatus();
                        if (!TenantStatus.NORMAL.getCode().equals(tenantStatus)) {
                            throw new ServiceException("您的账号状态为异常状态，请联系客服查询。");
                        }
                    } else {
                        throw new ServiceException("账号或密码有误，请重新输入");
                    }
                }
            } else {
                throw new ServiceException("账号或密码有误，请重新输入");
            }
        }
        UserDTO userDTO = userMapper.selectUserByUserAccountAndTenantId(userAccount, tenantId);
        if (StringUtils.isNull(userDTO)) {
            throw new ServiceException("账号或密码有误，请重新输入");
        }
        userDTO.setAvatar(this.convertToFullFilePath(userDTO.getAvatar()));
        userDTO.setTenantLogo(this.convertToFullFilePath(userDTO.getTenantLogo()));
        //角色集合
        Set<String> roles = userRoleService.getRoleCodes(userDTO);
        List<RoleDTO> roleList = userDTO.getRoles();
        Long departmentId = userDTO.getDepartmentId();
        Long employeeId = userDTO.getEmployeeId();
        //设置查看用户ID集合
        Set<Integer> dataScope = new HashSet<>();
        Set<Long> userIds = new HashSet<>();
        userIds.add(userDTO.getUserId());
        Set<Long> userIdSet = null;
        roleList.forEach(roleDTO -> dataScope.add(roleDTO.getDataScope()));
        if (!dataScope.contains(RoleDataScope.ALL.getCode())) {
            if (dataScope.contains(RoleDataScope.ALL_SUB_DEPARTMENT.getCode())) {
                userIdSet = userMapper.selectUserIdsByDepartmentId(departmentId, employeeId, RoleDataScope.ALL_SUB_DEPARTMENT.getCode());
            } else if (dataScope.contains(RoleDataScope.DEPARTMENT.getCode())) {
                userIdSet = userMapper.selectUserIdsByDepartmentId(departmentId, employeeId, RoleDataScope.DEPARTMENT.getCode());
            } else if (dataScope.contains(RoleDataScope.SELF_AND_SUBORDINATE.getCode())) {
                userIdSet = userMapper.selectUserIdsByDepartmentId(departmentId, employeeId, RoleDataScope.SELF_AND_SUBORDINATE.getCode());
            }
            if (StringUtils.isNotEmpty(userIdSet)) {
                userIds.addAll(userIdSet);
            }
        }
        userDTO.setUserIds(userIds);
        //销售云访问控制
        Boolean enableSalesAccess = tenantConfig.getEnableSalesAccess();
        userDTO.setSalesCloudFlag(enableSalesAccess);
        //权限集合
        Set<String> permissions = roleMenuService.getMenuPermission(userDTO);
        LoginUserVO sysUserVo = new LoginUserVO();
        sysUserVo.setUserDTO(userDTO);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return sysUserVo;
    }


    @Override
    public UserInfoVO getInfo() {
        UserInfoVO userInfoVO = new UserInfoVO();
        Long userId = SecurityUtils.getUserId();
        UserDTO userDTO = userMapper.selectUserByUserId(userId);
        if (StringUtils.isNull(userDTO)) {
            throw new ServiceException("用户不存在");
        }
        userDTO.setAvatar(this.convertToFullFilePath(userDTO.getAvatar()));
        userDTO.setTenantLogo(this.convertToFullFilePath(userDTO.getTenantLogo()));
        //角色集合
        Set<String> roles = userRoleService.getRoleCodes(userDTO);
        //权限集合
        Set<String> permissions = roleMenuService.getMenuPermission(userDTO);
        String tenantLogo = userDTO.getTenantLogo();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDTO, userVO);
        userVO.setTenantLogo(fileConfig.getFullDomain(tenantLogo));
        userInfoVO.setUser(userVO);
        userInfoVO.setRoles(roles);
        userInfoVO.setPermissions(permissions);
        return userInfoVO;
    }

    /**
     * 查询用户资料
     *
     * @return 用户个人信息对象
     */
    @Override
    public UserProfileVO getProfile() {
        Long userId = SecurityUtils.getUserId();
        UserDTO userDTO = userMapper.selectUserByUserId(userId);
        if (StringUtils.isNull(userDTO)) {
            throw new ServiceException("当前用户不存在");
        }
        UserProfileVO userProfileVO = new UserProfileVO();
        BeanUtils.copyProperties(userDTO, userProfileVO);
        userProfileVO.setTenantLogo(fileConfig.getFullDomain(userProfileVO.getTenantLogo()));
        userProfileVO.setAvatar(fileConfig.getFullDomain(userProfileVO.getAvatar()));
        return userProfileVO;
    }

    /**
     * 修改用户资料
     *
     * @param avatarFile 头像文件
     * @return 结果
     */
    @Override
    public UserProfileVO editAvatar(MultipartFile avatarFile) {
        Long userId = SecurityUtils.getUserId();
        UserDTO userOfDB = userMapper.selectUserByUserId(userId);
        if (StringUtils.isNull(userOfDB)) {
            throw new ServiceException("当前用户不存在");
        }
        Date nowDate = DateUtils.getNowDate();
        User user = new User();
        user.setUserId(userId);
        String avatar = null;
        if (StringUtils.isNotNull(avatarFile) && !avatarFile.isEmpty()) {
            String extension = FileTypeUtils.getExtension(avatarFile);
            if (!StringUtils.equalsAnyIgnoreCase(extension, MimeTypeUtils.IMAGE_EXTENSION)) {
                throw new ServiceException("文件格式不正确，请上传" + Arrays.toString(MimeTypeUtils.IMAGE_EXTENSION) + "格式");
            }
            R<FileDTO> fileResult = remoteFileService.upLoad(avatarFile, "user/avatar", SecurityConstants.INNER);
            if (StringUtils.isNull(fileResult) || StringUtils.isNull(fileResult.getData())) {
                throw new ServiceException("文件服务异常，请联系管理员");
            }
            avatar = fileResult.getData().getFilePath();
        }
        if (StringUtils.isNotEmpty(avatar)) {
            user.setAvatar(avatar);
        }
        user.setUpdateBy(userId);
        user.setUpdateTime(nowDate);
        int i = userMapper.updateUser(user);
        //同步销售云头像
        userLogic.syncSaleUserAvatar(avatarFile);
        // 更新缓存用户信息
        if (i > 0) {
            LoginUserVO loginUser = SecurityUtils.getLoginUser();
            UserProfileVO userProfileVO = new UserProfileVO();
            BeanUtils.copyProperties(userOfDB, userProfileVO);
            String fullDomain = fileConfig.getFullDomain(userProfileVO.getAvatar());
            if (StringUtils.isNotEmpty(user.getAvatar())) {
                loginUser.getUserDTO().setAvatar(fullDomain);
                userProfileVO.setAvatar(fullDomain);
            }
            tokenService.setLoginUser(loginUser);
            return userProfileVO;
        } else {
            throw new ServiceException("修改失败，请联系管理员。");
        }
    }

    /**
     * 修改用户资料
     *
     * @param userDTO 用户修改对象
     * @return 结果
     */
    @Override
    public int editProfile(UserDTO userDTO) {
        Long userId = SecurityUtils.getUserId();
        UserDTO userOfDB = userMapper.selectUserByUserId(userId);
        if (StringUtils.isNull(userOfDB)) {
            throw new ServiceException("当前用户不存在");
        }
        userDTO.setUserId(userId);
        userLogic.checkUserUnique(userDTO);
        Date nowDate = DateUtils.getNowDate();
        String email = userDTO.getEmail();
        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setUpdateBy(userId);
        user.setUpdateTime(nowDate);
        int i = userMapper.updateUser(user);
        // 更新缓存用户信息
        if (i > 0) {
            //同步销售云
            userLogic.syncSaleEditUser(userDTO);
            LoginUserVO loginUser = SecurityUtils.getLoginUser();
            loginUser.getUserDTO().setEmail(email);
            tokenService.setLoginUser(loginUser);
            return i;
        } else {
            throw new ServiceException("修改失败，请联系管理员。");
        }
    }

    /**
     * 查询用户表
     *
     * @param userId 用户表主键
     * @return 用户表
     */
    @Override
    public UserDTO selectUserByUserId(Long userId) {
        UserDTO userDTO = userMapper.selectUserByUserId(userId);
        if (StringUtils.isNull(userDTO)) {
            throw new ServiceException("当前用户不存在");
        }
        List<RoleDTO> roleList = roleMapper.selectRolesByUserId(userId);
        userDTO.setRoles(roleList);
        if (StringUtils.isNotEmpty(roleList)) {
            Set<Long> roleIds = new HashSet<>();
            List<String> roleNames = new ArrayList<>();
            for (RoleDTO roleDTO : roleList) {
                Long roleId = roleDTO.getRoleId();
                String roleName = roleDTO.getRoleName();
                roleIds.add(roleId);
                roleNames.add(roleName);
            }
            userDTO.setRoleIds(roleIds);
            userDTO.setRoleNames(CollUtil.join(roleNames, StrUtil.COMMA));
        }
        return userDTO;
    }

    /**
     * 初始化用户缓存
     *
     * @param userId 用户表主键
     * @return 用户表
     */
    @Override
    public UserProfileVO initUserCache(Long userId) {
        UserDTO userDTO = userMapper.selectUserByUserId(userId);
        UserProfileVO userProfileVO = null;
        if (StringUtils.isNotNull(userDTO)) {
            String key = CacheConstants.USER_KEY + userId;
            userProfileVO = new UserProfileVO();
            BeanUtils.copyProperties(userDTO, userProfileVO);
            redisService.setCacheObject(key, userProfileVO);
        }
        return userProfileVO;
    }

    /**
     * 根据用户ID查询用户角色列表
     *
     * @param userId 用户表主键
     * @return 用户表
     */
    @Override
    public List<RoleDTO> selectUserRolesByUserId(Long userId) {
        List<RoleDTO> roleDTOS = roleMapper.selectRolesByUserId(userId);
        return SecurityUtils.isAdmin(userId) ? roleDTOS.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()) : roleDTOS;
    }

    /**
     * 查询用户表列表
     *
     * @param userDTO 用户表
     * @return 用户表
     */
    @Override
    @DataScope(userAlias = "u")
    public List<UserDTO> selectUserList(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        String employeeName = userDTO.getEmployeeName();
        Map<String, Object> params = user.getParams();
        if (StringUtils.isNotEmpty(employeeName)) {
            params.put("employeeName", employeeName);
        }
        user.setParams(params);
        return userMapper.selectUserList(user);
    }

    /**
     * 处理返回
     *
     * @param result 用户表
     * @return 用户表集合
     */
    @Override
    public void handleResult(List<UserDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(UserDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }

    /**
     * 查询用户表列表
     *
     * @param userIds 用户ids
     * @return 用户表集合
     */
    @Override
    public List<UserDTO> getUsersByUserIds(Set<Long> userIds) {
        return userMapper.selectUserListByUserIds(userIds);
    }

    /**
     * 新增用户表
     *
     * @param userDTO 用户表
     * @return 结果
     */
    @Transactional
    @Override
    public UserDTO insertUser(UserDTO userDTO) {
        //新增人员
        Employee employee = userLogic.insertEmployee(userDTO);
        //新增帐号
        UserDTO userResult = userLogic.insertUser(userDTO);
        userLogic.syncSalesAddUser(userDTO, userResult.getUserId(), BusinessConstants.NORMAL, employee);
        return userResult;
    }

    /**
     * 修改用户表
     *
     * @param userDTO 用户表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateUser(UserDTO userDTO) {
        Long userId = userDTO.getUserId();
        UserDTO userByUserId = userMapper.selectUserByUserId(userId);
        if (StringUtils.isNull(userByUserId)) {
            throw new ServiceException("修改失败，当前用户不存在");
        }
        userDTO.setEmployeeId(userByUserId.getEmployeeId());
        userLogic.checkUserUnique(userDTO);
        //数据权限 todo
        //查找当前用户角色
        List<UserRoleDTO> userRoleDTOS = userRoleMapper.selectUserRoleListByUserId(userId);
        if (StringUtils.isEmpty(userRoleDTOS)) {
            userLogic.insertUserRole(userDTO);
        } else { //更新用户角色
            Map<Long, Long> userRoleMap = new HashMap<>();
            userRoleDTOS.forEach(userRoleDTO -> userRoleMap.put(userRoleDTO.getRoleId(), userRoleDTO.getUserRoleId()));
            this.updateUserRole(userDTO, userRoleMap);
        }
        userLogic.updateEmployee(userDTO);
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        //头像修改放到用户自己编辑
        user.setAvatar(null);
        user.setUpdateTime(DateUtils.getNowDate());
        user.setUpdateBy(SecurityUtils.getUserId());
        int row = userMapper.updateUser(user);
        if (row > 0) {
            //同步销售云
            userLogic.syncSaleEditUser(userDTO);
            this.initUserCache(userId);
        }
        return row;
    }

    /**
     * 逻辑批量删除用户表
     *
     * @param userIds 需要删除的用户ID集合
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int logicDeleteUserByUserIds(Set<Long> userIds) {
        Long userId = SecurityUtils.getUserId();
        String userAccount = SecurityUtils.getUserAccount();
        for (Long id : userIds) {
            if (SecurityUtils.isAdmin(id)) {
                throw new ServiceException("不允许删除超级管理员用户");
            }
        }
        if (userIds.contains(userId)) {
            throw new ServiceException("不能删除当前用户");
        }
        List<UserDTO> userDTOS = userMapper.selectUserListByUserIds(userIds);
        if (StringUtils.isEmpty(userDTOS)) {
            throw new ServiceException("删除失败，用户不存在");
        }
        userDTOS.forEach(userDTO -> {
            if (userDTO.isAdmin()) {
                throw new ServiceException("系统管理员帐号不可删除，请重新勾选。");
            }
        });
        if (userIds.size() != userDTOS.size()) {
            userIds = userDTOS.stream().map(UserDTO::getUserId).collect(Collectors.toSet());
        }
        Date nowDate = DateUtils.getNowDate();
        if (StringUtils.isNotEmpty(userIds)) {
            List<Long> delList = userRoleMapper.selectUserRoleIdsByUserIds(userIds);
            if (StringUtils.isNotEmpty(delList)) {
                //逻辑删除用户角色关系
                userRoleMapper.logicDeleteUserRoleByUserIds(delList, userId, nowDate);
            }
        }
        return userMapper.logicDeleteUserByUserIds(userIds, userId, nowDate);
    }

    /**
     * 逻辑删除用户表信息
     *
     * @param userDTO 用户表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int logicDeleteUserByUserId(UserDTO userDTO) {
        Long userId = userDTO.getUserId();
        Long operateUserId = SecurityUtils.getUserId();
        if (SecurityUtils.isAdmin(userId)) {
            throw new ServiceException("不允许删除超级管理员用户。");
        }
        if (operateUserId.equals(userId)) {
            throw new ServiceException("当前用户不能删除");
        }
        UserDTO userByUserId = userMapper.selectUserByUserId(userId);
        if (StringUtils.isNull(userByUserId)) {
            throw new ServiceException("删除失败，当前用户不存在");
        }
        if (userByUserId.isAdmin()) {
            throw new ServiceException("系统管理员帐号不可删除。");
        }
        Date nowDate = DateUtils.getNowDate();
        List<UserRoleDTO> userRoleDTOS = userRoleMapper.selectUserRoleListByUserId(userId);
        if (StringUtils.isNotEmpty(userRoleDTOS)) {
            List<Long> delUserRoleIds = userRoleDTOS.stream().map(UserRoleDTO::getUserRoleId).collect(Collectors.toList());
            //逻辑删除用户角色关系
            userRoleMapper.logicDeleteUserRoleByUserRoleIds(delUserRoleIds, operateUserId, nowDate);
        }
        User user = new User();
        user.setUserId(userId);
        user.setUpdateBy(operateUserId);
        user.setUpdateTime(nowDate);
        return userMapper.logicDeleteUserByUserId(user);
    }

    /**
     * 重置密码
     *
     * @param userDTO
     * @return 结果
     */
    @Override
    public int resetPwd(UserDTO userDTO) {
        Long userId = userDTO.getUserId();
        String password = userDTO.getPassword();
        Date nowDate = DateUtils.getNowDate();
        Long operateUserId = SecurityUtils.getUserId();
        UserDTO userByUserId = userMapper.selectUserByUserId(userId);
        if (StringUtils.isNull(userByUserId)) {
            throw new ServiceException("当前用户不存在");
        }
        User user = new User();
        user.setUserId(userId);
        user.setPassword(SecurityUtils.encryptPassword(password));
        user.setUpdateTime(nowDate);
        user.setUpdateBy(operateUserId);
        //同步销售云
        userLogic.syncSaleResetUserPassword(userId, password);
        return userMapper.updateUser(user);
    }

    /**
     * 重置帐号
     *
     * @param userDTO
     * @return 结果
     */
    @Override
    public int resetUserAccount(UserDTO userDTO) {
        Long userId = userDTO.getUserId();
        String userAccount = userDTO.getUserAccount();
        String password = userDTO.getPassword();
        Date nowDate = DateUtils.getNowDate();
        Long operateUserId = SecurityUtils.getUserId();
        //校验用户
        UserDTO userByUserId = userMapper.selectUserByUserId(userId);
        if (StringUtils.isNull(userByUserId)) {
            throw new ServiceException("当前用户不存在");
        }
        boolean isAdmin = UserType.SYSTEM.getCode().equals(userByUserId.getUserType());
        if (isAdmin && !userId.equals(operateUserId)) {
            throw new ServiceException("不允许非系统管理员重置系统管理员帐号");
        }
        String oldUserAccount = userByUserId.getUserAccount();
        if (userAccount.equals(oldUserAccount)) {
            throw new ServiceException("帐号不能和原帐号相同");
        }
        UserDTO selectUserByUserAccount = userMapper.selectUserByUserAccount(userAccount);
        if (StringUtils.isNotNull(selectUserByUserAccount)) {
            throw new ServiceException("帐号已存在");
        }
        User user = new User();
        user.setUserId(userId);
        user.setUserAccount(userAccount);
        user.setPassword(SecurityUtils.encryptPassword(password));
        user.setUpdateTime(nowDate);
        user.setUpdateBy(operateUserId);
        //同步人员手机号更新
        userDTO.setEmployeeId(userByUserId.getEmployeeId());
        userLogic.updateEmployee(userDTO);
        //同步销售云
        userLogic.syncSaleResetUserAccount(userId, oldUserAccount, userAccount, password, isAdmin);
        return userMapper.updateUser(user);
    }

    /**
     * 编辑用户状态
     *
     * @param userStatusDTO
     * @return 结果
     */
    @Override
    public int editUserStatus(UserStatusDTO userStatusDTO) {
        Set<Long> userIds = userStatusDTO.getUserIds();
        Integer status = userStatusDTO.getStatus();
        if (!Arrays.asList(BusinessConstants.NORMAL, BusinessConstants.DISABLE).contains(status)) {
            throw new ServiceException("状态错误");
        }
        boolean disable = BusinessConstants.DISABLE.equals(status);
        Long userId = SecurityUtils.getUserId();
        if (disable && userIds.contains(userId)) {
            throw new ServiceException("当前用户不可停用");
        }
        List<UserDTO> userDTOS = userMapper.selectUserListByUserIds(userIds);
        if (StringUtils.isEmpty(userDTOS)) {
            throw new ServiceException("用户不存在");
        }
        if (disable) {
            userDTOS.forEach(userDTO -> {
                if (userDTO.isAdmin()) {
                    throw new ServiceException("系统管理员不可停用");
                }
            });
        }
        if (userIds.size() != userDTOS.size()) {
            userIds = userDTOS.stream().map(UserDTO::getUserId).collect(Collectors.toSet());
        }
        Date nowDate = DateUtils.getNowDate();
        //同步销售云
        userLogic.syncSaleSetUserStatus(userIds, status);
        return userMapper.updateUserStatusByUserIds(userIds, status, userId, nowDate);
    }

    /**
     * 校验用户是否存在
     *
     * @param userAccount
     * @return 结果
     */
    @Override
    @IgnoreTenant
    public Boolean checkUserAccountExists(String userAccount) {
        return !this.checkUserAccountUnique(userAccount);
    }

    /**
     * 重置用户密码
     *
     * @param userUpdatePasswordDTO 用户更新密码实体
     * @return 结果
     */
    @Override
    public int resetUserPwd(UserUpdatePasswordDTO userUpdatePasswordDTO) {
        String oldPassword = userUpdatePasswordDTO.getOldPassword();
        String newPassword = userUpdatePasswordDTO.getNewPassword();
        Long userId = SecurityUtils.getUserId();
        UserDTO userDTO = userMapper.selectUserByUserId(userId);
        if (StringUtils.isNull(userDTO)) {
            throw new ServiceException("修改密码失败，找不到该用户。");
        }
        String password = userDTO.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            throw new ServiceException("原密码错误，请重新输入");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            throw new ServiceException("新密码与原密码一致，请重新输入");
        }
        newPassword = SecurityUtils.encryptPassword(newPassword);
        User user = new User();
        user.setUserId(userId);
        user.setPassword(newPassword);
        user.setUpdateBy(userId);
        user.setUpdateTime(DateUtils.getNowDate());
        //同步销售云
        userLogic.syncSaleResetUserPassword(userId, newPassword);
        int i = userMapper.updateUser(user);
        //更新成功则更新缓存用户密码
        if (i > 0) {
            LoginUserVO loginUser = SecurityUtils.getLoginUser();
            loginUser.getUserDTO().setPassword(newPassword);
            tokenService.setLoginUser(loginUser);
        }
        return i;
    }

    /**
     * 用户授权角色
     */
    @Override
    public void authRoles(AuthRolesDTO authRolesDTO) {
        Set<Long> userIds = authRolesDTO.getUserIds();
        Set<Long> roleIds = authRolesDTO.getRoleIds();
        List<UserRoleDTO> userRoleDTOS = userRoleMapper.selectUserRoleListByUserIds(new ArrayList<>(userIds));
        Set<String> userRoleSet = new HashSet<>();
        if (StringUtils.isNotEmpty(userRoleDTOS)) {
            userRoleDTOS.forEach(userRoleDTO -> userRoleSet.add(userRoleDTO.getUserId() + Constants.COLON_EN + userRoleDTO.getRoleId()));
        }
        List<UserRole> userRoles = new ArrayList<>();
        Date nowDate = DateUtils.getNowDate();
        Long userIdOfLogin = SecurityUtils.getUserId();
        for (Long userId : userIds) {
            roleIds.forEach(roleId -> {
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
            });
        }
        //批量新增用户角色
        if (StringUtils.isNotEmpty(userRoles)) {
            roleService.handleSalesRoleUser(userRoles);
            userRoleMapper.batchUserRole(userRoles);
        }
    }

    /**
     * 查询未分配用户员工列表
     */
    @Override
    public List<EmployeeDTO> unallocatedEmployees(Long userId) {
        return userLogic.unallocatedEmployees(userId);
    }

    /**
     * 远程 通过人员ID集合查询用户id
     *
     * @param employeeIds
     * @return
     */
    @Override
    public List<UserDTO> selectByemployeeIds(List<Long> employeeIds) {
        return userMapper.selectUserByEmployeeIds(employeeIds);
    }

    /**
     * 批量新增用户表信息
     *
     * @param userDtos 用户表对象
     */
    @Override
    @Transactional
    public int insertUsers(List<UserDTO> userDtos) {
        List<User> userList = new ArrayList();

        for (UserDTO userDTO : userDtos) {
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            user.setCreateBy(SecurityUtils.getUserId());
            user.setCreateTime(DateUtils.getNowDate());
            user.setUpdateTime(DateUtils.getNowDate());
            user.setUpdateBy(SecurityUtils.getUserId());
            user.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            userList.add(user);
        }
        return userMapper.batchUser(userList);
    }

    /**
     * 批量修改用户表信息
     *
     * @param userDtos 用户表对象
     */
    @Override
    @Transactional
    public int updateUsers(List<UserDTO> userDtos) {
        List<User> userList = new ArrayList();

        for (UserDTO userDTO : userDtos) {
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            user.setCreateBy(SecurityUtils.getUserId());
            user.setCreateTime(DateUtils.getNowDate());
            user.setUpdateTime(DateUtils.getNowDate());
            user.setUpdateBy(SecurityUtils.getUserId());
            userList.add(user);
        }
        return userMapper.updateUsers(userList);
    }

    /**
     * 更新用户角色信息
     *
     * @param user        用户对象
     * @param userRoleMap 用户旧角色集合
     */
    public void updateUserRole(UserDTO user, Map<Long, Long> userRoleMap) {
        Set<Long> roleIds = user.getRoleIds();
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        Set<Long> insertRoleIds = new HashSet<>();
        if (StringUtils.isNotEmpty(roleIds)) {
            for (Long roleId : roleIds) {
                if (userRoleMap.containsKey(roleId)) {
                    userRoleMap.remove(roleId);
                } else {
                    insertRoleIds.add(roleId);
                }
            }
        }
        //新增
        if (StringUtils.isNotEmpty(insertRoleIds)) {
            userLogic.insertUserRole(user.getUserId(), insertRoleIds);
        }
        if (StringUtils.isNotEmpty(userRoleMap)) {
            Set<Long> removeUserRoleIds = new HashSet<>(userRoleMap.values());
            if (StringUtils.isNotEmpty(removeUserRoleIds)) {
                //执行假删除
                userRoleMapper.logicDeleteUserRoleByUserRoleIds(new ArrayList<>(removeUserRoleIds), userId, nowDate);
            }
        }
    }

    /**
     * 校验用户帐号是否唯一
     *
     * @param userAccount 用户名称
     * @return 结果
     */

    public Boolean checkUserAccountUnique(String userAccount) {
        int count = userMapper.checkUserAccountUnique(userAccount);
        return count <= 0;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param userDTO 用户信息
     * @return
     */
    public Boolean checkMobilePhoneUnique(UserDTO userDTO) {
        Long userId = StringUtils.isNull(userDTO.getUserId()) ? -1L : userDTO.getUserId();
        UserDTO info = userMapper.checkMobilePhoneUnique(userDTO.getMobilePhone());
        return StringUtils.isNull(info) || info.getUserId().equals(userId);
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    public Boolean checkEmailUnique(UserDTO user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        UserDTO info = userMapper.checkEmailUnique(user.getEmail());
        return StringUtils.isNull(info) || info.getUserId().equals(userId);
    }

    /**
     * @description: 将文件路径处理为全路径
     * @Author: hzk
     * @date: 2023/3/1 13:53
     * @param: [oldFilePath]
     * @return: java.lang.String
     **/
    private String convertToFullFilePath(String oldFilePath) {
        return fileConfig.getFullDomain(oldFilePath);
    }

    /**
     * @description: 获取用户统计数据
     * @Author: hzk
     * @date: 2023/5/24 15:57
     * @param: []
     * @return: net.qixiaowei.system.manage.api.vo.user.UserPageCountVO
     **/
    @Override
    public UserPageCountVO getUserPageCount() {
        List<Integer> userStatusList = userMapper.getUserStatusList();
        Integer total = 0;
        Integer unactivated = 0;
        if (StringUtils.isNotEmpty(userStatusList) && userStatusList.size() == 2) {
            total = userStatusList.get(0);
            unactivated = userStatusList.get(1);
        }
        UserPageCountVO userPageCountVO = new UserPageCountVO();
        userPageCountVO.setTotal(total);
        userPageCountVO.setUnactivated(unactivated);
        return userPageCountVO;
    }
}

