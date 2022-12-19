package net.qixiaowei.operate.cloud.controller.product;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.excel.CustomVerticalCellStyleStrategy;
import net.qixiaowei.integration.common.utils.excel.ExcelUtils;
import net.qixiaowei.integration.common.utils.excel.SelectSheetWriteHandler;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.excel.product.ProductExcel;
import net.qixiaowei.operate.cloud.excel.product.ProductImportListener;
import net.qixiaowei.operate.cloud.service.product.IProductService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDictionaryDataService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author TANGMICHI
 * @since 2022-10-08
 */
@RestController
@RequestMapping("product")
public class ProductController extends BaseController {


    @Autowired
    private IProductService productService;
    @Autowired
    private RemoteDictionaryDataService remoteDictionaryDataService;


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

    /**
     * 导出模板
     */
    @SneakyThrows
//    @RequiresPermissions("system:manage:employee:import")
    @GetMapping("/export-template")
    public void exportUser(HttpServletResponse response) {
        Map<Integer, List<String>> selectMap = new HashMap<>();
        //自定义表头
        List<List<String>> head = ProductImportListener.head(selectMap);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("产品配置" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        CustomVerticalCellStyleStrategy levelStrategy = new CustomVerticalCellStyleStrategy(head);

        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap))
                .head(head)
                .registerWriteHandler(levelStrategy)
                .registerWriteHandler(new SimpleColumnWidthStyleStrategy(21))
                // 重写AbstractColumnWidthStyleStrategy策略的setColumnWidth方法
                .registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                    @Override
                    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
                        if (integer == 0) {
                            Row row = cell.getRow();
                            row.setHeightInPoints(144);
                        }
                    }
                })
                .sheet("产品配置")// 设置 sheet 的名字
                .doWrite(new ArrayList<>());
    }
    /**
     * 导入产品
     */
//    @RequiresPermissions("system:manage:employee:import")
    @PostMapping("import")
    public AjaxResult importEmployee(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }

        List<ProductExcel> list = new ArrayList<>();

        //构建读取器
        ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
        ExcelReaderSheetBuilder sheet = read.sheet(0);
        List<Map<Integer, String>> listMap = sheet.doReadSync();


        ProductExcel productExcel = new ProductExcel();
        ExcelUtils.mapToListModel(2, 0, listMap, productExcel, list);
        // 调用importer方法
        productService.importProduct(list);

        return AjaxResult.success();
    }
}
