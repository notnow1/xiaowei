package net.qixiaowei.operate.cloud.controller.product;

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
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.service.product.IProductService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-10-08
*/
@RestController
@RequestMapping("product")
public class ProductController extends BaseController
{


    @Autowired
    private IProductService productService;

    /**
    * 分页查询产品表列表
    */
    @RequiresPermissions("operate:cloud:product:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(ProductDTO productDTO){
    startPage();
    List<ProductDTO> list = productService.selectProductList(productDTO);
    return getDataTable(list);
    }

    /**
    * 查询产品表列表
    */
    @RequiresPermissions("operate:cloud:product:list")
    @GetMapping("/list")
    public AjaxResult list(ProductDTO productDTO){
    List<ProductDTO> list = productService.selectProductList(productDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增产品表
    */
    @RequiresPermissions("operate:cloud:product:add")
    @Log(title = "新增产品表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody ProductDTO productDTO) {
    return toAjax(productService.insertProduct(productDTO));
    }


    /**
    * 修改产品表
    */
    @RequiresPermissions("operate:cloud:product:edit")
    @Log(title = "修改产品表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody ProductDTO productDTO)
    {
    return toAjax(productService.updateProduct(productDTO));
    }

    /**
    * 逻辑删除产品表
    */
    @RequiresPermissions("operate:cloud:product:remove")
    @Log(title = "删除产品表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody ProductDTO productDTO)
    {
    return toAjax(productService.logicDeleteProductByProductId(productDTO));
    }
    /**
    * 批量修改产品表
    */
    @RequiresPermissions("operate:cloud:product:edits")
    @Log(title = "批量修改产品表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<ProductDTO> productDtos)
    {
    return toAjax(productService.updateProducts(productDtos));
    }

    /**
    * 批量新增产品表
    */
    @RequiresPermissions("operate:cloud:product:insertProducts")
    @Log(title = "批量新增产品表", businessType = BusinessType.INSERT)
    @PostMapping("/insertProducts")
    public AjaxResult insertProducts(@RequestBody List<ProductDTO> productDtos)
    {
    return toAjax(productService.insertProducts(productDtos));
    }

    /**
    * 逻辑批量删除产品表
    */
    @RequiresPermissions("operate:cloud:product:removes")
    @Log(title = "批量删除产品表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<ProductDTO>  ProductDtos)
    {
    return toAjax(productService.logicDeleteProductByProductIds(ProductDtos));
    }
}
