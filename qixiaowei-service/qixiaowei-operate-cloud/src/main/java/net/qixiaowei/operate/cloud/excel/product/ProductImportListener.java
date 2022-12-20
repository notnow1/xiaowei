package net.qixiaowei.operate.cloud.excel.product;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.qixiaowei.integration.common.utils.StringUtils;
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
     * excel头
     *
     * @param selectMap
     * @param dictionaryTypeDTO
     * @param dictionaryLabels
     * @param productUnitDTOS
     * @return
     */
    public static List<List<String>> head(Map<Integer, List<String>> selectMap, DictionaryTypeDTO dictionaryTypeDTO, List<String> dictionaryLabels, List<ProductUnitDTO> productUnitDTOS) {
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
        head2.add("上级产品编码*");

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
        if (StringUtils.isNotEmpty(productUnitDTOS)){
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
        head4.add(dictionaryTypeDTO.getDictionaryName());
        selectMap.put(4, dictionaryLabels);
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
}


