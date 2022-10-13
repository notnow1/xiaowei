package net.qixiaowei.system.manage.service.impl.system;

import java.util.*;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.UserConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.vo.system.MetaVO;
import net.qixiaowei.system.manage.api.vo.system.RoleMenuTreeVO;
import net.qixiaowei.system.manage.api.vo.system.RouterVO;
import net.qixiaowei.system.manage.mapper.system.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.system.Menu;
import net.qixiaowei.system.manage.api.dto.system.MenuDTO;
import net.qixiaowei.system.manage.mapper.system.MenuMapper;
import net.qixiaowei.system.manage.service.system.IMenuService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import org.springframework.util.CollectionUtils;


/**
 * MenuService业务层处理
 *
 * @author hzk
 * @since 2022-10-07
 */
@Service
public class MenuServiceImpl implements IMenuService {
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;


    /**
     * 查询菜单表列表
     *
     * @param menuDTO 菜单表
     * @return 菜单表
     */
    @Override
    public List<MenuDTO> selectMenuList(MenuDTO menuDTO) {
        List<MenuDTO> menuList;
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);
        Long userId = SecurityUtils.getUserId();
        if (SecurityUtils.isAdmin()) {
            menuList = menuMapper.selectMenuList(menu);
        } else {
            menu.getParams().put("userId", userId);
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * 查询菜单表
     *
     * @param menuId 菜单表主键
     * @return 菜单表
     */
    @Override
    public MenuDTO selectMenuByMenuId(Long menuId) {
        return menuMapper.selectMenuByMenuId(menuId);
    }


    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<Tree<Long>> buildMenuTree(List<MenuDTO> menus) {
        //自定义属性名
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("menuId");
        treeNodeConfig.setNameKey("label");
        treeNodeConfig.setParentIdKey("parentMenuId");
        return TreeUtil.build(menus, Constants.TOP_PARENT_ID, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getMenuId());
            tree.setParentId(treeNode.getParentMenuId());
            tree.setName(treeNode.getMenuName());
        });
    }

    @Override
    public List<RouterVO> getRouters() {
        List<MenuDTO> menuList;
        Long userId = SecurityUtils.getUserId();
        if (SecurityUtils.isAdmin()) {
            menuList = menuMapper.selectMenuRouterAll();
        } else {
            menuList = menuMapper.selectMenuRouterByUserId(userId);
        }
        List<MenuDTO> childPerms = this.getChildPerms(menuList, 0);
        return this.buildMenuRouters(childPerms);
    }

    @Override
    public RoleMenuTreeVO roleMenuTreeSelect(Long roleId) {
        MenuDTO menuDTO = new MenuDTO();
        List<MenuDTO> menus = this.selectMenuList(menuDTO);
        RoleMenuTreeVO roleMenuTreeVO = new RoleMenuTreeVO();
        roleMenuTreeVO.setCheckedKeys(this.selectMenuListByRoleId(roleId));
        roleMenuTreeVO.setMenus(this.buildMenuTree(menus));
        return roleMenuTreeVO;
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return menuMapper.selectMenuListByRoleId(roleId);
    }

    /**
     * 新增菜单表
     *
     * @param menuDTO 菜单表
     * @return 结果
     */
    @Override
    public int insertMenu(MenuDTO menuDTO) {
        if (!this.checkMenuNameUnique(menuDTO)) {
            throw new ServiceException("新增菜单'" + menuDTO.getMenuName() + "'失败，菜单名称已存在");
        }
        if (Constants.FLAG_YES.equals(menuDTO.getExternalLinkFlag()) && !StringUtils.ishttp(menuDTO.getPath())) {
            throw new ServiceException("新增菜单'" + menuDTO.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);
        menu.setCreateBy(SecurityUtils.getUserId());
        menu.setCreateTime(DateUtils.getNowDate());
        menu.setUpdateTime(DateUtils.getNowDate());
        menu.setUpdateBy(SecurityUtils.getUserId());
        menu.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return menuMapper.insertMenu(menu);
    }

    /**
     * 修改菜单表
     *
     * @param menuDTO 菜单表
     * @return 结果
     */
    @Override
    public int updateMenu(MenuDTO menuDTO) {
        if (!this.checkMenuNameUnique(menuDTO)) {
            throw new ServiceException("修改菜单'" + menuDTO.getMenuName() + "'失败，菜单名称已存在");
        }
        if (Constants.FLAG_YES.equals(menuDTO.getExternalLinkFlag()) && !StringUtils.ishttp(menuDTO.getPath())) {
            throw new ServiceException("修改菜单'" + menuDTO.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        if (menuDTO.getMenuId().equals(menuDTO.getParentMenuId())) {
            throw new ServiceException("修改菜单'" + menuDTO.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);
        menu.setUpdateTime(DateUtils.getNowDate());
        menu.setUpdateBy(SecurityUtils.getUserId());
        return menuMapper.updateMenu(menu);
    }

    /**
     * 逻辑删除菜单表信息
     *
     * @param menuDTO 菜单表
     * @return 结果
     */
    @Override
    public int logicDeleteMenuByMenuId(MenuDTO menuDTO) {
        Long menuId = menuDTO.getMenuId();
        if (this.hasChildByMenuId(menuId)) {
            throw new ServiceException("存在子菜单,不允许删除");
        }
        if (this.checkMenuExistRole(menuId)) {
            throw new ServiceException("菜单已分配,不允许删除");
        }
        Menu menu = new Menu();
        menu.setMenuId(menuId);
        menu.setUpdateBy(SecurityUtils.getUserId());
        menu.setUpdateTime(DateUtils.getNowDate());
        return menuMapper.logicDeleteMenuByMenuId(menu);
    }


    @Override
    public boolean checkMenuNameUnique(MenuDTO menuDTO) {
        Long menuId = StringUtils.isNull(menuDTO.getMenuId()) ? -1L : menuDTO.getMenuId();
        MenuDTO info = menuMapper.checkMenuNameUnique(menuDTO.getMenuName(), menuDTO.getParentMenuId());
        return StringUtils.isNull(info) || menuId.equals(info.getMenuId());
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public boolean hasChildByMenuId(Long menuId) {
        int result = menuMapper.hasChildByMenuId(menuId);
        return result > 0;
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public boolean checkMenuExistRole(Long menuId) {
        int result = roleMenuMapper.checkMenuExistRole(menuId);
        return result > 0;
    }

    /**
     * 根据角色ID集合查询权限
     *
     * @param roleIds 角色ID集合
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByRoleIds(List<Long> roleIds) {
        List<String> perms = menuMapper.selectMenuPermsByRoleIds(roleIds);
        return new HashSet<>(perms);
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<MenuDTO> getChildPerms(List<MenuDTO> list, int parentId) {
        List<MenuDTO> returnList = new ArrayList<MenuDTO>();
        for (Iterator<MenuDTO> iterator = list.iterator(); iterator.hasNext(); ) {
            MenuDTO t = (MenuDTO) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentMenuId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<MenuDTO> list, MenuDTO t) {
        // 得到子节点列表
        List<MenuDTO> childList = getChildList(list, t);
        t.setChildren(childList);
        for (MenuDTO tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<MenuDTO> getChildList(List<MenuDTO> list, MenuDTO t) {
        List<MenuDTO> newList = new ArrayList<MenuDTO>();
        Iterator<MenuDTO> it = list.iterator();
        while (it.hasNext()) {
            MenuDTO n = (MenuDTO) it.next();
            if (n.getParentMenuId().longValue() == t.getMenuId().longValue()) {
                newList.add(n);
            }
        }
        return newList;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<MenuDTO> list, MenuDTO t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */

    public List<RouterVO> buildMenuRouters(List<MenuDTO> menus) {
        List<RouterVO> routers = new LinkedList<RouterVO>();
        for (MenuDTO menu : menus) {
            RouterVO router = new RouterVO();
            router.setHidden(Constants.FLAG_NO.equals(menu.getVisibleFlag()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon(), Constants.FLAG_NO.equals(menu.getCacheFlag()), menu.getPath()));
            router.setCloudProduct(StringUtils.isNotNull(menu.getProductPackageId()));
            List<MenuDTO> cMenus = menu.getChildren();
            if (StringUtils.isNotEmpty(cMenus)) {
                if (UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                    router.setAlwaysShow(true);
                    router.setRedirect("noRedirect");
                    router.setChildren(buildMenuRouters(cMenus));
                } else {
                    //菜单的菜单，往上提一级，跟父级平级
                    cMenus.forEach(childMenu -> {
                        RouterVO routerChild = new RouterVO();
                        routerChild.setHidden(Constants.FLAG_NO.equals(childMenu.getVisibleFlag()));
                        routerChild.setName(getRouteName(childMenu));
                        routerChild.setPath(getRouterPath(childMenu));
                        routerChild.setComponent(getComponent(childMenu));
                        routerChild.setQuery(childMenu.getQuery());
                        routerChild.setMeta(new MetaVO(childMenu.getMenuName(), childMenu.getIcon(), Constants.FLAG_NO.equals(childMenu.getCacheFlag()), childMenu.getPath()));
                        routerChild.setCloudProduct(StringUtils.isNotNull(childMenu.getProductPackageId()));
                        routers.add(routerChild);
                    });
                }
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVO> childrenList = new ArrayList<RouterVO>();
                RouterVO children = new RouterVO();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon(), Constants.FLAG_NO.equals(menu.getCacheFlag()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentMenuId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVO> childrenList = new ArrayList<RouterVO>();
                RouterVO children = new RouterVO();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(MenuDTO menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(MenuDTO menu) {
        return menu.getParentMenuId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && Constants.FLAG_NO.equals(menu.getExternalLinkFlag());
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(MenuDTO menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentMenuId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentMenuId().intValue() && Constants.FLAG_NO.equals(menu.getExternalLinkFlag())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(MenuDTO menu) {
        return Constants.FLAG_NO.equals(menu.getExternalLinkFlag()) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, "."},
                new String[]{"", "", "", "/"});
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(MenuDTO menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentMenuId().intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(MenuDTO menu) {
        return menu.getParentMenuId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

}

