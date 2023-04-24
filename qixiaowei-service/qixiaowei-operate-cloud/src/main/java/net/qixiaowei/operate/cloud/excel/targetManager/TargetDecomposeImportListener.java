package net.qixiaowei.operate.cloud.excel.targetManager;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.Convert;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.dto.targetManager.DecomposeDetailCyclesDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
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

    /**
     * @param selectMap                   下拉框
     * @param targetDecomposeDTO          目标分解详情数据
     * @param parentDepartmentExcelNames  部门下拉框值
     * @param employeeExcelExcelNames     人员下拉框值
     * @param principalEmployeeExcelNames 滚动预测负责人下拉框值
     * @param parentIndustryExcelNames    行业下拉框值
     * @param parentProductExcelNames     产品下拉框值
     * @param areaExcelNames              区域下拉框值
     * @param provinceExcelNames          省份下拉框值
     * @return
     */
    public static List<List<String>> headTemplate(Map<Integer, List<String>> selectMap, TargetDecomposeDTO targetDecomposeDTO, List<String> parentDepartmentExcelNames, List<String> employeeExcelExcelNames, List<String> principalEmployeeExcelNames, List<String> parentIndustryExcelNames, List<String> parentProductExcelNames, List<String> areaExcelNames, List<String> provinceExcelNames) {
        List<Map<String, String>> fileNameList = targetDecomposeDTO.getFileNameList();
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("币种：人民币   单位：万元");
        head0.add("目标年度");
        head0.add("指标名称");
        head0.add("分解维度");
        head0.add("时间维度");
        head0.add("");
        head0.add("分解基础信息：");
        head0.add("指标名称：");
        head0.add(StringUtils.isNotBlank(targetDecomposeDTO.getIndicatorName()) ? targetDecomposeDTO.getIndicatorName() : "");
        head0.add("");
        head0.add("分解详细信息：");

        // 第二列
        List<String> head1 = new ArrayList<String>();
        head1.add("币种：人民币   单位：万元");
        head1.add(StringUtils.isNotNull(targetDecomposeDTO.getTargetYear()) ? targetDecomposeDTO.getTargetYear().toString() : "");
        head1.add(StringUtils.isNotBlank(targetDecomposeDTO.getIndicatorName()) ? targetDecomposeDTO.getIndicatorName() : "");
        head1.add(StringUtils.isNotBlank(targetDecomposeDTO.getDecompositionDimension()) ? targetDecomposeDTO.getDecompositionDimension() : "");
        if (StringUtils.isNotNull(targetDecomposeDTO.getTimeDimension())) {
            if (targetDecomposeDTO.getTimeDimension() == 1) {
                head1.add(StringUtils.isNotNull(targetDecomposeDTO.getTimeDimension()) ? "年度" : "");
            } else if (targetDecomposeDTO.getTimeDimension() == 2) {
                head1.add(StringUtils.isNotNull(targetDecomposeDTO.getTimeDimension()) ? "半年度" : "");
            } else if (targetDecomposeDTO.getTimeDimension() == 3) {
                head1.add(StringUtils.isNotNull(targetDecomposeDTO.getTimeDimension()) ? "季度" : "");
            } else if (targetDecomposeDTO.getTimeDimension() == 4) {
                head1.add(StringUtils.isNotNull(targetDecomposeDTO.getTimeDimension()) ? "月度" : "");
            } else if (targetDecomposeDTO.getTimeDimension() == 5) {
                head1.add(StringUtils.isNotNull(targetDecomposeDTO.getTimeDimension()) ? "周" : "");
            }
        }
        head1.add("");
        head1.add("");
        head1.add("挑战值");
        head1.add(null != targetDecomposeDTO.getChallengeValue() ? targetDecomposeDTO.getChallengeValue().setScale(2, RoundingMode.HALF_UP).toString() : "0.00 ");
        head1.add("");
        head1.add("");
        // 第三列
        List<String> head2 = new ArrayList<String>();
        head2.add("");
        head2.add("");
        head2.add("");
        head2.add("");
        head2.add("");
        head2.add("");
        head2.add("");
        head2.add("目标值");
        head2.add(null != targetDecomposeDTO.getTargetValue() ? targetDecomposeDTO.getTargetValue().setScale(2, RoundingMode.HALF_UP).toString() : "0.00  ");
        head2.add("");
        head2.add("");
        // 第四列
        List<String> head3 = new ArrayList<String>();
        head3.add("");
        head3.add("");
        head3.add("");
        head3.add("");
        head3.add("");
        head3.add("");
        head3.add("");
        head3.add("保底值");
        head3.add(null != targetDecomposeDTO.getGuaranteedValue() ? targetDecomposeDTO.getGuaranteedValue().setScale(2, RoundingMode.HALF_UP).toString() : "0.00   ");
        head3.add("");
        head3.add("");
        // 第五列
        List<String> head4 = new ArrayList<String>();
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("分解目标");
        head4.add(null != targetDecomposeDTO.getDecomposeTarget() ? targetDecomposeDTO.getDecomposeTarget().setScale(2, RoundingMode.HALF_UP).toString() : "0.00    ");
        head4.add("");
        head4.add("");


        for (int i = 0; i < fileNameList.size(); i++) {
            if (i == 0) {
                head0.add(fileNameList.get(i).get("label") + "*");

                dropListMap(selectMap, parentDepartmentExcelNames, employeeExcelExcelNames, parentIndustryExcelNames, parentProductExcelNames, areaExcelNames, provinceExcelNames, fileNameList, i, 0);
                if (fileNameList.size() == 1) {
                    head1.add("滚动预测负责人*");
                    selectMap.put(1, principalEmployeeExcelNames);

                    if (1 == targetDecomposeDTO.getTimeDimension()) {
                        head2.add("年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);


                    } else if (2 == targetDecomposeDTO.getTimeDimension()) {
                        head2.add("上年度");
                        head3.add("下年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);


                    } else if (3 == targetDecomposeDTO.getTimeDimension()) {
                        // 第五列
                        List<String> head5 = new ArrayList<String>();
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head2.add("一季度");
                        head3.add("二季度");
                        head4.add("三季度");
                        head5.add("四季度");

                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);

                    } else if (4 == targetDecomposeDTO.getTimeDimension()) {
                        head2.add("1月");
                        head3.add("2月");
                        head4.add("3月");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        for (int i1 = 0; i1 < 9; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add((i1 + 4) + "月");
                            list.add(head8);
                        }

                    } else if (5 == targetDecomposeDTO.getTimeDimension()) {
                        head2.add("第1周");
                        head3.add("第2周");
                        head4.add("第3周");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        for (int i1 = 0; i1 < 49; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("第" + (i1 + 4) + "周");
                            list.add(head8);
                        }
                    }

                }
            } else if (i == 1) {
                head1.add(fileNameList.get(i).get("label") + "*");
                dropListMap(selectMap, parentDepartmentExcelNames, employeeExcelExcelNames, parentIndustryExcelNames, parentProductExcelNames, areaExcelNames, provinceExcelNames, fileNameList, i, 1);
                if (fileNameList.size() == 2) {
                    head2.add("滚动预测负责人*");
                    selectMap.put(2, principalEmployeeExcelNames);
                    if (1 == targetDecomposeDTO.getTimeDimension()) {
                        head3.add("年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);

                    } else if (2 == targetDecomposeDTO.getTimeDimension()) {
                        head3.add("上年度");
                        head4.add("下年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);

                    } else if (3 == targetDecomposeDTO.getTimeDimension()) {
                        // 第五列
                        List<String> head5 = new ArrayList<String>();
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");

                        // 第五列
                        List<String> head6 = new ArrayList<String>();
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head3.add("一季度");
                        head4.add("二季度");
                        head5.add("三季度");
                        head6.add("四季度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);


                    } else if (4 == targetDecomposeDTO.getTimeDimension()) {

                        head3.add("1月");
                        head4.add("2月");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);

                        for (int i1 = 0; i1 < 10; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add((i1 + 3) + "月");
                            list.add(head8);
                        }

                    } else if (5 == targetDecomposeDTO.getTimeDimension()) {

                        head3.add("第1周");
                        head4.add("第2周");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        for (int i1 = 0; i1 < 50; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("第" + (i1 + 3) + "周");
                            list.add(head8);
                        }
                    }
                }

            } else if (i == 2) {
                head2.add(fileNameList.get(i).get("label") + "*");
                dropListMap(selectMap, parentDepartmentExcelNames, employeeExcelExcelNames, parentIndustryExcelNames, parentProductExcelNames, areaExcelNames, provinceExcelNames, fileNameList, i, 2);
                if (fileNameList.size() == 3) {
                    head3.add("滚动预测负责人*");
                    selectMap.put(3, principalEmployeeExcelNames);

                    if (1 == targetDecomposeDTO.getTimeDimension()) {
                        head4.add("年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                    } else if (2 == targetDecomposeDTO.getTimeDimension()) {
                        // 第五列
                        List<String> head5 = new ArrayList<String>();
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");

                        // 第五列
                        List<String> head6 = new ArrayList<String>();
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head5.add("上年度");
                        head6.add("下年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);

                    } else if (3 == targetDecomposeDTO.getTimeDimension()) {
                        // 第五列
                        List<String> head5 = new ArrayList<String>();
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");

                        // 第五列
                        List<String> head6 = new ArrayList<String>();
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head4.add("一季度");
                        head5.add("二季度");
                        head6.add("三季度");
                        // 动态列
                        List<String> head7 = new ArrayList<String>();
                        head7.add("");
                        head7.add("");
                        head7.add("");
                        head7.add("");
                        head7.add("");
                        head7.add("");
                        head7.add("");
                        head7.add("");
                        head7.add("");
                        head7.add("");
                        head7.add("");
                        head7.add("四季度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);


                    } else if (4 == targetDecomposeDTO.getTimeDimension()) {
                        head4.add("1月");

                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);


                        for (int i1 = 0; i1 < 11; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add((i1 + 2) + "月");
                            list.add(head8);
                        }

                    } else if (5 == targetDecomposeDTO.getTimeDimension()) {
                        head4.add("第1周");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        for (int i1 = 0; i1 < 51; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("第" + (i1 + 2) + "周");
                            list.add(head8);
                        }
                    }
                }
            } else if (i == 3) {
                head3.add(fileNameList.get(i).get("label") + "*");
                dropListMap(selectMap, parentDepartmentExcelNames, employeeExcelExcelNames, parentIndustryExcelNames, parentProductExcelNames, areaExcelNames, provinceExcelNames, fileNameList, i, 3);
                if (fileNameList.size() == 4) {
                    head4.add("滚动预测负责人*");
                    selectMap.put(4, principalEmployeeExcelNames);

                    if (1 == targetDecomposeDTO.getTimeDimension()) {
                        // 第五列
                        List<String> head5 = new ArrayList<String>();
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                    } else if (2 == targetDecomposeDTO.getTimeDimension()) {
                        // 第五列
                        List<String> head5 = new ArrayList<String>();
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");

                        // 第五列
                        List<String> head6 = new ArrayList<String>();
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head5.add("上年度");
                        head6.add("下年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                    } else if (3 == targetDecomposeDTO.getTimeDimension()) {
                        // 第五列
                        List<String> head5 = new ArrayList<String>();
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");
                        head5.add("");

                        // 第五列
                        List<String> head6 = new ArrayList<String>();
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head5.add("一季度");
                        head6.add("二季度");

                        // 动态列
                        List<String> head8 = new ArrayList<String>();
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("三季度");
                        // 动态列
                        List<String> head9 = new ArrayList<String>();
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("四季度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head8);
                        list.add(head9);

                    } else if (4 == targetDecomposeDTO.getTimeDimension()) {
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        for (int i1 = 0; i1 < 12; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add((i1 + 1) + "月");
                            list.add(head8);
                        }

                    } else if (5 == targetDecomposeDTO.getTimeDimension()) {
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        for (int i1 = 0; i1 < 52; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("第" + (i1 + 1) + "周");
                            list.add(head8);
                        }
                    }
                }
            } else if (i == 4) {
                head4.add(fileNameList.get(i).get("label") + "*");
                dropListMap(selectMap, parentDepartmentExcelNames, employeeExcelExcelNames, parentIndustryExcelNames, parentProductExcelNames, areaExcelNames, provinceExcelNames, fileNameList, i, 4);
                if (fileNameList.size() == 5) {
                    // 第五列
                    List<String> head5 = new ArrayList<String>();
                    head5.add("");
                    head5.add("");
                    head5.add("");
                    head5.add("");
                    head5.add("");
                    head5.add("");
                    head5.add("");
                    head5.add("");
                    head5.add("");
                    head5.add("");
                    head5.add("");
                    head5.add("滚动预测负责人*");
                    selectMap.put(5, principalEmployeeExcelNames);
                    if (1 == targetDecomposeDTO.getTimeDimension()) {
                        // 第五列
                        List<String> head6 = new ArrayList<String>();
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                    } else if (2 == targetDecomposeDTO.getTimeDimension()) {
                        // 动态列
                        List<String> head8 = new ArrayList<String>();
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("上年度");
                        // 动态列
                        List<String> head9 = new ArrayList<String>();
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("下年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head8);
                        list.add(head9);

                    } else if (3 == targetDecomposeDTO.getTimeDimension()) {
                        // 第五列
                        List<String> head6 = new ArrayList<String>();
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("");
                        head6.add("一季度");
                        // 动态列
                        List<String> head8 = new ArrayList<String>();
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("二季度");
                        // 动态列
                        List<String> head9 = new ArrayList<String>();
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("三季度");
                        // 动态列
                        List<String> head10 = new ArrayList<String>();
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("四季度");

                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head8);
                        list.add(head9);
                        list.add(head10);

                    } else if (4 == targetDecomposeDTO.getTimeDimension()) {

                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        for (int i1 = 0; i1 < 12; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add((i1 + 1) + "月");
                            list.add(head8);
                        }

                    } else if (5 == targetDecomposeDTO.getTimeDimension()) {
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);

                        for (int i1 = 0; i1 < 52; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("第" + (i1 + 1) + "周");
                            list.add(head8);
                        }
                    }
                }
            } else if (i == 5) {
                // 第五列
                List<String> head5 = new ArrayList<String>();
                head5.add("");
                head5.add("");
                head5.add("");
                head5.add("");
                head5.add("");
                head5.add("");
                head5.add("");
                head5.add("");
                head5.add("");
                head5.add("");
                head5.add("");
                head5.add(fileNameList.get(i).get("label") + "*");
                dropListMap(selectMap, parentDepartmentExcelNames, employeeExcelExcelNames, parentIndustryExcelNames, parentProductExcelNames, areaExcelNames, provinceExcelNames, fileNameList, i, 5);
                // 第五列
                List<String> head6 = new ArrayList<String>();
                head6.add("");
                head6.add("");
                head6.add("");
                head6.add("");
                head6.add("");
                head6.add("");
                head6.add("");
                head6.add("");
                head6.add("");
                head6.add("");
                head6.add("");
                head6.add("滚动预测负责人*");
                selectMap.put(6, principalEmployeeExcelNames);
                list.add(head0);
                list.add(head1);
                list.add(head2);
                list.add(head3);
                list.add(head4);
                list.add(head5);
                list.add(head6);
                if (1 == targetDecomposeDTO.getTimeDimension()) {
                    // 动态列
                    List<String> head8 = new ArrayList<String>();
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("年度");
                    list.add(head8);

                } else if (2 == targetDecomposeDTO.getTimeDimension()) {
                    // 动态列
                    List<String> head8 = new ArrayList<String>();
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("上年度");

                    // 动态列
                    List<String> head9 = new ArrayList<String>();
                    head9.add("");
                    head9.add("");
                    head9.add("");
                    head9.add("");
                    head9.add("");
                    head9.add("");
                    head9.add("");
                    head9.add("");
                    head9.add("");
                    head9.add("");
                    head9.add("");
                    head9.add("下年度");
                    list.add(head8);
                    list.add(head9);
                } else if (3 == targetDecomposeDTO.getTimeDimension()) {
                    for (int i1 = 1; i1 <= 4; i1++) {
                        // 动态列
                        List<String> head8 = new ArrayList<String>();
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add(Convert.int2chineseNum(i1) + "季度");
                        list.add(head8);
                    }
                } else if (4 == targetDecomposeDTO.getTimeDimension()) {
                    for (int i1 = 1; i1 <= 12; i1++) {
                        // 动态列
                        List<String> head8 = new ArrayList<String>();
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add(i1 + "月");

                        list.add(head8);
                    }
                } else if (5 == targetDecomposeDTO.getTimeDimension()) {
                    for (int i1 = 1; i1 <= 52; i1++) {
                        // 动态列
                        List<String> head8 = new ArrayList<String>();
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("第" + i1 + "周");
                        list.add(head8);
                    }
                }

            }
        }


        return list;
    }

    private static void dropListMap(Map<Integer, List<String>> selectMap, List<String> parentDepartmentExcelNames, List<String> employeeExcelExcelNames, List<String> parentIndustryExcelNames, List<String> parentProductExcelNames, List<String> areaExcelNames, List<String> provinceExcelNames, List<Map<String, String>> fileNameList, int i, int indexRow) {
        if (StringUtils.equals(fileNameList.get(i).get("label"), "销售员")) {
            selectMap.put(indexRow, employeeExcelExcelNames);
        } else if (StringUtils.equals(fileNameList.get(i).get("label"), "产品")) {
            selectMap.put(indexRow, parentProductExcelNames);
        } else if (StringUtils.equals(fileNameList.get(i).get("label"), "区域")) {
            selectMap.put(indexRow, areaExcelNames);
        } else if (StringUtils.equals(fileNameList.get(i).get("label"), "部门")) {
            selectMap.put(indexRow, parentDepartmentExcelNames);
        } else if (StringUtils.equals(fileNameList.get(i).get("label"), "省份")) {
            selectMap.put(indexRow, provinceExcelNames);
        } else if (StringUtils.equals(fileNameList.get(i).get("label"), "行业")) {
            selectMap.put(indexRow, parentIndustryExcelNames);
        }
    }

    /**
     * 导出目标分解详情数据
     *
     * @param targetDecomposeDTO
     * @return
     */
    public static List<List<String>> headDetails(TargetDecomposeDTO targetDecomposeDTO) {
        List<Map<String, String>> fileNameList = targetDecomposeDTO.getFileNameList();
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("币种：人民币   单位：万元");
        head0.add("目标年度");
        head0.add("指标名称");
        head0.add("分解维度");
        head0.add("时间维度");
        head0.add("");
        head0.add("分解基础信息：");
        head0.add("指标名称：");
        head0.add(StringUtils.isNotBlank(targetDecomposeDTO.getIndicatorName()) ? targetDecomposeDTO.getIndicatorName() : "");
        head0.add("");
        head0.add("分解详细信息：");

        // 第二列
        List<String> head1 = new ArrayList<String>();
        head1.add("币种：人民币   单位：万元");
        head1.add(StringUtils.isNotNull(targetDecomposeDTO.getTargetYear()) ? targetDecomposeDTO.getTargetYear().toString() : "");
        head1.add(StringUtils.isNotBlank(targetDecomposeDTO.getIndicatorName()) ? targetDecomposeDTO.getIndicatorName() : "");
        head1.add(StringUtils.isNotBlank(targetDecomposeDTO.getDecompositionDimension()) ? targetDecomposeDTO.getDecompositionDimension() : "");
        if (StringUtils.isNotNull(targetDecomposeDTO.getTimeDimension())) {
            if (targetDecomposeDTO.getTimeDimension() == 1) {
                head1.add(StringUtils.isNotNull(targetDecomposeDTO.getTimeDimension()) ? "年度" : "");
            } else if (targetDecomposeDTO.getTimeDimension() == 2) {
                head1.add(StringUtils.isNotNull(targetDecomposeDTO.getTimeDimension()) ? "半年度" : "");
            } else if (targetDecomposeDTO.getTimeDimension() == 3) {
                head1.add(StringUtils.isNotNull(targetDecomposeDTO.getTimeDimension()) ? "季度" : "");
            } else if (targetDecomposeDTO.getTimeDimension() == 4) {
                head1.add(StringUtils.isNotNull(targetDecomposeDTO.getTimeDimension()) ? "月度" : "");
            } else if (targetDecomposeDTO.getTimeDimension() == 5) {
                head1.add(StringUtils.isNotNull(targetDecomposeDTO.getTimeDimension()) ? "周" : "");
            }
        }
        head1.add("");
        head1.add("");
        head1.add("挑战值");
        head1.add(null != targetDecomposeDTO.getChallengeValue() ? targetDecomposeDTO.getChallengeValue().setScale(2, RoundingMode.HALF_UP).toString() : "0.00");
        head1.add("");
        head1.add("");
        // 第三列
        List<String> head2 = new ArrayList<String>();
        head2.add("");
        head2.add("");
        head2.add("");
        head2.add("");
        head2.add("");
        head2.add("");
        head2.add("");
        head2.add("目标值");
        head2.add(null != targetDecomposeDTO.getTargetValue() ? targetDecomposeDTO.getTargetValue().setScale(2, RoundingMode.HALF_UP).toString() : "0.00");
        head2.add("");
        head2.add("");
        // 第四列
        List<String> head3 = new ArrayList<String>();
        head3.add("");
        head3.add("");
        head3.add("");
        head3.add("");
        head3.add("");
        head3.add("");
        head3.add("");
        head3.add("保底值");
        head3.add(null != targetDecomposeDTO.getGuaranteedValue() ? targetDecomposeDTO.getGuaranteedValue().setScale(2, RoundingMode.HALF_UP).toString() : "0.00");
        head3.add("");
        head3.add("");
        // 第五列
        List<String> head4 = new ArrayList<String>();
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("分解目标");
        head4.add(null != targetDecomposeDTO.getDecomposeTarget() ? targetDecomposeDTO.getDecomposeTarget().setScale(2, RoundingMode.HALF_UP).toString() : "0.00");
        head4.add("");
        head4.add("");
        // 第六列
        List<String> head5 = new ArrayList<String>();
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("已分解");
        head5.add(null != targetDecomposeDTO.getDecomposed() ? targetDecomposeDTO.getDecomposed().setScale(2, RoundingMode.HALF_UP).toString() : "0.00");
        head5.add("");
        head5.add("");
        // 第七列
        List<String> head6 = new ArrayList<String>();
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("未分解");
        head6.add(null != targetDecomposeDTO.getUndecomposed() ? targetDecomposeDTO.getUndecomposed().setScale(2, RoundingMode.HALF_UP).toString() : "0.00");
        head6.add("");
        head6.add("");
        if (fileNameList.size() >= 6) {
            head6.add("滚动预测负责人");
        }

        // 第七列
        List<String> head7 = new ArrayList<String>();
        head7.add("");
        head7.add("");
        head7.add("");
        head7.add("");
        head7.add("");
        head7.add("");
        head7.add("");
        head7.add("");
        head7.add("");
        head7.add("");
        head7.add("");
        if (fileNameList.size() >= 6) {
            head7.add("汇总金额");
        }


        for (int i = 0; i < fileNameList.size(); i++) {
            if (i == 0) {
                head0.add(fileNameList.get(i).get("label"));
                if (fileNameList.size() == 1) {
                    head1.add("滚动预测负责人");
                    head2.add("汇总金额");
                    if (1 == targetDecomposeDTO.getTimeDimension()) {
                        head3.add("年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                    } else if (2 == targetDecomposeDTO.getTimeDimension()) {
                        head3.add("上年度");
                        head4.add("下年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                    } else if (3 == targetDecomposeDTO.getTimeDimension()) {
                        head3.add("一季度");
                        head4.add("二季度");
                        head5.add("三季度");
                        head6.add("四季度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);


                    } else if (4 == targetDecomposeDTO.getTimeDimension()) {
                        head3.add("1月");
                        head4.add("2月");
                        head5.add("3月");
                        head6.add("4月");
                        head7.add("5月");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);


                        for (int i1 = 0; i1 < 7; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add((i1 + 6) + "月");
                            list.add(head8);
                        }

                    } else if (5 == targetDecomposeDTO.getTimeDimension()) {
                        head3.add("第1周");
                        head4.add("第2周");
                        head5.add("第3周");
                        head6.add("第4周");
                        head7.add("第5周");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);

                        for (int i1 = 0; i1 < 47; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("第" + (i1 + 6) + "周");
                            list.add(head8);
                        }
                    }

                }
            } else if (i == 1) {
                head1.add(fileNameList.get(i).get("label"));
                if (fileNameList.size() == 2) {
                    head2.add("滚动预测负责人");
                    head3.add("汇总金额");
                    if (1 == targetDecomposeDTO.getTimeDimension()) {
                        head4.add("年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                    } else if (2 == targetDecomposeDTO.getTimeDimension()) {
                        head4.add("上年度");
                        head5.add("下年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                    } else if (3 == targetDecomposeDTO.getTimeDimension()) {
                        head4.add("一季度");
                        head5.add("二季度");
                        head6.add("三季度");
                        head7.add("四季度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);

                    } else if (4 == targetDecomposeDTO.getTimeDimension()) {
                        head4.add("1月");
                        head5.add("2月");
                        head6.add("3月");
                        head7.add("4月");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);
                        for (int i1 = 0; i1 < 8; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add((i1 + 5) + "月");
                            list.add(head8);
                        }

                    } else if (5 == targetDecomposeDTO.getTimeDimension()) {
                        head4.add("第1周");
                        head5.add("第2周");
                        head6.add("第3周");
                        head7.add("第4周");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);
                        for (int i1 = 0; i1 < 48; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("第" + (i1 + 5) + "周");
                            list.add(head8);
                        }
                    }
                }

            } else if (i == 2) {
                head2.add(fileNameList.get(i).get("label"));
                if (fileNameList.size() == 3) {
                    head3.add("滚动预测负责人");
                    head4.add("汇总金额");
                    if (1 == targetDecomposeDTO.getTimeDimension()) {
                        head5.add("年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                    } else if (2 == targetDecomposeDTO.getTimeDimension()) {
                        head5.add("上年度");
                        head6.add("下年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);


                    } else if (3 == targetDecomposeDTO.getTimeDimension()) {
                        head5.add("一季度");
                        head6.add("二季度");
                        head7.add("三季度");
                        // 动态列
                        List<String> head8 = new ArrayList<String>();
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("四季度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);
                        list.add(head8);


                    } else if (4 == targetDecomposeDTO.getTimeDimension()) {
                        head5.add("1月");
                        head6.add("2月");
                        head7.add("3月");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);

                        for (int i1 = 0; i1 < 9; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add((i1 + 4) + "月");
                            list.add(head8);
                        }

                    } else if (5 == targetDecomposeDTO.getTimeDimension()) {
                        head5.add("第1周");
                        head6.add("第2周");
                        head7.add("第3周");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);
                        for (int i1 = 0; i1 < 49; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("第" + (i1 + 4) + "周");
                            list.add(head8);
                        }
                    }
                }
            } else if (i == 3) {
                head3.add(fileNameList.get(i).get("label"));
                if (fileNameList.size() == 4) {
                    head4.add("滚动预测负责人");
                    head5.add("汇总金额");
                    if (1 == targetDecomposeDTO.getTimeDimension()) {
                        head6.add("年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                    } else if (2 == targetDecomposeDTO.getTimeDimension()) {
                        head6.add("上年度");
                        head7.add("下年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);
                    } else if (3 == targetDecomposeDTO.getTimeDimension()) {
                        head6.add("一季度");
                        head7.add("二季度");

                        // 动态列
                        List<String> head8 = new ArrayList<String>();
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("三季度");
                        // 动态列
                        List<String> head9 = new ArrayList<String>();
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("四季度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);
                        list.add(head8);
                        list.add(head9);

                    } else if (4 == targetDecomposeDTO.getTimeDimension()) {
                        head6.add("1月");
                        head7.add("2月");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);
                        for (int i1 = 0; i1 < 10; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add((i1 + 3) + "月");
                            list.add(head8);
                        }

                    } else if (5 == targetDecomposeDTO.getTimeDimension()) {
                        head6.add("第1周");
                        head7.add("第2周");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);
                        for (int i1 = 0; i1 < 50; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("第" + (i1 + 3) + "周");
                            list.add(head8);
                        }
                    }
                }
            } else if (i == 4) {
                head4.add(fileNameList.get(i).get("label"));
                if (fileNameList.size() == 5) {
                    head5.add("滚动预测负责人");
                    head6.add("汇总金额");
                    if (1 == targetDecomposeDTO.getTimeDimension()) {
                        head7.add("年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);
                    } else if (2 == targetDecomposeDTO.getTimeDimension()) {
                        // 动态列
                        List<String> head8 = new ArrayList<String>();
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("上年度");
                        // 动态列
                        List<String> head9 = new ArrayList<String>();
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("下年度");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);
                        list.add(head8);
                        list.add(head9);

                    } else if (3 == targetDecomposeDTO.getTimeDimension()) {
                        head7.add("一季度");
                        // 动态列
                        List<String> head8 = new ArrayList<String>();
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("");
                        head8.add("二季度");
                        // 动态列
                        List<String> head9 = new ArrayList<String>();
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("");
                        head9.add("三季度");
                        // 动态列
                        List<String> head10 = new ArrayList<String>();
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("");
                        head10.add("四季度");

                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);
                        list.add(head8);
                        list.add(head9);
                        list.add(head10);

                    } else if (4 == targetDecomposeDTO.getTimeDimension()) {
                        head7.add("1月");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);
                        for (int i1 = 0; i1 < 11; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add((i1 + 2) + "月");
                            list.add(head8);
                        }

                    } else if (5 == targetDecomposeDTO.getTimeDimension()) {
                        head6.add("第1周");
                        list.add(head0);
                        list.add(head1);
                        list.add(head2);
                        list.add(head3);
                        list.add(head4);
                        list.add(head5);
                        list.add(head6);
                        list.add(head7);
                        for (int i1 = 0; i1 < 51; i1++) {
                            // 动态列
                            List<String> head8 = new ArrayList<String>();
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("");
                            head8.add("第" + (i1 + 2) + "周");
                            list.add(head8);
                        }
                    }
                }
            } else if (i == 5) {
                head5.add(fileNameList.get(i).get("label"));
            }
        }


        if (fileNameList.size() >= 6) {
            list.add(head0);
            list.add(head1);
            list.add(head2);
            list.add(head3);
            list.add(head4);
            list.add(head5);
            list.add(head6);
            list.add(head7);
            if (1 == targetDecomposeDTO.getTimeDimension()) {
                // 动态列
                List<String> head8 = new ArrayList<String>();
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("年度");
                list.add(head8);

            } else if (2 == targetDecomposeDTO.getTimeDimension()) {
                // 动态列
                List<String> head8 = new ArrayList<String>();
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("");
                head8.add("上年度");

                // 动态列
                List<String> head9 = new ArrayList<String>();
                head9.add("");
                head9.add("");
                head9.add("");
                head9.add("");
                head9.add("");
                head9.add("");
                head9.add("");
                head9.add("");
                head9.add("");
                head9.add("");
                head9.add("");
                head9.add("下年度");
                list.add(head8);
                list.add(head9);
            } else if (3 == targetDecomposeDTO.getTimeDimension()) {
                for (int i = 1; i <= 4; i++) {
                    // 动态列
                    List<String> head8 = new ArrayList<String>();
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add(Convert.int2chineseNum(i) + "季度");
                    list.add(head8);
                }
            } else if (4 == targetDecomposeDTO.getTimeDimension()) {
                for (int i = 1; i <= 12; i++) {
                    // 动态列
                    List<String> head8 = new ArrayList<String>();
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add(i + "月");

                    list.add(head8);
                }
            } else if (5 == targetDecomposeDTO.getTimeDimension()) {
                for (int i = 1; i <= 52; i++) {
                    // 动态列
                    List<String> head8 = new ArrayList<String>();
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("");
                    head8.add("第" + i + "周");
                    list.add(head8);
                }
            }
        }

        return list;
    }

    /**
     * 导出滚动预测详情数据
     *
     * @param targetDecomposeDTO
     * @return
     */
    public static List<List<String>> headRollDetailsTemplate(TargetDecomposeDTO targetDecomposeDTO) {
        if (StringUtils.isNull(targetDecomposeDTO)) {
            throw new ServiceException("数据不存在 请刷新页面重试！");
        }
        List<List<String>> list = new ArrayList<List<String>>();
        List<Map<String, String>> fileNameList = targetDecomposeDTO.getFileNameList();
        String timeDimensionName = null;
        Integer timeDimension = targetDecomposeDTO.getTimeDimension();

        if (timeDimension == 1) {
            timeDimensionName = "年度";
        } else if (timeDimension == 2) {
            timeDimensionName = "半年度";
        } else if (timeDimension == 3) {
            timeDimensionName = "季度";
        } else if (timeDimension == 4) {
            timeDimensionName = "月度";
        } else if (timeDimension == 5) {
            timeDimensionName = "周";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i1 = 0; i1 < targetDecomposeDTO.getFileNameList().size(); i1++) {
            if (i1 != 0) {
                stringBuffer.append("+").append(targetDecomposeDTO.getFileNameList().get(i1).get("label"));
            } else {
                stringBuffer.append(targetDecomposeDTO.getFileNameList().get(i1).get("label"));
            }
        }
        // 动态列
        List<String> head0 = new ArrayList<String>();
        head0.add("币种：人民币   单位：万元");
        head0.add("目标年度");
        head0.add("指标名称");
        head0.add("分解维度");
        head0.add("时间维度");
        head0.add("");
        head0.add("分解详细信息：");


        List<String> head1 = new ArrayList<String>();
        head1.add("币种：人民币   单位：万元");
        head1.add(StringUtils.isNotBlank(targetDecomposeDTO.getTargetYear().toString()) ? targetDecomposeDTO.getTargetYear().toString() : "");
        head1.add(StringUtils.isNotBlank(targetDecomposeDTO.getIndicatorName()) ? targetDecomposeDTO.getIndicatorName() : "");
        head1.add(StringUtils.isNotBlank(stringBuffer.toString()) ? stringBuffer.toString() : "");
        head1.add(StringUtils.isNotBlank(timeDimensionName) ? timeDimensionName : "");
        head1.add("");
        // 动态列
        for (int i = 0; i < fileNameList.size(); i++) {
            if (i == 0) {
                head0.add(fileNameList.get(i).get("label"));
                head0.add(fileNameList.get(i).get("label"));
                list.add(head0);
                if (fileNameList.size() < 2) {
                    head1.add("");
                    head1.add("滚动预测负责人");
                    head1.add("滚动预测负责人");
                    list.add(head1);
                }
            } else if (i == 1) {
                head1.add("");
                head1.add(fileNameList.get(i).get("label"));
                head1.add(fileNameList.get(i).get("label"));
                list.add(head1);
            } else {
                // 动态列
                List<String> head2 = new ArrayList<String>();
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add(fileNameList.get(i).get("label"));
                head2.add(fileNameList.get(i).get("label"));
                list.add(head2);
            }
        }
        if (fileNameList.size() > 1) {
            List<String> head3 = new ArrayList<String>();
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("滚动预测负责人");
            head3.add("滚动预测负责人");
            list.add(head3);
        }
        if (1 == targetDecomposeDTO.getTimeDimension()) {
            // 动态列
            List<String> head10 = new ArrayList<String>();
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("整年度");
            head10.add("目标值");
            List<String> head11 = new ArrayList<String>();
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("整年度");
            head11.add("预测值");
            List<String> head12 = new ArrayList<String>();
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("整年度");
            head12.add("实际值");
            list.add(head10);
            list.add(head11);
            list.add(head12);

        } else if (2 == targetDecomposeDTO.getTimeDimension()) {
            // 动态列
            List<String> head10 = new ArrayList<String>();
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("上半年");
            head10.add("目标值");
            List<String> head11 = new ArrayList<String>();
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("上半年");
            head11.add("预测值");
            List<String> head12 = new ArrayList<String>();
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("上半年");
            head12.add("实际值");


            // 动态列
            List<String> head13 = new ArrayList<String>();
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("下半年");
            head13.add("目标值");
            List<String> head14 = new ArrayList<String>();
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("下半年");
            head14.add("预测值");
            List<String> head15 = new ArrayList<String>();
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("下半年");
            head15.add("实际值");

            list.add(head10);
            list.add(head11);
            list.add(head12);
            list.add(head13);
            list.add(head14);
            list.add(head15);
        } else if (3 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 4; i++) {
                // 动态列
                List<String> head13 = new ArrayList<String>();
                List<String> head14 = new ArrayList<String>();
                List<String> head15 = new ArrayList<String>();

                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");

                head13.add(Convert.int2chineseNum(i) + "季度");
                head14.add(Convert.int2chineseNum(i) + "季度");
                head15.add(Convert.int2chineseNum(i) + "季度");
                head13.add("目标值");
                head14.add("预测值");
                head15.add("实际值");
                list.add(head13);
                list.add(head14);
                list.add(head15);

            }
        } else if (4 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 12; i++) {
                // 动态列
                List<String> head13 = new ArrayList<String>();
                List<String> head14 = new ArrayList<String>();
                List<String> head15 = new ArrayList<String>();
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");

                head13.add(i + "月");
                head14.add(i + "月");
                head15.add(i + "月");

                head13.add("目标值");
                head14.add("预测值");
                head15.add("实际值");
                list.add(head13);
                list.add(head14);
                list.add(head15);
            }
        } else if (5 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 52; i++) {
                // 动态列
                List<String> head13 = new ArrayList<String>();
                List<String> head14 = new ArrayList<String>();
                List<String> head15 = new ArrayList<String>();
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head13.add(i + "周");
                head14.add(i + "周");
                head15.add(i + "周");


                head13.add("目标值");
                head14.add("预测值");
                head15.add("实际值");
                list.add(head13);
                list.add(head14);
                list.add(head15);
            }
        }
        return list;
    }

    /**
     * 导出滚动预测导入下载模板
     *
     * @param targetDecomposeDTO
     * @return
     */
    public static List<List<String>> headRollDetails(TargetDecomposeDTO targetDecomposeDTO) {
        if (StringUtils.isNull(targetDecomposeDTO)) {
            throw new ServiceException("数据不存在 请刷新页面重试！");
        }
        List<List<String>> list = new ArrayList<List<String>>();
        List<Map<String, String>> fileNameList = targetDecomposeDTO.getFileNameList();
        String timeDimensionName = null;
        Integer timeDimension = targetDecomposeDTO.getTimeDimension();

        if (timeDimension == 1) {
            timeDimensionName = "年度";
        } else if (timeDimension == 2) {
            timeDimensionName = "半年度";
        } else if (timeDimension == 3) {
            timeDimensionName = "季度";
        } else if (timeDimension == 4) {
            timeDimensionName = "月度";
        } else if (timeDimension == 5) {
            timeDimensionName = "周";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i1 = 0; i1 < targetDecomposeDTO.getFileNameList().size(); i1++) {
            if (i1 != 0) {
                stringBuffer.append("+").append(targetDecomposeDTO.getFileNameList().get(i1).get("label"));
            } else {
                stringBuffer.append(targetDecomposeDTO.getFileNameList().get(i1).get("label"));
            }
        }
        // 动态列
        List<String> head0 = new ArrayList<String>();
        head0.add("币种：人民币   单位：万元");
        head0.add("目标年度");
        head0.add("指标名称");
        head0.add("分解维度");
        head0.add("时间维度");
        head0.add("");
        head0.add("分解详细信息：");


        List<String> head1 = new ArrayList<String>();
        head1.add("币种：人民币   单位：万元");
        head1.add(StringUtils.isNotBlank(targetDecomposeDTO.getTargetYear().toString()) ? targetDecomposeDTO.getTargetYear().toString() : "");
        head1.add(StringUtils.isNotBlank(targetDecomposeDTO.getIndicatorName()) ? targetDecomposeDTO.getIndicatorName() : "");
        head1.add(StringUtils.isNotBlank(stringBuffer.toString()) ? stringBuffer.toString() : "");
        head1.add(StringUtils.isNotBlank(timeDimensionName) ? timeDimensionName : "");
        head1.add("");
        // 动态列
        for (int i = 0; i < fileNameList.size(); i++) {
            if (i == 0) {
                head0.add(fileNameList.get(i).get("label"));
                head0.add(fileNameList.get(i).get("label"));
                list.add(head0);
                if (fileNameList.size() < 2) {
                    head1.add("");
                    head1.add("滚动预测负责人");
                    head1.add("滚动预测负责人");
                    list.add(head1);
                }
            } else if (i == 1) {
                head1.add("");
                head1.add(fileNameList.get(i).get("label"));
                head1.add(fileNameList.get(i).get("label"));
                list.add(head1);
            } else {
                // 动态列
                List<String> head2 = new ArrayList<String>();
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add(fileNameList.get(i).get("label"));
                head2.add(fileNameList.get(i).get("label"));
                list.add(head2);
            }
        }
        if (fileNameList.size() > 1) {
            List<String> head3 = new ArrayList<String>();
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("滚动预测负责人");
            head3.add("滚动预测负责人");
            list.add(head3);
        }


        List<String> head4 = new ArrayList<String>();
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("年度");
        head4.add("分解目标");
        list.add(head4);
        List<String> head5 = new ArrayList<String>();
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("年度");
        head5.add("年度预测");
        list.add(head5);
        List<String> head6 = new ArrayList<String>();
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("年度");
        head6.add("累计实际值");
        list.add(head6);

        List<String> head8 = new ArrayList<String>();
        head8.add("");
        head8.add("");
        head8.add("");
        head8.add("");
        head8.add("");
        head8.add("");
        head8.add("");
        head8.add("年度");
        head8.add("目标完成率（%）");
        list.add(head8);

        if (1 == targetDecomposeDTO.getTimeDimension()) {
            // 动态列
            List<String> head10 = new ArrayList<String>();
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("整年度");
            head10.add("目标值");
            List<String> head11 = new ArrayList<String>();
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("整年度");
            head11.add("预测值");
            List<String> head12 = new ArrayList<String>();
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("整年度");
            head12.add("实际值");
            list.add(head10);
            list.add(head11);
            list.add(head12);

        } else if (2 == targetDecomposeDTO.getTimeDimension()) {
            // 动态列
            List<String> head10 = new ArrayList<String>();
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("上半年");
            head10.add("目标值");
            List<String> head11 = new ArrayList<String>();
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("上半年");
            head11.add("预测值");
            List<String> head12 = new ArrayList<String>();
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("上半年");
            head12.add("实际值");


            // 动态列
            List<String> head13 = new ArrayList<String>();
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("下半年");
            head13.add("目标值");
            List<String> head14 = new ArrayList<String>();
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("下半年");
            head14.add("预测值");
            List<String> head15 = new ArrayList<String>();
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("下半年");
            head15.add("实际值");

            list.add(head10);
            list.add(head11);
            list.add(head12);
            list.add(head13);
            list.add(head14);
            list.add(head15);
        } else if (3 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 4; i++) {
                // 动态列
                List<String> head13 = new ArrayList<String>();
                List<String> head14 = new ArrayList<String>();
                List<String> head15 = new ArrayList<String>();

                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");

                head13.add(Convert.int2chineseNum(i) + "季度");
                head14.add(Convert.int2chineseNum(i) + "季度");
                head15.add(Convert.int2chineseNum(i) + "季度");
                head13.add("目标值");
                head14.add("预测值");
                head15.add("实际值");
                list.add(head13);
                list.add(head14);
                list.add(head15);

            }
        } else if (4 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 12; i++) {
                // 动态列
                List<String> head13 = new ArrayList<String>();
                List<String> head14 = new ArrayList<String>();
                List<String> head15 = new ArrayList<String>();
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");

                head13.add(i + "月");
                head14.add(i + "月");
                head15.add(i + "月");

                head13.add("目标值");
                head14.add("预测值");
                head15.add("实际值");
                list.add(head13);
                list.add(head14);
                list.add(head15);
            }
        } else if (5 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 52; i++) {
                // 动态列
                List<String> head13 = new ArrayList<String>();
                List<String> head14 = new ArrayList<String>();
                List<String> head15 = new ArrayList<String>();
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head13.add(i + "周");
                head14.add(i + "周");
                head15.add(i + "周");


                head13.add("目标值");
                head14.add("预测值");
                head15.add("实际值");
                list.add(head13);
                list.add(head14);
                list.add(head15);
            }
        }
        return list;
    }

    /**
     * 经营结果分析报表导出详情Excel
     *
     * @param targetDecomposeDTO
     * @return
     */
    public static List<List<String>> headResultDetails(TargetDecomposeDTO targetDecomposeDTO) {
        if (StringUtils.isNull(targetDecomposeDTO)) {
            throw new ServiceException("数据不存在 请刷新页面重试！");
        }
        List<List<String>> list = new ArrayList<List<String>>();
        List<Map<String, String>> fileNameList = targetDecomposeDTO.getFileNameList();
        String timeDimensionName = null;
        Integer timeDimension = targetDecomposeDTO.getTimeDimension();

        if (timeDimension == 1) {
            timeDimensionName = "年度";
        } else if (timeDimension == 2) {
            timeDimensionName = "半年度";
        } else if (timeDimension == 3) {
            timeDimensionName = "季度";
        } else if (timeDimension == 4) {
            timeDimensionName = "月度";
        } else if (timeDimension == 5) {
            timeDimensionName = "周";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i1 = 0; i1 < targetDecomposeDTO.getFileNameList().size(); i1++) {
            if (i1 != 0) {
                stringBuffer.append("+").append(targetDecomposeDTO.getFileNameList().get(i1).get("label"));
            } else {
                stringBuffer.append(targetDecomposeDTO.getFileNameList().get(i1).get("label"));
            }
        }
        // 动态列
        List<String> head0 = new ArrayList<String>();
        head0.add("币种：人民币   单位：万元");
        head0.add("目标年度");
        head0.add("指标名称");
        head0.add("分解维度");
        head0.add("时间维度");
        head0.add("");
        head0.add("分解详细信息：");


        List<String> head1 = new ArrayList<String>();
        head1.add("币种：人民币   单位：万元");
        head1.add(StringUtils.isNotBlank(targetDecomposeDTO.getTargetYear().toString()) ? targetDecomposeDTO.getTargetYear().toString() : "");
        head1.add(StringUtils.isNotBlank(targetDecomposeDTO.getIndicatorName()) ? targetDecomposeDTO.getIndicatorName() : "");
        head1.add(StringUtils.isNotBlank(stringBuffer.toString()) ? stringBuffer.toString() : "");
        head1.add(StringUtils.isNotBlank(timeDimensionName) ? timeDimensionName : "");
        head1.add("");
        // 动态列
        for (int i = 0; i < fileNameList.size(); i++) {
            if (i == 0) {
                head0.add(fileNameList.get(i).get("label"));
                head0.add(fileNameList.get(i).get("label"));
                list.add(head0);
                if (fileNameList.size() < 2) {
                    head1.add("");
                    head1.add("滚动预测负责人");
                    head1.add("滚动预测负责人");
                    list.add(head1);
                }
            } else if (i == 1) {
                head1.add("");
                head1.add(fileNameList.get(i).get("label"));
                head1.add(fileNameList.get(i).get("label"));
                list.add(head1);
            } else {
                // 动态列
                List<String> head2 = new ArrayList<String>();
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add("");
                head2.add(fileNameList.get(i).get("label"));
                head2.add(fileNameList.get(i).get("label"));
                list.add(head2);
            }
        }
        if (fileNameList.size() > 1) {
            List<String> head3 = new ArrayList<String>();
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("");
            head3.add("滚动预测负责人");
            head3.add("滚动预测负责人");
            list.add(head3);
        }


        List<String> head4 = new ArrayList<String>();
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("");
        head4.add("年度");
        head4.add("分解目标");
        list.add(head4);
        List<String> head5 = new ArrayList<String>();
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("");
        head5.add("年度");
        head5.add("年度预测");
        list.add(head5);
        List<String> head6 = new ArrayList<String>();
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("");
        head6.add("年度");
        head6.add("累计实际值");
        list.add(head6);
        List<String> head7 = new ArrayList<String>();
        head7.add("");
        head7.add("");
        head7.add("");
        head7.add("");
        head7.add("");
        head7.add("");
        head7.add("");
        head7.add("年度");
        head7.add("平均预测偏差率（%）");
        list.add(head7);
        List<String> head8 = new ArrayList<String>();
        head8.add("");
        head8.add("");
        head8.add("");
        head8.add("");
        head8.add("");
        head8.add("");
        head8.add("");
        head8.add("年度");
        head8.add("目标完成率（%）");
        list.add(head8);
        List<String> head9 = new ArrayList<String>();
        head9.add("");
        head9.add("");
        head9.add("");
        head9.add("");
        head9.add("");
        head9.add("");
        head9.add("");
        head9.add("年度");
        head9.add("平均目标完成率（%）");
        list.add(head9);
        if (1 == targetDecomposeDTO.getTimeDimension()) {
            // 动态列
            List<String> head10 = new ArrayList<String>();
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("整年度");
            head10.add("目标值");
            List<String> head11 = new ArrayList<String>();
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("整年度");
            head11.add("预测值");
            List<String> head12 = new ArrayList<String>();
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("整年度");
            head12.add("实际值");
            List<String> head13 = new ArrayList<String>();
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("整年度");
            head13.add("预测偏差率（%）");
            List<String> head14 = new ArrayList<String>();
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("整年度");
            head14.add("目标完成率（%）");
            list.add(head10);
            list.add(head11);
            list.add(head12);
            list.add(head13);
            list.add(head14);

        } else if (2 == targetDecomposeDTO.getTimeDimension()) {
            // 动态列
            List<String> head10 = new ArrayList<String>();
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("");
            head10.add("上半年");
            head10.add("目标值");
            List<String> head11 = new ArrayList<String>();
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("");
            head11.add("上半年");
            head11.add("预测值");
            List<String> head12 = new ArrayList<String>();
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("");
            head12.add("上半年");
            head12.add("实际值");

            List<String> head19 = new ArrayList<String>();
            head19.add("");
            head19.add("");
            head19.add("");
            head19.add("");
            head19.add("");
            head19.add("");
            head19.add("");
            head19.add("上半年");
            head19.add("预测偏差率（%）");
            List<String> head20 = new ArrayList<String>();
            head20.add("");
            head20.add("");
            head20.add("");
            head20.add("");
            head20.add("");
            head20.add("");
            head20.add("");
            head20.add("上半年");
            head20.add("目标完成率（%）");
            // 动态列
            List<String> head13 = new ArrayList<String>();
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("");
            head13.add("下半年");
            head13.add("目标值");
            List<String> head14 = new ArrayList<String>();
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("");
            head14.add("下半年");
            head14.add("预测值");
            List<String> head15 = new ArrayList<String>();
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("");
            head15.add("下半年");
            head15.add("实际值");
            List<String> head21 = new ArrayList<String>();
            head21.add("年度:" + targetDecomposeDTO.getTargetYear() + "年");
            head21.add("指标名称：" + targetDecomposeDTO.getIndicatorName());
            head21.add("分解维度:" + stringBuffer);
            head21.add("时间维度：" + timeDimensionName);
            head21.add("下半年");
            head21.add("预测偏差率（%）");
            List<String> head22 = new ArrayList<String>();
            head22.add("年度:" + targetDecomposeDTO.getTargetYear() + "年");
            head22.add("指标名称：" + targetDecomposeDTO.getIndicatorName());
            head22.add("分解维度:" + stringBuffer);
            head22.add("时间维度：" + timeDimensionName);
            head22.add("下半年");
            head22.add("目标完成率（%）");
            list.add(head10);
            list.add(head11);
            list.add(head12);
            list.add(head19);
            list.add(head20);
            list.add(head13);
            list.add(head14);
            list.add(head15);
            list.add(head21);
            list.add(head22);
        } else if (3 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 4; i++) {
                // 动态列
                List<String> head13 = new ArrayList<String>();
                List<String> head14 = new ArrayList<String>();
                List<String> head15 = new ArrayList<String>();
                List<String> head16 = new ArrayList<String>();
                List<String> head17 = new ArrayList<String>();
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head17.add("");
                head17.add("");
                head17.add("");
                head17.add("");
                head17.add("");
                head17.add("");
                head17.add("");

                head13.add(Convert.int2chineseNum(i) + "季度");
                head14.add(Convert.int2chineseNum(i) + "季度");
                head15.add(Convert.int2chineseNum(i) + "季度");
                head16.add(Convert.int2chineseNum(i) + "季度");
                head17.add(Convert.int2chineseNum(i) + "季度");
                head13.add("目标值");
                head14.add("预测值");
                head15.add("实际值");
                head16.add("预测偏差率（%）");
                head17.add("目标完成率（%）");
                list.add(head13);
                list.add(head14);
                list.add(head15);
                list.add(head16);
                list.add(head17);

            }
        } else if (4 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 12; i++) {
                // 动态列
                List<String> head13 = new ArrayList<String>();
                List<String> head14 = new ArrayList<String>();
                List<String> head15 = new ArrayList<String>();
                List<String> head16 = new ArrayList<String>();
                List<String> head17 = new ArrayList<String>();
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head17.add("");
                head17.add("");
                head17.add("");
                head17.add("");
                head17.add("");
                head17.add("");
                head17.add("");

                head13.add(i + "月");
                head14.add(i + "月");
                head15.add(i + "月");
                head16.add(i + "月");
                head17.add(i + "月");

                head13.add("目标值");
                head14.add("预测值");
                head15.add("实际值");
                head16.add("预测偏差率（%）");
                head17.add("目标完成率（%）");
                list.add(head13);
                list.add(head14);
                list.add(head15);
                list.add(head16);
                list.add(head17);
            }
        } else if (5 == targetDecomposeDTO.getTimeDimension()) {
            for (int i = 1; i <= 52; i++) {
                // 动态列
                List<String> head13 = new ArrayList<String>();
                List<String> head14 = new ArrayList<String>();
                List<String> head15 = new ArrayList<String>();
                List<String> head16 = new ArrayList<String>();
                List<String> head17 = new ArrayList<String>();
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head13.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head14.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head15.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head16.add("");
                head17.add("");
                head17.add("");
                head17.add("");
                head17.add("");
                head17.add("");
                head17.add("");
                head17.add("");
                head13.add(i + "周");
                head14.add(i + "周");
                head15.add(i + "周");
                head16.add(i + "周");
                head17.add(i + "周");


                head13.add("目标值");
                head14.add("预测值");
                head15.add("实际值");
                head16.add("预测偏差率（%）");
                head17.add("目标完成率（%）");
                list.add(head13);
                list.add(head14);
                list.add(head15);
                list.add(head16);
                list.add(head17);
            }
        }
        return list;
    }

    /**
     * 滚动预测导入解析数据
     *
     * @param row
     * @param line
     * @param maps
     * @param list
     * @param targetDecomposeDTO
     */
    public static void mapToListModel(int row, int line, List<Map<Integer, String>> maps, List<DecomposeDetailCyclesDTO> list, TargetDecomposeDTO targetDecomposeDTO) {
        List<Integer> listNew = new ArrayList<>();
        List<Integer> listAdd = new ArrayList<>();
        List<Integer> listForecast = new ArrayList<>();
        AtomicInteger index = new AtomicInteger();
        for (int i = 0; i < maps.size(); i++) {
            //从第几行开始
            if (i == row) {
                Map<Integer, String> map = maps.get(i);
                map.forEach((key, value) -> {
                    if (StringUtils.equals(map.get(key), "滚动预测负责人")) {
                        index.set(key + 1);
                    }
                });
            }
            //从第几行开始
            if (i >= (row + 1)) {
                DecomposeDetailCyclesDTO decomposeDetailCyclesDTO = new DecomposeDetailCyclesDTO();
                Map<Integer, String> map = maps.get(i);
                for (Integer key : map.keySet()) {
                    //从第几列开始
                    if (key >= index.get()) {
                        if (StringUtils.isNull(targetDecomposeDTO)) {
                            throw new ServiceException("数据不存在  请刷新页面重试");
                        }
                        Integer timeDimension = targetDecomposeDTO.getTimeDimension();
                        if (timeDimension == 1) {
                            listNew.add(index.get());
                        } else if (timeDimension == 2) {
                            int index2 = index.get();
                            for (int i1 = 0; i1 < 2; i1++) {
                                listNew.add(index2);
                                index2 = index2 + 3;
                            }

                        } else if (timeDimension == 3) {
                            int index2 = index.get();
                            for (int i1 = 0; i1 < 4; i1++) {
                                listNew.add(index2);
                                index2 = index2 + 3;
                            }
                        } else if (timeDimension == 4) {
                            int index2 = index.get();
                            for (int i1 = 0; i1 < 12; i1++) {
                                listNew.add(index2);
                                index2 = index2 + 3;
                            }
                        } else if (timeDimension == 5) {
                            int index2 = index.get();
                            for (int i1 = 0; i1 < 52; i1++) {
                                listNew.add(index2);
                                index2 = index2 + 3;
                            }
                        }
                        if (timeDimension == 1) {
                            listForecast.add(index.get() + 1);
                        } else if (timeDimension == 2) {
                            int index2 = index.get();
                            for (int i1 = 0; i1 < 2; i1++) {
                                if (i1 == 0) {
                                    index2 = index2 + 1;
                                    listForecast.add(index2);
                                } else {
                                    index2 = index2 + 3;
                                    listForecast.add(index2);
                                }
                            }

                        } else if (timeDimension == 3) {
                            int index2 = index.get();
                            for (int i1 = 0; i1 < 4; i1++) {
                                if (i1 == 0) {
                                    index2 = index2 + 1;
                                    listForecast.add(index2);
                                } else {
                                    index2 = index2 + 3;
                                    listForecast.add(index2);
                                }
                            }
                        } else if (timeDimension == 4) {
                            int index2 = index.get();
                            for (int i1 = 0; i1 < 12; i1++) {
                                if (i1 == 0) {
                                    index2 = index2 + 1;
                                    listForecast.add(index2);
                                } else {
                                    index2 = index2 + 3;
                                    listForecast.add(index2);
                                }
                            }
                        } else if (timeDimension == 5) {
                            int index2 = index.get();
                            for (int i1 = 0; i1 < 52; i1++) {
                                if (i1 == 0) {
                                    index2 = index2 + 1;
                                    listForecast.add(index2);
                                } else {
                                    index2 = index2 + 3;
                                    listForecast.add(index2);
                                }
                            }
                        }

                        if (timeDimension == 1) {
                            listAdd.add(index.get() + 2);
                        } else if (timeDimension == 2) {
                            int index2 = index.get();
                            for (int i1 = 0; i1 < 2; i1++) {
                                if (i1 == 0) {
                                    index2 = index2 + 2;
                                    listAdd.add(index2);
                                } else {
                                    index2 = index2 + 3;
                                    listAdd.add(index2);
                                }
                            }

                        } else if (timeDimension == 3) {
                            int index2 = index.get();
                            for (int i1 = 0; i1 < 4; i1++) {
                                if (i1 == 0) {
                                    index2 = index2 + 2;
                                    listAdd.add(index2);
                                } else {
                                    index2 = index2 + 3;
                                    listAdd.add(index2);
                                }
                            }
                        } else if (timeDimension == 4) {
                            int index2 = index.get();
                            for (int i1 = 0; i1 < 12; i1++) {
                                if (i1 == 0) {
                                    index2 = index2 + 2;
                                    listAdd.add(index2);
                                } else {
                                    index2 = index2 + 3;
                                    listAdd.add(index2);
                                }
                            }
                        } else if (timeDimension == 5) {
                            int index2 = index.get();
                            for (int i1 = 0; i1 < 52; i1++) {
                                if (i1 == 0) {
                                    index2 = index2 + 2;
                                    listAdd.add(index2);
                                } else {
                                    index2 = index2 + 3;
                                    listAdd.add(index2);
                                }
                            }
                        }
                        if (listNew.stream().distinct().collect(Collectors.toList()).contains(key)) {
                            decomposeDetailCyclesDTO = new DecomposeDetailCyclesDTO();
                        }


                        if (listForecast.stream().distinct().collect(Collectors.toList()).contains(key)) {
                            decomposeDetailCyclesDTO.setCycleForecast(new BigDecimal("0"));
                            if (StringUtils.isNotBlank(map.get(key))) {
                                decomposeDetailCyclesDTO.setCycleForecast(new BigDecimal(map.get(key)));
                            }
                        }


                        if (listAdd.stream().distinct().collect(Collectors.toList()).contains(key)) {
                            decomposeDetailCyclesDTO.setCycleActual(new BigDecimal("0"));

                            if (StringUtils.isNotBlank(map.get(key))) {
                                decomposeDetailCyclesDTO.setCycleActual(new BigDecimal(map.get(key)));
                            }
                        }

                        if (listAdd.stream().distinct().collect(Collectors.toList()).contains(key)) {
                            try {
                                //添加list
                                list.add(decomposeDetailCyclesDTO);
                            } catch (Exception e) {
                                throw new ServiceException("读取excel数据 转换实体类失败！");
                            }
                        }
                    }
                }

            }
        }
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
        head0.add("币种：人民币     单位：万元");
        head0.add("目标年度");
        // 第二列
        List<String> head1 = new ArrayList<String>();
        head1.add("币种：人民币     单位：万元");
        head1.add("指标名称");
        // 第三列
        List<String> head2 = new ArrayList<String>();
        head2.add("币种：人民币     单位：万元");
        head2.add("分解维度");
        // 第四列
        List<String> head3 = new ArrayList<String>();
        head3.add("币种：人民币     单位：万元");
        head3.add("时间维度");
        // 第五列
        List<String> head4 = new ArrayList<String>();
        head4.add("币种：人民币     单位：万元");
        head4.add("公司目标");
        // 第六列
        List<String> head5 = new ArrayList<String>();
        head5.add("币种：人民币     单位：万元");
        head5.add("分解目标");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        list.add(head4);
        list.add(head5);
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
            if (StringUtils.isNull(targetDecomposeExcel.getTargetValue())) {
                targetDecomposeExcel.setTargetValue(new BigDecimal("0.00"));
            }
            if (StringUtils.isNull(targetDecomposeExcel.getDecomposeTarget())) {
                targetDecomposeExcel.setDecomposeTarget(new BigDecimal("0.00"));
            }
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
            data.add(targetDecomposeExcel.getTargetValue().setScale(2, RoundingMode.HALF_UP).toString());
            //分解目标
            data.add(targetDecomposeExcel.getDecomposeTarget().setScale(2, RoundingMode.HALF_UP).toString());
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
        list3.add("挑战值：" + (targetDecomposeDTO.getChallengeValue() == null ? 0 : targetDecomposeDTO.getChallengeValue()));
        list4.add("目标值：" + (targetDecomposeDTO.getTargetValue() == null ? 0 : targetDecomposeDTO.getTargetValue()));
        list5.add("保底值：" + (targetDecomposeDTO.getGuaranteedValue() == null ? 0 : targetDecomposeDTO.getGuaranteedValue()));
        list6.add("分解目标：" + (targetDecomposeDTO.getDecomposeTarget() == null ? 0 : targetDecomposeDTO.getDecomposeTarget()));
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
        if (StringUtils.isNotEmpty(targetDecomposeDetailsExcelList)) {
            //汇总金额合计
            String amountTargetSum = targetDecomposeDetailsExcelList.get(0).getAmountTargetSum();
            //周期目标值总计集合
            List<String> cycleTargetSum = targetDecomposeDetailsExcelList.get(0).getCycleTargetSum();
            //分解维度数据集合
            List<String> decompositionDimensions1 = targetDecomposeDetailsExcelList.get(0).getDecompositionDimensions();

            for (int i = 0; i < targetDecomposeDetailsExcelList.size(); i++) {
                //每一行的数据集合
                List<Object> data = new ArrayList<Object>();
                TargetDecomposeDetailsExcel targetDecomposeDetailsExcel = targetDecomposeDetailsExcelList.get(i);

                //分解维度数据集合
                List<String> decompositionDimensions = targetDecomposeDetailsExcel.getDecompositionDimensions();
                //周期目标值集合
                List<String> cycleTargets = targetDecomposeDetailsExcel.getCycleTargets();

                if (StringUtils.isNotEmpty(decompositionDimensions)) {
                    for (String decompositionDimension : decompositionDimensions) {
                        data.add(decompositionDimension);
                    }
                }

                //负责人名称
                data.add(targetDecomposeDetailsExcel.getPrincipalEmployeeName());
                //汇总金额
                data.add(StringUtils.isNotBlank(targetDecomposeDetailsExcel.getAmountTarget()) ? targetDecomposeDetailsExcel.getAmountTarget() : "0.00");
                if (StringUtils.isNotEmpty(cycleTargets)) {
                    for (String cycleTarget : cycleTargets) {
                        data.add(StringUtils.isNotBlank(cycleTarget) ? cycleTarget : "0.00");
                    }
                }

                list.add(data);
            }

            //每一行的数据集合
            List<Object> dataMax = new ArrayList<Object>();
            while (dataMax.size() < decompositionDimensions1.size()) {
                dataMax.add("");
            }
            dataMax.add("合计");
            dataMax.add(StringUtils.isNotBlank(amountTargetSum) ? amountTargetSum : "0.00");
            if (StringUtils.isNotEmpty(cycleTargetSum)) {
                for (String s : cycleTargetSum) {
                    dataMax.add(StringUtils.isNotBlank(s) ? s : "0.00");
                }
            }
            list.add(dataMax);
        }
        return list;
    }


    /**
     * 封装滚动预测导出数据
     *
     * @param targetDecomposeDetailsDTOS
     * @param targetDecomposeDTO
     * @return
     */
    public static List detailsRollDataList(List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS, TargetDecomposeDTO targetDecomposeDTO, boolean totalFlag) {
        List<Map<String, String>> fileNameList = new ArrayList<>();
        if (StringUtils.isNotNull(targetDecomposeDTO)) {
            fileNameList = targetDecomposeDTO.getFileNameList();
        }

        //写入excel数据集合
        List<List<Object>> list = new ArrayList<List<Object>>();
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOS)) {
            TargetDecomposeDetailsDTO targetDecomposeDetailsDTO1 = new TargetDecomposeDetailsDTO();
            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOAllList = new ArrayList<>();
            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOSizeList = new ArrayList<>();
            for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOS) {
                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS = targetDecomposeDetailsDTO.getDecomposeDetailCyclesDTOS();
                //分解目标
                if (null == targetDecomposeDetailsDTO.getAmountTarget() || targetDecomposeDetailsDTO.getAmountTarget().compareTo(new BigDecimal("0")) == 0) {
                    targetDecomposeDetailsDTO.setAmountTarget(new BigDecimal("0.00"));
                }
                //年度预测值
                if (null == targetDecomposeDetailsDTO.getForecastYear() || targetDecomposeDetailsDTO.getForecastYear().compareTo(new BigDecimal("0")) == 0) {
                    targetDecomposeDetailsDTO.setForecastYear(new BigDecimal("0.00"));
                }
                //累计实际值
                if (null == targetDecomposeDetailsDTO.getActualTotal() || targetDecomposeDetailsDTO.getActualTotal().compareTo(new BigDecimal("0")) == 0) {
                    targetDecomposeDetailsDTO.setActualTotal(new BigDecimal("0.00"));
                }
                //目标完成率
                if (null == targetDecomposeDetailsDTO.getTargetPercentageComplete() || targetDecomposeDetailsDTO.getTargetPercentageComplete().compareTo(new BigDecimal("0")) == 0) {
                    targetDecomposeDetailsDTO.setTargetPercentageComplete(new BigDecimal("0.00"));
                }
                if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOS)) {
                    for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOS) {
                        //周期目标值
                        if (null == decomposeDetailCyclesDTO.getCycleTarget() || decomposeDetailCyclesDTO.getCycleTarget().compareTo(new BigDecimal("0")) == 0) {
                            decomposeDetailCyclesDTO.setCycleTarget(new BigDecimal("0.00"));
                        }
                        //周期预测值
                        if (null == decomposeDetailCyclesDTO.getCycleForecast() || decomposeDetailCyclesDTO.getCycleForecast().compareTo(new BigDecimal("0")) == 0) {
                            decomposeDetailCyclesDTO.setCycleForecast(new BigDecimal("0.00"));
                        }
                        //周期实际值
                        if (null == decomposeDetailCyclesDTO.getCycleActual() || decomposeDetailCyclesDTO.getCycleActual().compareTo(new BigDecimal("0")) == 0) {
                            decomposeDetailCyclesDTO.setCycleActual(new BigDecimal("0.00"));
                        }
                    }
                }
                decomposeDetailCyclesDTOAllList.addAll(decomposeDetailCyclesDTOS);
            }

            BigDecimal amountTarget = targetDecomposeDetailsDTOS.stream().map(TargetDecomposeDetailsDTO::getAmountTarget).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            BigDecimal actualTotal = targetDecomposeDetailsDTOS.stream().map(TargetDecomposeDetailsDTO::getActualTotal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            targetDecomposeDetailsDTO1.setAmountTarget(amountTarget);
            targetDecomposeDetailsDTO1.setForecastYear(targetDecomposeDetailsDTOS.stream().map(TargetDecomposeDetailsDTO::getForecastYear).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
            targetDecomposeDetailsDTO1.setActualTotal(actualTotal);
            BigDecimal targetPercentageComplete = new BigDecimal("0.00");
            if (amountTarget.compareTo(new BigDecimal("0")) != 0 && actualTotal.compareTo(new BigDecimal("0")) != 0) {
                targetPercentageComplete = actualTotal.divide(amountTarget, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
            }
            //目标完成率
            targetDecomposeDetailsDTO1.setTargetPercentageComplete(targetPercentageComplete);
            if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOAllList)) {
                //根据周期数(顺序递增)分组
                Map<Integer, List<DecomposeDetailCyclesDTO>> decomposeDetailCyclesDataMap = decomposeDetailCyclesDTOAllList.parallelStream().filter(f -> null != f.getCycleNumber()).collect(Collectors.groupingBy(DecomposeDetailCyclesDTO::getCycleNumber));
                for (Integer key : decomposeDetailCyclesDataMap.keySet()) {
                    List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = decomposeDetailCyclesDataMap.get(key);
                    if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOList)) {
                        DecomposeDetailCyclesDTO decomposeDetailCyclesDTO = new DecomposeDetailCyclesDTO();
                        //周期目标值
                        BigDecimal cycleTarget = decomposeDetailCyclesDTOList.stream().map(DecomposeDetailCyclesDTO::getCycleTarget).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);
                        //周期实际值
                        BigDecimal cycleActual = decomposeDetailCyclesDTOList.stream().map(DecomposeDetailCyclesDTO::getCycleActual).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);
                        //周期预测值
                        BigDecimal cycleForecast = decomposeDetailCyclesDTOList.stream().map(DecomposeDetailCyclesDTO::getCycleForecast).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);
                        decomposeDetailCyclesDTO.setCycleNumber(key);
                        decomposeDetailCyclesDTO.setCycleTarget(cycleTarget);
                        decomposeDetailCyclesDTO.setCycleActual(cycleActual);
                        decomposeDetailCyclesDTO.setCycleForecast(cycleForecast);
                        decomposeDetailCyclesDTOSizeList.add(decomposeDetailCyclesDTO);

                    }
                }
            }
            targetDecomposeDetailsDTO1.setDecomposeDetailCyclesDTOS(decomposeDetailCyclesDTOSizeList);
            for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOS) {
                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS = targetDecomposeDetailsDTO.getDecomposeDetailCyclesDTOS();

                List<Object> data = new ArrayList<>();
                if (StringUtils.isNotEmpty(fileNameList)) {
                    for (Map<String, String> stringStringMap : fileNameList) {
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("employeeId", stringStringMap.get("value"))) {
                            data.add(targetDecomposeDetailsDTO.getEmployeeName());
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("areaId", stringStringMap.get("value"))) {
                            data.add(targetDecomposeDetailsDTO.getAreaName());
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("departmentId", stringStringMap.get("value"))) {
                            data.add(targetDecomposeDetailsDTO.getDepartmentName());
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("productId", stringStringMap.get("value"))) {
                            data.add(targetDecomposeDetailsDTO.getProductName());
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("regionId", stringStringMap.get("value"))) {
                            data.add(targetDecomposeDetailsDTO.getRegionName());
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("industryId", stringStringMap.get("value"))) {
                            data.add(targetDecomposeDetailsDTO.getIndustryName());
                        }
                    }
                }
                //滚动预测负责人
                data.add(targetDecomposeDetailsDTO.getPrincipalEmployeeName());
                if (totalFlag) {
                    //分解目标
                    data.add(targetDecomposeDetailsDTO.getAmountTarget().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    //年度预测值
                    data.add(targetDecomposeDetailsDTO.getForecastYear().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    //累计实际值
                    data.add(targetDecomposeDetailsDTO.getActualTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    //目标完成率
                    data.add(targetDecomposeDetailsDTO.getTargetPercentageComplete().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }

                if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOS)) {
                    for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOS) {
                        //周期目标值
                        data.add(decomposeDetailCyclesDTO.getCycleTarget().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        //周期预测值
                        data.add(decomposeDetailCyclesDTO.getCycleForecast().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        //周期实际值
                        data.add(decomposeDetailCyclesDTO.getCycleActual().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                }
                list.add(data);
            }

            if (totalFlag) {
                List<Object> data = new ArrayList<>();
                if (StringUtils.isNotEmpty(fileNameList)) {
                    for (Map<String, String> stringStringMap : fileNameList) {
                        data.add("");
                    }
                }
                data.add("合计");
                //分解目标
                data.add(targetDecomposeDetailsDTO1.getAmountTarget().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                //年度预测值
                data.add(targetDecomposeDetailsDTO1.getForecastYear().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                //累计实际值
                data.add(targetDecomposeDetailsDTO1.getActualTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                //目标完成率
                data.add(targetDecomposeDetailsDTO1.getTargetPercentageComplete().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS = targetDecomposeDetailsDTO1.getDecomposeDetailCyclesDTOS();
                if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOS)) {
                    for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOS) {
                        //周期目标值
                        data.add(decomposeDetailCyclesDTO.getCycleTarget().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        //周期预测值
                        data.add(decomposeDetailCyclesDTO.getCycleForecast().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        //周期实际值
                        data.add(decomposeDetailCyclesDTO.getCycleActual().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                }
                list.add(data);
            }
        }
        return list;
    }

    /**
     * 封装经营结果分析报表导出数据
     *
     * @param targetDecomposeDetailsDTOS
     * @param targetDecomposeDTO
     * @return
     */
    public static List detailsResultDataList(List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS, TargetDecomposeDTO targetDecomposeDTO, boolean totalFlag) {
        List<Map<String, String>> fileNameList = new ArrayList<>();
        if (StringUtils.isNotNull(targetDecomposeDTO)) {
            fileNameList = targetDecomposeDTO.getFileNameList();
        }
        //写入excel数据集合
        List<List<Object>> list = new ArrayList<List<Object>>();
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOS)) {
            TargetDecomposeDetailsDTO targetDecomposeDetailsDTO1 = new TargetDecomposeDetailsDTO();
            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOAllList = new ArrayList<>();
            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOSizeList = new ArrayList<>();
            for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOS) {

                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS = targetDecomposeDetailsDTO.getDecomposeDetailCyclesDTOS();
                //分解目标
                if (null == targetDecomposeDetailsDTO.getAmountTarget()) {
                    targetDecomposeDetailsDTO.setAmountTarget(new BigDecimal("0.00").setScale(2));
                }
                //年度预测值
                if (null == targetDecomposeDetailsDTO.getForecastYear()) {
                    targetDecomposeDetailsDTO.setForecastYear(new BigDecimal("0.00").setScale(2));
                }
                //累计实际值
                if (null == targetDecomposeDetailsDTO.getActualTotal()) {
                    targetDecomposeDetailsDTO.setActualTotal(new BigDecimal("0.00").setScale(2));
                }
                //预测与目标偏差率平均值
                if (null == targetDecomposeDetailsDTO.getForecastDeviationRateAve()) {
                    targetDecomposeDetailsDTO.setForecastDeviationRateAve(new BigDecimal("0.00").setScale(2));
                }
                //目标完成率
                if (null == targetDecomposeDetailsDTO.getTargetPercentageComplete()) {
                    targetDecomposeDetailsDTO.setTargetPercentageComplete(new BigDecimal("0.00").setScale(2));
                }
                //目标完成率平均值
                if (null == targetDecomposeDetailsDTO.getTargetPercentageCompleteAve()) {
                    targetDecomposeDetailsDTO.setTargetPercentageCompleteAve(new BigDecimal("0.00").setScale(2));
                }
                if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOS)) {
                    for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOS) {
                        //周期目标值
                        if (null == decomposeDetailCyclesDTO.getCycleTarget()) {
                            decomposeDetailCyclesDTO.setCycleTarget(new BigDecimal("0.00").setScale(2));
                        }
                        //周期预测值
                        if (null == decomposeDetailCyclesDTO.getCycleForecast()) {
                            decomposeDetailCyclesDTO.setCycleForecast(new BigDecimal("0.00").setScale(2));
                        }
                        //周期实际值
                        if (null == decomposeDetailCyclesDTO.getCycleActual()) {
                            decomposeDetailCyclesDTO.setCycleActual(new BigDecimal("0.00").setScale(2));
                        }

                        //预测偏差
                        if (null == decomposeDetailCyclesDTO.getCycleForecastDeviation()) {
                            decomposeDetailCyclesDTO.setCycleForecastDeviation(new BigDecimal("0.00").setScale(2));
                        }
                        //目标完成率
                        if (null == decomposeDetailCyclesDTO.getCyclePercentageComplete()) {
                            decomposeDetailCyclesDTO.setCyclePercentageComplete(new BigDecimal("0.00").setScale(2));
                        }
                    }
                }
                decomposeDetailCyclesDTOAllList.addAll(decomposeDetailCyclesDTOS);
            }

            BigDecimal amountTarget = targetDecomposeDetailsDTOS.stream().map(TargetDecomposeDetailsDTO::getAmountTarget).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal actualTotal = targetDecomposeDetailsDTOS.stream().map(TargetDecomposeDetailsDTO::getActualTotal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            targetDecomposeDetailsDTO1.setAmountTarget(amountTarget.setScale(2, BigDecimal.ROUND_HALF_UP));
            targetDecomposeDetailsDTO1.setForecastYear(targetDecomposeDetailsDTOS.stream().map(TargetDecomposeDetailsDTO::getForecastYear).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP));
            targetDecomposeDetailsDTO1.setActualTotal(actualTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
            BigDecimal targetPercentageComplete = new BigDecimal("0");
            if (amountTarget.compareTo(new BigDecimal("0")) != 0 && actualTotal.compareTo(new BigDecimal("0")) != 0) {
                targetPercentageComplete = actualTotal.divide(amountTarget, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
            }
            //目标完成率
            targetDecomposeDetailsDTO1.setTargetPercentageComplete(targetPercentageComplete.setScale(2, BigDecimal.ROUND_HALF_UP));
            if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOAllList)) {
                //根据周期数(顺序递增)分组
                Map<Integer, List<DecomposeDetailCyclesDTO>> decomposeDetailCyclesDataMap = decomposeDetailCyclesDTOAllList.parallelStream().filter(f -> null != f.getCycleNumber()).collect(Collectors.groupingBy(DecomposeDetailCyclesDTO::getCycleNumber));
                for (Integer key : decomposeDetailCyclesDataMap.keySet()) {
                    List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = decomposeDetailCyclesDataMap.get(key);
                    if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOList)) {
                        DecomposeDetailCyclesDTO decomposeDetailCyclesDTO = new DecomposeDetailCyclesDTO();
                        //周期目标值
                        BigDecimal cycleTarget = decomposeDetailCyclesDTOList.stream().map(DecomposeDetailCyclesDTO::getCycleTarget).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                        //周期实际值
                        BigDecimal cycleActual = decomposeDetailCyclesDTOList.stream().map(DecomposeDetailCyclesDTO::getCycleActual).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                        //周期预测值
                        BigDecimal cycleForecast = decomposeDetailCyclesDTOList.stream().map(DecomposeDetailCyclesDTO::getCycleForecast).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal cycleForecastDeviation = new BigDecimal("0");
                        BigDecimal cyclePercentageComplete = new BigDecimal("0");
                        if (cycleActual.subtract(cycleForecast).compareTo(new BigDecimal("0")) != 0 && cycleActual.compareTo(new BigDecimal("0")) != 0) {
                            //预测合计
                            cycleForecastDeviation = cycleActual.subtract(cycleForecast).divide(cycleActual, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
                        }
                        if (cycleActual.subtract(cycleForecast).compareTo(new BigDecimal("0")) != 0 && cycleActual.compareTo(new BigDecimal("0")) != 0) {
                            //目标完成率
                            cyclePercentageComplete = cycleActual.divide(cycleTarget, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
                        }

                        decomposeDetailCyclesDTO.setCycleNumber(key);
                        decomposeDetailCyclesDTO.setCycleTarget(cycleTarget.setScale(2, BigDecimal.ROUND_HALF_UP));
                        decomposeDetailCyclesDTO.setCycleActual(cycleActual.setScale(2, BigDecimal.ROUND_HALF_UP));
                        decomposeDetailCyclesDTO.setCycleForecast(cycleForecast.setScale(2, BigDecimal.ROUND_HALF_UP));
                        decomposeDetailCyclesDTO.setCycleForecastDeviation(cycleForecastDeviation.setScale(2, BigDecimal.ROUND_HALF_UP));
                        decomposeDetailCyclesDTO.setCyclePercentageComplete(cyclePercentageComplete.setScale(2, BigDecimal.ROUND_HALF_UP));

                        decomposeDetailCyclesDTOSizeList.add(decomposeDetailCyclesDTO);

                    }
                }
            }
            targetDecomposeDetailsDTO1.setDecomposeDetailCyclesDTOS(decomposeDetailCyclesDTOSizeList);
            for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOS) {
                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS = targetDecomposeDetailsDTO.getDecomposeDetailCyclesDTOS();

                List<Object> data = new ArrayList<>();
                if (StringUtils.isNotEmpty(fileNameList)) {
                    for (Map<String, String> stringStringMap : fileNameList) {
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("employeeId", stringStringMap.get("value"))) {
                            data.add(targetDecomposeDetailsDTO.getEmployeeName());
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("areaId", stringStringMap.get("value"))) {
                            data.add(targetDecomposeDetailsDTO.getAreaName());
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("departmentId", stringStringMap.get("value"))) {
                            data.add(targetDecomposeDetailsDTO.getDepartmentName());
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("productId", stringStringMap.get("value"))) {
                            data.add(targetDecomposeDetailsDTO.getProductName());
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("regionId", stringStringMap.get("value"))) {
                            data.add(targetDecomposeDetailsDTO.getRegionName());
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("industryId", stringStringMap.get("value"))) {
                            data.add(targetDecomposeDetailsDTO.getIndustryName());
                        }
                    }
                }
                //滚动预测负责人
                data.add(targetDecomposeDetailsDTO.getPrincipalEmployeeName());
                //分解目标
                data.add(targetDecomposeDetailsDTO.getAmountTarget().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                //年度预测值
                data.add(targetDecomposeDetailsDTO.getForecastYear().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                //累计实际值
                data.add(targetDecomposeDetailsDTO.getActualTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                //平均预测偏差率（%）
                data.add(targetDecomposeDetailsDTO.getForecastDeviationRateAve().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                //目标完成率
                data.add(targetDecomposeDetailsDTO.getTargetPercentageComplete().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                //平均目标完成率（%）
                data.add(targetDecomposeDetailsDTO.getTargetPercentageCompleteAve().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOS)) {
                    for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOS) {
                        //周期目标值
                        data.add(decomposeDetailCyclesDTO.getCycleTarget().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        //周期预测值
                        data.add(decomposeDetailCyclesDTO.getCycleForecast().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        //周期实际值
                        data.add(decomposeDetailCyclesDTO.getCycleActual().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        //预测偏差
                        data.add(decomposeDetailCyclesDTO.getCycleForecastDeviation().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        //目标完成率
                        data.add(decomposeDetailCyclesDTO.getCyclePercentageComplete().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                }
                list.add(data);
            }
            if (totalFlag) {
                List<Object> data = new ArrayList<>();
                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS = targetDecomposeDetailsDTO1.getDecomposeDetailCyclesDTOS();
                for (Map<String, String> stringStringMap : fileNameList) {
                    data.add("");
                }
                data.add("合计");
                //分解目标
                data.add(targetDecomposeDetailsDTO1.getAmountTarget().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                //年度预测值
                data.add(targetDecomposeDetailsDTO1.getForecastYear().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                //累计实际值
                data.add(targetDecomposeDetailsDTO1.getActualTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOS)) {
                    BigDecimal cycleForecastDeviationSum = new BigDecimal("0");
                    for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOS) {
                        cycleForecastDeviationSum = cycleForecastDeviationSum.add(decomposeDetailCyclesDTO.getCycleForecastDeviation().abs());
                    }
                    if (cycleForecastDeviationSum.compareTo(new BigDecimal("0")) != 0) {
                        //平均预测偏差率（%）
                        data.add(cycleForecastDeviationSum.divide(new BigDecimal(String.valueOf(decomposeDetailCyclesDTOS.size())), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    } else {
                        //平均预测偏差率（%）
                        data.add("0.00");
                    }
                }

                //目标完成率
                data.add(targetDecomposeDetailsDTO1.getTargetPercentageComplete().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOS)) {
                    BigDecimal cyclePercentageCompleteSum = new BigDecimal("0");
                    for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOS) {
                        cyclePercentageCompleteSum = cyclePercentageCompleteSum.add(decomposeDetailCyclesDTO.getCyclePercentageComplete().abs());
                    }
                    if (cyclePercentageCompleteSum.compareTo(new BigDecimal("0")) != 0) {
                        //平均预测偏差率（%）
                        data.add(cyclePercentageCompleteSum.divide(new BigDecimal(String.valueOf(decomposeDetailCyclesDTOS.size())), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    } else {
                        //平均预测偏差率（%）
                        data.add("0.00");
                    }
                }

                if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOS)) {
                    for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOS) {
                        //周期目标值
                        data.add(decomposeDetailCyclesDTO.getCycleTarget().setScale(2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        //周期预测值
                        data.add(decomposeDetailCyclesDTO.getCycleForecast().setScale(2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        //周期实际值
                        data.add(decomposeDetailCyclesDTO.getCycleActual().setScale(2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        //预测偏差
                        data.add(decomposeDetailCyclesDTO.getCycleForecastDeviation().setScale(2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        //目标完成率
                        data.add(decomposeDetailCyclesDTO.getCyclePercentageComplete().setScale(2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                }
                list.add(data);
            }
        }
        return list;
    }
}


