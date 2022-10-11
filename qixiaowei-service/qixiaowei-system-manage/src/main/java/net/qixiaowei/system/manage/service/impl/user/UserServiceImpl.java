package net.qixiaowei.system.manage.service.impl.user;

import java.util.List;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.vo.UserVO;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;
import net.qixiaowei.system.manage.api.vo.user.UserInfoVO;
import net.qixiaowei.system.manage.service.system.IRoleMenuService;
import net.qixiaowei.system.manage.service.system.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

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
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setCreateBy(SecurityUtils.getUserId());
        user.setCreateTime(DateUtils.getNowDate());
        user.setUpdateTime(DateUtils.getNowDate());
        user.setUpdateBy(SecurityUtils.getUserId());
        user.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return userMapper.insertUser(user);
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
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setUpdateTime(DateUtils.getNowDate());
        user.setUpdateBy(SecurityUtils.getUserId());
        return userMapper.updateUser(user);
    }

    /**
     * 逻辑批量删除用户表
     *
     * @param userDtos 需要删除的用户表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int logicDeleteUserByUserIds(List<UserDTO> userDtos) {
        List<Long> stringList = new ArrayList();
        for (UserDTO userDTO : userDtos) {
            stringList.add(userDTO.getUserId());
        }
        return userMapper.logicDeleteUserByUserIds(stringList, userDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
    }

    /**
     * 物理删除用户表信息
     *
     * @param userId 用户表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int deleteUserByUserId(Long userId) {
        return userMapper.deleteUserByUserId(userId);
    }

    /**
     * 逻辑删除用户表信息
     *
     * @param userDTO 用户表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteUserByUserId(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return userMapper.logicDeleteUserByUserId(user, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除用户表信息
     *
     * @param userDTO 用户表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteUserByUserId(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return userMapper.deleteUserByUserId(user.getUserId());
    }

    /**
     * 物理批量删除用户表
     *
     * @param userDtos 需要删除的用户表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteUserByUserIds(List<UserDTO> userDtos) {
        List<Long> stringList = new ArrayList();
        for (UserDTO userDTO : userDtos) {
            stringList.add(userDTO.getUserId());
        }
        return userMapper.deleteUserByUserIds(stringList);
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
}

