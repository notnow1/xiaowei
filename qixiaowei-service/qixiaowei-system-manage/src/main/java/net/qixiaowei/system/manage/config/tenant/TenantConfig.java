package net.qixiaowei.system.manage.config.tenant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * @Description: 租户相关配置
 * @title: TenantConfig
 * @Author hzk
 * @Date: 2022/11/29 10:40
 * @Version 1.0
 */
@Data
@Configuration
@RefreshScope
public class TenantConfig {

    /**
     * 租户的主域名
     */
    @Value("${tenant.mainDomain:qixiaowei.net}")
    private String mainDomain;

    /**
     * 已经存在的子域名
     */
    @Value("${tenant.existedDomains:file}")
    private Set<String> existedDomains;

    /**
     * 管理租户的主域名前缀
     */
    @Value("${tenant.adminDomainPrefix:}")
    private String adminDomainPrefix;

    /**
     * 属于管理平台的菜单集合
     */
    @Value("${tenant.adminMenuIds:1,2,11,19,49,48,356}")
    private Set<Long> adminMenuIds;


}
