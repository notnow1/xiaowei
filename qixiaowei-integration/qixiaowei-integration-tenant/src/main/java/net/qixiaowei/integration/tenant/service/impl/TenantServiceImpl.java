package net.qixiaowei.integration.tenant.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.constant.CacheConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.integration.tenant.service.ITenantService;
import net.qixiaowei.system.manage.api.remote.tenant.RemoteTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * @description: 租户接口实现类
 * @Author: hzk
 * @date: 2022/12/16 15:39
 **/
@Service
@Slf4j
public class TenantServiceImpl implements ITenantService {


    @Autowired
    private RemoteTenantService remoteTenantService;

    @Autowired
    private RedisService redisService;

    @Override
    public List<Long> getTenantIds() {
        List<Long> tenantIds = null;
        try {
            Boolean hadCache = redisService.hasKey(CacheConstants.TENANT_IDS_KEY);
            if (hadCache) {
                tenantIds = redisService.getCacheList(CacheConstants.TENANT_IDS_KEY);
                if (StringUtils.isNotEmpty(tenantIds)) {
                    return tenantIds;
                }
            }
            R<List<Long>> remoteTenantServiceTenantIds = remoteTenantService.getTenantIds(SecurityConstants.INNER);
            if (R.SUCCESS != remoteTenantServiceTenantIds.getCode()) {
                log.error("获取租户集合失败:{}", remoteTenantServiceTenantIds.getMsg());
                return tenantIds;
            }
            tenantIds = remoteTenantServiceTenantIds.getData();

        } catch (Exception e) {
            log.error("获取租户ID集合异常，原因为:{}", e.getMessage());
        }
        return tenantIds;
    }

}
