package net.qixiaowei.strategy.cloud.excel.marketInsight;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.*;

import net.qixiaowei.integration.common.enums.strategy.PlanBusinessUnitCode;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightMacroDTO;
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

    /**
     * 自定义表头
     * @param marketInsightMacroDTO
     * @return
     */
    public static List<List<String>> head(MarketInsightMacroDTO marketInsightMacroDTO) {
        String businessUnitDecompose = marketInsightMacroDTO.getBusinessUnitDecompose();
        List<String> businessUnitDecomposes = Arrays.asList(businessUnitDecompose.split(","));
        Long productId = marketInsightMacroDTO.getProductId();
        Long areaId = marketInsightMacroDTO.getAreaId();
        Long departmentId = marketInsightMacroDTO.getDepartmentId();
        Long industryId = marketInsightMacroDTO.getIndustryId();

        List<Map<String, String>> dropList = PlanBusinessUnitCode.getDropList(marketInsightMacroDTO.getBusinessUnitDecompose());
        List<List<String>> list = new ArrayList<List<String>>();

        List<String> head2 = new ArrayList<String>();
        head2.add("规划年度："+marketInsightMacroDTO.getPlanYear());
        list.add(head2);
        List<String> head3 = new ArrayList<String>();
        head3.add("规划业务单元名称："+marketInsightMacroDTO.getPlanBusinessUnitName());
        list.add(head3);

        if (StringUtils.isNotEmpty(dropList)){
            for (Map<String, String> stringStringMap : dropList) {
                List<String> head4 = new ArrayList<String>();
                String name = stringStringMap.get("name");
                if (name.equals("productId")){
                    head4.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getProductName());
                }else if (name.equals("areaId")){
                    head4.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getAreaName());
                }else if (name.equals("departmentId")){
                    head4.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getDepartmentName());
                }else if (name.equals("industryId")){
                    head4.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getIndustryName());
                }
                list.add(head4);
            }
        }
        return list;
    }


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


