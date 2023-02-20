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
import net.qixiaowei.integration.common.enums.system.DictionaryTypeCode;
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
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryItemService;
import net.qixiaowei.strategy.cloud.api.remote.industry.RemoteIndustryAttractionService;
import net.qixiaowei.system.manage.api.domain.basic.Config;
import net.qixiaowei.system.manage.api.domain.basic.DictionaryData;
import net.qixiaowei.system.manage.api.domain.basic.DictionaryType;
import net.qixiaowei.system.manage.api.domain.basic.Indicator;
import net.qixiaowei.system.manage.api.domain.system.Role;
import net.qixiaowei.system.manage.api.domain.system.RoleMenu;
import net.qixiaowei.system.manage.api.domain.system.UserRole;
import net.qixiaowei.system.manage.api.domain.tenant.Tenant;
import net.qixiaowei.system.manage.api.domain.user.User;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.mapper.basic.ConfigMapper;
import net.qixiaowei.system.manage.mapper.basic.DictionaryDataMapper;
import net.qixiaowei.system.manage.mapper.basic.DictionaryTypeMapper;
import net.qixiaowei.system.manage.mapper.basic.IndicatorMapper;
import net.qixiaowei.system.manage.mapper.system.RoleMapper;
import net.qixiaowei.system.manage.mapper.system.RoleMenuMapper;
import net.qixiaowei.system.manage.mapper.system.UserRoleMapper;
import net.qixiaowei.system.manage.mapper.tenant.TenantMapper;
import net.qixiaowei.system.manage.mapper.user.UserMapper;
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


    private static final List<DictionaryData> INIT_DICTIONARY_DATA = new ArrayList<>(6);

    private static final Map<Integer, Indicator> INIT_INDICATOR = new HashMap<>();

    static {
        //初始化枚举值
        INIT_DICTIONARY_DATA.add(DictionaryData.builder().dictionaryLabel("通用件").dictionaryValue("1").defaultFlag(1).sort(1).status(BusinessConstants.NORMAL).build());
        INIT_DICTIONARY_DATA.add(DictionaryData.builder().dictionaryLabel("标准件").dictionaryValue("2").defaultFlag(0).sort(2).status(BusinessConstants.NORMAL).build());
        INIT_DICTIONARY_DATA.add(DictionaryData.builder().dictionaryLabel("自制件").dictionaryValue("3").defaultFlag(0).sort(3).status(BusinessConstants.NORMAL).build());
        INIT_DICTIONARY_DATA.add(DictionaryData.builder().dictionaryLabel("外购件").dictionaryValue("4").defaultFlag(0).sort(4).status(BusinessConstants.NORMAL).build());
        INIT_DICTIONARY_DATA.add(DictionaryData.builder().dictionaryLabel("外协件").dictionaryValue("5").defaultFlag(0).sort(5).status(BusinessConstants.NORMAL).build());
        INIT_DICTIONARY_DATA.add(DictionaryData.builder().dictionaryLabel("原材料").dictionaryValue("6").defaultFlag(0).sort(6).status(BusinessConstants.NORMAL).build());
        //初始化指标
        INIT_INDICATOR.put(1, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW001").indicatorName("订单（不含税）").sort(1).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(2, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW002").indicatorName("销售收入").sort(2).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(3, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW003").indicatorName("销售成本").sort(3).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(4, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW004").indicatorName("材料成本").sort(1).level(2).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(5, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW005").indicatorName("材料成本率").sort(2).level(2).indicatorValueType(2).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(6, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW006").indicatorName("直接制造人工").sort(3).level(2).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(7, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW007").indicatorName("直接制造人工率").sort(4).level(2).indicatorValueType(2).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(8, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW008").indicatorName("间接制造人工").sort(5).level(2).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(9, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW009").indicatorName("间接制造人工率").sort(6).level(2).indicatorValueType(2).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(10, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW010").indicatorName("销售毛利").sort(4).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(11, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW011").indicatorName("销售毛利率").sort(5).level(1).indicatorValueType(2).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(12, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW012").indicatorName("研发费用").sort(6).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(13, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW013").indicatorName("销售费用").sort(7).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(14, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW014").indicatorName("管理费用").sort(8).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(15, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW015").indicatorName("其他业务收支").sort(9).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(16, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW016").indicatorName("营业利润（EBIT）").sort(10).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(17, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW017").indicatorName("财务费用").sort(11).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(18, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW018").indicatorName("税前利润").sort(12).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(19, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW019").indicatorName("企业所得税").sort(13).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(20, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW020").indicatorName("净利润").sort(14).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(21, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW021").indicatorName("净利润率").sort(15).level(1).indicatorValueType(2).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(22, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW022").indicatorName("回款金额（含税）").sort(16).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(23, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW023").indicatorName("经营性现金流").sort(17).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(24, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW024").indicatorName("总资产").sort(18).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(25, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW025").indicatorName("资产负债率").sort(19).level(1).indicatorValueType(2).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(26, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW026").indicatorName("现金及现金等价物").sort(20).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(27, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW027").indicatorName("运营资产（存货+应收）").sort(21).level(1).indicatorValueType(1).choiceFlag(0).examineDirection(1).drivingFactorFlag(0).build());
    }

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
    private DictionaryTypeMapper dictionaryTypeMapper;

    @Autowired
    private DictionaryDataMapper dictionaryDataMapper;

    @Autowired
    private IndicatorMapper indicatorMapper;

    @Autowired
    private RemoteSalaryItemService remoteSalaryItemService;
    @Autowired
    private RemoteIndustryAttractionService remoteIndustryAttractionService;

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
            boolean initUserInfo = this.initUserInfo(tenant, initMenuIds);
            //4、配置，如启用行业配置---config
            boolean initConfig = this.initConfig();
            //5、初始化产品类别---dictionary_type、dictionary_data
            boolean initDictionary = this.initDictionary();
            //6、初始化预置指标---indicator
            boolean initIndicator = this.initIndicator();
            //7、初始化工资条---salary_item
            boolean initSalaryItem = this.initSalaryItem();
            //7、初始化工资条---salary_item
            boolean initIndustryAttraction = this.initIndustryAttraction();
            initSuccess.set(initUserInfo && initConfig && initDictionary && initIndicator && initSalaryItem && initIndustryAttraction);
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
        Long roleIdOfAdmin = roleMapper.selectRoleIdOfAdmin();
        if (StringUtils.isNull(roleIdOfAdmin)) {
            return;
        }
        Set<Long> nowMenuIds = roleMenuMapper.selectMenuIdsByRoleId(roleIdOfAdmin);
        //新增权限。只给管理员角色。
        if (StringUtils.isEmpty(nowMenuIds)) {
            this.initRoleMenu(initMenuIds, roleIdOfAdmin);
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
        this.initRoleMenu(addMenuIds, roleIdOfAdmin);
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
    public boolean initUserInfo(Tenant tenant, Set<Long> initMenuIds) {
        Long userId = SecurityUtils.getUserId();
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
        user.setCreateBy(userId);
        user.setUpdateBy(userId);
        user.setCreateTime(nowDate);
        user.setUpdateTime(nowDate);
        boolean userSuccess = userMapper.insertUser(user) > 0;
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
        role.setCreateBy(userId);
        role.setUpdateBy(userId);
        role.setCreateTime(nowDate);
        role.setUpdateTime(nowDate);
        boolean roleSuccess = roleMapper.insertRole(role) > 0;
        //角色赋权
        Long roleId = role.getRoleId();
        boolean initRoleMenuSuccess = this.initRoleMenu(initMenuIds, roleId);
        //新增用户角色关联
        boolean userRoleSuccess = this.initUserRole(userId, nowDate, userIdOfInit, roleId);
        return userSuccess && roleSuccess && initRoleMenuSuccess && userRoleSuccess;
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
     * @param: [initMenuIds, roleId]
     * @return: boolean
     **/
    private boolean initRoleMenu(Set<Long> initMenuIds, Long roleId) {
        if (StringUtils.isEmpty(initMenuIds)) {
            return true;
        }
        boolean initRoleMenu = true;
        Long userId = SecurityUtils.getUserId();
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
     * @param: []
     * @return: boolean
     **/
    public boolean initConfig() {
        Long userId = SecurityUtils.getUserId();
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
    public boolean initDictionary() {
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        //初始化字典类型
        DictionaryType dictionaryType = new DictionaryType();
        dictionaryType.setDictionaryType(DictionaryTypeCode.PRODUCT_CATEGORY.getCode());
        dictionaryType.setDictionaryName(DictionaryTypeCode.PRODUCT_CATEGORY.getInfo());
        dictionaryType.setMenuZerothName("设置管理");
        dictionaryType.setMenuFirstName("经营云配置");
        dictionaryType.setMenuSecondName("产品配置");
        dictionaryType.setRemark("");
        dictionaryType.setStatus(BusinessConstants.NORMAL);
        dictionaryType.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        dictionaryType.setCreateBy(userId);
        dictionaryType.setUpdateBy(userId);
        dictionaryType.setCreateTime(nowDate);
        dictionaryType.setUpdateTime(nowDate);
        boolean dictionaryTypeSuccess = dictionaryTypeMapper.insertDictionaryType(dictionaryType) > 0;
        Long dictionaryTypeId = dictionaryType.getDictionaryTypeId();
        //初始化字典数据
        List<DictionaryData> dictionaryData = new ArrayList<>(6);
        for (DictionaryData initDictionaryData : INIT_DICTIONARY_DATA) {
            initDictionaryData.setDictionaryTypeId(dictionaryTypeId);
            initDictionaryData.setRemark("");
            initDictionaryData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            initDictionaryData.setCreateBy(userId);
            initDictionaryData.setUpdateBy(userId);
            initDictionaryData.setCreateTime(nowDate);
            initDictionaryData.setUpdateTime(nowDate);
            dictionaryData.add(initDictionaryData);
        }
        boolean dictionaryDataSuccess = dictionaryDataMapper.batchDictionaryData(dictionaryData) > 0;
        return dictionaryTypeSuccess && dictionaryDataSuccess;
    }

    /**
     * @description: 初始化指标
     * @Author: hzk
     * @date: 2022/12/13 10:47
     * @param: []
     * @return: boolean
     **/
    public boolean initIndicator() {
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        //先初始化销售成本
        Indicator indicator = INIT_INDICATOR.get(3);
        indicator.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        indicator.setCreateBy(userId);
        indicator.setUpdateBy(userId);
        indicator.setCreateTime(nowDate);
        indicator.setUpdateTime(nowDate);
        boolean indicatorSuccess = indicatorMapper.insertIndicator(indicator) > 0;
        Long indicatorId = indicator.getIndicatorId();
        List<Indicator> indicators = new ArrayList<>(26);
        for (Map.Entry<Integer, Indicator> entry : INIT_INDICATOR.entrySet()) {
            Integer key = entry.getKey();
            Indicator value = entry.getValue();
            if (key == 3) {
                continue;
            }
            //销售成本下的二级指标处理
            if (key > 3 && key < 10) {
                value.setParentIndicatorId(indicatorId);
                value.setAncestors(indicatorId.toString());
            }
            value.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            value.setCreateBy(userId);
            value.setUpdateBy(userId);
            value.setCreateTime(nowDate);
            value.setUpdateTime(nowDate);
            indicators.add(value);
        }
        //初始化指标
        boolean indicatorsSuccess = indicatorMapper.batchIndicator(indicators) > 0;
        return indicatorSuccess && indicatorsSuccess;
    }

    /**
     * @description: 初始化工资条
     * @Author: hzk
     * @date: 2022/12/13 10:47
     * @param: []
     * @return: boolean
     **/
    public boolean initSalaryItem() {
        boolean initSalaryItem = true;
        R<Boolean> booleanR = remoteSalaryItemService.initSalaryItem(SecurityConstants.INNER);
        if (R.SUCCESS != booleanR.getCode()) {
            initSalaryItem = false;
        } else {
            Boolean data = booleanR.getData();
            if (!data) {
                initSalaryItem = false;
            }
        }
        return initSalaryItem;
    }
    public boolean initIndustryAttraction() {
        boolean initSalaryItem = true;
        R<Boolean> booleanR = remoteIndustryAttractionService.initIndustryAttraction(SecurityConstants.INNER);
        if (R.SUCCESS != booleanR.getCode()) {
            initSalaryItem = false;
        } else {
            Boolean data = booleanR.getData();
            if (!data) {
                initSalaryItem = false;
            }
        }
        return initSalaryItem;
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
