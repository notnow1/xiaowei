package net.qixiaowei.operate.cloud.excel.targetManager;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.qixiaowei.integration.common.text.Convert;
import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.excel.ExcelUtils;
import net.qixiaowei.operate.cloud.api.dto.targetManager.DecomposeDetailCyclesDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeService;

/**
 * TargetDecomposeImportListener
 *
 * @author TANGMICHI
 * @since 2022-10-27
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TargetDecomposeImportListener extends AnalysisEventListener<Map<Integer, String>> {
    /**
     * 默认每隔3000条存储数据库
     */
    private int batchCount = 3000;
    /**
     * 缓存的数据列表
     */
    private List<TargetDecomposeExcel> list = new ArrayList<>();
    /**
     * 用户service
     */
    private final ITargetDecomposeService targetDecomposeService;
    /**
     * 解析数据(多sheet使用)
     * key是sheetName，value是相应sheet的解析数据
     */
    private final Map<String, List<Map<Integer, String>>> dataMap = new HashMap<>();

    /**
     * excle所有数据(单sheet使用)
     */
    private final List<Map<Integer, String>> listMap = new ArrayList<>();

    public static List<List<String>> headTemplate(TargetDecomposeDTO targetDecomposeDTO) {
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
        // 第一列
        List<String> headNull = new ArrayList<String>();
        head0.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
        headNull.add("");
        list.add(head0);
        list.add(headNull);
        List<Map<String, String>> fileNameList = targetDecomposeDTO.getFileNameList();
        for (Map<String, String> stringStringMap : fileNameList) {
            // 动态列
            List<String> head1 = new ArrayList<String>();
            head1.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
            head1.add("分解维度");
            head1.add(stringStringMap.get("label"));
            list.add(head1);
        }
        if (1 == targetDecomposeDTO.getTimeDimension()) {

            // 动态列
            List<String> head2 = new ArrayList<String>();
            head2.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
            head2.add("时间维度");
            head2.add("年度");
            list.add(head2);
        } else if (2 == targetDecomposeDTO.getTimeDimension()) {
            // 动态列
            List<String> head2 = new ArrayList<String>();
            head2.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
            head2.add("时间维度");
            head2.add("上年度");
            // 动态列
            List<String> head3 = new ArrayList<String>();
            head3.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
            head3.add("时间维度");
            head3.add("下年度");
            list.add(head2);
            list.add(head3);
        } else if (3 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 4; i++) {
                // 动态列
                List<String> head2 = new ArrayList<String>();
                head2.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
                head2.add("时间维度");
                head2.add(Convert.int2chineseNum(i) + "季度");
                list.add(head2);
            }
        } else if (4 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 12; i++) {
                // 动态列
                List<String> head2 = new ArrayList<String>();
                head2.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
                head2.add("时间维度");
                head2.add(i + "月度");
                list.add(head2);
            }
        } else if (5 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 52; i++) {
                // 动态列
                List<String> head2 = new ArrayList<String>();
                head2.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
                head2.add("时间维度");
                head2.add(i + "周");
                list.add(head2);
            }
        }
        return list;
    }

    @Override
    public void invoke(Map<Integer, String> map, AnalysisContext analysisContext) {
        listMap.add(map);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 调用importer方法
        targetDecomposeService.importTargetDecompose(list);
        // 存储完成清理list
        list.clear();
    }


    /**
     * 创建表头，可以创建复杂的表头
     *
     * @return2
     */
    public static List<List<String>> head() {
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("目标年度");
        // 第二列
        List<String> head1 = new ArrayList<String>();
        head1.add("指标名称");
        // 第三列
        List<String> head2 = new ArrayList<String>();
        head2.add("分解维度");
        // 第四列
        List<String> head3 = new ArrayList<String>();
        head3.add("时间维度");
        // 第五列
        List<String> head4 = new ArrayList<String>();
        head4.add("公司目标");
        // 第六列
        List<String> head5 = new ArrayList<String>();
        head5.add("分解目标");
        // 第七列
        List<String> head6 = new ArrayList<String>();
        head6.add("目标差异");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        list.add(head4);
        list.add(head5);
        list.add(head6);
        return list;
    }

    /**
     * excel数据
     *
     * @param targetDecomposeExcelList
     * @return
     */
    public static List dataList(List<TargetDecomposeExcel> targetDecomposeExcelList) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        for (TargetDecomposeExcel targetDecomposeExcel : targetDecomposeExcelList) {
            List<Object> data = new ArrayList<Object>();
            //目标年度
            data.add(targetDecomposeExcel.getTargetYear());
            //指标名称
            data.add(targetDecomposeExcel.getIndicatorName());
            //分解维度
            data.add(targetDecomposeExcel.getDecompositionDimension());
            //时间维度
            data.add(targetDecomposeExcel.getTimeDimensionName());
            //公司目标
            data.add(targetDecomposeExcel.getTargetValue());
            //分解目标
            data.add(targetDecomposeExcel.getDecomposeTarget());
            //目标差异
            data.add(targetDecomposeExcel.getTargetDifference());
            list.add(data);
        }
        return list;
    }

    /**
     * excel解析导出数据
     *
     * @param targetDecomposeDTO
     * @return
     */
    public static List excelParseObject(TargetDecomposeDTO targetDecomposeDTO) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        TargetDecomposeImportListener.excelParseObject(list, targetDecomposeDTO);
        return list;
    }

    /**
     * 封装excel重复数据
     *
     * @param list
     */
    public static void excelParseObject(List<List<Object>> list, TargetDecomposeDTO targetDecomposeDTO) {
        List<Object> list1 = new ArrayList<>();
        List<Object> list2 = new ArrayList<>();
        List<Object> list3 = new ArrayList<>();
        List<Object> list4 = new ArrayList<>();
        List<Object> list5 = new ArrayList<>();
        List<Object> list6 = new ArrayList<>();
        List<Object> list7 = new ArrayList<>();
        List<Object> list8 = new ArrayList<>();
        list1.add("目标年度：" + targetDecomposeDTO.getTargetYear());
        list2.add("指标名称：" + targetDecomposeDTO.getIndicatorName());
        list3.add("挑战值：" + targetDecomposeDTO.getChallengeValue());
        list4.add("目标值：" + targetDecomposeDTO.getTargetValue());
        list5.add("保底值：" + targetDecomposeDTO.getGuaranteedValue());
        list6.add("分解目标：" + targetDecomposeDTO.getDecomposeTarget());
        list7.add("");
        list8.add("注：省份维度录入汉字，部门/销售员/产品/区域/行业维度录入编码");
        list.add(list1);
        list.add(list2);
        list.add(list3);
        list.add(list4);
        list.add(list5);
        list.add(list6);
        list.add(list7);
        list.add(list8);
    }
}


