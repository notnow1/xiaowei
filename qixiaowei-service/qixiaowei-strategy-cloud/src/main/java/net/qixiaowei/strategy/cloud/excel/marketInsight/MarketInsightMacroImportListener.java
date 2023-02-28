package net.qixiaowei.strategy.cloud.excel.marketInsight;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightMacroService;

/**
* MarketInsightMacroImportListener
* @author TANGMICHI
* @since 2023-02-28
*/
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MarketInsightMacroImportListener extends AnalysisEventListener<MarketInsightMacroExcel>{
        /**
        * 默认每隔3000条存储数据库
        */
        private int batchCount = 3000;
        /**
        * 缓存的数据列表
        */
        private List<MarketInsightMacroExcel> list = new ArrayList<>();
        /**
         * 用户service
         */
        private final IMarketInsightMacroService marketInsightMacroService;

        @Override
        public void invoke(MarketInsightMacroExcel data, AnalysisContext context) {
        list.add(data);
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
        // 存储完成清理list
        list.clear();
        }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 存储完成清理list
        list.clear();
        }
}


