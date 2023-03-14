package net.qixiaowei.strategy.cloud.excel.marketInsight;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.*;

import net.qixiaowei.integration.common.enums.strategy.PlanBusinessUnitCode;
import net.qixiaowei.integration.common.utils.DateUtils;
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
        List<Map<String, String>> dropList = PlanBusinessUnitCode.getExportDropList(marketInsightMacroDTO.getBusinessUnitDecompose());
        List<List<String>> list = new ArrayList<List<String>>();

        List<String> head2 = new ArrayList<String>();
        head2.add("规划年度："+marketInsightMacroDTO.getPlanYear());
        head2.add("规划业务单元名称："+marketInsightMacroDTO.getPlanBusinessUnitName());
        if (StringUtils.isNotEmpty(dropList)){
            for (Map<String, String> stringStringMap : dropList) {
                String name = stringStringMap.get("name");
                if (name.equals("productId")){
                    head2.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getProductName());
                }else if (name.equals("areaId")){
                    head2.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getAreaName());
                }else if (name.equals("departmentId")){
                    head2.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getDepartmentName());
                }else if (name.equals("industryId")){
                    head2.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getIndustryName());
                }
            }
        }
        head2.add("视角");
        list.add(head2);
        List<String> head5 = new ArrayList<String>();
        head5.add("规划年度："+marketInsightMacroDTO.getPlanYear());
        head5.add("规划业务单元名称："+marketInsightMacroDTO.getPlanBusinessUnitName());
        if (StringUtils.isNotEmpty(dropList)){
            for (Map<String, String> stringStringMap : dropList) {
                String name = stringStringMap.get("name");
                if (name.equals("productId")){
                    head5.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getProductName());
                }else if (name.equals("areaId")){
                    head5.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getAreaName());
                }else if (name.equals("departmentId")){
                    head5.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getDepartmentName());
                }else if (name.equals("industryId")){
                    head5.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getIndustryName());
                }
            }
        }
        head5.add("与企业相关因素");
        list.add(head5);

        List<String> head6 = new ArrayList<String>();
        head6.add("规划年度："+marketInsightMacroDTO.getPlanYear());
        head6.add("规划业务单元名称："+marketInsightMacroDTO.getPlanBusinessUnitName());
        if (StringUtils.isNotEmpty(dropList)){
            for (Map<String, String> stringStringMap : dropList) {
                String name = stringStringMap.get("name");
                if (name.equals("productId")){
                    head6.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getProductName());
                }else if (name.equals("areaId")){
                    head6.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getAreaName());
                }else if (name.equals("departmentId")){
                    head6.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getDepartmentName());
                }else if (name.equals("industryId")){
                    head6.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getIndustryName());
                }
            }
        }
        head6.add("变化及趋势");
        list.add(head6);

        List<String> head7 = new ArrayList<String>();
        head7.add("规划年度："+marketInsightMacroDTO.getPlanYear());
        head7.add("规划业务单元名称："+marketInsightMacroDTO.getPlanBusinessUnitName());
        if (StringUtils.isNotEmpty(dropList)){
            for (Map<String, String> stringStringMap : dropList) {
                String name = stringStringMap.get("name");
                if (name.equals("productId")){
                    head7.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getProductName());
                }else if (name.equals("areaId")){
                    head7.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getAreaName());
                }else if (name.equals("departmentId")){
                    head7.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getDepartmentName());
                }else if (name.equals("industryId")){
                    head7.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getIndustryName());
                }
            }
        }
        head7.add("影响描述");
        list.add(head7);

        List<String> head8 = new ArrayList<String>();
        head8.add("规划年度："+marketInsightMacroDTO.getPlanYear());
        head8.add("规划业务单元名称："+marketInsightMacroDTO.getPlanBusinessUnitName());
        if (StringUtils.isNotEmpty(dropList)){
            for (Map<String, String> stringStringMap : dropList) {
                String name = stringStringMap.get("name");
                if (name.equals("productId")){
                    head8.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getProductName());
                }else if (name.equals("areaId")){
                    head8.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getAreaName());
                }else if (name.equals("departmentId")){
                    head8.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getDepartmentName());
                }else if (name.equals("industryId")){
                    head8.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getIndustryName());
                }
            }
        }
        head8.add("建议措施");
        list.add(head8);

        List<String> head9 = new ArrayList<String>();
        head9.add("规划年度："+marketInsightMacroDTO.getPlanYear());
        head9.add("规划业务单元名称："+marketInsightMacroDTO.getPlanBusinessUnitName());
        if (StringUtils.isNotEmpty(dropList)){
            for (Map<String, String> stringStringMap : dropList) {
                String name = stringStringMap.get("name");
                if (name.equals("productId")){
                    head9.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getProductName());
                }else if (name.equals("areaId")){
                    head9.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getAreaName());
                }else if (name.equals("departmentId")){
                    head9.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getDepartmentName());
                }else if (name.equals("industryId")){
                    head9.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getIndustryName());
                }
            }
        }
        head9.add("规划期");
        list.add(head9);

        List<String> head10 = new ArrayList<String>();
        head10.add("规划年度："+marketInsightMacroDTO.getPlanYear());
        head10.add("规划业务单元名称："+marketInsightMacroDTO.getPlanBusinessUnitName());
        if (StringUtils.isNotEmpty(dropList)){
            for (Map<String, String> stringStringMap : dropList) {
                String name = stringStringMap.get("name");
                if (name.equals("productId")){
                    head10.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getProductName());
                }else if (name.equals("areaId")){
                    head10.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getAreaName());
                }else if (name.equals("departmentId")){
                    head10.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getDepartmentName());
                }else if (name.equals("industryId")){
                    head10.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getIndustryName());
                }
            }
        }
        head10.add("预估机会点金额");
        list.add(head10);

        List<String> head11 = new ArrayList<String>();
        head11.add("规划年度："+marketInsightMacroDTO.getPlanYear());
        head11.add("规划业务单元名称："+marketInsightMacroDTO.getPlanBusinessUnitName());
        if (StringUtils.isNotEmpty(dropList)){
            for (Map<String, String> stringStringMap : dropList) {
                String name = stringStringMap.get("name");
                if (name.equals("productId")){
                    head11.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getProductName());
                }else if (name.equals("areaId")){
                    head11.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getAreaName());
                }else if (name.equals("departmentId")){
                    head11.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getDepartmentName());
                }else if (name.equals("industryId")){
                    head11.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getIndustryName());
                }
            }
        }
        head11.add("提出人姓名");
        list.add(head11);

        List<String> head12 = new ArrayList<String>();
        head12.add("规划年度："+marketInsightMacroDTO.getPlanYear());
        head12.add("规划业务单元名称："+marketInsightMacroDTO.getPlanBusinessUnitName());
        if (StringUtils.isNotEmpty(dropList)){
            for (Map<String, String> stringStringMap : dropList) {
                String name = stringStringMap.get("name");
                if (name.equals("productId")){
                    head12.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getProductName());
                }else if (name.equals("areaId")){
                    head12.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getAreaName());
                }else if (name.equals("departmentId")){
                    head12.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getDepartmentName());
                }else if (name.equals("industryId")){
                    head12.add(stringStringMap.get("label")+"；"+marketInsightMacroDTO.getIndustryName());
                }
            }
        }
        head12.add("提出人工号");
        list.add(head12);
        return list;
    }

    public static List<List<Object>> dataList(List<MarketInsightMacroExcel> marketInsightMacroExcelList) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        for (MarketInsightMacroExcel marketInsightMacroExcel : marketInsightMacroExcelList) {
            List<Object> data = new ArrayList<Object>();
            data.add(marketInsightMacroExcel.getVisualAngleName());
            data.add(marketInsightMacroExcel.getCompanyRelatedFactor());
            data.add(marketInsightMacroExcel.getChangeTrend());
            data.add(marketInsightMacroExcel.getInfluenceDescription());
            data.add(marketInsightMacroExcel.getRecommendedPractice());
            data.add(marketInsightMacroExcel.getPlanPeriod());
            data.add(marketInsightMacroExcel.getEstimateOpportunityAmount());
            data.add(marketInsightMacroExcel.getProposeEmployeeName());
            data.add(marketInsightMacroExcel.getProposeEmployeeCode());
            list.add(data);
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


