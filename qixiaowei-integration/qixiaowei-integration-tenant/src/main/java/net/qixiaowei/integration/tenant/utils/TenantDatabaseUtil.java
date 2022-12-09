package net.qixiaowei.integration.tenant.utils;

import cn.hutool.core.collection.CollUtil;
import net.qixiaowei.integration.tenant.context.TenantContextHolder;

import java.util.HashSet;
import java.util.Set;

/**
 * @description 租户数据处理工具
 * @Author hzk
 * @Date 2022-12-02 19:13
 **/
public class TenantDatabaseUtil {

    /**
     * 忽略租户的表
     */
    protected static final Set<String> ignoreTables = new HashSet<>();

    static {
        ignoreTables.add("tenant");
        ignoreTables.add("tenant_contacts");
        ignoreTables.add("tenant_contract");
        ignoreTables.add("product_package");
        ignoreTables.add("menu");
        ignoreTables.add("nation");
        ignoreTables.add("country");
        ignoreTables.add("region");
        ignoreTables.add("industry_default");
        ignoreTables.add("message_content_config");
    }

    /**
     * @description: 是否忽略租户
     * @Author: hzk
     * @date: 2022/12/2 19:19
     * @param: [tableName]
     * @return: java.lang.Boolean
     **/
    public static Boolean isIgnore(String tableName) {
        return TenantContextHolder.isIgnoreTenant() || CollUtil.contains(ignoreTables, tableName);
    }
}
