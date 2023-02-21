package net.qixiaowei.system.manage.service.system;

import java.util.List;
import java.util.Set;

import cn.hutool.core.lang.tree.Tree;
import net.qixiaowei.system.manage.api.dto.system.MenuDTO;
import net.qixiaowei.system.manage.api.vo.system.RoleMenuTreeVO;
import net.qixiaowei.system.manage.api.vo.system.RouterVO;


/**
 * MenuService接口
 *
 * @author hzk
 * @since 2022-10-07
 */
public interface IMenuService {

    /**
     * 查询菜单表列表
     *
     * @param menuDTO 菜单表
     * @param filterAdmin 过滤管理菜单
     * @return 菜单表集合
     */
    List<MenuDTO> selectMenuList(MenuDTO menuDTO, boolean filterAdmin);

    /**
     * 查询所有菜单ID集合
     *
     * @param filterAdmin 是否过滤管理菜单
     * @return 菜单表集合
     */
    Set<Long> selectMenuIdsAll(boolean filterAdmin);

    /**
     * 查询菜单表
     *
     * @param menuId 菜单表主键
     * @return 菜单表
     */
    MenuDTO selectMenuByMenuId(Long menuId);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    List<Tree<Long>> buildMenuTree(List<MenuDTO> menus);

    /**
     * 获取路由信息
     *
     * @return 下拉树结构列表
     */
    List<RouterVO> getRouters();

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    Set<Long> selectMenuListByRoleId(Long roleId);

    /**
     * 根据租户合同ID查询菜单树信息
     *
     * @param tenantContractId 角色ID
     * @return 选中菜单列表
     */
    Set<Long> selectMenuListByTenantContractId(Long tenantContractId);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menuDTO 菜单信息
     * @return 结果
     */
    boolean checkMenuNameUnique(MenuDTO menuDTO);

    /**
     * 逻辑删除菜单表信息
     *
     * @param menuDTO
     * @return 结果
     */
    int logicDeleteMenuByMenuId(MenuDTO menuDTO);

    /**
     * 根据角色ID集合查询权限
     *
     * @param roleIds 角色ID集合
     * @return 权限列表
     */
    Set<String> selectMenuPermsByRoleIds(List<Long> roleIds);

    /**
     * 新增菜单表
     *
     * @param menuDTO 菜单表
     * @return 结果
     */
    int insertMenu(MenuDTO menuDTO);

    /**
     * 修改菜单表
     *
     * @param menuDTO 菜单表
     * @return 结果
     */
    int updateMenu(MenuDTO menuDTO);


}
