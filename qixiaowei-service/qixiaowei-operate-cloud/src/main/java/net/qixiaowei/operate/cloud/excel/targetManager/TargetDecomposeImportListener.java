package net.qixiaowei.operate.cloud.excel.targetManager;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.qixiaowei.integration.common.text.Convert;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
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
public class TargetDecomposeImportListener extends AnalysisEventListener<Map<Integer, String>> {
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
    /**
     * 解析数据(多sheet使用)
     * key是sheetName，value是相应sheet的解析数据
     */
    private final Map<String, List<Map<Integer, String>>> dataMap = new HashMap<>();

    /**
     * excle所有数据(单sheet使用)
     */
    private final List<Map<Integer, String>> listMap = new ArrayList<>();

    public static List<List<String>> headTemplate(TargetDecomposeDTO targetDecomposeDTO) {
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
        head0.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
        list.add(head0);

        List<Map<String, String>> fileNameList = targetDecomposeDTO.getFileNameList();
        for (Map<String, String> stringStringMap : fileNameList) {
            // 动态列
            List<String> head1 = new ArrayList<String>();
            head1.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
            head1.add("分解维度");
            head1.add(stringStringMap.get("label"));
            list.add(head1);
        }
        if (1 == targetDecomposeDTO.getTimeDimension()) {

            // 动态列
            List<String> head2 = new ArrayList<String>();
            head2.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
            head2.add("时间维度");
            head2.add("年度");
            list.add(head2);
        } else if (2 == targetDecomposeDTO.getTimeDimension()) {
            // 动态列
            List<String> head2 = new ArrayList<String>();
            head2.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
            head2.add("时间维度");
            head2.add("上年度");
            // 动态列
            List<String> head3 = new ArrayList<String>();
            head3.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
            head3.add("时间维度");
            head3.add("下年度");
            list.add(head2);
            list.add(head3);
        } else if (3 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 4; i++) {
                // 动态列
                List<String> head2 = new ArrayList<String>();
                head2.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
                head2.add("时间维度");
                head2.add(Convert.int2chineseNum(i) + "季度");
                list.add(head2);
            }
        } else if (4 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 12; i++) {
                // 动态列
                List<String> head2 = new ArrayList<String>();
                head2.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
                head2.add("时间维度");
                head2.add(i + "月");
                list.add(head2);
            }
        } else if (5 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 52; i++) {
                // 动态列
                List<String> head2 = new ArrayList<String>();
                head2.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
                head2.add("时间维度");
                head2.add(i + "周");
                list.add(head2);
            }
        }
        return list;
    }

    /**
     * 导出目标分解详情数据
     * @param targetDecomposeDTO
     * @return
     */
    public static List<List<String>> headDetails(TargetDecomposeDTO targetDecomposeDTO) {
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("");
        head0.add("");
        list.add(head0);

        List<Map<String, String>> fileNameList = targetDecomposeDTO.getFileNameList();
        for (Map<String, String> stringStringMap : fileNameList) {
            // 动态列
            List<String> head1 = new ArrayList<String>();
            head1.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
            head1.add(stringStringMap.get("label"));
            list.add(head1);
        }
        List<String> head2 = new ArrayList<String>();
        head2.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
        head2.add("滚动预测负责人");
        list.add(head2);
        List<String> head3 = new ArrayList<String>();
        head3.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
        head3.add("汇总金额");
        list.add(head3);
        if (1 == targetDecomposeDTO.getTimeDimension()) {

            // 动态列
            List<String> head4 = new ArrayList<String>();
            head4.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
            head4.add("年度");
            list.add(head4);
        } else if (2 == targetDecomposeDTO.getTimeDimension()) {
            // 动态列
            List<String> head5 = new ArrayList<String>();
            head5.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
            head5.add("上年度");
            // 动态列
            List<String> head6 = new ArrayList<String>();
            head6.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
            head6.add("下年度");
            list.add(head5);
            list.add(head6);
        } else if (3 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 4; i++) {
                // 动态列
                List<String> head7 = new ArrayList<String>();
                head7.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
                head7.add(Convert.int2chineseNum(i) + "季度");
                list.add(head7);
            }
        } else if (4 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 12; i++) {
                // 动态列
                List<String> head8 = new ArrayList<String>();
                head8.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
                head8.add(i + "月");
                list.add(head8);
            }
        } else if (5 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 52; i++) {
                // 动态列
                List<String> head9 = new ArrayList<String>();
                head9.add(targetDecomposeDTO.getIndicatorName() + "目标分解");
                head9.add(i + "周");
                list.add(head9);
            }
        }
        return list;
    }

    @Override
    public void invoke(Map<Integer, String> map, AnalysisContext analysisContext) {
        listMap.add(map);
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
     *
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

    /**
     * excel解析导出数据
     *
     * @param targetDecomposeDTO
     * @return
     */
    public static List excelParseObject(TargetDecomposeDTO targetDecomposeDTO) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        TargetDecomposeImportListener.excelParseObject(list, targetDecomposeDTO);
        return list;
    }

    /**
     * 封装excel重复数据
     *
     * @param list
     */
    public static void excelParseObject(List<List<Object>> list, TargetDecomposeDTO targetDecomposeDTO) {
        List<Object> list1 = new ArrayList<>();
        List<Object> list2 = new ArrayList<>();
        List<Object> list3 = new ArrayList<>();
        List<Object> list4 = new ArrayList<>();
        List<Object> list5 = new ArrayList<>();
        List<Object> list6 = new ArrayList<>();
        List<Object> list7 = new ArrayList<>();
        List<Object> list8 = new ArrayList<>();
        list1.add("目标年度：" + targetDecomposeDTO.getTargetYear());
        list2.add("指标名称：" + targetDecomposeDTO.getIndicatorName());
        list3.add("挑战值：" + targetDecomposeDTO.getChallengeValue());
        list4.add("目标值：" + targetDecomposeDTO.getTargetValue());
        list5.add("保底值：" + targetDecomposeDTO.getGuaranteedValue());
        list6.add("分解目标：" + targetDecomposeDTO.getDecomposeTarget());
        list7.add("");
        list8.add("注：省份维度录入汉字，部门/销售员/产品/区域/行业维度录入编码");
        list.add(list1);
        list.add(list2);
        list.add(list3);
        list.add(list4);
        list.add(list5);
        list.add(list6);
        list.add(list7);
        list.add(list8);
    }

    /**
     * 封装导出数据
     *
     * @param targetDecomposeDetailsExcelList
     * @param targetDecomposeDTO
     * @return
     */
    public static List detailsDataList(List<TargetDecomposeDetailsExcel> targetDecomposeDetailsExcelList, TargetDecomposeDTO targetDecomposeDTO) {
        //写入excel数据集合
        List<List<Object>> list = new ArrayList<List<Object>>();
        if (StringUtils.isNotEmpty(targetDecomposeDetailsExcelList)){
            //汇总金额合计
            String amountTargetSum = targetDecomposeDetailsExcelList.get(0).getAmountTargetSum();
            //周期目标值集合
            List<String> cycleTargetSum = targetDecomposeDetailsExcelList.get(0).getCycleTargetSum();
            //分解维度数据集合
            List<String> decompositionDimensions1 = targetDecomposeDetailsExcelList.get(0).getDecompositionDimensions();
            for (int i = 0; i < Math.max(targetDecomposeDetailsExcelList.size(),8); i++) {
                //每一行的数据集合
                List<Object> data = new ArrayList<Object>();
                TargetDecomposeDetailsExcel targetDecomposeDetailsExcel = new TargetDecomposeDetailsExcel();
                if (i<targetDecomposeDetailsExcelList.size()){
                     targetDecomposeDetailsExcel = targetDecomposeDetailsExcelList.get(i);
                }


                //分解维度数据集合
                List<String> decompositionDimensions = targetDecomposeDetailsExcel.getDecompositionDimensions();
                //周期目标值集合
                List<String> cycleTargets = targetDecomposeDetailsExcel.getCycleTargets();
                if (i == 0){
                    data.add("分解维度：" + StringUtils.strip(decompositionDimensions.toString(),"[]").replaceAll(",","+"));
                }else if (i ==1){
                    if (StringUtils.isNotEmpty(cycleTargets)){
                        if (cycleTargets.size()==1){
                            data.add("时间维度：年度");
                        }else if (cycleTargets.size()==4){
                            data.add("时间维度：季度");
                        }else if (cycleTargets.size()==12){
                            data.add("时间维度：月度");
                        }else if (cycleTargets.size()==52){
                            data.add("时间维度：周");
                        }
                    }
                }else if (i ==2){
                    data.add("挑战值：" + targetDecomposeDTO.getChallengeValue());
                }else if (i ==3){
                    data.add("目标值：" + targetDecomposeDTO.getTargetValue());
                }else if (i ==4){
                    data.add("保底值：" + targetDecomposeDTO.getGuaranteedValue());
                }else if (i ==5){
                    data.add("分解目标：" + targetDecomposeDTO.getDecomposeTarget());
                }else if (i ==6 ){
                    data.add("已分解：" + amountTargetSum);
                }else if (i ==7){
                    data.add("未分解：" + targetDecomposeDTO.getDecomposeTarget().subtract(new BigDecimal(amountTargetSum)));
                }
                if (StringUtils.isNotEmpty(decompositionDimensions)){
                    for (String decompositionDimension : decompositionDimensions) {
                        data.add(decompositionDimension);
                    }
                }
                if (targetDecomposeDetailsExcelList.size()<8){
                    if (targetDecomposeDetailsExcelList.size() == i){
                        data.add("");
                        data.add("合计");
                        data.add(amountTargetSum);
                        if (StringUtils.isNotEmpty(cycleTargetSum)) {
                            for (String s : cycleTargetSum) {
                                data.add(s);
                            }
                        }
                    }
                }
                //负责人名称
                data.add(targetDecomposeDetailsExcel.getPrincipalEmployeeName());
                //汇总金额
                data.add(targetDecomposeDetailsExcel.getAmountTarget());
                if (StringUtils.isNotEmpty(cycleTargets)){
                    for (String cycleTarget : cycleTargets) {
                        data.add(cycleTarget);
                    }
                }

                list.add(data);
            }
            if (targetDecomposeDetailsExcelList.size()>=8){
                //每一行的数据集合
                List<Object> dataMax = new ArrayList<Object>();
                while (dataMax.size()<(decompositionDimensions1.size()+1)){
                    dataMax.add("");
                }
                dataMax.add("合计");
                dataMax.add(amountTargetSum);
                if (StringUtils.isNotEmpty(cycleTargetSum)) {
                    for (String s : cycleTargetSum) {
                        dataMax.add(s);
                    }
                }
                list.add(dataMax);
            }
        }
        return list;
    }
}


