package net.qixiaowei.operate.cloud.api.domain.employee;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
* 人力预算明细表
* @author TANGMICHI
* @since 2022-11-22
*/
@Data
@Accessors(chain = true)
public class EmployeeBudgetDetails extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  employeeBudgetDetailsId;
     /**
     * 人力预算ID
     */
     private  Long  employeeBudgetId;
     /**
     * 岗位职级
     */
     private  Integer  officialRank;
     /**
     * 上年期末人数
     */
     private  Integer  numberLastYear;
     /**
     * 本年新增人数
     */
     private  Integer  countAdjust;
     /**
     * 平均新增数
     */
     private BigDecimal averageAdjust;

}

