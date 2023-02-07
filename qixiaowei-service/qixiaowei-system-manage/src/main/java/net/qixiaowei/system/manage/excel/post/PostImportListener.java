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
                head0.add("说明：\n" +
                        "1、带有红色星号*的列为必填项，若不填写，则该行数据导入失败。\n" +
                        "2、职级体系为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败。\n" +
                        "3、岗位职级下限、岗位职级上限需填写在对应职级体系的职级范围区间内，若未填写，则默认按照所选择职级体系的最低/最高职级范围带出，若填写的值不在职级体系的职级范围区间内，则该行数据导入失败。\n" +
                        "4、岗位状态为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败，若未填写，则默认为“生效”状态。\n" +
                        "5、适用组织为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败。\n" +
                        "6、若一个岗位有多个适用组织，则每个组织均需填充一行数据。\n" +
                        "7、若岗位编码重复，系统视为重复数据，本次导入失败。");
                head0.add("岗位编码*");
                // 第二列
                List<String> head1 = new ArrayList<String>();
                head1.add("说明：\n" +
                        "1、带有红色星号*的列为必填项，若不填写，则该行数据导入失败。\n" +
                        "2、职级体系为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败。\n" +
                        "3、岗位职级下限、岗位职级上限需填写在对应职级体系的职级范围区间内，若未填写，则默认按照所选择职级体系的最低/最高职级范围带出，若填写的值不在职级体系的职级范围区间内，则该行数据导入失败。\n" +
                        "4、岗位状态为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败，若未填写，则默认为“生效”状态。\n" +
                        "5、适用组织为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败。\n" +
                        "6、若一个岗位有多个适用组织，则每个组织均需填充一行数据。\n" +
                        "7、若岗位编码重复，系统视为重复数据，本次导入失败。");
                head1.add("岗位名称*");
                // 第三列
                List<String> head2 = new ArrayList<String>();
                head2.add("说明：\n" +
                        "1、带有红色星号*的列为必填项，若不填写，则该行数据导入失败。\n" +
                        "2、职级体系为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败。\n" +
                        "3、岗位职级下限、岗位职级上限需填写在对应职级体系的职级范围区间内，若未填写，则默认按照所选择职级体系的最低/最高职级范围带出，若填写的值不在职级体系的职级范围区间内，则该行数据导入失败。\n" +
                        "4、岗位状态为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败，若未填写，则默认为“生效”状态。\n" +
                        "5、适用组织为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败。\n" +
                        "6、若一个岗位有多个适用组织，则每个组织均需填充一行数据。\n" +
                        "7、若岗位编码重复，系统视为重复数据，本次导入失败。");
                head2.add("职级体系*");
                selectMap.put(2, officialRankSystemNames);

                // 第四列
                List<String> head3 = new ArrayList<String>();
                head3.add("说明：\n" +
                        "1、带有红色星号*的列为必填项，若不填写，则该行数据导入失败。\n" +
                        "2、职级体系为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败。\n" +
                        "3、岗位职级下限、岗位职级上限需填写在对应职级体系的职级范围区间内，若未填写，则默认按照所选择职级体系的最低/最高职级范围带出，若填写的值不在职级体系的职级范围区间内，则该行数据导入失败。\n" +
                        "4、岗位状态为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败，若未填写，则默认为“生效”状态。\n" +
                        "5、适用组织为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败。\n" +
                        "6、若一个岗位有多个适用组织，则每个组织均需填充一行数据。\n" +
                        "7、若岗位编码重复，系统视为重复数据，本次导入失败。");
                head3.add("岗位职级下限");
                // 第五列
                List<String> head4 = new ArrayList<String>();
                head4.add("说明：\n" +
                        "1、带有红色星号*的列为必填项，若不填写，则该行数据导入失败。\n" +
                        "2、职级体系为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败。\n" +
                        "3、岗位职级下限、岗位职级上限需填写在对应职级体系的职级范围区间内，若未填写，则默认按照所选择职级体系的最低/最高职级范围带出，若填写的值不在职级体系的职级范围区间内，则该行数据导入失败。\n" +
                        "4、岗位状态为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败，若未填写，则默认为“生效”状态。\n" +
                        "5、适用组织为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败。\n" +
                        "6、若一个岗位有多个适用组织，则每个组织均需填充一行数据。\n" +
                        "7、若岗位编码重复，系统视为重复数据，本次导入失败。");
                head4.add("岗位职级上限");
                // 第六列
                List<String> head5 = new ArrayList<String>();
                head5.add("说明：\n" +
                        "1、带有红色星号*的列为必填项，若不填写，则该行数据导入失败。\n" +
                        "2、职级体系为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败。\n" +
                        "3、岗位职级下限、岗位职级上限需填写在对应职级体系的职级范围区间内，若未填写，则默认按照所选择职级体系的最低/最高职级范围带出，若填写的值不在职级体系的职级范围区间内，则该行数据导入失败。\n" +
                        "4、岗位状态为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败，若未填写，则默认为“生效”状态。\n" +
                        "5、适用组织为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败。\n" +
                        "6、若一个岗位有多个适用组织，则每个组织均需填充一行数据。\n" +
                        "7、若岗位编码重复，系统视为重复数据，本次导入失败。");
                head5.add("岗位状态");
                selectMap.put(5, Arrays.asList("生效", "失效"));

                // 第七列
                List<String> head6 = new ArrayList<String>();
                head6.add("说明：\n" +
                        "1、带有红色星号*的列为必填项，若不填写，则该行数据导入失败。\n" +
                        "2、职级体系为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败。\n" +
                        "3、岗位职级下限、岗位职级上限需填写在对应职级体系的职级范围区间内，若未填写，则默认按照所选择职级体系的最低/最高职级范围带出，若填写的值不在职级体系的职级范围区间内，则该行数据导入失败。\n" +
                        "4、岗位状态为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败，若未填写，则默认为“生效”状态。\n" +
                        "5、适用组织为下拉选择，请勿填写其他内容或将其他内容粘贴至此，若填写其他系统内不存在的数据，会导致该行数据导入失败。\n" +
                        "6、若一个岗位有多个适用组织，则每个组织均需填充一行数据。\n" +
                        "7、若岗位编码重复，系统视为重复数据，本次导入失败。");
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


