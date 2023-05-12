package net.qixiaowei.sales.cloud.api.remote.sync;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.sales.cloud.api.dto.sync.*;
import net.qixiaowei.sales.cloud.api.factory.sync.RemoteSyncAdminFallbackFactory;
import net.qixiaowei.sales.cloud.api.vo.sync.SalesLoginVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 悟空admin服务
 */
@FeignClient(contextId = "remoteSyncAdminService", value = "wk-admin", fallbackFactory = RemoteSyncAdminFallbackFactory.class)
public interface RemoteSyncAdminService {
    /**
     * 企业-名称设置等
     */
    @PostMapping("/adminConfig/sync/setAdminConfig")
    R<?> syncSetAdminConfig(@RequestBody AdminCompanyDTO adminCompanyDTO, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);

    /**
     * 用户-新增
     */
    @PostMapping("/adminUser/sync/add")
    R<?> syncUserAdd(@RequestBody SyncUserDTO syncUserDTO, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);

    /**
     * 用户-编辑
     */
    @PostMapping("/adminUser/sync/edit")
    R<?> syncUserEdit(@RequestBody SyncUserDTO syncUserDTO, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);

    /**
     * 用户-状态修改
     */
    @PostMapping("/adminUser/sync/setUserStatus")
    R<?> syncUserSetStatus(@RequestBody SyncUserStatusDTO syncUserStatusDTO, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);

    /**
     * 用户-密码修改
     */
    @PostMapping("/adminUser/sync/resetPassword")
    R<?> syncResetPassword(@RequestBody SyncUserStatusDTO syncUserStatusDTO, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);

    /**
     * 用户-重置帐号
     */
    @PostMapping("/adminUser/sync/usernameEdit")
    R<?> syncUsernameEdit(@RequestParam("id") Long id, @RequestParam("username") String username, @RequestParam("password") String password, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);

    /**
     * 用户-重置主帐号
     */
    @PostMapping("/adminCompanyManager/sync/resetManagerUser")
    R<?> syncResetManagerUser(@RequestBody SyncManagerUserResetDTO syncManagerUserResetDTO, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);

    /**
     * 用户-修改头像
     */
    @PostMapping(value = "/adminUser/updateImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<?> syncUserImg(@RequestPart("file") MultipartFile file, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);

    /**
     * 同步注册
     */
    @PostMapping("/cloud/sync/register")
    R<?> syncRegister(@RequestBody SyncResisterDTO syncResisterDTO, @RequestHeader(SecurityConstants.SALES_SIGN) String salesSign);

    /**
     * 同步更新
     */
    @PostMapping("/cloud/sync/update")
    R<?> syncUpdate(@RequestBody SyncTenantUpdateDTO syncTenantUpdateDTO, @RequestHeader(SecurityConstants.SALES_SIGN) String salesSign);

    /**
     * 同步登录
     */
    @PostMapping("/cloud/sync/login")
    R<SalesLoginVO> syncLogin(@RequestParam("phone") String phone, @RequestParam("companyId") Long companyId, @RequestParam("time") String time, @RequestHeader(SecurityConstants.SALES_SIGN) String salesSign);

    /**
     * 角色-同步新增
     */
    @PostMapping("/adminRole/sync/add")
    R<?> syncRoleAdd(@RequestBody SyncRoleDTO syncRoleDTO, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);

    /**
     * 角色-同步编辑
     */
    @PostMapping("/adminRole/sync/edit")
    R<?> syncRoleEdit(@RequestBody SyncRoleDTO syncRoleDTO, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);

    /**
     * 角色-同步删除
     */
    @PostMapping("/adminRole/sync/delete")
    R<?> syncRoleDelete(@RequestParam("roleId") Long roleId, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);

    /**
     * 角色-关联用户
     */
    @PostMapping("/adminRole/sync/relatedUser")
    R<?> syncRoleRelatedUser(@RequestBody SyncRoleUserDTO syncRoleUserDTO, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);

    /**
     * 部门-新增
     */
    @PostMapping("/adminDept/sync/add")
    R<?> syncDeptAdd(@RequestBody SyncDeptDTO syncDeptDTO, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);

    /**
     * 部门-编辑
     */
    @PostMapping("/adminDept/sync/edit")
    R<?> syncDeptEdit(@RequestBody SyncDeptDTO syncDeptDTO, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);

    /**
     * 部门-删除
     */
    @PostMapping("/adminDept/sync/delete/{deptId}")
    R<?> syncDeptDelete(@PathVariable("deptId") Long deptId, @RequestHeader(SecurityConstants.SALES_TOKEN_NAME) String salesToken);
}
