package net.qixiaowei.operate.cloud.api.domain.targetManager;


import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 目标结果表
 *
 * @author TANGMICHI
 * @since 2022-11-07
 */
@Data
@Accessors(chain = true)
public class TargetOutcome extends BaseEntity {

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
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;

}

