package net.qixiaowei.system.manage.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.system.MenuDTO;
import net.qixiaowei.system.manage.service.system.IMenuService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author hzk
 * @since 2022-10-07
 */
@RestController
@RequestMapping("menu")
public class MenuController extends BaseController {


    @Autowired
    private IMenuService menuService;


    /**
     * 查询菜单表列表
     */
    @RequiresPermissions("system:manage:menu:list")
    @GetMapping("/list")
    public AjaxResult list(MenuDTO menuDTO) {
        List<MenuDTO> list = menuService.selectMenuList(menuDTO);
        return AjaxResult.success(list);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @RequiresPermissions("system:manage:menu:info")
    @GetMapping(value = "/info/{menuId}")
    public AjaxResult info(@PathVariable Long menuId) {
        return AjaxResult.success(menuService.selectMenuByMenuId(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeSelect")
    public AjaxResult treeSelect(MenuDTO menuDTO) {
        List<MenuDTO> menus = menuService.selectMenuList(menuDTO);
        return AjaxResult.success(menuService.buildMenuTree(menus));
    }

    /**
     * 新增菜单表
     */
    @RequiresPermissions("system:manage:menu:add")
    @Log(title = "新增菜单表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody MenuDTO menuDTO) {
        return toAjax(menuService.insertMenu(menuDTO));
    }

    /**
     * 修改菜单表
     */
    @RequiresPermissions("system:manage:menu:edit")
    @Log(title = "修改菜单表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody MenuDTO menuDTO) {
        return toAjax(menuService.updateMenu(menuDTO));
    }

    /**
     * 逻辑删除菜单表
     */
    @RequiresPermissions("system:manage:menu:remove")
    @Log(title = "删除菜单表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody MenuDTO menuDTO) {
        return toAjax(menuService.logicDeleteMenuByMenuId(menuDTO));
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    public AjaxResult getRouters() {
        return AjaxResult.success(menuService.getRouters());
    }

}
