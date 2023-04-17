package net.qixiaowei.sales.cloud.api.factory.sync;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.sales.cloud.api.dto.sync.*;
import net.qixiaowei.sales.cloud.api.remote.sync.RemoteSyncAdminService;
import net.qixiaowei.sales.cloud.api.vo.sync.SalesLoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


/**
 * 悟空admin服务降级处理
 */
@Component
public class RemoteSyncAdminFallbackFactory implements FallbackFactory<RemoteSyncAdminService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteSyncAdminFallbackFactory.class);

    @Override
    public RemoteSyncAdminService create(Throwable throwable) {
        log.error("悟空admin服务调用失败:{}", throwable.getMessage());
        return new RemoteSyncAdminService() {

            @Override
            public R<?> syncUserAdd(SyncUserDTO syncUserDTO, String salesToken) {
                return R.fail("远程同步新增用户失败:" + throwable.getMessage());
            }

            @Override
            public R<?> syncUserEdit(SyncUserDTO syncUserDTO, String salesToken) {
                return R.fail("远程同步编辑用户失败:" + throwable.getMessage());
            }

            @Override
            public R<?> syncUserSetStatus(SyncUserStatusDTO syncUserStatusDTO, String salesToken) {
                return R.fail("远程同步用户状态失败:" + throwable.getMessage());
            }

            @Override
            public R<?> syncResetPassword(SyncUserStatusDTO syncUserStatusDTO, String salesToken) {
                return R.fail("远程同步用户修改密码失败:" + throwable.getMessage());
            }

            @Override
            public R<?> syncRegister(SyncResisterDTO syncResisterDTO, String source) {
                return R.fail("远程同步注册失败:" + throwable.getMessage());
            }

            @Override
            public R<?> syncUpdate(SyncTenantUpdateDTO syncTenantUpdateDTO, String salesSign) {
                return R.fail("远程同步更新失败:" + throwable.getMessage());
            }

            @Override
            public R<SalesLoginVO> syncLogin(String phone, Long companyId, String time, String source) {
                return R.fail("远程同步登录失败:" + throwable.getMessage());
            }

            @Override
            public R<?> syncRoleAdd(SyncRoleDTO syncRoleDTO, String salesToken) {
                return R.fail("远程同步角色新增失败:" + throwable.getMessage());
            }

            @Override
            public R<?> syncRoleEdit(SyncRoleDTO syncRoleDTO, String salesToken) {
                return R.fail("远程同步角色编辑失败:" + throwable.getMessage());
            }

            @Override
            public R<?> syncRoleDelete(Long roleId, String salesToken) {
                return R.fail("远程同步角色删除失败:" + throwable.getMessage());
            }

            @Override
            public R<?> syncRoleRelatedUser(SyncRoleUserDTO syncRoleUserDTO, String salesToken) {
                return R.fail("远程同步角色用户关联失败:" + throwable.getMessage());
            }

            @Override
            public R<?> syncDeptAdd(SyncDeptDTO syncDeptDTO, String salesToken) {
                return R.fail("远程同步部门新增失败:" + throwable.getMessage());
            }

            @Override
            public R<?> syncDeptEdit(SyncDeptDTO syncDeptDTO, String salesToken) {
                return R.fail("远程同步部门编辑失败:" + throwable.getMessage());
            }

            @Override
            public R<?> syncDeptDelete(Long deptId, String salesToken) {
                return R.fail("远程同步部门删除失败:" + throwable.getMessage());
            }
        };
    }
}
