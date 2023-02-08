package net.qixiaowei.system.manage.api.domain.field;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 字段列表配置表
* @author hzk
* @since 2023-02-08
*/
@Data
@Accessors(chain = true)
public class FieldListConfig extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  fieldListConfigId;
     /**
     * 字段配置ID
     */
     private  Long  fieldConfigId;
     /**
     * 用户ID
     */
     private  Long  userId;
     /**
     * 字段宽度
     */
     private  Integer  fieldWidth;
     /**
     * 排序
     */
     private  Integer  sort;
     /**
     * 显示标记:0否;1是
     */
     private  Integer  showFlag;
     /**
     * 固定标记:0否;1是
     */
     private  Integer  fixationFlag;
     /**
     * 强制显示:0否;1是
     */
     private  Integer  showForce;
     /**
     * 强制固定:0否;1是
     */
     private  Integer  fixationForce;

}

