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
public class EmployeeImportListener extends AnalysisEventListener<EmployeeExcel> {

    /**
     * 默认每隔3000条存储数据库
     */
    private int batchCount = 3000;

    /**
     * 用户service
     */
    private  IEmployeeService employeeService;
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
    /**
     * 下载模板 导入excel数据
     *
     * @param selectMap
     * @param parentDepartmentExcelNames
     * @param postNames
     * @param errorExcelId
     * @return
     */
    public static List<List<String>> importHead(Map<Integer, List<String>> selectMap, List<String> parentDepartmentExcelNames, List<String> postNames, String errorExcelId) {
        List<List<String>> list = new ArrayList<List<String>>();
        //错误数据列
        if (StringUtils.isNotBlank(errorExcelId)){
            // 第一列
            List<String> head0 = new ArrayList<String>();
            head0.add("说明：\n" +
                    "1、带*为必填项\n" +
                    "2、员工工号、身份证号码不可重复\n" +
                    "3、户口所在地、参保地应按照XX省XX市录入\n" +
                    "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                    "5、个人职级应在岗位职级上下限范围内");
            head0.add("工号*");
            list.add(head0);
        }
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head0.add("工号*");
        // 第二列
        List<String> head1 = new ArrayList<String>();
        head1.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head1.add("姓名*");
        // 第三列
        List<String> head2 = new ArrayList<String>();
        head2.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head2.add("用工关系状态*");
        selectMap.put(2, Arrays.asList("在职", "离职"));

        // 第四列
        List<String> head3 = new ArrayList<String>();
        head3.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head3.add("性别");
        selectMap.put(3, Arrays.asList("男", "女"));
        // 第五列
        List<String> head4 = new ArrayList<String>();
        head4.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head4.add("身份证号码*");
        // 第六列
        List<String> head5 = new ArrayList<String>();
        head5.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head5.add("婚姻状况");
        selectMap.put(5, Arrays.asList("未婚", "已婚"));

        // 第七列
        List<String> head6 = new ArrayList<String>();
        head6.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head6.add("国籍");
        // 第八列
        List<String> head7 = new ArrayList<String>();
        head7.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head7.add("民族");


        // 第九列
        List<String> head8 = new ArrayList<String>();
        head8.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head8.add("户口所在地");
        // 第十列
        List<String> head9 = new ArrayList<String>();
        head9.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head9.add("参保地");
        // 第十一列
        List<String> head10 = new ArrayList<String>();
        head10.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head10.add("常住地");
        // 第十二列
        List<String> head11 = new ArrayList<String>();
        head11.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head11.add("入职时间*");
        // 第十二列
        List<String> head25 = new ArrayList<String>();
        head25.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head25.add("离职时间");
        // 第十三列
        List<String> head12 = new ArrayList<String>();
        head12.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head12.add("部门*");
        selectMap.put(13, parentDepartmentExcelNames);

        // 第十四列
        List<String> head13 = new ArrayList<String>();
        head13.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head13.add("岗位*");
        selectMap.put(14, postNames);
        // 第十五列
        List<String> head14 = new ArrayList<String>();
        head14.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head14.add("个人职级*");

        // 第十六列
        List<String> head15 = new ArrayList<String>();
        head15.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head15.add("基本工资");

        // 第十七列
        List<String> head16 = new ArrayList<String>();
        head16.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head16.add("手机*");

        // 第十八列
        List<String> head17 = new ArrayList<String>();
        head17.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head17.add("邮箱");
        // 第十九列
        List<String> head18 = new ArrayList<String>();
        head18.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head18.add("微信");
        // 第二十列
        List<String> head19 = new ArrayList<String>();
        head19.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head19.add("通信地址");
        // 第二十一列
        List<String> head20 = new ArrayList<String>();
        head20.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head20.add("详细通信地址");

        // 第二十二列
        List<String> head21 = new ArrayList<String>();
        head21.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
        head21.add("紧急联系人姓名");

        // 第二十三列
        List<String> head22 = new ArrayList<String>();

        head22.add("说明：\n" +
                "1、带*为必填项\n" +
                "2、员工工号、身份证号码不可重复\n" +
                "3、户口所在地、参保地应按照XX省XX市录入\n" +
                "4、常住地、通信地址应按照XX省XX市XX区（县）录入\n" +
                "5、个人职级应在岗位职级上下限范围内");
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
        list.add(head25);
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
     * 导出excel数据
     *
     * @param parentDepartmentExcelNames
     * @param postNames
     * @return
     */
    public static List<List<String>> exportHead(List<String> parentDepartmentExcelNames, List<String> postNames) {
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("工号");
        // 第二列
        List<String> head1 = new ArrayList<String>();
        head1.add("姓名");
        // 第三列
        List<String> head2 = new ArrayList<String>();
        head2.add("用工关系状态");


        // 第四列
        List<String> head3 = new ArrayList<String>();
        head3.add("性别");

        // 第五列
        List<String> head4 = new ArrayList<String>();
        head4.add("身份证号码");
        // 第六列
        List<String> head5 = new ArrayList<String>();
        head5.add("婚姻状况");


        // 第七列
        List<String> head6 = new ArrayList<String>();
        head6.add("国籍");
        // 第八列
        List<String> head7 = new ArrayList<String>();
        head7.add("民族");


        // 第九列
        List<String> head8 = new ArrayList<String>();
        head8.add("户口所在地");
        // 第十列
        List<String> head9 = new ArrayList<String>();
        head9.add("参保地");
        // 第十一列
        List<String> head10 = new ArrayList<String>();
        head10.add("常住地");
        // 第十二列
        List<String> head11 = new ArrayList<String>();
        head11.add("入职时间");
        // 第十二列
        List<String> head23 = new ArrayList<String>();
        head23.add("离职时间");
        // 第十三列
        List<String> head12 = new ArrayList<String>();
        head12.add("部门");


        // 第十四列
        List<String> head13 = new ArrayList<String>();
        head13.add("岗位");

        // 第十五列
        List<String> head14 = new ArrayList<String>();
        head14.add("个人职级");

        // 第十六列
        List<String> head15 = new ArrayList<String>();
        head15.add("基本工资");

        // 第十七列
        List<String> head16 = new ArrayList<String>();
        head16.add("手机");

        // 第十八列
        List<String> head17 = new ArrayList<String>();
        head17.add("邮箱");
        // 第十九列
        List<String> head18 = new ArrayList<String>();
        head18.add("微信");
        // 第二十列
        List<String> head19 = new ArrayList<String>();
        head19.add("通信地址");
        // 第二十一列
        List<String> head20 = new ArrayList<String>();
        head20.add("详细通信地址");

        // 第二十二列
        List<String> head21 = new ArrayList<String>();
        head21.add("紧急联系人姓名");

        // 第二十三列
        List<String> head22 = new ArrayList<String>();
        head22.add("紧急联系人电话");


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
        list.add(head23);
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
     * @param errorExcelId
     * @return
     */
    public static List dataList(List<EmployeeExcel> employeeExcelList, String errorExcelId) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        for (int i = 0; i < employeeExcelList.size(); i++) {
            List<Object> data = new ArrayList<Object>();
            ExcelUtils.packList(employeeExcelList.get(i), data, errorExcelId);
            list.add(data);
        }
        return list;
    }

    /**
     * 封装导出模板数据
     *
     * @param errorExcelId
     * @param employeeExcelList
     * @return
     */
    public static List dataTemplateList(String errorExcelId, List<EmployeeExcel> employeeExcelList) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        if (StringUtils.isNotBlank(errorExcelId)){
            for (int i = 0; i < employeeExcelList.size(); i++) {
                List<Object> data = new ArrayList<Object>();
                ExcelUtils.packList(employeeExcelList.get(i), data, errorExcelId);
                list.add(data);
            }
        }
        return list;
    }
    @Override
    public void invoke(EmployeeExcel excel, AnalysisContext analysisContext) {
        list.add(excel);
    }

    @SneakyThrows
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        logger.info("Excel解析完成");

        EmployeeExcel employeeExcel = new EmployeeExcel();
        ExcelUtils.mapToListModel(1, 0, listMap, employeeExcel, list, true);


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
