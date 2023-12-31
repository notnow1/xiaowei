package net.qixiaowei.operate.cloud.api.domain.bonus;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 部门奖金预算项目表
* @author TANGMICHI
* @since 2022-12-01
*/
@Data
@Accessors(chain = true)
public class DeptBonusBudgetItems extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  deptBonusBudgetItemsId;
     /**
     * 部门奖金包预算ID
     */
     private  Long  deptBonusBudgetId;
     /**
     * 部门奖金预算明细ID
     */
     private  Long  deptBonusBudgetDetailsId;
     /**
     * 工资项ID
     */
     private  Long  salaryItemId;
     /**
     * 奖金占比
     */
     private BigDecimal bonusPercentage;
     /**
     * 排序
     */
     private  Integer  sort;


}

