package net.qixiaowei.operate.cloud.api.domain.targetManager;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
 * 目标分解详情周期表
 *
 * @author TANGMICHI
 * @since 2022-10-28
 */
@Data
@Accessors(chain = true)
public class DecomposeDetailCycles extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long decomposeDetailCyclesId;
    /**
     * 目标分解详情ID
     */
    private Long targetDecomposeDetailsId;
    /**
     * 周期数(顺序递增)
     */
    private Integer cycleNumber;
    /**
     * 周期目标值
     */
    private BigDecimal cycleTarget;
    /**
     * 周期预测值
     */
    private BigDecimal cycleForecast;
    /**
     * 周期实际值
     */
    private BigDecimal cycleActual;


}

