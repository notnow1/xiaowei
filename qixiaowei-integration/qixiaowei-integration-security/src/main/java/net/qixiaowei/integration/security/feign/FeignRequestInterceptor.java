package net.qixiaowei.integration.security.feign;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.utils.ServletUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.ip.IpUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign 请求拦截器
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest httpServletRequest = ServletUtils.getRequest();
        if (StringUtils.isNotNull(httpServletRequest)) {
            Map<String, String> headers = ServletUtils.getHeaders(httpServletRequest);
            // 传递用户信息请求头，防止丢失
            String userId = headers.get(SecurityConstants.DETAILS_USER_ID);
            if (StringUtils.isNotEmpty(userId)) {
                requestTemplate.header(SecurityConstants.DETAILS_USER_ID, userId);
            }
            String userKey = headers.get(SecurityConstants.USER_KEY);
            if (StringUtils.isNotEmpty(userKey)) {
                requestTemplate.header(SecurityConstants.USER_KEY, userKey);
            }
            String userAccount = headers.get(SecurityConstants.DETAILS_USER_ACCOUNT);
            if (StringUtils.isNotEmpty(userAccount)) {
                requestTemplate.header(SecurityConstants.DETAILS_USER_ACCOUNT, userAccount);
            }
            String authentication = headers.get(SecurityConstants.AUTHORIZATION_HEADER);
            if (StringUtils.isNotEmpty(authentication)) {
                requestTemplate.header(SecurityConstants.AUTHORIZATION_HEADER, authentication);
            }
            String tenantId = headers.get(SecurityConstants.DETAILS_TENANT_ID);
            if (StringUtils.isNotEmpty(tenantId)) {
                requestTemplate.header(SecurityConstants.DETAILS_TENANT_ID, tenantId);
            }
            String employeeId = headers.get(SecurityConstants.DETAILS_EMPLOYEE_ID);
            if (StringUtils.isNotEmpty(employeeId)) {
                requestTemplate.header(SecurityConstants.DETAILS_EMPLOYEE_ID, employeeId);
            }

            // 配置客户端IP
            requestTemplate.header("X-Forwarded-For", IpUtils.getIpAddr(ServletUtils.getRequest()));
        }
    }
}