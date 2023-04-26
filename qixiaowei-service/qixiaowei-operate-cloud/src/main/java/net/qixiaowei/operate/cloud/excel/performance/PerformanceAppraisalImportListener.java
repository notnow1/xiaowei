package net.qixiaowei.operate.cloud.excel.performance;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalColumnsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;

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
    public static List<List<String>> headOrgSystemTemplate(Map<Integer, List<String>> selectMap, List<PerformanceRankFactorDTO> performanceRankFactorDTOS, List<EmployeeDTO> employeeData, boolean isError) {
        List<String> listSelectTwo = new ArrayList<>();
        for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
            listSelectTwo.add(performanceRankFactorDTO.getPerformanceRankName());
        }
        listSelectTwo.add("不考核");
        List<String> listSelectThree = new ArrayList<>();
        for (EmployeeDTO employeeDatum : employeeData) {
            listSelectThree.add(employeeDatum.getEmployeeName() + "（" + employeeDatum.getEmployeeCode() + "）");
        }
        List<List<String>> list = new ArrayList<List<String>>();
        if (isError) {
            // 第0列
            List<String> head = new ArrayList<String>();
            head.add("错误信息");
            list.add(head);
        }
        // 第1列
        List<String> head0 = new ArrayList<String>();
        head0.add("考核对象");
        // 第1列
        List<String> head1 = new ArrayList<String>();
        head1.add("评议总分数");
        // 第2列
        List<String> head2 = new ArrayList<String>();
        head2.add("考核结果*");
        selectMap.put(isError ? 3 : 2, listSelectTwo);
        // 第3列
        List<String> head3 = new ArrayList<String>();
        head3.add("考核责任人");
        selectMap.put(isError ? 4 : 3, listSelectThree);
        // 第4列
        List<String> head4 = new ArrayList<String>();
        head4.add("备注");
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
        head0.add("组织编码");
        // 第2列
        List<String> head1 = new ArrayList<String>();
        head1.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head1.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head1.add("   3、用户可在后面追加、编辑自定义列。");
        head1.add("考核对象");
        // 第2列
        List<String> head2 = new ArrayList<String>();
        head2.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head2.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head2.add("   3、用户可在后面追加、编辑自定义列。");
        head2.add("考核结果");
        selectMap.put(2, listSelect);
        // 第3列
        List<String> head3 = new ArrayList<String>();
        head3.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head3.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head3.add("   3、用户可在后面追加、编辑自定义列。");
        head3.add("自定义列1");
        // 第4列
        List<String> head4 = new ArrayList<String>();
        head4.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head4.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head4.add("   3、用户可在后面追加、编辑自定义列。");
        head4.add("自定义列2");
        // 第4列
        List<String> head5 = new ArrayList<String>();
        head5.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head5.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head5.add("   3、用户可在后面追加、编辑自定义列。");
        head5.add("自定义列3");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        list.add(head4);
        list.add(head5);
        return list;
    }

    /**
     * 定义组织自定义导出模板 表头
     *
     * @return List
     */
    public static List<List<String>> headOrgCustom(Map<Integer, List<String>> selectMap, List<PerformanceRankFactorDTO> performanceRankFactorDTOS, List<PerformanceAppraisalColumnsDTO> appraisalColumnsDTOList) {
        List<String> columnList = new ArrayList<>();
        if (StringUtils.isNotEmpty(appraisalColumnsDTOList)) {
            for (PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO : appraisalColumnsDTOList) {
                columnList.add(performanceAppraisalColumnsDTO.getColumnName());
            }
        }
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
        head0.add("组织编码");
        // 第2列
        List<String> head1 = new ArrayList<String>();
        head1.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head1.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head1.add("   3、用户可在后面追加、编辑自定义列。");
        head1.add("考核对象");
        // 第2列
        List<String> head2 = new ArrayList<String>();
        head2.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head2.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head2.add("   3、用户可在后面追加、编辑自定义列。");
        head2.add("考核结果");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        selectMap.put(2, listSelect);
        for (String column : columnList) {
            List<String> head = new ArrayList<String>();
            head.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
            head.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
            head.add("   3、用户可在后面追加、编辑自定义列。");
            head.add(column);
            list.add(head);
        }
        return list;
    }

    /**
     * 个人绩效归档导入系统模板
     *
     * @return 结果
     */
    public static List<List<String>> headPerSystemTemplate(Map<Integer, List<String>> selectMap, List<PerformanceRankFactorDTO> performanceRankFactorDTOS, List<EmployeeDTO> employeeData, boolean isError) {
        List<String> listSelectSix = new ArrayList<>();
        for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
            listSelectSix.add(performanceRankFactorDTO.getPerformanceRankName());
        }
        listSelectSix.add("不考核");
        List<String> listSelectSeven = new ArrayList<>();
        for (EmployeeDTO employeeDatum : employeeData) {
            listSelectSeven.add(employeeDatum.getEmployeeName() + "（" + employeeDatum.getEmployeeCode() + "）");
        }
        List<List<String>> list = new ArrayList<List<String>>();
        if (isError) {
            // 第0列
            List<String> head = new ArrayList<String>();
            head.add("错误信息");
            list.add(head);
        }
        // 第1列
        List<String> head0 = new ArrayList<String>();
        head0.add("员工工号");
        // 第2列
        List<String> head1 = new ArrayList<String>();
        head1.add("员工姓名");
        // 第3列
        List<String> head2 = new ArrayList<String>();
        head2.add("部门");
        // 第4列
        List<String> head3 = new ArrayList<String>();
        head3.add("岗位");
        // 第4列
        List<String> head4 = new ArrayList<String>();
        head4.add("个人职级");
        // 第4列
        List<String> head5 = new ArrayList<String>();
        head5.add("评议总分数");
        // 第4列
        List<String> head6 = new ArrayList<String>();
        selectMap.put(isError ? 7 : 6, listSelectSix);
        head6.add("考核结果*");
        // 第4列
        List<String> head7 = new ArrayList<String>();
        selectMap.put(isError ? 8 : 7, listSelectSeven);
        head7.add("考核责任人");
        // 第4列
        List<String> head8 = new ArrayList<String>();
        head8.add("备注");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        list.add(head4);
        list.add(head5);
        list.add(head6);
        list.add(head7);
        list.add(head8);
        return list;
    }

    /**
     * 个人绩效归档导入自定义模板
     *
     * @return 结果
     */
    public static List<List<String>> headPerCustomTemplate(Map<Integer, List<String>> selectMap, List<PerformanceRankFactorDTO> performanceRankFactorDTOS) {
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
        head0.add("员工工号");
        // 第2列
        List<String> head1 = new ArrayList<String>();
        head1.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head1.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head1.add("   3、用户可在后面追加、编辑自定义列。");
        head1.add("员工姓名");
        // 第3列
        List<String> head2 = new ArrayList<String>();
        head2.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head2.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head2.add("   3、用户可在后面追加、编辑自定义列。");
        head2.add("岗位");
        // 第4列
        List<String> head3 = new ArrayList<String>();
        head3.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head3.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head3.add("   3、用户可在后面追加、编辑自定义列。");
        head3.add("部门");
        // 第4列
        List<String> head4 = new ArrayList<String>();
        head4.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head4.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head4.add("   3、用户可在后面追加、编辑自定义列。");
        head4.add("个人职级");
        // 第4列
        List<String> head5 = new ArrayList<String>();
        head5.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head5.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head5.add("   3、用户可在后面追加、编辑自定义列。");
        selectMap.put(5, listSelect);
        head5.add("考核结果");
        // 第4列
        List<String> head6 = new ArrayList<String>();
        head6.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head6.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head6.add("   3、用户可在后面追加、编辑自定义列。");
        head6.add("自定义列1");
        // 第4列
        List<String> head7 = new ArrayList<String>();
        head7.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head7.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head7.add("   3、用户可在后面追加、编辑自定义列。");
        head7.add("自定义列2");
        // 第4列
        List<String> head8 = new ArrayList<String>();
        head8.add("注：1、【考核对象】根据绩效考核任务中的范围带出。");
        head8.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head8.add("   3、用户可在后面追加、编辑自定义列。");
        head8.add("自定义列3");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        list.add(head4);
        list.add(head5);
        list.add(head6);
        list.add(head7);
        list.add(head8);
        return list;
    }

    /**
     * 定义组织自定义导出模板 表头
     *
     * @return List
     */
    public static List<List<String>> headPerCustom(Map<Integer, List<String>> selectMap, List<PerformanceRankFactorDTO> performanceRankFactorDTOS, List<PerformanceAppraisalColumnsDTO> appraisalColumnsDTOList) {
        List<String> columnList = new ArrayList<>();
        if (StringUtils.isNotEmpty(appraisalColumnsDTOList)) {
            for (PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO : appraisalColumnsDTOList) {
                columnList.add(performanceAppraisalColumnsDTO.getColumnName());
            }
        }
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
        head0.add("员工工号");
        // 第2列
        List<String> head1 = new ArrayList<String>();
        head1.add("注：1、【员工工号】、【员工姓名】、【岗位】、【部门】、【个人职级】根据绩效考核任务中的范围带出。");
        head1.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head1.add("   3、用户可在后面追加、编辑自定义列。");
        head1.add("员工姓名");
        // 第3列
        List<String> head2 = new ArrayList<String>();
        head2.add("注：1、【员工工号】、【员工姓名】、【岗位】、【部门】、【个人职级】根据绩效考核任务中的范围带出。");
        head2.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head2.add("   3、用户可在后面追加、编辑自定义列。");
        head2.add("岗位");
        // 第4列
        List<String> head3 = new ArrayList<String>();
        head3.add("注：1、【员工工号】、【员工姓名】、【岗位】、【部门】、【个人职级】根据绩效考核任务中的范围带出。");
        head3.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head3.add("   3、用户可在后面追加、编辑自定义列。");
        head3.add("部门");
        // 第4列
        List<String> head4 = new ArrayList<String>();
        head4.add("注：1、【员工工号】、【员工姓名】、【岗位】、【部门】、【个人职级】根据绩效考核任务中的范围带出。");
        head4.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head4.add("   3、用户可在后面追加、编辑自定义列。");
        head4.add("个人职级");
        // 第4列
        List<String> head5 = new ArrayList<String>();
        head5.add("注：1、【员工工号】、【员工姓名】、【岗位】、【部门】、【个人职级】根据绩效考核任务中的范围带出。");
        head5.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
        head5.add("   3、用户可在后面追加、编辑自定义列。");
        selectMap.put(5, listSelect);
        head5.add("考核结果");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        list.add(head4);
        list.add(head5);
        for (String column : columnList) {
            List<String> head = new ArrayList<String>();
            head.add("注：1、【员工工号】、【员工姓名】、【岗位】、【部门】、【个人职级】根据绩效考核任务中的范围带出。");
            head.add("   2、【考核结果】为下拉选择项，枚举值根据绩效考核任务中所选的绩效等级带出，同时增加“不考核”选项。");
            head.add("   3、用户可在后面追加、编辑自定义列。");
            head.add(column);
            list.add(head);
        }
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
     * @param errorList                          错误列表
     * @param appraisalDTO                       绩效考核
     * @return Collection
     */
    public static Collection<List<Object>> dataTemplateList(List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList, List<List<Object>> errorList, PerformanceAppraisalDTO appraisalDTO) {
        if (StringUtils.isNull(appraisalDTO)) {
            throw new ServiceException("当前要导出的绩效任务不存在");
        }
        List<List<Object>> list = new ArrayList<>();
        if (StringUtils.isEmpty(errorList)) {
            for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
                List<Object> data = new ArrayList<Object>();
                if (appraisalDTO.getAppraisalObject() == 1) {
                    data.add(performanceAppraisalObjectsDTO.getAppraisalObjectName() + "（" + performanceAppraisalObjectsDTO.getAppraisalObjectCode() + "）");
                } else {
                    data.add(performanceAppraisalObjectsDTO.getAppraisalObjectCode());
                    data.add(performanceAppraisalObjectsDTO.getAppraisalObjectName());
                    data.add(performanceAppraisalObjectsDTO.getDepartmentName());
                    data.add(performanceAppraisalObjectsDTO.getPostName());
                    data.add(performanceAppraisalObjectsDTO.getOfficialRankName());
                }
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                list.add(data);
            }
            return list;
        } else {
            return errorList;
        }

    }

}


