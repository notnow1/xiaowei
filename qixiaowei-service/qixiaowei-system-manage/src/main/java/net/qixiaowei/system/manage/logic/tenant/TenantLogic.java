package net.qixiaowei.system.manage.logic.tenant;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.message.BusinessSubtype;
import net.qixiaowei.integration.common.enums.system.RoleCode;
import net.qixiaowei.integration.common.enums.system.RoleDataScope;
import net.qixiaowei.integration.common.enums.system.RoleType;
import net.qixiaowei.integration.common.enums.user.UserType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.tenant.annotation.IgnoreTenant;
import net.qixiaowei.integration.tenant.utils.TenantUtils;
import net.qixiaowei.message.api.dto.backlog.BacklogSendDTO;
import net.qixiaowei.message.api.remote.backlog.RemoteBacklogService;
import net.qixiaowei.operate.cloud.api.remote.RemoteOperateCloudInitDataService;
import net.qixiaowei.strategy.cloud.api.remote.RemoteStrategyCloudInitDataService;
import net.qixiaowei.system.manage.api.domain.basic.Config;
import net.qixiaowei.system.manage.api.domain.system.Role;
import net.qixiaowei.system.manage.api.domain.system.RoleMenu;
import net.qixiaowei.system.manage.api.domain.system.UserRole;
import net.qixiaowei.system.manage.api.domain.tenant.Tenant;
import net.qixiaowei.system.manage.api.domain.user.User;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.mapper.basic.ConfigMapper;
import net.qixiaowei.system.manage.mapper.system.RoleMapper;
import net.qixiaowei.system.manage.mapper.system.RoleMenuMapper;
import net.qixiaowei.system.manage.mapper.system.UserRoleMapper;
import net.qixiaowei.system.manage.mapper.tenant.TenantMapper;
import net.qixiaowei.system.manage.mapper.user.UserMapper;
import net.qixiaowei.system.manage.service.basic.IDictionaryTypeService;
import net.qixiaowei.system.manage.service.basic.IIndicatorService;
import net.qixiaowei.system.manage.service.user.IUserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @description 租户相关逻辑处理
 * @Author hzk
 * @Date 2022-12-02 11:38
 **/
@Component
@Slf4j
public class TenantLogic {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private IDictionaryTypeService dictionaryTypeService;

    @Autowired
    private IIndicatorService iIndicatorService;

    @Autowired
    private RemoteOperateCloudInitDataService remoteOperateCloudInitDataService;

    @Autowired
    private RemoteStrategyCloudInitDataService remoteStrategyCloudInitDataService;

    @Autowired
    private RemoteBacklogService remoteBacklogService;

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private RemoteEmployeeService remoteEmployeeService;

    @Autowired
    private IUserConfigService userConfigService;

    /**
     * @description: 初始化租户数据
     * @Author: hzk
     * @date: 2023/2/2 15:13
     * @param: [tenant, initMenuIds]
     * @return: java.lang.Boolean
     **/
    @IgnoreTenant
    public Boolean initTenantData(Tenant tenant, Set<Long> initMenuIds) {
        AtomicReference<Boolean> initSuccess = new AtomicReference<>(true);
        Long tenantId = tenant.getTenantId();
        TenantUtils.execute(tenantId, () -> {
            //1、初始化用户、用户配置---user
            //2、初始化用户角色+用户关联角色---role、user_role
            //3、角色赋权---role_menu
            Long userId = this.initUserInfo(tenant, initMenuIds);
            //4、配置，如启用行业配置---config
            boolean initConfig = this.initConfig(userId);
            //5、初始化枚举值---dictionary_type、dictionary_data
            boolean initDictionary = this.initDictionary(userId);
            //6、初始化预置指标---indicator
            boolean initIndicator = this.initIndicator(userId);
            //7、初始化经营云
            boolean initSalaryItem = this.initOperateCloud(userId);
            //8、初始化战略云
            boolean initStrategyCloud = this.initStrategyCloud(userId);
            initSuccess.set(initConfig && initDictionary && initIndicator && initSalaryItem && initStrategyCloud);
            //continue...
        });
        return initSuccess.get();
    }

