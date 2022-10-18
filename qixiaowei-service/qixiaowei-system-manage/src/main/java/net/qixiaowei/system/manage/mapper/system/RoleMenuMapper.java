package net.qixiaowei.system.manage.mapper.system;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.system.RoleMenu;
import net.qixiaowei.system.manage.api.dto.system.RoleMenuDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* RoleMenuMapper接口
* @author hzk
* @since 2022-10-07
*/
public interface RoleMenuMapper{

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int checkMenuExistRole(Long menuId);
    /**
    * 查询角色菜单表
    *
    * @param roleMenuId 角色菜单表主键
    * @return 角色菜单表
    */
    RoleMenuDTO selectRoleMenuByRoleMenuId(@Param("roleMenuId")Long roleMenuId);

    /**
     * 根据角色ID查询角色菜单表列表
     *
     * @param roleId 角色ID
     * @return 角色菜单表集合
     */
    List<RoleMenuDTO> selectRoleMenuListByRoleId(@Param("roleId")Long roleId);

    /**
     * 根据角色ID集合查询角色菜单ID集合
     *
     * @param roleIds 角色ID
     * @return 角色菜单ID集合
     */
    List<Long> selectRoleMenuIdsByRoleIds(@Param("roleIds")List<Long> roleIds);


    /**
    * 查询角色菜单表列表
    *
    * @param roleMenu 角色菜单表
    * @return 角色菜单表集合
    */
    List<RoleMenuDTO> selectRoleMenuList(@Param("roleMenu")RoleMenu roleMenu);

    /**
    * 新增角色菜单表
    *
    * @param roleMenu 角色菜单表
    * @return 结果
    */
    int insertRoleMenu(@Param("roleMenu")RoleMenu roleMenu);

    /**
    * 修改角色菜单表
    *
    * @param roleMenu 角色菜单表
    * @return 结果
    */
    int updateRoleMenu(@Param("roleMenu")RoleMenu roleMenu);

    /**
    * 批量修改角色菜单表
    *
    * @param roleMenuList 角色菜单表
    * @return 结果
    */
    int updateRoleMenus(@Param("roleMenuList")List<RoleMenu> roleMenuList);
    /**
    * 逻辑删除角色菜单表
    *
    * @param roleMenu
    * @return 结果
    */
    int logicDeleteRoleMenuByRoleMenuId(@Param("roleMenu")RoleMenu roleMenu,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
    * 逻辑批量删除角色菜单表
    *
    * @param roleMenuIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteRoleMenuByRoleMenuIds(@Param("roleMenuIds")List<Long> roleMenuIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除角色菜单表
    *
    * @param roleMenuId 角色菜单表主键
    * @return 结果
    */
    int deleteRoleMenuByRoleMenuId(@Param("roleMenuId")Long roleMenuId);

    /**
    * 物理批量删除角色菜单表
    *
    * @param roleMenuIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteRoleMenuByRoleMenuIds(@Param("roleMenuIds")List<Long> roleMenuIds);

    /**
    * 批量新增角色菜单表
    *
    * @param RoleMenus 角色菜单表列表
    * @return 结果
    */
    int batchRoleMenu(@Param("roleMenus")List<RoleMenu> RoleMenus);
}
