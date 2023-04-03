package net.qixiaowei.operate.cloud.excel.product;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.common.utils.excel.ExcelUtils;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDataDTO;
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationDataDTO;
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationParamDTO;
import net.qixiaowei.operate.cloud.api.dto.product.ProductUnitDTO;
import net.qixiaowei.operate.cloud.service.product.IProductService;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryTypeDTO;

/**
 * ProductImportListener
 *
 * @author TANGMICHI
 * @since 2022-12-19
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductImportListener extends AnalysisEventListener<ProductExcel> {
    /**
     * 默认每隔3000条存储数据库
     */
    private int batchCount = 3000;
    /**
     * 缓存的数据列表
     */
    private List<ProductExcel> list = new ArrayList<>();
    /**
     * 用户service
     */
    private final IProductService productService;

    /**
     * 下载模板 导入数据的excel
     *
     * @param selectMap
     * @param dictionaryTypeDTO
     * @param dictionaryLabels
     * @param productUnitDTOS
     * @return
     */
    public static List<List<String>> importHead(Map<Integer, List<String>> selectMap, DictionaryTypeDTO dictionaryTypeDTO, List<String> dictionaryLabels, List<ProductUnitDTO> productUnitDTOS) {
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("填写说明：\n" +
                "1、*为必填字段；\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\n" +
                "3、产品编码有唯一性校验，若出现重复，可能会导致导入失败；\n" +
                "4、上级产品编码若为空，则该产品视为一级层级;\n" +
                "5、产品规格信息中的参数名称与参数值成对出现，若需增加新参数，在后面继续填充参数名称列与参数值列即可；\n" +
                "6、若一个产品存在n个规格，则需要填充n行，这些行的产品基本信息相同，产品规格信息内容可不同；\n" +
                "7、产品编码录入时可使用英文字母以及数字，请勿使用中文，若使用中文会导致系统无法识别；\n" +
                "8、编辑导入模板时，若涉及到需要填充数字的字段，请注意单元格格式，避免以“0”作为开头的数字被省略掉“0”；\n" +
                "9、产品量纲、产品类别、是否上下架为下拉选择。");

        head0.add("产品基本信息");
        head0.add("产品编码*");
        // 第二列
        List<String> head1 = new ArrayList<String>();
        head1.add("填写说明：\n" +
                "1、*为必填字段；\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\n" +
                "3、产品编码有唯一性校验，若出现重复，可能会导致导入失败；\n" +
                "4、上级产品编码若为空，则该产品视为一级层级;\n" +
                "5、产品规格信息中的参数名称与参数值成对出现，若需增加新参数，在后面继续填充参数名称列与参数值列即可；\n" +
                "6、若一个产品存在n个规格，则需要填充n行，这些行的产品基本信息相同，产品规格信息内容可不同；\n" +
                "7、产品编码录入时可使用英文字母以及数字，请勿使用中文，若使用中文会导致系统无法识别；\n" +
                "8、编辑导入模板时，若涉及到需要填充数字的字段，请注意单元格格式，避免以“0”作为开头的数字被省略掉“0”；\n" +
                "9、产品量纲、产品类别、是否上下架为下拉选择。");
        head1.add("产品基本信息");
        head1.add("产品名称*");
        // 第三列
        List<String> head2 = new ArrayList<String>();
        head2.add("填写说明：\n" +
                "1、*为必填字段；\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\n" +
                "3、产品编码有唯一性校验，若出现重复，可能会导致导入失败；\n" +
                "4、上级产品编码若为空，则该产品视为一级层级;\n" +
                "5、产品规格信息中的参数名称与参数值成对出现，若需增加新参数，在后面继续填充参数名称列与参数值列即可；\n" +
                "6、若一个产品存在n个规格，则需要填充n行，这些行的产品基本信息相同，产品规格信息内容可不同；\n" +
                "7、产品编码录入时可使用英文字母以及数字，请勿使用中文，若使用中文会导致系统无法识别；\n" +
                "8、编辑导入模板时，若涉及到需要填充数字的字段，请注意单元格格式，避免以“0”作为开头的数字被省略掉“0”；\n" +
                "9、产品量纲、产品类别、是否上下架为下拉选择。");
        head2.add("产品基本信息");
        head2.add("上级产品编码");

        // 第四列
        List<String> head3 = new ArrayList<String>();
        head3.add("填写说明：\n" +
                "1、*为必填字段；\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\n" +
                "3、产品编码有唯一性校验，若出现重复，可能会导致导入失败；\n" +
                "4、上级产品编码若为空，则该产品视为一级层级;\n" +
                "5、产品规格信息中的参数名称与参数值成对出现，若需增加新参数，在后面继续填充参数名称列与参数值列即可；\n" +
                "6、若一个产品存在n个规格，则需要填充n行，这些行的产品基本信息相同，产品规格信息内容可不同；\n" +
                "7、产品编码录入时可使用英文字母以及数字，请勿使用中文，若使用中文会导致系统无法识别；\n" +
                "8、编辑导入模板时，若涉及到需要填充数字的字段，请注意单元格格式，避免以“0”作为开头的数字被省略掉“0”；\n" +
                "9、产品量纲、产品类别、是否上下架为下拉选择。");
        head3.add("产品基本信息");
        head3.add("产品量纲*");
        if (StringUtils.isNotEmpty(productUnitDTOS)) {
            selectMap.put(3, productUnitDTOS.stream().map(ProductUnitDTO::getProductUnitName).collect(Collectors.toList()));
        }

        // 第五列
        List<String> head4 = new ArrayList<String>();
        head4.add("填写说明：\n" +
                "1、*为必填字段；\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\n" +
                "3、产品编码有唯一性校验，若出现重复，可能会导致导入失败；\n" +
                "4、上级产品编码若为空，则该产品视为一级层级;\n" +
                "5、产品规格信息中的参数名称与参数值成对出现，若需增加新参数，在后面继续填充参数名称列与参数值列即可；\n" +
                "6、若一个产品存在n个规格，则需要填充n行，这些行的产品基本信息相同，产品规格信息内容可不同；\n" +
                "7、产品编码录入时可使用英文字母以及数字，请勿使用中文，若使用中文会导致系统无法识别；\n" +
                "8、编辑导入模板时，若涉及到需要填充数字的字段，请注意单元格格式，避免以“0”作为开头的数字被省略掉“0”；\n" +
                "9、产品量纲、产品类别、是否上下架为下拉选择。");
        head4.add("产品基本信息");
        if (StringUtils.isNotNull(dictionaryTypeDTO)){
            head4.add(dictionaryTypeDTO.getDictionaryName());

        }
        if (StringUtils.isNotEmpty(dictionaryLabels)){
            selectMap.put(4, dictionaryLabels);
        }
        // 第六列
        List<String> head5 = new ArrayList<String>();
        head5.add("填写说明：\n" +
                "1、*为必填字段；\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\n" +
                "3、产品编码有唯一性校验，若出现重复，可能会导致导入失败；\n" +
                "4、上级产品编码若为空，则该产品视为一级层级;\n" +
                "5、产品规格信息中的参数名称与参数值成对出现，若需增加新参数，在后面继续填充参数名称列与参数值列即可；\n" +
                "6、若一个产品存在n个规格，则需要填充n行，这些行的产品基本信息相同，产品规格信息内容可不同；\n" +
                "7、产品编码录入时可使用英文字母以及数字，请勿使用中文，若使用中文会导致系统无法识别；\n" +
                "8、编辑导入模板时，若涉及到需要填充数字的字段，请注意单元格格式，避免以“0”作为开头的数字被省略掉“0”；\n" +
                "9、产品量纲、产品类别、是否上下架为下拉选择。");
        head5.add("产品基本信息");
        head5.add("是否上下架");
        selectMap.put(5, Arrays.asList("上架", "下架"));

        // 第七列
        List<String> head6 = new ArrayList<String>();
        head6.add("填写说明：\n" +
                "1、*为必填字段；\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\n" +
                "3、产品编码有唯一性校验，若出现重复，可能会导致导入失败；\n" +
                "4、上级产品编码若为空，则该产品视为一级层级;\n" +
                "5、产品规格信息中的参数名称与参数值成对出现，若需增加新参数，在后面继续填充参数名称列与参数值列即可；\n" +
                "6、若一个产品存在n个规格，则需要填充n行，这些行的产品基本信息相同，产品规格信息内容可不同；\n" +
                "7、产品编码录入时可使用英文字母以及数字，请勿使用中文，若使用中文会导致系统无法识别；\n" +
                "8、编辑导入模板时，若涉及到需要填充数字的字段，请注意单元格格式，避免以“0”作为开头的数字被省略掉“0”；\n" +
                "9、产品量纲、产品类别、是否上下架为下拉选择。");
        head6.add("产品基本信息");
        head6.add("产品描述");
        // 第八列
        List<String> head7 = new ArrayList<String>();
        head7.add("填写说明：\n" +
                "1、*为必填字段；\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\n" +
                "3、产品编码有唯一性校验，若出现重复，可能会导致导入失败；\n" +
                "4、上级产品编码若为空，则该产品视为一级层级;\n" +
                "5、产品规格信息中的参数名称与参数值成对出现，若需增加新参数，在后面继续填充参数名称列与参数值列即可；\n" +
                "6、若一个产品存在n个规格，则需要填充n行，这些行的产品基本信息相同，产品规格信息内容可不同；\n" +
                "7、产品编码录入时可使用英文字母以及数字，请勿使用中文，若使用中文会导致系统无法识别；\n" +
                "8、编辑导入模板时，若涉及到需要填充数字的字段，请注意单元格格式，避免以“0”作为开头的数字被省略掉“0”；\n" +
                "9、产品量纲、产品类别、是否上下架为下拉选择。");
        head7.add("产品规格信息");
        head7.add("规格");


        // 第九列
        List<String> head8 = new ArrayList<String>();
        head8.add("填写说明：\n" +
                "1、*为必填字段；\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\n" +
                "3、产品编码有唯一性校验，若出现重复，可能会导致导入失败；\n" +
                "4、上级产品编码若为空，则该产品视为一级层级;\n" +
                "5、产品规格信息中的参数名称与参数值成对出现，若需增加新参数，在后面继续填充参数名称列与参数值列即可；\n" +
                "6、若一个产品存在n个规格，则需要填充n行，这些行的产品基本信息相同，产品规格信息内容可不同；\n" +
                "7、产品编码录入时可使用英文字母以及数字，请勿使用中文，若使用中文会导致系统无法识别；\n" +
                "8、编辑导入模板时，若涉及到需要填充数字的字段，请注意单元格格式，避免以“0”作为开头的数字被省略掉“0”；\n" +
                "9、产品量纲、产品类别、是否上下架为下拉选择。");
        head8.add("产品规格信息");
        head8.add("目录价（单位：元）");
        // 第十列
        List<String> head9 = new ArrayList<String>();
        head9.add("填写说明：\n" +
                "1、*为必填字段；\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\n" +
                "3、产品编码有唯一性校验，若出现重复，可能会导致导入失败；\n" +
                "4、上级产品编码若为空，则该产品视为一级层级;\n" +
                "5、产品规格信息中的参数名称与参数值成对出现，若需增加新参数，在后面继续填充参数名称列与参数值列即可；\n" +
                "6、若一个产品存在n个规格，则需要填充n行，这些行的产品基本信息相同，产品规格信息内容可不同；\n" +
                "7、产品编码录入时可使用英文字母以及数字，请勿使用中文，若使用中文会导致系统无法识别；\n" +
                "8、编辑导入模板时，若涉及到需要填充数字的字段，请注意单元格格式，避免以“0”作为开头的数字被省略掉“0”；\n" +
                "9、产品量纲、产品类别、是否上下架为下拉选择。");
        head9.add("产品规格信息");
        head9.add("参数名称");
        // 第十一列
        List<String> head10 = new ArrayList<String>();
        head10.add("填写说明：\n" +
                "1、*为必填字段；\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\n" +
                "3、产品编码有唯一性校验，若出现重复，可能会导致导入失败；\n" +
                "4、上级产品编码若为空，则该产品视为一级层级;\n" +
                "5、产品规格信息中的参数名称与参数值成对出现，若需增加新参数，在后面继续填充参数名称列与参数值列即可；\n" +
                "6、若一个产品存在n个规格，则需要填充n行，这些行的产品基本信息相同，产品规格信息内容可不同；\n" +
                "7、产品编码录入时可使用英文字母以及数字，请勿使用中文，若使用中文会导致系统无法识别；\n" +
                "8、编辑导入模板时，若涉及到需要填充数字的字段，请注意单元格格式，避免以“0”作为开头的数字被省略掉“0”；\n" +
                "9、产品量纲、产品类别、是否上下架为下拉选择。");
        head10.add("产品规格信息");
        head10.add("参数值");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        list.add(head4);
        list.add(head5);
        list.add(head6);
        list.add(head7);
        list.add(head8);
        list.add(head9);
        list.add(head10);
        return list;
    }

    /**
     *导出数据的excel
     *
     * @param dictionaryTypeDTO
     * @param dictionaryLabels
     * @param productUnitDTOS
     * @return
     */
    public static List<List<String>> exportHead(DictionaryTypeDTO dictionaryTypeDTO, List<String> dictionaryLabels, List<ProductUnitDTO> productUnitDTOS) {
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("产品编码");
        // 第二列
        List<String> head1 = new ArrayList<String>();
        head1.add("产品名称");
        // 第三列
        List<String> head2 = new ArrayList<String>();
        head2.add("上级产品编码");

        // 第四列
        List<String> head3 = new ArrayList<String>();
        head3.add("产品量纲");


        // 第五列
        List<String> head4 = new ArrayList<String>();
        if (StringUtils.isNotNull(dictionaryTypeDTO)){
            head4.add(dictionaryTypeDTO.getDictionaryName());
        }

        // 第六列
        List<String> head5 = new ArrayList<String>();
        head5.add("是否上下架");


        // 第七列
        List<String> head6 = new ArrayList<String>();
        head6.add("产品描述");
        // 第八列
        List<String> head7 = new ArrayList<String>();
        head7.add("规格");


        // 第九列
        List<String> head8 = new ArrayList<String>();
        head8.add("目录价（单位：元）");
        // 第十列
        List<String> head9 = new ArrayList<String>();
        head9.add("参数名称");
        // 第十一列
        List<String> head10 = new ArrayList<String>();
        head10.add("参数值");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        list.add(head4);
        list.add(head5);
        list.add(head6);
        list.add(head7);
        list.add(head8);
        list.add(head9);
        list.add(head10);
        return list;
    }
    @SneakyThrows
    public static List<List<Object>> dataList(List<ProductExportExcel> productExportExcelList) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        ProductExportExcel productExportExcel = new ProductExportExcel();
        ProductExportExcel productExportExcel1 = productExportExcel.getClass().newInstance();
        Field[] fields = productExportExcel1.getClass().getDeclaredFields();

        if (StringUtils.isNotEmpty(productExportExcelList)) {
            for (ProductExportExcel exportExcel : productExportExcelList) {
                List<Object> data = new ArrayList<Object>();
                for (int i = 0; i < fields.length; i++) {
                    //产品规格参数集合
                    List<String> productSpecificationParamList = exportExcel.getProductSpecificationParamList();
                    //产品规格数据集合
                    List<String> productDataList = exportExcel.getProductDataList();
                    if (i == 0) {
                        data.add(exportExcel.getProductCode());
                    } else if (i == 1) {
                        data.add(exportExcel.getProductName());
                    } else if (i == 2) {
                        data.add(exportExcel.getParentProductCode());
                    } else if (i == 3) {
                        data.add(exportExcel.getProductUnitName());
                    } else if (i == 4) {
                        data.add(exportExcel.getProductCategoryName());
                    } else if (i == 5) {
                        data.add(exportExcel.getListingFlag());
                    }else if (i == 6) {
                        data.add(exportExcel.getProductDescription());
                    }
                    else if (i == 7) {
                        data.add(exportExcel.getSpecificationName());
                    } else if (i == 8) {
                        data.add(exportExcel.getListPrice());
                    }
                    if (i == 9) {
                        if (StringUtils.isNotEmpty(productDataList)&&productDataList.get(0) != null) {
                            int count = 0;
                            for (int i1 = 0; i1 < (productDataList.size()*2); i1++) {
                                if ((i1 + 1) % 2 == 0) {
                                    data.add(productDataList.get(count));
                                    count++;
                                } else {
                                    if (StringUtils.isNotEmpty(productSpecificationParamList)&&productSpecificationParamList.get(0) != null) {
                                        data.add(productSpecificationParamList.get(count));
                                    }
                                }
                            }
                        }
                    }
                    if (i == 10) {
                        continue;
                    }
                }
                list.add(data);
            }


        }
        return list;
    }

    @Override
    public void invoke(ProductExcel data, AnalysisContext context) {
        list.add(data);
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
            // 调用importer方法
            productService.importProduct(list);
            // 存储完成清理list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 调用importer方法
        productService.importProduct(list);
        // 存储完成清理list
        list.clear();
    }

    /**
     * @param row  行
     * @param line 列
     * @param maps 数据
     * @param list 实体类集合
     */
    public static void mapToListModel(int row, int line, List<Map<Integer, String>> maps, List<ProductExcel> list) {
        for (int i = 0; i < maps.size(); i++) {
            List<Object> list2 = new ArrayList<>();
            //产品主表
            ProductExcel productExcel = new ProductExcel();
            //产品参数名称集合
            List<ProductSpecificationParamDTO> productSpecificationParamDTOList = new ArrayList<>();
            //产品规格数据表集合
            List<ProductDataDTO> productDataDTOList = new ArrayList<>();
            //产品数据
            ProductDataDTO productDataDTO = new ProductDataDTO();
            //参数值集合
            List<ProductSpecificationDataDTO> productSpecificationDataDTOList = new ArrayList<>();
            //从第几行开始
            if (i >= row) {
                Map<Integer, String> map = maps.get(i);
                map.forEach((key, value) -> {
                    //从第几列开始
                    if (key >= line && key < 7) {
                        String s = map.get(key);
                        if (StringUtils.isBlank(s)) {
                            list2.add("");
                        } else {
                            list2.add(s.trim());
                        }
                    }
                    if (key >= 7) {
                        if (key == 7) {
                            if (StringUtils.isNotBlank(value)){
                                productDataDTO.setSpecificationName(value.trim());
                            }else {
                                productDataDTO.setSpecificationName(value);
                            }

                        }
                        if (key == 8) {
                            if (StringUtils.isNotBlank(value)){
                                productDataDTO.setListPrice(new BigDecimal(value.trim()));
                            }else {
                                if (StringUtils.isNotBlank(value)){
                                    productDataDTO.setListPrice(new BigDecimal(value));
                                }else {
                                    productDataDTO.setListPrice(new BigDecimal("0"));
                                }

                            }

                        }
                        if (key >= 9) {

                            if (key % 2 == 0) {
                                //产品规格数据表
                                ProductSpecificationDataDTO productSpecificationDataDTO = new ProductSpecificationDataDTO();
                                if (StringUtils.isNotBlank(value)){
                                    //参数值
                                    productSpecificationDataDTO.setValue(value.trim());
                                }else {
                                    //参数值
                                    productSpecificationDataDTO.setValue(value);
                                }

                                productSpecificationDataDTOList.add(productSpecificationDataDTO);
                                productDataDTO.setProductSpecificationDataDTOList(productSpecificationDataDTOList);
                            } else {
                                //产品规格参数
                                ProductSpecificationParamDTO productSpecificationParamDTO = new ProductSpecificationParamDTO();
                                if (StringUtils.isNotBlank(value)){
                                    productSpecificationParamDTO.setSpecificationParamName(value.trim());
                                }else {
                                    productSpecificationParamDTO.setSpecificationParamName(value);
                                }

                                productSpecificationParamDTOList.add(productSpecificationParamDTO);
                            }
                        }
                    }

                });
                Map<Integer, String> map2 = maps.get(i);
                map2.forEach((key, value) -> {
                    if (key == 8) {
                        if (StringUtils.isNotEmpty(productSpecificationParamDTOList)) {
                            list2.add(productSpecificationParamDTOList);
                        } else {
                            List<ProductSpecificationParamDTO> productSpecificationParamDTOS = new ArrayList<>();
                            list2.add(productSpecificationParamDTOS);
                        }

                    }
                    if (key == 9) {
                        productDataDTOList.add(productDataDTO);
                        list2.add(productDataDTOList);
                    }

                });
                try {
                    ProductExcel productExcel1 = ProductImportListener.listToModel(list2, productExcel);
                    //添加list
                    list.add(productExcel1);
                } catch (Exception e) {
                    throw new ServiceException("模板格式不正确！");
                }
            }


        }


    }

    /**
     * 将list转换为实体类(使用此方法字段属性为BigDecimal时，会报错，请改为其他属性，插入数据库时需修改成对应的属性，此方法的实体类仅作为Excel导入导出使用！)
     *
     * @param list
     * @return
     * @throws Exception
     */
    public static ProductExcel listToModel(List<Object> list, ProductExcel productExcel) throws Exception {
        ProductExcel productExcel1 = productExcel.getClass().newInstance();

        Field[] fields = productExcel1.getClass().getDeclaredFields();
        if (list.size() != fields.length) {
            return productExcel1;
        }
        for (int k = 0, len = fields.length; k < len; k++) {
            if (k == 0) {
                productExcel1.setProductCode(list.get(k).toString());
            }
            if (k == 1) {
                productExcel1.setProductName(list.get(k).toString());
            }
            if (k == 2) {
                productExcel1.setParentProductCode(list.get(k).toString());
            }
            if (k == 3) {
                productExcel1.setProductUnitName(list.get(k).toString());
            }
            if (k == 4) {
                productExcel1.setProductCategoryName(list.get(k).toString());
            }
            if (k == 5) {
                productExcel1.setListingFlag(list.get(k).toString());
            }
            if (k == 6) {
                productExcel1.setProductDescription(list.get(k).toString());
            }
            if (k == 7) {
                List<ProductSpecificationParamDTO> productSpecificationParamDTOS = ProductImportListener.listToModel2((List<Object>) list.get(k), new ProductSpecificationParamDTO());

                productExcel1.setProductSpecificationParamDTOList(productSpecificationParamDTOS);
            }
            if (k == 8) {
                List<ProductDataDTO> productDataDTOS = ProductImportListener.listToModel2((List<Object>) list.get(k), new ProductDataDTO());
                productExcel1.setProductDataDTOList(productDataDTOS);
            }
        }
        return productExcel1;
    }

    /**
     * @param list
     * @return
     * @throws Exception
     */
    public static <T> List<T> listToModel2(List<Object> list, T t) throws Exception {
        List<T> tList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            T t1 = (T) list.get(i);
            T t2 = (T) t.getClass().newInstance();
            BeanUtils.copyProperties(t1, t2);
            tList.add(t2);
        }
        return tList;
    }
}


