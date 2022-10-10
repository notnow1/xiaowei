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
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationDTO;
import net.qixiaowei.operate.cloud.service.product.IProductSpecificationService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-10-09
*/
@RestController
@RequestMapping("productSpecification")
public class ProductSpecificationController extends BaseController
{


    @Autowired
    private IProductSpecificationService productSpecificationService;


    /**
    * 查询产品规格表详情
    */
    @RequiresPermissions("operate:cloud:productSpecification:info")
    @GetMapping("/info/{productSpecificationId}")
    public AjaxResult info(@PathVariable Long productSpecificationId){
    ProductSpecificationDTO productSpecificationDTO = productSpecificationService.selectProductSpecificationByProductSpecificationId(productSpecificationId);
        return AjaxResult.success(productSpecificationDTO);
    }

    /**
    * 分页查询产品规格表列表
    */
    @RequiresPermissions("operate:cloud:productSpecification:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(ProductSpecificationDTO productSpecificationDTO){
    startPage();
    List<ProductSpecificationDTO> list = productSpecificationService.selectProductSpecificationList(productSpecificationDTO);
    return getDataTable(list);
    }

    /**
    * 查询产品规格表列表
    */
    @RequiresPermissions("operate:cloud:productSpecification:list")
    @GetMapping("/list")
    public AjaxResult list(ProductSpecificationDTO productSpecificationDTO){
    List<ProductSpecificationDTO> list = productSpecificationService.selectProductSpecificationList(productSpecificationDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增产品规格表
    */
    @RequiresPermissions("operate:cloud:productSpecification:add")
    @Log(title = "新增产品规格表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody ProductSpecificationDTO productSpecificationDTO) {
    return toAjax(productSpecificationService.insertProductSpecification(productSpecificationDTO));
    }


    /**
    * 修改产品规格表
    */
    @RequiresPermissions("operate:cloud:productSpecification:edit")
    @Log(title = "修改产品规格表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody ProductSpecificationDTO productSpecificationDTO)
    {
    return toAjax(productSpecificationService.updateProductSpecification(productSpecificationDTO));
    }

    /**
    * 逻辑删除产品规格表
    */
    @RequiresPermissions("operate:cloud:productSpecification:remove")
    @Log(title = "删除产品规格表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody ProductSpecificationDTO productSpecificationDTO)
    {
    return toAjax(productSpecificationService.logicDeleteProductSpecificationByProductSpecificationId(productSpecificationDTO));
    }
    /**
    * 批量修改产品规格表
    */
    @RequiresPermissions("operate:cloud:productSpecification:edits")
    @Log(title = "批量修改产品规格表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<ProductSpecificationDTO> productSpecificationDtos)
    {
    return toAjax(productSpecificationService.updateProductSpecifications(productSpecificationDtos));
    }

    /**
    * 批量新增产品规格表
    */
    @RequiresPermissions("operate:cloud:productSpecification:insertProductSpecifications")
    @Log(title = "批量新增产品规格表", businessType = BusinessType.INSERT)
    @PostMapping("/insertProductSpecifications")
    public AjaxResult insertProductSpecifications(@RequestBody List<ProductSpecificationDTO> productSpecificationDtos)
    {
    return toAjax(productSpecificationService.insertProductSpecifications(productSpecificationDtos));
    }

    /**
    * 逻辑批量删除产品规格表
    */
    @RequiresPermissions("operate:cloud:productSpecification:removes")
    @Log(title = "批量删除产品规格表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<ProductSpecificationDTO>  ProductSpecificationDtos)
    {
    return toAjax(productSpecificationService.logicDeleteProductSpecificationByProductSpecificationIds(ProductSpecificationDtos));
    }
}
