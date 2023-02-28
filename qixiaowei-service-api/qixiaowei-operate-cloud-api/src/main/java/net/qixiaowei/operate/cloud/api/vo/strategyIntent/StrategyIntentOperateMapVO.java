package net.qixiaowei.operate.cloud.api.vo.strategyIntent;

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
public class StrategyIntentOperateMapVO {

    /**
     * ID
     */
    private  Long strategyIntentOperateId;
    /**
     * 经营年度
     */
    private  Integer operateYear;
    /**
     * 经营值
     */
    private  BigDecimal operateValue;
}

