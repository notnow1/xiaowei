package net.qixiaowei.system.manage.logic.user;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.constant.CacheConstants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.user.UserType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.sales.cloud.api.dto.sync.SyncUserDTO;
import net.qixiaowei.sales.cloud.api.dto.sync.SyncUserStatusDTO;
import net.qixiaowei.sales.cloud.api.remote.sync.RemoteSyncAdminService;
import net.qixiaowei.system.manage.api.domain.system.UserRole;
import net.qixiaowei.system.manage.api.domain.user.User;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.vo.user.UserProfileVO;
import net.qixiaowei.system.manage.mapper.basic.EmployeeMapper;
import net.qixiaowei.system.manage.mapper.system.UserRoleMapper;
import net.qixiaowei.system.manage.mapper.user.UserMapper;
import net.qixiaowei.system.manage.service.user.IUserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description 用户相关逻辑处理
 * @Author hzk
 * @Date 2023-04-13 11:38
 **/
@Component
@Slf4j
public class UserLogic {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RemoteSyncAdminService remoteSyncAdminService;

    /**
     * 查询未分配用户员工列表
     */
    public List<EmployeeDTO> unallocatedEmployees(Long userId) {
        return employeeMapper.unallocatedUserList(userId);
    }

    public UserDTO insertUser(UserDTO userDTO) {
        this.checkUserUnique(userDTO);
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setUserType(UserType.OTHER.getCode());
        user.setCreateBy(SecurityUtils.getUserId());
        user.setCreateTime(DateUtils.getNowDate());
        user.setUpdateTime(DateUtils.getNowDate());
        user.setUpdateBy(SecurityUtils.getUserId());
        user.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        Integer status = user.getStatus();
        if (StringUtils.isNull(status)) {
            user.setStatus(BusinessConstants.NORMAL);
        }
        //新增用户
        int row = userMapper.insertUser(user);
        Long userId = user.getUserId();
        //初始化用户配置
        userConfigService.initUserConfig(userId);
        this.initUserCache(userId);
        userDTO.setUserId(userId);
        //新增用户角色
        insertUserRole(userDTO);
        userDTO.setUserId(userId);
        return userDTO;
    }

    /**
     * 初始化用户缓存
     *
     * @param userId 用户表主键
     * @return 用户表
     */
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

    public void insertUserRole(UserDTO user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Set<Long> roleIds) {
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
     * 校验用户是否唯一
     *
     * @param userDTO 用户
     * @return 结果
     */
    public void checkUserUnique(UserDTO userDTO) {
        List<UserDTO> userDTOS = userMapper.checkUserUnique(userDTO);
        Long userId = userDTO.getUserId();
        if (StringUtils.isNotEmpty(userDTOS)) {
            if (StringUtils.isNotNull(userId)) {
                userDTOS = userDTOS.stream().filter(user -> !userId.equals(user.getUserId())).collect(Collectors.toList());
            }
            if (StringUtils.isNotEmpty(userDTOS)) {
                userDTOS.forEach(user -> {
                    Long employeeId = user.getEmployeeId();
                    String userAccount = user.getUserAccount();
                    String mobilePhone = user.getMobilePhone();
                    String email = user.getEmail();
                    if (StringUtils.isNotNull(employeeId) && employeeId.equals(userDTO.getEmployeeId())) {
                        throw new ServiceException("账号已存在");
                    }
                    if (StringUtils.isNotEmpty(userAccount) && userAccount.equals(userDTO.getUserAccount())) {
                        throw new ServiceException("账号已存在");
                    }
                    if (StringUtils.isNotEmpty(mobilePhone) && mobilePhone.equals(userDTO.getMobilePhone())) {
                        throw new ServiceException("手机号码已存在");
                    }
                    if (StringUtils.isNotEmpty(email) && email.equals(userDTO.getEmail())) {
                        throw new ServiceException("邮箱已存在");
                    }
                });
            }
        }
    }

    /**
     * @description: 销售云同步编辑用户
     * @Author: hzk
     * @date: 2023/4/12 18:14
     * @param: [user]
     * @return: void
     **/
    public void syncSaleEditUser(UserDTO userDTO) {
        String salesToken = SecurityUtils.getSalesToken();
        if (StringUtils.isNotEmpty(salesToken)) {
            SyncUserDTO syncUserDTO = new SyncUserDTO();
            syncUserDTO.setUserId(userDTO.getUserId());
            syncUserDTO.setMobile(userDTO.getMobilePhone());
            syncUserDTO.setEmail(userDTO.getEmail());
            syncUserDTO.setStatus(userDTO.getStatus());
            Set<Long> roleIds = userDTO.getRoleIds();
            if (StringUtils.isNotEmpty(roleIds)) {
                String join = CollUtil.join(roleIds, StrUtil.COMMA);
                syncUserDTO.setRoleId(join);
            }
            R<?> r = remoteSyncAdminService.syncUserEdit(syncUserDTO, salesToken);
            if (0 != r.getCode()) {
                log.error("同步销售云用户编辑失败:{}", r.getMsg());
                throw new ServiceException("用户编辑失败");
            }
        }
    }

    /**
     * @description: 销售云同步用户状态
     * @Author: hzk
     * @date: 2023/4/13 14:31
     * @param: [userIds, status]
     * @return: void
     **/
    public void syncSaleSetUserStatus(Set<Long> userIds, Integer status) {
        String salesToken = SecurityUtils.getSalesToken();
        if (StringUtils.isNotEmpty(salesToken)) {
            SyncUserStatusDTO syncUserStatusDTO = new SyncUserStatusDTO();
            syncUserStatusDTO.setIds(userIds);
            syncUserStatusDTO.setStatus(status);
            R<?> r = remoteSyncAdminService.syncUserSetStatus(syncUserStatusDTO, salesToken);
            if (0 != r.getCode()) {
                log.error("同步销售云用户状态失败:{}", r.getMsg());
                throw new ServiceException("用户状态修改失败");
            }
        }
    }

    /**
     * @description: 重置销售云用户密码
     * @Author: hzk
     * @date: 2023/4/14 11:34
     * @param: [userId, password]
     * @return: void
     **/
    public void syncSaleResetUserPassword(Long userId, String password) {
        if (StringUtils.isNull(userId) || StringUtils.isEmpty(password)) {
            return;
        }
        Set<Long> userIds = new HashSet<>();
        userIds.add(userId);
        this.syncSaleResetUserPassword(userIds, password);
    }

    /**
     * @description: 同步销售云重置用户密码
     * @Author: hzk
     * @date: 2023/4/14 11:34
     * @param: [userIds, password]
     * @return: void
     **/
    public void syncSaleResetUserPassword(Set<Long> userIds, String password) {
        if (StringUtils.isEmpty(userIds) || StringUtils.isEmpty(password)) {
            return;
        }
        String salesToken = SecurityUtils.getSalesToken();
        if (StringUtils.isNotEmpty(salesToken)) {
            SyncUserStatusDTO syncUserStatusDTO = new SyncUserStatusDTO();
            syncUserStatusDTO.setIds(userIds);
            syncUserStatusDTO.setPassword(password);
            R<?> r = remoteSyncAdminService.syncResetPassword(syncUserStatusDTO, salesToken);
            if (0 != r.getCode()) {
                log.error("同步销售云用户密码失败:{}", r.getMsg());
                throw new ServiceException("用户密码修改失败");
            }
        }
    }

}
