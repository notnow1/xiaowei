package net.qixiaowei.operate.cloud.controller.product;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.DataFormatData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.excel.CustomVerticalCellStyleStrategy;
import net.qixiaowei.integration.common.utils.excel.SelectSheetWriteHandler;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.dto.product.ProductUnitDTO;
import net.qixiaowei.operate.cloud.excel.product.ProductExportExcel;
import net.qixiaowei.operate.cloud.excel.product.ProductExcel;
import net.qixiaowei.operate.cloud.excel.product.ProductImportListener;
import net.qixiaowei.operate.cloud.service.product.IProductService;
import net.qixiaowei.operate.cloud.service.product.IProductUnitService;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryTypeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDictionaryDataService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


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
    @Autowired
    private IProductUnitService productUnitService;


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
    public AjaxResult queryparent(@RequestParam(required = false) Long productId) {
        List<ProductDTO> list = productService.queryparent(productId);
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
     * 生成产品编码
     *
     * @return 产品编码
     */
    @RequiresPermissions(value = {"operate:cloud:product:add", "operate:cloud:product:edit"}, logical = Logical.OR)
    @GetMapping("/generate/productCode")
    public AjaxResult generateProductCode() {
        return AjaxResult.success("操作成功",productService.generateProductCode());
    }

    /**
     * 新增产品表
     */
    @Log(title = "新增产品", businessType = BusinessType.PRODUCT, businessId = "productId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:product:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(ProductDTO.AddProductDTO.class) ProductDTO productDTO) {
        return AjaxResult.success(productService.insertProduct(productDTO));
    }

    /**
     * 修改产品表
     */
    @Log(title = "编辑产品", businessType = BusinessType.PRODUCT, businessId = "productId", operationType = OperationType.UPDATE)
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
    @RequiresPermissions("system:manage:employee:import")
    @GetMapping("/export-template")
    public void exportProductTemplate(HttpServletResponse response) {
        List<String> dictionaryLabels = new ArrayList<>();

        //字典名称
        R<DictionaryTypeDTO> dictionaryTypeDTOR = remoteDictionaryDataService.selectDictionaryTypeByCode("PRODUCT_CATEGORY",SecurityConstants.INNER);
        DictionaryTypeDTO data = dictionaryTypeDTOR.getData();
        if (StringUtils.isNotNull(data)){
            //字典值
            R<List<DictionaryDataDTO>> listR = remoteDictionaryDataService.selectDictionaryDataByProduct(data.getDictionaryTypeId(), SecurityConstants.INNER);
            List<DictionaryDataDTO> dictionaryDataDTOList = listR.getData();
            if (StringUtils.isNotEmpty(dictionaryDataDTOList)){
                dictionaryLabels = dictionaryDataDTOList.stream().map(DictionaryDataDTO::getDictionaryLabel).collect(Collectors.toList());
            }
        }
        // 产品单位
        ProductUnitDTO productUnitDTO = new ProductUnitDTO();
        List<ProductUnitDTO> productUnitDTOS = productUnitService.selectProductUnitList(productUnitDTO);

        Map<Integer, List<String>> selectMap = new HashMap<>();
        //自定义表头
        List<List<String>> head = ProductImportListener.importHead(selectMap,data,dictionaryLabels,productUnitDTOS);


        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("产品配置" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");


        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .head(head)
                .inMemory(true)
                .useDefaultStyle(false)
                .sheet("产品配置")
                // 设置 sheet 的名字
                .registerWriteHandler(new SheetWriteHandler() {
                    @Override
                    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                        for (int i = 0; i < 24; i++) {
                            // 设置为文本格式
                            Sheet sheet = writeSheetHolder.getSheet();
                            CellStyle cellStyle = writeWorkbookHolder.getCachedWorkbook().createCellStyle();
                            // 49为文本格式
                            cellStyle.setDataFormat((short) 49);
                            // i为列，一整列设置为文本格式
                            sheet.setDefaultColumnStyle(i, cellStyle);

                        }
                    }
                })
                //设置文本
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

                        if (cell.getRowIndex() == 0) {
                            Sheet sheet = writeSheetHolder.getSheet();
                            Workbook workbook = sheet.getWorkbook();
                            // xlsx格式，如果是老版本格式的话就用 HSSFRichTextString
                            XSSFRichTextString richString = new XSSFRichTextString(cell.getStringCellValue());
                            Font font1 = workbook.createFont();
                            font1.setFontName("微软雅黑");
                            font1.setFontHeightInPoints((short) 11);
                            font1.setColor(IndexedColors.RED.getIndex());
                            font1.setBold(true);
                            richString.applyFont(0, 3, font1);
                            Font font2 = workbook.createFont();
                            font2.setFontName("微软雅黑");
                            font2.setFontHeightInPoints((short) 11);
                            font2.setColor(IndexedColors.BLACK.getIndex());
                            // 从哪到哪，你想设置成什么样的字体都行startIndex，endIndex
                            richString.applyFont(3, 99, font2);
                            // 再设置回每个单元格里
                            cell.setCellValue(richString);
                        }
                    }
                })
                //设置文本
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        Cell cell = context.getCell();
                        int rowIndex = cell.getRowIndex();
                        int columnIndex = cell.getColumnIndex();
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        //设置文本
                        DataFormatData dataFormatData = new DataFormatData();
                        dataFormatData.setIndex((short) 49);
                        writeCellStyle.setDataFormatData(dataFormatData);
                        // 设置字体
                        WriteFont headWriteFont = new WriteFont();
                        if (rowIndex == 0) {
                            //headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            //headWriteFont.setFontHeightInPoints((short) 11);
                            //加粗
                            //headWriteFont.setBold(true);
                            //headWriteFont.setFontName("微软雅黑");
                            //writeCellStyle.setWriteFont(headWriteFont);
                            //靠左
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            //设置 自动换行
                            writeCellStyle.setWrapped(true);
                            ;
                        } else {
                            //设置边框
                            writeCellStyle.setBorderLeft(BorderStyle.THIN);
                            writeCellStyle.setBorderTop(BorderStyle.THIN);
                            writeCellStyle.setBorderRight(BorderStyle.THIN);
                            writeCellStyle.setBorderBottom(BorderStyle.THIN);
                            //居左
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            if (columnIndex == 0 || columnIndex == 1 || columnIndex == 3 ) {
                                //设置 自动换行
                                writeCellStyle.setWrapped(true);
                                headWriteFont.setColor(IndexedColors.RED.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
                                headWriteFont.setFontName("微软雅黑");
                                writeCellStyle.setWriteFont(headWriteFont);
                            } else {
                                //设置 自动换行
                                writeCellStyle.setWrapped(true);
                                headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                                headWriteFont.setFontHeightInPoints((short) 11);
                                headWriteFont.setFontName("微软雅黑");
                                writeCellStyle.setWriteFont(headWriteFont);
                            }
                        }
                        cellData.setWriteCellStyle(writeCellStyle);
                    }
                })
                .registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                    @Override
                    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
                        Sheet sheet = writeSheetHolder.getSheet();
                        int columnIndex = cell.getColumnIndex();
                        int rowIndex = cell.getRowIndex();
                        Row row = cell.getRow();
                        if (rowIndex == 0) {
                            // 行高100
                            row.setHeight((short) (20 * 100));
                        } else {
                            // 行高16
                            row.setHeight((short) (20 * 16));
                        }
                        // 列宽16
                        sheet.setColumnWidth(columnIndex, (270 * 16));
                    }
                })
                .doWrite(new ArrayList<>());
    }
    /**
     * 导入产品
     */
    @RequiresPermissions("system:manage:employee:import")
    @PostMapping("import")
    public AjaxResult importProduct(MultipartFile file) throws IOException {
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

        //产品
        ProductImportListener.mapToListModel(2, 0, listMap, list);
        // 调用importer方法
        productService.importProduct(list);

        return AjaxResult.success();
    }

    /**
     * 导出产品
     */
    @SneakyThrows
    @RequiresPermissions("system:manage:employee:export")
    @GetMapping("export")
    public void exportProduct(@RequestParam Map<String, Object> employee,ProductDTO productDTO,HttpServletResponse response) {
        List<String> dictionaryLabels = new ArrayList<>();

        //字典名称
        R<DictionaryTypeDTO> dictionaryTypeDTOR = remoteDictionaryDataService.selectDictionaryTypeByCode("PRODUCT_CATEGORY",SecurityConstants.INNER);
        DictionaryTypeDTO data = dictionaryTypeDTOR.getData();
        if (StringUtils.isNotNull(data)){
            //字典值
            R<List<DictionaryDataDTO>> listR = remoteDictionaryDataService.selectDictionaryDataByProduct(data.getDictionaryTypeId(), SecurityConstants.INNER);
            List<DictionaryDataDTO> dictionaryDataDTOList = listR.getData();
            if (StringUtils.isNotEmpty(dictionaryDataDTOList)){
                dictionaryLabels = dictionaryDataDTOList.stream().map(DictionaryDataDTO::getDictionaryLabel).collect(Collectors.toList());
            }
        }
        // 产品单位
        ProductUnitDTO productUnitDTO = new ProductUnitDTO();
        List<ProductUnitDTO> productUnitDTOS = productUnitService.selectProductUnitList(productUnitDTO);


        //自定义表头
        List<List<String>> head = ProductImportListener.exportHead(data,dictionaryLabels,productUnitDTOS);
        List<Long> productIds = new ArrayList<>();
        if (!CheckObjectIsNullUtils.isNull(productDTO)){
            List<Long> productIds1 = productDTO.getProductIds();
            if (StringUtils.isNotEmpty(productIds1)){
                for (Long productId : productIds1) {
                    List<ProductDTO> productDTOS = productService.selectAncestors(productId);
                    if (StringUtils.isNotEmpty(productDTOS) ) {
                        productIds.addAll(productDTOS.stream().map(ProductDTO::getProductId).distinct().collect(Collectors.toList()));
                    }
                }
            }
        }else {
            //打平的树结构
            productIds = productService.treeToList();
        }

        //导出产品列表
        List<ProductExportExcel> productExportExcelList = productService.exportProduct(productIds);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("产品配置" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");


        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .inMemory(true)
                .useDefaultStyle(false)
                .head(head)
                .sheet("产品配置")// 设置 sheet 的名字
                .registerWriteHandler(new SheetWriteHandler() {
                    @Override
                    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                        for (int i = 0; i < 100; i++) {
                            // 设置为文本格式
                            Sheet sheet = writeSheetHolder.getSheet();
                            CellStyle cellStyle = writeWorkbookHolder.getCachedWorkbook().createCellStyle();
                            // 49为文本格式
                            cellStyle.setDataFormat((short) 49);
                            // i为列，一整列设置为文本格式
                            sheet.setDefaultColumnStyle(i, cellStyle);

                        }
                    }
                })
                //设置文本
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        Cell cell = context.getCell();
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        // 设置字体
                        WriteFont headWriteFont = new WriteFont();
                        if (context.getRowIndex() == 0) {
                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            //加粗
                            headWriteFont.setBold(true);
                            headWriteFont.setFontName("微软雅黑");
                            writeCellStyle.setWriteFont(headWriteFont);
                            //靠左
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            //设置 自动换行
                            writeCellStyle.setWrapped(true);
                            //设置边框
                            writeCellStyle.setBorderLeft(BorderStyle.THIN);
                            writeCellStyle.setBorderTop(BorderStyle.THIN);
                            writeCellStyle.setBorderRight(BorderStyle.THIN);
                            writeCellStyle.setBorderBottom(BorderStyle.THIN);
                            // 拿到poi的workbook
                            Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                            // 这里千万记住 想办法能复用的地方把他缓存起来 一个表格最多创建6W个样式
                            // 不同单元格尽量传同一个 cellStyle
                            //设置rgb颜色
                            byte[] rgb = new byte[]{(byte) 221, (byte) 235, (byte) 247};
                            CellStyle cellStyle = workbook.createCellStyle();
                            XSSFCellStyle xssfCellColorStyle = (XSSFCellStyle) cellStyle;
                            xssfCellColorStyle.setFillForegroundColor(new XSSFColor(rgb, null));
                            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND
                            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            // 由于这里没有指定dataformat 最后展示的数据 格式可能会不太正确
                            // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                            // cell里面去 会导致自己设置的不一样（很关键）
                            cellData.setOriginCellStyle(xssfCellColorStyle);
                            cell.setCellStyle(cellStyle);
                        } else {
                            //居左
                            writeCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                            //垂直居中
                            writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            //设置 自动换行
                            writeCellStyle.setWrapped(true);
                            headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                            headWriteFont.setFontHeightInPoints((short) 11);
                            headWriteFont.setFontName("微软雅黑");
                            writeCellStyle.setWriteFont(headWriteFont);
                        }
                        cellData.setWriteCellStyle(writeCellStyle);
                    }
                })
                .registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                    @Override
                    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
                        Sheet sheet = writeSheetHolder.getSheet();
                        int columnIndex = cell.getColumnIndex();
                        // 列宽16
                        sheet.setColumnWidth(columnIndex, (270 * 16));
                        // 行高
                        sheet.setDefaultRowHeight((short) (20 * 15));
                    }
                })
                .doWrite(ProductImportListener.dataList(productExportExcelList));

    }
}
