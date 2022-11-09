package net.qixiaowei.operate.cloud.controller.product;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationDataDTO;
import net.qixiaowei.operate.cloud.service.product.IProductSpecificationDataService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-10-09
*/
@RestController
@RequestMapping("productSpecificationData")
public class ProductSpecificationDataController extends BaseController
{


    @Autowired
    private IProductSpecificationDataService productSpecificationDataService;


    /**
    * 查询产品规格数据表详情
    */
    @RequiresPermissions("operate:cloud:productSpecificationData:info")
    @GetMapping("/info/{productSpecificationDataId}")
    public AjaxResult info(@PathVariable Long productSpecificationDataId){
    ProductSpecificationDataDTO productSpecificationDataDTO = productSpecificationDataService.selectProductSpecificationDataByProductSpecificationDataId(productSpecificationDataId);
        return AjaxResult.success(productSpecificationDataDTO);
    }

    /**
    * 分页查询产品规格数据表列表
    */
    @RequiresPermissions("operate:cloud:productSpecificationData:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(ProductSpecificationDataDTO productSpecificationDataDTO){
    startPage();
    List<ProductSpecificationDataDTO> list = productSpecificationDataService.selectProductSpecificationDataList(productSpecificationDataDTO);
    return getDataTable(list);
    }

    /**
    * 查询产品规格数据表列表
    */
    @RequiresPermissions("operate:cloud:productSpecificationData:list")
    @GetMapping("/list")
    public AjaxResult list(ProductSpecificationDataDTO productSpecificationDataDTO){
    List<ProductSpecificationDataDTO> list = productSpecificationDataService.selectProductSpecificationDataList(productSpecificationDataDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增产品规格数据表
    */
    @RequiresPermissions("operate:cloud:productSpecificationData:add")
    @Log(title = "新增产品规格数据表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody ProductSpecificationDataDTO productSpecificationDataDTO) {
    return toAjax(productSpecificationDataService.insertProductSpecificationData(productSpecificationDataDTO));
    }


    /**
    * 修改产品规格数据表
    */
    @RequiresPermissions("operate:cloud:productSpecificationData:edit")
    @Log(title = "修改产品规格数据表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody ProductSpecificationDataDTO productSpecificationDataDTO)
    {
    return toAjax(productSpecificationDataService.updateProductSpecificationData(productSpecificationDataDTO));
    }

    /**
    * 逻辑删除产品规格数据表
    */
    @RequiresPermissions("operate:cloud:productSpecificationData:remove")
    @Log(title = "删除产品规格数据表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody ProductSpecificationDataDTO productSpecificationDataDTO)
    {
    return toAjax(productSpecificationDataService.logicDeleteProductSpecificationDataByProductSpecificationDataId(productSpecificationDataDTO));
    }
    /**
    * 批量修改产品规格数据表
    */
    @RequiresPermissions("operate:cloud:productSpecificationData:edits")
    @Log(title = "批量修改产品规格数据表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<ProductSpecificationDataDTO> productSpecificationDataDtos)
    {
    return toAjax(productSpecificationDataService.updateProductSpecificationDatas(productSpecificationDataDtos));
    }

    /**
    * 批量新增产品规格数据表
    */
    @RequiresPermissions("operate:cloud:productSpecificationData:insertProductSpecificationDatas")
    @Log(title = "批量新增产品规格数据表", businessType = BusinessType.INSERT)
    @PostMapping("/insertProductSpecificationDatas")
    public AjaxResult insertProductSpecificationDatas(@RequestBody List<ProductSpecificationDataDTO> productSpecificationDataDtos)
    {
    return toAjax(productSpecificationDataService.insertProductSpecificationDatas(productSpecificationDataDtos));
    }

    /**
    * 逻辑批量删除产品规格数据表
    */
    @RequiresPermissions("operate:cloud:productSpecificationData:removes")
    @Log(title = "批量删除产品规格数据表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<ProductSpecificationDataDTO>  ProductSpecificationDataDtos)
    {
    return toAjax(productSpecificationDataService.logicDeleteProductSpecificationDataByProductSpecificationDataIds(ProductSpecificationDataDtos));
    }
}
