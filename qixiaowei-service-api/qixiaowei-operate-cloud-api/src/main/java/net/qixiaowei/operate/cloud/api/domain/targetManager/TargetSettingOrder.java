package net.qixiaowei.operate.cloud.api.domain.targetManager;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.math.BigDecimal;

/**
* 目标制定订单表
* @author Graves
* @since 2022-10-27
*/
@Data
@Accessors(chain = true)
public class TargetSettingOrder extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  targetSettingOrderId;
     /**
     * 目标制定ID
     */
     private  Long  targetSettingId;
     /**
     * 历史年度
     */
     private  Integer  historyYear;
     /**
     * 历史年度实际值
     */
     private BigDecimal historyActual;


}

