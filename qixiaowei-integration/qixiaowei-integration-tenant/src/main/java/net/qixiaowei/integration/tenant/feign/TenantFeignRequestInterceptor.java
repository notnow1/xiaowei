package net.qixiaowei.integration.tenant.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.context.SecurityContextHolder;
import net.qixiaowei.integration.common.utils.StringUtils;
import org.springframework.stereotype.Component;


/**
 * 租户feign 请求拦截器
 */
@Component
public class TenantFeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Long tenantId = SecurityContextHolder.getTenantId();
        if (StringUtils.isNotNull(tenantId)) {
            requestTemplate.header(SecurityConstants.DETAILS_TENANT_ID, tenantId.toString());
        }
    }
}