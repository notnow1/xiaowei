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
    @Value("${tenant.mainDomain:zenglve.com}")
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

    /**
     * 属于配置管理的菜单集合
     */
    @Value("${tenant.configManageMenuIds:4,5,174,11,19,49,48,356,6,8,41,42,43,183,27,184,185,9,44,45,46,189,190,7,10,199,28,29,202,14,33,34,35,173,194,13,30,31,32,198,365,12,36,40,47,206,207,208,17,209,210,16,37,38,39,214,15,215,216,217,218,24,225,226,227,23,57,58,59,231,232,233,22,222,223,224,18,50,490,219,220,221,20,21,234,235,236,25,237,60,61,240,26,63,64,62,244,381,382,383,384,385,388,389,390,391,392,393,419,446,420,448}")
    private Set<Long> configManageMenuIds;

    /**
     * 官网试用天数
     */
    @Value("${tenant.trialDays:7}")
    private Integer trialDays;

    /**
     * 客服人员号码
     */
    @Value("${tenant.supportStaffMobile:17796337792}")
    private String supportStaffMobile;

    /**
     * 是否开启销售云访问
     */
    @Value("${tenant.enableSalesAccess:true}")
    private Boolean enableSalesAccess;
}
