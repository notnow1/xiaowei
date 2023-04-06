package net.qixiaowei.system.manage.excel.post;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.qixiaowei.system.manage.service.basic.IPostService;

/**
* PostImportListener
* @author TANGMICHI
* @since 2023-02-02
*/
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PostImportListener extends AnalysisEventListener<PostExcel>{
        /**
        * 默认每隔3000条存储数据库
        */
        private int batchCount = 3000;
        /**
        * 缓存的数据列表
        */
        private List<PostExcel> list = new ArrayList<>();
        /**
         * 用户service
         */
        private final IPostService postService;
        /**
         * 下载模板 导入excel数据
         * @param selectMap
         * @param parentDepartmentExcelNames
         * @param officialRankSystemNames
         * @return
         */
        public static List<List<String>> importHead(Map<Integer, List<String>> selectMap, List<String> parentDepartmentExcelNames, List<String> officialRankSystemNames) {
                List<List<String>> list = new ArrayList<List<String>>();
                // 第一列
                List<String> head0 = new ArrayList<String>();
                head0.add("\"说明：\n" +
                        "1、带*为必填项\n" +
                        "2、岗位编码不可重复\n" +
                        "3、职级体系、岗位状态、适用组织为下拉选择，请勿填写其他内容\n" +
                        "4、职级上限、职级下限应在对应职级体系的职级范围内，若填写错误，则该行导入失败\n" +
                        "5、若一个岗位对应多个适用组织，应分行录入");
                head0.add("岗位编码*");
                // 第二列
                List<String> head1 = new ArrayList<String>();
                head1.add("\"说明：\n" +
                        "1、带*为必填项\n" +
                        "2、岗位编码不可重复\n" +
                        "3、职级体系、岗位状态、适用组织为下拉选择，请勿填写其他内容\n" +
                        "4、职级上限、职级下限应在对应职级体系的职级范围内，若填写错误，则该行导入失败\n" +
                        "5、若一个岗位对应多个适用组织，应分行录入");
                head1.add("岗位名称*");
                // 第三列
                List<String> head2 = new ArrayList<String>();
                head2.add("\"说明：\n" +
                        "1、带*为必填项\n" +
                        "2、岗位编码不可重复\n" +
                        "3、职级体系、岗位状态、适用组织为下拉选择，请勿填写其他内容\n" +
                        "4、职级上限、职级下限应在对应职级体系的职级范围内，若填写错误，则该行导入失败\n" +
                        "5、若一个岗位对应多个适用组织，应分行录入");
                head2.add("职级体系*");
                selectMap.put(2, officialRankSystemNames);

                // 第四列
                List<String> head3 = new ArrayList<String>();
                head3.add("\"说明：\n" +
                        "1、带*为必填项\n" +
                        "2、岗位编码不可重复\n" +
                        "3、职级体系、岗位状态、适用组织为下拉选择，请勿填写其他内容\n" +
                        "4、职级上限、职级下限应在对应职级体系的职级范围内，若填写错误，则该行导入失败\n" +
                        "5、若一个岗位对应多个适用组织，应分行录入");
                head3.add("职级上限*");
                // 第五列
                List<String> head4 = new ArrayList<String>();
                head4.add("\"说明：\n" +
                        "1、带*为必填项\n" +
                        "2、岗位编码不可重复\n" +
                        "3、职级体系、岗位状态、适用组织为下拉选择，请勿填写其他内容\n" +
                        "4、职级上限、职级下限应在对应职级体系的职级范围内，若填写错误，则该行导入失败\n" +
                        "5、若一个岗位对应多个适用组织，应分行录入");
                head4.add("职级下限*");
                // 第六列
                List<String> head5 = new ArrayList<String>();
                head5.add("\"说明：\n" +
                        "1、带*为必填项\n" +
                        "2、岗位编码不可重复\n" +
                        "3、职级体系、岗位状态、适用组织为下拉选择，请勿填写其他内容\n" +
                        "4、职级上限、职级下限应在对应职级体系的职级范围内，若填写错误，则该行导入失败\n" +
                        "5、若一个岗位对应多个适用组织，应分行录入");
                head5.add("岗位状态");
                selectMap.put(5, Arrays.asList("生效", "失效"));

                // 第七列
                List<String> head6 = new ArrayList<String>();
                head6.add("\"说明：\n" +
                        "1、带*为必填项\n" +
                        "2、岗位编码不可重复\n" +
                        "3、职级体系、岗位状态、适用组织为下拉选择，请勿填写其他内容\n" +
                        "4、职级上限、职级下限应在对应职级体系的职级范围内，若填写错误，则该行导入失败\n" +
                        "5、若一个岗位对应多个适用组织，应分行录入");
                head6.add("适用组织*");
                selectMap.put(6, parentDepartmentExcelNames);
                list.add(head0);
                list.add(head1);
                list.add(head2);
                list.add(head3);
                list.add(head4);
                list.add(head5);
                list.add(head6);
                return list;
        }

        @Override
        public void invoke(PostExcel data, AnalysisContext context) {
        list.add(data);
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
        // 存储完成清理list
        list.clear();
        }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 调用importer方法
        // 存储完成清理list
        list.clear();
        }
}