    /**
     * @description: 更新租户授权菜单
     * @Author: hzk
     * @date: 2023/2/2 15:14
     * @param: [tenantId, initMenuIds]
     * @return: void
     **/
    @IgnoreTenant
    public void updateTenantAuth(Long tenantId, Set<Long> initMenuIds) {
        TenantUtils.execute(tenantId, () -> {
            //更新租户用户授权
            this.updateTenantAuth(initMenuIds);
        });
    }

    /**
     * @description: 更新租户用户授权
     * @Author: hzk
     * @date: 2023/2/3 15:30
     * @param: [initMenuIds]
     * @return: void
     **/
    public void updateTenantAuth(Set<Long> initMenuIds) {
        //更新租户用户授权
        //找到租户管理员目前的权限。
        Long userId = 0L;
        Long roleIdOfAdmin = roleMapper.selectRoleIdOfAdmin();
        if (StringUtils.isNull(roleIdOfAdmin)) {
            return;
        }
        Set<Long> nowMenuIds = roleMenuMapper.selectMenuIdsByRoleId(roleIdOfAdmin);
        //新增权限。只给管理员角色。
        if (StringUtils.isEmpty(nowMenuIds)) {
            this.initRoleMenu(initMenuIds, roleIdOfAdmin, userId);
            return;
        }
        Set<Long> addMenuIds = new HashSet<>();
        if (StringUtils.isNotEmpty(initMenuIds)) {
            for (Long initMenuId : initMenuIds) {
                if (nowMenuIds.contains(initMenuId)) {
                    nowMenuIds.remove(initMenuId);
                } else {
                    addMenuIds.add(initMenuId);
                }
            }
        }
        //新增权限。只给管理员角色。
        this.initRoleMenu(addMenuIds, roleIdOfAdmin, userId);
        //取消权限。改租户所有角色。
        this.cancelRoleMenu(nowMenuIds);
    }


    /**
     * @description: 初始化租户用户相关信息
     * @Author: hzk
     * @date: 2022/12/13 10:46
     * @param: [tenant, initMenuIds]
     * @return: boolean
     **/
    public Long initUserInfo(Tenant tenant, Set<Long> initMenuIds) {
        Date nowDate = DateUtils.getNowDate();
        //新增用户
        User user = new User();
        user.setUserType(UserType.SYSTEM.getCode());
        user.setUserAccount(tenant.getAdminAccount());
        user.setPassword(SecurityUtils.encryptPassword(tenant.getAdminPassword()));
        user.setEmail(tenant.getAdminEmail());
        user.setStatus(BusinessConstants.NORMAL);
        user.setUserName(RoleCode.TENANT_ADMIN.getInfo());
        user.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        user.setCreateBy(0L);
        user.setUpdateBy(0L);
        user.setCreateTime(nowDate);
        user.setUpdateTime(nowDate);
        boolean userSuccess = userMapper.insertUser(user) > 0;
        if (!userSuccess) {
            throw new ServiceException("用户初始化异常，请联系管理员");
        }
        Long userIdOfInit = user.getUserId();
        //新增用户配置
        userConfigService.initUserConfig(userIdOfInit);
        //新增租户角色
        Role role = new Role();
        role.setRoleType(RoleType.BUILT_IN.getCode());
        role.setDataScope(RoleDataScope.ALL.getCode());
        role.setRoleCode(RoleCode.TENANT_ADMIN.getCode());
        role.setRoleName(RoleCode.TENANT_ADMIN.getInfo());
        role.setStatus(BusinessConstants.NORMAL);
        role.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        role.setCreateBy(userIdOfInit);
        role.setUpdateBy(userIdOfInit);
        role.setCreateTime(nowDate);
        role.setUpdateTime(nowDate);
        boolean roleSuccess = roleMapper.insertRole(role) > 0;
        //角色赋权
        Long roleId = role.getRoleId();
        boolean initRoleMenuSuccess = this.initRoleMenu(initMenuIds, roleId, userIdOfInit);
        //新增用户角色关联
        boolean userRoleSuccess = this.initUserRole(userIdOfInit, nowDate, userIdOfInit, roleId);
        if (!roleSuccess || !initRoleMenuSuccess || !userRoleSuccess) {
            throw new ServiceException("用户角色初始化异常，请联系管理员");
        }
        return userIdOfInit;
    }

