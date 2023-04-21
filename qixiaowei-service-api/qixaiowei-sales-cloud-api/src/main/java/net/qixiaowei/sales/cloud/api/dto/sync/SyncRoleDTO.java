package net.qixiaowei.sales.cloud.api.dto.sync;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hzk
 * 销售云同步角色
 */
@Data
public class SyncRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色类型-0、自定义角色1、管理角色 2、客户管理角色 3、人事角色 4、财务角色 5、项目角色 8、项目自定义角色
     */
    private Integer roleType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 数据权限 1、本人，2、本人及下属，3、本部门，4、本部门及下属部门，5、全部
     */
    private Integer dataType;
    /**
     * 赋予的菜单ID集合
     */
    private Set<Long> menuIds = new HashSet<>();

}
