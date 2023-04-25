package net.qixiaowei.operate.cloud.excel.salary;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.salary.OfficialRankEmolumentDTO;

import net.qixiaowei.operate.cloud.service.salary.IOfficialRankEmolumentService;

/**
 * OfficialRankEmolumentImportListener
 *
 * @author Graves
 * @since 2022-11-30
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OfficialRankEmolumentImportListener extends AnalysisEventListener<OfficialRankEmolumentExcel> {
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

    /**
     * 下载模板赋值
     *
     * @param officialRankEmolumentDTOS 职级确定薪酬列表
     * @return 结果
     */
    public static Collection<?> dataList(List<OfficialRankEmolumentDTO> officialRankEmolumentDTOS) {
        List<List<Object>> list = new ArrayList<>();
        for (OfficialRankEmolumentDTO officialRankEmolumentDTO : officialRankEmolumentDTOS) {
            List<Object> data = new ArrayList<Object>();
            data.add(officialRankEmolumentDTO.getOfficialRankName());
            data.add("");
            data.add("");
            data.add("");
            list.add(data);
        }
        return list;
    }

    /**
     * 表头
     *
     * @param officialRankSystemName 职级体系名称
     * @param isError                是否导出错误
     * @return 结果
     */
    public static List<List<String>> getHead(String officialRankSystemName, boolean isError) {
        List<List<String>> list = new ArrayList<List<String>>();
        if (isError) {
            List<String> head0 = new ArrayList<String>();
            head0.add("币种：人民币   单位：元");
            head0.add("职级体系：" + officialRankSystemName);
            head0.add("错误数据");
            list.add(head0);
        }
        List<String> head1 = new ArrayList<String>();
        head1.add("币种：人民币   单位：元");
        head1.add("职级体系：" + officialRankSystemName);
        head1.add("职级");
        list.add(head1);
        List<String> head2 = new ArrayList<String>();
        head2.add("币种：人民币   单位：元");
        head2.add("职级体系：" + officialRankSystemName);
        head2.add("工资上限");
        list.add(head2);
        List<String> head3 = new ArrayList<String>();
        head3.add("币种：人民币   单位：元");
        head3.add("职级体系：" + officialRankSystemName);
        head3.add("工资下限");
        list.add(head3);
        List<String> head4 = new ArrayList<String>();
        head4.add("币种：人民币   单位：元");
        head4.add("职级体系：" + officialRankSystemName);
        head4.add("工资中位数");
        list.add(head4);
        return list;
    }

    @Override
    public void invoke(OfficialRankEmolumentExcel data, AnalysisContext context) {
        list.add(data);
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
            // 调用importer方法
//            officialRankEmolumentService.importOfficialRankEmolument(list, file);
            // 存储完成清理list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 调用importer方法
//        officialRankEmolumentService.importOfficialRankEmolument(list, file);
        // 存储完成清理list
        list.clear();
    }
}


