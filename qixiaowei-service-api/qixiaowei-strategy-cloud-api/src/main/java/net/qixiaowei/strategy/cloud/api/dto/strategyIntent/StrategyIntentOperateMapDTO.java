package net.qixiaowei.strategy.cloud.api.dto.strategyIntent;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Map;

/**
* 战略意图经营嵌套子表
* @author TANGMICHI
* @since 2023-02-23
*/
@Data
@Accessors(chain = true)
public class StrategyIntentOperateMapDTO {

    /**
     * ID
     */
    private  Long strategyIntentOperateId;

    /**
     * 年度指标对应值集合
     */
    private Map<Integer, BigDecimal> yearValues;
}

