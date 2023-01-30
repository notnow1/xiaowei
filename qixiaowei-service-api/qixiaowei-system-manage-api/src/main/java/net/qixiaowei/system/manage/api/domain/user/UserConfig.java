package net.qixiaowei.system.manage.api.domain.user;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 用户配置表
* @author hzk
* @since 2023-01-30
*/
@Data
@Accessors(chain = true)
public class UserConfig extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  userConfigId;
     /**
     * 用户ID
     */
     private  Long  userId;
     /**
     * 用户配置类型
     */
     private  Integer  userConfigType;
     /**
     * 用户配置值
     */
     private  String  userConfigValue;
     /**
     * 状态:0关闭;1启用
     */
     private  Integer  status;

}

