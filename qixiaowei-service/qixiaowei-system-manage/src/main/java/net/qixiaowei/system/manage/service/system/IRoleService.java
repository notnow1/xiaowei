package net.qixiaowei.system.manage.service.system;

import java.util.List;

import net.qixiaowei.system.manage.api.dto.system.RoleAuthUsersDTO;
import net.qixiaowei.system.manage.api.dto.system.RoleDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;


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
     * 生成角色编码
     *
     * @return 角色编码
     */
    String generateRoleCode();

    /**
    * 新增角色表
    *
    * @param roleDTO 角色表
    * @return 结果
    */
    RoleDTO insertRole(RoleDTO roleDTO);

    /**
    * 修改角色表
    *
    * @param roleDTO 角色表
    * @return 结果
    */
    int updateRole(RoleDTO roleDTO);

    /**
     * 角色授权用户
     */
    void authUsers(RoleAuthUsersDTO roleAuthUsersDTO);

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param userDTO 用户信息
     * @return 用户信息集合信息
     */
    List<UserDTO> selectAllocatedList(UserDTO userDTO);

    /**
    * 逻辑批量删除角色表
    *
    * @param roleIds 需要删除的角色表集合
    * @return 结果
    */
    int logicDeleteRoleByRoleIds( List<Long> roleIds);

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
