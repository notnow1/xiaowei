package net.qixiaowei.operate.cloud.api.domain.targetManager;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
 * 目标制定回款集合表
 *
 * @author Graves
 * @since 2022-11-01
 */
@Data
@Accessors(chain = true)
public class TargetSettingRecoveries extends TenantEntity {

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
     * 类型:1:应回尽回;2:逾期清理;3:提前还款;4:销售收入目标;5:期末应收账款余额
     */
    private Integer type;
    /**
     * 上年实际值
     */
    private BigDecimal actualLastYear;
    /**
     * 挑战值
     */
    private BigDecimal challengeValue;
    /**
     * 目标值
     */
    private BigDecimal targetValue;
    /**
     * 保底值
     */
    private BigDecimal guaranteedValue;

}

