package net.qixiaowei.system.manage.service.impl.user;

import java.util.*;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.domain.system.UserRole;
import net.qixiaowei.system.manage.api.dto.system.UserRoleDTO;
import net.qixiaowei.system.manage.api.vo.UserVO;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;
import net.qixiaowei.system.manage.api.vo.user.UserInfoVO;
import net.qixiaowei.system.manage.mapper.system.UserRoleMapper;
import net.qixiaowei.system.manage.service.system.IRoleMenuService;
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
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * UserService业务层处理
 *
 * @author hzk
 * @since 2022-10-05
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleMenuService roleMenuService;


    @Override
    public LoginUserVO getUserByUserAccount(String userAccount) {
        UserDTO userDTO = userMapper.selectUserByUserAccount(userAccount);
        if (StringUtils.isNull(userDTO)) {
            throw new ServiceException("用户名或密码错误");
        }
        //角色集合
        Set<String> roles = userRoleService.getRoleCodes(userDTO);
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
        //角色集合
        Set<String> roles = userRoleService.getRoleCodes(userDTO);
        //权限集合
        Set<String> permissions = roleMenuService.getMenuPermission(userDTO);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDTO, userVO);
        userInfoVO.setUser(userVO);
        userInfoVO.setRoles(roles);
        userInfoVO.setPermissions(permissions);
        return userInfoVO;
    }

    /**
     * 查询用户表
     *
     * @param userId 用户表主键
     * @return 用户表
     */
    @Override
    public UserDTO selectUserByUserId(Long userId) {
        return userMapper.selectUserByUserId(userId);
    }

    /**
     * 查询用户表列表
     *
     * @param userDTO 用户表
     * @return 用户表
     */
    @Override
    public List<UserDTO> selectUserList(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return userMapper.selectUserList(user);
    }

    /**
     * 新增用户表
     *
     * @param userDTO 用户表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertUser(UserDTO userDTO) {
        this.checkUserUnique(userDTO);
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setCreateBy(SecurityUtils.getUserId());
        user.setCreateTime(DateUtils.getNowDate());
        user.setUpdateTime(DateUtils.getNowDate());
        user.setUpdateBy(SecurityUtils.getUserId());
        user.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        Integer status = user.getStatus();
        if (StringUtils.isNull(status)) {
            user.setStatus(1);
        }
        //新增用户
        int row = userMapper.insertUser(user);
        userDTO.setUserId(user.getUserId());
        //新增用户角色
        insertUserRole(userDTO);
        return row;
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
        this.checkUserAllowed(userDTO);
        Long userId = userDTO.getUserId();
        //数据权限 todo
        //查找当前用户角色
        List<UserRoleDTO> userRoleDTOS = userRoleMapper.selectUserRoleListByUserId(userId);
        if (StringUtils.isEmpty(userRoleDTOS)) {
            this.insertUserRole(userDTO);
        } else { //更新用户角色
            List<Long> oldUserRoles = userRoleDTOS.stream().map(UserRoleDTO::getUserRoleId).collect(Collectors.toList());
            this.updateUserRole(userDTO, oldUserRoles);
        }
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setUpdateTime(DateUtils.getNowDate());
        user.setUpdateBy(SecurityUtils.getUserId());
        return userMapper.updateUser(user);
    }

    /**
     * 逻辑批量删除用户表
     *
     * @param userIds 需要删除的用户ID集合
     * @return 结果
     */
    @Override
    public int logicDeleteUserByUserIds(List<Long> userIds) {
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        return userMapper.logicDeleteUserByUserIds(userIds, userId, nowDate);
    }

    /**
     * 逻辑删除用户表信息
     *
     * @param userDTO 用户表
     * @return 结果
     */
    @Override
    public int logicDeleteUserByUserId(UserDTO userDTO) {
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setUpdateBy(userId);
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
        Date nowDate = DateUtils.getNowDate();
        Long operateUserId = SecurityUtils.getUserId();
        this.checkUserAllowed(userDTO);
        User user = new User();
        user.setUserId(userId);
        user.setPassword(SecurityUtils.encryptPassword(userDTO.getPassword()));
        user.setUpdateTime(nowDate);
        user.setUpdateBy(operateUserId);
        return userMapper.updateUser(user);
    }

    /**
     * 批量新增用户表信息
     *
     * @param userDtos 用户表对象
     */
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
     * 校验用户是否唯一
     *
     * @param userDTO 用户
     * @return 结果
     */
    public void checkUserUnique(UserDTO userDTO) {
        List<UserDTO> userDTOS = userMapper.checkUserUnique(userDTO);
        Long userId = userDTO.getUserId();
        if (StringUtils.isNotEmpty(userDTOS)) {
            boolean addFlag = true;
            if (StringUtils.isNotNull(userId)) {
                userDTOS = userDTOS.stream().filter(user -> !userId.equals(user.getUserId())).collect(Collectors.toList());
                addFlag = false;
            }
            if (StringUtils.isNotEmpty(userDTOS)) {
                StringBuilder resultMessage = new StringBuilder();
                resultMessage.append(addFlag ? "新增用户'" : "修改用户'");
                resultMessage.append(userDTO.getUserAccount()).append("'失败，已存在:");
                userDTOS.forEach(user -> {
                    Long employeeId = user.getEmployeeId();
                    String userAccount = user.getUserAccount();
                    String mobilePhone = user.getMobilePhone();
                    String email = user.getEmail();
                    if (StringUtils.isNotNull(employeeId) && employeeId.equals(userDTO.getEmployeeId())) {
                        resultMessage.append("｛员工帐号｝");
                    }
                    if (StringUtils.isNotEmpty(userAccount) && userAccount.equals(userDTO.getUserAccount())) {
                        resultMessage.append("｛帐号｝");
                    }
                    if (StringUtils.isNotEmpty(mobilePhone) && userAccount.equals(userDTO.getMobilePhone())) {
                        resultMessage.append("｛手机号码｝");
                    }
                    if (StringUtils.isNotEmpty(email) && userAccount.equals(userDTO.getEmail())) {
                        resultMessage.append("｛邮箱｝");
                    }
                });
                throw new ServiceException(resultMessage.toString());
            }
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(UserDTO user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            Date nowDate = DateUtils.getNowDate();
            Long insertUserId = SecurityUtils.getUserId();
            // 新增用户与角色管理
            List<UserRole> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UserRole ur = new UserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                ur.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                ur.setCreateBy(insertUserId);
                ur.setUpdateBy(insertUserId);
                ur.setCreateTime(nowDate);
                ur.setUpdateTime(nowDate);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    public void checkUserAllowed(UserDTO user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 更新用户角色信息
     *
     * @param user         用户对象
     * @param oldUserRoles 用户旧角色集合
     */
    public void updateUserRole(UserDTO user, List<Long> oldUserRoles) {
        Long[] roleIds = user.getRoleIds();
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        List<Long> insertUserRoleIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(roleIds)) {
            for (Long userRole : roleIds) {
                if (oldUserRoles.contains(userRole)) {
                    oldUserRoles.remove(userRole);
                } else {
                    insertUserRoleIds.add(userRole);
                }
            }
        }
        //新增
        if (StringUtils.isNotEmpty(insertUserRoleIds)) {
            this.insertUserRole(user.getUserId(), insertUserRoleIds.toArray(new Long[insertUserRoleIds.size()]));
        }
        //执行假删除
        if (StringUtils.isNotEmpty(oldUserRoles)) {
            userRoleMapper.logicDeleteUserRoleByUserRoleIds(oldUserRoles, userId, nowDate);
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
}

