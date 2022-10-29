package net.qixiaowei.operate.cloud.excel.targetManager;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
public class TargetDecomposeImportListener extends AnalysisEventListener<TargetDecomposeExcel> {
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

    @Override
    public void invoke(TargetDecomposeExcel data, AnalysisContext context) {
        list.add(data);
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
            // 调用importer方法
            targetDecomposeService.importTargetDecompose(list);
            // 存储完成清理list
            list.clear();
        }
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


}


