package net.qixiaowei.system.manage.mapper.system;

import java.util.List;

import net.qixiaowei.system.manage.api.domain.system.Menu;
import net.qixiaowei.system.manage.api.dto.system.MenuDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Set;


/**
 * MenuMapper接口
 *
 * @author hzk
 * @since 2022-10-07
 */
public interface MenuMapper {
    /**
     * 根据角色ID集合查询权限
     *
     * @param roleIds 角色ID集合
     * @return 权限列表
     */
    List<String> selectMenuPermsByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 查询菜单表
     *
     * @param menuId 菜单表主键
     * @return 菜单表
     */
    MenuDTO selectMenuByMenuId(@Param("menuId") Long menuId);

    /**
     * 查询菜单表列表
     *
     * @param menu 菜单表
     * @return 菜单表集合
     */
    List<MenuDTO> selectMenuList(@Param("menu") Menu menu);

    /**
     * 查询所有菜单路由列表
     *
     * @return 菜单表集合
     */
    List<MenuDTO> selectMenuRouterAll();

    /**
     * 查询所有菜单ID集合
     *
     * @return 菜单表集合
     */
    Set<Long> selectMenuIdsAll();

    /**
     * 查询所有菜单集合
     *
     * @return 菜单表集合
     */
    List<MenuDTO> selectMenuAll();

    /**
     * 查询用户菜单路由列表
     *
     * @return 菜单表集合
     */
    List<MenuDTO> selectMenuRouterByUserId(Long userId);


    /**
     * 校验菜单名称是否唯一
     *
     * @param menuName     菜单名称
     * @param parentMenuId 父菜单ID
     * @return 结果
     */
    MenuDTO checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentMenuId") Long parentMenuId);


    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    List<MenuDTO> selectMenuListByUserId(Menu menu);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int hasChildByMenuId(Long menuId);


    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    Set<Long> selectMenuListByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色ID集合查询菜单树信息
     *
     * @param roleIds 角色ID集合
     * @return 选中菜单列表
     */
    Set<Long> selectMenuListByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据租户合同ID查询菜单树信息
     *
     * @param tenantContractId 租户合同ID
     * @return 选中菜单列表
     */
    Set<Long> selectMenuListByTenantContractId(@Param("tenantContractId") Long tenantContractId);


    /**
     * 新增菜单表
     *
     * @param menu 菜单表
     * @return 结果
     */
    int insertMenu(@Param("menu") Menu menu);

    /**
     * 修改菜单表
     *
     * @param menu 菜单表
     * @return 结果
     */
    int updateMenu(@Param("menu") Menu menu);

    /**
     * 批量修改菜单表
     *
     * @param menuList 菜单表
     * @return 结果
     */
    int updateMenus(@Param("menuList") List<Menu> menuList);

    /**
     * 逻辑删除菜单表
     *
     * @param menu
     * @return 结果
     */
    int logicDeleteMenuByMenuId(@Param("menu") Menu menu);

    /**
     * 逻辑批量删除菜单表
     *
     * @param menuIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteMenuByMenuIds(@Param("menuIds") List<Long> menuIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除菜单表
     *
     * @param menuId 菜单表主键
     * @return 结果
     */
    int deleteMenuByMenuId(@Param("menuId") Long menuId);

    /**
     * 物理批量删除菜单表
     *
     * @param menuIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteMenuByMenuIds(@Param("menuIds") List<Long> menuIds);

    /**
     * 批量新增菜单表
     *
     * @param Menus 菜单表列表
     * @return 结果
     */
    int batchMenu(@Param("menus") List<Menu> Menus);
}
