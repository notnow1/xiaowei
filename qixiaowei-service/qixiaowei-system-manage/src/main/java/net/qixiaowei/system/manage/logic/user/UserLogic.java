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
import net.qixiaowei.sales.cloud.api.dto.sync.SyncManagerUserResetDTO;
import net.qixiaowei.sales.cloud.api.dto.sync.SyncUserDTO;
import net.qixiaowei.sales.cloud.api.dto.sync.SyncUserStatusDTO;
import net.qixiaowei.sales.cloud.api.remote.sync.RemoteSyncAdminService;
import net.qixiaowei.system.manage.api.domain.basic.Employee;
import net.qixiaowei.system.manage.api.domain.basic.EmployeeInfo;
import net.qixiaowei.system.manage.api.domain.system.UserRole;
import net.qixiaowei.system.manage.api.domain.user.User;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.vo.user.UserProfileVO;
import net.qixiaowei.system.manage.logic.basic.EmployeeLogic;
import net.qixiaowei.system.manage.mapper.basic.DepartmentMapper;
import net.qixiaowei.system.manage.mapper.basic.EmployeeInfoMapper;
import net.qixiaowei.system.manage.mapper.basic.EmployeeMapper;
import net.qixiaowei.system.manage.mapper.basic.PostMapper;
import net.qixiaowei.system.manage.mapper.system.UserRoleMapper;
import net.qixiaowei.system.manage.mapper.user.UserMapper;
import net.qixiaowei.system.manage.service.user.IUserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
    private EmployeeInfoMapper employeeInfoMapper;
    @Autowired
    private EmployeeLogic employeeLogic;
    @Autowired
    private IUserConfigService userConfigService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RemoteSyncAdminService remoteSyncAdminService;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 查询未分配用户员工列表
     */
    public List<EmployeeDTO> unallocatedEmployees(Long userId) {
        return employeeMapper.unallocatedUserList(userId);
    }

    public Employee insertEmployee(UserDTO userDTO) {
        String employeeName = userDTO.getEmployeeName();
        String userAccount = userDTO.getUserAccount();
        Long departmentId = userDTO.getDepartmentId();
        if (StringUtils.isNull(departmentId)) {
            //部门为空，默认为顶级组织
            DepartmentDTO departmentDTO = departmentMapper.queryCompanyTopDepartment();
            if (StringUtils.isNull(departmentDTO)) {
                throw new ServiceException("部门数据缺失，请联系管理员");
            }
            departmentId = departmentDTO.getDepartmentId();
        }
        //查询是否已经存在员工
        List<EmployeeDTO> employeeDTOList = employeeMapper.selectEmployeeByEmployeeMobile(userAccount);
        if (StringUtils.isNotEmpty(employeeDTOList)) {
            throw new ServiceException("手机号已存在");
        }
        Date nowDate = DateUtils.getNowDate();
        Long userId = SecurityUtils.getUserId();
        String employeeCode = employeeLogic.generateEmployeeCode();
        //员工表
        Employee employee = new Employee();
        employee.setEmployeeCode(employeeCode);
        employee.setEmployeeName(employeeName);
        employee.setEmployeeMobile(userAccount);
        employee.setEmploymentStatus(1);
        employee.setEmployeeDepartmentId(departmentId);
        employee.setStatus(2);
        employee.setCreateBy(userId);
        employee.setCreateTime(nowDate);
        employee.setUpdateTime(nowDate);
        employee.setUpdateBy(userId);
        employee.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        employeeMapper.insertEmployee(employee);
        Long employeeId = employee.getEmployeeId();
        //员工信息表
        EmployeeInfo employeeInfo = new EmployeeInfo();
        employeeInfo.setEmployeeId(employeeId);
        employeeInfo.setCreateBy(userId);
        employeeInfo.setCreateTime(nowDate);
        employeeInfo.setUpdateTime(nowDate);
        employeeInfo.setUpdateBy(userId);
        employeeInfo.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        employeeInfoMapper.insertEmployeeInfo(employeeInfo);
        userDTO.setEmployeeId(employeeId);
        return employee;
    }

    public void updateEmployee(UserDTO userDTO) {
        Long employeeId = userDTO.getEmployeeId();
        if (StringUtils.isNull(employeeId)) {
            this.insertEmployee(userDTO);
            return;
        }
        String userAccount = userDTO.getUserAccount();
        Date nowDate = DateUtils.getNowDate();
        Long userId = SecurityUtils.getUserId();
        //员工表
        Employee employee = new Employee();
        employee.setEmployeeName(userDTO.getEmployeeName());
        employee.setEmployeeDepartmentId(userDTO.getDepartmentId());
        employee.setEmployeeId(employeeId);
        employee.setEmployeeMobile(userAccount);
        employee.setUpdateTime(nowDate);
        employee.setUpdateBy(userId);
        employeeMapper.updateEmployee(employee);
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
                    String userAccount = user.getUserAccount();
                    String email = user.getEmail();
                    if (StringUtils.isNotEmpty(userAccount) && userAccount.equals(userDTO.getUserAccount())) {
                        throw new ServiceException("账号已存在");
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
            syncUserDTO.setDeptId(userDTO.getDepartmentId());
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
     * @description: 同步销售云用户头像
     * @Author: hzk
     * @date: 2023/5/8 18:45
     * @param: [avatarFile]
     * @return: void
     **/
    public void syncSaleUserAvatar(MultipartFile avatarFile) {
        String salesToken = SecurityUtils.getSalesToken();
        if (StringUtils.isNotEmpty(salesToken)) {
            R<?> r = remoteSyncAdminService.syncUserImg(avatarFile, salesToken);
            if (0 != r.getCode()) {
                log.error("同步销售云用户头像失败:{}", r.getMsg());
                throw new ServiceException("用户头像编辑失败");
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

    /**
     * @description: 同步销售云用户
     * @Author: hzk
     * @date: 2023/4/12 18:13
     * @param: [userDTO, userId, status , employee]
     * @return: void
     **/
    public void syncSalesAddUser(UserDTO userDTO, Long userId, Integer status, Employee employee) {
        String userAccount = userDTO.getUserAccount();
        String password = userDTO.getPassword();
        String salesToken = SecurityUtils.getSalesToken();
        if (StringUtils.isNotEmpty(salesToken)) {
            SyncUserDTO syncUserDTO = new SyncUserDTO();
            String userName = employee.getEmployeeName() + "（" + employee.getEmployeeCode() + "）";
            syncUserDTO.setUserId(userId);
            syncUserDTO.setRealname(userName);
            syncUserDTO.setUsername(Optional.ofNullable(userAccount).orElse(employee.getEmployeeMobile()));
            syncUserDTO.setSex(employee.getEmployeeGender());
            syncUserDTO.setMobile(employee.getEmployeeMobile());
            syncUserDTO.setPassword(password);
            syncUserDTO.setEmail(employee.getEmployeeEmail());
            syncUserDTO.setDeptId(employee.getEmployeeDepartmentId());
            syncUserDTO.setStatus(status);
            syncUserDTO.setNum(employee.getEmployeeCode());
            //处理角色
            Set<Long> roleIds = userDTO.getRoleIds();
            if (StringUtils.isNotEmpty(roleIds)) {
                String join = CollUtil.join(roleIds, StrUtil.COMMA);
                syncUserDTO.setRoleId(join);
            }
            Long employeePostId = employee.getEmployeePostId();
            //处理岗位
            if (StringUtils.isNotNull(employeePostId)) {
                PostDTO postDTO = postMapper.selectPostByPostId(employeePostId);
                if (StringUtils.isNotNull(postDTO)) {
                    syncUserDTO.setPost(postDTO.getPostName());
                }
            }
            R<?> r = remoteSyncAdminService.syncUserAdd(syncUserDTO, salesToken);
            if (0 != r.getCode()) {
                log.error("同步销售云用户新增失败:{}", r.getMsg());
                throw new ServiceException("人员新增失败");
            }
        }
    }

    /**
     * @description: 同步销售云-重置帐号
     * @Author: hzk
     * @date: 2023/5/9 20:18
     * @param: [userId, oldUserAccount,userAccount, password,isAdmin]
     * @return: void
     **/
    public void syncSaleResetUserAccount(Long userId, String oldUserAccount, String userAccount, String password, boolean isAdmin) {
        if (StringUtils.isNull(userId) || StringUtils.isEmpty(userAccount) || StringUtils.isEmpty(password)) {
            return;
        }
        String salesToken = SecurityUtils.getSalesToken();
        if (StringUtils.isNotEmpty(salesToken)) {
            R<?> r;
            if (isAdmin) {
                SyncManagerUserResetDTO syncManagerUserResetDTO = new SyncManagerUserResetDTO();
                syncManagerUserResetDTO.setOldPhone(oldUserAccount);
                syncManagerUserResetDTO.setNewPhone(userAccount);
                syncManagerUserResetDTO.setNewPassword(password);
                r = remoteSyncAdminService.syncResetManagerUser(syncManagerUserResetDTO, salesToken);
            } else {
                r = remoteSyncAdminService.syncUsernameEdit(userId, userAccount, password, salesToken);
            }
            if (0 != r.getCode()) {
                log.error("同步销售云用户重置失败:{}", r.getMsg());
                throw new ServiceException("用户密码重置失败");
            }
        }
    }

}
