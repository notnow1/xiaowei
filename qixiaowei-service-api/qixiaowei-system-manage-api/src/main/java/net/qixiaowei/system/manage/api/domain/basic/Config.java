package net.qixiaowei.system.manage.api.domain.basic;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 配置表
* @author Graves
* @since 2022-10-18
*/
@Data
@Accessors(chain = true)
public class Config extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  configId;
     /**
     * 父级配置ID
     */
     private  Long  parentConfigId;
     /**
     * 节点路径code,用英文.做连接
     */
     private  String  pathCode;
     /**
     * 配置编码
     */
     private  String  configCode;
     /**
     * 配置值
     */
     private  String  configValue;
     /**
     * 状态:0失效;1生效
     */
     private  Integer  status;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

