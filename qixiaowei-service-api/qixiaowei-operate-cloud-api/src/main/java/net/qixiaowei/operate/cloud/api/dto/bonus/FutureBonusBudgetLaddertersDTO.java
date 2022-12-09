package net.qixiaowei.operate.cloud.api.dto.bonus;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;

/**
* 未来三年奖金趋势
* @author TANGMICHI
* @since 2022-11-26
*/
@Data
@Accessors(chain = true)
public class FutureBonusBudgetLaddertersDTO {

    /**
     * 预算年度
     */
    private  Integer budgetYear;

    /**
     * 奖金综合增长率
     */
    private  BigDecimal bonusCompositeRate;
    /**
     * 总奖金包预算
     */
    private BigDecimal amountBonusBudget;
}

