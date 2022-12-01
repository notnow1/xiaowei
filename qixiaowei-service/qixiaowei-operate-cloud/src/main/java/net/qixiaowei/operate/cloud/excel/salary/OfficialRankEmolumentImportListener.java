package net.qixiaowei.operate.cloud.excel.salary;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import net.qixiaowei.operate.cloud.service.salary.IOfficialRankEmolumentService;

/**
* OfficialRankEmolumentImportListener
* @author Graves
* @since 2022-11-30
*/
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OfficialRankEmolumentImportListener extends AnalysisEventListener<OfficialRankEmolumentExcel>{
        /**
        * 默认每隔3000条存储数据库
        */
        private int batchCount = 3000;
        /**
        * 缓存的数据列表
        */
        private List<OfficialRankEmolumentExcel> list = new ArrayList<>();
        /**
         * 用户service
         */
        private final IOfficialRankEmolumentService officialRankEmolumentService;

        @Override
        public void invoke(OfficialRankEmolumentExcel data, AnalysisContext context) {
        list.add(data);
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
        // 调用importer方法
        officialRankEmolumentService.importOfficialRankEmolument(list);
        // 存储完成清理list
        list.clear();
        }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 调用importer方法
        officialRankEmolumentService.importOfficialRankEmolument(list);
        // 存储完成清理list
        list.clear();
        }
}


