package net.qixiaowei.operate.cloud.excel.targetManager;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 目标结果表
 *
 * @author TANGMICHI
 * @since 2022-11-07
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class TargetOutcomeExcel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelIgnore
    @ExcelProperty("ID")
    private Long targetOutcomeId;
    /**
     * 目标年度
     */
    @ExcelIgnore
    @ExcelProperty("目标年度")
    private Integer targetYear;
    /**
     * 删除标记:0未删除;1已删除
     */
    @ExcelIgnore
    @ExcelProperty("删除标记:0未删除;1已删除")
    private Integer deleteFlag;
    /**
     * 关键指标
     */
    @ExcelProperty(index = 0)
    private String indicatorName;
    /**
     * 实际值合计
     */
    @ExcelProperty("实际值合计")
    private BigDecimal actualTotal;
    /**
     * 目标值
     */
    @ExcelProperty("目标值")
    private BigDecimal targetValue;
    /**
     * 目标完成率（%）
     */
    @ExcelProperty("目标完成率（%）")
    private BigDecimal targetCompletionRate;
    /**
     * 一月实际值
     */
    @ExcelProperty("1月")
    private BigDecimal actualJanuary;
    /**
     * 二月实际值
     */
    @ExcelProperty("2月")
    private BigDecimal actualFebruary;
    /**
     * 三月实际值
     */
    @ExcelProperty("3月")
    private BigDecimal actualMarch;
    /**
     * 四月实际值
     */
    @ExcelProperty("4月")
    private BigDecimal actualApril;
    /**
     * 五月实际值
     */
    @ExcelProperty("5月")
    private BigDecimal actualMay;
    /**
     * 六月实际值
     */
    @ExcelProperty("6月")
    private BigDecimal actualJune;
    /**
     * 七月实际值
     */
    @ExcelProperty("7月")
    private BigDecimal actualJuly;
    /**
     * 八月实际值
     */
    @ExcelProperty("8月")
    private BigDecimal actualAugust;
    /**
     * 九月实际值
     */
    @ExcelProperty("9月")
    private BigDecimal actualSeptember;
    /**
     * 十月实际值
     */
    @ExcelProperty("10月")
    private BigDecimal actualOctober;
    /**
     * 十一月实际值
     */
    @ExcelProperty("11月")
    private BigDecimal actualNovember;
    /**
     * 十二月实际值
     */
    @ExcelProperty("12月")
    private BigDecimal actualDecember;
}

