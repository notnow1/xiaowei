package net.qixiaowei.operate.cloud.excel.performance;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * PerformanceAppraisalImportListener
 *
 * @author Graves
 * @since 2022-11-24
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PerformanceAppraisalImportListener extends AnalysisEventListener<PerformanceAppraisalExcel> {
    /**
     * 默认每隔3000条存储数据库
     */
    private int batchCount = 3000;
    /**
     * 正文起始行
     */
    private final Integer headRowNumber;
    /**
     * 缓存的数据列表
     */
    private List<PerformanceAppraisalExcel> list = new ArrayList<>();

    /**
     * 定义组织系统导出模板 表头
     *
     * @return List
     */
    public static List<List<String>> headOrgSystemTemplate(Map<Integer, List<String>> selectMap, List<PerformanceRankFactorDTO> performanceRankFactorDTOS) {
        List<String> listSelect = new ArrayList<>();
        for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
            listSelect.add(performanceRankFactorDTO.getPerformanceRankName());
        }
        listSelect.add("不考核");
        List<List<String>> list = new ArrayList<List<String>>();
        // 第1列
        List<String> head0 = new ArrayList<String>();
        head0.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head0.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head0.add("考核对象");
        // 第2列
        List<String> head1 = new ArrayList<String>();
        head1.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head1.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head1.add("评议总分");
        // 第3列
        List<String> head2 = new ArrayList<String>();
        head2.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head2.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head2.add("考核结果");
        selectMap.put(2, listSelect);
        // 第4列
        List<String> head3 = new ArrayList<String>();
        head3.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head3.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head3.add("考核责任人工号");
        // 第4列
        List<String> head4 = new ArrayList<String>();
        head4.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head4.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head4.add("考核责任人姓名");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        list.add(head4);
        return list;
    }

    /**
     * 定义组织自定义导出模板 表头
     *
     * @return List
     */
    public static List<List<String>> headOrgCustomTemplate(Map<Integer, List<String>> selectMap, List<PerformanceRankFactorDTO> performanceRankFactorDTOS) {
        List<String> listSelect = new ArrayList<>();
        for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
            listSelect.add(performanceRankFactorDTO.getPerformanceRankName());
        }
        listSelect.add("不考核");
        List<List<String>> list = new ArrayList<List<String>>();
        // 第1列
        List<String> head0 = new ArrayList<String>();
        head0.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head0.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head0.add("   3、用户可在后面追加、编辑自定义列。");
        head0.add("考核对象");
        // 第2列
        List<String> head1 = new ArrayList<String>();
        head1.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head1.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head1.add("   3、用户可在后面追加、编辑自定义列。");
        head1.add("考核结果");
        selectMap.put(1, listSelect);
        // 第3列
        List<String> head2 = new ArrayList<String>();
        head2.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head2.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head2.add("   3、用户可在后面追加、编辑自定义列。");
        head2.add("自定义列1");
        // 第4列
        List<String> head3 = new ArrayList<String>();
        head3.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head3.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head3.add("   3、用户可在后面追加、编辑自定义列。");
        head3.add("自定义列2");
        // 第4列
        List<String> head4 = new ArrayList<String>();
        head4.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head4.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head4.add("   3、用户可在后面追加、编辑自定义列。");
        head4.add("自定义列3");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        list.add(head4);
        return list;
    }

    @Override
    public void invoke(PerformanceAppraisalExcel data, AnalysisContext context) {
        list.add(data);
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
            // 调用importer方法
//            performanceAppraisalService.importPerformanceAppraisal(list);
            // 存储完成清理list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 调用importer方法
//        performanceAppraisalService.importPerformanceAppraisal(list);
        // 存储完成清理list
        list.clear();
    }

    /**
     * 获取数据
     *
     * @param inputStream                    输入流
     * @param performanceAppraisalExcelClass excel类
     * @return Map
     */
    public Map<String, List<PerformanceAppraisalExcel>> getData(InputStream inputStream, Class<PerformanceAppraisalExcel> performanceAppraisalExcelClass) {

        return null;
    }

    /**
     * @param performanceAppraisalObjectsDTOList 考核对象列表
     * @return Collection
     */
    public static Collection<List<Object>> dataOrgTemplateList(List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList) {
        List<List<Object>> list = new ArrayList<>();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            List<Object> data = new ArrayList<Object>();
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectName());
            list.add(data);
        }
        return list;
    }
}


