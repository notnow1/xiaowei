package net.qixiaowei.operate.cloud.api.domain.salary;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 薪酬规划表
* @author TANGMICHI
* @since 2022-11-18
*/
@Data
@Accessors(chain = true)
public class EmolumentPlan extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  emolumentPlanId;
     /**
     * 预算年度
     */
     private  Integer  planYear;
     /**
     * 预算年前一年销售收入
     */
     private BigDecimal revenueBeforeOne;
     /**
     * 预算年销售收入
     */
     private  BigDecimal  revenue;
     /**
     * 预算年后一年销售收入
     */
     private  BigDecimal  revenueAfterOne;
     /**
     * 预算年后二年销售收入
     */
     private  BigDecimal  revenueAfterTwo;
     /**
     * 预算年前一年E/R值(%)
     */
     private  BigDecimal  erBeforeOne;
     /**
     * 预算年E/R值改进率(%)
     */
     private  BigDecimal  emolumentRevenueImprove;
     /**
     * 预算年后一年E/R值改进率(%)
     */
     private  BigDecimal  erImproveAfterOne;
     /**
     * 预算年后二年E/R值改进率(%)
     */
     private  BigDecimal  erImproveAfterTwo;
     /**
     * 预算年前一年总薪酬包
     */
     private  BigDecimal  emolumentPackageBeforeOne;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

