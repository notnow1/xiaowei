package net.qixiaowei.operate.cloud.api.domain.employee;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 人力预算表
* @author TANGMICHI
* @since 2022-11-18
*/
@Data
@Accessors(chain = true)
public class EmployeeBudget extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  employeeBudgetId;
     /**
     * 预算年度
     */
     private  Integer  budgetYear;
     /**
     * 预算部门ID
     */
     private  Long  departmentId;
     /**
     * 职级体系ID
     */
     private  Long  officialRankSystemId;
     /**
     * 预算周期:1季度;2月度
     */
     private  Integer  budgetCycle;
     /**
     * 上年期末人数
     */
     private  Integer  amountLastYear;
     /**
     * 本年新增人数
     */
     private  Integer  amountAdjust;
     /**
     * 平均新增数
     */
     private BigDecimal amountAverageAdjust;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

