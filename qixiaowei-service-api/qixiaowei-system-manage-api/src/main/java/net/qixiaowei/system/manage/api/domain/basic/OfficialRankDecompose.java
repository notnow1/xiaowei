package net.qixiaowei.system.manage.api.domain.basic;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
 * 职级分解表
 *
 * @author Graves
 * @since 2022-10-07
 */
@Data
@Accessors(chain = true)
public class OfficialRankDecompose extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long officialRankDecomposeId;
    /**
     * 职级体系ID
     */
    private Long officialRankSystemId;
    /**
     * 职级分解维度:1部门;2区域;3省份;4产品
     */
    private Integer rankDecomposeDimension;
    /**
     * 具体分解维度的ID
     */
    private Long decomposeDimension;
    /**
     * 工资系数
     */
    private BigDecimal salaryFactor;

}

