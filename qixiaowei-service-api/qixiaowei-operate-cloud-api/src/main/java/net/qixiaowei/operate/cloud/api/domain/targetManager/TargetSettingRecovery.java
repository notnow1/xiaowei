package net.qixiaowei.operate.cloud.api.domain.targetManager;


import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 目标制定回款表
 *
 * @author Graves
 * @since 2022-11-01
 */
@Data
@Accessors(chain = true)
public class TargetSettingRecovery extends BaseEntity {

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
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;

}

