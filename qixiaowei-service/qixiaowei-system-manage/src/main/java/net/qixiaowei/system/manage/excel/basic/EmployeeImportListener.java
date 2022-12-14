package net.qixiaowei.system.manage.excel.basic;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.excel.ExcelUtils;
import net.qixiaowei.system.manage.service.basic.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * EmployeeImportListener
 *
 * @author Chill
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmployeeImportListener extends AnalysisEventListener<Map<Integer, String>> {

    /**
     * 默认每隔3000条存储数据库
     */
    private int batchCount = 3000;

    /**
     * 用户service
     */
    private final IEmployeeService employeeService;
    /**
     * 缓存的数据列表
     */
    private List<EmployeeExcel> list = new ArrayList<>();
    /**
     * 解析数据(多sheet使用)
     * key是sheetName，value是相应sheet的解析数据
     */
    private final Map<String, List<Map<Integer, String>>> dataMap = new HashMap<>();

    /**
     * excle所有数据(单sheet使用)
     */
    private final List<Map<Integer, String>> listMap = new ArrayList<>();
    /**
     * 日志
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    public static List<List<String>> head(Map<Integer, List<String>> selectMap, List<String> parentDepartmentExcelNames, List<String> postNames) {
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head0.add("员工基本信息");
        head0.add("员工工号*");
        // 第二列
        List<String> head1 = new ArrayList<String>();
        head1.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head1.add("员工基本信息");
        head1.add("员工姓名*");
        // 第三列
        List<String> head2 = new ArrayList<String>();
        head2.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head2.add("员工基本信息");
        head2.add("用工关系状态*");

        // 第四列
        List<String> head3 = new ArrayList<String>();
        head3.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head3.add("员工基本信息");
        head3.add("性别");
        selectMap.put(3, Arrays.asList("男", "女"));
        // 第五列
        List<String> head4 = new ArrayList<String>();
        head4.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head4.add("员工基本信息");
        head4.add("身份证号码*");
        // 第六列
        List<String> head5 = new ArrayList<String>();
        head5.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head5.add("员工基本信息");
        head5.add("婚姻状况");
        selectMap.put(5, Arrays.asList("未婚", "已婚"));

        // 第七列
        List<String> head6 = new ArrayList<String>();
        head6.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head6.add("员工基本信息");
        head6.add("国籍");
        // 第八列
        List<String> head7 = new ArrayList<String>();
        head7.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head7.add("员工基本信息");
        head7.add("民族");


        // 第九列
        List<String> head8 = new ArrayList<String>();
        head8.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head8.add("员工基本信息");
        head8.add("户口所在地");
        // 第十列
        List<String> head9 = new ArrayList<String>();
        head9.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head9.add("员工基本信息");
        head9.add("参保地");
        // 第十一列
        List<String> head10 = new ArrayList<String>();
        head10.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head10.add("员工基本信息");
        head10.add("常住地");
        // 第十二列
        List<String> head11 = new ArrayList<String>();
        head11.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head11.add("员工基本信息");
        head11.add("入职日期*");
        // 第十三列
        List<String> head12 = new ArrayList<String>();
        head12.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head12.add("员工任职信息");
        head12.add("部门*");
        selectMap.put(12, parentDepartmentExcelNames);

        // 第十四列
        List<String> head13 = new ArrayList<String>();
        head13.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head13.add("员工任职信息");
        head13.add("岗位*");
        selectMap.put(13, postNames);
        // 第十五列
        List<String> head14 = new ArrayList<String>();
        head14.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head14.add("员工任职信息");
        head14.add("个人职级*");

        // 第十六列
        List<String> head15 = new ArrayList<String>();
        head15.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head15.add("员工任职信息");
        head15.add("基本工资");

        // 第十七列
        List<String> head16 = new ArrayList<String>();
        head16.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head16.add("员工联系信息");
        head16.add("手机*");

        // 第十八列
        List<String> head17 = new ArrayList<String>();
        head17.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head17.add("员工联系信息");
        head17.add("邮箱");
        // 第十九列
        List<String> head18 = new ArrayList<String>();
        head18.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head18.add("员工联系信息");
        head18.add("微信");
        // 第二十列
        List<String> head19 = new ArrayList<String>();
        head19.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head19.add("员工联系信息");
        head19.add("通信地址");
        // 第二十一列
        List<String> head20 = new ArrayList<String>();
        head20.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head20.add("员工联系信息");
        head20.add("详细通信地址");

        // 第二十二列
        List<String> head21 = new ArrayList<String>();
        head21.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head21.add("员工联系信息");
        head21.add("紧急联系人姓名");

        // 第二十三列
        List<String> head22 = new ArrayList<String>();
        head22.add("填写说明：\r\n" +
                "1、红色字体为必填字段；\r\n" +
                "2、若某一数据有误导致导入失败，所有数据将会导入失败；\r\n" +
                "3、员工工号、身份证号码有唯一性校验，若出现重复，可能会导致导入失败；\r\n" +
                "4、岗位需在该部门下所属岗位，否则将会导入失败；个人职级需在岗位职级上下限范围内，否则将会导入失败;\r\n" +
                "5、入职日期需按照YYYY/MM/DD格式进行录入，否则将会导入失败。");
        head22.add("员工联系信息");
        head22.add("紧急联系人电话*");


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
        list.add(head11);
        list.add(head12);
        list.add(head13);
        list.add(head14);
        list.add(head15);
        list.add(head16);
        list.add(head17);
        list.add(head18);
        list.add(head19);
        list.add(head20);
        list.add(head21);
        list.add(head22);
        return list;
    }

    /**
     * 封装导出数据
     *
     * @param employeeExcelList
     * @return
     */
    public static List dataList(List<EmployeeExcel> employeeExcelList) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        for (int i = 0; i < employeeExcelList.size(); i++) {
            List<Object> data = new ArrayList<Object>();
            ExcelUtils.packList(employeeExcelList.get(i), data);
            list.add(data);
        }
        return list;
    }


    @Override
    public void invoke(Map<Integer, String> map, AnalysisContext analysisContext) {
        listMap.add(map);
    }


    @SneakyThrows
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        logger.info("Excel解析完成");

        EmployeeExcel employeeExcel = new EmployeeExcel();
        ExcelUtils.mapToListModel(2, 0, listMap, employeeExcel, list);


        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
            // 调用importer方法
            employeeService.importEmployee(list);
            // 存储完成清理list
            list.clear();
        } else {
            // 调用importer方法
            employeeService.importEmployee(list);
            // 存储完成清理list
            list.clear();
        }
    }
}
