package net.qixiaowei.operate.cloud.api.domain.field;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 字段配置表
* @author hzk
* @since 2023-02-08
*/
@Data
@Accessors(chain = true)
public class FieldConfig extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  fieldConfigId;
     /**
     * 业务类型
     */
     private  Integer  businessType;
     /**
     * 字段名称
     */
     private  String  fieldName;
     /**
     * 字段标签
     */
     private  String  fieldLabel;
     /**
     * 字段类型
     */
     private  Integer  fieldType;

}

