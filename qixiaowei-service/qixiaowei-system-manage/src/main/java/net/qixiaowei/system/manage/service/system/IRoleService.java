package net.qixiaowei.system.manage.service.system;

import java.util.List;

import net.qixiaowei.system.manage.api.dto.system.RoleDTO;


/**
* RoleService接口
* @author hzk
* @since 2022-10-07
*/
public interface IRoleService{

    /**
     * 根据用户ID查询角色列表
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
    RoleDTO selectRoleByRoleId(Long roleId);

    /**
    * 查询角色表列表
    *
    * @param roleDTO 角色表
    * @return 角色表集合
    */
    List<RoleDTO> selectRoleList(RoleDTO roleDTO);

    /**
    * 新增角色表
    *
    * @param roleDTO 角色表
    * @return 结果
    */
    int insertRole(RoleDTO roleDTO);

    /**
    * 修改角色表
    *
    * @param roleDTO 角色表
    * @return 结果
    */
    int updateRole(RoleDTO roleDTO);

    /**
    * 逻辑批量删除角色表
    *
    * @param RoleDtos 需要删除的角色表集合
    * @return 结果
    */
    int logicDeleteRoleByRoleIds(List<RoleDTO> RoleDtos);

    /**
    * 逻辑删除角色表信息
    *
    * @param roleDTO
    * @return 结果
    */
    int logicDeleteRoleByRoleId(RoleDTO roleDTO);
    /**
    * 逻辑批量删除角色表
    *
    * @param RoleDtos 需要删除的角色表集合
    * @return 结果
    */
    int deleteRoleByRoleIds(List<RoleDTO> RoleDtos);

    /**
    * 逻辑删除角色表信息
    *
    * @param roleDTO
    * @return 结果
    */
    int deleteRoleByRoleId(RoleDTO roleDTO);


    /**
    * 删除角色表信息
    *
    * @param roleId 角色表主键
    * @return 结果
    */
    int deleteRoleByRoleId(Long roleId);
}