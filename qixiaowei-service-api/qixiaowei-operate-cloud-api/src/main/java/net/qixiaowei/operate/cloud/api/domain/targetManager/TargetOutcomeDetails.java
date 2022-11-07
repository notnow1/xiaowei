package net.qixiaowei.operate.cloud.api.domain.targetManager;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 目标结果详情表
* @author TANGMICHI
* @since 2022-11-07
*/
@Data
@Accessors(chain = true)
public class TargetOutcomeDetails extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  targetOutcomeDetailsId;
     /**
     * 目标结果ID
     */
     private  Long  targetOutcomeId;
     /**
     * 指标ID
     */
     private  Long  indicatorId;
     /**
     * 累计实际值
     */
     private  BigDecimal  actualTotal;
     /**
     * 一月实际值
     */
     private  BigDecimal  actualJanuary;
     /**
     * 二月实际值
     */
     private BigDecimal actualFebruary;
     /**
     * 三月实际值
     */
     private  BigDecimal  actualMarch;
     /**
     * 四月实际值
     */
     private  BigDecimal  actualApril;
     /**
     * 五月实际值
     */
     private  BigDecimal  actualMay;
     /**
     * 六月实际值
     */
     private  BigDecimal  actualJune;
     /**
     * 七月实际值
     */
     private  BigDecimal  actualJuly;
     /**
     * 八月实际值
     */
     private  BigDecimal  actualAugust;
     /**
     * 九月实际值
     */
     private  BigDecimal  actualSeptember;
     /**
     * 十月实际值
     */
     private  BigDecimal  actualOctober;
     /**
     * 十一月实际值
     */
     private  BigDecimal  actualNovember;
     /**
     * 十二月实际值
     */
     private  BigDecimal  actualDecember;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

