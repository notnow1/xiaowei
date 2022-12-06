package net.qixiaowei.operate.cloud.controller.product;

import java.util.List;

import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.operate.cloud.api.dto.product.ProductUnitDTO;
import net.qixiaowei.operate.cloud.service.product.IProductUnitService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author TANGMICHI
 * @since 2022-10-08
 */
@RestController
@RequestMapping("productUnit")
public class ProductUnitController extends BaseController {


    @Autowired
    private IProductUnitService productUnitService;


    /**
     * 查询产品单位表详情
     */
    @RequiresPermissions("operate:cloud:productUnit:info")
    @GetMapping("/info/{productUnitId}")
    public AjaxResult info(@PathVariable Long productUnitId) {
        ProductUnitDTO productUnitDTO = productUnitService.selectProductUnitByProductUnitId(productUnitId);
        return AjaxResult.success(productUnitDTO);
    }

    /**
     * 分页查询产品单位表列表
     */
    @RequiresPermissions("operate:cloud:productUnit:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(ProductUnitDTO productUnitDTO) {
        startPage();
        List<ProductUnitDTO> list = productUnitService.selectProductUnitList(productUnitDTO);
        return getDataTable(list);
    }

    /**
     * 新增产品单位表
     */
    @RequiresPermissions("operate:cloud:productUnit:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(ProductUnitDTO.AddProductUnitDTO.class) ProductUnitDTO productUnitDTO) {
        return AjaxResult.success(productUnitService.insertProductUnit(productUnitDTO));
    }

    /**
     * 修改产品单位表
     */
    @RequiresPermissions("operate:cloud:productUnit:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(ProductUnitDTO.UpdateProductUnitDTO.class) ProductUnitDTO productUnitDTO) {
        return toAjax(productUnitService.updateProductUnit(productUnitDTO));
    }

    /**
     * 逻辑删除产品单位表
     */
    @RequiresPermissions("operate:cloud:productUnit:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(ProductUnitDTO.DeleteProductUnitDTO.class) ProductUnitDTO productUnitDTO) {
        return toAjax(productUnitService.logicDeleteProductUnitByProductUnitId(productUnitDTO));
    }

    /**
     * 逻辑批量删除产品单位表
     */
    @RequiresPermissions("operate:cloud:productUnit:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> productUnitIds) {
        return toAjax(productUnitService.logicDeleteProductUnitByProductUnitIds(productUnitIds));
    }


    /**
     * 查询产品单位表列表
     */
    @GetMapping("/list")
    public AjaxResult list(ProductUnitDTO productUnitDTO) {
        List<ProductUnitDTO> list = productUnitService.selectProductUnitList(productUnitDTO);
        return AjaxResult.success(list);
    }
}