    /**
     * @description: 初始化用户角色
     * @Author: hzk
     * @date: 2023/1/31 17:49
     * @param: [userId, nowDate, userIdOfInit, roleId]
     * @return: boolean
     **/
    private boolean initUserRole(Long userId, Date nowDate, Long userIdOfInit, Long roleId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userIdOfInit);
        userRole.setRoleId(roleId);
        userRole.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        userRole.setCreateBy(userId);
        userRole.setUpdateBy(userId);
        userRole.setCreateTime(nowDate);
        userRole.setUpdateTime(nowDate);
        return userRoleMapper.insertUserRole(userRole) > 0;
    }

    /**
     * @description: 初始化角色菜单
     * @Author: hzk
     * @date: 2023/1/31 17:45
     * @param: [initMenuIds, roleId,userId]
     * @return: boolean
     **/
    private boolean initRoleMenu(Set<Long> initMenuIds, Long roleId, Long userId) {
        if (StringUtils.isEmpty(initMenuIds)) {
            return true;
        }
        boolean initRoleMenu = true;
        Date nowDate = DateUtils.getNowDate();
        List<RoleMenu> list = new ArrayList<>();
        for (Long menuId : initMenuIds) {
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
            initRoleMenu = roleMenuMapper.batchRoleMenu(list) > 0;
        }
        return initRoleMenu;
    }

    /**
     * @description: 取消授权菜单
     * @Author: hzk
     * @date: 2023/2/2 15:12
     * @param: [initMenuIds]
     * @return: boolean
     **/
    private boolean cancelRoleMenu(Set<Long> initMenuIds) {
        if (StringUtils.isEmpty(initMenuIds)) {
            return true;
        }
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        return roleMenuMapper.cancelRoleMenu(initMenuIds, userId, nowDate) > 0;
    }

    /**
     * @description: 初始化配置
     * @Author: hzk
     * @date: 2022/12/13 10:47
     * @param: [userId]
     * @return: boolean
     **/
    public boolean initConfig(Long userId) {
        Date nowDate = DateUtils.getNowDate();
        //初始化基础的配置
        Config basicConfig = new Config();
        basicConfig.setParentConfigId(Constants.TOP_PARENT_ID);
        basicConfig.setPathCode("");
        basicConfig.setConfigCode("basic");
        basicConfig.setRemark("基础配置");
        basicConfig.setStatus(BusinessConstants.NORMAL);
        basicConfig.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        basicConfig.setCreateBy(userId);
        basicConfig.setUpdateBy(userId);
        basicConfig.setCreateTime(nowDate);
        basicConfig.setUpdateTime(nowDate);
        boolean basicConfigSuccess = configMapper.insertConfig(basicConfig) > 0;
        Long configId = basicConfig.getConfigId();
        //初始化行业启用的配置
        Config config = new Config();
        config.setParentConfigId(configId);
        config.setPathCode("basic");
        config.setConfigCode("industry_enable");
        config.setConfigValue("1");
        config.setRemark("行业启用：1系统；2自定义");
        config.setStatus(BusinessConstants.NORMAL);
        config.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        config.setCreateBy(userId);
        config.setUpdateBy(userId);
        config.setCreateTime(nowDate);
        config.setUpdateTime(nowDate);
        boolean configSuccess = configMapper.insertConfig(config) > 0;
        return basicConfigSuccess && configSuccess;
    }

    /**
     * @description: 初始化枚举类
     * @Author: hzk
     * @date: 2022/12/13 10:47
     * @param: []
     * @return: boolean
     **/
    public boolean initDictionary(Long userId) {
        return dictionaryTypeService.initData(userId);
    }

    /**
     * @description: 初始化指标
     * @Author: hzk
     * @date: 2022/12/13 10:47
     * @param: []
     * @return: boolean
     **/
    public boolean initIndicator(Long userId) {
        return iIndicatorService.initData(userId);
    }

    /**
     * @description: 初始化经营云
     * @Author: hzk
     * @date: 2022/12/13 10:47
     * @param: [userId]
     * @return: boolean
     **/
    public boolean initOperateCloud(Long userId) {
        boolean initOperateCloud = true;
        R<Boolean> booleanR = remoteOperateCloudInitDataService.initData(userId, SecurityConstants.INNER);
        if (R.SUCCESS != booleanR.getCode()) {
            initOperateCloud = false;
        } else {
            Boolean data = booleanR.getData();
            if (!data) {
                initOperateCloud = false;
            }
        }
        return initOperateCloud;
    }

    /**
     * @description: 初始化战略云
     * @Author: hzk
     * @date: 2023/3/24 13:37
     * @param: [userId]
     * @return: boolean
     **/
    public boolean initStrategyCloud(Long userId) {
        boolean initStrategyCloud = true;
        R<Boolean> booleanR = remoteStrategyCloudInitDataService.initData(userId, SecurityConstants.INNER);
        if (R.SUCCESS != booleanR.getCode()) {
            initStrategyCloud = false;
        } else {
            Boolean data = booleanR.getData();
            if (!data) {
                initStrategyCloud = false;
            }
        }
        return initStrategyCloud;
    }

    /**
     * @description: 发送待办给客服
     * @Author: hzk
     * @date: 2022/12/13 21:56
     * @param: [tenantDomainApprovalId, tenantDTO]
     * @return: void
     **/
    @IgnoreTenant
    public void sendBacklog(Long tenantDomainApprovalId, TenantDTO tenantDTO) {
        TenantUtils.execute(0L, () -> {
            String supportStaff = tenantDTO.getSupportStaff();
            if (StringUtils.isNotEmpty(supportStaff)) {
                long[] supportStaffs = StrUtil.splitToLong(supportStaff, StrUtil.COMMA);
                List<Long> supportStaffIds = Convert.toList(Long.class, supportStaffs);
                R<List<EmployeeDTO>> listR = remoteEmployeeService.selectByEmployeeIds(supportStaffIds, SecurityConstants.INNER);
                if (R.SUCCESS != listR.getCode()) {
                    throw new ServiceException("查找客服失败");
                }
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    List<BacklogSendDTO> backlogSendDTOS = new ArrayList<>();
                    for (EmployeeDTO employeeDTO : data) {
                        Long userId = employeeDTO.getUserId();
                        BacklogSendDTO backlogSendDTO = new BacklogSendDTO();
                        backlogSendDTO.setBusinessType(BusinessSubtype.TENANT_DOMAIN_APPROVAL.getParentBusinessType().getCode());
                        backlogSendDTO.setBusinessSubtype(BusinessSubtype.TENANT_DOMAIN_APPROVAL.getCode());
                        backlogSendDTO.setBusinessId(tenantDomainApprovalId);
                        backlogSendDTO.setUserId(userId);
                        backlogSendDTO.setBacklogName("二级域名申请");
                        backlogSendDTO.setBacklogInitiator(tenantDTO.getTenantId());
                        backlogSendDTO.setBacklogInitiatorName(tenantDTO.getTenantName());
                        backlogSendDTOS.add(backlogSendDTO);
                    }
                    R<?> insertBacklogs = remoteBacklogService.insertBacklogs(backlogSendDTOS, SecurityConstants.INNER);
                    if (R.SUCCESS != insertBacklogs.getCode()) {
                        throw new ServiceException("申请域名通知失败");
                    }
                }
            }
        });
    }

}
