package net.qixiaowei.operate.cloud.api.domain.targetManager;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 目标分解详情周期快照表
* @author TANGMICHI
* @since 2022-10-31
*/
@Data
@Accessors(chain = true)
public class DetailCyclesSnapshot extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  detailCyclesSnapshotId;
     /**
     * 目标分解详情快照ID
     */
     private  Long  decomposeDetailsSnapshotId;
     /**
     * 周期数(顺序递增)
     */
     private  Integer  cycleNumber;
     /**
     * 周期目标值
     */
     private  BigDecimal  cycleTarget;
     /**
     * 周期预测值
     */
     private  BigDecimal  cycleForecast;
     /**
     * 周期实际值
     */
     private  BigDecimal  cycleActual;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

