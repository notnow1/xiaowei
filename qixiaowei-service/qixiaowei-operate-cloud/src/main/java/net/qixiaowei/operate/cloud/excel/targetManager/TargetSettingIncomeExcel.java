package net.qixiaowei.operate.cloud.excel.targetManager;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import lombok.Data;
import org.checkerframework.common.value.qual.StringVal;

import java.math.BigDecimal;

import static com.alibaba.excel.enums.BooleanEnum.TRUE;

/**
 * 目标制定收入表
 *
 * @author Graves
 * @since 2022-11-10
 */
@Data
@HeadRowHeight(16)
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT, wrapped = TRUE)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@HeadFontStyle(fontName = "微软雅黑", bold = TRUE, fontHeightInPoints = 11)
@ContentFontStyle(fontName = "微软雅黑")
@ContentRowHeight(15)
public class TargetSettingIncomeExcel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelIgnore
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "ID"})
    private Long targetSettingIncomeId;
    /**
     * 目标制定ID
     */
    @ExcelIgnore
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "目标制定ID"})
    private Long targetSettingId;
    /**
     * 一年前订单金额
     */
    @ExcelIgnore
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "一年前订单金额"})
    private BigDecimal moneyBeforeOne;
    /**
     * 两年前订单金额
     */
    @ExcelIgnore
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "两年前订单金额"})
    private BigDecimal moneyBeforeTwo;
    /**
     * 三年前订单金额
     */
    @ExcelIgnore
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "三年前订单金额"})
    private BigDecimal moneyBeforeThree;
    /**
     * 一年前订单转化率
     */
    @ExcelIgnore
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "一年前订单转化率"})
    private BigDecimal conversionBeforeOne;
    /**
     * 两年前订单转化率
     */
    @ExcelIgnore
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "两年前订单转化率"})
    private BigDecimal conversionBeforeTwo;
    /**
     * 三年前订单转化率
     */
    @ExcelIgnore
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "三年前订单转化率"})
    private BigDecimal conversionBeforeThree;
    /**
     * 删除标记:0未删除;1已删除
     */
    @ExcelIgnore
    @ColumnWidth(16)
    @ExcelProperty(value = {"币种：人民币     单位：万元", "删除标记:0未删除;1已删除"})
    private Integer deleteFlag;
    /**
     * 目标年度
     */
    @ExcelProperty(value = {"币种：人民币     单位：万元", "目标年度"})
    @ColumnWidth(16)
    private Integer targetYear;
    /**
     * 挑战值
     */
    @ExcelProperty(value = {"币种：人民币     单位：万元", "挑战值"})
    @ColumnWidth(16)
    @ContentStyle(dataFormat = 2)
    private BigDecimal challengeValue;
    /**
     * 目标值
     */
    @ExcelProperty(value = {"币种：人民币     单位：万元", "目标值"})
    @ColumnWidth(16)
    @ContentStyle(dataFormat = 2)
    private BigDecimal targetValue;
    /**
     * 保底值
     */
    @ExcelProperty(value = {"币种：人民币     单位：万元", "保底值"})
    @ColumnWidth(16)
    @ContentStyle(dataFormat = 2)
    private BigDecimal guaranteedValue;
    /**
     * 目标年度订单转化率（%）
     */
    @ExcelProperty(value = {"币种：人民币     单位：万元", "订单转化率（%）"})
    @ColumnWidth(16)
    @ContentStyle(dataFormat = 2)
    private BigDecimal percentage;
}

