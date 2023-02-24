package net.qixiaowei.system.manage.api.dto.system;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 用户角色表
* @author hzk
* @since 2022-10-07
*/
@Data
@Accessors(chain = true)
public class UserRoleDTO {

    //查询检验
    public interface QueryUserRoleDTO extends Default{

    }
    //新增检验
    public interface AddUserRoleDTO extends Default{

    }

    //新增检验
    public interface DeleteUserRoleDTO extends Default{

    }
    //修改检验
    public interface UpdateUserRoleDTO extends Default{

    }
    /**
    * ID
    */
    private  Long userRoleId;
    /**
    * 用户ID
    */
    private  Long userId;
    /**
    * 角色ID
    */
    private  Long roleId;
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
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private  Date  updateTime;

}

