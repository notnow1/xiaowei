package net.qixiaowei.operate.cloud.excel.salary;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
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

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 缓存的数据列表
     */
    List<Map<Integer, String>> list = new ArrayList<>();

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        String sheetName = context.readSheetHolder().getSheetName();
        dataMap.computeIfAbsent(sheetName, k -> new ArrayList<>());
        dataMap.get(sheetName).add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        logger.info("Excel解析完成");
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
}


