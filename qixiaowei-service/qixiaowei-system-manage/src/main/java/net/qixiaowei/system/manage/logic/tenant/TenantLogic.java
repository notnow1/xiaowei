package net.qixiaowei.system.manage.logic.tenant;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.context.SecurityContextHolder;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.message.BusinessSubtype;
import net.qixiaowei.integration.common.enums.system.RoleCode;
import net.qixiaowei.integration.common.enums.system.RoleDataScope;
import net.qixiaowei.integration.common.enums.system.RoleType;
import net.qixiaowei.integration.common.enums.user.UserType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.ServletUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.sign.SalesSignUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.tenant.annotation.IgnoreTenant;
import net.qixiaowei.integration.tenant.utils.TenantUtils;
import net.qixiaowei.message.api.dto.backlog.BacklogSendDTO;
import net.qixiaowei.message.api.remote.backlog.RemoteBacklogService;
import net.qixiaowei.operate.cloud.api.remote.RemoteOperateCloudInitDataService;
import net.qixiaowei.sales.cloud.api.dto.sync.SyncResisterDTO;
import net.qixiaowei.sales.cloud.api.dto.sync.SyncTenantUpdateDTO;
import net.qixiaowei.sales.cloud.api.remote.sync.RemoteSyncAdminService;
import net.qixiaowei.sales.cloud.api.vo.sync.SalesLoginVO;
import net.qixiaowei.strategy.cloud.api.remote.RemoteStrategyCloudInitDataService;
import net.qixiaowei.system.manage.api.domain.basic.Config;
import net.qixiaowei.system.manage.api.domain.system.Role;
import net.qixiaowei.system.manage.api.domain.system.RoleMenu;
import net.qixiaowei.system.manage.api.domain.system.UserRole;
import net.qixiaowei.system.manage.api.domain.tenant.Tenant;
import net.qixiaowei.system.manage.api.domain.user.User;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.mapper.basic.ConfigMapper;
import net.qixiaowei.system.manage.mapper.system.RoleMapper;
import net.qixiaowei.system.manage.mapper.system.RoleMenuMapper;
import net.qixiaowei.system.manage.mapper.system.UserRoleMapper;
import net.qixiaowei.system.manage.mapper.tenant.TenantMapper;
import net.qixiaowei.system.manage.mapper.user.UserMapper;
import net.qixiaowei.system.manage.service.basic.IDepartmentService;
import net.qixiaowei.system.manage.service.basic.IDictionaryTypeService;
import net.qixiaowei.system.manage.service.basic.IEmployeeService;
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

    public static final List<Long> SALES_MENUS = Arrays.asList(55L,
            //应用管理-查看应用,停用/启用应用
            56L, 86L, 175L,
            //办公管理-办公审批流,业务参数设置,日志模板设置,日志打印模板设置,办公审批打印模板设置
            176L, 177L, 178L, 179L, 180L, 181L,
            //客户管理设置-自定义字段设置,业务审批流,客户公海规则,打印模板设置,业务参数设置,业绩目标设置,市场活动表单设置
            182L, 186L, 187L, 188L, 191L, 192L, 193L, 195L,
            //初始化-初始化管理
            196L, 197L,
            //通讯录-查看
            200L, 201L,
            //公告-新建,编辑,删除
            203L, 204L, 205L, 211L,
            //线索管理-新建,编辑,查看列表,查看详情,导入,导出,删除,转移,转化
            212L, 213L, 228L, 229L, 230L, 238L, 239L, 241L, 242L, 243L,
            //市场活动-新建,查看列表,编辑,删除,启用/停用,查看详情
            253L, 254L, 255L, 260L, 261L, 262L, 265L,
            //客户管理-设置成交状态,新建,编辑,查看列表,查看详情,导入,导出,删除,转移,放入公海,锁定/解锁,编辑团队成员
            266L, 267L, 271L, 272L, 273L, 277L, 280L, 282L, 283L, 284L, 286L, 287L, 289L,
            //联系人管理-新建, 编辑, 查看列表, 查看详情, 删除, 转移, 联系人导出, 联系人导入, 编辑团队成员
            290L, 291L, 292L, 293L, 294L, 295L, 298L, 299L, 300L, 301L,
            //商机管理-新建, 编辑, 查看列表, 查看详情, 删除, 转移, 编辑团队成员, 导出, 打印
            302L, 303L, 304L, 307L, 308L, 309L, 312L, 313L, 314L, 316L,
            //合同管理-新建, 编辑, 查看列表, 查看详情, 删除, 转移, 编辑团队成员, 合同作废, 导出, 打印
            317L, 318L, 320L, 321L, 322L, 324L, 325L, 326L, 328L, 329L, 330L,
            //回款计划-新建, 转移, 编辑, 查看列表, 查看详情, 删除, 导出
            333L, 334L, 335L, 336L, 337L, 338L, 341L, 342L,
            //回款管理-新建, 编辑, 查看列表, 查看详情, 删除, 转移, 导出, 打印, 编辑团队成员
            343L, 345L, 351L, 352L, 359L, 367L, 399L, 427L, 435L, 447L,
            //产品配置-产品新增, 产品编辑, 产品详情, 导入, 导出, 删除
            450L, 451L, 452L, 453L, 454L, 455L, 456L,
            //客户回访管理-新建, 编辑, 查看列表, 查看详情, 删除
            457L, 458L, 459L, 460L, 461L, 462L,
            //发票管理-新建, 编辑, 查看列表, 查看详情, 删除, 转移, 标记开票, 重置开票信息, 导出
            463L, 464L, 465L, 466L, 467L, 468L, 469L, 470L, 471L, 472L,
            //跟进记录管理-查看, 新建, 编辑, 删除, 导入, 导出
            473L, 474L, 475L, 476L, 477L, 478L, 479L,
            //数据分析-办公分析, 业绩目标完成情况, 销售漏斗, 员工客户分析, 员工业绩分析, 产品分析, 客户画像分析, 排行榜
            480L, 481L, 482L, 483L, 484L, 485L, 486L, 487L, 488L);

    public static final Map<Long, List<Long>> SALES_MENUS_MAPPING = new HashMap<>();

    static {
        //应用管理-查看应用,停用/启用应用
        SALES_MENUS_MAPPING.put(56L, Arrays.asList(163L));
        SALES_MENUS_MAPPING.put(86L, Arrays.asList(164L));
        SALES_MENUS_MAPPING.put(175L, Arrays.asList(165L));
        //办公管理-办公审批流,业务参数设置,日志模板设置,日志打印模板设置,办公审批打印模板设置
        SALES_MENUS_MAPPING.put(176L, Arrays.asList(176L));
        SALES_MENUS_MAPPING.put(177L, Arrays.asList(177L));
        SALES_MENUS_MAPPING.put(178L, Arrays.asList(504L));
        SALES_MENUS_MAPPING.put(179L, Arrays.asList(1131L));
        SALES_MENUS_MAPPING.put(180L, Arrays.asList(1132L));
        SALES_MENUS_MAPPING.put(181L, Arrays.asList(1133L));
        //客户管理设置-自定义字段设置,业务审批流,客户公海规则,打印模板设置,业务参数设置,业绩目标设置,市场活动表单设置
        SALES_MENUS_MAPPING.put(182L, Arrays.asList(180L));
        SALES_MENUS_MAPPING.put(186L, Arrays.asList(181L));
        SALES_MENUS_MAPPING.put(187L, Arrays.asList(505L));
        SALES_MENUS_MAPPING.put(188L, Arrays.asList(182L));
        SALES_MENUS_MAPPING.put(191L, Arrays.asList(500L));
        SALES_MENUS_MAPPING.put(192L, Arrays.asList(183L));
        SALES_MENUS_MAPPING.put(193L, Arrays.asList(184L));
        SALES_MENUS_MAPPING.put(195L, Arrays.asList(926L));
        //初始化-初始化管理
        SALES_MENUS_MAPPING.put(196L, Arrays.asList(923L));
        SALES_MENUS_MAPPING.put(197L, Arrays.asList(924L));
        //通讯录-查看
        SALES_MENUS_MAPPING.put(200L, Arrays.asList(150L));
        SALES_MENUS_MAPPING.put(201L, Arrays.asList(151L));
        //公告-新建,编辑,删除
        SALES_MENUS_MAPPING.put(203L, Arrays.asList(187L));
        SALES_MENUS_MAPPING.put(204L, Arrays.asList(188L));
        SALES_MENUS_MAPPING.put(205L, Arrays.asList(189L));
        SALES_MENUS_MAPPING.put(211L, Arrays.asList(190L));
        //线索管理-新建,编辑,查看列表,查看详情,导入,导出,删除,转移,转化
        SALES_MENUS_MAPPING.put(212L, Arrays.asList(9L));
        SALES_MENUS_MAPPING.put(213L, Arrays.asList(17L));
        SALES_MENUS_MAPPING.put(228L, Arrays.asList(18L));
        SALES_MENUS_MAPPING.put(229L, Arrays.asList(19L));
        SALES_MENUS_MAPPING.put(230L, Arrays.asList(20L));
        SALES_MENUS_MAPPING.put(238L, Arrays.asList(21L));
        SALES_MENUS_MAPPING.put(239L, Arrays.asList(22L));
        SALES_MENUS_MAPPING.put(241L, Arrays.asList(23L));
        SALES_MENUS_MAPPING.put(242L, Arrays.asList(24L));
        SALES_MENUS_MAPPING.put(243L, Arrays.asList(25L));
        //市场活动-新建,查看列表,编辑,删除,启用/停用,查看详情
        SALES_MENUS_MAPPING.put(253L, Arrays.asList(200L));
        SALES_MENUS_MAPPING.put(254L, Arrays.asList(201L));
        SALES_MENUS_MAPPING.put(255L, Arrays.asList(202L));
        SALES_MENUS_MAPPING.put(260L, Arrays.asList(204L));
        SALES_MENUS_MAPPING.put(261L, Arrays.asList(205L));
        SALES_MENUS_MAPPING.put(262L, Arrays.asList(206L));
        SALES_MENUS_MAPPING.put(265L, Arrays.asList(207L));
        //客户管理-设置成交状态,新建,编辑,查看列表,查看详情,导入,导出,删除,转移,放入公海,锁定/解锁,编辑团队成员
        SALES_MENUS_MAPPING.put(266L, Arrays.asList(10L));
        SALES_MENUS_MAPPING.put(267L, Arrays.asList(191L));
        SALES_MENUS_MAPPING.put(271L, Arrays.asList(26L));
        SALES_MENUS_MAPPING.put(272L, Arrays.asList(27L));
        SALES_MENUS_MAPPING.put(273L, Arrays.asList(28L));
        SALES_MENUS_MAPPING.put(277L, Arrays.asList(29L));
        SALES_MENUS_MAPPING.put(280L, Arrays.asList(30L));
        SALES_MENUS_MAPPING.put(282L, Arrays.asList(31L));
        SALES_MENUS_MAPPING.put(283L, Arrays.asList(32L));
        SALES_MENUS_MAPPING.put(284L, Arrays.asList(33L));
        SALES_MENUS_MAPPING.put(286L, Arrays.asList(34L));
        SALES_MENUS_MAPPING.put(287L, Arrays.asList(35L));
        SALES_MENUS_MAPPING.put(289L, Arrays.asList(36L));
        //联系人管理-新建, 编辑, 查看列表, 查看详情, 删除, 转移, 联系人导出, 联系人导入, 编辑团队成员
        SALES_MENUS_MAPPING.put(290L, Arrays.asList(11L));
        SALES_MENUS_MAPPING.put(291L, Arrays.asList(40L));
        SALES_MENUS_MAPPING.put(292L, Arrays.asList(41L));
        SALES_MENUS_MAPPING.put(293L, Arrays.asList(42L));
        SALES_MENUS_MAPPING.put(294L, Arrays.asList(43L));
        SALES_MENUS_MAPPING.put(295L, Arrays.asList(44L));
        SALES_MENUS_MAPPING.put(298L, Arrays.asList(45L));
        SALES_MENUS_MAPPING.put(299L, Arrays.asList(107L));
        SALES_MENUS_MAPPING.put(300L, Arrays.asList(108L));
        SALES_MENUS_MAPPING.put(301L, Arrays.asList(933L));
        //商机管理-新建, 编辑, 查看列表, 查看详情, 删除, 转移, 编辑团队成员, 导出, 打印
        SALES_MENUS_MAPPING.put(302L, Arrays.asList(12L));
        SALES_MENUS_MAPPING.put(303L, Arrays.asList(46L));
        SALES_MENUS_MAPPING.put(304L, Arrays.asList(47L));
        SALES_MENUS_MAPPING.put(307L, Arrays.asList(48L));
        SALES_MENUS_MAPPING.put(308L, Arrays.asList(49L));
        SALES_MENUS_MAPPING.put(309L, Arrays.asList(50L));
        SALES_MENUS_MAPPING.put(312L, Arrays.asList(51L));
        SALES_MENUS_MAPPING.put(313L, Arrays.asList(52L));
        SALES_MENUS_MAPPING.put(314L, Arrays.asList(209L));
        SALES_MENUS_MAPPING.put(316L, Arrays.asList(501L));
        //合同管理-新建, 编辑, 查看列表, 查看详情, 删除, 转移, 编辑团队成员, 合同作废, 导出, 打印
        SALES_MENUS_MAPPING.put(317L, Arrays.asList(13L));
        SALES_MENUS_MAPPING.put(318L, Arrays.asList(53L));
        SALES_MENUS_MAPPING.put(320L, Arrays.asList(54L));
        SALES_MENUS_MAPPING.put(321L, Arrays.asList(55L));
        SALES_MENUS_MAPPING.put(322L, Arrays.asList(56L));
        SALES_MENUS_MAPPING.put(324L, Arrays.asList(57L));
        SALES_MENUS_MAPPING.put(325L, Arrays.asList(58L));
        SALES_MENUS_MAPPING.put(326L, Arrays.asList(59L));
        SALES_MENUS_MAPPING.put(328L, Arrays.asList(192L));
        SALES_MENUS_MAPPING.put(329L, Arrays.asList(208L));
        SALES_MENUS_MAPPING.put(330L, Arrays.asList(502L));
        //回款计划-新建, 转移, 编辑, 查看列表, 查看详情, 删除, 导出
        SALES_MENUS_MAPPING.put(333L, Arrays.asList(936L));
        SALES_MENUS_MAPPING.put(334L, Arrays.asList(937L));
        SALES_MENUS_MAPPING.put(335L, Arrays.asList(1135L));
        SALES_MENUS_MAPPING.put(336L, Arrays.asList(938L));
        SALES_MENUS_MAPPING.put(337L, Arrays.asList(939L));
        SALES_MENUS_MAPPING.put(338L, Arrays.asList(940L));
        SALES_MENUS_MAPPING.put(341L, Arrays.asList(941L));
        SALES_MENUS_MAPPING.put(342L, Arrays.asList(942L));
        //回款管理-新建, 编辑, 查看列表, 查看详情, 删除, 转移, 导出, 打印, 编辑团队成员
        SALES_MENUS_MAPPING.put(343L, Arrays.asList(14L));
        SALES_MENUS_MAPPING.put(345L, Arrays.asList(60L));
        SALES_MENUS_MAPPING.put(351L, Arrays.asList(61L));
        SALES_MENUS_MAPPING.put(352L, Arrays.asList(62L));
        SALES_MENUS_MAPPING.put(359L, Arrays.asList(63L));
        SALES_MENUS_MAPPING.put(367L, Arrays.asList(64L));
        SALES_MENUS_MAPPING.put(399L, Arrays.asList(71L));
        SALES_MENUS_MAPPING.put(427L, Arrays.asList(212L));
        SALES_MENUS_MAPPING.put(435L, Arrays.asList(503L));
        SALES_MENUS_MAPPING.put(447L, Arrays.asList(934L));
        //产品配置-产品新增, 产品编辑, 产品详情, 导入, 导出, 删除
        SALES_MENUS_MAPPING.put(450L, Arrays.asList(67L));
        SALES_MENUS_MAPPING.put(451L, Arrays.asList(65L, 69L));
        SALES_MENUS_MAPPING.put(452L, Arrays.asList(66L, 69L, 70L));
        SALES_MENUS_MAPPING.put(453L, Arrays.asList(68L));
        SALES_MENUS_MAPPING.put(454L, Arrays.asList(109L));
        SALES_MENUS_MAPPING.put(455L, Arrays.asList(110L));
        SALES_MENUS_MAPPING.put(456L, Arrays.asList(211L));
        //客户回访管理-新建, 编辑, 查看列表, 查看详情, 删除
        SALES_MENUS_MAPPING.put(457L, Arrays.asList(400L));
        SALES_MENUS_MAPPING.put(458L, Arrays.asList(401L));
        SALES_MENUS_MAPPING.put(459L, Arrays.asList(402L));
        SALES_MENUS_MAPPING.put(460L, Arrays.asList(403L));
        SALES_MENUS_MAPPING.put(461L, Arrays.asList(404L));
        SALES_MENUS_MAPPING.put(462L, Arrays.asList(405L));
        //发票管理-新建, 编辑, 查看列表, 查看详情, 删除, 转移, 标记开票, 重置开票信息, 导出
        SALES_MENUS_MAPPING.put(463L, Arrays.asList(420L));
        SALES_MENUS_MAPPING.put(464L, Arrays.asList(421L));
        SALES_MENUS_MAPPING.put(465L, Arrays.asList(422L));
        SALES_MENUS_MAPPING.put(466L, Arrays.asList(423L));
        SALES_MENUS_MAPPING.put(467L, Arrays.asList(424L));
        SALES_MENUS_MAPPING.put(468L, Arrays.asList(425L));
        SALES_MENUS_MAPPING.put(469L, Arrays.asList(426L));
        SALES_MENUS_MAPPING.put(470L, Arrays.asList(427L));
        SALES_MENUS_MAPPING.put(471L, Arrays.asList(428L));
        SALES_MENUS_MAPPING.put(472L, Arrays.asList(932L));
        //跟进记录管理-查看, 新建, 编辑, 删除, 导入, 导出
        SALES_MENUS_MAPPING.put(473L, Arrays.asList(440L));
        SALES_MENUS_MAPPING.put(474L, Arrays.asList(441L));
        SALES_MENUS_MAPPING.put(475L, Arrays.asList(442L));
        SALES_MENUS_MAPPING.put(476L, Arrays.asList(443L));
        SALES_MENUS_MAPPING.put(477L, Arrays.asList(444L));
        SALES_MENUS_MAPPING.put(478L, Arrays.asList(928L));
        SALES_MENUS_MAPPING.put(479L, Arrays.asList(929L));
        //数据分析-办公分析, 业绩目标完成情况, 销售漏斗, 员工客户分析, 员工业绩分析, 产品分析, 客户画像分析, 排行榜
        SALES_MENUS_MAPPING.put(480L, Arrays.asList(2L));
        SALES_MENUS_MAPPING.put(481L, Arrays.asList(146L, 147L));
        SALES_MENUS_MAPPING.put(482L, Arrays.asList(97L, 102L));
        SALES_MENUS_MAPPING.put(483L, Arrays.asList(98L, 103L));
        SALES_MENUS_MAPPING.put(484L, Arrays.asList(99L, 104L));
        SALES_MENUS_MAPPING.put(485L, Arrays.asList(101L, 106L));
        SALES_MENUS_MAPPING.put(486L, Arrays.asList(117L, 118L));
        SALES_MENUS_MAPPING.put(487L, Arrays.asList(123L, 124L));
        SALES_MENUS_MAPPING.put(488L, Arrays.asList(125L, 126L));
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
    private IDepartmentService departmentService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private RemoteEmployeeService remoteEmployeeService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private RemoteSyncAdminService remoteSyncAdminService;

    /**
     * @description: 初始化租户数据
     * @Author: hzk
     * @date: 2023/2/2 15:13
     * @param: [tenant, initMenuIds]
     * @return: java.lang.Boolean
     **/
    @IgnoreTenant
    public Boolean initTenantData(Tenant tenant, Date endTime, Set<Long> initMenuIds) {
        AtomicReference<Boolean> initSuccess = new AtomicReference<>(true);
        Long tenantId = tenant.getTenantId();
        TenantUtils.execute(tenantId, () -> {
            //1、初始化用户、用户配置---user
            //2、初始化用户角色+用户关联角色---role、user_role
            //3、角色赋权---role_menu
            Long userId = this.initUserInfo(tenant, endTime, initMenuIds);
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
    public void updateTenantAuth(Tenant updateTenant, Set<Long> initMenuIds, Date endTime) {
        TenantUtils.execute(updateTenant.getTenantId(), () -> {
            //更新租户用户授权
            this.updateTenantAuthOfTenantId(updateTenant, initMenuIds, endTime);
        });
    }

    /**
     * @description: 更新租户用户授权
     * @Author: hzk
     * @date: 2023/2/3 15:30
     * @param: [initMenuIds]
     * @return: void
     **/
    public void updateTenantAuthOfTenantId(Tenant updateTenant, Set<Long> initMenuIds, Date endTime) {
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
            //处理销售云
            this.updateSalesAuth(updateTenant, endTime, roleIdOfAdmin, nowMenuIds, null);
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
        this.updateSalesAuth(updateTenant, endTime, roleIdOfAdmin, addMenuIds, nowMenuIds);
    }

    /**
     * @description: 首次初始化租户到销售云
     * @Author: hzk
     * @date: 2023/4/17 16:27
     * @param: [tenant, endTime]
     * @return: void
     **/
    public void initTenantSales(Tenant tenant, Date endTime) {
        TenantUtils.execute(tenant.getTenantId(), () -> {
            UserDTO userDTO = userMapper.selectUserOfAdmin();
            if (StringUtils.isNotNull(userDTO)) {
                Long roleIdOfAdmin = roleMapper.selectRoleIdOfAdmin();
                this.syncSales(tenant, userDTO.getUserId(), roleIdOfAdmin, endTime, null);
            }
        });

    }

    /**
     * @description: 初始化租户销售云基础部门-部门、人员
     * @Author: hzk
     * @date: 2023/4/17 16:35
     * @param: [tenantId]
     * @return: void
     **/
    public void initTenantSalesBase(Long tenantId) {
        TenantUtils.execute(tenantId, () -> {
            String salesToken = this.getAdminSalesToken(tenantId);
            SecurityContextHolder.setSalesToken(salesToken);
            //初始化部门
            departmentService.initSalesDepartment();
            //初始化人员
            employeeService.initSalesEmployee();

        });
    }

    /**
     * @description: 获取管理员用户的token
     * @Author: hzk
     * @date: 2023/4/17 17:07
     * @param: [tenantId]
     * @return: java.lang.String
     **/
    public String getAdminSalesToken(Long tenantId) {
        String salesToken = "";
        UserDTO userDTO = userMapper.selectUserOfAdmin();
        if (StringUtils.isNotNull(userDTO)) {
            String userAccount = userDTO.getUserAccount();
            Date nowDate = DateUtils.getNowDate();
            String time = DateUtil.formatDateTime(nowDate);
            String saleSign = SalesSignUtils.buildSaleSign(userAccount, time);
            // todo 会挤掉其他用户
            R<SalesLoginVO> r = remoteSyncAdminService.syncLogin(userAccount, tenantId, time, saleSign);
            if (0 != r.getCode()) {
                throw new ServiceException("系统异常");
            }
            SalesLoginVO salesLoginVO = r.getData();
            if (StringUtils.isNotNull(salesLoginVO)) {
                salesToken = salesLoginVO.getAdminToken();
            }
        }
        return salesToken;
    }

    /**
     * @description: 初始化租户用户相关信息
     * @Author: hzk
     * @date: 2022/12/13 10:46
     * @param: [tenant, initMenuIds]
     * @return: boolean
     **/
    public Long initUserInfo(Tenant tenant, Date endTime, Set<Long> initMenuIds) {
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
        //初始化销售云-企业信息、用户、角色菜单
        Set<Long> salesInitMenuIds = this.getSalesInitMenuIds(initMenuIds);
        this.syncSales(tenant, userIdOfInit, roleId, endTime, salesInitMenuIds);
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

    /**
     * @description: 同步销售云
     * @Author: hzk
     * @date: 2023/4/4 11:29
     * @param: [tenant, userId, roleId, endTime, salesInitMenuIds]
     * @return: void
     **/
    private void syncSales(Tenant tenant, Long userId, Long roleId, Date endTime, Set<Long> salesInitMenuIds) {
        String adminAccount = tenant.getAdminAccount();
        String adminPassword = tenant.getAdminPassword();
        SyncResisterDTO syncResisterDTO = new SyncResisterDTO();
        syncResisterDTO.setCompanyName(tenant.getTenantName());
        syncResisterDTO.setCompanyId(tenant.getTenantId());
        syncResisterDTO.setEndTime(endTime);
        syncResisterDTO.setPhone(adminAccount);
        syncResisterDTO.setPassword(adminPassword);
        syncResisterDTO.setInitMenuIds(salesInitMenuIds);
        syncResisterDTO.setRoleId(roleId);
        syncResisterDTO.setUserId(userId);
        String salesSign = SalesSignUtils.buildSaleSign(adminAccount, endTime);
        R<?> r = remoteSyncAdminService.syncRegister(syncResisterDTO, salesSign);
        if (0 != r.getCode()) {
            log.error("同步销售云注册失败:{}", r.getMsg());
            throw new ServiceException("销售云初始化失败");
        }
    }

    /**
     * @description: 根据需要初始化的菜单ID集合，获取到销售云的菜单ID集合
     * @Author: hzk
     * @date: 2023/4/3 10:07
     * @param: [initMenuIds]
     * @return: java.util.Set<java.lang.Long>
     **/
    private Set<Long> getSalesInitMenuIds(Set<Long> initMenuIds) {
        Set<Long> salesInitMenuIds = new HashSet<>();
        if (StringUtils.isNotEmpty(initMenuIds)) {
            for (Long initMenuId : initMenuIds) {
                if (SALES_MENUS_MAPPING.containsKey(initMenuId)) {
                    List<Long> list = SALES_MENUS_MAPPING.get(initMenuId);
                    salesInitMenuIds.addAll(list);
                }
            }
        }
        return salesInitMenuIds;
    }

    /**
     * @description: 更新销售云授权信息
     * @Author: hzk
     * @date: 2023/4/6 20:29
     * @param: [updateTenant, endTime, roleIdOfAdmin, addMenuIds, removeMenuIds]
     * @return: void
     **/
    private void updateSalesAuth(Tenant updateTenant, Date endTime, Long roleIdOfAdmin, Set<Long> addMenuIds, Set<Long> removeMenuIds) {
        Long tenantId = updateTenant.getTenantId();
        String tenantName = updateTenant.getTenantName();
        //处理销售云
        Set<Long> salesAddMenuIds = this.getSalesInitMenuIds(addMenuIds);
        Set<Long> salesRemoveMenuIds = this.getSalesInitMenuIds(removeMenuIds);
        //调用销售云接口
        SyncTenantUpdateDTO syncTenantUpdateDTO = new SyncTenantUpdateDTO();
        syncTenantUpdateDTO.setCompanyId(tenantId);
        syncTenantUpdateDTO.setCompanyName(tenantName);
        syncTenantUpdateDTO.setAddMenuIds(salesAddMenuIds);
        syncTenantUpdateDTO.setRemoveMenuIds(salesRemoveMenuIds);
        syncTenantUpdateDTO.setRoleId(roleIdOfAdmin);
        syncTenantUpdateDTO.setEndTime(endTime);
        String salesSign = SalesSignUtils.buildSaleSign(tenantId.toString(), endTime);
        R<?> r = remoteSyncAdminService.syncUpdate(syncTenantUpdateDTO, salesSign);
        if (0 != r.getCode()) {
            log.error("同步销售云更新租户失败:{}", r.getMsg());
            throw new ServiceException("编辑租户失败，销售云同步异常");
        }
    }

}
