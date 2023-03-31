package net.qixiaowei.strategy.cloud.api.domain.marketInsight;


import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;

import java.math.BigDecimal;

import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 市场洞察客户选择表
 *
 * @author TANGMICHI
 * @since 2023-03-07
 */
@Data
@Accessors(chain = true)
public class MiCustomerChoice extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long miCustomerChoiceId;
    /**
     * 市场洞察客户ID
     */
    private Long marketInsightCustomerId;
    /**
     * 行业ID
     */
    private Long industryId;
    /**
     * 行业明朝
     */
    private String industryName;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 准入标记:0否;1是
     */
    private Integer admissionFlag;
    /**
     * 客户类别
     */
    private Long customerCategory;
    /**
     * 排序
     */
    private Integer sort;

}

