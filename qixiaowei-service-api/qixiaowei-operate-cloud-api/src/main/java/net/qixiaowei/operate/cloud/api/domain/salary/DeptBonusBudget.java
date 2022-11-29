package net.qixiaowei.operate.cloud.api.domain.salary;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 部门奖金包预算表
* @author TANGMICHI
* @since 2022-11-29
*/
@Data
@Accessors(chain = true)
public class DeptBonusBudget extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  deptBonusBudgetId;
     /**
     * 预算年度
     */
     private  Integer  budgetYear;
     /**
     * 战略奖比例
     */
     private BigDecimal strategyAwardPercentage;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

