package net.qixiaowei.system.manage.excel.basic;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
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


    public static List<List<String>> head(Map<Integer, List<String>> selectMap) {
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("");
        head0.add("*为必填项");
        head0.add("数据要求（本行不填充数据，仅作说明）：");
        // 第二列
        List<String> head1 = new ArrayList<String>();
        head1.add("员工基本信息");
        head1.add("工号*");
        head1.add("文本录入，本文档中所有字段都需要注意单元格格式，如果首位为0，避免0被删除，唯一性校验，如果重复则导入失败");
        // 第三列
        List<String> head2 = new ArrayList<String>();
        head2.add("员工基本信息");
        head2.add("姓名*");
        head2.add("文本录入");
        // 第四列
        List<String> head3 = new ArrayList<String>();
        head3.add("员工基本信息");
        head3.add("用工关系状态*");
        head3.add("下拉选择，若不填写，系统默认为在职，枚举值：在职；离职");
        selectMap.put(3,Arrays.asList("在职","离职"));
        // 第五列
        List<String> head4 = new ArrayList<String>();
        head4.add("员工基本信息");
        head4.add("性别");
        head4.add("下拉选择，枚举值：男；女");
        selectMap.put(4,Arrays.asList("男","女"));
        // 第六列
        List<String> head5 = new ArrayList<String>();
        head5.add("员工基本信息");
        head5.add("身份证号码*");
        head5.add("文本录入，唯一性校验，如果重复则导入失败*");
        // 第七列
        List<String> head6 = new ArrayList<String>();
        head6.add("员工基本信息");
        head6.add("出生日期");
        head6.add("文本录入，可以不填写，系统根据身份证号码自动带出，格式：YYYY/MM/DD");
        // 第八列
        List<String> head7 = new ArrayList<String>();
        head7.add("员工基本信息");
        head7.add("婚姻状况");
        head7.add("下拉选择，枚举值：未婚；已婚");
        selectMap.put(7,Arrays.asList("未婚","已婚"));
        // 第九列
        List<String> head8 = new ArrayList<String>();
        head8.add("员工基本信息");
        head8.add("国籍");
        head8.add("文本录入，若不填写，系统默认为中国");

        // 第十列
        List<String> head9 = new ArrayList<String>();
        head9.add("员工基本信息");
        head9.add("民族");
        head9.add("文本录入，若不填写，系统默认为汉族");

        // 第十一列
        List<String> head10 = new ArrayList<String>();
        head10.add("员工基本信息");
        head10.add("户口所在地");
        head10.add("文本录入，XX省XX市");
        // 第十二列
        List<String> head11 = new ArrayList<String>();
        head11.add("员工基本信息");
        head11.add("参保地");
        head11.add("文本录入，XX省XX市");
        // 第十三列
        List<String> head12 = new ArrayList<String>();
        head12.add("员工基本信息");
        head12.add("常住地");
        head12.add("文本录入，XX省XX市");
        // 第十四列
        List<String> head13 = new ArrayList<String>();
        head13.add("员工基本信息");
        head13.add("入职日期*");
        head13.add("文本录入，格式：YYYY/MM/DD");
        // 第十五列
        List<String> head14 = new ArrayList<String>();
        head14.add("员工基本信息");
        head14.add("离职日期");
        head14.add("文本录入，用工关系状态为离职时，该字段才生效，格式：YYYY/MM/DD");

        // 第十六列
        List<String> head15 = new ArrayList<String>();
        head15.add("员工任职信息");
        head15.add("部门编码*");
        head15.add("文本录入");
        // 第十七列
        List<String> head16 = new ArrayList<String>();
        head16.add("员工任职信息");
        head16.add("岗位编码*");
        head16.add("文本录入");

        // 第十八列
        List<String> head17 = new ArrayList<String>();
        head17.add("员工任职信息");
        head17.add("个人职级");
        head17.add("文本录入");

        // 第十九列
        List<String> head18 = new ArrayList<String>();
        head18.add("员工任职信息");
        head18.add("基本工资");
        head18.add("文本录入，金额保留两位小数");
        // 第二十列
        List<String> head19 = new ArrayList<String>();
        head19.add("员工联系信息");
        head19.add("手机*");
        head19.add("文本录入");

        // 第二十一列
        List<String> head20 = new ArrayList<String>();
        head20.add("员工联系信息");
        head20.add("邮箱");
        head20.add("文本录入");

        // 第二十二列
        List<String> head21 = new ArrayList<String>();
        head21.add("员工联系信息");
        head21.add("微信");
        head21.add("文本录入");
        // 第二十三列
        List<String> head22 = new ArrayList<String>();
        head22.add("员工联系信息");
        head22.add("通信地址");
        head22.add("文本录入，XX省XX市XX区（县）");

        // 第二十四列
        List<String> head23 = new ArrayList<String>();
        head23.add("员工联系信息");
        head23.add("紧急联系人姓名");
        head23.add("文本录入");

        // 第二十五列
        List<String> head24 = new ArrayList<String>();
        head24.add("员工联系信息");
        head24.add("紧急联系人电话");
        head24.add("文本录入");

        // 第二十六列
        List<String> head25 = new ArrayList<String>();
        head25.add("员工联系信息");
        head25.add("详细通信地址");
        head25.add("文本录入");

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
        list.add(head23);
        list.add(head24);
        list.add(head25);
        return list;
    }

    /**
     * 封装导出数据
     * @param employeeExcelList
     * @return
     */
    public static List dataList(List<EmployeeExcel> employeeExcelList) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        for (int i = 0; i < (Math.max(employeeExcelList.size(), 7)); i++) {
            List<Object> data = new ArrayList<Object>();
            String s = EmployeeImportListener.packData(i);
            data.add(s);
            if (i<employeeExcelList.size()){

                ExcelUtils.packList(employeeExcelList.get(i),data);
            }
            list.add(data);
        }
        return list;
    }
    /**
     * 封装导出模板数据
     * @return
     */
    public static List templateData() {
        List<List<Object>> list = new ArrayList<List<Object>>();
        for (int i = 0; i < 7; i++) {
            List<Object> data = new ArrayList<Object>();
            String s = EmployeeImportListener.packData(i);
            data.add(s);
            list.add(data);
        }
        return list;
    }

    /**
     * 封装头数据
     *
     * @param i
     * @return
     */
    private static String packData(int i) {
        if (i == 0) {
            return "填写示例：";
        } else if (i == 1) {
            return "注意事项：";
        } else if (i == 2) {
            return "1、对于有唯一性校验的字段，如果导入值重复，则该字段导入失败";
        } else if (i == 3) {
            return "2、对于必录字段，如果导入失败，则整条数据导入失败";
        } else if (i == 4) {
            return "3、对于非必录字段，如果导入失败，则整条数据导入成功，该字段为空";
        } else if (i == 5) {
            return "4、对于录入的字段，用户的填充值需要与系统值匹配";
        }
        return null;
    }

    @Override
    public void invoke(Map<Integer, String> map, AnalysisContext analysisContext) {
        listMap.add(map);
    }



    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        logger.info("Excel解析完成");
        EmployeeExcel employeeExcel = new EmployeeExcel();
        ExcelUtils.mapToListModel(3,1,listMap,employeeExcel,list);
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
            // 调用importer方法
            employeeService.importEmployee(list);
            // 存储完成清理list
            list.clear();
        }
        //防止重复执行
        if (StringUtils.isNotEmpty(list)){
            // 调用importer方法
            employeeService.importEmployee(list);
            // 存储完成清理list
            list.clear();
        }
    }
}
