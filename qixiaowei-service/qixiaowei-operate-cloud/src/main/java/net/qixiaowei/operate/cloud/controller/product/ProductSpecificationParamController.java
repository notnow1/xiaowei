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
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationParamDTO;
import net.qixiaowei.operate.cloud.service.product.IProductSpecificationParamService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-10-08
*/
@RestController
@RequestMapping("productSpecificationParam")
public class ProductSpecificationParamController extends BaseController
{


    @Autowired
    private IProductSpecificationParamService productSpecificationParamService;


    /**
    * 查询产品规格参数表详情
    */
    //@RequiresPermissions("operate:cloud:productSpecificationParam:info")
    @GetMapping("/info/{productSpecificationParamId}")
    public AjaxResult info(@PathVariable Long productSpecificationParamId){
    ProductSpecificationParamDTO productSpecificationParamDTO = productSpecificationParamService.selectProductSpecificationParamByProductSpecificationParamId(productSpecificationParamId);
        return AjaxResult.success(productSpecificationParamDTO);
    }

    /**
    * 分页查询产品规格参数表列表
    */
    //@RequiresPermissions("operate:cloud:productSpecificationParam:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(ProductSpecificationParamDTO productSpecificationParamDTO){
    startPage();
    List<ProductSpecificationParamDTO> list = productSpecificationParamService.selectProductSpecificationParamList(productSpecificationParamDTO);
    return getDataTable(list);
    }

    /**
    * 查询产品规格参数表列表
    */
    //@RequiresPermissions("operate:cloud:productSpecificationParam:list")
    @GetMapping("/list")
    public AjaxResult list(ProductSpecificationParamDTO productSpecificationParamDTO){
    List<ProductSpecificationParamDTO> list = productSpecificationParamService.selectProductSpecificationParamList(productSpecificationParamDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增产品规格参数表
    */
    //@RequiresPermissions("operate:cloud:productSpecificationParam:add")
    //@Log(title = "新增产品规格参数表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody ProductSpecificationParamDTO productSpecificationParamDTO) {
    return toAjax(productSpecificationParamService.insertProductSpecificationParam(productSpecificationParamDTO));
    }


    /**
    * 修改产品规格参数表
    */
    //@RequiresPermissions("operate:cloud:productSpecificationParam:edit")
    //@Log(title = "修改产品规格参数表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody ProductSpecificationParamDTO productSpecificationParamDTO)
    {
    return toAjax(productSpecificationParamService.updateProductSpecificationParam(productSpecificationParamDTO));
    }

    /**
    * 逻辑删除产品规格参数表
    */
    //@RequiresPermissions("operate:cloud:productSpecificationParam:remove")
    //@Log(title = "删除产品规格参数表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody ProductSpecificationParamDTO productSpecificationParamDTO)
    {
    return toAjax(productSpecificationParamService.logicDeleteProductSpecificationParamByProductSpecificationParamId(productSpecificationParamDTO));
    }
    /**
    * 批量修改产品规格参数表
    */
    //@RequiresPermissions("operate:cloud:productSpecificationParam:edits")
    //@Log(title = "批量修改产品规格参数表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<ProductSpecificationParamDTO> productSpecificationParamDtos)
    {
    return toAjax(productSpecificationParamService.updateProductSpecificationParams(productSpecificationParamDtos));
    }

    /**
    * 批量新增产品规格参数表
    */
    //@RequiresPermissions("operate:cloud:productSpecificationParam:insertProductSpecificationParams")
    //@Log(title = "批量新增产品规格参数表", businessType = BusinessType.INSERT)
    @PostMapping("/insertProductSpecificationParams")
    public AjaxResult insertProductSpecificationParams(@RequestBody List<ProductSpecificationParamDTO> productSpecificationParamDtos)
    {
    return toAjax(productSpecificationParamService.insertProductSpecificationParams(productSpecificationParamDtos));
    }

    /**
    * 逻辑批量删除产品规格参数表
    */
    //@RequiresPermissions("operate:cloud:productSpecificationParam:removes")
    //@Log(title = "批量删除产品规格参数表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<ProductSpecificationParamDTO>  ProductSpecificationParamDtos)
    {
    return toAjax(productSpecificationParamService.logicDeleteProductSpecificationParamByProductSpecificationParamIds(ProductSpecificationParamDtos));
    }
}
