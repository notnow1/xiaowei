package net.qixiaowei.operate.cloud.api.domain.bonus;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 部门奖金预算明细表
* @author TANGMICHI
* @since 2022-11-30
*/
@Data
@Accessors(chain = true)
public class DeptBonusBudgetDetails extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  deptBonusBudgetDetailsId;
     /**
     * 部门奖金包预算ID
     */
     private  Long  deptBonusBudgetId;
     /**
     * 部门ID
     */
     private  Long  departmentId;
     /**
     * 部门奖金占比
     */
     private BigDecimal deptBonusPercentage;
     /**
     * 部门重要性系数
     */
     private  BigDecimal  departmentImportanceFactor;


}

