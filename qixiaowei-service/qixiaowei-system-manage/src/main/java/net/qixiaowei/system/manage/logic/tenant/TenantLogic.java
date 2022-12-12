package net.qixiaowei.system.manage.logic.tenant;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.enums.system.RoleCode;
import net.qixiaowei.integration.common.enums.system.RoleDataScope;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.tenant.annotation.IgnoreTenant;
import net.qixiaowei.system.manage.api.domain.system.Role;
import net.qixiaowei.system.manage.api.domain.system.RoleMenu;
import net.qixiaowei.system.manage.api.domain.system.UserRole;
import net.qixiaowei.system.manage.api.domain.tenant.Tenant;
import net.qixiaowei.system.manage.api.domain.user.User;
import net.qixiaowei.system.manage.mapper.system.RoleMapper;
import net.qixiaowei.system.manage.mapper.system.RoleMenuMapper;
import net.qixiaowei.system.manage.mapper.system.UserRoleMapper;
import net.qixiaowei.system.manage.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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


    @IgnoreTenant
    public void initTenantData(Tenant tenant) {
        //1、初始化用户---user
        //2、初始化用户角色+用户关联角色---role、user_role
        //3、角色赋权---role_menu
        this.initUserInfo(tenant);

        //4、配置，如启用行业配置---config todo

        //5、初始化产品类别---dictionary_type、dictionary_data todo

        //6、初始化预置指标---indicator todo

        //7、初始化工资条---salary_item todo 增加远程接口

        //continue...
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
}
