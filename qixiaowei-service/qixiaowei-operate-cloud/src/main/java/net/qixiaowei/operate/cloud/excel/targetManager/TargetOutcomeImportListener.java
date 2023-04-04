package net.qixiaowei.operate.cloud.excel.targetManager;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDetailsDTO;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
     * @return 结果
     */
    public static List<List<String>> headTemplate(Integer targetYear, List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList) {
        List<List<String>> list = new ArrayList<List<String>>();
        int month = DateUtils.getMonth();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("币种：人民币    单位：万元");
        head0.add("目标年度");
        head0.add("");
        head0.add("指标名称");
        head0.add("指标名称");
        list.add(head0);
        // 数据
        int year = DateUtils.getYear();
        if (year == targetYear) {
            List<String> head1 = new ArrayList<String>();
            if (month == 1) {
                head1.add("币种：人民币    单位：万元");
                head1.add(targetYear.toString());
                list.add(head1);
            }
            // 第二列
            for (int i = 1; i < month; i++) {
                head1 = new ArrayList<String>();
                if (i == 1) {
                    head1.add("币种：人民币    单位：万元");
                    head1.add(targetYear.toString());
                    head1.add("");
                    head1.add("实际值");
                    head1.add(i + "月");
                } else {
                    head1.add("");
                    head1.add("");
                    head1.add("");
                    head1.add("实际值");
                    head1.add(i + "月");
                }
                list.add(head1);
            }
        } else if (year < targetYear) {
            List<String> head1 = new ArrayList<String>();
            head1.add("币种：人民币    单位：万元");
            head1.add(targetYear.toString());
            list.add(head1);
            head1.add("");
            head1.add("实际值");
            head1.add("实际值");
        } else {
            // 第二列
            for (int i = 1; i < 13; i++) {
                List<String> head1 = new ArrayList<String>();
                if (i == 1) {
                    head1.add("币种：人民币    单位：万元");
                    head1.add(targetYear.toString());
                    head1.add("");
                    head1.add("实际值");
                    head1.add(i + "月");
                } else {
                    head1.add("");
                    head1.add("");
                    head1.add("");
                    head1.add("实际值");
                    head1.add(i + "月");
                }
                list.add(head1);
            }
        }
        return list;
    }

    /**
     * 创建表头，可以创建复杂的表头
     *
     * @return 结果
     */
    public static List<List<String>> head(TargetOutcomeDTO targetOutcomeDTO) {
        Integer targetYear = targetOutcomeDTO.getTargetYear();
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("币种：人民币    单位：万元");
        head0.add("目标年度");
        head0.add("");
        head0.add("指标名称");
        head0.add("指标名称");
        // 第一列
        List<String> head1 = new ArrayList<String>();
        head1.add("币种：人民币    单位：万元");
        head1.add(targetYear.toString());
        head1.add("");
        head1.add("指标编码");
        head1.add("指标编码");
        // 第一列
        List<String> head2 = new ArrayList<String>();
        head2.add("");
        head2.add("");
        head2.add("");
        head2.add("实际值合计");
        head2.add("实际值合计");
        // 第一列
        List<String> head3 = new ArrayList<String>();
        head3.add("");
        head3.add("");
        head3.add("");
        head3.add("目标值");
        head3.add("目标值");
        // 第一列
        List<String> head4 = new ArrayList<String>();
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("目标完成率（%）");
        head4.add("目标完成率（%）");
        // 第二列
        List<String> head5 = new ArrayList<String>();
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("实际值");
        head5.add("1月");
        // 第二列
        List<String> head6 = new ArrayList<String>();
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("实际值");
        head6.add("2月");
        // 第二列
        List<String> head7 = new ArrayList<String>();
        head7.add("");
        head7.add("");
        head7.add("");
        head7.add("实际值");
        head7.add("3月");
        // 第二列
        List<String> head8 = new ArrayList<String>();
        head8.add("");
        head8.add("");
        head8.add("");
        head8.add("实际值");
        head8.add("4月");
        // 第三列
        List<String> head9 = new ArrayList<String>();
        head9.add("");
        head9.add("");
        head9.add("");
        head9.add("实际值");
        head9.add("5月");
        // 第四列
        List<String> head10 = new ArrayList<String>();
        head10.add("");
        head10.add("");
        head10.add("");
        head10.add("实际值");
        head10.add("6月");
        // 第五列
        List<String> head11 = new ArrayList<String>();
        head11.add("");
        head11.add("");
        head11.add("");
        head11.add("实际值");
        head11.add("7月");
        // 第六列
        List<String> head12 = new ArrayList<String>();
        head12.add("");
        head12.add("");
        head12.add("");
        head12.add("实际值");
        head12.add("8月");
        // 第七列
        List<String> head13 = new ArrayList<String>();
        head13.add("");
        head13.add("");
        head13.add("");
        head13.add("实际值");
        head13.add("9月");
        // 第七列
        List<String> head14 = new ArrayList<String>();
        head14.add("");
        head14.add("");
        head14.add("");
        head14.add("实际值");
        head14.add("10月");
        // 第七列
        List<String> head15 = new ArrayList<String>();
        head15.add("");
        head15.add("");
        head15.add("");
        head15.add("实际值");
        head15.add("11月");
        // 第七列
        List<String> head16 = new ArrayList<String>();
        head16.add("");
        head16.add("");
        head16.add("");
        head16.add("实际值");
        head16.add("12月");
        int month = DateUtils.getMonth();
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        list.add(head4);
        if (targetYear == DateUtils.getYear()) {
            if (month > 1) {
                list.add(head5);
            }
            if (month > 2) {
                list.add(head6);
            }
            if (month > 3) {
                list.add(head7);
            }
            if (month > 4) {
                list.add(head8);
            }
            if (month > 5) {
                list.add(head9);
            }
            if (month > 6) {
                list.add(head10);
            }
            if (month > 7) {
                list.add(head11);
            }
            if (month > 8) {
                list.add(head12);
            }
            if (month > 9) {
                list.add(head13);
            }
            if (month > 10) {
                list.add(head14);
            }
            if (month > 11) {
                list.add(head15);
            }
        } else if (targetYear < DateUtils.getYear()) {
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
        }
        return list;
    }

    /**
     * excel数据
     *
     * @param targetOutcomeExcelList excel列表
     * @param targetOutcomeDTO       dot
     * @return 结果
     */
    public static List<List<Object>> dataList(List<TargetOutcomeExcel> targetOutcomeExcelList, TargetOutcomeDTO targetOutcomeDTO) {
        Integer targetYear = targetOutcomeDTO.getTargetYear();
        List<List<Object>> list = new ArrayList<>();
        for (TargetOutcomeExcel targetOutcomeExcel : targetOutcomeExcelList) {
            List<Object> data = new ArrayList<Object>();
            // 关键指标
            data.add(targetOutcomeExcel.getIndicatorName());
            // 关键指标编码
            data.add(targetOutcomeExcel.getIndicatorCode());
            // 实际值合计
            data.add(targetOutcomeExcel.getActualTotal());
            // 目标值
            data.add(targetOutcomeExcel.getTargetValue());
            // 目标完成率（%）
            data.add(targetOutcomeExcel.getTargetCompletionRate());
            int month = DateUtils.getMonth();
            if (targetYear == DateUtils.getYear()) {
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
            } else if (targetYear < DateUtils.getYear()) {
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
            }
            list.add(data);
        }
        return list;
    }

    /**
     * 导出模板的Code与名称
     *
     * @param targetOutcomeDTO            dto
     * @param targetOutcomeDetailsDTOList 详情表
     * @param indicatorService            指标服务
     * @return Collection
     */
    public static Collection<List<Object>> dataTemplateList(TargetOutcomeDTO targetOutcomeDTO, List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList, RemoteIndicatorService indicatorService) {
        List<List<Object>> list = new ArrayList<>();
        Integer targetYear = targetOutcomeDTO.getTargetYear();
        List<Object> data;
        // 第一行
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            data = new ArrayList<>();
            data.add(targetOutcomeDetailsDTO.getIndicatorName());
            if (targetYear == DateUtils.getYear()) {
                for (int i = 1; i < DateUtils.getMonth(); i++) {
                    data.add("");
                }
            } else if (targetYear < DateUtils.getYear()) {
                for (int i = 1; i < 13; i++) {
                    data.add("");
                }
            } else {
                data.add("");
            }
            list.add(data);
        }
        return list;
    }


}


