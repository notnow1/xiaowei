package net.qixiaowei.operate.cloud.api.domain.targetManager;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 目标制定订单表
* @author Graves
* @since 2022-10-27
*/
@Data
@Accessors(chain = true)
public class TargetSettingOrder extends BaseEntity {

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
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

