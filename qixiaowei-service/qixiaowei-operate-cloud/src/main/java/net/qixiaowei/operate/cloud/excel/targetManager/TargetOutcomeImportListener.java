package net.qixiaowei.operate.cloud.excel.targetManager;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.qixiaowei.integration.common.utils.DateUtils;
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
    }

    /**
     * 创建导入模板表头，可以创建复杂的表头
     *
     * @return2
     */
    public static List<List<String>> headTemplate(Integer targetYear) {
        int year = DateUtils.getYear();
        if (year == targetYear) {
            return getNowHead();
        } else if (year > targetYear) {
            return geLastedHead();
        } else {
            return getFutureHead();
        }

    }

    /**
     * 获取未来年份的头部
     *
     * @return
     */
    private static List<List<String>> getFutureHead() {
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("关键指标");
        list.add(head0);
        return list;
    }

    /**
     * 获取过去年份的头部
     *
     * @return
     */
    private static List<List<String>> geLastedHead() {
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("关键指标");
        // 第二列
        List<String> head1 = new ArrayList<String>();
        head1.add("实际值");
        head1.add("1月");
        // 第二列
        List<String> head2 = new ArrayList<String>();
        head2.add("实际值");
        head2.add("2月");
        // 第二列
        List<String> head3 = new ArrayList<String>();
        head3.add("实际值");
        head3.add("3月");
        // 第二列
        List<String> head4 = new ArrayList<String>();
        head4.add("实际值");
        head4.add("4月");
        // 第三列
        List<String> head5 = new ArrayList<String>();
        head5.add("实际值");
        head5.add("5月");
        // 第四列
        List<String> head6 = new ArrayList<String>();
        head6.add("实际值");
        head6.add("6月");
        // 第五列
        List<String> head7 = new ArrayList<String>();
        head7.add("实际值");
        head7.add("7月");
        // 第六列
        List<String> head8 = new ArrayList<String>();
        head8.add("实际值");
        head8.add("8月");
        // 第七列
        List<String> head9 = new ArrayList<String>();
        head9.add("实际值");
        head9.add("9月");
        // 第七列
        List<String> head10 = new ArrayList<String>();
        head10.add("实际值");
        head10.add("10月");
        // 第七列
        List<String> head11 = new ArrayList<String>();
        head11.add("实际值");
        head11.add("11月");
        // 第七列
        List<String> head12 = new ArrayList<String>();
        head11.add("实际值");
        head11.add("12月");
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
        return list;
    }

    /**
     * 获取当前年份的head
     *
     * @return
     */
    private static List<List<String>> getNowHead() {
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("关键指标");
        // 第二列
        List<String> head1 = new ArrayList<String>();
        head1.add("实际值");
        head1.add("1月");
        // 第二列
        List<String> head2 = new ArrayList<String>();
        head2.add("实际值");
        head2.add("2月");
        // 第二列
        List<String> head3 = new ArrayList<String>();
        head3.add("实际值");
        head3.add("3月");
        // 第二列
        List<String> head4 = new ArrayList<String>();
        head4.add("实际值");
        head4.add("4月");
        // 第三列
        List<String> head5 = new ArrayList<String>();
        head5.add("实际值");
        head5.add("5月");
        // 第四列
        List<String> head6 = new ArrayList<String>();
        head6.add("实际值");
        head6.add("6月");
        // 第五列
        List<String> head7 = new ArrayList<String>();
        head7.add("实际值");
        head7.add("7月");
        // 第六列
        List<String> head8 = new ArrayList<String>();
        head8.add("实际值");
        head8.add("8月");
        // 第七列
        List<String> head9 = new ArrayList<String>();
        head9.add("实际值");
        head9.add("9月");
        // 第七列
        List<String> head10 = new ArrayList<String>();
        head10.add("实际值");
        head10.add("10月");
        // 第七列
        List<String> head11 = new ArrayList<String>();
        head11.add("实际值");
        head11.add("11月");
        int month = DateUtils.getMonth();
        list.add(head0);
        if (month > 1) {
            list.add(head1);
        }
        if (month > 2) {
            list.add(head2);
        }
        if (month > 3) {
            list.add(head3);
        }
        if (month > 4) {
            list.add(head4);
        }
        if (month > 5) {
            list.add(head5);
        }
        if (month > 6) {
            list.add(head6);
        }
        if (month > 7) {
            list.add(head7);
        }
        if (month > 8) {
            list.add(head8);
        }
        if (month > 9) {
            list.add(head9);
        }
        if (month > 10) {
            list.add(head10);
        }
        if (month > 11) {
            list.add(head11);
        }
        return list;
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
        int month = DateUtils.getMonth();
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        if (month > 1) {
            list.add(head4);
        }
        if (month > 2) {
            list.add(head5);
        }
        if (month > 3) {
            list.add(head6);
        }
        if (month > 4) {
            list.add(head7);
        }
        if (month > 5) {
            list.add(head8);
        }
        if (month > 6) {
            list.add(head9);
        }
        if (month > 7) {
            list.add(head10);
        }
        if (month > 8) {
            list.add(head11);
        }
        if (month > 9) {
            list.add(head12);
        }
        if (month > 10) {
            list.add(head13);
        }
        if (month > 11) {
            list.add(head14);
        }
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
            int month = DateUtils.getMonth();
            if (month > 1) {
                // 1月
                data.add(targetOutcomeExcel.getActualJanuary());
            }
            if (month > 2) {
                // 2月
                data.add(targetOutcomeExcel.getActualFebruary());
            }
            if (month > 3) {
                // 3月
                data.add(targetOutcomeExcel.getActualMarch());
            }
            if (month > 4) {
                // 4月
                data.add(targetOutcomeExcel.getActualApril());
            }
            if (month > 5) {
                // 5月
                data.add(targetOutcomeExcel.getActualMay());
            }
            if (month > 6) {
                // 6月
                data.add(targetOutcomeExcel.getActualJune());
            }
            if (month > 7) {
                // 7月
                data.add(targetOutcomeExcel.getActualJuly());
            }
            if (month > 8) {
                // 8月
                data.add(targetOutcomeExcel.getActualAugust());
            }
            if (month > 9) {
                // 9月
                data.add(targetOutcomeExcel.getActualSeptember());
            }
            if (month > 10) {
                // 10月
                data.add(targetOutcomeExcel.getActualOctober());
            }
            if (month > 11) {
                // 11月
                data.add(targetOutcomeExcel.getActualNovember());
            }
            if (month > 13) {
                // 12月
                data.add(targetOutcomeExcel.getActualDecember());
            }
            list.add(data);
        }
        return list;
    }

}


