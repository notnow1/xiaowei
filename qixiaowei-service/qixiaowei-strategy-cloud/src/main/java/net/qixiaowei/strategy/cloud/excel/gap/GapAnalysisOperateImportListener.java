package net.qixiaowei.strategy.cloud.excel.gap;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisOperateService;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GapAnalysisOperateImportListener
 *
 * @author Graves
 * @since 2023-02-24
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GapAnalysisOperateImportListener extends AnalysisEventListener<GapAnalysisOperateExcel> {
    /**
     * 默认每隔3000条存储数据库
     */
    private int batchCount = 3000;
    /**
     * 缓存的数据列表
     */
    private List<GapAnalysisOperateExcel> list = new ArrayList<>();
    /**
     * 用户service
     */
    private final IGapAnalysisOperateService gapAnalysisOperateService;

    private static String HEAD_TEMPLATE = "说明：\n" +
            "1、指标为下拉选择。\n" +
            "2、根据用户所选择的历史年度，生成对应年度区间的目标值与实际值。\n" +
            "3、请确保每个指标在各年度下均有值，若未录入，则视为“0”。\n" +
            "4、实际值与目标值格式：保存两位小数。\n" +
            "5、同一指标在相同年度下仅可存在一条数据，若重复，则本模板导入失败。";

    @Override
    public void invoke(GapAnalysisOperateExcel data, AnalysisContext context) {
        list.add(data);
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
            // 调用importer方法
            gapAnalysisOperateService.importGapAnalysisOperate(list);
            // 存储完成清理list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 调用importer方法
        gapAnalysisOperateService.importGapAnalysisOperate(list);
        // 存储完成清理list
        list.clear();
    }

    /**
     * 定义表头模板
     *
     * @param operateHistoryYear 历史经营年份
     * @param operateYear        年度
     * @param indicatorDTOS      指标DTO
     * @return List
     */
    public static List<List<String>> headTemplate(Integer operateHistoryYear, Integer operateYear, List<IndicatorDTO> indicatorDTOS, Map<Integer, List<String>> selectMap) {
        List<Integer> operateYears = new ArrayList<>();
        List<String> listSelect = indicatorDTOS.stream().map(IndicatorDTO::getIndicatorName).collect(Collectors.toList());
        for (int year = operateYear - 1; year >= operateYear - operateHistoryYear; year--) {
            operateYears.add(year);
        }
        List<List<String>> list = new ArrayList<List<String>>();
        // 第1列
        List<String> head0 = new ArrayList<String>();
        head0.add(HEAD_TEMPLATE);
        head0.add("指标");
        head0.add("指标");
        selectMap.put(0, listSelect);
        list.add(head0);
        for (Integer year : operateYears) {
            List<String> head1 = new ArrayList<String>();
            head1.add(HEAD_TEMPLATE);
            head1.add(year + "年");
            head1.add("目标值");
            list.add(head1);
            List<String> head2 = new ArrayList<String>();
            head2.add(HEAD_TEMPLATE);
            head2.add(year + "年");
            head2.add("实际值");
            list.add(head2);
        }
        return list;
    }
}


