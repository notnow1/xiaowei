package net.qixiaowei.operate.cloud.excel.targetManager;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TargetOutcomeImportListener
 *
 * @author TANGMICHI
 * @since 2022-11-07
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TargetOutcomeImportListener extends AnalysisEventListener<Map<Integer, String>> {
    /**
     * 默认每隔3000条存储数据库
     */
    private int batchCount = 3000;
    /**
     * 缓存的数据列表
     */
    private List<String> list = new ArrayList<>();

    /**
     * 解析数据
     * key是sheetName，value是相应sheet的解析数据
     */
    private final Map<String, List<Map<Integer, String>>> dataMap = new HashMap<>();

    private final Long targetOutcomeId;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ITargetOutcomeService targetOutcomeService;


    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        String sheetName = context.readSheetHolder().getSheetName();
        dataMap.computeIfAbsent(sheetName, k -> new ArrayList<>());
        dataMap.get(sheetName).add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        logger.info("Excel解析完成");
        targetOutcomeService.importTargetOutcome(dataMap,targetOutcomeId);
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
        head0.add("关键指标");
        // 第一列
        List<String> head1 = new ArrayList<String>();
        head1.add("实际值合计");
        // 第一列
        List<String> head2 = new ArrayList<String>();
        head2.add("目标值");
        // 第一列
        List<String> head3 = new ArrayList<String>();
        head3.add("目标完成率（%）");
        // 第二列
        List<String> head4 = new ArrayList<String>();
        head4.add("实际值");
        head4.add("1月");
        // 第二列
        List<String> head5 = new ArrayList<String>();
        head5.add("实际值");
        head5.add("2月");
        // 第二列
        List<String> head6 = new ArrayList<String>();
        head6.add("实际值");
        head6.add("3月");
        // 第二列
        List<String> head7 = new ArrayList<String>();
        head7.add("实际值");
        head7.add("4月");
        // 第三列
        List<String> head8 = new ArrayList<String>();
        head8.add("实际值");
        head8.add("5月");
        // 第四列
        List<String> head9 = new ArrayList<String>();
        head9.add("实际值");
        head9.add("6月");
        // 第五列
        List<String> head10 = new ArrayList<String>();
        head10.add("实际值");
        head10.add("7月");
        // 第六列
        List<String> head11 = new ArrayList<String>();
        head11.add("实际值");
        head11.add("8月");
        // 第七列
        List<String> head12 = new ArrayList<String>();
        head12.add("实际值");
        head12.add("9月");
        // 第七列
        List<String> head13 = new ArrayList<String>();
        head13.add("实际值");
        head13.add("10月");
        // 第七列
        List<String> head14 = new ArrayList<String>();
        head14.add("实际值");
        head14.add("11月");
        // 第七列
        List<String> head15 = new ArrayList<String>();
        head15.add("实际值");
        head15.add("12月");
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
        return list;
    }

    /**
     * excel数据
     *
     * @param targetOutcomeExcelList
     * @return
     */
    public static List<List<Object>> dataList(List<TargetOutcomeExcel> targetOutcomeExcelList) {
        List<List<Object>> list = new ArrayList<>();
        for (TargetOutcomeExcel targetOutcomeExcel : targetOutcomeExcelList) {
            List<Object> data = new ArrayList<Object>();
            // 关键指标
            data.add(targetOutcomeExcel.getIndicatorName());
            // 实际值合计
            data.add(targetOutcomeExcel.getActualTotal());
            // 目标值
            data.add(targetOutcomeExcel.getTargetValue());
            // 目标完成率（%）
            data.add(targetOutcomeExcel.getTargetCompletionRate());
            // 1月
            data.add(targetOutcomeExcel.getActualJanuary());
            // 2月
            data.add(targetOutcomeExcel.getActualFebruary());
            // 3月
            data.add(targetOutcomeExcel.getActualMarch());
            // 4月
            data.add(targetOutcomeExcel.getActualApril());
            // 5月
            data.add(targetOutcomeExcel.getActualMay());
            // 6月
            data.add(targetOutcomeExcel.getActualJune());
            // 7月
            data.add(targetOutcomeExcel.getActualJuly());
            // 8月
            data.add(targetOutcomeExcel.getActualAugust());
            // 9月
            data.add(targetOutcomeExcel.getActualSeptember());
            // 10月
            data.add(targetOutcomeExcel.getActualOctober());
            // 11月
            data.add(targetOutcomeExcel.getActualNovember());
            // 12月
            data.add(targetOutcomeExcel.getActualDecember());
            list.add(data);
        }
        return list;
    }

}


