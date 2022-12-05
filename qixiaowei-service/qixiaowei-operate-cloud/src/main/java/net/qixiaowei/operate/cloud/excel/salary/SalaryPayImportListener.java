package net.qixiaowei.operate.cloud.excel.salary;

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
import net.qixiaowei.operate.cloud.service.salary.ISalaryItemService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryPayDetailsService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 缓存的数据列表
     */
    List<Map<Integer, String>> list = new ArrayList<>();
    /**
     * 工资发薪服务
     */
    private final ISalaryPayService salaryPayService;

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        list.add(headMap);
    }

    @Override
    public void invoke(Map<Integer, String> map, AnalysisContext context) {
        list.add(map);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        logger.info("Excel解析完成");
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
            // 调用importer方法
            salaryPayService.importSalaryPay(list);
            // 存储完成清理list
            list.clear();
        }
        //防止重复执行
        if (StringUtils.isNotEmpty(list)) {
            // 调用importer方法
            salaryPayService.importSalaryPay(list);
            // 存储完成清理list
            list.clear();
        }
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
        list.add(head0);
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
     * @param salaryPayDTOS           公子发薪表
     * @param salaryItemService       工资项服务
     * @param salaryPayDetailsService 详情服务
     * @return List
     */
    public static List<List<Object>> dataList(List<SalaryPayDTO> salaryPayDTOS, ISalaryPayDetailsService salaryPayDetailsService, ISalaryItemService salaryItemService) {
        if (StringUtils.isEmpty(salaryPayDTOS)) {
            throw new ServiceException("工资发薪列表为空");
        }
        List<Long> salaryPayIds = new ArrayList<>();
        for (SalaryPayDTO salaryPayDTO : salaryPayDTOS) {
            salaryPayIds.add(salaryPayDTO.getSalaryPayId());
        }
        List<SalaryPayDetailsDTO> salaryPayDetailsDTOS = salaryPayDetailsService.selectSalaryPayDetailsBySalaryPayIds(salaryPayIds);



        return null;
    }
}


