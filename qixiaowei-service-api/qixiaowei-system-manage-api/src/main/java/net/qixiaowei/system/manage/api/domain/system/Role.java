package net.qixiaowei.system.manage.api.domain.system;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;


/**
 * 角色表
 *
 * @author hzk
 * @since 2022-10-07
 */
@Data
@Accessors(chain = true)
public class Role extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long roleId;
    /**
     * 角色类型:0内置角色;1自定义角色
     */
    private Integer roleType;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 数据范围:1全公司;2本部门及下属部门;3本部门;4本人及下属;5本人
     */
    private Integer dataScope;
    /**
     * 分配的产品包
     */
    private String productPackage;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态:0失效;1生效
     */
    private Integer status;

}

