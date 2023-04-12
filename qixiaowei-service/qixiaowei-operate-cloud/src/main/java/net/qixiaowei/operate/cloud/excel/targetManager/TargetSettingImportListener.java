package net.qixiaowei.operate.cloud.excel.targetManager;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TargetSettingImportListener
 *
 * @author Graves
 * @since 2022-10-27
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TargetSettingImportListener<TargetSettingExcel> extends AnalysisEventListener<TargetSettingExcel> {
    /**
     * 默认每隔3000条存储数据库
     */
    private int batchCount = 3000;
    /**
     * 缓存的数据列表
     */
    private List<List<TargetSettingExcel>> data = new ArrayList<>();
    /**
     * 解析数据
     * key是sheetName，value是相应sheet的解析数据
     */
    private final Map<String, List<TargetSettingExcel>> dataMap = new HashMap<>();
    /**
     * 合并单元格
     * key键是sheetName，value是相应sheet的合并单元格数据
     */
    private final Map<String, List<CellExtra>> mergeMap = new HashMap<>();

    /**
     * 正文起始行
     */
    private final Integer headRowNumber;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void invoke(TargetSettingExcel data, AnalysisContext context) {
        String sheetName = context.readSheetHolder().getSheetName();
        dataMap.computeIfAbsent(sheetName, k -> new ArrayList<>());
        dataMap.get(sheetName).add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        logger.info("Excel解析完成");
    }

    /**
     * 获取解析数据
     */
    public Map<String, List<TargetSettingExcel>> getData(InputStream in, Class<TargetSettingExcel> clazz) {
        try {
            EasyExcel.read(in, clazz, this)
                    .extraRead(CellExtraTypeEnum.MERGE)
                    .headRowNumber(headRowNumber)
                    .doReadAll();
        } catch (Exception e) {
            logger.error("Excel读取异常");
        }
        return dataMap;
    }

    /**
     * 创建表头，可以创建复杂的表头
     *
     * @return2
     */
    public static List<List<String>> headRecovery() {
        List<List<String>> list = new ArrayList<List<String>>();
        // 目标年度
        List<String> head0 = new ArrayList<String>();
        head0.add("币种：人民币     单位：万元");
        head0.add("目标年度");
        // ArrayList<>()（应收账款周转天数）
        List<String> head1 = new ArrayList<String>();
        head1.add("币种：人民币     单位：万元");
        head1.add("DSO\n（应收账款周转天数）");
        head1.add("DSO\n（应收账款周转天数）");
        // 期末应收账款余额
        List<String> head2 = new ArrayList<String>();
        head2.add("币种：人民币     单位：万元");
        head2.add("期末应收账款余额");
        head2.add("挑战值");
        // 回款总目标
        List<String> head3 = new ArrayList<String>();
        head3.add("币种：人民币     单位：万元");
        head3.add("期末应收账款余额");
        head3.add("目标值");
        // 应回尽回
        List<String> head4 = new ArrayList<String>();
        head4.add("币种：人民币     单位：万元");
        head4.add("期末应收账款余额");
        head4.add("保底值");
        // 逾期清理
        List<String> head5 = new ArrayList<String>();
        head5.add("币种：人民币     单位：万元");
        head5.add("回款总目标");
        head5.add("挑战值");
        // 提前回款
        List<String> head6 = new ArrayList<String>();
        head6.add("币种：人民币     单位：万元");
        head6.add("回款总目标");
        head6.add("目标值");
        // 第二列
        List<String> head7 = new ArrayList<String>();
        head7.add("币种：人民币     单位：万元");
        head7.add("回款总目标");
        head7.add("保底值");
        // 第三列
        List<String> head8 = new ArrayList<String>();
        head8.add("币种：人民币     单位：万元");
        head8.add("1.应回尽回");
        head8.add("挑战值");
        // 第四列
        List<String> head9 = new ArrayList<String>();
        head9.add("币种：人民币     单位：万元");
        head9.add("1.应回尽回");
        head9.add("目标值");
        // 第五列
        List<String> head10 = new ArrayList<String>();
        head10.add("币种：人民币     单位：万元");
        head10.add("1.应回尽回");
        head10.add("保底值");
        // 第六列
        List<String> head11 = new ArrayList<String>();
        head11.add("币种：人民币     单位：万元");
        head11.add("2.逾期清理");
        head11.add("挑战值");
        // 第七列
        List<String> head12 = new ArrayList<String>();
        head12.add("币种：人民币     单位：万元");
        head12.add("2.逾期清理");
        head12.add("目标值");
        // 第七列
        List<String> head13 = new ArrayList<String>();
        head13.add("币种：人民币     单位：万元");
        head13.add("2.逾期清理");
        head13.add("保底值");
        // 第七列
        List<String> head14 = new ArrayList<String>();
        head14.add("币种：人民币     单位：万元");
        head14.add("3.提前回款");
        head14.add("挑战值");
        // 第七列
        List<String> head15 = new ArrayList<String>();
        head15.add("币种：人民币     单位：万元");
        head15.add("3.提前回款");
        head15.add("目标值");
        // 第七列
        List<String> head16 = new ArrayList<String>();
        head16.add("币种：人民币     单位：万元");
        head16.add("3.提前回款");
        head16.add("保底值");
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
        list.add(head11);
        list.add(head12);
        list.add(head13);
        list.add(head14);
        list.add(head15);
        list.add(head16);
        return list;
    }

    /**
     * excel数据
     *
     * @param targetSettingRecoveriesExcels excel
     * @return 结果
     */
    public static List<List<Object>> dataList(List<TargetSettingRecoveriesExcel> targetSettingRecoveriesExcels) {
        List<List<Object>> list = new ArrayList<>();
        for (TargetSettingRecoveriesExcel targetSettingRecoveriesExcel : targetSettingRecoveriesExcels) {
            List<Object> data = new ArrayList<Object>();
            // 1.目标年度                   data.add(targetYear);
            // 2.DSO（应收账款周转天数）      data.add(DSO);
            // 3.挑战值				       LinkedHashMap.for  →  data.add()
            //   目标值				       LinkedHashMap.for  →  data.add()
            //   保底值				       LinkedHashMap.for  →  data.add()
            // list.add(data);
            data.add(targetSettingRecoveriesExcel.getTargetYear());
            // DSO
            data.add(targetSettingRecoveriesExcel.getDSO());
            // 挑战值
            Map<String, BigDecimal> targetMap = targetSettingRecoveriesExcel.getTargetMap();
            Map<String, BigDecimal> challengeMap = targetSettingRecoveriesExcel.getChallengeMap();
            Map<String, BigDecimal> guaranteedMap = targetSettingRecoveriesExcel.getGuaranteedMap();

            data.add(challengeMap.get("期末应收账款余额").setScale(2, RoundingMode.HALF_UP).toString());
            data.add(targetMap.get("期末应收账款余额").setScale(2, RoundingMode.HALF_UP).toString());
            data.add(guaranteedMap.get("期末应收账款余额").setScale(2, RoundingMode.HALF_UP).toString());

            data.add(challengeMap.get("回款总目标").setScale(2, RoundingMode.HALF_UP).toString());
            data.add(targetMap.get("回款总目标").setScale(2, RoundingMode.HALF_UP).toString());
            data.add(guaranteedMap.get("回款总目标").setScale(2, RoundingMode.HALF_UP).toString());

            data.add(challengeMap.get("1.应回尽回").setScale(2, RoundingMode.HALF_UP).toString());
            data.add(targetMap.get("1.应回尽回").setScale(2, RoundingMode.HALF_UP).toString());
            data.add(guaranteedMap.get("1.应回尽回").setScale(2, RoundingMode.HALF_UP).toString());

            data.add(challengeMap.get("2.逾期清理").setScale(2, RoundingMode.HALF_UP).toString());
            data.add(targetMap.get("2.逾期清理").setScale(2, RoundingMode.HALF_UP).toString());
            data.add(guaranteedMap.get("2.逾期清理").setScale(2, RoundingMode.HALF_UP).toString());

            data.add(challengeMap.get("3.提前回款").setScale(2, RoundingMode.HALF_UP).toString());
            data.add(targetMap.get("3.提前回款").setScale(2, RoundingMode.HALF_UP).toString());
            data.add(guaranteedMap.get("3.提前回款").setScale(2, RoundingMode.HALF_UP).toString());

            list.add(data);
        }
        return list;
    }
}


