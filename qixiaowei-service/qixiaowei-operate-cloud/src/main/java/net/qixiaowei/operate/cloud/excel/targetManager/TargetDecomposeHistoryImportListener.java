package net.qixiaowei.operate.cloud.excel.targetManager;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeHistoryService;

/**
* TargetDecomposeHistoryImportListener
* @author TANGMICHI
* @since 2022-10-31
*/
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TargetDecomposeHistoryImportListener extends AnalysisEventListener<TargetDecomposeHistoryExcel>{
        /**
        * 默认每隔3000条存储数据库
        */
        private int batchCount = 3000;
        /**
        * 缓存的数据列表
        */
        private List<TargetDecomposeHistoryExcel> list = new ArrayList<>();
        /**
         * 用户service
         */
        private final ITargetDecomposeHistoryService targetDecomposeHistoryService;

        @Override
        public void invoke(TargetDecomposeHistoryExcel data, AnalysisContext context) {
        list.add(data);
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
        // 调用importer方法
        targetDecomposeHistoryService.importTargetDecomposeHistory(list);
        // 存储完成清理list
        list.clear();
        }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 调用importer方法
        targetDecomposeHistoryService.importTargetDecomposeHistory(list);
        // 存储完成清理list
        list.clear();
        }
}


