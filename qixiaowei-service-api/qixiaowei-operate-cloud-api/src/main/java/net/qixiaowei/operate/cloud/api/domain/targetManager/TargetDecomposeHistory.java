package net.qixiaowei.operate.cloud.api.domain.targetManager;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 目标分解历史版本表
* @author TANGMICHI
* @since 2022-10-31
*/
@Data
@Accessors(chain = true)
public class TargetDecomposeHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  targetDecomposeHistoryId;
     /**
     * 目标分解ID
     */
     private  Long  targetDecomposeId;
     /**
     * 版本号
     */
     private  String  version;
     /**
     * 预测周期
     */
     private  String  forecastCycle;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

