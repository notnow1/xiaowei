package net.qixiaowei.system.manage.excel.basic;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import net.qixiaowei.system.manage.service.basic.IEmployeeService;

import java.util.List;
import java.util.Map;

public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {

    private static final int BATCH_COUNT = 1000;
    private List<Map<Integer, String>> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    /**
     * 用户service
     */
    private  IEmployeeService employeeService;



    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        String s = JSON.toJSONString(data);

        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        // 调用importer方法

        // 存储完成清理list
        cachedDataList.clear();
    }
}
