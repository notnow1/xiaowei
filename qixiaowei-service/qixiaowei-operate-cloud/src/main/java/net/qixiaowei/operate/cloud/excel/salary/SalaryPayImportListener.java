package net.qixiaowei.operate.cloud.excel.salary;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDetailsDTO;
import net.qixiaowei.operate.cloud.api.vo.salary.SalaryPayImportTempDataVO;
import net.qixiaowei.operate.cloud.service.salary.ISalaryItemService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SalaryPayImportListener
 *
 * @author Graves
 * @since 2022-11-17
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SalaryPayImportListener extends AnalysisEventListener<Map<Integer, String>> {
    /**
     * 解析数据
     * key是sheetName，value是相应sheet的解析数据
     */
    private final Map<String, List<Map<Integer, String>>> dataMap = new HashMap<>();
    /**
     * 默认每隔3000条存储数据库
     */
    private int batchCount = 3000;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 工资项导入临时数据VO
     */
    private SalaryPayImportTempDataVO salaryPayImportTempDataVO;
    /**
     * 缓存的数据列表
     */
    List<Map<Integer, String>> list = new ArrayList<>();
    /**
     * 工资发薪服务
     */
    private final ISalaryPayService salaryPayService;
    /**
     * 工资项服务
     */
    private final ISalaryItemService salaryItemService;

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        List<SalaryItemDTO> salaryItemDTOS = salaryItemService.selectSalaryItemList(new SalaryItemDTO());
        if (StringUtils.isEmpty(salaryItemDTOS)) {
            throw new ServiceException("当前工资项未进行任何配置，请联系管理员");
        }
        if (headMap.size() > (salaryItemDTOS.size() + 3)) {
            throw new ServiceException("当前系统配置的薪酬类别与导入的薪酬类别不匹配，请检查.");
        }
        Map<String, SalaryItemDTO> salaryItemOfThirdLevelItemMap = new HashMap<>();
        Map<Long, SalaryItemDTO> salaryItemMap = new HashMap<>();
        Set<String> salaryItemSet = new HashSet<>();
        //目前导入的工资项未与配置项目作比较，如果做比较，导入的项目如果比配置的项目多，看后续产品意思要不要报错或者略过不处理
        for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
            String thirdLevelItem = salaryItemDTO.getThirdLevelItem();
            Long salaryItemId = salaryItemDTO.getSalaryItemId();
            salaryItemOfThirdLevelItemMap.put(thirdLevelItem, salaryItemDTO);
            salaryItemMap.put(salaryItemId, salaryItemDTO);
            salaryItemSet.add(thirdLevelItem);
        }
        List<String> errorThirdLevelItemOfImport = new ArrayList<>();
        for (int i = 3; i < headMap.size(); i++) {
            String thirdLevelItemOfImport = headMap.get(i);
            if (!salaryItemSet.contains(thirdLevelItemOfImport)) {
                errorThirdLevelItemOfImport.add(thirdLevelItemOfImport);
            }
        }
        if (StringUtils.isNotEmpty(errorThirdLevelItemOfImport)) {
            throw new ServiceException("当前系统配置的薪酬类别与导入的薪酬类别不匹配，请确认是否是最新的导入模版，有问题的列为:" + CollUtil.join(errorThirdLevelItemOfImport, StrUtil.COMMA));
        }
        salaryPayImportTempDataVO = new SalaryPayImportTempDataVO();
        salaryPayImportTempDataVO.setSalaryItemOfThirdLevelItemMap(salaryItemOfThirdLevelItemMap);
        salaryPayImportTempDataVO.setSalaryItemMap(salaryItemMap);
        salaryPayImportTempDataVO.setHeadMap(headMap);
    }

    @Override
    public void invoke(Map<Integer, String> map, AnalysisContext context) {
        list.add(map);
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
            // 调用importer方法
            salaryPayService.importSalaryPay(salaryPayImportTempDataVO, list);
            // 存储完成清理list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        logger.info("Excel解析完成");
        salaryPayService.importSalaryPay(salaryPayImportTempDataVO, list);
        // 存储完成清理list
        list.clear();
    }

    /**
     * 创建导入模板表头，可以创建复杂的表头
     *
     * @return
     */
    public static List<List<String>> headTemplate(List<SalaryItemDTO> salaryItemDTOS) {
        List<List<String>> list = new ArrayList<List<String>>();
        // 第一列
        List<String> head0 = new ArrayList<String>();
        head0.add("员工工号");
        head0.add("文本字段，填充员工工号");
        // 第二列
        List<String> head1 = new ArrayList<String>();
        head1.add("发薪年月");
        head1.add("格式：YYYY/MM");
        // 第一列
        List<String> head2 = new ArrayList<String>();
        head2.add("员工姓名");
        head2.add("文本字段，填充员工姓名");
        list.add(head0);
        list.add(head2);
        list.add(head1);
        for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
            list.add(new ArrayList<String>() {{
                add(salaryItemDTO.getThirdLevelItem());
                add(" 展示所有生效的工资条配置，若为0则可以不填写");
            }});
        }
        return list;
    }

    /**
     * 封装导出数据
     *
     * @param salaryPayDTOS 公子发薪表
     * @return List
     */
    public static List<List<Object>> dataMonthList(List<SalaryPayDTO> salaryPayDTOS) {
        List<List<Object>> list = new ArrayList<>();
        if (StringUtils.isEmpty(salaryPayDTOS)) {
            throw new ServiceException("工资发薪列表为空");
        }
        for (SalaryPayDTO salaryPayDTO : salaryPayDTOS) {
            List<SalaryPayDetailsDTO> salaryPayDetailsDTOList = salaryPayDTO.getSalaryPayDetailsDTOList();
            for (SalaryPayDetailsDTO salaryPayDetailsDTO : salaryPayDetailsDTOList) {
                if (StringUtils.isNotNull(salaryPayDetailsDTO.getAmount()) && salaryPayDetailsDTO.getAmount().compareTo(BigDecimal.ZERO) != 0) {
                    salaryPayDetailsDTO.setIsCondition(1);
                } else {
                    salaryPayDetailsDTO.setIsCondition(0);
                }
            }
        }
        for (SalaryPayDTO salaryPayDTO : salaryPayDTOS) {
            List<SalaryPayDetailsDTO> salaryPayDetailsDTOList = salaryPayDTO.getSalaryPayDetailsDTOList();
            //三级表头
            List<Object> data2 = new ArrayList<Object>();
            data2.add("工号");
            data2.add("姓名");
            data2.add("部门");
            data2.add("年度");
            data2.add("月份");
            for (SalaryPayDetailsDTO salaryPayDetailsDTO : salaryPayDetailsDTOList) {
                if (salaryPayDetailsDTO.getIsCondition() == 1) {
                    data2.add(salaryPayDetailsDTO.getThirdLevelItem());
                }
            }
            list.add(data2);
            //数据内容
            List<Object> data3 = new ArrayList<Object>();
            data3.add(salaryPayDTO.getEmployeeCode());
            data3.add(salaryPayDTO.getEmployeeName());
            data3.add(salaryPayDTO.getEmployeeDepartmentName());
            data3.add(salaryPayDTO.getPayYear());
            data3.add(salaryPayDTO.getPayMonth());
            for (SalaryPayDetailsDTO salaryPayDetailsDTO : salaryPayDetailsDTOList) {
                if (salaryPayDetailsDTO.getIsCondition() == 1) {
                    data3.add(salaryPayDetailsDTO.getAmount());
                }
            }
            list.add(data3);
            List<Object> data0 = new ArrayList<Object>();
            list.add(data0);
        }
        return list;
    }

    /**
     * 月度工资数据导出
     *
     * @param salaryPayDTOS  工资数据
     * @param salaryItemDTOS 项目列表
     * @return 结果
     */
    public static Collection<?> dataList(List<SalaryPayDTO> salaryPayDTOS, List<SalaryItemDTO> salaryItemDTOS) {
        List<List<Object>> list = new ArrayList<>();
        List<Object> data;
        for (SalaryPayDTO salaryPayDTO : salaryPayDTOS) {
            data = new ArrayList<>();
            data.add(salaryPayDTO.getEmployeeCode());
            data.add(salaryPayDTO.getEmployeeName());
            data.add(salaryPayDTO.getEmployeeDepartmentName());
            data.add(salaryPayDTO.getTopLevelDepartmentName());
            data.add(salaryPayDTO.getPayYearMonth());
            List<SalaryPayDetailsDTO> salaryPayDetailsDTOList = salaryPayDTO.getSalaryPayDetailsDTOList();
            if (StringUtils.isNotEmpty(salaryPayDetailsDTOList)) {
                for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
                    List<Long> salaryItemIds = salaryPayDetailsDTOList.stream().map(SalaryPayDetailsDTO::getSalaryItemId).filter(Objects::nonNull).collect(Collectors.toList());
                    if (salaryItemIds.contains(salaryItemDTO.getSalaryItemId())) {
                        SalaryPayDetailsDTO salaryPayDetailsDTO = salaryPayDetailsDTOList.stream().filter(s -> s.getSalaryItemId().equals(salaryItemDTO.getSalaryItemId())).collect(Collectors.toList()).get(0);
                        data.add(salaryPayDetailsDTO.getAmount());
                    } else {
                        data.add("0");
                    }
                }
                list.add(data);
            }
        }
        return list;
    }

    /**
     * 表头
     *
     * @param salaryItemDTOS 项目dots
     * @return 结果
     */
    public static List<List<String>> head(List<SalaryItemDTO> salaryItemDTOS) {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = new ArrayList<String>();
        head0.add("员工工号");
        list.add(head0);
        List<String> head1 = new ArrayList<String>();
        head1.add("员工姓名");
        list.add(head1);
        List<String> head2 = new ArrayList<String>();
        head2.add("所属一级部门");
        list.add(head2);
        List<String> head3 = new ArrayList<String>();
        head3.add("最小部门");
        list.add(head3);
        List<String> head4 = new ArrayList<String>();
        head4.add("发薪年月");
        list.add(head4);
        if (StringUtils.isNotEmpty(salaryItemDTOS)) {
            for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
                List<String> head = new ArrayList<String>();
                head.add(salaryItemDTO.getThirdLevelItem());
                list.add(head);
            }
        }
        return list;
    }

}


