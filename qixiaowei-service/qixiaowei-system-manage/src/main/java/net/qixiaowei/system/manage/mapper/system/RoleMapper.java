package net.qixiaowei.system.manage.mapper.system;

import java.util.List;

import net.qixiaowei.system.manage.api.domain.system.Role;
import net.qixiaowei.system.manage.api.dto.system.RoleDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * RoleMapper接口
 *
 * @author hzk
 * @since 2022-10-07
 */
public interface RoleMapper {

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleDTO> selectRolesByUserId(Long userId);


    /**
     * 查询角色表
     *
     * @param roleId 角色表主键
     * @return 角色表
     */
    RoleDTO selectRoleByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色ID集合查询角色
     *
     * @param roleIds 角色ID集合
     * @return 角色列表
     */
    List<RoleDTO> selectRolesByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 查询角色表
     *
     * @param roleCode 角色编码
     * @return 角色表
     */
    RoleDTO selectRoleByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 查询角色表列表
     *
     * @param role 角色表
     * @return 角色表集合
     */
    List<RoleDTO> selectRoleList(@Param("role") Role role);

    /**
     * 新增角色表
     *
     * @param role 角色表
     * @return 结果
     */
    int insertRole(@Param("role") Role role);

    /**
     * 初始化租户角色表
     *
     * @param role 角色表
     * @return 结果
     */
    int initTenantRole(@Param("role") Role role);

    /**
     * 修改角色表
     *
     * @param role 角色表
     * @return 结果
     */
    int updateRole(@Param("role") Role role);

    /**
     * 批量修改角色表
     *
     * @param roleList 角色表
     * @return 结果
     */
    int updateRoles(@Param("roleList") List<Role> roleList);

    /**
     * 逻辑删除角色表
     *
     * @param role
     * @return 结果
     */
    int logicDeleteRoleByRoleId(@Param("role") Role role, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 逻辑批量删除角色表
     *
     * @param roleIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteRoleByRoleIds(@Param("roleIds") List<Long> roleIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除角色表
     *
     * @param roleId 角色表主键
     * @return 结果
     */
    int deleteRoleByRoleId(@Param("roleId") Long roleId);

    /**
     * 物理批量删除角色表
     *
     * @param roleIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteRoleByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 批量新增角色表
     *
     * @param Roles 角色表列表
     * @return 结果
     */
    int batchRole(@Param("roles") List<Role> Roles);
}
