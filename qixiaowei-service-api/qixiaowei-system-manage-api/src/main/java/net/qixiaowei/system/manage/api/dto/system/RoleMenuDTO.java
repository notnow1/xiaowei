package net.qixiaowei.system.manage.api.dto.system;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 角色菜单表
* @author hzk
* @since 2022-10-07
*/
@Data
@Accessors(chain = true)
public class RoleMenuDTO {

    //查询检验
    public interface QueryRoleMenuDTO extends Default{

    }
    //新增检验
    public interface AddRoleMenuDTO extends Default{

    }

    //新增检验
    public interface DeleteRoleMenuDTO extends Default{

    }
    //修改检验
    public interface UpdateRoleMenuDTO extends Default{

    }
    /**
    * ID
    */
    private  Long roleMenuId;
    /**
    * 角色ID
    */
    private  Long roleId;
    /**
    * 菜单ID
    */
    private  Long menuId;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 创建人
    */
    private  Long createBy;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

