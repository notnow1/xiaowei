package net.qixiaowei.system.manage.api.vo.tenant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 租户信息返回vo
 *
 * @author hzk
 * @since 2022-12-14
 */
@Data
@Accessors(chain = true)
public class TenantInfoVO {

    private Long tenantId;
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
     * 租户行业名称
     */
    private String tenantIndustryName;
    /**
     * 域名
     */
    private String domain;
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
     * 是否有待审核的域名
     */
    private Boolean hadApprovalDomain = false;
    /**
     * 合同开始时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date contractStartTime;
    /**
     * 合同结束时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date contractEndTime;

}

