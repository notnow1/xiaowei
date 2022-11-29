package net.qixiaowei.operate.cloud.api.domain.targetManager;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;
import java.util.List;

/**
* 目标制定
* @author Graves
* @since 2022-10-27
*/
@Data
@Accessors(chain = true)
public class TargetSetting extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  targetSettingId;
     /**
     * 目标制定类型:0自定义;1销售订单;2销售收入;3销售回款
     */
     private  Integer  targetSettingType;
     /**
     * 指标ID
     */
     private  Long  indicatorId;
     /**
      * 指标ID集合
      */
     private List<Long> indicatorIds;
     /**
     * 目标年度
     */
     private  Integer  targetYear;
     /**
     * 百分比(%)
     */
     private  BigDecimal  percentage;
     /**
     * 挑战值
     */
     private  BigDecimal  challengeValue;
     /**
     * 目标值
     */
     private  BigDecimal  targetValue;
     /**
     * 保底值
     */
     private BigDecimal guaranteedValue;
     /**
     * 排序
     */
     private  Integer  sort;

}

