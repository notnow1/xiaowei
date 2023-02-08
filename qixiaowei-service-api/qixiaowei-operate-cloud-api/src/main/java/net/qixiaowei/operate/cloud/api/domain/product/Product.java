package net.qixiaowei.operate.cloud.api.domain.product;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
 * 产品表
 *
 * @author TANGMICHI
 * @since 2022-10-08
 */
@Data
@Accessors(chain = true)
public class Product extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long productId;
    /**
     * 父级产品ID
     */
    private Long parentProductId;
    /**
     * 父级产品名称
     */
    private String parentProductName;
    /**
     * 祖级列表ID，按层级用英文逗号隔开
     */
    private String ancestors;
    /**
     * 产品编码
     */
    private String productCode;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 层级
     */
    private Integer level;
    /**
     * 产品单位ID
     */
    private Long productUnitId;
    private String productUnitName;

    /**
     * 产品类别
     */
    private String productCategory;
    /**
     * 产品描述
     */
    private String productDescription;
    /**
     * 目录价
     */
    private BigDecimal listPrice;
    /**
     * 上架标记：0下架;1上架
     */
    private Integer listingFlag;


}

