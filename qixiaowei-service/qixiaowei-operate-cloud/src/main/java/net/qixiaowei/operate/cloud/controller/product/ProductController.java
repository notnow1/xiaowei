package net.qixiaowei.operate.cloud.controller.product;

import java.util.List;

import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.service.product.IProductService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author TANGMICHI
 * @since 2022-10-08
 */
@RestController
@RequestMapping("product")
public class ProductController extends BaseController {


    @Autowired
    private IProductService productService;


    /**
     * 返回产品层级
     */
    @RequiresPermissions(value = {"operate:cloud:product:list", "operate:cloud:product:pageList"}, logical = Logical.OR)
    @GetMapping("/selectLevel")
    public AjaxResult selectLevel() {
        return AjaxResult.success(productService.selectLevel());
    }

    /**
     * 查询产品表详情
     */
    @RequiresPermissions("operate:cloud:product:info")
    @GetMapping("/info/{productId}")
    public AjaxResult info(@PathVariable Long productId) {
        ProductDTO productDTO = productService.selectProductByProductId(productId);
        return AjaxResult.success(productDTO);
    }

    /**
     * 查询上级产品
     */
    @RequiresPermissions(value = {"operate:cloud:product:list", "operate:cloud:product:pageList"}, logical = Logical.OR)
    @GetMapping("/queryparent")
    public AjaxResult queryparent() {
        List<ProductDTO> list = productService.queryparent();
        return AjaxResult.success(list);
    }

    /**
     * 分页查询产品表列表
     */
    @RequiresPermissions("operate:cloud:product:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(ProductDTO productDTO) {
        startPage();
        List<ProductDTO> list = productService.selectProductList(productDTO);
        return getDataTable(list);
    }

    /**
     * 查询产品表列表
     */
    @RequiresPermissions(value = {"operate:cloud:product:list", "operate:cloud:product:pageList"}, logical = Logical.OR)
    @GetMapping("/list")
    public AjaxResult list(ProductDTO productDTO) {
        List<ProductDTO> list = productService.selectProductList(productDTO);
        return AjaxResult.success(list);
    }

    /**
     * 新增产品表
     */
    @RequiresPermissions("operate:cloud:product:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(ProductDTO.AddProductDTO.class) ProductDTO productDTO) {
        return AjaxResult.success(productService.insertProduct(productDTO));
    }

    /**
     * 修改产品表
     */
    @RequiresPermissions("operate:cloud:product:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(ProductDTO.UpdateProductDTO.class) ProductDTO productDTO) {
        return toAjax(productService.updateProduct(productDTO));
    }

    /**
     * 逻辑删除产品表
     */
    @RequiresPermissions("operate:cloud:product:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(ProductDTO.DeleteProductDTO.class) ProductDTO productDTO) {
        return toAjax(productService.logicDeleteProductByProductId(productDTO));
    }

    /**
     * 逻辑批量删除产品表
     */
    @RequiresPermissions("operate:cloud:product:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> productIds) {
        return toAjax(productService.logicDeleteProductByProductIds(productIds));
    }
}
