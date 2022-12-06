package net.qixiaowei.integration.tenant.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 多租户上下文的Holder
 *
 * @author hzk
 */
public class TenantContextHolder {

    /**
     * 是否忽略租户
     */
    private static final ThreadLocal<Boolean> IGNORE_TENANT = new TransmittableThreadLocal<>();


    public static void setIgnoreTenant(Boolean ignoreTenant) {
        IGNORE_TENANT.set(ignoreTenant);
    }

    /**
     * 当前是否忽略租户
     *
     * @return 是否忽略
     */
    public static boolean isIgnoreTenant() {
        return Boolean.TRUE.equals(IGNORE_TENANT.get());
    }

    public static void remove() {
        IGNORE_TENANT.remove();
    }

}
