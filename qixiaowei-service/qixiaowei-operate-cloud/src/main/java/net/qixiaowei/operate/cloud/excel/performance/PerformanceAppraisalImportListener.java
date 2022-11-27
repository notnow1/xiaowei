package net.qixiaowei.operate.cloud.excel.performance;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalService;

import java.util.ArrayList;
import java.util.List;

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
     * 缓存的数据列表
     */
    private List<PerformanceAppraisalExcel> list = new ArrayList<>();
    /**
     * 用户service
     */
    private final IPerformanceAppraisalService performanceAppraisalService;

    /**
     * 定义组织系统导出模板 表头
     *
     * @return
     */
    public static List<List<String>> headOrgSystemTemplate() {
        List<List<String>> list = new ArrayList<List<String>>();
        // 第1列
        List<String> head0 = new ArrayList<String>();
        head0.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head0.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head0.add("考核对象");
        // 第2列
        List<String> head1 = new ArrayList<String>();
        head1.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head0.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head1.add("评议总分");
        // 第3列
        List<String> head2 = new ArrayList<String>();
        head2.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head0.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head2.add("考核结果");
        // 第4列
        List<String> head3 = new ArrayList<String>();
        head3.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head0.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head3.add("考核责任人工号");
        // 第4列
        List<String> head4 = new ArrayList<String>();
        head4.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head0.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head4.add("考核责任人姓名");
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
            performanceAppraisalService.importPerformanceAppraisal(list);
            // 存储完成清理list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 调用importer方法
        performanceAppraisalService.importPerformanceAppraisal(list);
        // 存储完成清理list
        list.clear();
    }
}


