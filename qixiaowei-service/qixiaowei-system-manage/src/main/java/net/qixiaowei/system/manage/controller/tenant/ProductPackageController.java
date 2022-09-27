package net.qixiaowei.system.manage.controller.tenant;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.tenant.ProductPackageDTO;
import net.qixiaowei.system.manage.service.tenant.IProductPackageService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-09-27
*/
@RestController
@RequestMapping("productPackage")
public class ProductPackageController extends BaseController
{


    @Autowired
    private IProductPackageService productPackageService;

    /**
    * 分页查询产品包列表
    */
    @RequiresPermissions("system:manage:productPackage:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(ProductPackageDTO productPackageDTO){
    startPage();
    List<ProductPackageDTO> list = productPackageService.selectProductPackageList(productPackageDTO);
    return getDataTable(list);
    }

    /**
    * 查询产品包列表
    */
    @RequiresPermissions("system:manage:productPackage:list")
    @GetMapping("/list")
    public AjaxResult list(ProductPackageDTO productPackageDTO){
    List<ProductPackageDTO> list = productPackageService.selectProductPackageList(productPackageDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增产品包
    */
    @RequiresPermissions("system:manage:productPackage:add")
    @Log(title = "新增产品包", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody ProductPackageDTO productPackageDTO) {
    return toAjax(productPackageService.insertProductPackage(productPackageDTO));
    }


    /**
    * 修改产品包
    */
    @RequiresPermissions("system:manage:productPackage:edit")
    @Log(title = "修改产品包", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody ProductPackageDTO productPackageDTO)
    {
    return toAjax(productPackageService.updateProductPackage(productPackageDTO));
    }

    /**
    * 逻辑删除产品包
    */
    @RequiresPermissions("system:manage:productPackage:remove")
    @Log(title = "删除产品包", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody ProductPackageDTO productPackageDTO)
    {
    return toAjax(productPackageService.logicDeleteProductPackageByProductPackageId(productPackageDTO));
    }
    /**
    * 批量修改产品包
    */
    @RequiresPermissions("system:manage:productPackage:edits")
    @Log(title = "批量修改产品包", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<ProductPackageDTO> productPackageDtos)
    {
    return toAjax(productPackageService.updateProductPackages(productPackageDtos));
    }

    /**
    * 批量新增产品包
    */
    @RequiresPermissions("system:manage:productPackage:insertProductPackages")
    @Log(title = "批量新增产品包", businessType = BusinessType.INSERT)
    @PostMapping("/insertProductPackages")
    public AjaxResult insertProductPackages(@RequestBody List<ProductPackageDTO> productPackageDtos)
    {
    return toAjax(productPackageService.insertProductPackages(productPackageDtos));
    }

    /**
    * 逻辑批量删除产品包
    */
    @RequiresPermissions("system:manage:productPackage:removes")
    @Log(title = "批量删除产品包", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<ProductPackageDTO>  ProductPackageDtos)
    {
    return toAjax(productPackageService.logicDeleteProductPackageByProductPackageIds(ProductPackageDtos));
    }
}
