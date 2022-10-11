package net.qixiaowei.system.manage.api.domain.system;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 角色菜单表
* @author hzk
* @since 2022-10-07
*/
@Data
@Accessors(chain = true)
public class RoleMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  roleMenuId;
     /**
     * 角色ID
     */
     private  Long  roleId;
     /**
     * 菜单ID
     */
     private  Long  menuId;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

