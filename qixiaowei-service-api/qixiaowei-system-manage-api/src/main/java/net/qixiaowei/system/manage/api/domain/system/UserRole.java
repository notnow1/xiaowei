package net.qixiaowei.system.manage.api.domain.system;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 用户角色表
* @author hzk
* @since 2022-10-07
*/
@Data
@Accessors(chain = true)
public class UserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  userRoleId;
     /**
     * 用户ID
     */
     private  Long  userId;
     /**
     * 角色ID
     */
     private  Long  roleId;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

