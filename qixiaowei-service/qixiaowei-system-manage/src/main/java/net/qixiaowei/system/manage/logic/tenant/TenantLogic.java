package net.qixiaowei.system.manage.logic.tenant;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.enums.system.DictionaryTypeCode;
import net.qixiaowei.integration.common.enums.system.RoleCode;
import net.qixiaowei.integration.common.enums.system.RoleDataScope;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.tenant.annotation.IgnoreTenant;
import net.qixiaowei.integration.tenant.utils.TenantUtils;
import net.qixiaowei.system.manage.api.domain.basic.Config;
import net.qixiaowei.system.manage.api.domain.basic.DictionaryData;
import net.qixiaowei.system.manage.api.domain.basic.DictionaryType;
import net.qixiaowei.system.manage.api.domain.basic.Indicator;
import net.qixiaowei.system.manage.api.domain.system.Role;
import net.qixiaowei.system.manage.api.domain.system.RoleMenu;
import net.qixiaowei.system.manage.api.domain.system.UserRole;
import net.qixiaowei.system.manage.api.domain.tenant.Tenant;
import net.qixiaowei.system.manage.api.domain.user.User;
import net.qixiaowei.system.manage.mapper.basic.ConfigMapper;
import net.qixiaowei.system.manage.mapper.basic.DictionaryDataMapper;
import net.qixiaowei.system.manage.mapper.basic.DictionaryTypeMapper;
import net.qixiaowei.system.manage.mapper.basic.IndicatorMapper;
import net.qixiaowei.system.manage.mapper.system.RoleMapper;
import net.qixiaowei.system.manage.mapper.system.RoleMenuMapper;
import net.qixiaowei.system.manage.mapper.system.UserRoleMapper;
import net.qixiaowei.system.manage.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @description 租户相关逻辑处理
 * @Author hzk
 * @Date 2022-12-02 11:38
 **/
@Component
@Slf4j
public class TenantLogic {

    private static final Map<Integer, String> DICTIONARY_LABEL = new HashMap<>();

    private static final Map<Integer, Indicator> INIT_INDICATOR = new HashMap<>();

