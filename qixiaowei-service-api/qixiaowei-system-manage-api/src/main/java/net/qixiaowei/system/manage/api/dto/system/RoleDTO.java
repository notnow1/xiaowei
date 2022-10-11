package net.qixiaowei.system.manage.api.dto.system;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 角色表
* @author hzk
* @since 2022-10-07
*/
@Data
@Accessors(chain = true)
public class RoleDTO {

    //查询检验
    public interface QueryRoleDTO extends Default{

    }
    //新增检验
    public interface AddRoleDTO extends Default{

    }

    //新增检验
    public interface DeleteRoleDTO extends Default{

    }
    //修改检验
    public interface UpdateRoleDTO extends Default{

    }
    /**
    * ID
    */
    private  Long roleId;
    /**
    * 角色编码
    */
    private  String roleCode;
    /**
    * 角色名称
    */
    private  String roleName;
    /**
    * 数据范围:1全公司;2本部门及下属部门;3本部门;4本人及下属;5本人
    */
    private  Integer dataScope;
    /**
    * 分配的产品包
    */
    private  String productPackage;
    /**
    * 排序
    */
    private  Integer sort;
    /**
    * 备注
    */
    private  String remark;
    /**
    * 状态:0失效;1生效
    */
    private  Integer status;
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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

