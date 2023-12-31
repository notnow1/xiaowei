package net.qixiaowei.gateway.filter;

import cn.hutool.core.date.DateUtil;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import net.qixiaowei.integration.common.constant.CacheConstants;
import net.qixiaowei.integration.common.constant.HttpStatus;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.TokenConstants;
import net.qixiaowei.integration.common.utils.JwtUtils;
import net.qixiaowei.integration.common.utils.ServletUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.gateway.config.properties.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

/**
 * 网关鉴权
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    // 排除过滤的 uri 地址，nacos自行添加
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Autowired
    private RedisService redisService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (StringUtils.matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }
        String userkey = JwtUtils.getUserKey(claims);
        boolean islogin = redisService.hasKey(getTokenKey(userkey));
        if (!islogin) {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }
        String userId = JwtUtils.getUserId(claims);
        String userAccount = JwtUtils.getUserAccount(claims);
        String salesToken = JwtUtils.getUserSalesToken(claims);
        String tenantId = JwtUtils.getTenantId(claims);
        String employeeId = JwtUtils.getEmployeeId(claims);
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userAccount)) {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }
        //被挤掉提示
        String userNewToken = redisService.getCacheObject(getUserTokenKey(userId));
        if (StringUtils.isNotEmpty(userNewToken)) {
            if (!userNewToken.equals(userkey)) {
                LoginUserVO user = redisService.getCacheObject(getTokenKey(userkey));
                long expireTime = System.currentTimeMillis();
                if (StringUtils.isNotNull(user) && StringUtils.isNotNull(user.getLoginTime())) {
                    expireTime = user.getLoginTime();
                }
                ///删掉key，返回过期
                redisService.deleteObject(userkey);
                String errorMsg = "您的账号" + DateUtil.formatDateTime(DateUtil.date(expireTime)) + "在别处登录。如非本人操作，则密码可能已泄露，建议修改密码";
                return unauthorizedResponse(exchange, errorMsg);
            }
        }

        // 设置用户信息到请求
        addHeader(mutate, SecurityConstants.USER_KEY, userkey);
        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userId);
        addHeader(mutate, SecurityConstants.DETAILS_USER_ACCOUNT, userAccount);
        addHeader(mutate, SecurityConstants.DETAILS_TENANT_ID, tenantId);
        addHeader(mutate, SecurityConstants.DETAILS_EMPLOYEE_ID, employeeId);
        addHeader(mutate, SecurityConstants.SALES_TOKEN_NAME, salesToken);

        // 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = ServletUtils.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name) {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 获取用户缓存key
     */
    private String getUserTokenKey(String userId) {
        return CacheConstants.LOGIN_USER_TOKEN_KEY + userId;
    }

    /**
     * 获取缓存key
     */
    private String getTokenKey(String token) {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }

    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }

    @Override
    public int getOrder() {
        return -200;
    }
}