package net.qixiaowei.system.manage.api.domain.tenant;


import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 租户表
 *
 * @author TANGMICHI
 * @since 2022-09-24
 */
@Data
@Accessors(chain = true)
public class Tenant extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 租户ID集合
     */
    private List<Long> tenantIds;
    /**
     * 租户编码
     */
    private String tenantCode;
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 租户地址
     */
    private String tenantAddress;
    /**
     * 租户行业
     */
    private Long tenantIndustry;
    /**
     * 域名
     */
    private String domain;
     /**
     * 管理员姓名
     */
     private  String  adminName;
     /**
     * 管理员帐号
     */
    private String adminAccount;
    /**
     * 管理员密码
     */
    private String adminPassword;
    /**
     * 管理员邮箱
     */
    private String adminEmail;
    /**
     * 客服人员
     */
    private String supportStaff;
    /**
     * 租户登录背景图片URL
     */
    private String loginBackground;
    /**
     * 租户logo图片URL
     */
    private String tenantLogo;
    /**
     * 状态（0待初始化 1正常 2禁用 3过期）
     */
    private Integer tenantStatus;

    /**
     * 租户行业名称
     */
    private String tenantIndustryName;

    /**
     * 客服人员名称
     */
    private String supportStaffName;

}

