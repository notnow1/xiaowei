package net.qixiaowei.operate.cloud.api.domain.salary;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 奖金预算表
* @author TANGMICHI
* @since 2022-11-26
*/
@Data
@Accessors(chain = true)
public class BonusBudget extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  bonusBudgetId;
     /**
     * 预算年度
     */
     private  Integer  budgetYear;
     /**
     * 总奖金包预算
     */
     private BigDecimal amountBonusBudget;
     /**
     * 预算年度前一年的总奖金包
     */
     private  BigDecimal  bonusBeforeOne;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

