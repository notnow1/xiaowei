package net.qixiaowei.system.manage.api.dto.system;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import net.qixiaowei.integration.common.domain.dto.BaseDTO;
import net.qixiaowei.integration.common.enums.system.RoleCode;
import net.qixiaowei.integration.common.enums.system.RoleType;

/**
 * 角色表
 *
 * @author hzk
 * @since 2022-10-07
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class RoleDTO extends BaseDTO {

    public RoleDTO(Long roleId) {
        this.roleId = roleId;
    }

    //查询检验
    public interface QueryRoleDTO extends Default {

    }

    //新增检验
    public interface AddRoleDTO extends Default {

    }

    //新增检验
    public interface DeleteRoleDTO extends Default {

    }

    //修改检验
    public interface UpdateRoleDTO extends Default {

    }

    /**
     * ID
     */
    @NotNull(message = "角色ID不能为空", groups = {RoleDTO.UpdateRoleDTO.class, RoleDTO.DeleteRoleDTO.class})
    private Long roleId;
    /**
     * 角色类型:0内置角色;1自定义角色
     */
    private Integer roleType;
    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空", groups = {RoleDTO.AddRoleDTO.class})
    private String roleCode;
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空", groups = {RoleDTO.AddRoleDTO.class})
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
     * 备注
     */
    private String remark;
    /**
     * 状态:0失效;1生效
     */
    private Integer status;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 菜单组
     */
    private Set<Long> menuIds;
    /**
     * 角色菜单权限
     */
    private Set<String> permissions;

    public boolean isAdmin() {
        return isAdmin(this.roleType, this.remark);
    }

    public static boolean isAdmin(Integer roleType, String remark) {
        return RoleType.BUILT_IN.getCode().equals(roleType) && RoleCode.TENANT_ADMIN.getRemark().equals(remark);
    }

}