    static {
        DICTIONARY_LABEL.put(1, "通用件");
        DICTIONARY_LABEL.put(2, "标准件");
        DICTIONARY_LABEL.put(3, "自制件");
        DICTIONARY_LABEL.put(4, "外购件");
        DICTIONARY_LABEL.put(5, "外协件");
        DICTIONARY_LABEL.put(6, "原材料");

        INIT_INDICATOR.put(1, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW001").indicatorName("订单（不含税）").sort(1).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(2, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW002").indicatorName("销售收入").sort(2).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(3, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW003").indicatorName("销售成本").sort(3).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(4, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW004").indicatorName("材料成本").sort(1).level(2).indicatorValueType(1).choiceFlag(1).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(5, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW005").indicatorName("材料成本率").sort(2).level(2).indicatorValueType(2).choiceFlag(1).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(6, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW006").indicatorName("直接制造人工").sort(3).level(2).indicatorValueType(1).choiceFlag(1).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(7, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW007").indicatorName("直接制造人工率").sort(4).level(2).indicatorValueType(2).choiceFlag(1).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(8, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW008").indicatorName("间接制造人工").sort(5).level(2).indicatorValueType(1).choiceFlag(1).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(9, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW009").indicatorName("间接制造人工率").sort(6).level(2).indicatorValueType(2).choiceFlag(1).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(10, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW010").indicatorName("销售毛利").sort(4).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(11, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW011").indicatorName("销售毛利率").sort(5).level(1).indicatorValueType(2).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(12, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW012").indicatorName("研发费用").sort(6).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(13, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW013").indicatorName("销售费用").sort(7).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(14, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW014").indicatorName("管理费用").sort(8).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(15, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW015").indicatorName("其他业务收支").sort(9).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(16, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW016").indicatorName("营业利润（EBIT）").sort(10).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(17, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW017").indicatorName("财务费用").sort(11).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(0).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(18, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW018").indicatorName("税前利润").sort(12).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(19, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW019").indicatorName("企业所得税").sort(13).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(20, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW020").indicatorName("净利润").sort(14).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(21, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW021").indicatorName("净利润率").sort(15).level(1).indicatorValueType(2).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(22, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW022").indicatorName("回款金额（含税）").sort(16).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(23, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW023").indicatorName("经营性现金流").sort(17).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(24, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW024").indicatorName("总资产").sort(18).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(25, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW025").indicatorName("资产负债率").sort(19).level(1).indicatorValueType(2).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(26, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW026").indicatorName("现金及现金等价物").sort(20).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
        INIT_INDICATOR.put(27, Indicator.builder().parentIndicatorId(Constants.TOP_PARENT_ID).ancestors("").indicatorType(1).indicatorCode("CW027").indicatorName("运营资产（存货+应收）").sort(21).level(1).indicatorValueType(1).choiceFlag(1).examineDirection(1).drivingFactorFlag(0).build());
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


    @IgnoreTenant
    public void initTenantData(Tenant tenant) {
        //1、初始化用户---user
        //2、初始化用户角色+用户关联角色---role、user_role
        //3、角色赋权---role_menu
        this.initUserInfo(tenant);
        Long tenantId = tenant.getTenantId();
        TenantUtils.execute(tenantId, () -> {
            //4、配置，如启用行业配置---config
            this.initConfig(tenantId);
            //5、初始化产品类别---dictionary_type、dictionary_data
            this.initDictionary(tenantId);
            //6、初始化预置指标---indicator
            this.initIndicator(tenantId);
            //7、初始化工资条---salary_item todo 增加远程接口
            //continue...
        });
    }


    public void initUserInfo(Tenant tenant) {
        Long tenantId = tenant.getTenantId();
        //新增用户
        User user = new User();
        user.setTenantId(tenantId);
        user.setUserAccount(tenant.getAdminAccount());
        user.setPassword(SecurityUtils.encryptPassword(tenant.getAdminPassword()));
        user.setStatus(BusinessConstants.NORMAL);
        user.setUserName(RoleCode.TENANT_ADMIN.getInfo());
        userMapper.initTenantUser(user);
        Role role = new Role();
        role.setTenantId(tenantId);
        role.setDataScope(RoleDataScope.ALL.getCode());
        role.setRoleCode(RoleCode.TENANT_ADMIN.getCode());
        role.setRoleName(RoleCode.TENANT_ADMIN.getInfo());
        role.setStatus(BusinessConstants.NORMAL);
        //新增租户角色
        roleMapper.initTenantRole(role);
        //角色赋权 todo
        Long roleId = role.getRoleId();
        List<RoleMenu> list = new ArrayList<>();
//        for (Long menuId : menuIds) {
//            RoleMenu rm = new RoleMenu();
//            rm.setTenantId(tenantId);
//            rm.setRoleId(roleId);
//            rm.setMenuId(menuId);
//            list.add(rm);
//        }
        if (list.size() > 0) {
            roleMenuMapper.batchInitTenantRoleMenu(list);
        }
        //用户角色关联
        UserRole userRole = new UserRole();
        userRole.setTenantId(tenantId);
        userRole.setUserId(user.getUserId());
        userRole.setRoleId(role.getRoleId());
        userRoleMapper.initTenantUserRole(userRole);
    }

    public void initConfig(Long tenantId) {
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
        configMapper.insertConfig(basicConfig);
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
        configMapper.insertConfig(config);
    }


    public void initDictionary(Long tenantId) {
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
        dictionaryTypeMapper.insertDictionaryType(dictionaryType);
        Long dictionaryTypeId = dictionaryType.getDictionaryTypeId();
        //初始化字典数据
        List<DictionaryData> dictionaryData = new ArrayList<>(6);
        Integer sort = 1;
        DictionaryData dictionaryData1 = new DictionaryData();
        dictionaryData1.setDictionaryTypeId(dictionaryTypeId);
        dictionaryData1.setDictionaryLabel(DICTIONARY_LABEL.get(sort));
        dictionaryData1.setDictionaryValue(sort.toString());
        dictionaryData1.setDefaultFlag(1);
        dictionaryData1.setSort(sort);
        dictionaryData1.setRemark("");
        dictionaryData1.setStatus(BusinessConstants.NORMAL);
        dictionaryData1.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        dictionaryData1.setCreateBy(userId);
        dictionaryData1.setUpdateBy(userId);
        dictionaryData1.setCreateTime(nowDate);
        dictionaryData1.setUpdateTime(nowDate);
        dictionaryData.add(dictionaryData1);
        sort++;
        for (; sort < 7; sort++) {
            dictionaryData1.setDictionaryLabel(DICTIONARY_LABEL.get(sort));
            dictionaryData1.setDictionaryValue(sort.toString());
            dictionaryData1.setDefaultFlag(0);
            dictionaryData1.setSort(sort);
            dictionaryData.add(dictionaryData1);
        }
        dictionaryDataMapper.batchDictionaryData(dictionaryData);
    }


    public void initIndicator(Long tenantId) {
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        //先初始化销售成本
        Indicator indicator = INIT_INDICATOR.get(3);
        indicator.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        indicator.setCreateBy(userId);
        indicator.setUpdateBy(userId);
        indicator.setCreateTime(nowDate);
        indicator.setUpdateTime(nowDate);
        indicatorMapper.insertIndicator(indicator);
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
        indicatorMapper.batchIndicator(indicators);

    }
}
