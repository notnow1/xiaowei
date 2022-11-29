package net.qixiaowei.operate.cloud.api.domain.targetManager;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
 * 目标制定回款表
 *
 * @author Graves
 * @since 2022-11-01
 */
@Data
@Accessors(chain = true)
public class TargetSettingRecovery extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long targetSettingRecoveriesId;
    /**
     * 目标制定ID
     */
    private Long targetSettingId;
    /**
     * 上年年末应收账款余额
     */
    private BigDecimal balanceReceivables;
    /**
     * DSO(应收账款周转天数)基线
     */
    private Integer baselineValue;
    /**
     * DSO(应收账款周转天数)改进天数
     */
    private Integer improveDays;


}

