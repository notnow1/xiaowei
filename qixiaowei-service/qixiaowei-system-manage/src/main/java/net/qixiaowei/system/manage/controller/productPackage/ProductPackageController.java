package net.qixiaowei.system.manage.controller.productPackage;

import java.util.List;

import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.dto.productPackage.ProductPackageDTO;
import net.qixiaowei.system.manage.service.productPackage.IProductPackageService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author TANGMICHI
 * @since 2022-10-09
 */
@RestController
@RequestMapping("productPackage")
public class ProductPackageController extends BaseController {


    @Autowired
    private IProductPackageService productPackageService;

    /**
     * 新增产品包
     */
    @RequiresPermissions("system:manage:productPackage:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody ProductPackageDTO productPackageDTO) {
        return toAjax(productPackageService.insertProductPackage(productPackageDTO));
    }

    /**
     * 修改产品包
     */
    @RequiresPermissions("system:manage:productPackage:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody ProductPackageDTO productPackageDTO) {
        return toAjax(productPackageService.updateProductPackage(productPackageDTO));
    }

    /**
     * 查询产品包详情
     */
    @RequiresPermissions(value = {"system:manage:productPackage:info", "system:manage:productPackage:edit"}, logical = Logical.OR)
    @GetMapping("/info/{productPackageId}")
    public AjaxResult info(@PathVariable Long productPackageId) {
        ProductPackageDTO productPackageDTO = productPackageService.selectProductPackageByProductPackageId(productPackageId);
        return AjaxResult.success(productPackageDTO);
    }

    /**
     * 分页查询产品包列表
     */
    @RequiresPermissions("system:manage:productPackage:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(ProductPackageDTO productPackageDTO) {
        startPage();
        List<ProductPackageDTO> list = productPackageService.selectProductPackageList(productPackageDTO);
        return getDataTable(list);
    }

    /**
     * 逻辑删除产品包
     */
    @RequiresPermissions("system:manage:productPackage:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody ProductPackageDTO productPackageDTO) {
        return toAjax(productPackageService.logicDeleteProductPackageByProductPackageId(productPackageDTO));
    }

    /**
     * 批量修改产品包
     */
    @RequiresPermissions("system:manage:productPackage:edits")
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<ProductPackageDTO> productPackageDtos) {
        return toAjax(productPackageService.updateProductPackages(productPackageDtos));
    }

    /**
     * 查询所有产品包列表
     */
    @GetMapping("/list")
    public AjaxResult list() {
        return AjaxResult.success(productPackageService.selectProductPackageAll());
    }

}
