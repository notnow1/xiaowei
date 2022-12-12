package net.qixiaowei.integration.tenant.utils;

import net.qixiaowei.integration.common.context.SecurityContextHolder;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.tenant.context.TenantContextHolder;

/**
 * @description 租户处理工具
 * @Author hzk
 * @Date 2022-12-12 15:13
 **/
public class TenantUtils {

    /**
     * 使用指定租户，执行对应的逻辑
     * 如果当前是忽略租户的情况下，会被强制设置成不忽略租户
     * 执行完成后，再恢复回去
     *
     * @param tenantId 租户ID
     * @param runnable 执行逻辑
     */
    public static void execute(Long tenantId, Runnable runnable) {
        if (StringUtils.isNull(tenantId)) {
            throw new ServiceException("租户ID不能为空");
        }
        String oldTenantId = SecurityContextHolder.getTenantId().toString();
        Boolean oldIgnore = TenantContextHolder.isIgnoreTenant();
        try {
            SecurityContextHolder.setTenantId(tenantId.toString());
            TenantContextHolder.setIgnoreTenant(false);
            // 执行逻辑
            runnable.run();
        } finally {
            SecurityContextHolder.setTenantId(oldTenantId);
            TenantContextHolder.setIgnoreTenant(oldIgnore);
        }
    }


}
