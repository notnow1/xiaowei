package net.qixiaowei.operate.cloud.api.domain.targetManager;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.util.List;

/**
 * 目标结果表
 *
 * @author TANGMICHI
 * @since 2022-11-07
 */
@Data
@Accessors(chain = true)
public class TargetOutcome extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long targetOutcomeId;
    /**
     * 目标年度
     */
    private Integer targetYear;
    /**
     * 指标ID集合
     */
    private List<Long> indicatorIds;
    /**
     * 保证倒退12个月的数据 每个id只能有两个
     */
    private Integer limitYear;
}

