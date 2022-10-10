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
import net.qixiaowei.operate.cloud.api.dto.product.ProductFileDTO;
import net.qixiaowei.operate.cloud.service.product.IProductFileService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-10-09
*/
@RestController
@RequestMapping("productFile")
public class ProductFileController extends BaseController
{


    @Autowired
    private IProductFileService productFileService;


    /**
    * 查询产品文件表详情
    */
    @RequiresPermissions("operate:cloud:productFile:info")
    @GetMapping("/info/{productFileId}")
    public AjaxResult info(@PathVariable Long productFileId){
    ProductFileDTO productFileDTO = productFileService.selectProductFileByProductFileId(productFileId);
        return AjaxResult.success(productFileDTO);
    }

    /**
    * 分页查询产品文件表列表
    */
    @RequiresPermissions("operate:cloud:productFile:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(ProductFileDTO productFileDTO){
    startPage();
    List<ProductFileDTO> list = productFileService.selectProductFileList(productFileDTO);
    return getDataTable(list);
    }

    /**
    * 查询产品文件表列表
    */
    @RequiresPermissions("operate:cloud:productFile:list")
    @GetMapping("/list")
    public AjaxResult list(ProductFileDTO productFileDTO){
    List<ProductFileDTO> list = productFileService.selectProductFileList(productFileDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增产品文件表
    */
    @RequiresPermissions("operate:cloud:productFile:add")
    @Log(title = "新增产品文件表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody ProductFileDTO productFileDTO) {
    return toAjax(productFileService.insertProductFile(productFileDTO));
    }


    /**
    * 修改产品文件表
    */
    @RequiresPermissions("operate:cloud:productFile:edit")
    @Log(title = "修改产品文件表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody ProductFileDTO productFileDTO)
    {
    return toAjax(productFileService.updateProductFile(productFileDTO));
    }

    /**
    * 逻辑删除产品文件表
    */
    @RequiresPermissions("operate:cloud:productFile:remove")
    @Log(title = "删除产品文件表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody ProductFileDTO productFileDTO)
    {
    return toAjax(productFileService.logicDeleteProductFileByProductFileId(productFileDTO));
    }
    /**
    * 批量修改产品文件表
    */
    @RequiresPermissions("operate:cloud:productFile:edits")
    @Log(title = "批量修改产品文件表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<ProductFileDTO> productFileDtos)
    {
    return toAjax(productFileService.updateProductFiles(productFileDtos));
    }

    /**
    * 批量新增产品文件表
    */
    @RequiresPermissions("operate:cloud:productFile:insertProductFiles")
    @Log(title = "批量新增产品文件表", businessType = BusinessType.INSERT)
    @PostMapping("/insertProductFiles")
    public AjaxResult insertProductFiles(@RequestBody List<ProductFileDTO> productFileDtos)
    {
    return toAjax(productFileService.insertProductFiles(productFileDtos));
    }

    /**
    * 逻辑批量删除产品文件表
    */
    @RequiresPermissions("operate:cloud:productFile:removes")
    @Log(title = "批量删除产品文件表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<ProductFileDTO>  ProductFileDtos)
    {
    return toAjax(productFileService.logicDeleteProductFileByProductFileIds(ProductFileDtos));
    }
}
